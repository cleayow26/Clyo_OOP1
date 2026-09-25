package com.clyo.frontend.objects.enemies;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.clyo.frontend.Collidable;
import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.Player;

public class Enemy extends GameObject {

    protected String name;
    protected int hp;
    protected int maxHp;
    protected long scoreValue;

    public Enemy(
            String name,
            int hp) {

        super(450, 400, 50, 50);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 100;
    }

    public Enemy(
            float x,
            float y,
            float width,
            float height,
            String name,
            int hp) {

        super(x, y, width, height);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 100;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(long scoreValue) {
        this.scoreValue = scoreValue;
    }

    public void takeDamage(int damage) {

        hp -= damage;

        if (hp < 0) {
            hp = 0;
        }

        System.out.println(
                name
                + " took "
                + damage
                + " damage! HP: "
                + hp
                + "/"
                + maxHp
        );

        if (hp <= 0) {

            System.out.println(
                    name
                    + " was defeated!"
            );

            destroy();
        }
    }

    public void attack(Player player, int damage) {

        player.setHp(
                player.getHp() - damage
        );

        System.out.println(
                name
                + " attacked "
                + player.getName()
                + " for "
                + damage
                + " damage!"
        );
    }

    @Override
    public void update(float delta) {
        // Enemy stays in place.
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {

        shapeRenderer.setColor(1, 0, 0, 1);

        shapeRenderer.rect(
                x,
                y,
                width,
                height
        );
    }

    @Override
    public void onCollision(Collidable other) {
        // Enemy collision behavior.
    }
}