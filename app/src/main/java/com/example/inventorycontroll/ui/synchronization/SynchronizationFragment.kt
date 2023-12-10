package com.example.inventorycontroll.ui.synchronization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.inventorycontroll.R
import com.example.inventorycontroll.databinding.FragmentSynchronizationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SynchronizationFragment : Fragment() {

    private val vm by viewModels<SynchronizationViewModel>()
    private lateinit var binding: FragmentSynchronizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSynchronizationBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SynchronizationFragment()
    }
}