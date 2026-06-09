package com.example.primebus.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primebus.R
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
private fun OffersScreenPreview() {
    val navController = rememberNavController()
    OffersScreen(
        navController = navController
    )
}

@Composable
fun OffersScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFFF0F3F9))
    ) {
        PrivacyToolBar(
            title = "Offers & Rewards",
            onBackClick={navController.popBackStack()}
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                OffersTypes(
                    "BUS100",
                    "Get ₹100 off",
                    "on your first booking with us. No minimum spend.",
                    "Expires in 3 days"
                )
                Spacer(modifier = Modifier.height(10.dp))
                OffersTypes(
                    "UPI10",
                    "10% Cashback",
                    "Pay using any UPI app. Max cashback ₹50.",
                    "Expires in 3 days"
                )
                Spacer(modifier = Modifier.height(10.dp))
                OffersTypes(
                    "WEEKEND150",
                    "Flat ₹150 off",
                    "on all weekend routes - save on Sat & Sun trips",
                    "Expires in 3 days"
                )
            }
            item {
                InviteEarnCard(
                    couponCode = "PRIME50",
                    referralAmount = "₹50"
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun OffersTypes(
    coupon : String,
    title: String,
    subtitle: String,
    expiredLimit: String,
){
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))   // rounds the corners
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF0F4F9),RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = coupon,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        color = Color(0xFF1E293B),      // dark slate for better contrast
                        letterSpacing = 0.5.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    color = Color.DarkGray
                )
            }
        }
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = expiredLimit,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.inter)),
                color = Color.DarkGray
            )
            Button(
                onClick = { },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE8EEF9), // important: let the background brush show
                    contentColor = Color(0xFF00236F)
                ),
                contentPadding = PaddingValues(horizontal = 25.dp, vertical = 0.dp)
            )
            {
                Text(
                    text = "Apply",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteEarnCard(
    modifier: Modifier = Modifier,
    couponCode: String = "PRIME50",
    referralAmount: String = "₹50"
) {
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Invite & Earn",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Text(
                text = "Share your code and get $referralAmount on their first successful trip.",
                fontSize = 14.sp,
                color = Color(0xFF475569),

            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = couponCode,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.5.sp,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.padding(start = 12.dp)
                )
                IconButton(
                    onClick = {
                        val clip = ClipData.newPlainText("Coupon Code", couponCode)
                        clipboardManager.setPrimaryClip(clip)
                        Toast.makeText(context, "Copied: $couponCode", Toast.LENGTH_SHORT).show()
                    }
                ){
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = "Copy coupon code",
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(21.dp)
                    )
                }
            }
        }
    }
}