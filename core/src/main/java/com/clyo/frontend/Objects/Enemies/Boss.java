package com.clyo.frontend.objects.enemies;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Boss extends Enemy {

    public Boss(
            String name,
            int hp) {

        super(
                name,
                hp
        );

        width = 80;
        height = 80;

        setScoreValue(500);
    }

    public Boss(
            float x,
            float y) {

        super(
                x,
                y,
                80,
                80,
                "Stage 1 Boss",
                100
        );

        setScoreValue(500);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {

        shapeRenderer.setColor(
                0.7f,
                0,
                0.7f,
                1
        );

        shapeRenderer.rect(
                x,
                y,
                width,
                height
        );
    }
}