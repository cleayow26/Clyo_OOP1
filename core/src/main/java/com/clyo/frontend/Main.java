
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

import com.clyo.frontend.objects.patterns.entityStrategy.SCurveMovement;
import com.clyo.frontend.objects.patterns.entityStrategy.TargetPointMovement;
import com.clyo.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.clyo.frontend.objects.patterns.shootingStrategy.AlternatingRingShot;

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

    private AssetManager assetManager;
    private AudioSystem audioSystem;

    // ==========================================
    // FAIRY WAVE VARIABLES
    // ==========================================

    private float fairySpawnTimer = 0f;
    private int fairySpawnIndex = 0;

    private final float[] fairySpawnX = {
            60f, 68f, 76f, 84f, 92f
    };

    private static final float FAIRY_FIRST_SPAWN = 1f;
    private static final float FAIRY_SPAWN_INTERVAL = 0.267f;

    // ==========================================
    // BOSS VARIABLES
    // ==========================================

    private float enemyShootTimer = 0f;

    private float bossPhaseTimer = 0f;
    private int currentBossPhase = 0;

    private static final float BOSS_SHOOT_INTERVAL = 1.5f;
    private static final float BOSS_PHASE_INTERVAL = 4f;

    // ==========================================
    // CREATE
    // ==========================================

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

        // ======================================
        // CREATE PLAYER
        // ======================================

        player = EntityFactory.createPlayer(
                200, 50,
                "Reimu Hakurei",
                100,
                15,
                3
        );

        // ======================================
        // CREATE BOSS
        // ======================================

        boss = EntityFactory.createBoss(
                200, 420,
                "Rumia",
                150
        );

        configureBoss();

        // ======================================
        // CREATE ITEMS
        // ======================================

        powerItem = EntityFactory.createItem(
                180, 450,
                ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
                220, 480,
                ItemType.POINT
        );

        // ======================================
        // REGISTER ENTITIES
        // ======================================

        entities.add(player);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        // Give BulletManager access to entities
        bulletManager.setEntities(entities);

        // ======================================
        // INPUT HANDLER AND HUD
        // ======================================

        inputHandler = new InputHandler(entities);
        gameHUD = new GameHUD();

        player.registerObserver(gameHUD);

        System.out.println("[Main] Game initialized!");
        System.out.println("[Main] Total entities: " + entities.size());
    }

    // ==========================================
    // CONFIGURE BOSS
    // ==========================================

    private void configureBoss() {

        if (boss == null) {
            return;
        }

        boss.setX(200f);
        boss.setY(420f);

        boss.setMovementPattern(
                new TargetPointMovement(
                        200f,
                        420f,
                        150f
                )
        );

        boss.setShootingPattern(
                new SpreadShot(
                        220f,
                        5,
                        25f,
                        10,
                        player,
                        "bullet_danmaku_red"
                )
        );

        enemyShootTimer = 0f;
        bossPhaseTimer = 0f;
        currentBossPhase = 0;
    }

    // ==========================================
    // FAIRY WAVE SPAWNING
    // ==========================================

    private void updateFairyWave(float delta) {

        fairySpawnTimer += delta;

        while (fairySpawnIndex < fairySpawnX.length) {

            float requiredTime = FAIRY_FIRST_SPAWN
                    + fairySpawnIndex * FAIRY_SPAWN_INTERVAL;

            if (fairySpawnTimer < requiredTime) {
                break;
            }

            spawnFairy(fairySpawnIndex);

            fairySpawnIndex++;
        }
    }

    // ==========================================
    // SPAWN FAIRY
    // ==========================================

    private void spawnFairy(int index) {

        float spawnX = fairySpawnX[index];

        Fairy newFairy = EntityFactory.createFairy(
                spawnX,
                480f,
                "Left S-Curve Fairy",
                8,
                "fairy_idle_blue"
        );

        newFairy.setMovementPattern(
                new SCurveMovement(
                        130f,
                        (float) Math.toRadians(270f),
                        false
                )
        );

        fairy.add(newFairy);
        entities.add(newFairy);

        System.out.println(
                "[Main] Fairy spawned: " + (index + 1)
        );
    }

    // ==========================================
    // FAIRY SHOOTING SCHEDULER
    // ==========================================

    private void updateFairyShooting(float delta) {

        for (Fairy currentFairy : fairy) {

            if (currentFairy == null
                    || currentFairy.isDestroyed()) {
                continue;
            }

            if (currentFairy.canShoot(delta)) {

                currentFairy.shootBullet(bulletManager);
            }
        }
    }

    // ==========================================
    // CLEAN FAIRY LIST
    // ==========================================

    private void cleanFairyList() {

        Iterator<Fairy> iterator = fairy.iterator();

        while (iterator.hasNext()) {

            Fairy currentFairy = iterator.next();

            if (currentFairy == null
                    || currentFairy.isDestroyed()
                    || currentFairy.isOffScreen(416, 560)) {

                iterator.remove();
            }
        }
    }

    // ==========================================
    // BOSS SHOOTING SCHEDULER
    // ==========================================

    private void updateEnemyScheduler(float delta) {

        if (boss == null || boss.isDestroyed()) {
            return;
        }

        enemyShootTimer += delta;

        if (enemyShootTimer >= BOSS_SHOOT_INTERVAL) {

            boss.shootBullet(bulletManager);

            enemyShootTimer = 0f;
        }
    }

    // ==========================================
    // BOSS PHASE SWITCHING
    // ==========================================

    private void updateBossPhase(float delta) {

        if (boss == null || boss.isDestroyed()) {
            return;
        }

        bossPhaseTimer += delta;

        if (bossPhaseTimer >= BOSS_PHASE_INTERVAL) {

            bossPhaseTimer = 0f;

            currentBossPhase = (currentBossPhase + 1) % 2;

            if (currentBossPhase == 0) {

                // PHASE 0: TARGETED SPREAD SHOT

                boss.setMovementPattern(
                        new TargetPointMovement(
                                120f,
                                420f,
                                150f
                        )
                );

                boss.setShootingPattern(
                        new SpreadShot(
                                220f,
                                5,
                                25f,
                                10,
                                player,
                                "bullet_danmaku_red"
                        )
                );

                System.out.println(
                        "[Boss] Phase 0: Targeted Spread Shot"
                );

            } else {

                // PHASE 1: ALTERNATING RING SHOT

                boss.setMovementPattern(
                        new TargetPointMovement(
                                280f,
                                420f,
                                150f
                        )
                );

                boss.setShootingPattern(
                        new AlternatingRingShot(
                                180f,
                                16,
                                10
                        )
                );

                System.out.println(
                        "[Boss] Phase 1: Alternating Ring Shot"
                );
            }
        }
    }

    // ==========================================
    // UPDATE AND CLEAN ENTITIES
    // ==========================================

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
                            screenHeight
                    )) {

                iterator.remove();
            }
        }
    }

    // ==========================================
    // RESTART GAME
    // ==========================================

    private void restartGame() {

        System.out.println("[Main] Restarting game...");

        entities.clear();

        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();

        fairy.clear();

        player.resetGame();

        // Reset wave timers
        fairySpawnTimer = 0f;
        fairySpawnIndex = 0;

        // Reset boss
        boss = EntityFactory.createBoss(
                200, 420,
                "Rumia",
                150
        );

        configureBoss();

        // Recreate items
        powerItem = EntityFactory.createItem(
                180, 450,
                ItemType.POWER
        );

        pointItem = EntityFactory.createItem(
                220, 480,
                ItemType.POINT
        );

        // Restore entities
        entities.add(player);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        bulletManager.setEntities(entities);

        inputHandler.setEntities(entities);

        System.out.println("[Main] Game restarted!");
    }

    // ==========================================
    // RENDER
    // ==========================================

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        // ======================================
        // RESTART WHEN GAME OVER
        // ======================================

        if (Gdx.input != null
                && player.isGameOver()
                && Gdx.input.isKeyJustPressed(Input.Keys.R)) {

            restartGame();
        }

        // ======================================
        // PLAYER INPUT
        // ======================================

        if (!player.isGameOver()) {

            List<Command> commands =
                    inputHandler.handleInput(delta);

            for (Command command : commands) {
                command.execute(player, bulletManager);
            }

            // ==================================
            // FAIRY WAVE
            // ==================================

            updateFairyWave(delta);

            updateFairyShooting(delta);

            // ==================================
            // BOSS SCHEDULERS
            // ==================================

            updateEnemyScheduler(delta);
            updateBossPhase(delta);
        }

        // ======================================
        // UPDATE ENTITIES
        // ======================================

        updateAndClean(
                entities,
                delta,
                416,
                560
        );

        cleanFairyList();

        // ======================================
        // UPDATE BULLETS
        // ======================================

        bulletManager.update(
                delta,
                416,
                560
        );

        // ======================================
        // COLLISIONS
        // ======================================

        collisionReferee.resolveCollisions(
                player,
                entities,
                bulletManager
        );

        // ======================================
        // CLEAR SCREEN
        // ======================================

        ScreenUtils.clear(
                0.1f,
                0.1f,
                0.15f,
                1f
        );

        // ======================================
        // DRAW SPRITES AND HUD
        // ======================================

        batch.begin();

        for (GameObject entity : entities) {

            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        bulletManager.render(batch);

        gameHUD.render(batch);

        batch.end();

        // ======================================
        // DRAW ENTITY SHAPES
        // ======================================

        shapeRenderer.begin(
                ShapeRenderer.ShapeType.Filled
        );

        for (GameObject entity : entities) {

            if (!entity.isDestroyed()) {
                entity.render(shapeRenderer);
            }
        }

        shapeRenderer.end();

        // ======================================
        // HUD BACKGROUND
        // ======================================

        gameHUD.renderBackground();
    }

    // ==========================================
    // DISPOSE
    // ==========================================

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
