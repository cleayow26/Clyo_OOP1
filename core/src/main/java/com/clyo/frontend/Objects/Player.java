
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
import com.clyo.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.clyo.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.clyo.frontend.objects.patterns.shootingStrategy.FrontShot;
import com.clyo.frontend.objects.patterns.shootingStrategy.SideShot;
import com.clyo.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.clyo.frontend.systems.AssetManager;
import com.clyo.frontend.systems.AudioSystem;
import com.clyo.frontend.systems.BulletManager;
import com.clyo.frontend.observers.GameObserver;

import java.util.ArrayList;
import java.util.List;

public class Player extends EntityShooter {

    private String name;
    private int hp;
    private int maxHp;
    private int power;
    private int spellCards;
    private long score;

    private final List<GameObserver> observers = new ArrayList<>();

    private int lives = 3;
    private int grazeCount = 0;

    private boolean focused = false;

    private float shootTimer = 0f;
    private float shootCooldown = 5f / 60f;

    private float homingShootTimer = 0f;
    private float homingCooldown = 30f / 60f;

    private float invulnerableTimer = 0f;
    private float movementSpeed = 250f;

    private ShootingPattern mainShootingPattern;
    private ShootingPattern sideShootingPattern;

    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public Player(String name, int hp, int power, int spellCards) {
        super(320, 50, 50, 50, 240f);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;

        initializePlayer();
    }

    public Player(
            float x,
            float y,
            String name,
            int hp,
            int maxHp,
            int power) {

        super(x, y, 50, 50, 240f);

        this.name = name;
        this.hp = hp;
        this.maxHp = maxHp;
        this.power = power;
        this.spellCards = 3;
        this.score = 0;

        initializePlayer();
    }

    private void initializePlayer() {
        setPlayerShooter(true);
        setMovementPattern(new FixedMovement());

        setAnimation(
                AssetManager.getInstance().getAnimation("player_idle")
        );

        shootTimer = shootCooldown;
        homingShootTimer = homingCooldown;

        updatePowerStrategy();
    }

