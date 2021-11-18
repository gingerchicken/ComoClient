package net.como.client.cheats;

import net.como.client.CheatClient;
import net.como.client.events.ClientTickEvent;
import net.como.client.events.RenderPortalOverlayEvent;
import net.como.client.structures.Cheat;
import net.como.client.structures.events.Event;
import net.como.client.structures.settings.Setting;
import net.como.client.utils.ClientUtils;

public class NoPortal extends Cheat {
    public NoPortal() {
        super("NoPortal");

        this.addSetting(new Setting("NoOverlay", true));
        this.addSetting(new Setting("NoNausea", true));
        this.addSetting(new Setting("AllowTyping", true));
    
        this.description = "Allows portal effects to be toggled.";
    }

    @Override
    public void activate() {
        this.addListen(RenderPortalOverlayEvent.class);
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(RenderPortalOverlayEvent.class);
        this.removeListen(ClientTickEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (!ClientUtils.isInNetherPortal() || !(boolean)this.getSetting("NoNausea").value) break;

                CheatClient.me().lastNauseaStrength = 0;
                CheatClient.me().nextNauseaStrength = 0.00001f;

                break;
            }
            case "RenderPortalOverlayEvent": {
                RenderPortalOverlayEvent e = (RenderPortalOverlayEvent)event;
                if ((boolean)this.getSetting("NoOverlay").value) {
                    e.ci.cancel();
                }

                break;
            }
        }
    }
}
