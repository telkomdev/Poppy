package com.telkomdev.poppy


import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.lang.NumberFormatException


fun main(args: Array<String>) = runBlocking {

    val portStr: String? = System.getenv("PORT")
    val queueSizeStr: String? = System.getenv("QUEUE_SIZE")
    if (portStr == null) {
        println("port cannot be empty")
        System.exit(1)
    }

    if (queueSizeStr == null) {
        println("queue size cannot be empty")
        System.exit(1)
    }

    val port: Int
    val queueSize: Int

    try {
        port = Integer.parseInt(portStr)
        queueSize = Integer.parseInt(queueSizeStr)

        val server = Server(port, queueSize)

        server.start()


    } catch (e: NumberFormatException) {
        println("error getting environment variable")
        System.exit(1)
    } catch (e: IOException) {
        println("error start server ${e.message}")
        System.exit(1)
    }

}

