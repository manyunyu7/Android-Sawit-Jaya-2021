package com.feylabs.sawitjaya.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.FragmentUserHomeBinding

class UserHomeFragment : Fragment() {


    var _binding : FragmentUserHomeBinding? = null
    val binding get() = _binding as FragmentUserHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_home, container, false)
        _binding = FragmentUserHomeBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_userHomeFragment_to_settingsFragment)
        }



    }

}