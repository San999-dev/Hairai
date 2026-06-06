package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.HairCareViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: HairCareViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: HairCareViewModel) {
    // Session state
    var loggedInEmail by remember { mutableStateOf<String?>(null) }

    // Screen swapper states
    var currentScreen by remember { mutableStateOf("landing") }

    // Collect Room Live Flows
    val diagnoses by viewModel.allDiagnoses.collectAsStateWithLifecycle()
    val recommendedProducts by viewModel.recommendedProductsForCurrent.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val progressLogs by viewModel.progressLogs.collectAsStateWithLifecycle()
    
    // UI indicator states
    val isPro by viewModel.isProUser.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGeneratingDiagnosis.collectAsStateWithLifecycle()
    val isChatSending by viewModel.isSendingChatMessage.collectAsStateWithLifecycle()
    val currentReport by viewModel.currentDiagnosisReport.collectAsStateWithLifecycle()

    if (loggedInEmail == null) {
        // Authenticate Block Screen
        AuthScreen(onLoginSuccess = { email ->
            loggedInEmail = email
        })
    } else {
        // Secured Protected Dashboard Area Layout
        Scaffold(
            bottomBar = {
                // Do not show bottom tabs when filling the scientific wizard intake
                if (currentScreen != "wizard") {
                    NavigationBar(
                        containerColor = Color.White,
                        tonalElevation = 8.dp,
                        modifier = Modifier.testTag("app_navigation_bar")
                    ) {
                        NavigationBarItem(
                            selected = currentScreen == "landing",
                            onClick = { currentScreen = "landing" },
                            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Welcome", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = Color.Gray
                            )
                        )

                        NavigationBarItem(
                            selected = currentScreen == "dashboard",
                            onClick = { currentScreen = "dashboard" },
                            icon = { Icon(imageVector = Icons.Default.Dashboard, contentDescription = "Dashboard") },
                            label = { Text("Reports", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = Color.Gray
                            )
                        )

                        NavigationBarItem(
                            selected = currentScreen == "chat",
                            onClick = { currentScreen = "chat" },
                            icon = { Icon(imageVector = Icons.Default.Forum, contentDescription = "AI Chat") },
                            label = { Text("Expert Chat", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = Color.Gray
                            )
                        )

                        NavigationBarItem(
                            selected = currentScreen == "subscription",
                            onClick = { currentScreen = "subscription" },
                            icon = { Icon(imageVector = Icons.Default.CardMembership, contentDescription = "Stripe billing") },
                            label = { Text("Membership", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = Color.Gray
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    "landing" -> LandingScreen(
                        isProUser = isPro,
                        onStartScan = { currentScreen = "wizard" },
                        onViewReports = { currentScreen = "dashboard" },
                        onStartChat = { currentScreen = "chat" },
                        onTogglePro = { viewModel.toggleProUser() }
                    )
                    
                    "wizard" -> DiagnosisWizardScreen(
                        isPro = isPro,
                        diagnosesCount = diagnoses.size,
                        isGenerating = isGenerating,
                        onNavigateBack = { currentScreen = "landing" },
                        onRunDiagnosis = { hairType, scalpType, problems, stress, diet, sleep, styling ->
                            viewModel.runDiagnosis(
                                hairType,
                                scalpType,
                                problems,
                                stress,
                                diet,
                                sleep,
                                styling,
                                onSuccess = {
                                    currentScreen = "dashboard"
                                }
                            )
                        },
                        onTriggerUpgrade = {
                            currentScreen = "subscription"
                        }
                    )

                    "dashboard" -> DashboardScreen(
                        currentReport = currentReport,
                        allDiagnoses = diagnoses,
                        recommendedProducts = recommendedProducts,
                        progressLogs = progressLogs,
                        onSelectReport = { diagnosis ->
                            viewModel.setSelectedDiagnosis(diagnosis)
                        },
                        onAddLog = { feel, strength, breakage, notes ->
                            viewModel.addProgressLog(feel, strength, breakage, notes)
                        },
                        onStartScan = { currentScreen = "wizard" }
                    )

                    "chat" -> ChatScreen(
                        messages = chatMessages,
                        isSending = isChatSending,
                        onSendMessage = { query ->
                            viewModel.sendChatMessage(query)
                        },
                        onClearChat = {
                            viewModel.clearChatHistory()
                        }
                    )

                    "subscription" -> SubscriptionScreen(
                        isPro = isPro,
                        onTogglePro = {
                            viewModel.toggleProUser()
                        }
                    )
                }
            }
        }
    }
}
