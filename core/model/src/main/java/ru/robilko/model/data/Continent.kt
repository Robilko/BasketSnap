package ru.robilko.model.data

import androidx.annotation.DrawableRes
import ru.robilko.model.R

enum class Continent(val title: String, @DrawableRes val iconResId: Int) {
    AFRICA("Africa", R.drawable.ic_africa),
    ASIA("Asia", R.drawable.ic_asia),
    Europe("Europe", R.drawable.ic_europe),
    SOUTH_AMERICA("South-America", R.drawable.ic_america),
    WORLD("World", R.drawable.ic_world);

    companion object {
        @DrawableRes
        fun getIconResId(title: String): Int {
            return entries.firstOrNull {
                it.title.uppercase() == title.uppercase().trim()
            }?.iconResId ?: R.drawable.ic_flag_placeholder
        }
    }
}