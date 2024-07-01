package net.mitask.packet

import com.google.common.reflect.ClassPath
import org.slf4j.LoggerFactory
import java.util.*

class PacketManager {
    companion object {
        private val logger = LoggerFactory.getLogger(this.javaClass)
        private val packetMap: MutableMap<PacketEnum, Packet> = EnumMap(PacketEnum::class.java);

        fun loadPackets() {
            logger.info("Loading Packets...")
            val time = System.nanoTime()

            for (info in ClassPath.from(Thread.currentThread().contextClassLoader).topLevelClasses) {
                if (info.name.startsWith("net.mitask.packet.impl")) {
                    val clazz = info.load()
                    val packet: Packet = clazz.getConstructor().newInstance() as Packet

                    val packetFromMap = packetMap.get(packet.packetEnum)
                    if(packetFromMap != null) {
                        logger.warn("Packet $packet (${info.name}) is already in PacketMap. Skipping")
                        continue
                    }
                    packetMap.put(packet.packetEnum, packet)
                }
            }

            logger.info("Loaded ${packetMap.size} packets in ${(System.nanoTime() - time) / 1_000_000_000.toDouble()} second(s)")
        }

        fun getPacket(packetEnum: PacketEnum): Packet? {
            return packetMap[packetEnum]
        }

        fun getPacket(packetID: Int): Packet? {
            return packetMap[PacketEnum.getPacketByID(packetID)]
        }
    }
}