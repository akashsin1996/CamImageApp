package com.skydroid.camimageapp

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

/**
 * Created by Akash Singh on 02-June-2023.
 */

class ApiRepo {


    fun postDocumentData(
        farmerId: String,
        token: String,
        rewBody1: RequestBody,
        rewBody2: RequestBody,
        rewBody3: RequestBody,
        rewBody4: RequestBody,
        mediaImage: MultipartBody.Part
    ): Call<PostDocumentResponse> =
        ApiClientGenric.instance.postDocumentData(farmerId,token,rewBody1,rewBody2,rewBody3,rewBody4,mediaImage)

}