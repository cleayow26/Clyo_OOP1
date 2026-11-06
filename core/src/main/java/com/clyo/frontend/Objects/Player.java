
package com.clyo.frontend.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.clyo.frontend.observers.GameObserver;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {

    private String name;
    private int hp;
    private int maxHp;
    private int power;
    private int spellCards;
    private long score;

    private final List<GameObserver> observers = new ArrayList<>();

    // PROBLEM 4: PLAYER ATTRIBUTES
    private int lives = 3;
    private int grazeCount = 0;
    private boolean focused = false;
    private float shootTimer = 0.1f;
    private float shootCooldown = 0.1f;
    private float invulnerableTimer = 0f;

    private float movementSpeed = 250f;

    private ShootingPattern shootingPattern;



public void registerObserver(GameObserver observer) {
    if (observer != null && !observers.contains(observer)) {
        observers.add(observer);

        observer.onScoreChanged(score);
        observer.onHpChanged(hp);
        observer.onLivesChanged(lives);
        observer.onSpellCardsChanged(spellCards);
        observer.onPowerChanged(power);
        observer.onGrazeChanged(grazeCount);
    }
}

public void removeObserver(GameObserver observer) {
    observers.remove(observer);
}

private void notifyScoreChanged() {
    for (GameObserver obs : observers) {
        obs.onScoreChanged(score);
    }
}

private void notifyHpChanged() {
    for (GameObserver obs : observers) {
        obs.onHpChanged(hp);
    }
}

private void notifyLivesChanged() {
    for (GameObserver obs : observers) {
        obs.onLivesChanged(lives);
    }
}

private void notifySpellCardsChanged() {
    for (GameObserver obs : observers) {
        obs.onSpellCardsChanged(spellCards);
    }
}

private void notifyPowerChanged() {
    for (GameObserver obs : observers) {
        obs.onPowerChanged(power);
    }
}

private void notifyGrazeChanged() {
    for (GameObserver obs : observers) {
        obs.onGrazeChanged(grazeCount);
    }
}

    // ==========================================
    // CONSTRUCTORS
    // ==========================================

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

    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

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

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = Math.max(0, lives);
    }

    public int getGrazeCount() {
        return grazeCount;
    }

    public void setGrazeCount(int grazeCount) {
        this.grazeCount = Math.max(0, grazeCount);
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public float getShootTimer() {
        return shootTimer;
    }

    public void setShootTimer(float shootTimer) {
        this.shootTimer = shootTimer;
    }

    public float getShootCooldown() {
        return shootCooldown;
    }

    public void setShootCooldown(float shootCooldown) {
        this.shootCooldown = shootCooldown;
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }

    public void setInvulnerableTimer(float invulnerableTimer) {
        this.invulnerableTimer = invulnerableTimer;
    }

    // ==========================================
    // SHOOT ENEMY
    // ==========================================

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

    // ==========================================
    // PROBLEM 5: SHOOT BULLET
    // ==========================================

    public void shootBullet(BulletManager bulletManager) {

        if (bulletManager == null) {
            return;
        }

        if (shootTimer < shootCooldown) {
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
                    10 + power
            );
        }

        shootTimer = 0f;
    }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }

    // ==========================================
    // PROBLEM 5: UPDATE
    // ==========================================

    @Override
    public void update(float delta) {

        updateAnimationTime(delta);

        shootTimer += delta;

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;

            if (invulnerableTimer < 0f) {
                invulnerableTimer = 0f;
            }
        }
    }

    // ==========================================
    // PROBLEM 5: MOVE
    // ==========================================

    public void move(float dx, float dy) {

        x += dx * movementSpeed;
        y += dy * movementSpeed;

        x = Math.max(32f, Math.min(x, 416f - width));
        y = Math.max(16f, Math.min(y, 560f - height));
    }

    // ==========================================
    // PROBLEM 5: USE BOMB
    // ==========================================

    public void useBomb(
            BulletManager bulletManager,
            List<GameObject> entities) {

        if (spellCards > 0) {

            spellCards--;

            invulnerableTimer = 5f;

            if (bulletManager != null) {
                bulletManager.clearEnemyBullets(entities);
            }

            System.out.println("[Player] Spell Card activated!");
        } else {
            System.out.println("[Player] No spell cards available!");
        }
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    // ==========================================
    // PROBLEM 5: GRAZE
    // ==========================================

    public void addGraze() {
        grazeCount++;
        addScore(50);
    }

    // ==========================================
    // HITBOX
    // ==========================================

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

    // ==========================================
    // COLLECT ITEM
    // ==========================================

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

    // ==========================================
    // PROBLEM 6: SPRITEBATCH RENDER
    // ==========================================

    @Override
    public void render(SpriteBatch batch) {

        if (isDestroyed()) {
            return;
        }

        super.render(batch);
    }

    // ==========================================
    // PROBLEM 6: FOCUS RENDERING
    // ==========================================

    @Override
    public void render(ShapeRenderer shapeRenderer) {

        if (isDestroyed()) {
            return;
        }

        if (getSprite() == null && getAnimation() == null) {
            shapeRenderer.setColor(0, 0, 1, 1);
            shapeRenderer.rect(x, y, width, height);
        }

        if (focused) {
            shapeRenderer.setColor(Color.YELLOW);

            shapeRenderer.rect(
                    x + width / 2f - 4f,
                    y + height / 2f - 4f,
                    8f,
                    8f
            );
        }
    }

    // ==========================================
    // ADDITION: PLAYER DAMAGE AND LIVES
    // ==========================================

    public void takeDamage(int damage) {

        if (isDestroyed() || invulnerableTimer > 0f) {
            return;
        }

        hp -= damage;

        if (hp < 0) {
            hp = 0;
        }

        if (hp <= 0) {
            loseLife();
        }
    }

    public void loseLife() {

        lives--;

        if (lives > 0) {
            respawn();
        } else {
            hp = 0;
            destroy();

            System.out.println("[Player] Game Over!");
        }
    }

    public void respawn() {

        hp = maxHp;
        x = 200f;
        y = 50f;

        invulnerableTimer = 3f;

        activate();

        System.out.println("[Player] Respawned!");
    }

    public void resetGame() {

        hp = maxHp;
        lives = 3;
        power = 0;
        spellCards = 3;
        score = 0;
        grazeCount = 0;

        focused = false;
        shootTimer = shootCooldown;
        invulnerableTimer = 0f;

        x = 200f;
        y = 50f;

        activate();

        System.out.println("[Player] Game reset!");
    }

    public boolean isAlive() {
        return lives > 0 && hp > 0;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    // ==========================================
    // COLLISION
    // ==========================================

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
