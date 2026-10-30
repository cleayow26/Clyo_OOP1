
package com.clyo.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import com.clyo.frontend.objects.Player;
import com.clyo.frontend.objects.enemies.Boss;
import com.clyo.frontend.objects.enemies.Fairy;
import com.clyo.frontend.objects.items.Item;
import com.clyo.frontend.objects.items.ItemType;

import com.clyo.frontend.systems.AssetManager;
import com.clyo.frontend.systems.AudioSystem;
import com.clyo.frontend.systems.BulletManager;
import com.clyo.frontend.systems.CollisionReferee;
import com.clyo.frontend.systems.EntityFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    private Player player;
    private Boss boss;

    private List<Fairy> fairy;
    private List<GameObject> entities;

    private BulletManager bulletManager;
    private CollisionReferee collisionReferee;

    private float enemyShootTimer = 0f;

    private AssetManager assetManager;
    private AudioSystem audioSystem;

    @Override
    public void create() {

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        bulletManager = new BulletManager();
        collisionReferee = new CollisionReferee();

        assetManager = AssetManager.getInstance();
        assetManager.init();

        audioSystem = AudioSystem.getInstance();
        audioSystem.init();

        player = EntityFactory.createPlayer(
                280, 40, "Reimu Hakurei", 100, 15, 3
        );

        Fairy redFairy = EntityFactory.createFairy(
                150, 380, "Red Fairy", 20
        );

        Fairy blueFairy = EntityFactory.createFairy(
                250, 380, "Blue Fairy", 20,
                "fairy_idle_blue"
        );

        boss = EntityFactory.createBoss(
                380, 400, "Rumia", 150
        );

        Item powerItem = EntityFactory.createItem(
                200, 450, ItemType.POWER
        );

        Item pointItem = EntityFactory.createItem(
                320, 480, ItemType.POINT
        );

        fairy.add(redFairy);
        fairy.add(blueFairy);

        entities.add(player);
        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        System.out.println(
                "Total entities: " + entities.size()
        );
    }

    // =====================================
    // UPDATE AND CLEAN
    // =====================================

    public <T extends GameObject> void updateAndClean(
            List<T> list,
            float delta,
            float screenWidth,
            float screenHeight) {

        Iterator<T> iterator = list.iterator();

        while (iterator.hasNext()) {

            T entity = iterator.next();

            entity.update(delta);

            if (entity.isDestroyed()
                    || entity.isOffScreen(
                            screenWidth,
                            screenHeight)) {

                iterator.remove();
            }
        }
    }

    // =====================================
    // BOSS SHOOTING SCHEDULER
    // =====================================

    private void updateEnemyScheduler(float delta) {

        enemyShootTimer += delta;

        if (enemyShootTimer >= 1.5f) {

            if (boss != null && !boss.isDestroyed()) {
                boss.shootBullet(bulletManager);
            }

            enemyShootTimer = 0f;
        }
    }

    // =====================================
    // MAIN GAME LOOP
    // =====================================

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // PLAYER SHOOTING

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {

            player.shootBullet(bulletManager);

            audioSystem.playShoot();
        }

        // BOSS SHOOTING

        updateEnemyScheduler(delta);

        // UPDATE NORMAL ENTITIES

        updateAndClean(
                entities,
                delta,
                screenWidth,
                screenHeight
        );

        // UPDATE BULLETS
        bulletManager.update(
                delta,
                screenWidth,
                screenHeight
        );

        // COLLISION REFEREE

        collisionReferee.resolveCollisions(
                player,
                entities,
                bulletManager
        );

        // CLEAR SCREEN

        ScreenUtils.clear(
                0.1f, 0.1f, 0.15f, 1f
        );

        // RENDER SPRITES

        batch.begin();

        for (GameObject entity : entities) {

            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        bulletManager.render(batch);

        batch.end();

        // RENDER SHAPES

        shapeRenderer.begin(
                ShapeRenderer.ShapeType.Filled
        );

        for (GameObject entity : entities) {

            if (!entity.isDestroyed()) {
                entity.render(shapeRenderer);
            }
        }

        shapeRenderer.end();
    }

    // =====================================
    // DISPOSE
    // =====================================

    @Override
    public void dispose() {

        if (audioSystem != null) {
            audioSystem.dispose();
            audioSystem = null;
        }

        if (batch != null) {
            batch.dispose();
            batch = null;
        }

        if (shapeRenderer != null) {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }

        if (assetManager != null) {
            assetManager.dispose();
            assetManager = null;
        }
    }
}
