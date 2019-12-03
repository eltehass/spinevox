package com.spinevox.app.network

import kotlinx.coroutines.Deferred
import retrofit2.http.*
import java.io.Serializable

interface SpineVoxApi {

    @FormUrlEncoded
    @POST("api/auth/users/create/")
    fun registration(@Field("phone") phone: String,
                     @Field("email") email: String? = null,
                     @Field("first_name") firstName: String,
                     @Field("last_name") lastName: String = "",
                     @Field("password") password: String,
                     @Field("about_us") aboutUs: String = ""): Deferred<DetailResponse>

    @FormUrlEncoded
    @POST("api/auth/users/activate/")
    fun registrationConfirm(@Field("phone") phone: String, @Field("token") token: String): Deferred<DetailResponse>

    @FormUrlEncoded
    @POST("api/auth/jwt/create/")
    fun login(@Field("phone") phone: String, @Field("password") password: String): Deferred<LoginResponse>

    @FormUrlEncoded
    @POST("api/auth/social/facebook/")
    fun facebookLogin(@Field("access_token") accessToken: String): Deferred<LoginResponse>

    @FormUrlEncoded
    @POST("api/auth/social/google/")
    fun googleLogin(@Field("access_token") accessToken: String): Deferred<LoginResponse>

    @POST("api/auth/jwt/verify/")
    fun verifyToken(@Field("token") token: String): Deferred<LoginResponse>

    @POST("api/auth/jwt/refresh/")
    fun refreshToken(@Field("token") token: String): Deferred<RefreshResponse>

    @GET("api/auth/me/")
    fun me(@Header("Authorization") authorization: String): Deferred<DataClass>
    //PUTS

    @FormUrlEncoded
    @PUT("api/auth/me/photo/")
    fun changePhoto(@Header("Authorization") authorization: String, @Field("source_photo") sourcePhoto: String): Deferred<ChangePhotoResponse>

    @FormUrlEncoded
    @POST("api/auth/me/email/send/")
    fun changeEmail(@Header("Authorization") authorization: String, @Field("email") email: String): Deferred<DetailResponse>

    @FormUrlEncoded
    @POST("api/posedetect/inspection/")
    fun sendInspectionList(@Header("Authorization") authorization: String,
                           @Field("inspection_type") inspectionType: String,
                           @Field("back_image_source") backImage: String,
                           @Field("profile_image_source") profileImage: String,
                           @Field("skoliometry_pelvis") skoliometryPelvis: Int? = null,
                           @Field("skoliometry_lumbar") skoliometryLumbar: Int? = null,
                           @Field("skoliometry_chest") skoliometryChest: Int? = null) : Deferred<Any>

    @POST("api/auth/me/email/confirm/")
    fun changeEmailConfirm(@Field("email") email: String, @Field("token") token: String): Deferred<DetailResponse>

    @FormUrlEncoded
    @POST("api/auth/password/")
    fun setPassword(@Header("Authorization") authorization: String, @Field("new_password") newPassword: String, @Field("current_password") currentPassword: String): Deferred<DetailResponse>

    @POST("api/auth/password/reset/")
    fun resetPassword(@Field("phone") phone: String): Deferred<DetailResponse>

    @POST("api/auth/password/reset/confirm/")
    fun resetPasswordConfirm(@Field("code") code: String, @Field("phone") phone: String, @Field("new_password") newPassword: String): Deferred<DetailResponse>


    @POST("api/auth/notification/connect/")
    fun notificationConnect(@Field("name") deviceId: String, @Field("registration_id") key: String, @Field("device_id") uniqueId: String, @Field("type") iso: String): Deferred<DetailResponse>

}

//"api/auth/jwt/create/"
data class LoginResponse(val data: Data, val detail: String) : Serializable {
    data class Data(val token: String) : Serializable
}

data class RefreshResponse(val data: Data, val detail: String) : Serializable {
    data class Data(val token: String, val user_type: String) : Serializable
}

data class DataClass(val data: MeData): Serializable

data class MeData(
    val id: Int,
    val phone: String,
    val email: String?,
    val is_confirm_email: Boolean,
    val first_name: String,
    val last_name: String,
    val profile: Profile
) : Serializable {
    data class Profile(
        val photo: String?,
        val gender: String,
        val birthday: String,
        val height: Double,
        val weight: Double,
        val sit: String,
        val pain_mark: String,
        val is_ready: Boolean
    ) : Serializable
}

data class ChangePhotoResponse(val data: DataClass, val detail: String) : Serializable {
    data class DataClass(val photo: String): Serializable
}
data class DetailResponse(val detail: String) : Serializable