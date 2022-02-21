package net.como.client.modules.movement;

import net.como.client.events.GetVelocityMultiplierEvent;
import net.como.client.structures.Module;
import net.como.client.structures.events.Event;

public class NoSlow extends Module {

    public NoSlow() {
        super("NoSlow");

        this.description = "Allows you to never slow down";

        this.setCategory("Movement");
    }
    
    @Override
    public void activate() {
        this.addListen(GetVelocityMultiplierEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(GetVelocityMultiplierEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "GetVelocityMultiplierEvent": {
                GetVelocityMultiplierEvent e = (GetVelocityMultiplierEvent)event;

                e.cir.setReturnValue(1f);
                
                break;
            }
        }
    }
}
