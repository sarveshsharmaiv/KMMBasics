package com.example.networkdatabase.shared.cache

import kotlin.Int
import kotlin.Long
import kotlin.String

data class Template(
  val seoID: Long,
  val dimension: String,
  val categoryID: Int,
  val templateID: Long,
  val previewURL: String?,
  val thumbnailURL: String?,
  val websiteThumbnailURL: String?,
  val defaultPreviewURL: String?,
  val defaultThumbnailURL: String?,
  val defaultWebsiteThumbnailURL: String?
) {
  override fun toString(): String = """
  |Template [
  |  seoID: $seoID
  |  dimension: $dimension
  |  categoryID: $categoryID
  |  templateID: $templateID
  |  previewURL: $previewURL
  |  thumbnailURL: $thumbnailURL
  |  websiteThumbnailURL: $websiteThumbnailURL
  |  defaultPreviewURL: $defaultPreviewURL
  |  defaultThumbnailURL: $defaultThumbnailURL
  |  defaultWebsiteThumbnailURL: $defaultWebsiteThumbnailURL
  |]
  """.trimMargin()
}
