package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "diagnoses")
data class UserDiagnosis(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val hairType: String,
    val scalpType: String,
    val problems: String, // Comma separated, e.g. "Hair Fall, Dandruff"
    val stressLevel: String,
    val dietQuality: String,
    val sleepQuality: String,
    val heatStyling: String,
    val diagnosisSummary: String,
    val rootCause: String,
    val severity: String,
    val solutionPlan: String,
    val recommendedProductIds: String // Comma separated IDs
)

@Entity(tableName = "products")
data class HairProduct(
    @PrimaryKey val id: String,
    val name: String,
    val brand: String,
    val category: String, // "Shampoo", "Conditioner", "Hair Oil", "Serum"
    val targetProblem: String, // e.g. "Dandruff", "Hair Fall", "Dryness"
    val ingredients: String,
    val hairTypeCompatibility: String,
    val price: Double,
    val reasonForSelection: String,
    val usageInstructions: String,
    val frequencyPerWeek: Int,
    val imageUrlPlaceholder: String // Accent color descriptor or local icon ID
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val role: String, // "user" or "assistant"
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "progress_logs")
data class ProgressLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val scalpFeel: Int,   // 1 to 5
    val hairStrength: Int, // 1 to 5
    val breakageLevel: Int, // 1 to 5
    val notes: String
)

@Dao
interface DiagnosisDao {
    @Query("SELECT * FROM diagnoses ORDER BY timestamp DESC")
    fun getAllDiagnoses(): Flow<List<UserDiagnosis>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnosis(diagnosis: UserDiagnosis): Long

    @Query("SELECT * FROM diagnoses WHERE id = :id")
    suspend fun getDiagnosisById(id: Int): UserDiagnosis?

    @Query("DELETE FROM diagnoses")
    suspend fun clearAll()
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<HairProduct>>

    @Query("SELECT * FROM products WHERE id IN (:ids)")
    suspend fun getProductsByIds(ids: List<String>): List<HairProduct>

    @Query("SELECT * FROM products WHERE targetProblem = :problem OR category = :category")
    suspend fun getProductsForProblemOrCategory(problem: String, category: String): List<HairProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<HairProduct>)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearHistory()
}

@Dao
interface ProgressLogDao {
    @Query("SELECT * FROM progress_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<ProgressLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ProgressLog)
}

@Database(
    entities = [UserDiagnosis::class, HairProduct::class, ChatMessage::class, ProgressLog::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diagnosisDao(): DiagnosisDao
    abstract fun productDao(): ProductDao
    abstract fun chatDao(): ChatDao
    abstract fun progressLogDao(): ProgressLogDao
}
