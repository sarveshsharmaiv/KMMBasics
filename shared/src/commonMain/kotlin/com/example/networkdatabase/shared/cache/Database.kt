package com.example.networkdatabase.shared.cache
import com.example.networkdatabase.shared.Entity.PreviewList
import com.example.networkdatabase.shared.Entity.Template
import com.example.networkdatabase.shared.Entity.TemplateResources
import com.example.networkdatabase.shared.cache.AppDatabase

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun createTemplates(templates: List<Template>) {
        dbQuery.transaction {
            templates.forEach { template ->
                val dbTemplate = dbQuery.selectTemplateById(template.templateID).executeAsOneOrNull()
                if (dbTemplate == null) {
                    dbQuery.insertTemplates(seoID = template.seoID,
                        dimension = template.dimension,
                        categoryID = template.categoryID,
                        templateID = template.templateID,
                        previewURL = template.previewList.story[template.dimension]?.previewURL,
                        thumbnailURL = template.previewList.story[template.dimension]?.thumbnailURL,
                        websiteThumbnailURL = template.previewList.story[template.dimension]?.websiteThumbnailURL,
                        defaultPreviewURL = template.previewList.story["default"]?.previewURL,
                        defaultThumbnailURL = template.previewList.story["default"]?.thumbnailURL,
                        defaultWebsiteThumbnailURL = template.previewList.story["default"]?.websiteThumbnailURL
                    )
                }
            }
        }
    }

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllTemplates()
        }
    }

    internal fun getAllTemplates(): List<Template> {
        return dbQuery.selectAllTemplates(::mapTemplateSelecting).executeAsList()
    }

    private fun mapTemplateSelecting(seoID: Long, dimension: String, categoryID: Int, templateID: Long, previewURL: String?,
                                     thumbnailURL: String?, websiteThumbnailURL: String?, defaultPreviewURL: String?,
                                     defaultThumbnailURL: String?, defaultWebsiteThumbnailURL: String?): Template {
        val templateResource = TemplateResources(previewURL = previewURL, thumbnailURL = thumbnailURL, websiteThumbnailURL = websiteThumbnailURL)
        val defaultTemplateResource = TemplateResources(previewURL = defaultPreviewURL, thumbnailURL = defaultThumbnailURL,
            websiteThumbnailURL = defaultWebsiteThumbnailURL)
        val story = mapOf("default" to defaultTemplateResource, dimension to templateResource)
        val preview = PreviewList(story = story)
        return Template(seoID = seoID, dimension = dimension, templateID = templateID, categoryID = categoryID, previewList = preview)
    }
}