
package com.clyo.frontend.objects.enemies;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.clyo.frontend.objects.patterns.ShootingPattern;
import com.clyo.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.clyo.frontend.systems.BulletManager;
import com.clyo.frontend.systems.AssetManager;

public class Boss extends Enemy {

    private ShootingPattern shootingPattern =
            new SpreadShot(200f, 3, 30f);

    public Boss(String name, int hp) {
        super(name, hp);

        width = 80;
        height = 80;

        setScoreValue(500);
        setAnimation(
                AssetManager.getInstance().getAnimation("boss_idle")
        );
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 80, 80, name, hp);

        setScoreValue(500);
        setAnimation(
                AssetManager.getInstance().getAnimation("boss_idle")
        );
    }

    public Boss(float x, float y) {
        this(x, y, "Rumia", 100);
    }

    public void shootBullet(BulletManager bulletManager) {
        if (shootingPattern == null
                || bulletManager == null
                || isDestroyed()) {
            return;
        }

        shootingPattern.execute(
                x + width / 2f - 8f,
                y,
                bulletManager,
                false
        );
    }

    public ShootingPattern getShootingPattern() {
        return shootingPattern;
    }

    public void setShootingPattern(ShootingPattern shootingPattern) {
        this.shootingPattern = shootingPattern;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (getSprite() == null && getAnimation() == null) {
            shapeRenderer.setColor(0.7f, 0, 0.7f, 1);
            shapeRenderer.rect(x, y, width, height);
        }
    }
}
