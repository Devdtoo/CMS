package com.example.cms

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.actuation.Animate
import com.aldebaran.qi.sdk.`object`.actuation.Animation
import com.aldebaran.qi.sdk.`object`.conversation.Chat
import com.aldebaran.qi.sdk.`object`.conversation.QiChatbot
import com.aldebaran.qi.sdk.`object`.conversation.Topic
import com.aldebaran.qi.sdk.builder.AnimateBuilder
import com.aldebaran.qi.sdk.builder.AnimationBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.FileReader

class MainActivity : RobotActivity(), RobotLifecycleCallbacks {

    private val TAG = "MainActivity"
    private lateinit var firstTopic: String
    private lateinit var secondTopic: String
    private lateinit var topicResponse: List<TopicResponse>
    private lateinit var topicResponse1: List<TopicResponse>
    private lateinit var dynamicTopicFileCreator: DynamicTopicFileCreator
    private lateinit var dynamicTopicFileCreator1: DynamicTopicFileCreator
    private lateinit var qiChatConversation: QiChatConversation
    private lateinit var topic: Topic
    private lateinit var qiChatbot: QiChatbot
    private lateinit var chat: Chat
    private var chatFuture: Future<Void>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        QiSDK.register(this, this)

        val gson = Gson()
        val string = "[\n" +
                "  {\n" +
                "   \"robotificationId\" : 1234,\n" +
                "   \"chat\": {\n" +
                "    \"input\": [\n" +
                "      \"hi\",\n" +
                "      \"hello\",\n" +
                "      \"Hey\",\n" +
                "      \"Hi Pepper\",\n" +
                "      \"Hello Pepper\"\n" +
                "    ],\n" +
                "    \"output\": [\n" +
                "      \"Hello, Welcome to Proven Robotics\",\n" +
                "      \"Alright, Here are the details for iPhone\"\n" +
                "    ],\n" +
                "    \"nextScreen\": {\n" +
                "      \"id\": \"1\",\n" +
                "      \"name\": \"Dashboard\"\n" +
                "    }\n" +
                "  }\n" +
                " },\n" +
                " {\n" +
                "   \"robotificationId\" : 122,\n" +
                "   \"chat\": {\n" +
                "    \"input\": [\n" +
                "      \"find me a meeting room\",\n" +
                "      \"find a meeting\"\n" +
                "    ],\n" +
                "    \"output\": [\n" +
                "      \"Ok May I know your name\"\n" +
                "    ],\n" +
                "    \"nextScreen\": {\n" +
                "      \"id\": \"2\",\n" +
                "      \"name\": \"FindMeeting\"\n" +
                "    }\n" +
                "  }\n" +
                " }\n" +
                ",\n" +
                "  {\n" +
                "    \"robotificationId\" : 1267,\n" +
                "    \"chat\": {\n" +
                "      \"input\": [\n" +
                "        \"My Name is rustam\",\n" +
                "        \"rustam\"\n" +
                "      ],\n" +
                "      \"output\": [\n" +
                "        \"Your meeting with adele is booked from today to today\"\n" +
                "      ],\n" +
                "      \"nextScreen\": {\n" +
                "        \"id\": \"2\",\n" +
                "        \"name\": \"MeetingRoom\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "]"
        val data = gson.fromJson(string,ClassList::class.java)

        topicResponse = data

//        topicResponse = listOf(
//            TopicResponse(
//                Chat(
//                    input = listOf("Hi Hello Hey"),
//                    nextScreen = NextScreen("1", "Dashboard"),
//                    output = listOf("Welcome to proven Solution, How can I Help you?")
//                ), 12
//            ),
//            TopicResponse(
//                Chat(
//                    input = "Find meeting room",
//                    nextScreen = NextScreen("1", "FindMeetingRoom"),
//                    output = listOf("Ok May I know your name?")
//                ), 14
//            )
//        )

        dynamicTopicFileCreator = DynamicTopicFileCreator(topicResponse)
//        dynamicTopicFileCreator1 = DynamicTopicFileCreator(topicResponse1)
        firstTopic = dynamicTopicFileCreator.getTopicFile()
//        secondTopic = dynamicTopicFileCreator1.getTopicFile()
        Log.i(TAG, "$firstTopic\n")
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        qiChatConversation = QiChatConversation(qiContext!!)
        firstBtn?.setOnClickListener {
            GlobalScope.launch {
                startChat(firstTopic)
            }
        }

//        secondBtn?.setOnClickListener {
//            GlobalScope.launch {
//                startChat(secondTopic)
//            }
//        }
    }

    override fun onRobotFocusLost() {
    }

    override fun onRobotFocusRefused(reason: String?) {
    }

    private fun startChat(topicString: String) {
        GlobalScope.launch {
            stopChat()
        }
        topic = qiChatConversation.buildTopic(topicString)
        qiChatbot = qiChatConversation.createChatBot(topic)
        chat = qiChatConversation.createChat(qiChatbot)
        chatFuture = chat.async().run()
    }

    private fun stopChat() {
        chatFuture?.requestCancellation()
    }
}