
package com.clyo.frontend.objects.patterns.shootingStrategy;

import com.clyo.frontend.objects.patterns.ShootingPattern;
import com.clyo.frontend.systems.BulletManager;

public class AlternatingRingShot implements ShootingPattern {

    private float baseSpeed;
    private int bulletCountPerRing;
    private int damage;
    private int ringLayers;
    private float speedStep;
    private float rotationOffset = 0f;
    private float rotationSpeed = 0f;

    public AlternatingRingShot(float baseSpeed, int bulletCountPerRing,
                               int damage, int ringLayers, float speedStep) {
        this.baseSpeed = baseSpeed;
        this.bulletCountPerRing = Math.max(3, bulletCountPerRing);
        this.damage = damage;
        this.ringLayers = Math.max(1, ringLayers);
        this.speedStep = speedStep;
    }

    public AlternatingRingShot(float baseSpeed, int bulletCountPerRing, int damage) {
        this(baseSpeed, bulletCountPerRing, damage, 2, 35f);
    }

    @Override
    public void execute(float originX, float originY,
                        BulletManager bulletManager, boolean isPlayer) {
        if (bulletManager == null) return;

        String[] colors = {
            "bullet_danmaku_red",
            "bullet_danmaku_blue",
            "bullet_danmaku_purple",
            "bullet_danmaku_green"
        };

        float angleStep = 360f / bulletCountPerRing;

        for (int layer = 0; layer < ringLayers; layer++) {
            float speed = baseSpeed + layer * speedStep;
            float phase = rotationOffset + (angleStep / ringLayers) * layer;

            for (int i = 0; i < bulletCountPerRing; i++) {
                float angle = phase + i * angleStep;
                float radians = (float) Math.toRadians(angle);

                float vx = (float) Math.cos(radians) * speed;
                float vy = (float) Math.sin(radians) * speed;

                if (isPlayer) {
                    bulletManager.spawnPlayerBullet(
                        originX, originY, vx, vy, damage
                    );
                } else {
                    bulletManager.spawnEnemyBullet(
                        originX, originY, vx, vy, damage, colors[layer % colors.length]
                    );
                }
            }
        }

        rotationOffset = (rotationOffset + rotationSpeed) % 360f;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getBaseSpeed() { return baseSpeed; }
    public void setBaseSpeed(float speed) { baseSpeed = speed; }

    public int getBulletCountPerRing() { return bulletCountPerRing; }
    public void setBulletCountPerRing(int count) {
        bulletCountPerRing = Math.max(3, count);
    }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
}
