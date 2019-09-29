package com.telkomdev.poppy

import kotlinx.coroutines.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketTimeoutException

val BUFFER: ByteArray? = ByteArray(1024)
val OK: ByteArray? = byteArrayOf(79, 75, 10)

class Server(private val port: Int, waitQueueSize: Int) {

    private val serverSocket: ServerSocket = ServerSocket()

    init {

        val socketAddress = InetSocketAddress("localhost", port)
        serverSocket.bind(socketAddress, waitQueueSize)
    }

    suspend fun start() = coroutineScope {
        while (true) {
            try {
                println("waiting for client connection on port $port")
                val socket = serverSocket.accept()

                println("new client connected ${socket.remoteSocketAddress}")

                // handle each client to its thread
                launch (Dispatchers.Default) {
                    handleClient(socket)
                }


            } catch (e: SocketTimeoutException) {
                println("server timeout ${e.message}")
                break
            } catch (e: IOException) {
                println(e.message)
                break
            }
        }
    }

    private fun handleClient(socket: Socket) {
        try {
            var socketReader: DataInputStream?
            var socketWriter: DataOutputStream?

            socketReader = DataInputStream(socket.getInputStream())
            socketWriter = DataOutputStream(socket.getOutputStream())

            var len: Int
            while (socketReader.read(BUFFER).let { len = it; it != -1 }) {
                println("message len $len")

                val messageByte = BUFFER

                val msgString = String(messageByte!!)
                println(msgString)

                socketWriter.write(OK)
                socketWriter.flush()

            }

            println("client ${socket.remoteSocketAddress} disconnected")
            socket.close()
        } catch (e: Exception) {
            println(e.message)
        }
    }
}