
package com.clyo.frontend.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.BulletType;
import com.clyo.frontend.objects.items.Item;
import com.clyo.frontend.objects.items.ItemType;
import com.clyo.frontend.objects.bullets.Bullet;

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

    public BulletManager() {
        playerBulletPool = new LinkedList<>();
        enemyBulletPool = new LinkedList<>();

        activePlayerBullets = new ArrayList<>();
        activeEnemyBullets = new ArrayList<>();
    }

    // ==========================================
    // QUESTION 1: SPAWN PLAYER BULLET
    // ==========================================

    public Bullet spawnPlayerBullet(
            float x,
            float y,
            float vx,
            float vy,
            int damage) {

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

        activePlayerBullets.add(bullet);

        return bullet;
    }

    // ==========================================
    // QUESTION 1: SPAWN ENEMY BULLET
    // ==========================================

    public Bullet spawnEnemyBullet(
            float x,
            float y,
            float vx,
            float vy,
            int damage) {

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

        activeEnemyBullets.add(bullet);

        return bullet;
    }

    // ==========================================
    // QUESTION 2: UPDATE AND RECYCLE BULLETS
    // ==========================================

    public void update(
            float delta,
            float screenWidth,
            float screenHeight) {

        Iterator<Bullet> playerIterator =
                activePlayerBullets.iterator();

        while (playerIterator.hasNext()) {

            Bullet bullet = playerIterator.next();

            bullet.update(delta);

            if (bullet.isDestroyed()
                    || bullet.isOffScreen(
                            screenWidth,
                            screenHeight)) {

                bullet.destroy();

                playerIterator.remove();

                playerBulletPool.offer(bullet);
            }
        }

        Iterator<Bullet> enemyIterator =
                activeEnemyBullets.iterator();

        while (enemyIterator.hasNext()) {

            Bullet bullet = enemyIterator.next();

            bullet.update(delta);

            if (bullet.isDestroyed()
                    || bullet.isOffScreen(
                            screenWidth,
                            screenHeight)) {

                bullet.destroy();

                enemyIterator.remove();

                enemyBulletPool.offer(bullet);
            }
        }
    }

    // ==========================================
    // PROBLEM 3: CLEAR ENEMY BULLETS
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

            iterator.remove();

            enemyBulletPool.offer(bullet);
        }
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    // ==========================================
    // PROBLEM 3: CLEAR PLAYER BULLETS
    // ==========================================

    public void clearPlayerBullets() {

        Iterator<Bullet> pIter =
                activePlayerBullets.iterator();

        while (pIter.hasNext()) {

            Bullet bullet = pIter.next();

            bullet.destroy();

            pIter.remove();

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
