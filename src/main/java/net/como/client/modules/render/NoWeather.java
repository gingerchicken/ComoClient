package net.como.client.modules.render;

import net.como.client.events.render.GetRainGradientEvent;
import net.como.client.misc.Module;
import net.como.client.misc.events.Event;

public class NoWeather extends Module {
    public NoWeather() {
        super("AntiBritish");

        this.setDescription("Hides the rain.");
        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(GetRainGradientEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(GetRainGradientEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "GetRainGradientEvent": {
                ((GetRainGradientEvent)event).cir.setReturnValue(0f);
                break;
            }
        }
    }
}
