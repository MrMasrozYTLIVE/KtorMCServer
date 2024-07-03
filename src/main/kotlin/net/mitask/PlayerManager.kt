package net.mitask

import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mitask.packet.Packet
import net.mitask.packet.PacketEnum
import net.mitask.packet.PacketManager
import net.mitask.packet.impl.player.PacketChat
import net.mitask.packet.impl.player.PacketDisconnectKick
import net.mitask.util.ChatColor
import org.slf4j.LoggerFactory

class PlayerManager(privte val socket: Socket) {
    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)
    private val reader = socket.openReadChannel()
    private val writer = socket.openWriteChannel(autoFlush = true)

    lateinit var player: Player;

    suspend fun handleConnection() {
        while(!socket.isClosed) {
            readPacket()
        }
    }

    private suspend fun readPacket() {
        val packetID = reader.readByte().toInt()
        val packet = PacketManager.getPacket(packetID) ?: return Unit.run {if(logger.isDebugEnabled) logger.warn("Received unknown packet $packetID from ${player.username}")}
        packet.readData(this, reader)
    }

    suspend fun sendPacket(packet: Packet) {
        logger.debug("Sending packet ${packet.packetEnum.name} (${packet.packetEnum.packetID}) to user ${player.username}")
        packet.writeData(this, writer)
    }

    suspend fun kickPlayer(reason: String) {
        sendPacket(PacketDisconnectKick(reason))
    }

    suspend fun shutdownConnection() {
        logger.info("Player ${player.username} left the game")
        MinecraftServer.sendPacketToAll(PacketChat("${ChatColor.YELLOW}<${player.username}> has left the game."))

        withContext(Dispatchers.IO) {
            writer.close()
            reader.cancel()
            socket.close()
        }
    }
}
