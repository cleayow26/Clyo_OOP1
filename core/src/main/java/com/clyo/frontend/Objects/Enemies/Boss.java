
package com.clyo.frontend.objects.enemies;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.clyo.frontend.systems.AssetManager;

public class Boss extends Enemy {

    public Boss(String name, int hp) {
        super(name, hp);

        width = 80;
        height = 80;

        setScoreValue(500);
        setAnimation(
                AssetManager.getInstance().getAnimation("boss_idle")
        );
    }

    public Boss(float x, float y) {
        super(
                x,
                y,
                80,
                80,
                "Stage 1 Boss",
                100
        );

        setScoreValue(500);
        setAnimation(
                AssetManager.getInstance().getAnimation("boss_idle")
        );
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (getSprite() == null && getAnimation() == null) {
            shapeRenderer.setColor(0.7f, 0, 0.7f, 1);
            shapeRenderer.rect(x, y, width, height);
        }
    }
}
