package net.mitask.packet

enum class PacketEnum(val packetID: Int) {
    Login(0x01),
    Handshake(0x02),
    Chat(0x03),
    PositionLook(0x0D),
    DisconnectKick(0xFF);

    companion object {
        fun getPacketByID(packetID: Int): PacketEnum? {
            values().forEach {
                if(it.packetID == packetID) return it
            }

            return null
        }
    }
}