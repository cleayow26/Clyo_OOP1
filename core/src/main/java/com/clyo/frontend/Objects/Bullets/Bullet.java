package com.clyo.frontend.objects.bullets;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.clyo.frontend.Collidable;
import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.BulletType;
import com.clyo.frontend.objects.enemies.Enemy;

public class Bullet extends GameObject {

    private int damage;

    private BulletType type;

    private float speed = 400f;

    public Bullet(
            float x,
            float y,
            BulletType type,
            int damage) {

        super(
                x,
                y,
                8,
                15
        );

        this.type = type;
        this.damage = damage;
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

    @Override
    public void update(float delta) {

        y += speed * delta;
    }

    @Override
    public void render(
            ShapeRenderer shapeRenderer) {

        shapeRenderer.setColor(
                1,
                1,
                0,
                1
        );

        shapeRenderer.rect(
                x,
                y,
                width,
                height
        );
    }

    @Override
    public void onCollision(
            Collidable other) {

        if (other instanceof Enemy) {

            Enemy enemy =
                    (Enemy) other;

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