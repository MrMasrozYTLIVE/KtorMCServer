package net.mitask.packet.impl.player

import io.ktor.utils.io.*
import net.mitask.PlayerManager
import net.mitask.packet.Packet
import net.mitask.packet.PacketEnum

class PacketDisconnectKick(val reason: String = "No reason provided"): Packet(PacketEnum.DisconnectKick) {
    override suspend fun readData(playerManager: PlayerManager, reader: ByteReadChannel) {
        playerManager.shutdownConnection()
    }

    override suspend fun writeData(playerManager: PlayerManager, writer: ByteWriteChannel) {
        writer.writeByte(this.packetEnum.packetID)
        writeString16(writer, reason)
        playerManager.shutdownConnection()
    }
}