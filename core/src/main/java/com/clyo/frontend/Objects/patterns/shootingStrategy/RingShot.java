package com.clyo.frontend.objects.patterns.shootingStrategy;

import com.clyo.frontend.systems.BulletManager;
import com.clyo.frontend.objects.patterns.ShootingPattern;

public class RingShot implements ShootingPattern {

    private float speed;
    private int numBullets;
    private int damage;

    public RingShot(float speed, int numBullets, int damage) {
        this.speed = speed;
        this.numBullets = numBullets;
        this.damage = damage;
    }

    @Override
    public void execute(float originX, float originY,
                        BulletManager bulletManager, boolean isPlayer) {

        float angleStep = 360f / numBullets;

        for (int i = 0; i < numBullets; i++) {
            float angle = i * angleStep;
            float radians = (float) Math.toRadians(angle);

            float vx = (float) Math.cos(radians) * speed;
            float vy = (float) Math.sin(radians) * speed;

            if (isPlayer) {
                bulletManager.spawnPlayerBullet(
                    originX, originY, vx, vy, damage
                );
            } else {
                bulletManager.spawnEnemyBullet(
                    originX, originY, vx, vy, damage
                );
            }
        }
    }
}