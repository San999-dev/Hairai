package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SubscriptionScreen(
    isPro: Boolean,
    onTogglePro: () -> Unit
) {
    val scrollState = rememberScrollState()

    var isPaying by remember { mutableStateOf(false) }

    // Card details input states
    var cardNumber by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCvc by remember { mutableStateOf("") }

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

            // Page Header
            Column {
                Text(
                    text = "MEMBERSHIP DECK",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Clinician Pro Plan",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = CarbonDark
                )
                Text(
                    text = "Activate unlimited clinical hair diagnostics & persistence chat logs",
                    fontSize = 12.sp,
                    color = MutedText
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isPro) {
                ProActiveSuccessView(onClearPro = onTogglePro)
            } else {
                ClinicianTierBenefitsCard()

                Spacer(modifier = Modifier.height(24.dp))

                // HIGH FIDELITY CREDIT CARD MOCKUP (PREMIUM CRAFT TOUCH)
                Text(
                    text = "SECURE BILLING CHANNEL",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CarbonDark,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                InteractiveCreditCardMockup(
                    num = cardNumber,
                    expiry = cardExpiry,
                    cvc = cardCvc
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Billing Box Form
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    border = BorderStroke(1.dp, LightGrayBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Stripe Secured",
                                tint = GreenSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Stripe Gateway Integration Sandbox",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CarbonDark
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Selected Price Row Info
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Monthly Pro Access Plan",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CarbonDark
                                )
                                Text(
                                    text = "Automated local sandbox clearance key.",
                                    fontSize = 11.sp,
                                    color = MutedText
                                )
                            }
                            Text(
                                text = "$14.99/mo",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = BluePrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Card Input Fields
                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = {
                                if (it.length <= 16) cardNumber = it.filter { c -> c.isDigit() }
                            },
                            label = { Text("Visa or Mastercard Number", fontSize = 12.sp) },
                            leadingIcon = { Icon(imageVector = Icons.Default.CreditCard, contentDescription = "Card No", tint = MutedText) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = LightGrayBorder,
                                focusedBorderColor = BluePrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("card_number_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = cardExpiry,
                                onValueChange = {
                                    if (it.length <= 4) cardExpiry = it.filter { c -> c.isDigit() }
                                },
                                label = { Text("Expiry (MM/YY)", fontSize = 11.sp) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = LightGrayBorder,
                                    focusedBorderColor = BluePrimary
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("card_expiry_input")
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            OutlinedTextField(
                                value = cardCvc,
                                onValueChange = {
                                    if (it.length <= 3) cardCvc = it.filter { c -> c.isDigit() }
                                },
                                label = { Text("Security CVC", fontSize = 11.sp) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = LightGrayBorder,
                                    focusedBorderColor = BluePrimary
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("card_cvc_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        val inputsValid = cardNumber.length == 16 && cardExpiry.length == 4 && cardCvc.length == 3

                        if (isPaying) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(color = BluePrimary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Securing sandbox connection with Stripe API...",
                                    fontSize = 11.sp,
                                    color = MutedText
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    isPaying = true
                                    cardNumber = ""
                                    cardExpiry = ""
                                    cardCvc = ""
                                    onTogglePro()
                                    isPaying = false
                                },
                                enabled = inputsValid,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BluePrimary,
                                    disabledContainerColor = Color(0xFFE2E8F0)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("submit_upgrade_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = "Verified Pay", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Pay $14.99 & Authenticate Pro License", fontWeight = FontWeight.Black, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun InteractiveCreditCardMockup(num: String, expiry: String, cvc: String) {
    // Format card number nicely
    val formattedNum = remember(num) {
        val padded = num.padEnd(16, '•')
        padded.chunked(4).joinToString("  ")
    }

    // Format expiry date nicely
    val formattedExpiry = remember(expiry) {
        if (expiry.length >= 2) {
            "${expiry.substring(0, 2)}/${expiry.substring(2).padEnd(2, '•')}"
        } else {
            expiry.padEnd(2, '•') + "/••"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1E293B), // Dark Carbon primary slate
                        Color(0xFF0F172A),
                        Color(0xFF334155)
                    )
                )
            )
            .padding(24.dp)
    ) {
        // Aesthetic glowing circle backgrounds
        Box(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
                .background(Color(0xFF3B82F6).copy(alpha = 0.15f), RoundedCornerShape(100.dp))
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Standard Microchip & Brand logo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Microchip drawn
                Box(
                    modifier = Modifier
                        .size(34.dp, 26.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFBBF24)) // gold microchip
                )

                Text(
                    text = "KERASENSE PLATINUM",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White.copy(alpha = 0.6f),
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
            }

            // Card Number digits
            Text(
                text = formattedNum,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = CleanWhite,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.5.sp,
                modifier = Modifier.fillMaxWidth()
            )

            // Cardholder and expiry details row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "CARDHOLDER",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.5f),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "HAIR RECOVERY MEMBER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CleanWhite,
                        fontFamily = FontFamily.SansSerif
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "VAL THRU",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.5f),
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = formattedExpiry,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CleanWhite,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "SEC CVC",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.5f),
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = cvc.padEnd(3, '•'),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CleanWhite,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClinicianTierBenefitsCard() {
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
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PastelAmber),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Membership Advantage",
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Pro Membership Perks",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = CarbonDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            BenefitRow(title = "Unlimited Molecular Diagnoses reports", desc = "Execute diagnostic evaluations and formulate customized hair conditions reports as frequently as symptoms change.")
            BenefitRow(title = "Dr. Sophia (Premium Chat Access)", desc = "Certified trichology assistant references your historical diagnosis records synchronously within the chat pipeline.")
            BenefitRow(title = "Reveals High Precision Formulas", desc = "Identifies target product brands (zinc PCA, botanical oils, anti-fungals, and peptides) with exact weekly guidelines.")
            BenefitRow(title = "Local historical self-audit graphs", desc = "Record comfort ratings daily to construct responsive recovery charts.")
        }
    }
}

@Composable
fun BenefitRow(title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Membership Benefit Icon",
            tint = GreenSecondary,
            modifier = Modifier
                .size(16.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CarbonDark)
            Text(text = desc, fontSize = 11.sp, color = MutedText, lineHeight = 15.sp)
        }
    }
}

@Composable
fun ProActiveSuccessView(onClearPro: () -> Unit) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        border = BorderStroke(2.dp, GreenSecondary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(PastelGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Clinician License Clear",
                    tint = GreenSecondary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Pro System Active",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = CarbonDark
            )
            Text(
                text = "SANDBOX CLINICIAN SUBSCRIPTION SIGNED",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GreenSecondary,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your client account has successfully unlocked unlimited generative health reports, precision active compound catalog suggestions, and historical progression trends synchronized dynamically.",
                fontSize = 13.sp,
                color = MutedText,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider(color = LightGrayBorder)
            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onClearPro) {
                Text(
                    text = "Developer Action: Reset Membership License",
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
