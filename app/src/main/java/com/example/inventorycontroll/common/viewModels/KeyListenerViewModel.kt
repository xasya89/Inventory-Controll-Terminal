package com.example.inventorycontroll.common.viewModels

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeyListenerViewModel @Inject constructor(private val barcodeDao: BarcodeDao, private val shopService: ShopService): ViewModel() {
    val barcode = MutableLiveData<String>("")
    private var _barcode:String = ""

    val findGood = MutableLiveData<Good?>(null)

    fun appendKey(key: KeyEvent?){
        if (key?.action==KeyEvent.ACTION_DOWN){
            when (key?.keyCode){
                KeyEvent.KEYCODE_0 -> _barcode = _barcode + "0"
                KeyEvent.KEYCODE_1 -> _barcode = _barcode + "1"
                KeyEvent.KEYCODE_2 -> _barcode = _barcode + "2"
                KeyEvent.KEYCODE_3 -> _barcode = _barcode + "3"
                KeyEvent.KEYCODE_4 -> _barcode = _barcode + "4"
                KeyEvent.KEYCODE_5 -> _barcode = _barcode + "5"
                KeyEvent.KEYCODE_6 -> _barcode = _barcode + "6"
                KeyEvent.KEYCODE_7 -> _barcode = _barcode + "7"
                KeyEvent.KEYCODE_8 -> _barcode = _barcode + "8"
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
            }
        }
    }
}