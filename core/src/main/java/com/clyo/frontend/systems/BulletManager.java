
package com.clyo.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.BulletType;
import com.clyo.frontend.objects.bullets.Bullet;
import com.clyo.frontend.objects.items.Item;
import com.clyo.frontend.objects.items.ItemType;
import com.clyo.frontend.objects.patterns.bulletStrategy.BulletMovementPattern;
import com.clyo.frontend.objects.patterns.bulletStrategy.FantasySealMovement;
import com.clyo.frontend.objects.patterns.bulletStrategy.HomingBulletMovement;
import com.clyo.frontend.objects.patterns.shootingStrategy.RingShot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BulletManager {

    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;

    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;

    private List<GameObject> entities;

    public BulletManager() {
        playerBulletPool = new LinkedList<>();
        enemyBulletPool = new LinkedList<>();

        activePlayerBullets = new ArrayList<>();
        activeEnemyBullets = new ArrayList<>();

        entities = new ArrayList<>();
    }

    // ==========================================
    // ENTITY MANAGEMENT
    // ==========================================

    public void setEntities(List<GameObject> entities) {
        this.entities = entities != null
                ? entities
                : new ArrayList<>();
    }

    public List<GameObject> getEntities() {
        return entities;
    }

    // ==========================================
    // SPAWN PLAYER BULLET
    // ==========================================

    public Bullet spawnPlayerBullet(
            float x,
            float y,
            float vx,
            float vy,
            int damage) {

        return spawnPlayerBullet(
                x, y, vx, vy, damage, null
        );
    }

    public Bullet spawnPlayerBullet(
            float x,
            float y,
            float vx,
            float vy,
            int damage,
            String spriteKey) {

        Bullet bullet;

        if (!playerBulletPool.isEmpty()) {
            bullet = playerBulletPool.poll();
        } else {
            bullet = new Bullet(
                    x,
                    y,
                    400f,
                    BulletType.AMULET,
                    damage
            );
        }

        bullet.init(
                x,
                y,
                vx,
                vy,
                BulletType.AMULET,
                damage,
                true
        );

        applySprite(bullet, spriteKey);

        activePlayerBullets.add(bullet);

        return bullet;
    }

    // ==========================================
    // SPAWN ENEMY BULLET
    // ==========================================

    public Bullet spawnEnemyBullet(
            float x,
            float y,
            float vx,
            float vy,
            int damage) {

        return spawnEnemyBullet(
                x, y, vx, vy, damage, null
        );
    }

    public Bullet spawnEnemyBullet(
            float x,
            float y,
            float vx,
            float vy,
            int damage,
            String spriteKey) {

        Bullet bullet;

        if (!enemyBulletPool.isEmpty()) {
            bullet = enemyBulletPool.poll();
        } else {
            bullet = new Bullet(
                    x,
                    y,
                    400f,
                    BulletType.DANMAKU,
                    damage
            );
        }

        bullet.init(
                x,
                y,
                vx,
                vy,
                BulletType.DANMAKU,
                damage,
                false
        );

        applySprite(bullet, spriteKey);

        activeEnemyBullets.add(bullet);

        return bullet;
    }

    // ==========================================
    // APPLY CUSTOM BULLET SPRITE
    // ==========================================

    private void applySprite(Bullet bullet, String spriteKey) {

        if (bullet == null) {
            return;
        }

        String key = spriteKey != null
                ? spriteKey
                : "bullet_danmaku";

        if (AssetManager.getInstance().getTextureRegion(key) != null) {
            bullet.setSprite(
                    AssetManager.getInstance().getTextureRegion(key)
            );
        }
    }

    // ==========================================
    // SPAWN BOMB ORBS
    // ==========================================

    public void spawnBombOrbs(
            float x,
            float y,
            GameObject target,
            List<GameObject> entities) {

        int orbCount = 8;
        float orbSpeed = 260f;
        int damage = 50;

        RingShot ringShot = new RingShot(
                orbSpeed,
                orbCount,
                damage
        );

        ringShot.execute(x, y, this, true);

        // Apply homing movement to the newly spawned orbs.
        int firstNewBullet = activePlayerBullets.size() - orbCount;

        for (int i = Math.max(0, firstNewBullet);
             i < activePlayerBullets.size();
             i++) {

            Bullet bullet = activePlayerBullets.get(i);

            bullet.setMovementPattern(
                    new FantasySealMovement(target, orbSpeed, 5f)
            );
        }

        if (entities != null) {
            setEntities(entities);
        }
    }

    // ==========================================
    // UPDATE AND RECYCLE BULLETS
    // ==========================================

    public void update(
            float delta,
            float screenWidth,
            float screenHeight) {

        updateBulletList(
                activePlayerBullets,
                playerBulletPool,
                delta,
                screenWidth,
                screenHeight
        );

        updateBulletList(
                activeEnemyBullets,
                enemyBulletPool,
                delta,
                screenWidth,
                screenHeight
        );
    }

    private void updateBulletList(
            List<Bullet> activeBullets,
            Queue<Bullet> pool,
            float delta,
            float screenWidth,
            float screenHeight) {

        Iterator<Bullet> iterator = activeBullets.iterator();

        while (iterator.hasNext()) {

            Bullet bullet = iterator.next();

            if (bullet.getMovementPattern()
                    instanceof HomingBulletMovement) {

                HomingBulletMovement homing =
                        (HomingBulletMovement) bullet.getMovementPattern();

                if (homing.getEntities() == null) {
                    homing.setEntities(entities);
                }
            }

            bullet.update(delta);

            if (bullet.isDestroyed()
                    || bullet.isOffScreen(screenWidth, screenHeight)) {

                bullet.destroy();

                iterator.remove();

                bullet.setMovementPattern(null);

                pool.offer(bullet);
            }
        }
    }

    // ==========================================
    // CLEAR ENEMY BULLETS
    // ==========================================

    public void clearEnemyBullets(List<GameObject> entities) {

        Iterator<Bullet> iterator =
                activeEnemyBullets.iterator();

        while (iterator.hasNext()) {

            Bullet bullet = iterator.next();

            if (entities != null) {

                Item pointItem = EntityFactory.createItem(
                        bullet.getX(),
                        bullet.getY(),
                        ItemType.POINT
                );

                entities.add(pointItem);
            }

            bullet.destroy();

            bullet.setMovementPattern(null);

            iterator.remove();

            enemyBulletPool.offer(bullet);
        }
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    // ==========================================
    // CLEAR PLAYER BULLETS
    // ==========================================

    public void clearPlayerBullets() {

        Iterator<Bullet> iterator =
                activePlayerBullets.iterator();

        while (iterator.hasNext()) {

            Bullet bullet = iterator.next();

            bullet.destroy();

            bullet.setMovementPattern(null);

            iterator.remove();

            playerBulletPool.offer(bullet);
        }
    }

    // ==========================================
    // RENDER BULLETS
    // ==========================================

    public void render(SpriteBatch batch) {

        for (Bullet bullet : activePlayerBullets) {

            if (!bullet.isDestroyed()) {
                bullet.render(batch);
            }
        }

        for (Bullet bullet : activeEnemyBullets) {

            if (!bullet.isDestroyed()) {
                bullet.render(batch);
            }
        }
    }

    // ==========================================
    // GETTERS
    // ==========================================

    public List<Bullet> getActivePlayerBullets() {
        return activePlayerBullets;
    }

    public List<Bullet> getActiveEnemyBullets() {
        return activeEnemyBullets;
    }

    public int getPlayerPoolSize() {
        return playerBulletPool.size();
    }

    public int getEnemyPoolSize() {
        return enemyBulletPool.size();
    }
}
