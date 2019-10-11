package com.telkomdev.poppy

import kotlinx.coroutines.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketTimeoutException

val BUFFER: ByteArray? = ByteArray(1024)
val OK: ByteArray? = byteArrayOf(79, 75, 10)
val UNAUTHENTICATED = byteArrayOf(117, 110, 97, 117, 116, 104, 101, 110, 116, 105, 99, 97, 116, 101, 100, 10)
val AUTHENTICATED = byteArrayOf(97, 117, 116, 104, 101, 110, 116, 105, 99, 97, 116, 101, 100, 10)

fun writeMessage(socketWriter: OutputStream, message: ByteArray) {
    socketWriter.write(message)
    socketWriter.flush()
}

class Server(private val port: Int, waitQueueSize: Int, private var auth: String = "") {

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
                launch(Dispatchers.Default) {
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

    private fun handleClient(socket: Socket) = try {
        var socketReader: DataInputStream?
        var socketWriter: DataOutputStream?

        socketReader = DataInputStream(socket.getInputStream())
        socketWriter = DataOutputStream(socket.getOutputStream())

        var len: Int
        var isLogin = false

        while (socketReader.read(BUFFER).let { len = it; it != -1 }) {

            // read message from the client,
            // the length of the BUFFER itself is 1024
            // and we only need a byte of a message that sends by the client
            // so we slice the message based on len variable that hold the length of a message from th client
            // and slice begin from 0
            val messageByte = BUFFER?.sliceArray(IntRange(0, len - 2))

            if (!isLogin) {
                writeMessage(socketWriter, UNAUTHENTICATED)

                // convert auth string to byte array
                val authByte = auth.toByteArray()

                // compare authByte with messageByte(auth)
                if (authByte.contentEquals(messageByte!!)) {
                    // if login succeed
                    // then set isLogin to true
                    isLogin = true
                    writeMessage(socketWriter, AUTHENTICATED)
                }
            } else {

                val msgString = String(messageByte!!)
                println(msgString)

                writeMessage(socketWriter, OK!!)
            }

        }

        println("client ${socket.remoteSocketAddress} disconnected")
        socket.close()
    } catch (e: Exception) {
        println(e.message)
    }
}