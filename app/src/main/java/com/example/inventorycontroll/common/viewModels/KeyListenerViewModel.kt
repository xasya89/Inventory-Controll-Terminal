package com.example.inventorycontroll.common.viewModels

import android.util.Log
import android.view.KeyEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.inventorycontroll.common.shopService.ShopService
import com.example.inventorycontroll.inventoryDatabase.dao.BarcodeDao
import com.example.inventorycontroll.inventoryDatabase.entities.Good
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeyListenerViewModel @Inject constructor(private val barcodeDao: BarcodeDao, private val shopService: ShopService): ViewModel() {
    val barcode = MutableLiveData<String>("")
    private var _barcode:String = ""

    val findGood = MutableLiveData<Good?>(null)

    private var debounceJob: Job? = null
    private var isScanActive = false
    fun appendKey(key: KeyEvent?){
        if (key?.action==KeyEvent.ACTION_DOWN){
            val newChar = key.keyCode.toString()
            if(newChar=="188") isScanActive = true
            if(!isScanActive) return

            debounceJob?.cancel()
            if(newChar!="188")
                _barcode = _barcode + newChar.replace("13", "")
            debounceJob = viewModelScope.launch {
                delay(300L)
                viewModelScope.launch (Dispatchers.IO){
                    Log.d("barcode",_barcode)
                    if(_barcode=="") return@launch
                    val good = barcodeDao.getGoodByBarcode(shopService.selectShop!!.dbName, _barcode).firstOrNull()
                    findGood.postValue(good)
                    barcode.postValue(_barcode)
                    _barcode=""
                    isScanActive = false
                }
            }
        }
    }

    fun findGood(barcode: String){
        viewModelScope.launch (Dispatchers.IO + getCoroutineExceptionHandler()){
            val selectDbName = shopService.selectShop!!.dbName
            val good = barcodeDao.getGoodByBarcode(selectDbName, barcode).firstOrNull()
                ?: return@launch
            findGood.postValue(good)
        }
    }

    private fun getCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, throwable ->
            Log.e(this.toString(), throwable.message.toString())
        }
    }
}