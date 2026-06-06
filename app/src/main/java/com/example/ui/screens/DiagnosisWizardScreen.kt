package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisWizardScreen(
    isPro: Boolean,
    diagnosesCount: Int,
    isGenerating: Boolean,
    onNavigateBack: () -> Unit,
    onRunDiagnosis: (
        hairType: String,
        scalpType: String,
        problems: List<String>,
        stress: String,
        diet: String,
        sleep: String,
        styling: String
    ) -> Unit,
    onTriggerUpgrade: () -> Unit
) {
    // Limit check for Free users simulation
    val isLimitExceeded = !isPro && diagnosesCount >= 2

    var currentStep by remember { mutableStateOf(1) }

    // Intake Form States
    var selectedHairType by remember { mutableStateOf("Straight") }
    var selectedScalpType by remember { mutableStateOf("Normal") }
    val selectedProblems = remember { mutableStateListOf<String>() }

    var stressLevel by remember { mutableStateOf("Moderate") }
    var dietQuality by remember { mutableStateOf("Average") }
    var sleepQuality by remember { mutableStateOf("6 - 8 Hours") }
    var stylingUsage by remember { mutableStateOf("Rarely") }

    // Scientific Loading Phrases
    var loadingTipIndex by remember { mutableStateOf(0) }
    val loadingTips = listOf(
        "Measuring superficial scalp pH-acid mantle...",
        "Simulating glandular sebum secretion rates...",
        "Tracing biological impacts of cortisol hormones...",
        "Assembling 7-day botanical product layering matrices...",
        "Synthesizing customized organic sulfur repair molecules...",
        "Predicting tensile follicle elasticity indexes..."
    )

    LaunchedEffect(isGenerating) {
        if (isGenerating) {
            while (true) {
                delay(2200)
                loadingTipIndex = (loadingTipIndex + 1) % loadingTips.size
            }
        }
    }

    Scaffold(
        topBar = {
            OptInToEdgeToEdge {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "HAIRAI ASSESSMENT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BluePrimary,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = "Symptom Intake Wizard",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = CarbonDark
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CarbonDark)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        },
        containerColor = CreamBackground
    ) { paddingVals ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingVals)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CreamBackground,
                            WarmSurface
                        )
                    )
                )
        ) {
            if (isLimitExceeded) {
                LimitExceededCard(onTriggerUpgrade = onTriggerUpgrade)
            } else if (isGenerating) {
                // High-End Laboratory Simulation Loader View
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = BluePrimary,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(96.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = "Computing",
                            tint = BluePrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = "GENETICS ENGINE CALIBRATING",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedText,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.5.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    AnimatedContent(
                        targetState = loadingTips[loadingTipIndex],
                        transitionSpec = {
                            fadeIn() + slideInVertically { it } togetherWith fadeOut() + slideOutVertically { -it }
                        },
                        label = "Tips"
                    ) { tipText ->
                        Text(
                            text = tipText,
                            fontSize = 15.sp,
                            color = CarbonDark,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Dr. Sophia's automated chemistry engine is matching optimal botanical formulation surfactants for your direct bio-type report.",
                        fontSize = 12.sp,
                        color = MutedText,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    // Modern compact steps header indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DIAGNOSTIC MATRIX",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = BluePrimary,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "STAGE $currentStep OF 3",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CarbonDark,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Simulated Clean Progress Segment lines
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        repeat(3) { stepIndex ->
                            val active = currentStep >= stepIndex + 1
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(if (active) BluePrimary else LightGrayBorder)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Step Layout Renderers
                    when (currentStep) {
                        1 -> InteractiveProfilesSection(
                            selectedHairType = selectedHairType,
                            onHairSelected = { selectedHairType = it },
                            selectedScalpType = selectedScalpType,
                            onScalpSelected = { selectedScalpType = it }
                        )
                        2 -> ProfessionalSymptomsSection(
                            selectedProblems = selectedProblems,
                            onProblemToggled = { problem ->
                                if (selectedProblems.contains(problem)) {
                                    selectedProblems.remove(problem)
                                } else {
                                    selectedProblems.add(problem)
                                }
                            }
                        )
                        3 -> PremiumLifestyleSection(
                            stress = stressLevel,
                            onStressChanged = { stressLevel = it },
                            diet = dietQuality,
                            onDietChanged = { dietQuality = it },
                            sleep = sleepQuality,
                            onSleepChanged = { sleepQuality = it },
                            styling = stylingUsage,
                            onStylingChanged = { stylingUsage = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Flow Buttons Navigation row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (currentStep > 1) {
                            OutlinedButton(
                                onClick = { currentStep -= 1 },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, LightGrayBorder),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CarbonDark),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Back", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                        }

                        Button(
                            onClick = {
                                if (currentStep < 3) {
                                    currentStep += 1
                                } else {
                                    onRunDiagnosis(
                                        selectedHairType,
                                        selectedScalpType,
                                        selectedProblems.toList(),
                                        stressLevel,
                                        dietQuality,
                                        sleepQuality,
                                        stylingUsage
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(if (currentStep > 1) 1.5f else 1f)
                                .height(52.dp)
                                .testTag("wizard_next_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = if (currentStep == 3) "Synthesize Analytical Report" else "Proceed",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = if (currentStep == 3) Icons.Default.Science else Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Proceed",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InteractiveProfilesSection(
    selectedHairType: String,
    onHairSelected: (String) -> Unit,
    selectedScalpType: String,
    onScalpSelected: (String) -> Unit
) {
    val hairTypesInfo = listOf(
        Triple("Straight", "Flat & Sleek", Icons.Default.Waves),
        Triple("Wavy", "S-Shape Waves", Icons.Default.Waves),
        Triple("Curly", "Coiled Curls", Icons.Default.Waves),
        Triple("Coily", "Z-Pattern Spirals", Icons.Default.Waves)
    )

    val scalpTypesInfo = listOf(
        Triple("Oily", "Excessive Lipid Profile", Icons.Default.WaterDrop),
        Triple("Dry", "Moisture-Deficit Skin", Icons.Default.Opacity),
        Triple("Normal", "Hydrated pH-Balanced", Icons.Default.Spa),
        Triple("Sensitive", "Prone to Redness / Itch", Icons.Default.Warning)
    )

    Column {
        Text(
            text = "Biological Strand Form",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = CarbonDark
        )
        Text(
            text = "Select your hereditary hair growth form. Helps predict cortical oil propagation rates.",
            fontSize = 12.sp,
            color = MutedText,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Hair Grid 2x2 Layout
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            val chunkedHair = hairTypesInfo.chunked(2)
            chunkedHair.forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    rowItems.forEach { (type, description, icon) ->
                        val isSel = selectedHairType == type
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSel) PastelBlue else CleanWhite
                            ),
                            border = BorderStroke(
                                width = if (isSel) 1.5.dp else 1.dp,
                                color = if (isSel) BluePrimary else LightGrayBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onHairSelected(type) }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = type,
                                        tint = if (isSel) BluePrimary else MutedText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    if (isSel) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Active",
                                            tint = BluePrimary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = type, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CarbonDark)
                                Text(text = description, fontSize = 11.sp, color = MutedText)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Epithelial Scalp Profile",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = CarbonDark
        )
        Text(
            text = "Calibrates correct active surfactant densities and anti-fungal seeder properties.",
            fontSize = 12.sp,
            color = MutedText,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Scalp Grid 2x2 Layout
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            val chunkedScalp = scalpTypesInfo.chunked(2)
            chunkedScalp.forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    rowItems.forEach { (type, description, icon) ->
                        val isSel = selectedScalpType == type
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSel) PastelGreen else CleanWhite
                            ),
                            border = BorderStroke(
                                width = if (isSel) 1.5.dp else 1.dp,
                                color = if (isSel) GreenSecondary else LightGrayBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onScalpSelected(type) }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = type,
                                        tint = if (isSel) GreenSecondary else MutedText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    if (isSel) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Active",
                                            tint = GreenSecondary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = type, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CarbonDark)
                                Text(text = description, fontSize = 11.sp, color = MutedText)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfessionalSymptomsSection(
    selectedProblems: List<String>,
    onProblemToggled: (String) -> Unit
) {
    val symptomCatalog = listOf(
        SymptomItem("Hair Fall", "Experiencing bulb shedding or strand thinning.", Icons.Default.Warning, Color(0xFFFEE2E2), Color(0xFFEF4444)),
        SymptomItem("Dandruff", "White scaling flakes or oily yellowish build-up.", Icons.Default.Waves, Color(0xFFE0F2FE), Color(0xFF0EA5E9)),
        SymptomItem("Itching", "Red patches, constant tightness or scalp discomfort.", Icons.Default.Spa, Color(0xFFECFDF5), Color(0xFF10B981)),
        SymptomItem("Breakage", "Splitting ends, brittle shaft friction snapping.", Icons.Default.QueryStats, Color(0xFFFEF3C7), Color(0xFFD97706)),
        SymptomItem("Frizz", "High outer cuticle porosity and static reaction.", Icons.Default.Air, Color(0xFFF3E8FF), Color(0xFFA855F7))
    )

    Column {
        Text(
            text = "Active Clinical Symptoms",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = CarbonDark
        )
        Text(
            text = "Select all active anomalies. Dr. Sophia's recipe matrix activates unique molecules for each checked item.",
            fontSize = 12.sp,
            color = MutedText,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            symptomCatalog.forEach { sym ->
                val isChecked = selectedProblems.contains(sym.title)
                Card(
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, if (isChecked) BluePrimary else LightGrayBorder),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isChecked) PastelBlue.copy(alpha = 0.5f) else CleanWhite
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProblemToggled(sym.title) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(sym.iconBackground),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = sym.icon,
                                contentDescription = sym.title,
                                tint = sym.iconTint,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sym.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = CarbonDark
                            )
                            Text(
                                text = sym.desc,
                                fontSize = 12.sp,
                                color = MutedText
                            )
                        }

                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { onProblemToggled(sym.title) },
                            colors = CheckboxDefaults.colors(checkedColor = BluePrimary)
                        )
                    }
                }
            }
        }
    }
}

data class SymptomItem(
    val title: String,
    val desc: String,
    val icon: ImageVector,
    val iconBackground: Color,
    val iconTint: Color
)

@Composable
fun PremiumLifestyleSection(
    stress: String,
    onStressChanged: (String) -> Unit,
    diet: String,
    onDietChanged: (String) -> Unit,
    sleep: String,
    onSleepChanged: (String) -> Unit,
    styling: String,
    onStylingChanged: (String) -> Unit
) {
    Column {
        Text(
            text = "Environmental & Lifestyle Matrix",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = CarbonDark
        )
        Text(
            text = "Mechanical friction and physiological stresses alter normal follicle growth phases. Please declare your average week.",
            fontSize = 12.sp,
            color = MutedText,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        SleekOptionCardRow(
            label = "Physiological Stress Level",
            desc = "Cortisol tightens micro-vessels carrying nutrients to the bulbs.",
            options = listOf("Low", "Moderate", "High"),
            selected = stress,
            onSelected = onStressChanged,
            activeColor = BluePrimary
        )

        Spacer(modifier = Modifier.height(18.dp))

        SleekOptionCardRow(
            label = "Dietary Amino Nutrient Rate",
            desc = "Proteins, Iron, and Zinc trace elements seed healthy keratin synthesis.",
            options = listOf("Poor", "Average", "Excellent"),
            selected = diet,
            onSelected = onDietChanged,
            activeColor = GreenSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        SleekOptionCardRow(
            label = "Cellular Renewal Sleep Duration",
            desc = "Nocturnal cell regeneration matches Circadian rhythm cycle peaks.",
            options = listOf("< 6 Hours", "6 - 8 Hours", "8+ Hours"),
            selected = sleep,
            onSelected = onSleepChanged,
            activeColor = Color(0xFFA855F7)
        )

        Spacer(modifier = Modifier.height(18.dp))

        SleekOptionCardRow(
            label = "Mechanical / Thermal Stress",
            desc = "Hot irons, chemicals, or blow dryers fracture external hair cuticles.",
            options = listOf("Rarely", "Weekly", "Daily"),
            selected = styling,
            onSelected = onStylingChanged,
            activeColor = Color(0xFFF59E0B)
        )
    }
}

@Composable
fun SleekOptionCardRow(
    label: String,
    desc: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    activeColor: Color
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        border = BorderStroke(1.dp, LightGrayBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CarbonDark)
            Text(text = desc, fontSize = 11.sp, color = MutedText, modifier = Modifier.padding(bottom = 12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { option ->
                    val isSel = selected == option
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSel) activeColor.copy(alpha = 0.15f) else Color(0xFFF1F5F9))
                            .border(
                                1.5.dp,
                                if (isSel) activeColor else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { onSelected(option) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option,
                            fontSize = 12.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSel) activeColor else CarbonDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LimitExceededCard(onTriggerUpgrade: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(PastelAmber),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock",
                tint = Color(0xFFD97706),
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "AI Consultations Limit Reached",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = CarbonDark,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "You have generated your 2 complimentary reports as a Basic standard member. To calculate unlimited diagnostic reports and save historic recovery records, update to our Pro platform tier.",
            fontSize = 13.sp,
            color = MutedText,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onTriggerUpgrade,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Star, contentDescription = "Upgrade", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Unlock Clinician Pro (Simulated Key)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun OptInToEdgeToEdge(content: @Composable () -> Unit) {
    Box(modifier = Modifier.statusBarsPadding()) {
        content()
    }
}
