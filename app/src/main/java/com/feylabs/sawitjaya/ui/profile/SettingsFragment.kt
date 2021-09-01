package com.feylabs.sawitjaya.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.base.BaseFragment
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.remote.response.ChangePasswordResponse
import com.feylabs.sawitjaya.data.remote.response.User
import com.feylabs.sawitjaya.data.remote.response.UserUpdateProfileResponse
import com.feylabs.sawitjaya.databinding.SettingsFragmentBinding
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.feylabs.sawitjaya.service.Resource
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import timber.log.Timber

class SettingsFragment : BaseFragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onResume() {
        super.onResume()

    }

    var _binding: SettingsFragmentBinding? = null
    val binding get() = _binding as SettingsFragmentBinding

    private lateinit var viewModel: SettingsViewModel
    private lateinit var authViewModel: AuthViewModel

    lateinit var localProfileObserver: Observer<AuthEntity>
    lateinit var remoteUpdateProfileObserver: Observer<Resource<UserUpdateProfileResponse?>>
    lateinit var remoteChangePasswordeObserver: Observer<Resource<ChangePasswordResponse?>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.settings_fragment, container, false)
        _binding = SettingsFragmentBinding.bind(view)

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ServiceLocator.provideFactory(requireContext())
        viewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.userHomeFragment)
        }

        binding.btnChangePhoto.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_changeProfilePictureFragment)
        }

        binding.btnSavePassword.setOnClickListener {
            val old_password = binding.etPasswordOld.text.toString()
            val new_password = binding.etPasswordNew.text.toString()

            authViewModel.changePassword(old_password, new_password)
                .observe(viewLifecycleOwner, remoteChangePasswordeObserver)
        }

        /**
         * Save General Profile
         */
        binding.btnSaveChanges.setOnClickListener {
            val name = binding.etNama.text.toString()
            val contact = binding.etContact.text.toString()
            val email = binding.etEmail.text.toString()
            viewModel.updateProfile(name, email, contact, "3")
                .observe(viewLifecycleOwner, remoteUpdateProfileObserver)
        }

        setObserver()

        viewModel.getProfileLocally()
        viewModel.localProfileLD.observe(viewLifecycleOwner, localProfileObserver)
    }

    private fun setObserver() {
        localProfileObserver = Observer {
            binding.apply {
                tvNama.text = it.name
                etContact.setText(it.contact)
                etEmail.setText(it.email)
                etNama.setText(it.name)
                Glide
                    .with(requireContext())
                    .load(it.photo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.ivProfilePicture)
            }
        }

        remoteChangePasswordeObserver = Observer {
            when (it) {
                is Resource.Success -> {
                    showToast(it.data?.messageId.toString())
                }
                is Resource.Error -> {
                    showToast("Gagal Mengupdate Password : ${it.data?.messageId.toString()}")
                }
                is Resource.Loading -> {
                    showToast("Mengubah Password...")
                }
            }
        }

        remoteUpdateProfileObserver = Observer { it ->
            when (it) {
                is Resource.Success -> {
                    showToast(it.message.toString())
                    updateProfile(
                        it.data?.resData
                    )
                }
                is Resource.Error -> {
                    showToast("Gagal Mengupdate Profile : ${it.message.toString()}")
                }
                is Resource.Loading -> {

                }
            }
            Timber.d("update profile status = ${it.data}")
        }
    }

    private fun updateProfile(user: User?) {
        user?.let {
            val entity = AuthEntity(
                user_id = user.id,
                name = user.name,
                role = user.role,
                contact = user.contact,
                status = user.status,
                email = user.email,
                photo = user.photo_path,
                photo_base64 = ""
            )
            authViewModel.saveAuthInfo(entity)
        }
        viewModel.getProfileLocally()
    }

}