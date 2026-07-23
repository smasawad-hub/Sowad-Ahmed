package com.example.data.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class GeminiPart(
    val text: String? = null
)

data class GeminiContent(
    val parts: List<GeminiPart>
)

data class GeminiRequest(
    val contents: List<GeminiContent>
)

data class GeminiCandidate(
    val content: GeminiContent?
)

data class GeminiResponse(
    val candidates: List<GeminiCandidate>?
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: GeminiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeminiApi::class.java)
    }
}

class GeminiService {

    suspend fun generateCaption(userPrompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "✨ ${userPrompt.ifEmpty { "Living life in the sphere of endless possibilities" }} #SphereVibes #TechLife #AI"
        }

        try {
            val promptText = "Generate a short, engaging social media caption with 2 relevant hashtags based on this prompt: $userPrompt"
            val req = GeminiRequest(listOf(GeminiContent(listOf(GeminiPart(promptText)))))
            val resp = GeminiClient.api.generateContent(apiKey, req)
            val generated = resp.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            generated?.trim() ?: "✨ Exploring the future with Sphere! #SphereAI #Innovation"
        } catch (e: Exception) {
            "🚀 Elevating everyday moments on Sphere! #Sphere #SocialFuture"
        }
    }

    suspend fun translatePost(text: String, targetLang: String = "English"): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "[AI Translated to $targetLang]: $text"
        }

        try {
            val promptText = "Translate the following social media post text directly to $targetLang without commentary:\n\"$text\""
            val req = GeminiRequest(listOf(GeminiContent(listOf(GeminiPart(promptText)))))
            val resp = GeminiClient.api.generateContent(apiKey, req)
            resp.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.trim()
                ?: "[Translated]: $text"
        } catch (e: Exception) {
            "[AI Translated]: $text"
        }
    }

    suspend fun askAssistant(userMessage: String): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getLocalAiReply(userMessage)
        }

        try {
            val promptText = "You are Sphere AI, an intelligent, helpful, and friendly AI Assistant built inside the Sphere social network. Answer concisely and clearly:\n$userMessage"
            val req = GeminiRequest(listOf(GeminiContent(listOf(GeminiPart(promptText)))))
            val resp = GeminiClient.api.generateContent(apiKey, req)
            resp.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.trim()
                ?: getLocalAiReply(userMessage)
        } catch (e: Exception) {
            getLocalAiReply(userMessage)
        }
    }

    private fun getApiKey(): String {
        return try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }
    }

    private fun getLocalAiReply(input: String): String {
        val lower = input.lowercase()
        return when {
            "caption" in lower || "post" in lower -> "Here's a catchy caption idea: 'Connecting beyond boundaries on #Sphere ✨ What's inspiring your day?'"
            "growth" in lower || "follower" in lower || "monetize" in lower -> "To grow your Sphere audience: 1) Post short reels daily 2) Host live Q&As in Communities 3) Enable Creator Subscriptions in your Profile settings!"
            "2fa" in lower || "security" in lower -> "Sphere uses End-to-End Encryption for direct chats and supports TOTP Two-Factor Authentication under Profile > Security."
            else -> "Hello! I'm Sphere AI. I can help you craft viral posts, translate content, moderate comments, and analyze your creator growth metrics!"
        }
    }
}
