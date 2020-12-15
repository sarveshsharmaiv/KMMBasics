import com.example.networkdatabase.shared.Entity.Template
import com.example.networkdatabase.shared.Network.NetworkService
import com.example.networkdatabase.shared.cache.Database
import com.example.networkdatabase.shared.cache.DatabaseDriverFactory

class TemplatesSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory = databaseDriverFactory)
    private val api = NetworkService()

    @Throws(Exception::class) suspend fun getTemplates(forceReload: Boolean): List<Template> {
        val cachedValues = database.getAllTemplates()
        return if (cachedValues.isNotEmpty() && !forceReload) {
            cachedValues
        } else {
            api.getAllTemplates().also {
                database.clearDatabase()
                database.createTemplates(it)
            }
        }
    }
}