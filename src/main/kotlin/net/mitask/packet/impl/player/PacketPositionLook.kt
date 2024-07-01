package net.mitask.packet.impl.player

import io.ktor.utils.io.*
import net.mitask.MinecraftServer
import net.mitask.PlayerManager
import net.mitask.packet.Packet
import net.mitask.packet.PacketEnum

class PacketPositionLook: Packet(PacketEnum.PositionLook) {
    override suspend fun readData(playerManager: PlayerManager, reader: ByteReadChannel) {
        playerManager.player.updatePosition(
            reader.readDouble(), // X
            reader.readDouble(), // Y
            reader.readDouble(), // Stance
            reader.readDouble(), // Z
            reader.readFloat(), // Yaw
            reader.readFloat(), // Pitch
            reader.readBoolean() // onGround
        );
    }

    override suspend fun writeData(playerManager: PlayerManager, writer: ByteWriteChannel) {
        writer.writeByte(this.packetEnum.packetID)
        writer.writeDouble(playerManager.player.posX)
        writer.writeDouble(playerManager.player.stance)
        writer.writeDouble(playerManager.player.posY)
        writer.writeDouble(playerManager.player.posZ)
        writer.writeFloat(playerManager.player.yaw)
        writer.writeFloat(playerManager.player.pitch)
        writer.writeBoolean(playerManager.player.onGround)
    }
}