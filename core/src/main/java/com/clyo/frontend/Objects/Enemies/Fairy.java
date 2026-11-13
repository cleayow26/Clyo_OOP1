
package com.clyo.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.clyo.frontend.objects.patterns.shootingStrategy.FrontShot;
import com.clyo.frontend.objects.patterns.entityStrategy.LinearEntityMovement;
import com.clyo.frontend.systems.AssetManager;

public class Fairy extends Enemy {

    private float shootTimer = 0f;
    private float shootDelay = 0.67f;
    private float shootInterval = 1.4f;

    private int maxShots = 2;
    private int shotsFired = 0;

    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public Fairy(String name, int hp) {
        this(450, 400, name, hp);
    }

    public Fairy(float x, float y) {
        this(x, y, "Stage 1 Fairy", 20);
    }

    public Fairy(
            float x,
            float y,
            String name,
            int hp) {

        this(x, y, name, hp, 0.67f, 1.4f, 2);
    }

    public Fairy(
            float x,
            float y,
            String name,
            int hp,
            float shootDelay,
            float shootInterval,
            int maxShots) {

        super(
                x,
                y,
                50,
                50,
                Color.PINK,
                name,
                hp,
                500L
        );

        this.shootDelay = shootDelay;
        this.shootInterval = shootInterval;
        this.maxShots = maxShots;

        initializeFairy();
    }

    private void initializeFairy() {

        setShootingPattern(
                new FrontShot(150f, 10)
        );

        setMovementPattern(
                new LinearEntityMovement(0f, -40f)
        );

        setAnimation(
                AssetManager.getInstance().getAnimation("fairy_idle")
        );
    }

    // ==========================================
    // SHOOTING CONTROL
    // ==========================================

    public boolean canShoot(float delta) {

        if (isDestroyed()) {
            return false;
        }

        shootTimer += delta;

        if (shotsFired == 0 && shootTimer >= shootDelay) {
            shotsFired++;
            shootTimer = 0f;
            return true;
        }

        if (shotsFired < maxShots
                && shootTimer >= shootInterval) {

            shotsFired++;
            shootTimer = 0f;
            return true;
        }

        return false;
    }

    public void resetShooting() {
        shootTimer = 0f;
        shotsFired = 0;
    }

    public int getShotsFired() {
        return shotsFired;
    }

    public int getMaxShots() {
        return maxShots;
    }

    public float getShootDelay() {
        return shootDelay;
    }

    public float getShootInterval() {
        return shootInterval;
    }

    // ==========================================
    // UPDATE
    // ==========================================

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    // ==========================================
    // RENDER
    // ==========================================

    @Override
    public void render(ShapeRenderer shapeRenderer) {

        if (isDestroyed() || shapeRenderer == null) {
            return;
        }

        if (getSprite() == null && getAnimation() == null) {
            shapeRenderer.setColor(Color.PINK);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}
