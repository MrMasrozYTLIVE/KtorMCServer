package net.mitask

class Player(val playerManager: PlayerManager, val username: String, var posX: Double, var posY: Double, var stance: Double, var posZ: Double, var yaw: Float, var pitch: Float, var onGround: Boolean) {
    var entityID: Int = 1;

    suspend fun updatePosition(x: Double?, y: Double?, stance: Double?, z: Double?, yaw: Float?, pitch: Float?, onGround: Boolean?) {
        if(x != null) {
//            this.prevXPosition = this.xPosition;
            this.posX = x;
        }
        if(y != null && stance != null) {
//            this.prevYPosition = this.yPosition;
            this.posY = y;
            this.stance = stance;

            val sy = stance - y;
            if(sy < 0.1 || sy > 1.65) playerManager.kickPlayer("Illegal Stance");
        }
        if(z != null) {
//            this.prevZPosition = this.zPosition;
            this.posZ = z;
        }
        if(yaw != null) this.yaw = yaw;
        if(pitch != null) this.pitch = pitch;
        if(onGround != null) this.onGround = onGround;

        // console.log(this.toString());
//        MinecraftServer.sendPacketToAll(PacketEntityTeleport(this.entityID, this.xPosition, this.yPosition, this.zPosition, 0, 0))
    }
}