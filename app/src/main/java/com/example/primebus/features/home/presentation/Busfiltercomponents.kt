package com.example.primebus.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primebus.BusFilterState
import com.example.primebus.BusSortOption
import com.example.primebus.BusTypeFilter
import com.example.primebus.R
import com.example.primebus.ui.theme.PrimeBusTheme
import com.example.primebus.ui.theme.gradientBrush

@Composable
fun BusFilterChipRow(
    filterState: BusFilterState,
    onTypeToggle: (BusTypeFilter) -> Unit,
    onSortChange: (BusSortOption) -> Unit,
    onMinRatingChange: (Double?) -> Unit,
    onClearAll: () -> Unit
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    var showSortSheet by remember { mutableStateOf(false) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F8FE)),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        item {
            FilterEntryChip(
                activeCount = filterState.activeFilterCount,
                onClick = { showFilterSheet = true }
            )
        }

        item {
            FilterChip(
                label = "Sort by",
                selected = filterState.sortOption != BusSortOption.NONE,
                onClick = { showSortSheet = true }
            )
        }

        item {
            FilterChip(
                label = "AC Sleeper",
                selected = filterState.selectedTypes.contains(BusTypeFilter.AC_SLEEPER),
                onClick = { onTypeToggle(BusTypeFilter.AC_SLEEPER) }
            )
        }

        item {
            FilterChip(
                label = "AC Non Sleeper",
                selected = filterState.selectedTypes.contains(BusTypeFilter.AC_NON_SLEEPER),
                onClick = { onTypeToggle(BusTypeFilter.AC_NON_SLEEPER) }
            )
        }

        if (filterState.isAnyFilterActive) {
            item {
                ClearAllChip(onClick = onClearAll)
            }
        }
    }

    if (showFilterSheet) {
        BusFilterBottomSheet(
            filterState = filterState,
            onTypeToggle = onTypeToggle,
            onMinRatingChange = onMinRatingChange,
            onClearAll = onClearAll,
            onDismiss = { showFilterSheet = false }
        )
    }

    if (showSortSheet) {
        BusSortBottomSheet(
            currentSort = filterState.sortOption,
            onSortSelected = { option ->
                onSortChange(option)
                showSortSheet = false
            },
            onDismiss = { showSortSheet = false }
        )
    }
}

