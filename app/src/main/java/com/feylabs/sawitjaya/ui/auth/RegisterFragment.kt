package com.feylabs.sawitjaya.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.utils.base.BaseFragment
import com.feylabs.sawitjaya.data.remote.request.RegisterRequestBody
import com.feylabs.sawitjaya.databinding.FragmentRegisterBinding
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.feylabs.sawitjaya.utils.service.Resource
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel


class RegisterFragment : BaseFragment() {


    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding as FragmentRegisterBinding

    lateinit var registerObserver: Observer<Resource<String?>>

    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        _binding = FragmentRegisterBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObserver()

        authViewModel = ViewModelProvider(
            requireActivity(),
            ServiceLocator.provideFactory(requireContext())
        ).get(AuthViewModel::class.java)


        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.authFragment)
        }

        binding.btnRegister.setOnClickListener {

            binding.apply {
                val email = etEmail.text.toString()
                val contact = etContact.text.toString()
                val name = etFullname.text.toString()
                val password = etPassword.text.toString()

                authViewModel.register(
                    RegisterRequestBody(
                        userContact = contact,
                        userEmail = email,
                        userName = name,
                        userPassword = password,
                        userRole = "3"
                    )
                ).observe(viewLifecycleOwner, registerObserver)

            }

        }

    }

    private fun setUpObserver() {
        registerObserver = Observer {
            when(it){
                is Resource.Success->{
                    showToast("Registrasi Berhasil, SIlakan Login Menggunakan Email dan password anda")
                    findNavController().navigate(R.id.authFragment)
                }
                is Resource.Error->{
                    showToast("Registrasi Gagal, SIlakan coba kembali nanti")
                }
                is Resource.Loading->{

                }
            }
        }
    }


}