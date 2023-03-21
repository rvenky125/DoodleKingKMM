package com.famas.doodlekingkmm.data.models


import com.famas.doodlekingkmm.data.models.ChatMessage

fun ChatMessage.matchesWord(word: String): Boolean {
    return message.lowercase().trim() == word.lowercase().trim()
}
