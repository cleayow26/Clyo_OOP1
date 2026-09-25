package com.clyo.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import com.clyo.frontend.objects.Player;
import com.clyo.frontend.objects.bullets.Bullet;
import com.clyo.frontend.objects.enemies.Boss;
import com.clyo.frontend.objects.enemies.Fairy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {

    private ShapeRenderer shapeRenderer;

    private Player player;

    private List<GameObject> entities;


    @Override
    public void create() {

        shapeRenderer =
                new ShapeRenderer();

        entities =
                new ArrayList<>();


        player = new Player(
                320,
                50,
                "Reimu Hakurei",
                100,
                100,
                25
        );

        Fairy fairy =
                new Fairy(
                        300,
                        400
                );

        Boss boss =
                new Boss(
                        500,
                        500
                );


        entities.add(player);

        entities.add(fairy);

        entities.add(boss);
    }


    // =================================================
    // QUESTION 2
    // GENERIC UPDATE AND CLEAN
    // =================================================

    public <T extends GameObject>
    void updateAndClean(
            List<T> list,
            float delta,
            float screenWidth,
            float screenHeight) {

        Iterator<T> iterator =
                list.iterator();

        while (iterator.hasNext()) {

            T entity =
                    iterator.next();

            entity.update(delta);


            if (entity.isOffScreen(
                    screenWidth,
                    screenHeight)
                    || entity.isDestroyed()) {

                System.out.println(
                        "Removed via Generic Iterator: "
                        + entity.getClass()
                                .getSimpleName()
                );

                iterator.remove();
            }
        }
    }


    @Override
    public void render() {

        float delta =
                Gdx.graphics.getDeltaTime();


        // =================================================
        // QUESTION 3 - SHOOTING
        // =================================================

        if (Gdx.input.isKeyJustPressed(
                Input.Keys.Z)) {

            Bullet bullet =
                    player.shootBullet();

            entities.add(bullet);
        }


        // =================================================
        // QUESTION 2 - UPDATE & CLEAN
        // =================================================

        updateAndClean(
                entities,
                delta,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );


        // =================================================
        // COLLISION
        // =================================================

        for (int i = 0;
             i < entities.size();
             i++) {

            for (int j = i + 1;
                 j < entities.size();
                 j++) {

                GameObject a =
                        entities.get(i);

                GameObject b =
                        entities.get(j);


                if (!a.isDestroyed()
                        && !b.isDestroyed()) {

                    if (a.getCoreHitbox()
                            .overlaps(
                                    b.getCoreHitbox())) {

                        a.onCollision(b);

                        b.onCollision(a);
                    }
                }
            }
        }


        // =================================================
        // RENDER
        // =================================================

        ScreenUtils.clear(
                0.1f,
                0.1f,
                0.15f,
                1f
        );


        shapeRenderer.begin(
                ShapeRenderer.ShapeType.Filled
        );


        for (GameObject entity :
                entities) {

            if (!entity.isDestroyed()) {

                entity.render(
                        shapeRenderer
                );
            }
        }


        shapeRenderer.end();
    }


    @Override
    public void dispose() {

        shapeRenderer.dispose();
    }
}