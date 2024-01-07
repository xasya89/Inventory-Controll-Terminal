package com.example.inventorycontroll.common

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebounceAction(private val delayMs: Long, private val param: () -> Unit) {
    private var debounceJob: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun offer() {
        debounceJob?.cancel()
        debounceJob = GlobalScope.launch {
            delay(delayMs)
            valueChanged()
        }
    }

    private fun valueChanged() {
        MainScope().launch {
            param()
        }
    }
}