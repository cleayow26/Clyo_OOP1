
package com.clyo.frontend.objects;

import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.patterns.ShootingPattern;
import com.clyo.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
import com.clyo.frontend.systems.BulletManager;

public abstract class EntityShooter extends GameObject {

    protected ShootingPattern shootingPattern;
    protected EntityMovementPattern movementPattern;

    protected float speed;

    // Menentukan apakah shooter adalah Player
    protected boolean isPlayerShooter = false;

    public EntityShooter(
            float x,
            float y,
            float width,
            float height,
            float speed) {

        super(x, y, width, height);
        this.speed = speed;
    }

    public EntityShooter(
            float x,
            float y,
            float width,
            float height,
            float speed,
            ShootingPattern shootingPattern,
            EntityMovementPattern movementPattern) {

        super(x, y, width, height);

        this.speed = speed;
        this.shootingPattern = shootingPattern;
        this.movementPattern = movementPattern;
    }

    @Override
    public void update(float delta) {
        updateAnimationTime(delta);
        updateMovement(delta);
    }

    public void updateMovement(float delta) {
        if (movementPattern != null && !isDestroyed()) {
            movementPattern.move(this, delta);
        }
    }

    public void shootBullet(BulletManager bulletManager) {

        if (shootingPattern == null
                || bulletManager == null
                || isDestroyed()) {
            return;
        }

        float originX = x + width / 2f - 8f;

        float originY = y
                + (isPlayerShooter ? height : 0f);

        shootingPattern.execute(
                originX,
                originY,
                bulletManager,
                isPlayerShooter
        );
    }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }

    public EntityMovementPattern getMovementPattern() {
        return movementPattern;
    }

    public void setMovementPattern(
            EntityMovementPattern movementPattern) {
        this.movementPattern = movementPattern;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isPlayerShooter() {
        return isPlayerShooter;
    }

    public void setPlayerShooter(boolean isPlayerShooter) {
        this.isPlayerShooter = isPlayerShooter;
    }
}
