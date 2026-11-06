
package com.clyo.frontend.commands;

import com.clyo.frontend.objects.Player;
import com.clyo.frontend.systems.BulletManager;

public class ShootCommand implements Command {

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null && bulletManager != null) {
            player.shootBullet(bulletManager);
        }
    }
}
