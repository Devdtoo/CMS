package com.example.cms

import android.util.Log
import java.lang.StringBuilder
import java.util.*

class DynamicTopicFileCreator(val list: List<TopicResponse>) {

    private val TAG = "DynamicTopicFileCreator"
    var topic = StringBuilder()

    init {
        createTopicName()
        createConcept()
        createUserRules()
        createProposals()
    }

    private fun createTopicName(){
        topic.append("topic: ~test()\n")
    }

    private fun createConcept(){
        list.forEach {
            val inputString = StringBuilder()
            it.chat.input.forEach { input->
                inputString.append("\"$input\"")
            }
            topic.append("concept:(${it.robotificationId}) [$inputString] \n")
        }
    }

    private fun createUserRules(){
            topic.append("u:(~${list[0].robotificationId}) ^enableThenGoto(${list[0].chat.nextScreen.name})\n")
    }

    private fun createProposals(){
        var count = 1
        list.forEach {
            topic.append("proposal:%${it.chat.nextScreen.name} ${it.chat.output[0]}\n")
            if (count < list.size) {
                topic.append("u1:(~${list[count].robotificationId}) ^enableThenGoto(${list[count].chat.nextScreen.name}) \n")
                topic.append("u1:(e:Dialog/NotUnderstood) I didn't understand that, \\pau=400\\ Please say it again? ^stayInScope\n")
            }
            count++
        }
    }

    fun createSubRules(){

    }

    fun getTopicFile():String{
        return topic.toString()
    }
}