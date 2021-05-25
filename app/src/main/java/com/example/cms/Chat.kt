package com.example.cms

data class Chat(
    val input: List<String>,
    val nextScreen: NextScreen,
    val output: List<String>
)