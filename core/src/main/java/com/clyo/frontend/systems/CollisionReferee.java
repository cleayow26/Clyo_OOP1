
package com.clyo.frontend.systems;

import java.util.ArrayList;
import java.util.List;

import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.Player;
import com.clyo.frontend.objects.bullets.Bullet;
import com.clyo.frontend.objects.enemies.Enemy;
import com.clyo.frontend.objects.items.Item;
import com.clyo.frontend.objects.items.ItemType;

public class CollisionReferee {

    public void resolveCollisions(
            Player player,
            List<GameObject> entities,
            BulletManager bulletManager) {

        List<GameObject> newDrops = new ArrayList<>();

        // ==========================================
        // 1. PLAYER BULLETS VS ENEMIES
        // ==========================================

        List<Bullet> activePlayerBullets =
                bulletManager.getActivePlayerBullets();

        for (Bullet bullet : activePlayerBullets) {

            if (bullet.isDestroyed()) {
                continue;
            }

            for (GameObject entity : entities) {

                if (entity instanceof Enemy) {

                    Enemy enemy = (Enemy) entity;

                    if (enemy.isDestroyed()) {
                        continue;
                    }

                    if (bullet.getCoreHitbox().overlaps(
                            enemy.getCoreHitbox())) {

                        enemy.takeDamage(bullet.getDamage());

                        if (enemy.isDestroyed()) {

                            player.addScore(
                                    enemy.getScoreValue()
                            );

                            newDrops.add(
                                    EntityFactory.createItem(
                                            enemy.getX(),
                                            enemy.getY(),
                                            ItemType.POWER
                                    )
                            );
                        }

                        bullet.destroy();
                        break;
                    }
                }
            }
        }

        // Add drops after checking entities
        entities.addAll(newDrops);

        // ==========================================
        // 2. ENEMY BULLETS VS PLAYER
        // ==========================================

        List<Bullet> activeEnemyBullets =
                bulletManager.getActiveEnemyBullets();

        for (Bullet bullet : activeEnemyBullets) {

            if (bullet.isDestroyed() || player.isDestroyed()) {
                continue;
            }

            // Direct hit
            if (bullet.getCoreHitbox().overlaps(
                    player.getCoreHitbox())) {

                if (!player.isInvulnerable()) {

                    System.out.println(
                            "[CollisionReferee] Player hit by enemy bullet!"
                    );

                    player.takeDamage(100);

                    bullet.destroy();
                    break;
                }
            }

            // Graze detection
            else if (!bullet.isGrazed()
                    && bullet.getCoreHitbox().overlaps(
                            player.getGrazeHitbox())) {

                bullet.setGrazed(true);

                player.addGraze();

                System.out.println(
                        "[CollisionReferee] Player grazed an enemy bullet!"
                );
            }
        }

        // ==========================================
        // 3. PLAYER VS ITEMS
        // ==========================================

        for (GameObject entity : entities) {

            if (entity instanceof Item) {

                Item item = (Item) entity;

                if (item.isDestroyed()) {
                    continue;
                }

                if (item.getCoreHitbox().overlaps(
                        player.getCoreHitbox())
                        || item.getCoreHitbox().overlaps(
                        player.getGrazeHitbox())) {

                    player.collectItem(item);
                }
            }
        }

        // ==========================================
        // 4. PLAYER VS ENEMY BODY
        // ==========================================

        for (GameObject entity : entities) {

            if (entity instanceof Enemy) {

                Enemy enemy = (Enemy) entity;

                if (enemy.isDestroyed() || player.isDestroyed()) {
                    continue;
                }

                if (enemy.getCoreHitbox().overlaps(
                        player.getCoreHitbox())) {

                    if (!player.isInvulnerable()) {

                        System.out.println(
                                "[CollisionReferee] CORE COLLISION! Player collided with "
                                        + enemy.getName() + "!"
                        );

                        player.takeDamage(100);
                    }
                }
            }
        }
    }
}
