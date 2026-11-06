
package com.clyo.frontend.commands;

import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.Player;
import com.clyo.frontend.systems.BulletManager;

import java.util.List;

public class BombCommand implements Command {

    private List<GameObject> entities;

    // Constructor kosong
    public BombCommand() {
    }

    // Constructor dengan entities
    public BombCommand(List<GameObject> entities) {
        this.entities = entities;
    }

    // Setter entities
    public void setEntities(List<GameObject> entities) {
        this.entities = entities;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {

        if (player != null) {
            player.useBomb(bulletManager, entities);
        }
    }
}
