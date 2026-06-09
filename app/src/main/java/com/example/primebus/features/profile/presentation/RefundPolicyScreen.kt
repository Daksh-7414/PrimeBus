package com.example.primebus.features.profile.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember
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

@Preview(showBackground = true)
@Composable
private fun RefundPolicyScreenPreview() {
    val navController = rememberNavController()

    RefundPolicyScreen(
        navController = navController
    )}

data class RefundMethod(
    val icon: Int,
    val title: String,
    val subtitle: String
)

data class PolicyNote(
    val icon: Int,
    val text: String
)

@Composable
fun RefundPolicyScreen(navController: NavHostController) {

    val refundMethods = remember {
        listOf(
            RefundMethod(R.drawable.wallet_icon, "PrimeBus Wallet", "2-4 hours"),
            RefundMethod(R.drawable.payment_icon, "UPI / Instant Pay", "24 hours"),
            RefundMethod(R.drawable.card_icon, "Credit / Debit Card", "5-7 days"),
            RefundMethod(R.drawable.net_banking_icon, "Net Banking", "3-5 days")
        )
    }

    val notes = remember {
        listOf(
            PolicyNote(
                R.drawable.about_icon,
                "Convenience fees charged at the time of booking are non-refundable even in case of full cancellation."
            ),
            PolicyNote(
                R.drawable.policies_icon,
                "Specific operator policies may override general rules for luxury or special event coaches."
            ),
            PolicyNote(
                R.drawable.wallet_icon,
                "Refunds will always be credited back to the original source of payment used during checkout."
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F9FF))
    ) {
        PrivacyToolBar(
            title = "Refund Policy",
            onBackClick={navController.popBackStack()}
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                RefundHeaderCard()
            }

            item {
                RefundTimelineSection(refundMethods)
            }

            item {
                ImportantNotesSection(notes)
            }
        }
    }
}

@Composable
fun RefundHeaderCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(
                        Color(0xFFE8EBF3),
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.refund_policy_icon),
                    contentDescription = null,
                    tint = Color(0xFF00236F),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Refund & Cancellation Rules",
                fontSize = 17.sp,
                color = Color(0xFF012470),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.inter))
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "We believe in flexibility. Our policy is designed to protect your travel plans while ensuring the sustainability of our premium bus network.",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.inter))
        )
    }
}

@Composable
fun RefundTimelineSection(methods: List<RefundMethod>) {
    Column {
        Text(
            text = "Refund Processing Timeline",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            methods.forEach { method ->
                RefundTypes(
                    icon = method.icon,
                    title = method.title,
                    subtitle = method.subtitle
                )
            }
        }
    }
}

@Composable
fun ImportantNotesSection(notes: List<PolicyNote>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFEFF2F9))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Important Notes",
            fontSize = 16.sp,
            color = Color(0xFF012470),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )

        notes.forEach { note ->
            PolicyNoteRow(note)
        }
    }
    Spacer(modifier = Modifier.height(4.dp))

}

@Composable
fun PolicyNoteRow(note: PolicyNote) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(note.icon),
            contentDescription = null,
            tint = Color(0xFF00236F),
            modifier = Modifier
                .padding(top = 5.dp)
                .size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = note.text,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            fontFamily = FontFamily(Font(R.font.inter))
        )
    }
}

@Composable
fun RefundTypes(
    title: String,
    subtitle: String,
    icon: Int,
){
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))   // rounds the corners
            .background(Color(0xFFEEF4FC))            // white background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Icon",
                    tint = Color(0xFF712AE2),
                    modifier = Modifier.size(21.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    color = Color.DarkGray
                )
            }
        }
    }
}