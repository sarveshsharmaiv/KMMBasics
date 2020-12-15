package com.example.networkdatabase.shared.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class Template(
    @SerialName("seo_id") val seoID: Long,
    @SerialName("dimension") val dimension: String,
    @SerialName("template_id") val templateID: Long,
    @SerialName("category_id") val categoryID: Int,
    @SerialName("preview_list") val previewList: PreviewList
)

@Serializable data class PreviewList(
    @SerialName("story") val story: Map<String,TemplateResources>
)

@Serializable data class TemplateResources(
    @SerialName("preview_url") val previewURL: String?,
    @SerialName("thumbnail_url") val thumbnailURL: String?,
    @SerialName("website_thumbnail_url") var websiteThumbnailURL: String? = ""
)
