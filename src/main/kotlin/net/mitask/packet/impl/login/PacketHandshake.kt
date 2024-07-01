package net.mitask.packet.impl.login;

import io.ktor.utils.io.*
import net.mitask.Player
import net.mitask.PlayerManager
import net.mitask.packet.Packet;
import net.mitask.packet.PacketEnum;

class PacketHandshake: Packet(PacketEnum.Handshake) {
    override suspend fun readData(playerManager: PlayerManager, reader: ByteReadChannel) {
        playerManager.player = Player(playerManager, readString16(reader), 0.0, 140.0, 142.0,0.0,0F, 0F, false)

        playerManager.sendPacket(this)
    }

    override suspend fun writeData(playerManager: PlayerManager, writer: ByteWriteChannel) {
        writer.writeByte(this.packetEnum.packetID)
        writeString16(writer, "-")
    }
}
