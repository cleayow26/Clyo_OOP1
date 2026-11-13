
package com.clyo.frontend.objects.bullets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.clyo.frontend.Collidable;
import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.BulletType;
import com.clyo.frontend.objects.enemies.Enemy;
import com.clyo.frontend.objects.patterns.bulletStrategy.BulletMovementPattern;
import com.clyo.frontend.systems.AssetManager;

public class Bullet extends GameObject {

    private BulletType bulletType;
    private int damage;

    private float vx;
    private float vy;
    private float angle = 90f;
    private float speed;

    private boolean isPlayerBullet;
    private boolean grazed;

    private BulletMovementPattern movementPattern;

    public Bullet(float x, float y, BulletType bulletType, int damage) {
        this(x, y, 400f, bulletType, damage);
    }

    public Bullet(
            float x,
            float y,
            float speed,
            BulletType bulletType,
            int damage) {

        super(x, y, 16, 16);

        this.bulletType = bulletType;
        this.damage = damage;
        this.speed = speed;

        this.vx = 0f;
        this.vy = speed;
        this.angle = speed < 0f ? 270f : 90f;

        this.isPlayerBullet = true;
        this.grazed = false;
        this.movementPattern = null;

        this.sprite = AssetManager.getInstance()
                .getTextureRegion("bullet_danmaku");
    }

    public void init(
            float x,
            float y,
            float vx,
            float vy,
            BulletType bulletType,
            int damage,
            boolean isPlayerBullet) {

        this.x = x;
        this.y = y;

        this.vx = vx;
        this.vy = vy;

        this.speed = (float) Math.hypot(vx, vy);

        this.angle = (float) Math.toDegrees(
                Math.atan2(vy, vx)
        );

        this.bulletType = bulletType;
        this.damage = damage;
        this.isPlayerBullet = isPlayerBullet;

        this.grazed = false;

        // Reset strategy so pooled bullets do not retain old behavior
        this.movementPattern = null;

        // Reset animation timer and reactivate
        activate();
    }

    @Override
    public void update(float delta) {
        if (isDestroyed()) {
            return;
        }

        updateAnimationTime(delta);

        if (movementPattern != null) {
            movementPattern.move(this, delta);
        } else {
            x += vx * delta;
            y += vy * delta;
        }

        // Keep angle synchronized with current movement direction
        if (vx != 0f || vy != 0f) {
            angle = (float) Math.toDegrees(Math.atan2(vy, vx));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch == null || isDestroyed()) {
            return;
        }

        if (sprite != null) {
            batch.draw(
                    sprite,
                    x,
                    y,
                    width / 2f,
                    height / 2f,
                    width,
                    height,
                    1f,
                    1f,
                    angle - 90f
            );
        } else {
            super.render(batch);
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (shapeRenderer == null || isDestroyed()) {
            return;
        }

        if (sprite == null && animation == null) {
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (isDestroyed()) {
            return;
        }

        if (other instanceof Enemy && isPlayerBullet) {
            Enemy enemy = (Enemy) other;

            System.out.println(
                    "Bullet hit " + enemy.getName()
                    + " for " + damage + " DMG!"
            );

            enemy.takeDamage(damage);
            destroy();
        }
    }

    public BulletType getType() {
        return bulletType;
    }

    public void setType(BulletType bulletType) {
        this.bulletType = bulletType;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public boolean isPlayerBullet() {
        return isPlayerBullet;
    }

    public void setPlayerBullet(boolean isPlayerBullet) {
        this.isPlayerBullet = isPlayerBullet;
    }

    public boolean hasBeenGrazed() {
        return grazed;
    }

    public boolean isGrazed() {
        return grazed;
    }

    public void setGrazed(boolean grazed) {
        this.grazed = grazed;
    }

    public BulletMovementPattern getMovementPattern() {
        return movementPattern;
    }

    public void setMovementPattern(
            BulletMovementPattern movementPattern) {
        this.movementPattern = movementPattern;
    }
}
