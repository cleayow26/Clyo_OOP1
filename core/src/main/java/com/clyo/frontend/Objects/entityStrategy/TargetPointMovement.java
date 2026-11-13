
package com.clyo.frontend.objects.patterns.entityStrategy;

import com.clyo.frontend.GameObject;

public class TargetPointMovement implements EntityMovementPattern {
    private float targetX;
    private float targetY;
    private float speed;

    public TargetPointMovement(float targetX, float targetY, float speed) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.speed = speed;
    }

    @Override
    public void move(GameObject entity, float delta) {
        if (entity == null) return;

        float dx = targetX - entity.getX();
        float dy = targetY - entity.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float step = speed * delta;

        if (distance <= step || distance < 1f) {
            entity.setX(targetX);
            entity.setY(targetY);
        } else {
            entity.setX(entity.getX() + dx / distance * step);
            entity.setY(entity.getY() + dy / distance * step);
        }
    }

    public boolean hasArrived(GameObject entity) {
        if (entity == null) return false;

        float dx = targetX - entity.getX();
        float dy = targetY - entity.getY();

        return dx * dx + dy * dy < 1f;
    }

    public float getTargetX() { return targetX; }
    public float getTargetY() { return targetY; }

    public void setTargetPoint(float x, float y) {
        targetX = x;
        targetY = y;
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
}
