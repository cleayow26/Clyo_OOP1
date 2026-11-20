
package com.clyo.frontend.states;

import com.clyo.frontend.objects.Player;
import com.clyo.frontend.objects.enemies.Boss;
import com.clyo.frontend.systems.BulletManager;

public abstract class NonSpellState implements WaveState {

    protected static final float TEST_TIMEOUT_SECONDS = 4f;
    protected static final float GLIDE_DISTANCE = 90f;
    protected static final float GLIDE_SECONDS = 1f;

    protected float phaseTimer = 0f;

    @Override
    public void onEnter(LevelWaveManager manager) {
        phaseTimer = 0f;

        System.out.println(
                "[NonSpellState] Entering " + getPhaseName()
        );
    }

    @Override
    public void update(float delta, LevelWaveManager manager) {
        phaseTimer += delta;
    }

    public abstract String getPhaseName();

    public abstract int getPhaseLife();

    public abstract int getLifeThreshold();

    public abstract float getTimeoutSeconds();

    public abstract WaveState createNextState();

    protected Player getPlayer(LevelWaveManager manager) {
        return manager.getPlayer();
    }

    protected Boss getBoss(LevelWaveManager manager) {
        return manager.getBoss();
    }

    protected BulletManager getBulletManager(LevelWaveManager manager) {
        return manager.getBulletManager();
    }
}
