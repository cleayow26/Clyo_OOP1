package com.clyo.frontend.objects.enemies;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Fairy extends Enemy {

    public Fairy(
            String name,
            int hp) {

        super(
                name,
                hp
        );

        setScoreValue(100);
    }

    public Fairy(
            float x,
            float y) {

        super(
                x,
                y,
                50,
                50,
                "Stage 1 Fairy",
                20
        );

        setScoreValue(100);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {

        shapeRenderer.setColor(1, 0, 0, 1);

        shapeRenderer.rect(
                x,
                y,
                width,
                height
        );
    }
}