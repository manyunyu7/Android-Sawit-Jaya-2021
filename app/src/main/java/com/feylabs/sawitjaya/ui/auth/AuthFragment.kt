package com.feylabs.sawitjaya.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.FragmentAuthFragmentBinding
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class AuthFragment : Fragment() {


    var _binding: FragmentAuthFragmentBinding? = null
    private val binding get() = _binding as FragmentAuthFragmentBinding
//    lateinit var authViewModel: AuthViewModel

    val authViewModel : AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_auth_fragment, container, false)
        _binding = FragmentAuthFragmentBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        authViewModel = ViewModelProvider(
//            requireActivity(),
//            ServiceLocator.provideFactory(requireContext())
//        ).get(AuthViewModel::class.java)



        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_loginFragment)
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_registerFragment)
        }

    }


}