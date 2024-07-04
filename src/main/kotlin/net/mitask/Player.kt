package net.mitask

import kotlin.math.absoluteValue
import kotlin.math.pow

class Player(val playerManager: PlayerManager, val username: String, var posX: Double, var posY: Double, var stance: Double, var posZ: Double, var yaw: Float, var pitch: Float, var onGround: Boolean) {
    var entityID: Int = 1
    var prevPosX: Double = posX
    var prevPosY: Double = posY
    var prevPosZ: Double = posZ

    suspend fun updatePosition(x: Double?, y: Double?, stance: Double?, z: Double?, yaw: Float?, pitch: Float?, onGround: Boolean?) {
        if(x != null) {
            if(x.isNaN() || x.absoluteValue > 3.2E7) return playerManager.kickPlayer("Illegal position")

            this.prevPosX = this.posX
            this.posX = x
        }

        if(y != null && stance != null) {
            this.prevPosY = this.posY
            this.posY = y
            this.stance = stance

            val sy = stance - y
            if(sy < 0.1 || sy > 1.65) return playerManager.kickPlayer("Illegal Stance")
        }

        if(z != null) {
            if(z.isNaN() || z.absoluteValue > 3.2E7) return playerManager.kickPlayer("Illegal position")

            this.prevPosZ = this.posZ
            this.posZ = z
        }

        if(yaw != null) this.yaw = yaw
        if(pitch != null) this.pitch = pitch
        if(onGround != null) this.onGround = onGround

        if(x != null || z != null) {
            val distX = (this.posX - this.prevPosX).pow(2)
            val distZ = (this.posZ - this.prevPosZ).pow(2)
            if(distX + distZ > 100) return playerManager.kickPlayer("You moved too quickly :( (Hacking?)")
        }

        // console.log(this.toString());
//        MinecraftServer.sendPacketToAll(PacketEntityTeleport(this.entityID, this.xPosition, this.yPosition, this.zPosition, 0, 0))
    }
}