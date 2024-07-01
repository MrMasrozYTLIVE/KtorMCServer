package net.mitask.packet

import io.ktor.utils.io.*
import net.mitask.PlayerManager

abstract class Packet(val packetEnum: PacketEnum) {
    abstract suspend fun readData(playerManager: PlayerManager, reader: ByteReadChannel)
    abstract suspend fun writeData(playerManager: PlayerManager, writer: ByteWriteChannel)

    suspend fun readString16(reader: ByteReadChannel): String {
        val byteArray = ByteArray(reader.readShort().toInt() * 2)
        reader.readFully(byteArray)
        return String(byteArray, Charsets.UTF_16BE)
    }

    suspend fun writeString16(writer: ByteWriteChannel, str: String) {
        writer.writeShort(str.length)
        writer.writeFully(str.toByteArray(Charsets.UTF_16BE))
    }

    override fun toString(): String {
        return "${packetEnum.name}[packetID=${packetEnum.packetID}]"
    }
}