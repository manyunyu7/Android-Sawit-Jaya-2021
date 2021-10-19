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
import com.feylabs.sawitjaya.ui.base.BaseFragment
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.remote.response.LoginResponse
import com.feylabs.sawitjaya.data.remote.response.User
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.databinding.FragmentLoginBinding
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.feylabs.sawitjaya.data.remote.service.Resource
import com.feylabs.sawitjaya.ui.MainMenuContainerActivity
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding as FragmentLoginBinding

    val authViewModel: AuthViewModel by viewModel()


    lateinit var observerLogin: Observer<Resource<LoginResponse?>>
    override fun initUI() {
    }

    override fun initObserver() {
    }

    override fun initAction() {
        binding.btnResetForm.setOnClickListener {
            binding.etUsername.text.clear()
            binding.etPassword.text.clear()
        }
    }

    override fun initData() {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        _binding = FragmentLoginBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLoginObserver()

        binding.labelBack.setOnClickListener {
            findNavController().navigate(R.id.authFragment)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.authFragment)
        }

        binding.btnLogin.setOnClickListener {
            MyPreference(requireContext()).clearPreferences()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            viewGone(binding.btnLogin)
            viewVisible(binding.loginLoading)
            authViewModel.login(username, password).observe(viewLifecycleOwner, observerLogin)
        }


    }

    private fun setLoginObserver() {
        observerLogin = Observer {
            when (it) {
                is Resource.Loading -> {
                    Timber.d("login loading")
                }
                is Resource.Success -> {
                    MyPreference(requireContext()).clearPreferences()
                    Timber.d("login success")

                    //Save user token
                    //PreferenceApp.setToken(this, it.data.toString())
                    Timber.d("api_token from db= ${it.data.toString()}")
                    Timber.d("api_token from pref = ${it.data.toString()}")

                    showToast("Login Berhasil")
                    proceedLogin(it.data?.user, it.data?.access_token.toString())
                }

                is Resource.Error -> {
                    Timber.d("login gagal")
                    showToast("Login Gagal")
                    viewGone(binding.loginLoading)
                    viewVisible(binding.btnLogin)
                }
            }
        }
    }

    private fun isLoginValid() {

    }

    private fun checkToken() {

    }


    private fun proceedLogin(user: User?, token: String) {
        requireActivity().finish()
        Timber.d("user data --login ${user.toString()}")

        user?.let {
            val entity = AuthEntity(
                user_id = user.id,
                name = user.name,
                email = user.email,
                contact = user.contact,
                photo = user.photo_path,
                role = user.role,
                status = user.status,
                photo_base64 = ""
            )
            authViewModel.saveAuthInfo(entity)
        }

        MyPreference(requireContext()).save("ROLE", user?.role.toString())
        MyPreference(requireContext()).saveUserID(user?.id.toString())
        MyPreference(requireContext()).saveTokenWithTemplate("$token")
        MyPreference(requireContext()).save("TOKEN_RAW", "$token")

        MyPreference(requireContext()).saveUserEmail(user?.email.toString())
        MyPreference(requireContext()).saveUserPassword(binding.etPassword.text.toString())

        Timber.d("Tokenn Saved : Bearer $token")
        startActivity(Intent(requireContext(), MainMenuContainerActivity::class.java))
    }

}