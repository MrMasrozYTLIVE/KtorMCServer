package net.mitask.packet.impl.player

import io.ktor.utils.io.*
import net.mitask.MinecraftServer
import net.mitask.PlayerManager
import net.mitask.packet.Packet
import net.mitask.packet.PacketEnum

class PacketChat(var message: String = "<Server> This should've never happen."): Packet(PacketEnum.Chat) {
    val NOTCHIAN_ALLOWED_SYMBOLS = "#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~⌂ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»"

    override suspend fun readData(playerManager: PlayerManager, reader: ByteReadChannel) {
        message = readString16(reader)
        if(message.length > 100) return playerManager.kickPlayer("Chat message is too long!")
        message = message.trim()

        for (char in message) {
            if(NOTCHIAN_ALLOWED_SYMBOLS.indexOf(char) < 0) return playerManager.kickPlayer("Illegal characters in chat")
        }

        if(message.startsWith("/")) {
            // TODO: Implement chat commands
            message = "Not implemented yet!"
            return playerManager.sendPacket(this)
        }

        message = "<${playerManager.player.username}> $message"

        MinecraftServer.sendPacketToAll(this)
    }

    override suspend fun writeData(playerManager: PlayerManager, writer: ByteWriteChannel) {
        writer.writeByte(this.packetEnum.packetID)
        writeString16(writer, message)
    }
}