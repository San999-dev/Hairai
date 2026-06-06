package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessage
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    isSending: Boolean,
    onSendMessage: (String) -> Unit,
    onClearChat: () -> Unit
) {
    val listState = rememberLazyListState()
    var userMessageText by remember { mutableStateOf("") }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LaunchedEffect(isSending) {
        if (isSending && messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val suggestedPrompts = listOf(
        "Why is my hair falling?",
        "Which shampoo is best?",
        "How to cure dry flakes?"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar (Hospital / Apothecary theme)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CleanWhite)
                    .border(BorderStroke(1.dp, LightGrayBorder))
                    .padding(horizontal = 24.dp, vertical = 14.dp)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PastelGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Clinician Doctor",
                            tint = GreenSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Dr. Sophia, Trichologist",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = CarbonDark
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(GreenSecondary)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "TRICHOLOGY CONSULTANT ACTIVE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenSecondary,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    IconButton(
                        onClick = onClearChat,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF1F5F9))
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Reset Chat Memory",
                            tint = CarbonDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Message list
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                if (messages.isEmpty()) {
                    item {
                        ChatIntroPanel(onPromptSelected = { prompt ->
                            onSendMessage(prompt)
                        })
                    }
                } else {
                    items(messages) { msg ->
                        ChatBubbleField(message = msg)
                    }
                }

                if (isSending) {
                    item {
                        TypingIndicatorBubble()
                    }
                }
            }

            // Quick Suggestions Chips Row (Static pre-seeder helper queries)
            if (messages.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    suggestedPrompts.forEach { prompt ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(CleanWhite)
                                .border(1.dp, LightGrayBorder, RoundedCornerShape(100.dp))
                                .clickable { onSendMessage(prompt) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ArrowOutward,
                                    contentDescription = "Ask",
                                    tint = BluePrimary,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = prompt,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BluePrimary
                                )
                            }
                        }
                    }
                }
            }

            // Message Composer Field Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CleanWhite)
                    .border(BorderStroke(1.dp, LightGrayBorder))
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = userMessageText,
                        onValueChange = { userMessageText = it },
                        placeholder = { Text("Ask Dr. Sophia about your scalp...", fontSize = 13.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF1F5F9),
                            focusedContainerColor = Color(0xFFF1F5F9),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = BluePrimary
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_text")
                            .heightIn(max = 120.dp),
                        textStyle = TextStyle(fontSize = 14.sp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (userMessageText.isNotBlank()) {
                                onSendMessage(userMessageText)
                                userMessageText = ""
                            }
                        },
                        enabled = userMessageText.isNotBlank() && !isSending,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(
                                if (userMessageText.isNotBlank() && !isSending) BluePrimary else Color(0xFFE2E8F0)
                            )
                            .testTag("chat_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Message",
                            tint = CleanWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatIntroPanel(onPromptSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(PastelGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Chat",
                tint = GreenSecondary,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Clinical Consultation Hub",
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = CarbonDark
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Speak live with Dr. Sophia, our conversational AI trichologist system. She evaluates active cortical damage indices and lifestyle stress hormone impacts inside your live timeline.",
            fontSize = 13.sp,
            color = MutedText,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "LAUNCH DIAGNOSTIC CHAT TOPICS",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = BluePrimary,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(10.dp))

        val introQuestions = listOf(
            Pair("“Why is my hair falling?”", "Assess shedding phases & root cortisol impact"),
            Pair("“Which shampoo is best?”", "Custom active formulations & compatibility"),
            Pair("“How to stop dandruff and scaling?”", "Soothes itching yeasts & regulates sebum")
        )

        introQuestions.forEach { item ->
            val cleanStr = item.first.replace("“", "").replace("”", "")
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, LightGrayBorder),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onPromptSelected(cleanStr) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Query",
                        tint = GreenSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = item.first,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CarbonDark
                        )
                        Text(
                            text = item.second,
                            fontSize = 11.sp,
                            color = MutedText
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubbleField(message: ChatMessage) {
    val isUser = message.role == "user"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) BluePrimary else CleanWhite
            ),
            border = if (isUser) null else BorderStroke(1.dp, LightGrayBorder),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = message.message,
                    fontSize = 13.sp,
                    color = if (isUser) CleanWhite else CarbonDark,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        val timeString = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
            .format(java.util.Date(message.timestamp))
        Text(
            text = timeString,
            fontSize = 9.sp,
            color = MutedText,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun TypingIndicatorBubble() {
    var tick by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(400)
            tick = (tick + 1) % 4
        }
    }

    val dots = ".".repeat(tick) + " ".repeat(3 - tick)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = CleanWhite),
            border = BorderStroke(1.dp, LightGrayBorder),
            modifier = Modifier.width(96.dp)
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Dr Sophia$dots",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenSecondary,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
