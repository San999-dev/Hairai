package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HairCareViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java, "haircare_database"
    ).build()

    val diagnosisDao = db.diagnosisDao()
    val productDao = db.productDao()
    val chatDao = db.chatDao()
    val progressDao = db.progressLogDao()

    // --- State Observables ---
    val allDiagnoses: StateFlow<List<UserDiagnosis>> = diagnosisDao.getAllDiagnoses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProducts: StateFlow<List<HairProduct>> = productDao.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessage>> = chatDao.getAllMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progressLogs: StateFlow<List<ProgressLog>> = progressDao.getAllLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- UI State Variables ---
    private val _isProUser = MutableStateFlow(false)
    val isProUser: StateFlow<Boolean> = _isProUser.asStateFlow()

    private val _isGeneratingDiagnosis = MutableStateFlow(false)
    val isGeneratingDiagnosis: StateFlow<Boolean> = _isGeneratingDiagnosis.asStateFlow()

    private val _isSendingChatMessage = MutableStateFlow(false)
    val isSendingChatMessage: StateFlow<Boolean> = _isSendingChatMessage.asStateFlow()

    private val _currentDiagnosisReport = MutableStateFlow<UserDiagnosis?>(null)
    val currentDiagnosisReport: StateFlow<UserDiagnosis?> = _currentDiagnosisReport.asStateFlow()

    private val _recommendedProductsForCurrent = MutableStateFlow<List<HairProduct>>(emptyList())
    val recommendedProductsForCurrent: StateFlow<List<HairProduct>> = _recommendedProductsForCurrent.asStateFlow()

    init {
        // Pre-seed product database if empty
        viewModelScope.launch {
            productDao.getAllProducts().first().let { current ->
                if (current.isEmpty()) {
                    productDao.insertProducts(ProductSeeder.getPreseededProducts())
                }
            }
            // Load latest diagnosis as current report if any exists
            allDiagnoses.collectLatest { diagnoses ->
                if (diagnoses.isNotEmpty() && _currentDiagnosisReport.value == null) {
                    setSelectedDiagnosis(diagnoses.first())
                }
            }
        }
    }

    fun toggleProUser() {
        _isProUser.value = !_isProUser.value
    }

    fun setSelectedDiagnosis(diagnosis: UserDiagnosis) {
        _currentDiagnosisReport.value = diagnosis
        viewModelScope.launch {
            val ids = diagnosis.recommendedProductIds.split(",").filter { it.isNotBlank() }
            if (ids.isNotEmpty()) {
                val items = productDao.getProductsByIds(ids)
                _recommendedProductsForCurrent.value = items
            } else {
                _recommendedProductsForCurrent.value = emptyList()
            }
        }
    }

    fun clearAllDiagnoses() {
        viewModelScope.launch {
            diagnosisDao.clearAll()
            _currentDiagnosisReport.value = null
            _recommendedProductsForCurrent.value = emptyList()
        }
    }

    // --- Diagnose Core Logic ---
    fun runDiagnosis(
        hairType: String,
        scalpType: String,
        problemsList: List<String>,
        stressLevel: String,
        dietQuality: String,
        sleepQuality: String,
        heatStyling: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isGeneratingDiagnosis.value = true
            val problemsStr = problemsList.joinToString(", ")
            
            // Generate report using Gemini
            val rawResult = GeminiService.generateHairReport(
                hairType = hairType,
                scalpType = scalpType,
                problems = problemsStr,
                stressLevel = stressLevel,
                dietQuality = dietQuality,
                sleepQuality = sleepQuality,
                heatStyling = heatStyling
            )

            // Parse blocks from markdown
            val summary = parseBlock(rawResult, "DIAGNOSIS_SUMMARY")
            val rootCauses = parseBlock(rawResult, "ROOT_CAUSES")
            val severity = parseBlock(rawResult, "SEVERITY")
            val plan = parseBlock(rawResult, "TREATMENT_PLAN")
            val routine = parseBlock(rawResult, "ROUTINE_PLAN")

            // Intelligence matcher logic for physical products
            val recommendedIds = mutableListOf<String>()
            
            val matchesDandruff = problemsStr.contains("Dandruff", ignoreCase = true) || problemsStr.contains("Itching", ignoreCase = true)
            val matchesHairFall = problemsStr.contains("Hair Fall", ignoreCase = true) || problemsStr.contains("Breakage", ignoreCase = true)
            val matchesDryness = scalpType.contains("Dry", ignoreCase = true) || problemsStr.contains("Frizz", ignoreCase = true)

            // Smart recommendation grouping
            if (matchesDandruff) {
                recommendedIds.add("shampoo_antifungal")
                recommendedIds.add("serum_scalp_dandruff")
            }
            if (matchesHairFall) {
                recommendedIds.add("shampoo_active_caffeine")
                recommendedIds.add("conditioner_keratin")
                recommendedIds.add("hair_oil_rosemary")
            }
            if (matchesDryness) {
                recommendedIds.add("shampoo_moisture")
                recommendedIds.add("conditioner_moisture")
                recommendedIds.add("hair_oil_argan")
            }
            if (recommendedIds.isEmpty()) {
                // Default fallback
                recommendedIds.add("shampoo_sensitive")
                recommendedIds.add("serum_frizz_shield")
            }

            // Create record
            val newDiagnosis = UserDiagnosis(
                hairType = hairType,
                scalpType = scalpType,
                problems = problemsStr,
                stressLevel = stressLevel,
                dietQuality = dietQuality,
                sleepQuality = sleepQuality,
                heatStyling = heatStyling,
                diagnosisSummary = summary.ifBlank { rawResult },
                rootCause = rootCauses.ifBlank { "Unspecified balance fluctuation due to physical factors." },
                severity = severity.ifBlank { "Moderate" },
                solutionPlan = plan.ifBlank { "Maintain standard washing cycles and avoid high thermal styling." } + "\n\n**Weekly Routine:**\n" + routine,
                recommendedProductIds = recommendedIds.joinToString(",")
            )

            val newId = diagnosisDao.insertDiagnosis(newDiagnosis)
            val savedRecord = newDiagnosis.copy(id = newId.toInt())
            
            setSelectedDiagnosis(savedRecord)
            _isGeneratingDiagnosis.value = false
            onSuccess()
        }
    }

    private fun parseBlock(text: String, tag: String): String {
        val startTag = "[$tag]"
        if (!text.contains(startTag)) return ""
        val split = text.split(startTag)
        if (split.size < 2) return ""
        val contentPart = split[1]
        // Cut at next tag occurrence
        val endIdx = contentPart.indexOf("[")
        return if (endIdx != -1) {
            contentPart.substring(0, endIdx).trim()
        } else {
            contentPart.trim()
        }
    }

    // --- Chat Logic ---
    fun sendChatMessage(messageText: String) {
        if (messageText.isBlank()) return
        viewModelScope.launch {
            // Save User message
            val userMsg = ChatMessage(role = "user", message = messageText)
            chatDao.insertMessage(userMsg)

            _isSendingChatMessage.value = true

            // Gather recent context (e.g. last 10 messages)
            val history = chatMessages.value.takeLast(10)

            // Call Gemini
            val aiResponse = GeminiService.getTrichologistAnswer(history, messageText)

            // Save Assistant response
            val assistantMsg = ChatMessage(role = "assistant", message = aiResponse)
            chatDao.insertMessage(assistantMsg)

            _isSendingChatMessage.value = false
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            chatDao.clearHistory()
        }
    }

    // --- Track Progress Activity ---
    fun addProgressLog(scalpFeel: Int, hairStrength: Int, breakageLevel: Int, notes: String) {
        viewModelScope.launch {
            val log = ProgressLog(
                scalpFeel = scalpFeel,
                hairStrength = hairStrength,
                breakageLevel = breakageLevel,
                notes = notes
            )
            progressDao.insertLog(log)
        }
    }
}
