
package com.clyo.frontend.objects.bullets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.clyo.frontend.Collidable;
import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.BulletType;
import com.clyo.frontend.objects.enemies.Enemy;
import com.clyo.frontend.systems.AssetManager;

public class Bullet extends GameObject {

    private int damage;
    private BulletType type;
    private float speed;

    // Existing constructor for player bullets.
    public Bullet(
            float x,
            float y,
            BulletType type,
            int damage) {

        this(x, y, 400f, type, damage);
    }

    // Constructor with configurable speed.
    public Bullet(
            float x,
            float y,
            float speed,
            BulletType type,
            int damage) {

        super(x, y, 8, 15);

        this.type = type;
        this.damage = damage;
        this.speed = speed;

        this.sprite = AssetManager.getInstance()
                .getTextureRegion("bullet_danmaku");
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public BulletType getType() {
        return type;
    }

    public void setType(BulletType type) {
        this.type = type;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void update(float delta) {
        updateAnimationTime(delta);
        y += speed * delta;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (getSprite() == null && getAnimation() == null) {
            shapeRenderer.setColor(1, 1, 0, 1);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Enemy) {
            Enemy enemy = (Enemy) other;

            System.out.println(
                    "Bullet hit "
                    + enemy.getName()
                    + " for "
                    + damage
                    + " DMG!"
            );

            enemy.takeDamage(damage);
            destroy();
        }
    }
}
