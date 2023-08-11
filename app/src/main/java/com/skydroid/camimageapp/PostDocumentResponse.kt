/*
 * *
 *  * Created by Akash Singh  on 13/06/23, 2:48 pm
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 13/06/23, 2:48 pm
 *
 */

package com.skydroid.camimageapp


import com.google.gson.annotations.SerializedName

data class PostDocumentResponse(
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("error")
    val error: Error?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("created_by")
        val createdBy: String?,
        @SerializedName("entity_id")
        val entityId: String?,
        @SerializedName("file_type")
        val fileType: String?,
        @SerializedName("filename")
        val filename: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("is_active")
        val isActive: Boolean?,
        @SerializedName("is_deleted")
        val isDeleted: Boolean?,
        @SerializedName("media_category")
        val mediaCategory: String?,
        @SerializedName("media_type")
        val mediaType: String?,
        @SerializedName("path")
        val path: String?,
        @SerializedName("updated_at")
        val updatedAt: Any?,
        @SerializedName("updated_by")
        val updatedBy: String?,
        @SerializedName("user")
        val user: String?
    )

    class Error
}