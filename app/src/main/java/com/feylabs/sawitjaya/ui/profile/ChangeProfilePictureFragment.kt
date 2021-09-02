package com.feylabs.sawitjaya.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.FragmentChangeProfilePictureBinding
import com.feylabs.sawitjaya.utils.TelegramGalleryActivity

import com.tangxiaolv.telegramgallery.GalleryActivity

import com.tangxiaolv.telegramgallery.GalleryConfig
import timber.log.Timber
import java.io.File

import android.net.Uri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.feylabs.sawitjaya.utils.base.BaseFragment
import com.feylabs.sawitjaya.injection.ServiceLocator
import com.feylabs.sawitjaya.utils.service.Resource
import com.feylabs.sawitjaya.ui.auth.viewmodel.AuthViewModel


class ChangeProfilePictureFragment : BaseFragment() {

    var _binding: FragmentChangeProfilePictureBinding? = null
    val binding get() = _binding as FragmentChangeProfilePictureBinding
    lateinit var uploadedFile: File

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ServiceLocator.provideFactory(requireContext())
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        binding.btnChoosePhoto.setOnClickListener {
            //open album
            Timber.d("open album")
            val config = GalleryConfig.Build()
                .limitPickPhoto(1)
                .singlePhoto(true)
                .hintOfPick("max 1 gambar")
                .build()
            TelegramGalleryActivity.openActivity(this, 120, config)
        }

        binding.btnSaveProfile.setOnClickListener {
            authViewModel.updateProfilePicture(uploadedFile).observe(viewLifecycleOwner, Observer {

                when (it) {
                    is Resource.Loading -> {
                        binding.tvProgress.text = it.data
                    }
                    is Resource.Success -> {
                        binding.tvProgress.text = ""
                        showToast(it.data.toString())
                        updatePhotoLocally()
                    }
                    is Resource.Error -> {
                        binding.tvProgress.text = ""
                        showToast(it.message.toString())
                    }
                }

            })
        }
    }

    private fun updatePhotoLocally() {

    }


//    fun getFIleFromImageView(imageView: ImageView): File {
//
//        val drawable = imageView.drawable
//        val bitmap = drawable.toBitmap()
//
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_profile_picture, container, false)
        _binding = FragmentChangeProfilePictureBinding.bind(view)
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //list of photos of seleced

        if (requestCode == 120) {
            val photos = data?.getSerializableExtra(GalleryActivity.PHOTOS) as List<String>
            val firstPhotos = photos[0]
            val uriFIle = "file://" + firstPhotos
            Timber.d("photosz picked $photos")
            Timber.d("photosz file picked $uriFIle")
            Timber.d("photosz upload uri parse : ${Uri.parse(uriFIle)}")

            Glide.with(requireContext())
                .load(Uri.parse(uriFIle)).into(binding.ivProfilePicture)

            uploadedFile = File(Uri.parse(uriFIle).path.toString())

        }

    }

    private fun loadIntoProfile(path: String) {
        Timber.d("load image into preview")
        val file = File(path).parent
        Timber.d("parent file ${file}")
        Glide.with(requireContext()).load(File(path)).into(binding.ivProfilePicture)
    }


}