package com.famas.doodlekingkmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform