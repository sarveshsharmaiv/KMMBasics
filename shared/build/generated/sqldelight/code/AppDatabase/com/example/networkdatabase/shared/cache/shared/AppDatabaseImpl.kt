package com.example.networkdatabase.shared.cache.shared

import com.example.networkdatabase.shared.cache.AppDatabase
import com.example.networkdatabase.shared.cache.AppDatabaseQueries
import com.example.networkdatabase.shared.cache.Template
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.internal.copyOnWriteList
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.MutableList
import kotlin.jvm.JvmField
import kotlin.reflect.KClass

internal val KClass<AppDatabase>.schema: SqlDriver.Schema
  get() = AppDatabaseImpl.Schema

internal fun KClass<AppDatabase>.newInstance(driver: SqlDriver): AppDatabase =
    AppDatabaseImpl(driver)

private class AppDatabaseImpl(
  driver: SqlDriver
) : TransacterImpl(driver), AppDatabase {
  override val appDatabaseQueries: AppDatabaseQueriesImpl = AppDatabaseQueriesImpl(this, driver)

  object Schema : SqlDriver.Schema {
    override val version: Int
      get() = 1

    override fun create(driver: SqlDriver) {
      driver.execute(null, """
          |CREATE TABLE Template (
          |    seoID INTEGER NOT NULL,
          |    dimension TEXT NOT NULL,
          |    categoryID INTEGER NOT NULL DEFAULT 1,
          |    templateID INTEGER NOT NULL,
          |    previewURL TEXT,
          |    thumbnailURL TEXT,
          |    websiteThumbnailURL TEXT,
          |    defaultPreviewURL TEXT,
          |    defaultThumbnailURL TEXT,
          |    defaultWebsiteThumbnailURL TEXT
          |)
          """.trimMargin(), 0)
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Int,
      newVersion: Int
    ) {
    }
  }
}

private class AppDatabaseQueriesImpl(
  private val database: AppDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), AppDatabaseQueries {
  internal val selectTemplateById: MutableList<Query<*>> = copyOnWriteList()

  internal val selectAllTemplates: MutableList<Query<*>> = copyOnWriteList()

  override fun <T : Any> selectTemplateById(templateID: Long, mapper: (
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
  ) -> T): Query<T> = SelectTemplateByIdQuery(templateID) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getLong(2)!!.toInt(),
      cursor.getLong(3)!!,
      cursor.getString(4),
      cursor.getString(5),
      cursor.getString(6),
      cursor.getString(7),
      cursor.getString(8),
      cursor.getString(9)
    )
  }

  override fun selectTemplateById(templateID: Long): Query<Template> =
      selectTemplateById(templateID) { seoID, dimension, categoryID, templateID_, previewURL,
      thumbnailURL, websiteThumbnailURL, defaultPreviewURL, defaultThumbnailURL,
      defaultWebsiteThumbnailURL ->
    Template(
      seoID,
      dimension,
      categoryID,
      templateID_,
      previewURL,
      thumbnailURL,
      websiteThumbnailURL,
      defaultPreviewURL,
      defaultThumbnailURL,
      defaultWebsiteThumbnailURL
    )
  }

  override fun <T : Any> selectAllTemplates(mapper: (
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
  ) -> T): Query<T> = Query(1685605619, selectAllTemplates, driver, "AppDatabase.sq",
      "selectAllTemplates", """
  |SELECT *
  |FROM Template
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getLong(2)!!.toInt(),
      cursor.getLong(3)!!,
      cursor.getString(4),
      cursor.getString(5),
      cursor.getString(6),
      cursor.getString(7),
      cursor.getString(8),
      cursor.getString(9)
    )
  }

  override fun selectAllTemplates(): Query<Template> = selectAllTemplates { seoID, dimension,
      categoryID, templateID, previewURL, thumbnailURL, websiteThumbnailURL, defaultPreviewURL,
      defaultThumbnailURL, defaultWebsiteThumbnailURL ->
    Template(
      seoID,
      dimension,
      categoryID,
      templateID,
      previewURL,
      thumbnailURL,
      websiteThumbnailURL,
      defaultPreviewURL,
      defaultThumbnailURL,
      defaultWebsiteThumbnailURL
    )
  }

  override fun insertTemplates(
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
  ) {
    driver.execute(577953, """
    |INSERT INTO Template(seoID, dimension, categoryID, templateID, previewURL, thumbnailURL, websiteThumbnailURL, defaultPreviewURL, defaultThumbnailURL, defaultWebsiteThumbnailURL)
    |VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """.trimMargin(), 10) {
      bindLong(1, seoID)
      bindString(2, dimension)
      bindLong(3, categoryID.toLong())
      bindLong(4, templateID)
      bindString(5, previewURL)
      bindString(6, thumbnailURL)
      bindString(7, websiteThumbnailURL)
      bindString(8, defaultPreviewURL)
      bindString(9, defaultThumbnailURL)
      bindString(10, defaultWebsiteThumbnailURL)
    }
    notifyQueries(577953, {database.appDatabaseQueries.selectTemplateById +
        database.appDatabaseQueries.selectAllTemplates})
  }

  override fun removeAllTemplates() {
    driver.execute(-841808773, """DELETE FROM Template""", 0)
    notifyQueries(-841808773, {database.appDatabaseQueries.selectTemplateById +
        database.appDatabaseQueries.selectAllTemplates})
  }

  private inner class SelectTemplateByIdQuery<out T : Any>(
    @JvmField
    val templateID: Long,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectTemplateById, mapper) {
    override fun execute(): SqlCursor = driver.executeQuery(1928097223, """
    |SELECT * FROM Template
    |WHERE templateID = ?
    """.trimMargin(), 1) {
      bindLong(1, templateID)
    }

    override fun toString(): String = "AppDatabase.sq:selectTemplateById"
  }
}