@Composable
private fun FilterEntryChip(
    activeCount: Int,
    onClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(gradientBrush)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {

        Text(
            text = if (activeCount > 0) "Filter ($activeCount)" else "Filter",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        Spacer(modifier = Modifier.width(6.dp))

        Icon(
            imageVector = Icons.Rounded.FilterList,
            contentDescription = "Filter",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Color(0xFFE3E8FF) else Color.White
    val textColor = if (selected) Color(0xFF3D3BC4) else Color(0xFF1F2937)
    val borderColor = if (selected) Color(0xFF3D3BC4) else Color(0xFFE2E8F0)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .border(0.5.dp, borderColor, RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
    }
}

@Composable
private fun ClearAllChip(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFFFF0F0))
            .border(1.dp, Color(0xFFFFC7C7), RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = "Clear filters",
            tint = Color(0xFFD61F1F),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Clear",
            color = Color(0xFFD61F1F),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusFilterBottomSheet(
    filterState: BusFilterState,
    onTypeToggle: (BusTypeFilter) -> Unit,
    onMinRatingChange: (Double?) -> Unit,
    onClearAll: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter Buses:",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00236F),
                    fontFamily = FontFamily(Font(R.font.inter))
                )

                if (filterState.isAnyFilterActive) {
                    Text(
                        text = "Clear All",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD61F1F),
                        fontFamily = FontFamily(Font(R.font.inter)),
                        modifier = Modifier.clickable(onClick = onClearAll)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "BUS TYPE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.inter))
            )

            Spacer(modifier = Modifier.height(4.dp))

            FilterCheckRow(
                label = "AC Sleeper",
                checked = filterState.selectedTypes.contains(BusTypeFilter.AC_SLEEPER),
                onToggle = { onTypeToggle(BusTypeFilter.AC_SLEEPER) }
            )

            FilterCheckRow(
                label = "AC Non Sleeper",
                checked = filterState.selectedTypes.contains(BusTypeFilter.AC_NON_SLEEPER),
                onToggle = { onTypeToggle(BusTypeFilter.AC_NON_SLEEPER) }
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "MINIMUM RATING",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.inter))
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(3.0, 3.5, 4.0, 4.5).forEach { rating ->
                    RatingOptionChip(
                        rating = rating,
                        selected = filterState.minRating == rating,
                        onClick = {
                            if (filterState.minRating == rating) {
                                onMinRatingChange(null)
                            } else {
                                onMinRatingChange(rating)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3D3BC4)
                )
            ) {
                Text(
                    text = "Apply",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.inter))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FilterCheckRow(
    label: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1F2937),
            fontFamily = FontFamily(Font(R.font.inter))
        )

        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (checked) Color(0xFF3D3BC4) else Color(0xFFF1F4FF))
                .border(
                    width = if (checked) 0.dp else 1.dp,
                    color = Color(0xFFD1D9FF),
                    shape = RoundedCornerShape(6.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun RatingOptionChip(
    rating: Double,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Color(0xFF3D3BC4) else Color(0xFFF1F4FF)
    val textColor = if (selected) Color.White else Color(0xFF1F2937)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Text(
            text = "${rating}+",
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusSortBottomSheet(
    currentSort: BusSortOption,
    onSortSelected: (BusSortOption) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    val options = listOf(
        BusSortOption.PRICE_LOW_TO_HIGH to "Price: Low to High",
        BusSortOption.PRICE_HIGH_TO_LOW to "Price: High to Low",
        BusSortOption.RATING_HIGH_TO_LOW to "Rating: High to Low"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Sort By",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00236F),
                modifier = Modifier.padding(start = 16.dp),
                fontFamily = FontFamily(Font(R.font.inter))
            )

            Spacer(modifier = Modifier.height(8.dp))

            options.forEach { (option, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSortSelected(option) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentSort == option,
                        onClick = { onSortSelected(option) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF3D3BC4)
                        )
                    )
                    Text(
                        text = label,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1F2937),
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
            }

            // Option to clear sort back to default order
            if (currentSort != BusSortOption.NONE) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSortSelected(BusSortOption.NONE) }
                        ,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentSort == BusSortOption.NONE,
                        onClick = { onSortSelected(BusSortOption.NONE) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF3D3BC4)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "None (default order)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1F2937),
                        fontFamily = FontFamily(Font(R.font.inter))
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BusFilterChipRowPreview() {
    PrimeBusTheme {
        BusFilterChipRow(
            filterState = BusFilterState(
                selectedTypes = setOf(BusTypeFilter.AC_SLEEPER),
                minRating = 4.0,
                sortOption = BusSortOption.PRICE_LOW_TO_HIGH
            ),
            onTypeToggle = {},
            onSortChange = {},
            onMinRatingChange = {},
            onClearAll = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterEntryChipPreview() {
    FilterEntryChip(
        activeCount = 3,
        onClick = {}
    )
}

// FilterChip Selected Preview
@Preview(showBackground = true)
@Composable
private fun FilterChipSelectedPreview() {
    FilterChip(
        label = "AC",
        selected = true,
        onClick = {}
    )
}

// FilterChip Unselected Preview
@Preview(showBackground = true)
@Composable
private fun FilterChipUnselectedPreview() {
    FilterChip(
        label = "Non-AC",
        selected = false,
        onClick = {}
    )
}

// FilterCheckRow Preview
@Preview(showBackground = true)
@Composable
private fun FilterCheckRowPreview() {
    FilterCheckRow(
        label = "Sleeper",
        checked = true,
        onToggle = {}
    )
}

// RatingOptionChip Preview
@Preview(showBackground = true)
@Composable
private fun RatingOptionChipPreview() {
    RatingOptionChip(
        rating = 4.0,
        selected = true,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun BusFilterBottomSheetPreview() {
    BusFilterBottomSheet(
        filterState = BusFilterState(
            selectedTypes = setOf(
                BusTypeFilter.AC_SLEEPER,
                BusTypeFilter.AC_NON_SLEEPER
            ),
            minRating = 4.0
        ),
        onTypeToggle = {},
        onMinRatingChange = {},
        onClearAll = {},
        onDismiss = {}
    )
}

// BusSortBottomSheet Preview
@Preview(showBackground = true, heightDp = 500)
@Composable
private fun BusSortBottomSheetPreview() {
    BusSortBottomSheet(
        currentSort = BusSortOption.PRICE_LOW_TO_HIGH,
        onSortSelected = {},
        onDismiss = {}
    )
}