    // ==========================================
    // OBSERVER PATTERN
    // ==========================================

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
        notifyHpChanged();
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = Math.max(0, Math.min(power, 128));
        updatePowerStrategy();
        notifyPowerChanged();
    }

    public int getSpellCards() {
        return spellCards;
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = Math.max(0, spellCards);
        notifySpellCardsChanged();
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
        notifyScoreChanged();
    }

    public void addScore(long amount) {
        score += amount;
        notifyScoreChanged();
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = Math.max(0, lives);
        notifyLivesChanged();
    }

    public int getGrazeCount() {
        return grazeCount;
    }

    public void setGrazeCount(int grazeCount) {
        this.grazeCount = Math.max(0, grazeCount);
        notifyGrazeChanged();
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        if (this.focused != focused) {
            this.focused = focused;
            updatePowerStrategy();
        }
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

    public float getHomingShootTimer() {
        return homingShootTimer;
    }

    public float getHomingCooldown() {
        return homingCooldown;
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }

    public void setInvulnerableTimer(float invulnerableTimer) {
        this.invulnerableTimer = Math.max(0f, invulnerableTimer);
    }

    public ShootingPattern getMainShootingPattern() {
        return mainShootingPattern;
    }

    public ShootingPattern getSideShootingPattern() {
        return sideShootingPattern;
    }

    // ==========================================
    // DYNAMIC POWER SHOOTING STRATEGY
    // ==========================================

    private void updatePowerStrategy() {

        float spreadAngle = focused ? 4f : 18f;
        float dualSpreadAngle = focused ? 3f : 12f;

        float sideHOffset = focused ? 8f : 24f;
        float sideVOffset = focused ? 16f : -16f;

        float leftAngle = focused ? 100f : 120f;
        float rightAngle = focused ? 80f : 60f;

        float normalSpeed = 720f;
        float homingSpeed = 500f;

        mainShootingPattern = null;
        sideShootingPattern = null;

        homingCooldown = 30f / 60f;

        if (power < 8) {
            mainShootingPattern = new FrontShot(
                    normalSpeed,
                    48
            );
        }
        else if (power < 16) {
            mainShootingPattern = new FrontShot(
                    normalSpeed,
                    48
            );

            sideShootingPattern = new SideShot(
                    homingSpeed,
                    14,
                    sideHOffset,
                    sideVOffset,
                    leftAngle,
                    rightAngle,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
            );
        }
        else if (power < 32) {
            mainShootingPattern = new SpreadShot(
                    normalSpeed,
                    2,
                    dualSpreadAngle,
                    30,
                    8f,
                    null,
                    null,
                    null
            );

            sideShootingPattern = new SideShot(
                    homingSpeed,
                    14,
                    sideHOffset,
                    sideVOffset,
                    leftAngle,
                    rightAngle,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
            );
        }
        else if (power < 48) {
            mainShootingPattern = new SpreadShot(
                    normalSpeed,
                    3,
                    spreadAngle,
                    26
            );

            sideShootingPattern = new SideShot(
                    homingSpeed,
                    14,
                    sideHOffset,
                    sideVOffset,
                    leftAngle,
                    rightAngle,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
            );
        }
        else if (power < 64) {
            mainShootingPattern = new SpreadShot(
                    normalSpeed,
                    3,
                    spreadAngle,
                    26
            );

            sideShootingPattern = new SideShot(
                    homingSpeed,
                    12,
                    sideHOffset,
                    sideVOffset,
                    leftAngle,
                    rightAngle,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
            );

            homingCooldown = 15f / 60f;
        }
        else if (power < 80) {
            mainShootingPattern = new SpreadShot(
                    normalSpeed,
                    3,
                    spreadAngle,
                    26
            );

            sideShootingPattern = new SideShot(
                    homingSpeed,
                    12,
                    sideHOffset,
                    sideVOffset,
                    leftAngle,
                    rightAngle,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
            );
        }
        else if (power < 128) {
            mainShootingPattern = new SpreadShot(
                    normalSpeed,
                    3,
                    spreadAngle,
                    26
            );

            sideShootingPattern = new SideShot(
                    homingSpeed,
                    8,
                    sideHOffset,
                    sideVOffset,
                    leftAngle - 10f,
                    rightAngle + 10f,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
            );
        }
        else {
            mainShootingPattern = new SpreadShot(
                    normalSpeed,
                    4,
                    spreadAngle * 1.2f,
                    24,
                    16f,
                    null,
                    null,
                    null
            );

            sideShootingPattern = new SideShot(
                    homingSpeed,
                    8,
                    sideHOffset,
                    sideVOffset,
                    leftAngle - 10f,
                    rightAngle + 10f,
                    true,
                    new HomingBulletMovement(360f),
                    "bullet_amulet_homing"
            );
        }

        setShootingPattern(mainShootingPattern);
    }

    // ==========================================
    // SHOOT ENEMY
    // ==========================================

    public void shoot(Enemy enemy) {
        if (enemy == null || enemy.isDestroyed()) {
            return;
        }

        enemy.takeDamage(power);

        if (enemy.isDestroyed()) {
            addScore(enemy.getScoreValue());
        }
    }

    public void shoot(Boss boss) {
        if (boss == null || boss.isDestroyed()) {
            return;
        }

        boss.takeDamage(power);

        if (boss.isDestroyed()) {
            addScore(boss.getScoreValue());
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
    // SHOOT BULLET
    // ==========================================

    public void shootBullet(BulletManager bulletManager) {

        if (bulletManager == null || isDestroyed()) {
            return;
        }

        float originX = x + width / 2f - 8f;
        float originY = y + height;

        if (shootTimer >= shootCooldown) {

            if (mainShootingPattern != null) {
                mainShootingPattern.execute(
                        originX,
                        originY,
                        bulletManager,
                        true
                );
            }
            else {
                bulletManager.spawnPlayerBullet(
                        originX,
                        originY,
                        0f,
                        720f,
                        10 + power
                );
            }

            shootTimer = 0f;
        }

        if (sideShootingPattern != null
                && homingShootTimer >= homingCooldown) {

            sideShootingPattern.execute(
                    originX,
                    originY,
                    bulletManager,
                    true
            );

            homingShootTimer = 0f;
        }
    }

    // ==========================================
    // UPDATE
    // ==========================================

    @Override
    public void update(float delta) {

        super.update(delta);

        shootTimer += delta;
        homingShootTimer += delta;

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;

            if (invulnerableTimer < 0f) {
                invulnerableTimer = 0f;
            }
        }
    }

    // ==========================================
    // MOVE
    // ==========================================

    public void move(float dx, float dy) {

        // InputHandler already multiplies dx and dy by delta.
        // Therefore, do not multiply by delta again here.
        x += dx * movementSpeed;
        y += dy * movementSpeed;

        // Keep player inside the game area.
        x = Math.max(32f, Math.min(x, 416f - width));
        y = Math.max(16f, Math.min(y, 560f - height));
    }

    // ==========================================
    // USE BOMB - FANTASY SEAL
    // ==========================================

    public void useBomb(
            BulletManager bulletManager,
            List<GameObject> entities) {

        if (spellCards <= 0) {
            System.out.println("[Player] No spell cards available!");
            return;
        }

        spellCards--;
        notifySpellCardsChanged();

        invulnerableTimer = 5f;

        if (bulletManager != null) {

            bulletManager.clearEnemyBullets(entities);

            GameObject target = null;

            if (entities != null) {
                for (GameObject entity : entities) {
                    if (entity instanceof Enemy
                            && !entity.isDestroyed()) {
                        target = entity;
                        break;
                    }
                }
            }

            bulletManager.spawnBombOrbs(
                    x + width / 2f,
                    y + height / 2f,
                    target,
                    entities
            );
        }

        System.out.println("[Player] Fantasy Seal activated!");
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    // ==========================================
    // GRAZE
    // ==========================================

    public void addGraze() {
        grazeCount++;
        addScore(50);
        notifyGrazeChanged();
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

        power = Math.min(
                128,
                power + item.getItemTypeEnum().getPowerBonus()
        );

        updatePowerStrategy();

        if (item.getItemTypeEnum().name().equals("LIFE")) {
            hp = Math.min(hp + 20, maxHp);
            notifyHpChanged();
        }

        if (item.getItemTypeEnum().name().equals("BOMB")) {
            spellCards++;
            notifySpellCardsChanged();
        }

        notifyScoreChanged();
        notifyPowerChanged();

        item.destroy();

        System.out.println("ITEM COLLECTED! Score: " + score);

        try {
            AudioSystem.getInstance().playItemCollect();
            System.out.println("Pickup sound triggered!");
        }
        catch (Exception e) {
            System.out.println("Pickup sound error:");
            e.printStackTrace();
        }
    }

    // ==========================================
    // SPRITEBATCH RENDER
    // ==========================================

    @Override
    public void render(SpriteBatch batch) {

        if (isDestroyed()) {
            return;
        }

        super.render(batch);
    }

    // ==========================================
    // FOCUS RENDERING
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
    // PLAYER DAMAGE AND LIVES
    // ==========================================

    public void takeDamage(int damage) {

        if (isDestroyed() || invulnerableTimer > 0f) {
            return;
        }

        hp -= damage;

        if (hp < 0) {
            hp = 0;
        }

        notifyHpChanged();

        if (hp <= 0) {
            loseLife();
        }
    }

    public void loseLife() {

        lives--;
        notifyLivesChanged();

        if (lives > 0) {
            respawn();
        }
        else {
            hp = 0;
            notifyHpChanged();
            destroy();

            System.out.println("[Player] Game Over!");
        }
    }

    public void respawn() {

        hp = maxHp;
        x = 200f;
        y = 50f;

        invulnerableTimer = 3f;

        shootTimer = shootCooldown;
        homingShootTimer = homingCooldown;

        activate();

        notifyHpChanged();

        updatePowerStrategy();

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
        homingShootTimer = homingCooldown;

        invulnerableTimer = 0f;

        x = 200f;
        y = 50f;

        activate();

        updatePowerStrategy();

        notifyHpChanged();
        notifyLivesChanged();
        notifySpellCardsChanged();
        notifyPowerChanged();
        notifyScoreChanged();
        notifyGrazeChanged();

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
