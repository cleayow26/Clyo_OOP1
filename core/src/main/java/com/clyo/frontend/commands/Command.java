
package com.clyo.frontend.commands;

import com.clyo.frontend.objects.Player;
import com.clyo.frontend.systems.BulletManager;

public interface Command {

    void execute(Player player, BulletManager bulletManager);

}
