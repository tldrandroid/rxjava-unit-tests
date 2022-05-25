package com.oliverspryn.android.rxjava.data

import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserProfileRepository @Inject constructor() {
    data class Logins(
        val deviceName: String,
        val location: String
    )

    data class UserInfo(
        val email: String,
        val name: String,
        val nickName: String,
        val picture: String
    )

    fun getActiveLogins(): Single<List<Logins>> = Single
        .just(
            listOf(
                Logins(deviceName = "Samsung Note 10", location = "New York, NY"),
                Logins(deviceName = "Samsung Note 20", location = "New York, NY"),
                Logins(deviceName = "Google Pixel 6 Pro", location = "Hackensack, NJ")
            )
        )
        .delay(250L, TimeUnit.MILLISECONDS)

    fun getUserProfile(): Single<UserInfo> = Single
        .just(
            UserInfo(
                email = "john.smith@gmail.com",
                name = "John Smith",
                nickName = "Johnny",
                picture = "https://www.fillmurray.com/300/300"
            )
        )
        .delay(500L, TimeUnit.MILLISECONDS)

    fun getUserProfileLongWait(): Single<UserInfo> = Single
        .just(
            UserInfo(
                email = "john.smith@gmail.com",
                name = "John Smith",
                nickName = "Johnny",
                picture = "https://www.fillmurray.com/300/300"
            )
        )
        .delay(10L, TimeUnit.SECONDS)
}
