
package com.clyo.frontend.objects.patterns.bulletStrategy;

import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.bullets.Bullet;

public class FantasySealMovement implements BulletMovementPattern {

    private final GameObject target;
    private final float speed;
    private final float turnSpeed;

    private float angle;
    private boolean initialized;

    public FantasySealMovement(
            GameObject target,
            float speed,
            float turnSpeed) {

        this.target = target;
        this.speed = speed;
        this.turnSpeed = turnSpeed;
        this.initialized = false;
    }

    @Override
    public void move(Bullet bullet, float delta) {

        if (bullet == null || bullet.isDestroyed()) {
            return;
        }

        if (!initialized) {
            if (target != null && !target.isDestroyed()) {
                float dx = target.getX() + target.getWidth() / 2f
                        - bullet.getX() - bullet.getWidth() / 2f;

                float dy = target.getY() + target.getHeight() / 2f
                        - bullet.getY() - bullet.getHeight() / 2f;

                angle = (float) Math.atan2(dy, dx);
            } else {
                angle = (float) Math.atan2(
                        bullet.getVy(), bullet.getVx()
                );
            }

            initialized = true;
        }

        if (target != null && !target.isDestroyed()) {

            float dx = target.getX() + target.getWidth() / 2f
                    - bullet.getX() - bullet.getWidth() / 2f;

            float dy = target.getY() + target.getHeight() / 2f
                    - bullet.getY() - bullet.getHeight() / 2f;

            float desiredAngle = (float) Math.atan2(dy, dx);
            float difference = normalizeAngle(desiredAngle - angle);

            float maxTurn = turnSpeed * delta;

            if (Math.abs(difference) <= maxTurn) {
                angle = desiredAngle;
            } else {
                angle += Math.signum(difference) * maxTurn;
            }
        }

        float vx = (float) Math.cos(angle) * speed;
        float vy = (float) Math.sin(angle) * speed;

        bullet.setVx(vx);
        bullet.setVy(vy);
        bullet.setSpeed(speed);
        bullet.setAngle((float) Math.toDegrees(angle));

        bullet.setX(bullet.getX() + vx * delta);
        bullet.setY(bullet.getY() + vy * delta);
    }

    private float normalizeAngle(float angle) {

        while (angle > Math.PI) {
            angle -= (float) (2 * Math.PI);
        }

        while (angle < -Math.PI) {
            angle += (float) (2 * Math.PI);
        }

        return angle;
    }
}
