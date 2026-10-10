
package com.clyo.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import com.clyo.frontend.objects.Player;
import com.clyo.frontend.objects.bullets.Bullet;
import com.clyo.frontend.objects.enemies.Boss;
import com.clyo.frontend.objects.enemies.Fairy;
import com.clyo.frontend.objects.items.Item;
import com.clyo.frontend.objects.items.ItemType;
import com.clyo.frontend.systems.AssetManager;
import com.clyo.frontend.systems.EntityFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    private Player player;
    private List<Fairy> fairy;
    private List<GameObject> entities;

    private AssetManager assetManager;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        fairy = new ArrayList<>();
        entities = new ArrayList<>();

        // Initialize assets before creating entities
        assetManager = AssetManager.getInstance();
        assetManager.init();

        // Create Player
        player = EntityFactory.createPlayer(
                280, 40, "Reimu Hakurei", 100, 15, 3
        );

        // Create Red Fairy
        Fairy redFairy = EntityFactory.createFairy(
                150, 380, "Red Fairy", 20
        );

        // Create Blue Fairy
        Fairy blueFairy = EntityFactory.createFairy(
                250, 380, "Blue Fairy", 20,
                "fairy_idle_blue"
        );

        // Create Boss
        Boss boss = EntityFactory.createBoss(
                380, 400, "Rumia", 150
        );

        // Create Items
        Item powerItem = EntityFactory.createItem(
                200, 450, ItemType.POWER
        );

        Item pointItem = EntityFactory.createItem(
                320, 480, ItemType.POINT
        );

        // Add Fairies to their list
        fairy.add(redFairy);
        fairy.add(blueFairy);

        // Add all entities to the main list
        entities.add(player);
        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        System.out.println("Total entities: " + entities.size());
    }

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
                            screenWidth, screenHeight)) {

                System.out.println(
                        "Removed via Generic Iterator: "
                        + entity.getClass().getSimpleName()
                );

                iterator.remove();
            }
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Shoot a bullet by pressing Z
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            Bullet bullet = player.shootBullet();
            entities.add(bullet);
        }

        updateAndClean(
                entities,
                delta,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        // Collision detection
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {

                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                if (!a.isDestroyed()
                        && !b.isDestroyed()
                        && a.getCoreHitbox().overlaps(
                                b.getCoreHitbox())) {

                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }

        // Clear the background
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // Render sprites and animations
        batch.begin();

        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }

        batch.end();

        // Render fallback shapes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(shapeRenderer);
            }
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }

        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }

        if (assetManager != null) {
            assetManager.dispose();
        }
    }
}
