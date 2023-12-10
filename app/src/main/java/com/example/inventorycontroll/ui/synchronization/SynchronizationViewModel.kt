package com.example.inventorycontroll.ui.synchronization

import androidx.lifecycle.ViewModel
import com.example.inventorycontroll.inventoryDatabase.dao.GoodDao
import com.example.inventorycontroll.inventoryDatabase.repositories.GoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SynchronizationViewModel @Inject constructor(
    private val dao: GoodDao
): ViewModel() {

}