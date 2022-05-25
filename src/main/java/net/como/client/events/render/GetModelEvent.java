package net.como.client.events.render;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.como.client.misc.events.Event;
import net.minecraft.entity.player.PlayerEntity;

public class GetModelEvent extends Event {
    public PlayerEntity player;
    public CallbackInfoReturnable<String> cir;

    public GetModelEvent(PlayerEntity player, CallbackInfoReturnable<String> cir) {
        this.player = player;
        this.cir = cir;
    }
}
