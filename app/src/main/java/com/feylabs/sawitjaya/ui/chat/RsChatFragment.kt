package com.feylabs.sawitjaya.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.data.remote.request.RsChatStoreRequestBody
import com.feylabs.sawitjaya.data.remote.response.RsChatResponseItem
import com.feylabs.sawitjaya.databinding.FragmentRsChatBinding
import com.feylabs.sawitjaya.ui.base.BaseFragment
import com.feylabs.sawitjaya.data.remote.service.Resource
import org.koin.android.viewmodel.ext.android.viewModel


class RsChatFragment : BaseFragment() {

    private var _binding: FragmentRsChatBinding? = null
    private val binding get() = _binding as FragmentRsChatBinding

    private val mAdapter by lazy { RsChatAdapter() }
    private val args: RsChatFragmentArgs by navArgs()

    var topicID = ""

    val viewModel: RsChatViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRsChatBinding.inflate(inflater)
        return binding.root
    }

    override fun initUI() {
        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            setHasFixedSize(true)
        }
    }

    override fun initObserver() {

        viewModel.insertChatItemLD.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    viewVisible(binding.includeLoading.root)
                }
                is Resource.Success -> {
                    viewGone(binding.includeLoading.root)
                    viewModel.fetchChat(topicID)
                }
                is Resource.Error -> {
                    showToast("Gagal Menambahkan Chat")
                }
            }
        })

        viewModel.chatItemLD.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    val data = it.data
                    data?.let { chatList ->
                        if (chatList.size > 0) {
                            viewGone(binding.includeEmpty.root)
                            mAdapter.addData(chatList)
                            mAdapter.notifyDataSetChanged()
                        }else{
                            binding.includeEmpty.tvDesc.text=getString(R.string.message_empty_chat)
                            viewVisible(binding.includeEmpty.root)
                        }
                    }
                    viewGone(binding.includeLoading.root)
                    binding.rvChat.scrollToPosition(mAdapter.itemCount)
                }
                is Resource.Error -> {
                    viewGone(binding.includeEmpty.root)
                    viewGone(binding.includeLoading.root)
                }
                is Resource.Loading -> {
                    viewGone(binding.includeEmpty.root)
                    viewVisible(binding.includeLoading.root)
                }
            }
        })

    }

    override fun initAction() {

        binding.btnSend.setOnClickListener {
            val text = binding.etInput.text.toString()
            mAdapter.insertItem(
                RsChatResponseItem(
                    createdAt = "",
                    id = 0,
                    isDeleted = false,
                    idSender = MyPreference(requireContext()).getUserID()?.toInt(),
                    isRead = false,
                    isSend = false,
                    message = text,
                    senderName = "Anda",
                    senderPhoto = "",
                    topic = 3,
                    type = 0,
                    updatedAt = "",
                )
            )
            mAdapter.notifyDataSetChanged()
            binding.etInput.text.clear()
            storeChatToServer(message = text)
        }
    }

    private fun storeChatToServer(message: String) {
        viewModel.insertChat(
            chatStoreRequestBody =
            RsChatStoreRequestBody(
                message = message,
                topic = topicID,
                type = "3"
            )
        )
    }

    override fun initData() {
        topicID = args.topicID
        viewModel.fetchChat(topicID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}