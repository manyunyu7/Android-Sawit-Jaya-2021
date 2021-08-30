package com.feylabs.sawitjaya.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.base.BaseFragment
import com.feylabs.sawitjaya.data.remote.response.LoginResponse
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.databinding.FragmentLoginBinding
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.feylabs.sawitjaya.service.Resource
import com.feylabs.sawitjaya.ui.staff.StaffMainMenu
import timber.log.Timber


class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding as FragmentLoginBinding

    lateinit var authViewModel: AuthViewModel


    lateinit var observerLogin: Observer<Resource<LoginResponse?>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        _binding = FragmentLoginBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLoginObserver()
        authViewModel = ViewModelProvider(
            requireActivity(),
            ServiceLocator.provideFactory(requireContext())
        ).get(AuthViewModel::class.java)

        binding.labelBack.setOnClickListener {
            findNavController().navigate(R.id.authFragment)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.authFragment)
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            authViewModel.login(username, password).observe(viewLifecycleOwner, observerLogin)
        }

    }

    private fun setLoginObserver() {
        observerLogin = Observer {
            when (it) {
                is Resource.Loading -> {
                    Timber.d("login loading")
//                    binding.loginProgressBar.show()
                }
                is Resource.Success -> {
                    Timber.d("login success")

                    //Save user token
//                    PreferenceApp.setToken(this, it.data.toString())
                    Timber.d("api_token from db= ${it.data.toString()}")
                    Timber.d("api_token from pref = ${it.data.toString()}")


//                    Fetching User Profile
//                    fetchUserProfile()

//                    binding.loginProgressBar.visibility = View.GONE
                    showToast("Login Berhasil")
                    proceedLogin()
                }
                is Resource.Error -> {
//                    swLoadingDialog.cancel()
                    Timber.d("login gagal")
//                    binding.loginProgressBar.hide()
                    showToast("Login Gagal")
                }
            }
        }
    }


    private fun proceedLogin() {
        requireActivity().finish()
        startActivity(Intent(requireContext(), StaffMainMenu::class.java))
    }

}