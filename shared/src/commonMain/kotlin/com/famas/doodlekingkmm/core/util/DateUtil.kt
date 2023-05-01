package com.famas.doodlekingkmm.core.util

import io.ktor.util.date.GMTDate

fun Long.asFormattedDate(): String {
    val date = GMTDate(this)
    return "${date.dayOfMonth} - ${date.month} - ${date.year}"
}