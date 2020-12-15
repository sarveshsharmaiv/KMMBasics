package com.example.networkdatabase.shared.cache

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String

interface AppDatabaseQueries : Transacter {
  fun <T : Any> selectTemplateById(templateID: Long, mapper: (
    seoID: Long,
    dimension: String,
    categoryID: Int,
    templateID: Long,
    previewURL: String?,
    thumbnailURL: String?,
    websiteThumbnailURL: String?,
    defaultPreviewURL: String?,
    defaultThumbnailURL: String?,
    defaultWebsiteThumbnailURL: String?
  ) -> T): Query<T>

  fun selectTemplateById(templateID: Long): Query<Template>

  fun <T : Any> selectAllTemplates(mapper: (
    seoID: Long,
    dimension: String,
    categoryID: Int,
    templateID: Long,
    previewURL: String?,
    thumbnailURL: String?,
    websiteThumbnailURL: String?,
    defaultPreviewURL: String?,
    defaultThumbnailURL: String?,
    defaultWebsiteThumbnailURL: String?
  ) -> T): Query<T>

  fun selectAllTemplates(): Query<Template>

  fun insertTemplates(
    seoID: Long,
    dimension: String,
    categoryID: Int,
    templateID: Long,
    previewURL: String?,
    thumbnailURL: String?,
    websiteThumbnailURL: String?,
    defaultPreviewURL: String?,
    defaultThumbnailURL: String?,
    defaultWebsiteThumbnailURL: String?
  )

  fun removeAllTemplates()
}
