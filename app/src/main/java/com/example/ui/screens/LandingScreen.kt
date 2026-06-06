package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle

@Composable
fun LandingScreen(
    isProUser: Boolean,
    onStartScan: () -> Unit,
    onViewReports: () -> Unit,
    onStartChat: () -> Unit,
    onTogglePro: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Interactive Scalp Emulator State
    var simulationMoisture by remember { mutableStateOf(55f) } // 10% to 100%
    var simulationPh by remember { mutableStateOf(5.5f) } // 3.5 to 8.5

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CreamBackground,
                        CreamBackground,
                        WarmSurface
                    )
                )
            )
    ) {
        // Decorative background grids matching premium organic style
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSpacing = 60.dp.toPx()
            val gridColor = Sage.copy(alpha = 0.12f)
            var currentX = 0f
            while (currentX < size.width) {
                drawLine(
                    color = gridColor,
                    start = Offset(currentX, 0f),
                    end = Offset(currentX, size.height),
                    strokeWidth = 0.8f
                )
                currentX += gridSpacing
            }
            var currentY = 0f
            while (currentY < size.height) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, currentY),
                    end = Offset(size.width, currentY),
                    strokeWidth = 0.8f
                )
                currentY += gridSpacing
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Science Header Badge Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(GoldLight)
                    .border(1.dp, GoldTexturizer.copy(alpha = 0.3f), RoundedCornerShape(100.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Science,
                        contentDescription = "Clinical Diagnostic",
                        tint = DeepBrown,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "✨ AI-POWERED HAIR SCIENCE",
                        color = DeepBrown,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Premium Brand Title Text using brand annotated typeface styles
            val annotatedTitle = buildAnnotatedString {
                append("Your Personal\n")
                withStyle(style = SpanStyle(
                    color = SageDark,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontWeight = FontWeight.Black
                )) {
                    append("Hair Specialist")
                }
                append("\nIs Here")
            }

            Text(
                text = annotatedTitle,
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                color = CharcoalDark,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif,
                lineHeight = 44.sp,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Get a clinical-grade hair & scalp diagnosis in 2 minutes. Personalized routines, product recommendations, and expert guidance — powered by AI.",
                fontSize = 14.sp,
                color = CharcoalDark,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Premium Dual CTA Hero Panels
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, WarmSurface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SageLight.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Memory,
                                contentDescription = "Gemini Diagnoses",
                                tint = SageDark
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Intelligent Hair Scan",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = CharcoalDark
                            )
                            Text(
                                text = "Instant botanical formula calculation",
                                fontSize = 12.sp,
                                color = MutedSlate
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Start Free Hair Test button
                    Button(
                        onClick = onStartScan,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SageDark,
                            contentColor = CleanWhite
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("start_scan_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Launch", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Start Free Hair Test",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Ask hair expert button
                    OutlinedButton(
                        onClick = onStartChat,
                        border = BorderStroke(1.dp, Sage),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SageDark),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("consult_expert_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Forum, contentDescription = "Consult Expert", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Ask Hair Expert Sophia",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Beautiful clinical stats counts row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "50K+",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = SageDark
                    )
                    Text(
                        text = "Diagnoses Formed",
                        fontSize = 11.sp,
                        color = MutedSlate,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(modifier = Modifier.width(1.dp).height(24.dp).background(WarmSurface))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "98.4%",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldTexturizer
                    )
                    Text(
                        text = "Clinician Accuracy",
                        fontSize = 11.sp,
                        color = MutedSlate,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(modifier = Modifier.width(1.dp).height(24.dp).background(WarmSurface))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "350+",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Sage
                    )
                    Text(
                        text = "Actives Cataloged",
                        fontSize = 11.sp,
                        color = MutedSlate,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // CUSTOM INTERACTIVE EMULATOR SECTION (SUPER PREMIUM CRAFT TOUCH)
            Text(
                text = "Scalp Barrier Bio-Emulator",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CarbonDark,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Adjust the biological parameters below to simulate scalp skin tissue integrity and trigger warnings before initiating your live scan.",
                fontSize = 12.sp,
                color = MutedText,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            )

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                border = BorderStroke(1.dp, LightGrayBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Two slider rows
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Scalp Moisture / Sebum", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CarbonDark)
                            Text("${simulationMoisture.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BluePrimary, fontFamily = FontFamily.Monospace)
                        }
                        Slider(
                            value = simulationMoisture,
                            onValueChange = { simulationMoisture = it },
                            valueRange = 10f..100f,
                            colors = SliderDefaults.colors(
                                thumbColor = BluePrimary,
                                activeTrackColor = BluePrimary,
                                inactiveTrackColor = LightGrayBorder
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Dermal pH Level", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CarbonDark)
                            Text(String.format("%.1f pH", simulationPh), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenSecondary, fontFamily = FontFamily.Monospace)
                        }
                        Slider(
                            value = simulationPh,
                            onValueChange = { simulationPh = it },
                            valueRange = 3.5f..8.5f,
                            colors = SliderDefaults.colors(
                                thumbColor = GreenSecondary,
                                activeTrackColor = GreenSecondary,
                                inactiveTrackColor = LightGrayBorder
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bio-Emulator Canvas rendering
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF0F172A)) // High contrast aesthetic background
                            .padding(12.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val isFlaky = simulationMoisture < 35f
                            val isOptimal = simulationPh in 4.7f..5.8f && simulationMoisture >= 45f
                            val isHighlyAlkaline = simulationPh > 6.5f

                            // Draw skin barrier baseline
                            val skinY = size.height * 0.75f
                            drawLine(
                                color = Color(0xFF475569),
                                start = Offset(0f, skinY),
                                end = Offset(size.width, skinY),
                                strokeWidth = 3f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                            )

                            // Draw follicle stems (3 hairs)
                            val hairXPositions = listOf(size.width * 0.25f, size.width * 0.5f, size.width * 0.75f)
                            hairXPositions.forEachIndexed { i, hX ->
                                // Follicle bulb root
                                drawCircle(
                                    color = if (isOptimal) Color(0xFF10B981) else if (isFlaky) Color(0xFFEF4444) else Color(0xFFEAB308),
                                    radius = 8f,
                                    center = Offset(hX, skinY)
                                )

                                // Hair shaft curving upwards
                                val startPoints = Offset(hX, skinY)
                                val controlPoint1 = Offset(hX - 15f + i * 5f, skinY * 0.5f)
                                val endPoint = Offset(hX - 25f + i * 8f, skinY * 0.1f)

                                // Draw curve
                                val path = androidx.compose.ui.graphics.Path().apply {
                                    moveTo(startPoints.x, startPoints.y)
                                    quadraticTo(controlPoint1.x, controlPoint1.y, endPoint.x, endPoint.y)
                                }
                                drawPath(
                                    path = path,
                                    color = if (isOptimal) Color.White else Color.White.copy(alpha = 0.6f),
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                                        width = if (isOptimal) 4f else 2.5f
                                    )
                                )

                                // Sebum droplets if moisture/sebum is high
                                if (simulationMoisture > 70f) {
                                    drawCircle(
                                        color = Color(0xFFEAB308).copy(alpha = 0.8f),
                                        radius = 5f,
                                        center = Offset(hX - 5f, skinY - 12f)
                                    )
                                }
                            }

                            // Dry flakes falling
                            if (isFlaky) {
                                drawCircle(color = Color.White.copy(alpha = 0.9f), radius = 3f, center = Offset(size.width * 0.3f, skinY - 30f))
                                drawCircle(color = Color.White.copy(alpha = 0.8f), radius = 2f, center = Offset(size.width * 0.65f, skinY - 45f))
                                drawCircle(color = Color.White.copy(alpha = 0.7f), radius = 3f, center = Offset(size.width * 0.8f, skinY - 20f))
                            }

                            // Malassezia Yeast spores for highly alkaline / sweaty scalp
                            if (isHighlyAlkaline) {
                                drawCircle(color = Color(0xFFEF4444).copy(alpha = 0.7f), radius = 4f, center = Offset(size.width * 0.15f, skinY - 5f))
                                drawCircle(color = Color(0xFFEF4444).copy(alpha = 0.8f), radius = 3f, center = Offset(size.width * 0.45f, skinY - 8f))
                                drawCircle(color = Color(0xFFEF4444).copy(alpha = 0.7f), radius = 4f, center = Offset(size.width * 0.85f, skinY - 4f))
                            }
                        }

                        // Status Badge in the simulator view
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    when {
                                        simulationPh in 4.7f..5.8f && simulationMoisture in 40f..70f -> Color(0xFF065F46)
                                        simulationMoisture < 35f -> Color(0xFF991B1B)
                                        simulationPh > 6.5f -> Color(0xFF92400E)
                                        else -> Color(0xFF1E293B)
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = when {
                                    simulationPh in 4.7f..5.8f && simulationMoisture in 40f..70f -> "Optimal Epithelium"
                                    simulationMoisture < 35f -> "Severe Dehydration FLAKING"
                                    simulationPh > 6.5f -> "Microbial Sebum ALKALINITY"
                                    simulationMoisture > 70f -> "Excessive Seborrheic EXCRETION"
                                    else -> "Mild Dysbiosis Alert"
                                }.uppercase(),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = CleanWhite,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Simulated diagnosis textual result
                    Text(
                        text = when {
                            simulationPh in 4.7f..5.8f && simulationMoisture in 40f..70f ->
                                "Microbiome balance looks perfectly protective. Acidic mantle repels standard fungal flaking yeast."
                            simulationMoisture < 35f ->
                                "Warning: Moisture deficit will dry out the cuticle cortex, triggering scaling dandruff, itchiness, and strand breakage."
                            simulationPh > 6.5f ->
                                "Warning: High alkalinity neutralizes natural protective barrier, exciting fungal yeast breeding & root inflammation."
                            simulationMoisture > 70f ->
                                "Notice: Highly hyperactive lipid excretion. Weekly washing frequency must be structured to prevent pore blockage."
                            else ->
                                "Notice: Imbalanced scalp profile. Standard botanical ingredients seeder values will need re-calibration."
                        },
                        fontSize = 11.sp,
                        color = MutedText,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // 3 Pillar Cards Section
            Text(
                text = "Specialized Diagnosis Pillars",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CarbonDark,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            FeatureCard(
                icon = Icons.Outlined.AssignmentInd,
                title = "Triple-Phase Diagnostic Engine",
                desc = "We record precise strand geometry, scaling behaviors, thermal heat styling, and dietary amino acids inside our clinical data setup.",
                color = PastelBlue,
                tint = BluePrimary
            )

            FeatureCard(
                icon = Icons.Outlined.Autorenew,
                title = "Bio-Prescriptive Formulation Matching",
                desc = "Maps customized prescription products targeting specific scalp conditions (zinc PCA, coal Tar, rosemary oil, caffeine) matching your DNA.",
                color = PastelGreen,
                tint = GreenSecondary
            )

            FeatureCard(
                icon = Icons.Outlined.Face,
                title = "Conversational Health Consultant",
                desc = "Chat anytime with Dr. Sophia, our certified trichology specialist system, capable of referencing your custom report history directly.",
                color = PastelAmber,
                tint = Color(0xFFD97706)
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Beautiful SaaS Tiers Section
            Text(
                text = "Premium Access Pricing",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CarbonDark,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Choose your plan. Lifetime simulation access keys initialized locally.",
                fontSize = 12.sp,
                color = MutedText,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            PricingCard(
                title = "SaaS Basic Plan",
                price = "$0",
                features = listOf(
                    "Clinical intake walkthrough wizard",
                    "Symptom matching catalog",
                    "Limit: Up to 2 AI-generated Diagnoses reports",
                    "Generic category recommendations (e.g. Shampoo)"
                ),
                isActive = !isProUser,
                isRecommended = false,
                buttonText = "Current Standing Tier",
                onSelected = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            PricingCard(
                title = "SaaS Clinician Pro Plan",
                price = "$14.99 / mo",
                features = listOf(
                    "Unlimited Generative AI Trichology Reports",
                    "Reveals exact medical-grade brands formulations",
                    "Persistent Conversational Memory inside Chat with Dr. Sophia",
                    "Dynamic recovery graphs tracking scalp & breaking",
                    "Dermatological referral warnings index triggers"
                ),
                isActive = isProUser,
                isRecommended = true,
                buttonText = if (isProUser) "Clinician Pro Active (Sandbox)" else "Upgrade to Clinician Pro",
                onSelected = onTogglePro
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun FeatureCard(
    icon: ImageVector,
    title: String,
    desc: String,
    color: Color,
    tint: Color
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        border = BorderStroke(1.dp, LightGrayBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    color = CarbonDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    color = MutedText,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
fun PricingCard(
    title: String,
    price: String,
    features: List<String>,
    isActive: Boolean,
    isRecommended: Boolean,
    buttonText: String,
    onSelected: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        border = BorderStroke(
            width = if (isRecommended) 2.dp else 1.dp,
            color = if (isRecommended) BluePrimary else LightGrayBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            if (isRecommended) {
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .clip(RoundedCornerShape(100.dp))
                        .background(BluePrimary)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "MOST POPULAR",
                        color = CleanWhite,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = CarbonDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = price,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Black,
                    color = CarbonDark
                )
                if (price != "$0") {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "/ monthly model",
                        fontSize = 11.sp,
                        color = MutedText,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = LightGrayBorder)
            Spacer(modifier = Modifier.height(16.dp))

            features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        tint = GreenSecondary,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feature,
                        fontSize = 12.sp,
                        color = CarbonDark,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSelected,
                enabled = !isActive || title.contains("Pro"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRecommended) BluePrimary else Color(0xFFF1F5F9),
                    contentColor = if (isRecommended) CleanWhite else CarbonDark
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = buttonText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
