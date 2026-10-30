
package com.clyo.frontend.systems;

import com.clyo.frontend.GameObject;
import com.clyo.frontend.Collidable;

import com.clyo.frontend.objects.Player;
import com.clyo.frontend.objects.bullets.Bullet;
import com.clyo.frontend.objects.enemies.Enemy;
import com.clyo.frontend.objects.items.Item;
import com.clyo.frontend.objects.items.ItemType;

import java.util.ArrayList;
import java.util.List;

public class CollisionReferee {

    public void resolveCollisions(
            Player player,
            List<GameObject> entities,
            BulletManager bulletManager) {

        if (player == null
                || entities == null
                || bulletManager == null) {
            return;
        }

        List<GameObject> newDrops = new ArrayList<>();

        // =====================================
        // QUESTION 3:
        // PLAYER BULLETS VS ENEMIES
        // =====================================

        for (Bullet bullet :
                bulletManager.getActivePlayerBullets()) {

            if (bullet.isDestroyed()) {
                continue;
            }

            for (GameObject entity : entities) {

                if (entity instanceof Enemy
                        && !entity.isDestroyed()) {

                    Enemy enemy = (Enemy) entity;

                    if (bullet.getCoreHitbox().overlaps(
                            enemy.getCoreHitbox())) {

                        enemy.takeDamage(bullet.getDamage());

                        if (enemy.isDestroyed()) {

                            player.addScore(
                                    enemy.getScoreValue()
                            );

                            Item powerDrop =
                                    EntityFactory.createItem(
                                            enemy.getX(),
                                            enemy.getY(),
                                            ItemType.POWER
                                    );

                            newDrops.add(powerDrop);
                        }

                        bullet.destroy();
                        break;
                    }
                }
            }
        }

        // =====================================
        // QUESTION 4:
        // ENEMY BULLETS VS PLAYER
        // CORE HIT AND GRAZE
        // =====================================

        for (Bullet bullet :
                bulletManager.getActiveEnemyBullets()) {

            if (bullet.isDestroyed()
                    || player.isDestroyed()) {
                continue;
            }

            // PRIORITY 1: CORE HIT

            if (bullet.getCoreHitbox().overlaps(
                    player.getCoreHitbox())) {

                player.onCollision(bullet);

                bullet.destroy();

                continue;
            }

            // PRIORITY 2: GRAZE HIT

            if (!bullet.hasBeenGrazed()
                    && bullet.getCoreHitbox().overlaps(
                            player.getGrazeHitbox())) {

                bullet.setGrazed(true);

                player.addScore(50);

                System.out.println(
                        "[CollisionReferee] GRAZE! "
                        + "+50 pts!"
                );
            }
        }

        // =====================================
        // QUESTION 5:
        // PLAYER VS ITEMS AND ENEMY BODY
        // =====================================

        for (GameObject entity : entities) {

            if (entity.isDestroyed()
                    || player.isDestroyed()) {
                continue;
            }

            // CASE A: ITEM COLLECTION

            if (entity instanceof Item) {

                Item item = (Item) entity;

                if (item.getCoreHitbox().overlaps(
                        player.getCoreHitbox())
                        || item.getCoreHitbox().overlaps(
                                player.getGrazeHitbox())) {

                    player.collectItem(item);
                }
            }

            // CASE B: ENEMY BODY CONTACT

            if (entity instanceof Enemy) {

                Enemy enemy = (Enemy) entity;

                if (enemy.getCoreHitbox().overlaps(
                        player.getCoreHitbox())) {

                    player.onCollision(enemy);
                }
            }
        }

        entities.addAll(newDrops);
    }
}
