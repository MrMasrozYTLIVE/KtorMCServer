package net.mitask

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mitask.packet.Packet
import net.mitask.packet.PacketManager
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread

class MinecraftServer(val port: Int) {
    val logger = LoggerFactory.getLogger(this.javaClass)

    init {
        PacketManager.loadPackets()

        runBlocking {
            val serverSocket = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().bind(port = port)
            logger.info("Server listening at ${serverSocket.localAddress}")
            while (true) {
                val socket = serverSocket.accept()
                logger.info("Accepted ${socket.remoteAddress}")
                thread {
                    launch {
                        val playerManager = PlayerManager(socket)
                        playerManagers.add(playerManager)
                        try {
                            playerManager.handleConnection()
                        } catch (e: Exception) {
                            logger.error("Exception happened during handling connection of player \"${playerManager.player.username}\"!", e)
                            playerManager.shutdownConnection()
                            playerManagers.remove(playerManager)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val MAX_PLAYERS: Int = 20

        val playerManagers: MutableList<PlayerManager> = ArrayList()

        suspend fun sendPacketToAll(packet: Packet) {
            playerManagers.forEach { it.sendPacket(packet) }
        }
    }
}

fun main() {
    MinecraftServer(25565)
}