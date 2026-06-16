package com.example.primebus.features.profile.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.primebus.R
import com.example.primebus.ui.theme.gradientBrush

@Preview(showBackground = true)
@Composable
private fun PrivacyPolicyScreenPreview() {
    val navController = rememberNavController()
    PrivacyPolicyScreen(
        navController = navController
    )}

@Composable
fun PrivacyPolicyScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F9FF))
    ) {
        PrivacyToolBar(
            "Privacy Policy",
            onBackClick={navController.popBackStack()}
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Column(Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Your Privacy Matters",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "At PrimeBus, we believe in being fully open and honest. This policy explains how we protect your travel, your information, and your online activity.",
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        fontWeight = FontWeight.Thin,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
            }
            item {
                ExpandableSection(
                    title = "Data Collection",
                    R.drawable.data_collection_icon,
                    Color(0xFF00236F),
                    Color(0xFFDCE1FF)
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 16.dp)
                    ) {
                        Text(
                            text = "We collect information that you provide directly to us when you create an account, search for bus routes, or complete a booking. This includes Personal Identifiers, Travel Preferences, Technical Data.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                ExpandableSection(
                    title = "Payment Security",
                    R.drawable.security_icon,
                    Color(0xFF712AE2),
                    Color(0xFFEADDFF)
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 16.dp)
                    ) {
                        Text(
                            text = "Security is the bedrock of PrimeBus. We utilize bank-grade encryption to ensure your financial transactions remain private.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
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
                    title = "User Rights",
                    icon = R.drawable.user_right_icon,
                    Color(0xFF00236F),
                    Color(0xFFDCE1FF)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp,end = 16.dp)
                    ) {
                        Text(
                            text = "User rights give you full control over your personal data request permanent deletion of your profile and download a complete copy of your travel history.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "You can also correct inaccuracies through the Account portal and opt out of personalized tracking with a single toggle.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                ExpandableSection(
                    title = "Cookies & Tracking",
                    icon = R.drawable.cookies_icon,
                    Color(0xFF712AE2),
                    Color(0xFFEADDFF)
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp,end = 16.dp)
                    ) {
                        Text(
                            text = "We use essential cookies to remember your login session and booking preferences. Third-party analytical cookies help us understand how users interact with our \"Editorial\" layout to improve usability.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "We never store full credit card numbers on our servers. All transactions are processed through PCI-DSS compliant gateways.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 0.dp)
                        .background(brush = gradientBrush, shape = RoundedCornerShape(25.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Still have questions?",
                            fontSize = 17.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Our dedicated privacy team is available to help you understand your data rights and our security measures.",
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
                Text(
                    text = "©2026 PrimeBus. All rights reserved.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFB6BDC8),
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyToolBar(
    title: String,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier.size(26.dp)
            )
        }
        Text(
            text = title,
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
    }
}

@Composable
fun ExpandableSection(
    title: String,
    icon: Int,
    iconcolor: Color,
    tint: Color,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false,
    content: @Composable (() -> Unit)
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Column(modifier = modifier.clip(RoundedCornerShape(20.dp))
        .background(Color.White)){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(tint, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Icon",
                    tint = iconcolor,
                    modifier = Modifier.size(21.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter)),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand"
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)) {
                content()
            }
        }
    }
}


