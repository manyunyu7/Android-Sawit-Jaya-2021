package com.feylabs.sawitjaya.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.ui.base.BaseFragment
import com.feylabs.sawitjaya.data.local.room.entity.AuthEntity
import com.feylabs.sawitjaya.data.remote.response.ChangePasswordResponse
import com.feylabs.sawitjaya.data.remote.response.User
import com.feylabs.sawitjaya.data.remote.response.UserUpdateProfileResponse
import com.feylabs.sawitjaya.databinding.SettingsFragmentBinding
import com.feylabs.sawitjaya.data.remote.service.Resource
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel
import com.feylabs.sawitjaya.utils.DialogUtils
import com.feylabs.sawitjaya.utils.UIHelper.loadImageFromURL
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SettingsFragment : BaseFragment() {

    override fun onResume() {
        super.onResume()
        viewModel.getProfileLocally()
    }

    var _binding: SettingsFragmentBinding? = null
    val binding get() = _binding as SettingsFragmentBinding

    val viewModel: SettingsViewModel by viewModel()
    val authViewModel: AuthViewModel by viewModel()

    lateinit var localProfileObserver: Observer<AuthEntity>
    lateinit var remoteUpdateProfileObserver: Observer<Resource<UserUpdateProfileResponse?>>
    lateinit var remoteChangePasswordeObserver: Observer<Resource<ChangePasswordResponse?>>

    override fun initUI() {
    }

    override fun initObserver() {

        localProfileObserver = Observer {
            binding.apply {
                tvNama.text = it.name
                etContact.setText(it.contact)
                etEmail.setText(it.email)
                etNama.setText(it.name)
                ivProfilePicture.loadImageFromURL(requireContext(), it.photo.toString())
            }
        }

        viewModel.localProfileLD.observe(viewLifecycleOwner, localProfileObserver)

        remoteChangePasswordeObserver = Observer {
            when (it) {
                is Resource.Success -> {
                    showToast(it.data?.messageId.toString())
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.modal_title_success),
                        message = getString(R.string.message_modal_success_req_rs),
                        positiveAction =
                        Pair(getString(R.string.label_Ok), {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
                is Resource.Error -> {
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = getString(R.string.title_modal_error_occured),
                        message = getString(R.string.message_modal_failed_update_password),
                        positiveAction = Pair("OK", {}),
                        negativeAction = Pair("Coba Lagi", {
                            changePasswordAction()
                        }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
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
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.modal_title_success),
                        message = getString(R.string.message_modal_success_update_profile),
                        positiveAction =
                        Pair(getString(R.string.label_Ok),
                            {}),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
                is Resource.Error -> {
                    DialogUtils.showCustomDialog(
                        context = requireContext(),
                        title = getString(R.string.title_modal_error_occured),
                        message = getString(R.string.message_modal_error_update_profile),
                        positiveAction = Pair("OK", {}),
                        negativeAction = Pair("Coba Lagi", {
                            saveProfileChangesToServer()
                        }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    showToast("Gagal Mengupdate Profile : ${it.message.toString()}")
                }
                is Resource.Loading -> {

                }
            }
            Timber.d("update profile status = ${it.data}")
        }
    }

    override fun initAction() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.userHomeFragment)
        }

        binding.btnChangePhoto.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_changeProfilePictureFragment)
        }

        binding.btnSavePassword.setOnClickListener {
            DialogUtils.showCustomDialog(
                context = requireContext(),
                title = getString(R.string.title_modal_are_you_sure),
                message = "Anda Yakin Ingin Mengganti Password",
                positiveAction = Pair("Ya", {
                    changePasswordAction()
                }),
                negativeAction = Pair("Tidak", {
                }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }

        /**
         * Save General Profile
         */
        binding.btnSaveChanges.setOnClickListener {
            DialogUtils.showCustomDialog(
                context = requireContext(),
                title = getString(R.string.title_modal_are_you_sure),
                message = getString(R.string.message_modal_save_profile_changes),
                positiveAction = Pair("Ya", {
                    saveProfileChangesToServer()
                }),
                negativeAction = Pair("Tidak", {
                }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }

    }

    override fun initData() {
        viewModel.getProfileLocally()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.settings_fragment, container, false)
        _binding = SettingsFragmentBinding.bind(view)

        return binding.root

    }

    private fun saveProfileChangesToServer() {
        val name = binding.etNama.text.toString()
        val contact = binding.etContact.text.toString()
        val email = binding.etEmail.text.toString()
        val role = MyPreference(requireContext()).getRole()
        viewModel.updateProfile(name, email, contact, role.toString())
            .observe(viewLifecycleOwner, remoteUpdateProfileObserver)
    }

    private fun changePasswordAction() {
        val old_password = binding.etPasswordOld.text.toString()
        val new_password = binding.etPasswordNew.text.toString()

        authViewModel.changePassword(old_password, new_password)
            .observe(viewLifecycleOwner, remoteChangePasswordeObserver)
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