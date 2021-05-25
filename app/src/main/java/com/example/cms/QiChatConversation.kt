package com.example.cms

import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.conversation.Chat
import com.aldebaran.qi.sdk.`object`.conversation.Chatbot
import com.aldebaran.qi.sdk.`object`.conversation.QiChatbot
import com.aldebaran.qi.sdk.`object`.conversation.Topic
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder

class QiChatConversation(private val qiContext: QiContext) {

    fun buildTopic(topicResource :String) : Topic{
        return TopicBuilder.with(qiContext)
            .withText(topicResource)
            .build()
    }

    fun createChatBot(topic: Topic) : QiChatbot{
        return QiChatbotBuilder.with(qiContext)
            .withTopic(topic)
            .build()
    }

    fun createChat(chatbot: Chatbot) : Chat{
        return ChatBuilder.with(qiContext)
            .withChatbot(chatbot)
            .build()
    }
}