package com.example.rickandmorty.data.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor

object HttpClientProvider {
    val client = HttpClient(OkHttp) {
        engine {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        defaultRequest {
            url("https://rickandmortyapi.com/api/")
        }
    }
}