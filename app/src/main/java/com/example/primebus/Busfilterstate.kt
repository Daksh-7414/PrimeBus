package com.example.primebus

enum class BusSortOption {
    NONE,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    RATING_HIGH_TO_LOW
}

enum class BusTypeFilter {
    AC_SLEEPER,
    AC_NON_SLEEPER
}

data class BusFilterState(
    val selectedTypes: Set<BusTypeFilter> = emptySet(),
    val minRating: Double? = null,
    val sortOption: BusSortOption = BusSortOption.NONE
) {
    val isAnyFilterActive: Boolean
        get() = selectedTypes.isNotEmpty() ||
                minRating != null ||
                sortOption != BusSortOption.NONE

    val activeFilterCount: Int
        get() = selectedTypes.size +
                (if (minRating != null) 1 else 0) +
                (if (sortOption != BusSortOption.NONE) 1 else 0)
}