package com.skydroid.camimageapp

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
/**
 * Created by Akash Singh on 02-June-2023.
 */

interface ApiInterface {

    @Multipart
    @POST("/api/v1/onboarding/users/{id}/user_media")
    fun postDocumentData(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Part("filename") filename: RequestBody,
        @Part("file_type") file_type: RequestBody,
        @Part("media_category") media_category: RequestBody,
        @Part("media_type") media_type: RequestBody,
        @Part mediaImage: MultipartBody.Part
    ): Call<PostDocumentResponse>

}