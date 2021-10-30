package com.feylabs.sawitjaya.data.remote

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.sawitjaya.data.local.preference.MyPreference
import com.feylabs.sawitjaya.data.remote.request.RegisterRequestBody
import com.feylabs.sawitjaya.data.remote.request.RequestSellRequest
import com.feylabs.sawitjaya.data.remote.request.RsChatStoreRequestBody
import com.feylabs.sawitjaya.data.remote.response.*
import com.feylabs.sawitjaya.injection.ServiceLocator.BASE_URL
import com.feylabs.sawitjaya.data.remote.service.ApiService
import com.feylabs.sawitjaya.data.remote.service.AuthService
import com.feylabs.sawitjaya.data.remote.service.Resource
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.lang.Exception
import retrofit2.Callback as retrocallbak

class RemoteDataSource(
    private val commonService: ApiService,
    private val authService: AuthService,
    private val context: Context
) {

    /*
    Login
    @param username,password
     */
    fun login(
        username: String,
        password: String
    ): MutableLiveData<Resource<LoginResponse?>> {

        val returnValue = MutableLiveData<Resource<LoginResponse?>>()

        authService.login(
            username,
            password
        ).enqueue(object : retrocallbak<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>, response: Response<LoginResponse>
            ) {
                if (response.body()?.status_code == 1) {
                    returnValue.postValue(Resource.Success(response.body(), ""))
                } else {
                    returnValue.postValue(Resource.Error(response.body()?.message.toString()))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                returnValue.postValue(Resource.Error("Terjadi Kesalahan"))
            }
        })
        return returnValue
    }

    /**
    Login
    @param username,password
     */
    fun updateProfile(
        name: String,
        email: String,
        contact: String,
        role: String,
        callback: CallbackUpdateProfile
    ) {
        callback.value(Resource.Loading())
        commonService.update_data(
            getTOKEN().toString(),
            email, role, name, contact,
        ).enqueue(object : retrocallbak<UserUpdateProfileResponse> {
            override fun onResponse(
                call: Call<UserUpdateProfileResponse>,
                response: Response<UserUpdateProfileResponse>
            ) {
                Timber.d("rds update_data ${response.raw()}")
                val messageID = response.body()?.messageId
                val messageEN = response.body()?.messageEn
                if (response.body()?.statusCode == 1) {
                    callback.value(Resource.Success(response.body(), messageID.toString()))
                } else {
                    callback.value(Resource.Error(response.body()?.messageId.toString()))
                }
            }

            override fun onFailure(call: Call<UserUpdateProfileResponse>, t: Throwable) {
                Timber.d("rds update_data : onFailure")
                callback.value(Resource.Error("Terjadi Kesalahan"))
            }
        })
    }


    /**
     * Register
     * @param body,callback
     *
     */
    fun register(
        body: RegisterRequestBody,
        callback: CallbackRegisterProfile
    ) {
        callback.value(Resource.Loading())
        authService.register(getTOKEN(), body)?.enqueue(object : retrocallbak<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Timber.d("response register (success) -> ${response.body()}")
                try {
                    val json = JSONObject(response.body()!!.string())
                    val statusCode: Int? = json?.getInt("status_code")
                    val message: String = json?.getString("message") ?: "";
                    Timber.d("response register status_code (success) -> ${statusCode}")
                    Timber.d("response register message (success) -> ${message}")
                    if (statusCode == 1) {
                        callback.value(Resource.Success("Registrasi Berhasil"))
                    } else {
                        callback.value(Resource.Error(message))
                    }
                } catch (e: Exception) {
                    Timber.d("raz exception at RemoteDataSource @register -> ${e.toString()}")
                    callback.value(Resource.Error("Registrasi Gagal"))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.value(Resource.Success("Registrasi Gagal"))
            }
        })
    }


    /**
     * Register
     * @param body,callback
     *
     */
    fun changePassword(
        old_password: String,
        new_password: String,
        callback: CallbackChangePasswordProfile
    ) {
        callback.value(Resource.Loading())

        commonService.changePassword(getTOKEN(), old_password, new_password)
            ?.enqueue(object : retrocallbak<ChangePasswordResponse> {
                override fun onResponse(
                    call: Call<ChangePasswordResponse>,
                    response: Response<ChangePasswordResponse>
                ) {
                    if (response.isSuccessful && response.body()?.statusCode == 1) {
                        callback.value(Resource.Success(response.body()))
                    } else {
                        callback.value(Resource.Error(response.body()?.messageId.toString()))
                    }
                }

                override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                    callback.value(Resource.Error("Error : " + t.localizedMessage.toString() + " " + t.message))
                }
            })
    }

    /**
     * get request sell by user id
     * @param userID : String
     *
     */
    suspend fun getRequestSellByUser(
        userID: String,
        status: String? = null,
        page: Int, per_page: Int, paginate: Boolean = true
    ) =
        commonService.getRequestSellByUser(
            userID = userID,
            paginate = paginate,
            page = page,
            per_page = per_page,
            authHeader = getTOKEN(),
            status = status,
        )

    /**
     * get news
     * @param body,callback
     *
     */
    suspend fun getNews() =
        commonService.getNews(getTOKEN())

    /**
     * get price
     * @param body,callback
     *
     */
    suspend fun getPrices() =
        commonService.getPrice(getTOKEN())

    /**
     * get mnotification
     * @param body,callback
     *
     */
    suspend fun getMNotificationByUser(userID: String) =
        commonService.getMNotificationByUser(userID, getTOKEN())

    /**
     * get mnotification
     * @param body,callback
     *
     */
    suspend fun getScaleDataByRsID(rsID: String) =
        commonService.getRsScaleByRsID(rsID, getTOKEN())

    /**
     * get mnotification
     * @param body,callback
     *
     */
    suspend fun refreshToken() =
        commonService.refreshToken(getTOKEN())

    /**
     * get mnotification
     * @param body,callback
     *
     */
    suspend fun storeScaleDataByRsID(rsID: String, result: String, createdBy: String) =
        commonService.storeRsScaleByRsID(
            rsID = rsID, result = result, createdBy = createdBy,
            token = getTOKEN()
        )

    /**
     * get mnotification
     * @param body,callback
     *
     */
    suspend fun deleteScaleDataByID(rsID: String) =
        commonService.deleteRsScaleByID(
            rsID = rsID,
            token = getTOKEN()
        )

    /**
     */
    fun storeRsFileSignature(
        rsID: String,
        type: String,
        file: File,
        callback: CallbackStoreRsSignature
    ) {
        AndroidNetworking.upload(
            BASE_URL + "request-sell/$rsID/store-signature"
        )
            .setPriority(Priority.HIGH)
            .addMultipartParameter("type", type)
            .addMultipartFile("photo", file)
            .addHeaders("Authorization", getTOKEN())
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->
                val value = "${bytesUploaded / 1024} KB of ${totalBytes / 1024} KB"
                callback.value(Resource.Loading(value))
            }
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Timber.d("FAN Upload Success : $response")
                    val res = response
                    val message = res?.getString("message") ?: "Default Message"
                    callback.value(Resource.Success(message, message))
                }

                override fun onError(anError: ANError?) {
                    Timber.d("FAN Upload Error : $anError")
                    Timber.d("FAN Upload Error RSID  : $rsID")
                    Timber.d("FAN Upload Error Body : ${anError?.errorBody}")
                    Timber.d("FAN Upload Error Code : ${anError?.errorCode}")
                    Timber.d("FAN Upload Error Detail : ${anError?.errorDetail}")
                    Timber.d("FAN Upload Error Localized Message : ${anError?.localizedMessage}")
                    callback.value(Resource.Error("Error : " + anError))
                }

            })
    }

    /**
     * Register
     * @param body,callback
     */
    fun changeProfiePicture(
        file: File,
        callback: CallbackChangeProfilePicture,
    ) {
        AndroidNetworking.upload(
            BASE_URL + "user/update-photo"
        )
            .setPriority(Priority.HIGH)
            .addMultipartFile("photo", file)
            .addHeaders("Authorization", getTOKEN())
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->
                val value = "${bytesUploaded / 1024} KB of ${totalBytes / 1024} KB"
                callback.value(Resource.Loading(value))
            }
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Timber.d("FAN Upload Success : $response")
                    val res = response
                    val status_code = res?.getInt("status_code") ?: 0
                    val message = res?.getString("message_id") ?: "Default Message"
                    if (status_code == 1) {
                        callback.value(Resource.Success(message, message))
                    } else {
                        //IF UPLOAD FILE IS FAILED
                        //Show the reason
                        Timber.d("FAN Upload Error : response statcode")
                        callback.value(Resource.Error(message, message))
                    }
                }

                override fun onError(anError: ANError?) {
                    Timber.d("FAN Upload Error : $anError")
                    Timber.d("FAN Upload Error Body : ${anError?.errorBody}")
                    Timber.d("FAN Upload Error Code : ${anError?.errorCode}")
                    Timber.d("FAN Upload Error Detail : ${anError?.errorDetail}")
                    Timber.d("FAN Upload Error Localized Message : ${anError?.localizedMessage}")
                    callback.value(Resource.Error("Error : " + anError))
                }

            })


    }

    /**
     * Send Request Sell From User
     * @param body,callback
     *
     */
    fun sendRequestSell(
        rsReq: RequestSellRequest,
        callback: CallbackUploadRS,
    ) {
        Timber.d("send request sell")
        val myNetwork = AndroidNetworking.upload(
            BASE_URL + "request-sell/store"
        )
        val file = rsReq.uploadFile

        myNetwork.apply {
            setPriority(Priority.HIGH)
            addHeaders("Authorization", getTOKEN())
            addMultipartParameter("lat", rsReq.lat)
            addMultipartParameter("long", rsReq.long)
            addMultipartParameter("address", rsReq.address)
            addMultipartParameter("est_weight", rsReq.estWeight)
            addMultipartParameter("contact", rsReq.contact)
            addMultipartParameter("status", rsReq.status)
            file?.forEachIndexed { index, file ->
                addMultipartFile("upload_file[$index]", file)
            }
        }
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->
                val value = "${bytesUploaded / 1024} KB of ${totalBytes / 1024} KB"
                callback.value(Resource.Loading(value))
            }
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Timber.d("FAN Upload Success : $response")
                    val res = response
                    val status_code = res?.getInt("status_code") ?: 0
                    val message = res?.getString("message_id") ?: "Default Message"
                    if (status_code == 1) {
                        callback.value(Resource.Success(message, message))
                    } else {
                        //IF UPLOAD FILE IS FAILED
                        //Show the reason
                        Timber.d("FAN Upload Error : response statcode")
                        callback.value(Resource.Error(message, message))
                    }
                }

                override fun onError(anError: ANError?) {
                    Timber.d("FAN Upload Error : $anError")
                    Timber.d("FAN Upload Error : ${anError?.errorBody}")
                    Timber.d("FAN Upload Error Body : ${anError?.errorBody}")
                    Timber.d("FAN Upload Error Code : ${anError?.errorCode}")
                    Timber.d("FAN Upload Error Detail : ${anError?.errorDetail}")
                    Timber.d("FAN Upload Error Localized Message : ${anError?.localizedMessage}")
                    callback.value(Resource.Error("Error : " + anError))
                }

            })


    }

    suspend fun getDetailRequestSell(id: String) =
        commonService.getRequestSellDetail(id, getTOKEN())

    suspend fun storeRsFinalInfo(
        rsID: String,
        finalPrice: String,
        finalMargin: String,
        pricePaid: String
    ) =
        commonService.storeFinalRsData(
            rsID = rsID, token = getTOKEN(), finalPrice = finalPrice,
            pricePaid = pricePaid, finalMargin = finalMargin
        )

    suspend fun getRsChatByTopic(topicId: String) =
        commonService.getRsChatByTopic(topicID = topicId, authHeader = getTOKEN())

    suspend fun getLandingMessage() =
        commonService.landingMessage(authHeader = getTOKEN())

    suspend fun changeRsStatus(rsID: String, status: String) =
        commonService.changeRsStatus(rsID = rsID, status = status, token = getTOKEN())

    suspend fun getProfileByUser() =
        commonService.getProfileByUser(getTOKEN())

    suspend fun insertChat(chatStoreRequestBody: RsChatStoreRequestBody) =
        commonService.storeChat(getTOKEN(), chatStoreRequestBody)

    interface CallbackUpdateProfile {
        fun value(response: Resource<UserUpdateProfileResponse?>)
    }

    interface CallbackRegisterProfile {
        fun value(response: Resource<String?>)
    }

    interface CallbackChangePasswordProfile {
        fun value(response: Resource<ChangePasswordResponse?>)
    }

    interface CallbackChangeProfilePicture {
        fun value(response: Resource<String?>)
    }

    interface CallbackUploadRS {
        fun value(response: Resource<String?>)
    }

    interface CallbackStoreRsSignature {
        fun value(response: Resource<String?>)
    }

    private fun getTOKEN(): String {
        val tokenn = MyPreference(context).getPrefString("TOKEN")
        Timber.d("tokenn hitted : ${tokenn}")
        return tokenn.toString()
    }

}