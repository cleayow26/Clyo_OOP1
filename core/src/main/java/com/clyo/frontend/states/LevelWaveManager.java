
package com.clyo.frontend.states;

import java.util.List;

import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.Player;
import com.clyo.frontend.objects.enemies.Boss;
import com.clyo.frontend.systems.BulletManager;

public class LevelWaveManager {

    private WaveState currentState;

    // Game objects
    private Player player;
    private Boss boss;
    private List<GameObject> entities;
    private BulletManager bulletManager;

    public LevelWaveManager() {
    }

    // ==========================================
    // STATE MANAGEMENT
    // ==========================================

    public void setState(WaveState newState) {
        if (newState == null) {
            return;
        }

        currentState = newState;

        System.out.println(
                "[LevelWaveManager] State changed to "
                        + newState.getClass().getSimpleName()
        );

        currentState.onEnter(this);
    }

    public void update(float delta) {
        if (currentState != null) {
            currentState.update(delta, this);
        }
    }

    public WaveState getCurrentState() {
        return currentState;
    }

    public boolean hasActiveState() {
        return currentState != null;
    }

    // ==========================================
    // GAME OBJECT ACCESS
    // ==========================================

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public List<GameObject> getEntities() {
        return entities;
    }

    public void setEntities(List<GameObject> entities) {
        this.entities = entities;
    }

    public BulletManager getBulletManager() {
        return bulletManager;
    }

    public void setBulletManager(BulletManager bulletManager) {
        this.bulletManager = bulletManager;
    }
}
