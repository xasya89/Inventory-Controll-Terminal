package com.example.inventorycontroll.ui.synchronization

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBining()
    }

    private fun initBining() = with(binding){
        startSynchronizationBtn.setOnClickListener {
            startSynchronizationAnimation.visibility = View.VISIBLE
            vm.synchronization()
        }
        vm.compliteSynchronization.observe(viewLifecycleOwner,{
            startSynchronizationAnimation.visibility = View.GONE
            Toast.makeText(context, "Снихронизация завершена", Toast.LENGTH_LONG).show()
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SynchronizationFragment()
    }
}