package com.example.data

import android.util.Log
import com.example.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"
    private const val MODEL = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateHairReport(
        hairType: String,
        scalpType: String,
        problems: String,
        stressLevel: String,
        dietQuality: String,
        sleepQuality: String,
        heatStyling: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "API Key is missing or placeholder!")
            return@withContext getMockDiagnosisReport(hairType, scalpType, problems)
        }

        val systemInstruction = """
            You are a certified senior trichologist and expert hair care professional.
            Analyze the user's hair profile:
            - Hair Type: $hairType
            - Scalp Type: $scalpType
            - Problems: $problems
            - Stress Level: $stressLevel
            - Diet Quality: $dietQuality
            - Sleep Quality: $sleepQuality
            - Heat Styling Usage: $heatStyling

            Provide a highly detailed, professional, structured commercial hair diagnosis report.
            Avoid definitive medical guarantees but give precise trichological insights.
            Ensure your output strictly uses the following exact format with headers:
            
            [DIAGNOSIS_SUMMARY]
            Provide a 3-4 sentence comprehensive diagnosis of their overall scalp and hair condition.
            
            [ROOT_CAUSES]
            List 2-3 primary biological or lifestyle root causes contributing to their symptoms. Format as bullet points.
            
            [SEVERITY]
            State the severity (Mild, Moderate, or Severe) and briefly explain why.
            
            [TREATMENT_PLAN]
            Provide a systematic, actionable step-by-step treatment plan (e.g., wash temperature, drying tips, oiling habits).
            
            [ROUTINE_PLAN]
            Describe a simple weekly calendar routine plan (e.g. Wash Days, Serums, Hydration Days).
        """.trimIndent()

        val prompt = "Generate my personalized hair report based on my scalp conditions and lifestyle."

        try {
            val jsonBody = buildRequestBody(prompt, systemInstruction)
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(jsonBody.toRequestBody("application/json".toMediaType()))
                .build()

            val response: Response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseString = response.body?.string() ?: ""
                val text = extractTextFromResponse(responseString)
                if (text.isNotBlank()) {
                    return@withContext text
                }
            } else {
                Log.e(TAG, "API call failed: ${response.code} : ${response.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Gemini diagnosis call", e)
        }

        return@withContext getMockDiagnosisReport(hairType, scalpType, problems)
    }

    suspend fun getTrichologistAnswer(
        history: List<ChatMessage>,
        latestUserMessage: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "API Key is missing or placeholder for chat!")
            return@withContext getMockChatResponse(latestUserMessage)
        }

        val systemInstruction = """
            You are an elite, certified trichologist and dermatologist-advisor in hair care.
            You give structured, clear, and reassuring answers to users' hair health queries.
            1. Act like a certified clinic professional.
            2. Never give absolute medical diagnostic claims; suggest consulting a local dermatologist for severe, bleeding, or extensive hair loss issues.
            3. Answer their question clearly with bullet points where helpful.
            4. Suggest specific hair care types (e.g. "sulfate-free hydrating shampoo", "caffeine hair oil", "zinc-pyrithione cleansers") based on their needs.
        """.trimIndent()

        try {
            // Build contents conversation history array
            val contentsArray = JSONArray()
            
            // Add previous history
            for (msg in history) {
                val roleStr = if (msg.role == "user") "user" else "model"
                val partObj = JSONObject().put("text", msg.message)
                val contentObj = JSONObject()
                    .put("role", roleStr)
                    .put("parts", JSONArray().put(partObj))
                contentsArray.put(contentObj)
            }

            // Add latest user query
            val latestPart = JSONObject().put("text", latestUserMessage)
            val latestContent = JSONObject()
                .put("role", "user")
                .put("parts", JSONArray().put(latestPart))
            contentsArray.put(latestContent)

            val rootJson = JSONObject()
                .put("contents", contentsArray)
                .put("systemInstruction", JSONObject().put("parts", JSONArray().put(JSONObject().put("text", systemInstruction))))

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(rootJson.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response: Response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseString = response.body?.string() ?: ""
                val text = extractTextFromResponse(responseString)
                if (text.isNotBlank()) {
                    return@withContext text
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Gemini chat call", e)
        }

        return@withContext getMockChatResponse(latestUserMessage)
    }

    private fun buildRequestBody(prompt: String, systemInstruction: String): String {
        val partsObj = JSONObject().put("text", prompt)
        val contentsObj = JSONObject()
            .put("role", "user")
            .put("parts", JSONArray().put(partsObj))

        val sysTextObj = JSONObject().put("text", systemInstruction)
        val systemInstructionObj = JSONObject()
            .put("role", "system")
            .put("parts", JSONArray().put(sysTextObj))

        val rootJson = JSONObject()
            .put("contents", JSONArray().put(contentsObj))
            .put("systemInstruction", systemInstructionObj)

        return rootJson.toString()
    }

    private fun extractTextFromResponse(jsonResponse: String): String {
        try {
            val root = JSONObject(jsonResponse)
            val candidates = root.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val contentObj = firstCandidate.optJSONObject("content")
                val parts = contentObj?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    return parts.getJSONObject(0).optString("text", "")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "JSON parsing error for response: $jsonResponse", e)
        }
        return ""
    }

    private fun getMockDiagnosisReport(hairType: String, scalpType: String, problems: String): String {
        val isDandruff = problems.contains("Dandruff", ignoreCase = true)
        val isHairFall = problems.contains("Hair Fall", ignoreCase = true)
        val isDryness = scalpType.contains("Dry", ignoreCase = true) || problems.contains("Breakage", ignoreCase = true)
        
        val severity = if (isHairFall && isDandruff) "Moderate to Severe" else "Mild to Moderate"
        
        return """
            [DIAGNOSIS_SUMMARY]
            Your scalp is exhibiting patterns of ${scalpType.lowercase()} conditions combined with $problems on a $hairType base. The lipid protection barrier is compromised, causing active epidermal cells to flake or follicle roots to weaken premature to normal lifecycle phases.
            
            [ROOT_CAUSES]
            - Accelerated epidermal cycle and yeast overgrowth triggered by pH changes and excessive $scalpType activity.
            - Nutritional or oxygen flow limitations to the dermal papilla from heightened stress levels and lack of recovery sleep.
            - Moisture loss in the outer hair cuticles resulting from repeated heat styling tool contact.
            
            [SEVERITY]
            $severity. While your scalp requires immediate targeted intervention, the hair bulb structures are healthy, meaning these conditions are highly reversible with proper regular care.
            
            [TREATMENT_PLAN]
            1. Transition to wash cycles with lukewarm to cold water only to keep natural oils balanced.
            2. Never rub wet hair vigorously; wrap in a soft microfiber towel and let air dry by 80% before blow drying on cool setting.
            3. Apply specialized serums directly to clean part lines for focused penetration.
            
            [ROUTINE_PLAN]
            - Monday: Cleanse with therapeutic pH shampoo. Run moisture conditioner through mid-lengths.
            - Wednesday: Leave-in hydration spray and sleep on silk pillowcase.
            - Friday: Apply rosemary follicle massage oil for 30 minutes before warm rinse.
            - Sunday: Exfoliating scalp serum followed by a protective lightweight conditioner sealing.
        """.trimIndent()
    }

    private fun getMockChatResponse(query: String): String {
        val lower = query.lowercase()
        return when {
            lower.contains("falling") || lower.contains("hair fall") || lower.contains("hair loss") -> {
                """
                    I understand how concerning hair fall can be. As a certified trichologist, let's break down the primary factors:
                    
                    **Common Causes of Hair Fall:**
                    - **Nutritional Deficiencies**: Low ferritin (iron), Vitamin D3, or B12 can stall the anagen (growth) phase.
                    - **Stress (Telogen Effluvium)**: High cortisol shocks follicles into the resting phase.
                    - **Scalp Environment**: Dandruff or oily sebum clog follicles, triggering inflammation.
                    
                    **Actionable Recommendations:**
                    1. Use a **Caffeine-based or Biotin/Keratin shampoo** to encourage follicle circulation.
                    2. Massage your scalp with 4-5 drops of **Rosemary oil** mixed with pumpkin seed oil 3 times per week. Studies prove rosemary oil boosts hair density similarly to Minoxidil 2%.
                    3. Ensure a high-protein diet rich in eggs, spinach, nuts, and berries.
                    
                    *Note: If hair shedding is sudden, extensive, or reveals round bald patches, please consult a local dermatologist for diagnostic blood panel evaluations.*
                """.trimIndent()
            }
            lower.contains("dandruff") || lower.contains("flake") || lower.contains("itching") || lower.contains("itch") -> {
                """
                    Scalp flaking and itching are highly manageable with the right topical chemistry.
                    
                    **Understanding the Flakes:**
                    - **Oily Scalp flaking (Seborrheic Dermatitis)**: Thick, oily, yellowish flakes caused by Malassezia yeast feeding on sebum.
                    - **Dry Scalp shedding**: Tiny, dry white flakes from moisture-barrier damage.
                    
                    **Actionable Scalp Routine:**
                    1. For **oily flakes**: Use an **Antifungal Shampoo containing Ketoconazole 1% or Zinc Pyrithione** 2-3 times a week. Let the lather rest on the scalp for 3 full minutes.
                    2. For **dry scalp**: Use a **sulfate-free mild shampoo** followed by an exfoliating Salicylic Acid serum to strip dead skin cells cleanly.
                    3. Hydrate! Drink 3L of water daily and minimize hot showers.
                """.trimIndent()
            }
            else -> {
                """
                    Excellent query. As your hair care assistant, I recommend maintaining a structured approach to hair health:
                    
                    **The Core Columns of Trichological Health:**
                    1. **Scalp Hygiene**: Keep your roots clean and pH balanced. Sebum and sweat can breed yeasts.
                    2. **Cuticle Nutrition**: Conditioners and serums seal moisture and guard against environmental damage.
                    3. **Follicle Stimulation**: Regular massage increases vital micro-flow of oxygen and amino acids.
                    
                    Please tell me more about your specific hair type or scalp conditions so I can give you an tailored answer!
                """.trimIndent()
            }
        }
    }
}
