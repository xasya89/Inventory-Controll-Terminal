package com.example.inventorycontroll.common

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Debounce(private val delayMs: Long, private val param: (String) -> Unit) {
    private var debounceJob: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun offer(value: String?) {
        debounceJob?.cancel()
        debounceJob = GlobalScope.launch {
            delay(delayMs)
            valueChanged(value)
        }
    }

    private fun valueChanged(value: String?) {
        if(value!=null && value.isNotEmpty())
            param(value)
    }
}