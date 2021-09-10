package net.como.client.utils;

import net.como.client.CheatClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtils {
    public static Vec3d getEyePos() {
        ClientPlayerEntity me = CheatClient.me();

        return new Vec3d(
            me.getX(),
            me.getY() + me.getEyeHeight(me.getPose()),
            me.getZ()
        );
    }

    public static Rotation getRequiredRotation(Vec3d vec) {
		Vec3d eyesPos = getEyePos();
		
        Vec3d delta = MathsUtils.Vec3dDiff(vec, eyesPos);
		
		double diffXZ = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
		
		double yaw = Math.toDegrees(Math.atan2(delta.z, delta.x)) - 90d;
		double pitch = -Math.toDegrees(Math.atan2(delta.y, diffXZ));
		
		return new Rotation(yaw, pitch);
	}

    public static final class Rotation {
        public final double yaw;
        public final double pitch;

        public Rotation(double yaw, double pitch) {
            this.yaw = MathHelper.wrapDegrees(yaw);
            this.pitch = MathHelper.wrapDegrees(pitch);
        }
    }
}
