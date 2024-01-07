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
            /*
            Log.d("barcode", key.keyCode.toString() + " " + KeyEvent.KEYCODE_8.toString() + " " + KeyEvent.KEYCODE_NUMPAD_8.toString())
            when (key?.keyCode){
                KeyEvent.KEYCODE_0 -> _barcode = _barcode + "0"
                KeyEvent.KEYCODE_NUMPAD_0 -> _barcode = _barcode + "0"
                KeyEvent.KEYCODE_1 -> _barcode = _barcode + "1"
                KeyEvent.KEYCODE_2 -> _barcode = _barcode + "2"
                KeyEvent.KEYCODE_3 -> _barcode = _barcode + "3"
                KeyEvent.KEYCODE_4 -> _barcode = _barcode + "4"
                KeyEvent.KEYCODE_5 -> _barcode = _barcode + "5"
                KeyEvent.KEYCODE_6 -> _barcode = _barcode + "6"
                KeyEvent.KEYCODE_7 -> _barcode = _barcode + "7"
                KeyEvent.KEYCODE_8 -> _barcode = _barcode + "8"
                KeyEvent.KEYCODE_NUMPAD_8 -> _barcode = _barcode + "8"
                KeyEvent.KEYCODE_9 -> _barcode = _barcode + "9"
                KeyEvent.KEYCODE_N -> _barcode = _barcode + "N"
                KeyEvent.KEYCODE_E -> _barcode = _barcode + "E"
                KeyEvent.KEYCODE_ENTER -> {
                    barcode.value = _barcode
                    viewModelScope.launch (Dispatchers.IO){
                        if(barcode.value=="") return@launch
                        val good = barcodeDao.getGoodByBarcode(shopService.getSelectShop().dbName, barcode.value!!).firstOrNull()
                        findGood.postValue(good)
                    }
                    _barcode = ""
                }
            }*/
        }
    }
}