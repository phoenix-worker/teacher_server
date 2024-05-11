package com.example.plugins

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        post("/notifyTodayTaskFinished") {
            val dir = File("/home/blogger/teacher")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, "teacherLog")
            if (!file.exists()) file.createNewFile()
            val fileWriter = FileWriter(file, true)
            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
            val dateString = simpleDateFormat.format(Date())
            fileWriter.append("$dateString\n")
            fileWriter.close()
            val scriptPath = "/home/blogger/teacher/scripts/success.sh"
            Runtime.getRuntime().exec("su -c $scriptPath blogger")
            call.respond(mapOf("result" to "success"))
        }
    }
}
