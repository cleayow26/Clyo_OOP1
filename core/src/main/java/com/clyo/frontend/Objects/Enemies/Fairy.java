
package com.clyo.frontend.objects.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.clyo.frontend.systems.AssetManager;

public class Fairy extends Enemy {

    public Fairy(String name, int hp) {
        super(name, hp);
        setScoreValue(100);
        setAnimation(
                AssetManager.getInstance().getAnimation("fairy_idle")
        );
    }

    public Fairy(float x, float y) {
        super(
                x,
                y,
                50,
                50,
                "Stage 1 Fairy",
                20
        );

        setScoreValue(100);
        setAnimation(
                AssetManager.getInstance().getAnimation("fairy_idle")
        );
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (getSprite() == null && getAnimation() == null) {
            shapeRenderer.setColor(1, 0, 0, 1);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}
