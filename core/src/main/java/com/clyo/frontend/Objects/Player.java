
package com.clyo.frontend.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.clyo.frontend.Collidable;
import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.bullets.Bullet;
import com.clyo.frontend.objects.enemies.Boss;
import com.clyo.frontend.objects.enemies.Enemy;
import com.clyo.frontend.objects.items.Item;
import com.clyo.frontend.objects.patterns.ShootingPattern;
import com.clyo.frontend.systems.AssetManager;
import com.clyo.frontend.systems.AudioSystem;
import com.clyo.frontend.systems.BulletManager;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int maxHp;
    private int power;
    private int spellCards;
    private long score;

    private ShootingPattern shootingPattern;

    public Player(String name, int hp, int power, int spellCards) {
        super(320, 50, 50, 50);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;

        setAnimation(
                AssetManager.getInstance().getAnimation("player_idle")
        );
    }

    public Player(
            float x,
            float y,
            String name,
            int hp,
            int maxHp,
            int power) {

        super(x, y, 50, 50);

        this.name = name;
        this.hp = hp;
        this.maxHp = maxHp;
        this.power = power;
        this.spellCards = 3;
        this.score = 0;

        setAnimation(
                AssetManager.getInstance().getAnimation("player_idle")
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void addScore(long amount) {
        score += amount;
    }

    public void shoot(Enemy enemy) {
        enemy.takeDamage(power);

        if (enemy.isDestroyed()) {
            score += enemy.getScoreValue();
        }
    }

    public void shoot(Boss boss) {
        boss.takeDamage(power);

        if (boss.isDestroyed()) {
            score += boss.getScoreValue();
        }
    }

    public Bullet shootBullet() {
        return new Bullet(
                x + width / 2f - 8f,
                y + height,
                BulletType.AMULET,
                25
        );
    }

    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager == null) {
            return;
        }

        if (shootingPattern != null) {
            shootingPattern.execute(
                    x + width / 2f - 8f,
                    y + height,
                    bulletManager,
                    true
            );
        } else {
            bulletManager.spawnPlayerBullet(
                    x + width / 2f - 8f,
                    y + height,
                    0f,
                    400f,
                    25
            );
        }
    }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }

    @Override
    public Hitbox getCoreHitbox() {
        return new Hitbox(
                x + width / 2f - 4f,
                y + height / 2f - 4f,
                8f,
                8f
        );
    }

    public Hitbox getGrazeHitbox() {
        return new Hitbox(x, y, width, height);
    }

    public void collectItem(Item item) {
        if (item == null || item.isDestroyed()) {
            return;
        }

        score += item.getScoreValue();
        power += item.getItemTypeEnum().getPowerBonus();

        if (item.getItemTypeEnum().name().equals("LIFE")) {
            hp = Math.min(hp + 20, maxHp);
        }

        if (item.getItemTypeEnum().name().equals("BOMB")) {
            spellCards++;
        }

        item.destroy();

        System.out.println("ITEM COLLECTED! Score: " + score);

        try {
            AudioSystem.getInstance().playItemCollect();
            System.out.println("Pickup sound triggered!");
        } catch (Exception e) {
            System.out.println("Pickup sound error:");
            e.printStackTrace();
        }
    }

    @Override
    public void update(float delta) {
        updateAnimationTime(delta);

        float movementSpeed = 250f;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= movementSpeed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += movementSpeed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += movementSpeed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= movementSpeed * delta;
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (getSprite() == null && getAnimation() == null) {
            shapeRenderer.setColor(0, 0, 1, 1);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item) {
            Item item = (Item) other;

            System.out.println("PLAYER COLLISION WITH ITEM!");

            if (!item.isDestroyed()) {
                collectItem(item);
            }
        }
    }
}
