package net.mitask.packet.impl.login;

import io.ktor.utils.io.*
import net.mitask.MinecraftServer
import net.mitask.PlayerManager
import net.mitask.packet.Packet;
import net.mitask.packet.PacketEnum;
import net.mitask.packet.impl.player.PacketChat
import net.mitask.packet.impl.player.PacketPositionLook
import net.mitask.util.ChatColor

class PacketLogin: Packet(PacketEnum.Login) {
    override suspend fun readData(playerManager: PlayerManager, reader: ByteReadChannel) {
        val protocol = reader.readInt()
        if(protocol > 14) return playerManager.kickPlayer("Server is outdated!")
        else if (protocol < 14) return playerManager.kickPlayer("Client is outdated!")

        if(MinecraftServer.playerManagers.size > MinecraftServer.MAX_PLAYERS) return playerManager.kickPlayer("Server is full!")

        val username = readString16(reader)
        if(!username.equals(playerManager.player.username)) return playerManager.kickPlayer("Your Handshake and Login usernames don't match!")
        val seed = reader.readLong()
        val dimension = reader.readByte()
        playerManager.sendPacket(this);
        playerManager.sendPacket(PacketPositionLook())
        MinecraftServer.sendPacketToAll(PacketChat("${ChatColor.YELLOW}<${username}> has joined the game."))
    }

    override suspend fun writeData(playerManager: PlayerManager, writer: ByteWriteChannel) {
        writer.writeByte(this.packetEnum.packetID)
        writer.writeInt(1) // EntityID
        writeString16(writer, "") // Unused
        writer.writeLong(0) // Seed
        writer.writeByte(0) // Dimension
    }
}
