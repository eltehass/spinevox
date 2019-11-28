package com.leo.spinevox.network

import kotlinx.coroutines.Deferred
import retrofit2.http.*
import java.io.Serializable

interface SpineVoxApi {

    @POST("api/auth/jwt/create/")
    fun login(@Field("phone") phone: String, @Field("password") password: String): Deferred<LoginResponse>

    @POST("api/auth/social/facebook/")
    fun facebookLogin(@Field("access_token") accessToken: String): Deferred<LoginResponse>

    @POST("api/auth/social/google/")
    fun googleLogin(@Field("access_token") accessToken: String): Deferred<LoginResponse>

    @POST("api/auth/jwt/verify/")
    fun verifyToken(@Field("token") token: String): Deferred<LoginResponse>

    @POST("api/auth/jwt/refresh/")
    fun refreshToken(@Field("token") token: String): Deferred<RefreshResponse>

    @GET("api/auth/me/")
    fun me(@Query("data") data: MeData): Deferred<Any>
    //PUTS

    @PUT("api/auth/me/photo/")
    fun changePhoto(@Part("source_photo") sourcePhoto: String): Deferred<ChangePhotoResponse>

    @POST("api/auth/me/email/send/")
    fun changeEmail(@Field("email") email: String): Deferred<DetailResponse>

    @POST("api/auth/me/email/confirm/")
    fun changeEmailConfirm(@Field("email") email: String, @Field("token") token: String): Deferred<DetailResponse>

    @POST("api/auth/password/")
    fun setPassword(@Field("new_password") newPassword: String, @Field("current_password") currentPassword: String): Deferred<DetailResponse>

    @POST("api/auth/password/reset/")
    fun resetPassword(@Field("phone") phone: String): Deferred<DetailResponse>

    @POST("api/auth/password/reset/confirm/")
    fun resetPasswordConfirm(@Field("code") code: String, @Field("phone") phone: String, @Field("new_password") newPassword: String): Deferred<DetailResponse>

    @POST("api/auth/users/create/")
    fun registration(@Field("phone") phone: String, @Field("email") email: String?,
                     @Field("first_name") firstName: String, @Field("last_name") lastName: String,
                     @Field("password") password: String, @Field("about_us") aboutUs: String): Deferred<DetailResponse>

    @POST("api/auth/users/activate/")
    fun registrationConfirm(@Field("phone") phone: String, @Field("token") token: String): Deferred<DetailResponse>

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

data class ChangePhotoResponse(val photo: String, val detail: String) : Serializable
data class DetailResponse(val detail: String) : Serializable