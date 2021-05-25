package com.example.cms

import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.conversation.BaseQiChatExecutor
import com.aldebaran.qi.sdk.`object`.conversation.QiChatbot

class DynamicExecutor(qiContext: QiContext,val chatbot: QiChatbot):BaseQiChatExecutor(qiContext) {
    override fun runWith(params: MutableList<String>?) {
        val welcomePhrase = chatbot.variable("welcomePhrase")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}