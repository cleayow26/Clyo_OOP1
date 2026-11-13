
package com.clyo.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.clyo.frontend.objects.patterns.ShootingPattern;
import com.clyo.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.clyo.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.clyo.frontend.systems.AssetManager;
import com.clyo.frontend.systems.BulletManager;

public class Boss extends Enemy {

    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public Boss(String name, int hp) {
        super(
                380, 400,
                64, 64,
                Color.BLUE,
                name,
                hp,
                5000L
        );

        initializeBoss();
    }

    public Boss(float x, float y, String name, int hp) {
        super(
                x, y,
                64, 64,
                Color.BLUE,
                name,
                hp,
                5000L
        );

        initializeBoss();
    }

    public Boss(float x, float y) {
        this(x, y, "Rumia", 100);
    }

    private void initializeBoss() {

        setShootingPattern(
                new SpreadShot(200f, 3, 30f)
        );

        setMovementPattern(new FixedMovement());

        setAnimation(
                AssetManager.getInstance().getAnimation("boss_idle")
        );
    }

    // ==========================================
    // SHOOT BULLET
    // ==========================================

    public void shootBullet(BulletManager bulletManager) {

        if (bulletManager == null || isDestroyed()) {
            return;
        }

        ShootingPattern pattern = getShootingPattern();

        if (pattern == null) {
            return;
        }

        pattern.execute(
                x + width / 2f - 8f,
                y,
                bulletManager,
                false
        );
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
            shapeRenderer.setColor(0.7f, 0f, 0.7f, 1f);
            shapeRenderer.rect(x, y, width, height);
        }
    }
}
