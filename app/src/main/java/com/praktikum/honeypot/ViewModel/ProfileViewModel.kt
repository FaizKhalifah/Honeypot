package com.praktikum.honeypot.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praktikum.honeypot.Data.Owner
import com.praktikum.honeypot.Interface.ApiResponse
import com.praktikum.honeypot.Util.PreferencesHelper
import com.praktikum.honeypot.Util.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.File
import java.io.FileOutputStream

class ProfileViewModel(private val context: Context) : ViewModel() {
    private val profileApiService = RetrofitClient.getProfileApiService(context)
    private val preferencesHelper = PreferencesHelper(context)
    private val _profile = MutableStateFlow<Owner?>(null)
    val profile: StateFlow<Owner?> = _profile

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val response = profileApiService.getProfile().awaitResponse()
                if (response.isSuccessful) {
                    _profile.value = response.body()
                } else {
                    _profile.value = null
                }
            } catch (e: Exception) {
                _profile.value = null
            }
        }
    }

    fun updateProfile(
        fieldType: String,
        value: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Create RequestBody objects, setting null for fields we're not updating
                val username = if (fieldType == "username") 
                    value.toRequestBody("text/plain".toMediaTypeOrNull()) else null
                val fullName = if (fieldType == "full_name" || fieldType == "fullname") 
                    value.toRequestBody("text/plain".toMediaTypeOrNull()) else null
                val contact = if (fieldType == "contact") 
                    value.toRequestBody("text/plain".toMediaTypeOrNull()) else null

                val response = profileApiService.updateProfile(
                    username = username,
                    fullName = fullName,
                    contact = contact,
                    profile_image = null
                ).awaitResponse()

                if (response.isSuccessful) {
                    fetchProfile()
                    onSuccess()
                } else {
                    onError("Failed to update: ${response.message()}")
                }
            } catch (e: HttpException) {
                onError("HTTP Error: ${e.message}")
            } catch (e: Exception) {
                onError("Unexpected Error: ${e.message}")
            }
        }
    }

    fun updateProfileImage(
        imageFile: File,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Get file extension from original file
                val extension = imageFile.extension.lowercase()
                
                // Validate file extension
                if (extension !in listOf("jpg", "jpeg", "png")) {
                    onError("Only .png, .jpg and .jpeg format allowed!")
                    return@launch
                }

                // Create MultipartBody.Part directly from the original file
                val requestBody = imageFile.asRequestBody("image/${extension}".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData(
                    "profile_image",
                    "profile_image.${extension}",
                    requestBody
                )

                val response = profileApiService.updateProfile(
                    username = null,
                    fullName = null,
                    contact = null,
                    profile_image = imagePart
                ).awaitResponse()

                if (response.isSuccessful) {
                    fetchProfile()
                    onSuccess()
                } else {
                    // Get the error message from the response if possible
                    val errorBody = response.errorBody()?.string()
                    onError(errorBody ?: "Failed to update profile image: ${response.message()}")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                onError(errorBody ?: "HTTP Error: ${e.message}")
            } catch (e: Exception) {
                onError("Unexpected Error: ${e.message}")
            }
        }
    }

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val passwordData = mapOf(
            "currentPassword" to currentPassword,
            "newPassword" to newPassword
        )

        viewModelScope.launch {
            try {
                val response = profileApiService.changePassword(passwordData).awaitResponse()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.message == "Password updated successfully") {
                        onSuccess()
                    } else {
                        onError("Unexpected server response: ${body?.message ?: "Unknown error"}")
                    }
                } else {
                    onError("Failed to change password: ${response.message()}")
                }
            } catch (e: HttpException) {
                onError("HTTP Error: ${e.message}")
            } catch (e: Exception) {
                onError("Unexpected Error: ${e.message}")
            }
        }
    }
    fun logout(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = profileApiService.logout().awaitResponse() // Call the logout API

                if (response.isSuccessful) {
                    // Clear the stored tokens if logout is successful
                    preferencesHelper.clearTokens()
                    onSuccess()
                } else {
                    onError("Failed to log out: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Logout failed: ${e.message}")
            }
        }
    }
}
