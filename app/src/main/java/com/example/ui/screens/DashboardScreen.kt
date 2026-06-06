package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HairProduct
import com.example.data.ProgressLog
import com.example.data.UserDiagnosis
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    currentReport: UserDiagnosis?,
    allDiagnoses: List<UserDiagnosis>,
    recommendedProducts: List<HairProduct>,
    progressLogs: List<ProgressLog>,
    onSelectReport: (UserDiagnosis) -> Unit,
    onAddLog: (feel: Int, strength: Int, breakage: Int, notes: String) -> Unit,
    onStartScan: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showLogDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // Dashboard Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "HAIRAI INTEL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BluePrimary,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Clinical Dashboard",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = CarbonDark
                    )
                }

                IconButton(
                    onClick = onStartScan,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BluePrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Live Scan",
                        tint = CleanWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (allDiagnoses.isEmpty()) {
                DashboardEmptyState(onStartScan = onStartScan)
            } else {
                // Timeline List
                Text(
                    text = "Report Timeline",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MutedText,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(allDiagnoses) { diagnosis ->
                        val isSelected = currentReport?.id == diagnosis.id
                        val dateString = java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault())
                            .format(java.util.Date(diagnosis.timestamp))

                        val mainProblem = diagnosis.problems.split(",").firstOrNull() ?: "General"

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) BluePrimary else CleanWhite)
                                .border(
                                    1.dp,
                                    if (isSelected) BluePrimary else LightGrayBorder,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onSelectReport(diagnosis) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.CalendarMonth,
                                    contentDescription = "Status",
                                    tint = if (isSelected) CleanWhite else MutedText,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = dateString,
                                        color = if (isSelected) CleanWhite else CarbonDark,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = mainProblem,
                                        color = if (isSelected) CleanWhite.copy(alpha = 0.8f) else MutedText,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }

                currentReport?.let { report ->
                    // HIGH END HEALTH GAUGE CIRCLE (PREMIUM CRAFT TOUCH)
                    FollicleIndexScoreCard(report = report)

                    Spacer(modifier = Modifier.height(20.dp))

                    DiagnosisMainReportCard(report = report)

                    Spacer(modifier = Modifier.height(20.dp))

                    TreatmentPlanDetailCard(report = report)

                    Spacer(modifier = Modifier.height(28.dp))

                    // Recommendations Title
                    Text(
                        text = "Prescription Formulations",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = CarbonDark,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Therapeutic active compounds designed for your diagnostic profiles.",
                        fontSize = 12.sp,
                        color = MutedText,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    if (recommendedProducts.isEmpty()) {
                        Text(
                            text = "No customized prescription products map to this report.",
                            fontSize = 13.sp,
                            color = MutedText
                        )
                    } else {
                        recommendedProducts.forEach { product ->
                            HairProductDisplayCard(product = product)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Beautiful interactive canvas graphical trend charts
                ProgressTrackerSection(
                    progressLogs = progressLogs,
                    onTriggerNewLog = { showLogDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }

    if (showLogDialog) {
        ProgressLoggingDialog(
            onDismiss = { showLogDialog = false },
            onSave = { feel, strength, breakage, notes ->
                onAddLog(feel, strength, breakage, notes)
                showLogDialog = false
            }
        )
    }
}

@Composable
fun DashboardEmptyState(onStartScan: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        border = BorderStroke(1.dp, LightGrayBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PastelBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = "Empty Core",
                    tint = BluePrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Assemble First Diagnosis",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = CarbonDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your customized hair science profile is waiting. Run our 3-step biological assessment wizard so Dr. Sophia's recipe engine can calibrate products customized to your scalp skin.",
                fontSize = 13.sp,
                color = MutedText,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onStartScan,
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.FlashOn, contentDescription = "Launch", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Start Intake Consultation", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun FollicleIndexScoreCard(report: UserDiagnosis) {
    val score = when {
        report.severity.contains("severe", ignoreCase = true) -> 38
        report.severity.contains("moderate", ignoreCase = true) -> 62
        else -> 84
    }

    val scoreColor = when {
        score < 45 -> Color(0xFFEF4444)
        score < 75 -> Color(0xFFF59E0B)
        else -> Color(0xFF10B981)
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        border = BorderStroke(1.dp, LightGrayBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radial Gauge drawing
            Box(
                modifier = Modifier.size(90.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Gray underlying circle
                    drawCircle(
                        color = Color(0xFFF1F5F9),
                        radius = size.minDimension / 2.3f,
                        style = Stroke(width = 16f)
                    )

                    // Active progress arc
                    val sweepAngle = 360f * (score / 100f)
                    drawArc(
                        color = scoreColor,
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        size = Size(size.width * 0.86f, size.height * 0.86f),
                        topLeft = Offset(size.width * 0.07f, size.height * 0.07f),
                        style = Stroke(width = 16f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$score",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = CarbonDark
                    )
                    Text(
                        text = "INDEX",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedText,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(scoreColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = when {
                            score < 45 -> "CRITICAL DYSBIOSIS"
                            score < 75 -> "MODERATE SENSITIVITY"
                            else -> "OPTIMAL PROTECTED"
                        },
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = scoreColor,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Follicle Health Index",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = CarbonDark
                )

                Text(
                    text = "Composite scalp metabolic status drawn directly from biological indicators.",
                    fontSize = 11.sp,
                    color = MutedText,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
fun DiagnosisMainReportCard(report: UserDiagnosis) {
    val dateString = java.text.SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.getDefault())
        .format(java.util.Date(report.timestamp))

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        border = BorderStroke(1.dp, LightGrayBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "METABOLIC LAB PROFILE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )

                val isSevere = report.severity.contains("severe", ignoreCase = true)
                val isMild = report.severity.contains("mild", ignoreCase = true)
                val badgeColor = if (isSevere) Color(0xFFFEE2E2) else if (isMild) PastelGreen else PastelAmber
                val textColor = if (isSevere) Color(0xFF991B1B) else if (isMild) Color(0xFF065F46) else Color(0xFF92400E)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeColor)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Severity: ${report.severity.uppercase()}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = textColor,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Grid of physical factors
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BioStatColumn(label = "Fiber Shape", value = report.hairType, icon = Icons.Outlined.Waves)
                BioStatColumn(label = "Scalp Tissue", value = report.scalpType, icon = Icons.Outlined.Eco)
                BioStatColumn(label = "Styling Heat", value = report.heatStyling, icon = Icons.Outlined.Thermostat)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = LightGrayBorder)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Symptom Diagnosis",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CarbonDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = report.diagnosisSummary,
                fontSize = 12.sp,
                color = MutedText,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Trigger Causes Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Attention",
                        tint = Color(0xFFD97706),
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Hereditary & Environmental Triggers",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = CarbonDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = report.rootCause,
                            color = MutedText,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Calibrated: $dateString",
                fontSize = 10.sp,
                color = MutedText,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BioStatColumn(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = label, tint = MutedText, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, fontSize = 10.sp, color = MutedText)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CarbonDark)
    }
}

@Composable
fun TreatmentPlanDetailCard(report: UserDiagnosis) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        border = BorderStroke(1.dp, LightGrayBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PastelGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Plan",
                        tint = GreenSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "7-Day Therapeutic Routine",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = CarbonDark
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = report.solutionPlan,
                fontSize = 12.sp,
                color = MutedText,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun HairProductDisplayCard(product: HairProduct) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        border = BorderStroke(1.dp, LightGrayBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when (product.imageUrlPlaceholder) {
                                    "Blue" -> PastelBlue
                                    "Green" -> PastelGreen
                                    "Amber" -> PastelAmber
                                    else -> PastelTeal
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = product.category.uppercase(),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = when (product.imageUrlPlaceholder) {
                                "Blue" -> BluePrimary
                                "Green" -> GreenSecondary
                                "Amber" -> Color(0xFFB45309)
                                else -> TealAccent
                            },
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = CarbonDark
                    )
                    Text(
                        text = "Laboratory: ${product.brand}",
                        fontSize = 11.sp,
                        color = MutedText
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$${product.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = BluePrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rationale
            Text(
                text = "Scientific rational matching:",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = CarbonDark
            )
            Text(
                text = product.reasonForSelection,
                fontSize = 12.sp,
                color = MutedText,
                lineHeight = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Directions
            Text(
                text = "Intensified clinical application:",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = CarbonDark
            )
            Text(
                text = product.usageInstructions,
                fontSize = 12.sp,
                color = MutedText,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = LightGrayBorder)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = "Period",
                        tint = GreenSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Apply ${product.frequencyPerWeek} times weekly",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenSecondary
                    )
                }

                var boughtDialog by remember { mutableStateOf(false) }
                OutlinedButton(
                    onClick = { boughtDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BluePrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("Acquire Formula", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
                }

                if (boughtDialog) {
                    AlertDialog(
                        onDismissRequest = { boughtDialog = false },
                        title = { Text("Stripe Checkout Portal") },
                        text = { Text("Adding ${product.name} to checkout pipeline. Simulated Stripe integration matches local sandbox balance key.") },
                        confirmButton = {
                            Button(onClick = { boughtDialog = false }) {
                                Text("Acknowledge Delivery")
                            }
                        }
                    )
                }
            }
        }
    }
}

// Progress Track charts
@Composable
fun ProgressTrackerSection(
    progressLogs: List<ProgressLog>,
    onTriggerNewLog: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        border = BorderStroke(1.dp, LightGrayBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timeline,
                        contentDescription = "Bio Graph",
                        tint = BluePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Recovery Bio-Graphs",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = CarbonDark
                    )
                }

                Button(
                    onClick = onTriggerNewLog,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("Self Audit", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // CUSTOM SPLINE GRAPH OF SELF-AUDITS (PREMIUM DESIGN HIGHLIGHT)
            if (progressLogs.isNotEmpty()) {
                Text(
                    text = "METALS / FOLLICLE METRICS RECOVERY PROGRESSION",
                    fontSize = 9.sp,
                    color = MutedText,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F172A))
                        .padding(14.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val pointsCount = progressLogs.size
                        val pointsVisible = progressLogs.reversed().take(6)

                        // Base grid lines
                        val verticalSpacing = size.height / 4f
                        for (i in 0..4) {
                            val lineY = verticalSpacing * i
                            drawLine(
                                color = Color(0xFF334155).copy(alpha = 0.5f),
                                start = Offset(0f, lineY),
                                end = Offset(size.width, lineY),
                                strokeWidth = 1f
                            )
                        }

                        if (pointsVisible.size > 1) {
                            val spacingX = size.width / (pointsVisible.size - 1)
                            val scalpFeelPoints = mutableListOf<Offset>()
                            val strengthPoints = mutableListOf<Offset>()

                            pointsVisible.forEachIndexed { idx, log ->
                                val x = spacingX * idx
                                // invert because height coordinate starts from 0 at the top
                                val scalpY = size.height - ((log.scalpFeel - 1) / 4f) * (size.height * 0.8f) - (size.height * 0.1f)
                                val strengthY = size.height - ((log.hairStrength - 1) / 4f) * (size.height * 0.8f) - (size.height * 0.1f)

                                scalpFeelPoints.add(Offset(x, scalpY))
                                strengthPoints.add(Offset(x, strengthY))

                                // Draw circular joint dots
                                drawCircle(color = Color(0xFF10B981), radius = 6f, center = Offset(x, scalpY))
                                drawCircle(color = Color(0xFF3B82F6), radius = 6f, center = Offset(x, strengthY))
                            }

                            // Draw Spline / Connected lines
                            val pathScalp = Path()
                            val pathStrength = Path()

                            scalpFeelPoints.forEachIndexed { idx, point ->
                                if (idx == 0) {
                                    pathScalp.moveTo(point.x, point.y)
                                } else {
                                    val prev = scalpFeelPoints[idx - 1]
                                    pathScalp.quadraticTo(prev.x, prev.y, point.x, point.y)
                                }
                            }

                            strengthPoints.forEachIndexed { idx, point ->
                                if (idx == 0) {
                                    pathStrength.moveTo(point.x, point.y)
                                } else {
                                    val prev = strengthPoints[idx - 1]
                                    pathStrength.quadraticTo(prev.x, prev.y, point.x, point.y)
                                }
                            }

                            drawPath(path = pathScalp, color = Color(0xFF10B981), style = Stroke(width = 4f))
                            drawPath(path = pathStrength, color = Color(0xFF3B82F6), style = Stroke(width = 4f))
                        } else {
                            // Single point
                            val log = pointsVisible.first()
                            val scalpY = size.height - ((log.scalpFeel - 1) / 4f) * (size.height * 0.8f) - (size.height * 0.1f)
                            drawCircle(color = Color(0xFF10B981), radius = 10f, center = Offset(size.width / 2, scalpY))
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(100.dp)).background(Color(0xFF10B981)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Scalp Tissue Integrity", fontSize = 9.sp, color = MutedText, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.width(18.dp))

                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(100.dp)).background(Color(0xFF3B82F6)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Strand Elastic Tensile Strength", fontSize = 9.sp, color = MutedText, fontFamily = FontFamily.Monospace)
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            if (progressLogs.isEmpty()) {
                Text(
                    text = "Self-audited recovery parameters are empty. Record daily scores for scalp comfort and hair toughness to construct responsive healing spline charts.",
                    color = MutedText,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
            } else {
                Text(
                    text = "All Historical Self Audits",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CarbonDark,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                progressLogs.forEach { log ->
                    val logDate = java.text.SimpleDateFormat("MMM dd, hh:mm a", java.util.Locale.getDefault())
                        .format(java.util.Date(log.timestamp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .background(Color(0xFFF1F5F9).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .border(1.dp, LightGrayBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = logDate, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BluePrimary, fontFamily = FontFamily.Monospace)
                            if (log.notes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(text = "\"${log.notes}\"", fontSize = 12.sp, color = CarbonDark)
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            ScoreIndicator(value = log.scalpFeel, label = "Scalp")
                            ScoreIndicator(value = log.hairStrength, label = "Strength")
                            ScoreIndicator(value = log.breakageLevel, label = "Shedding")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreIndicator(value: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 8.sp, color = MutedText, fontFamily = FontFamily.Monospace)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (value >= 4) PastelGreen else if (value >= 3) PastelAmber else Color(0xFFFEE2E2))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "$value/5",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (value >= 4) Color(0xFF065F46) else if (value >= 3) Color(0xFF92400E) else Color(0xFF991B1B)
            )
        }
    }
}

@Composable
fun ProgressLoggingDialog(
    onDismiss: () -> Unit,
    onSave: (feel: Int, strength: Int, breakage: Int, notes: String) -> Unit
) {
    var scalpFeel by remember { mutableStateOf(3) }
    var hairStrength by remember { mutableStateOf(3) }
    var breakageLevel by remember { mutableStateOf(3) }
    var diaryNotes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Clinical Self-Audit Entry",
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Log values matching your immediate tactile scalp comfort and hair resistance. Calibration scale ranges from 1 to 5.",
                    fontSize = 11.sp,
                    color = MutedText,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                SliderRatingField(label = "Scalp Skin Comfort / Hydration", value = scalpFeel, onValueChange = { scalpFeel = it })
                Spacer(modifier = Modifier.height(10.dp))
                SliderRatingField(label = "Fiber Tensile Resistance", value = hairStrength, onValueChange = { hairStrength = it })
                Spacer(modifier = Modifier.height(10.dp))
                SliderRatingField(label = "Follicle Falling Reduction", value = breakageLevel, onValueChange = { breakageLevel = it })

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = diaryNotes,
                    onValueChange = { diaryNotes = it },
                    label = { Text("Observations (redness patches, tightness feelings, etc.)") },
                    textStyle = TextStyle(fontSize = 13.sp),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(scalpFeel, hairStrength, breakageLevel, diaryNotes) },
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Log Recovery Point")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SliderRatingField(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CarbonDark)
            Text(text = "$value/5", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BluePrimary, fontFamily = FontFamily.Monospace)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 1f..5f,
            steps = 3,
            colors = SliderDefaults.colors(
                thumbColor = BluePrimary,
                activeTrackColor = BluePrimary,
                inactiveTrackColor = LightGrayBorder
            )
        )
    }
}
