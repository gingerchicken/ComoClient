package net.como.client.modules;

import net.como.client.ComoClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.PlayerMoveEvent;
import net.como.client.events.SendPacketEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.MathsUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class FreeCam extends Module {
    private Vec3d origin = new Vec3d(0, 0, 0);
    private float originPitch = 0, originYaw = 0;

    private void setOrigin() {
        this.origin = ComoClient.me().getPos();
        this.originYaw = ComoClient.me().getYaw();
        this.originPitch = ComoClient.me().getPitch();
        
    }

    private void revertOrigin() {
        ComoClient.me().setPos(origin.x, origin.y, origin.z);
        ComoClient.me().setYaw(this.originYaw);
        ComoClient.me().setPitch(this.originPitch);

        ComoClient.me().noClip = false;
    }

    public FreeCam() {
        super("FreeCam");

        this.addSetting(new Setting("Speed", 1f));

        this.description = "Allows you to fly around the world (but client-side)";
    }
    
    @Override
    public void activate() {
        this.setOrigin();

        this.addListen(ClientTickEvent.class);
        this.addListen(SendPacketEvent.class);
        this.addListen(PlayerMoveEvent.class);

        this.revertOrigin();
    }

    @Override
    public void deactivate() {
        this.revertOrigin();

        this.removeListen(ClientTickEvent.class);
        this.removeListen(SendPacketEvent.class);
        this.removeListen(PlayerMoveEvent.class);

        this.revertOrigin();
    }

    @Override
    public void fireEvent(Event event) {
        ClientPlayerEntity me = ComoClient.me();

        switch (event.getClass().getSimpleName()) {
            case "PlayerMoveEvent": {
                me.noClip = true;
                break;
            }
            case "SendPacketEvent": {
                SendPacketEvent e = (SendPacketEvent)event;
                if (e.packet instanceof PlayerMoveC2SPacket || e.packet instanceof ClientCommandC2SPacket) e.ci.cancel();

                break;
            }
            case "ClientTickEvent": {
                // Vals
                Vec3d v = Vec3d.ZERO;
                Vec3d pos = me.getPos();
                float speed = (float)this.getSetting("Speed").value;

                if (me.isSprinting()) speed *= 2;

                // Client settings
                me.setOnGround(true);
                me.setVelocity(0, 0, 0);

                // Game options
                GameOptions opt = ComoClient.getClient().options;

                // Controls
                if (opt.keyForward.isPressed()) v = v.add(ComoClient.me().getRotationVector());
                if (opt.keyBack.isPressed())    v = v.add(ComoClient.me().getRotationVector().multiply(-1));

                if (opt.keyRight.isPressed())   v = v.add(MathsUtils.getRightVelocity(ComoClient.me()));
                if (opt.keyLeft.isPressed())    v = v.add(MathsUtils.getRightVelocity(ComoClient.me()).multiply(-1));

                if (opt.keyJump.isPressed())    v = v.add(0,  1, 0);
                if (opt.keySneak.isPressed())   v = v.add(0, -1, 0);

                // Calculate the speed.
                v   = v.multiply(speed);
                pos = pos.add(v);

                // Set the velocity
                me.setPos(pos.x, pos.y, pos.z);

                break;
            }
        }
    }
}
