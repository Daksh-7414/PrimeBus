package com.example.primebus.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.primebus.R

@Preview(showBackground = true)
@Composable
private fun TermsConditionsScreenPreview() {
    val navController = rememberNavController()
    TermsConditionsScreen(
        navController = navController
    )
}

@Composable
fun TermsConditionsScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F9FF))
    ) {
        PrivacyToolBar(
            "Terms & Conditions",
            onBackClick={navController.popBackStack()}
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = "PrimeBus Service Agreements",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Last updated: May 24, 2026. Please read these terms carefully before using the PrimeBus platform to ensure a safe and premium travel experience.",
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
            }
            item {
                ExpandableSection(
                    title = "Bookings",
                    R.drawable.seat_icon,
                    Color(0xFF00236F),
                    Color(0xFFDCE1FF)
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 8.dp,
                                end = 16.dp
                            )
                    ) {
                        Text(
                            text = "All bookings made through the PrimeBus mobile application or website are subject to availability. A booking is only confirmed once the full payment has been processed and a confirmation ticket (e-ticket) has been issued.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExpandableSection(
                    title = "Cancellations",
                    R.drawable.cancel_icon,
                    Color(0xFF712AE2),
                    Color(0xFFEADDFF)
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 8.dp,
                                end = 16.dp
                            )
                    ) {
                        Text(
                            text = "Our cancellation policy is designed to be fair to both travelers and our operators. Refunds are calculated based on the timing of the cancellation request.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "With 24 or more hours notice, a 90% refund is issued to the original payment method. For 12 to 24 hours notice, a 50% refund applies. Cancellations made less than 12 hours before departure are non-refundable.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExpandableSection(
                    title = "Liability",
                    icon = R.drawable.liability_icon,
                    Color(0xFF00236F),
                    Color(0xFFDCE1FF)
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 8.dp,
                                end = 16.dp
                            )
                    ) {
                        Text(
                            text = "PrimeBus acts as an intermediary connecting passengers with licensed operators, but the carrier bears direct responsibility for transport safety.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "We are not liable for delays due to traffic, weather, or mechanical issues, though we assist with alternatives; lost luggage compensation is capped at ₹200 per passenger.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExpandableSection(
                    title = "Payments",
                    icon = R.drawable.card_icon,
                    Color(0xFF712AE2),
                    Color(0xFFEADDFF)
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 8.dp,
                                end = 16.dp
                            )
                    ) {
                        Text(
                            text = "We accept all major credit cards, debit cards, and digital wallets. All transactions are processed through encrypted, secure gateways.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "We never store full credit card numbers on our servers. All transactions are processed through PCI-DSS compliant gateways.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExpandableSection(
                    title = "Prohibited Activity",
                    icon = R.drawable.no_entry_icon,
                    Color(0xFFBA1A1A),
                    Color(0xFFFFDAD6)
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 8.dp,
                                end = 16.dp
                            )
                    ) {
                        Text(
                            text = "To maintain a premium onboard environment, PrimeBus strictly prohibits smoking, vaping, alcohol consumption, disruptive behavior, and illegal substances.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Violators will be removed from the bus at the nearest safe location without any refund.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
            }
            item {
                val gradientBrush  = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0D247C), // dark blue
                        Color(0xFF6F3BD5)  // purple
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = gradientBrush,
                            shape = RoundedCornerShape(25.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {

                        Text(
                            text = "Ready to travel?",
                            fontSize = 17.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "By continuing to use our services, you acknowledge that you have read and agreed to the terms outlined above.",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Thin,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {},
                            modifier = Modifier
                                .height(45.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = "Contact Privacy Team",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00236F),
                                fontFamily = FontFamily(Font(R.font.inter)),
                            )
                        }
                    }
                }
            }
        }
    }
}





