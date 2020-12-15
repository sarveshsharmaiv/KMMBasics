package com.example.networkdatabase.shared.Network

import com.example.networkdatabase.shared.Entity.Template
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

class NetworkService {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
            serializer = KotlinxSerializer(json)
        }
    }
    companion object {
        private const val LAUNCHES_ENDPOINT = "https://beta-staging.invideo.io/get_seo_templates?seo_key=ad%20maker"
    }

    suspend fun getAllTemplates(): List<Template> {
        return httpClient.get(LAUNCHES_ENDPOINT)
    }
}
