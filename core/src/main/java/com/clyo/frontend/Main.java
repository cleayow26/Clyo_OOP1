
package com.clyo.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import com.clyo.frontend.commands.Command;
import com.clyo.frontend.commands.InputHandler;
import com.clyo.frontend.ui.GameHUD;

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

    private Item powerItem;
    private Item pointItem;

    private List<Fairy> fairy;
    private List<GameObject> entities;

    private BulletManager bulletManager;
    private CollisionReferee collisionReferee;

    private InputHandler inputHandler;
    private GameHUD gameHUD;

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

        // CREATE PLAYER
        player = EntityFactory.createPlayer(
                200, 50, "Reimu Hakurei", 100, 15, 3
        );

        // CREATE FAIRIES
        fairy.add(
                EntityFactory.createFairy(
                        150, 380, "Red Fairy", 20
                )
        );

        fairy.add(
                EntityFactory.createFairy(
                        250, 380, "Blue Fairy", 20,
                        "fairy_idle_blue"
                )
        );

        // CREATE BOSS
        boss = EntityFactory.createBoss(
                250, 400, "Rumia", 150
        );

        // CREATE ITEMS
        powerItem = EntityFactory.createItem(
                180, 450, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
                220, 480, ItemType.POINT
        );

        // REGISTER ENTITIES
        entities.add(player);
        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        // INITIALIZE INPUT HANDLER AND HUD
        inputHandler = new InputHandler(entities);
        gameHUD = new GameHUD();

        // REGISTER HUD AS PLAYER OBSERVER
        player.registerObserver(gameHUD);

        System.out.println("Total entities: " + entities.size());
    }

    // UPDATE AND CLEAN ENTITIES
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

    // BOSS SHOOTING SCHEDULER
    private void updateEnemyScheduler(float delta) {

        enemyShootTimer += delta;

        if (enemyShootTimer >= 1.5f) {

            if (boss != null && !boss.isDestroyed()) {
                boss.shootBullet(bulletManager);
            }

            enemyShootTimer = 0f;
        }
    }

    // RESTART GAME
    private void restartGame() {

        System.out.println("[Main] Restarting game...");

        entities.clear();

        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();

        player.resetGame();

        entities.add(player);

        fairy.clear();

        fairy.add(
                EntityFactory.createFairy(
                        150, 380, "Red Fairy", 20
                )
        );

        fairy.add(
                EntityFactory.createFairy(
                        250, 380, "Blue Fairy", 20,
                        "fairy_idle_blue"
                )
        );

        boss = EntityFactory.createBoss(
                250, 400, "Rumia", 150
        );

        powerItem = EntityFactory.createItem(
                180, 450, ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
                220, 480, ItemType.POINT
        );

        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        inputHandler.setEntities(entities);

        enemyShootTimer = 0f;
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        // RESTART WHEN GAME OVER
        if (Gdx.input != null
                && player.isGameOver()
                && Gdx.input.isKeyJustPressed(Input.Keys.R)) {

            restartGame();
        }

        // HANDLE PLAYER COMMANDS
        if (!player.isGameOver()) {

            List<Command> commands =
                    inputHandler.handleInput(delta);

            for (Command command : commands) {
                command.execute(player, bulletManager);
            }
        }

        // UPDATE ENEMY SCHEDULER
        if (!player.isGameOver()) {
            updateEnemyScheduler(delta);
        }

        // UPDATE ENTITIES
        updateAndClean(
                entities,
                delta,
                416,
                560
        );

        // UPDATE BULLETS
        bulletManager.update(delta, 416, 560);

        // CHECK COLLISIONS
        collisionReferee.resolveCollisions(
                player,
                entities,
                bulletManager
        );

        // CLEAR SCREEN
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // DRAW SPRITES AND HUD
        batch.begin();

        for (GameObject entity : entities) {

            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        bulletManager.render(batch);

        gameHUD.render(batch);

        batch.end();

        // DRAW ENTITY SHAPES
        shapeRenderer.begin(
                ShapeRenderer.ShapeType.Filled
        );

        for (GameObject entity : entities) {

            if (!entity.isDestroyed()) {
                entity.render(shapeRenderer);
            }
        }

        shapeRenderer.end();

        // DRAW HUD BACKGROUND
        gameHUD.renderBackground();
    }

    @Override
    public void dispose() {

        if (gameHUD != null) {
            gameHUD.dispose();
        }

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
