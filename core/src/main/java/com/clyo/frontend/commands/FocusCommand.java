
package com.clyo.frontend.commands;

import com.clyo.frontend.objects.Player;
import com.clyo.frontend.systems.BulletManager;

public class FocusCommand implements Command {

    private boolean focused;

    public FocusCommand(boolean focused) {
        this.focused = focused;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {
            player.setFocused(focused);
        }
    }
}
