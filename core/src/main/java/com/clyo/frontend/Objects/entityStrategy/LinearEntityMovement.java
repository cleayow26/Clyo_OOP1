
package com.clyo.frontend.objects.patterns.entityStrategy;

import com.clyo.frontend.GameObject;

public class LinearEntityMovement implements EntityMovementPattern {

    private float velocityX;
    private float velocityY;

    public LinearEntityMovement(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    @Override
    public void move(GameObject entity, float delta) {

        if (entity == null || entity.isDestroyed()) {
            return;
        }

        entity.setX(entity.getX() + velocityX * delta);
        entity.setY(entity.getY() + velocityY * delta);
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }
}
