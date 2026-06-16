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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.primebus.R
import com.example.primebus.ui.theme.gradientBrush

@Preview(showBackground = true)
@Composable
private fun HelpCenterScreenPreview() {
    val navController = rememberNavController()
    HelpCenterScreen(
        navController = navController
    )
}

@Preview
@Composable
private fun HelpScreenToolBarPreview() {
    HelpScreenToolBar(
        {}
    )
}

@Composable
fun HelpScreenToolBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Help Center",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Refresh",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun HelpCenterScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F3F9))
    ) {
        HelpScreenToolBar({})
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFEFEFE))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center
                )
                {
                    Text(
                        text = "How can we help you?",
                        fontSize = 21.sp,
                        color = Color(0xFF012470),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Find quick answers for booking,\ncancellation, refund, and \npayment issues.",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 3,
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
            }
            item {
                Text(
                    text = "Quick Categories",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    Modifier.fillMaxWidth()
                    ,horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickHelpOption(
                        "Booking Issues",
                        R.drawable.ticket_icon,
                        modifier = Modifier.weight(1f)
                    )
                    QuickHelpOption(
                        "Cancellation",
                        R.drawable.calendar_cancel_icon,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickHelpOption(
                        "Refund Status",
                        R.drawable.payment_icon,
                        modifier = Modifier.weight(1f)
                    )
                    QuickHelpOption(
                        "Payment Failed",
                        R.drawable.payment_fail_icon,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickHelpOption(
                        title = "Seat Issues",
                        icon = R.drawable.person_seat_icon,
                        modifier = Modifier.weight(1f)
                    )
                    QuickHelpOption(
                        title = "Bus Delayed",
                        icon = R.drawable.clock_icon,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                Text(
                    text = "Popular FAQs",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
                Spacer(modifier = Modifier.height(12.dp))
                FaqSection(
                    title = "How do I track my bus in real-time?",
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 2.dp, end = 26.dp)
                    ) {
                        Text(
                            text = "You can track your bus by going to the \"Tickets\" section in the app. Select your active journey and tap on \"Live Track\". You will see the exact GPS location of your bus along with expected arrival times.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                FaqSection(
                    title = "When will I receive my refund for a cancelled ticket?",
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 2.dp, end = 16.dp)
                    ) {
                        Text(
                            text = "Refunds are typically processed within 5-7 business days depending on your bank. You can monitor the status in the 'Refund Status' category above.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                FaqSection(
                    title = "Can I change my seat after booking?",
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 2.dp, end = 16.dp,)
                    ) {
                        Text(
                            text = "Seat changes are permitted up to 12 hours before departure, subject to availability. Please visit the Manage Booking section to modify your details.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Can't find what you're looking for?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF444651),
                        fontFamily = FontFamily(Font(R.font.inter)),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = gradientBrush,
                                shape = RoundedCornerShape(16.dp) // match button shape for clip
                            ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
                    )
                    {
                        Icon(
                            painter = painterResource(R.drawable.contact_support_icon),
                            contentDescription = "Icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Contact Support",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.inter))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun QuickHelpOption(
    title: String,
    icon: Int,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp)
    ){
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color(0xFFDCE1FF),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Icon",
                tint = Color(0xFF00236F),
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )


    }
}

@Composable
fun FaqSection(
    title: String,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false,
    content: @Composable (() -> Unit)
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Column(modifier = modifier
        .clip(RoundedCornerShape(20.dp))
        .background(Color.White)){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
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
