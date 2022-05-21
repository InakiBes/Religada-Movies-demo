package com.religada.moviesdemo.notifications

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.BuildConfig
import com.google.firebase.messaging.FirebaseMessaging
import com.religada.moviesdemo.data.model.StandardResponse
import com.religada.moviesdemo.data.remote.ApiConnection
import com.religada.moviesdemo.utils.EncryptPrefManager
import com.religada.moviesdemo.utils.PrefManager
import com.religada.moviesdemo.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class TokenManager @Inject constructor(
    val context: Context,
    val encryptPref: EncryptPrefManager,
    val prefManager: PrefManager
) {
    fun getInstaFirebase() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { regTokenTask ->
                if (regTokenTask.isSuccessful) {
                    sendToServerIfNewToken(regTokenTask.result)
                    log("FCM registration token: ${regTokenTask.result}")
                } else {
                    log("Unable to retrieve registration token ${regTokenTask.exception}")
                }
            }
            FirebaseInstallations.getInstance().id.addOnCompleteListener { installationIdTask ->
                if (installationIdTask.isSuccessful) {
                    val token = installationIdTask.result
                    log( "Firebase Installations ID: ${token}")
                } else {
                    log("Unable to retrieve installations ID ${installationIdTask.exception}")
                }
            }
        } catch (e: Exception) {
            log("Error obteniendo la instancia de Firebase.")
            e.printStackTrace()
        }
    }

    private fun sendToServerIfNewToken(token: String) {
        val savedToken = encryptPref.getString(encryptPref.FIREBASE_TOKEN) ?: ""
        if (savedToken == token)
            return
        else {
            encryptPref.putString(encryptPref.FIREBASE_TOKEN, token)
            sendTokenToServer(token)
        }
    }

    private fun sendTokenToServer(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val body: RequestBody = RequestBody.create(
                    "application/hal+json".toMediaTypeOrNull(),
                    "{\"firebase_token\": \"$token\"}\n"
                )
                val retroService = ApiConnection.RETRO_SERVICE_NO_AUTH
                val apiResponse: Response<StandardResponse> =
                    retroService.sendFirebaseTokenToServer(body)
                if (apiResponse.isSuccessful) {
                    log("TOKEN enviado al servidor correctamente: $token")
                } else {
                    apiResponse.errorBody()?.let { responseBody ->
                        try {
                            val msg = JSONObject(responseBody.string())
                            log("ERROR enviando el firebase token: ${msg["error_msg"]}")
                        } catch (e: Exception) {
                            log("Error ${e.message}")
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                log("Error calling API to send Firebase Token to server")
            }
        }
    }
}