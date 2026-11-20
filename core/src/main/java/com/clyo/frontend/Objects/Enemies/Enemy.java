
package com.clyo.frontend.objects.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.clyo.frontend.Collidable;
import com.clyo.frontend.objects.EntityShooter;
import com.clyo.frontend.objects.Player;
import com.clyo.frontend.objects.patterns.ShootingPattern;
import com.clyo.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
import com.clyo.frontend.systems.AudioSystem;

public class Enemy extends EntityShooter {

    protected String name;
    protected int hp;
    protected int maxHp;
    protected long scoreValue;

    protected Color color = Color.RED;

    // State Pattern: Boss phase controls
    protected boolean invulnerable = false;
    protected boolean surviveAtZero = false;

    // ==========================================
    // CONSTRUCTOR 1
    // ==========================================

    public Enemy(String name, int hp) {
        super(450, 400, 50, 50, 0);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 100;
    }

    // ==========================================
    // CONSTRUCTOR 2
    // ==========================================

    public Enemy(
            float x,
            float y,
            float width,
            float height,
            String name,
            int hp) {

        super(x, y, width, height, 0);

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 100;
    }

    // ==========================================
    // CONSTRUCTOR 3: COLOR AND SCORE
    // ==========================================

    public Enemy(
            float x,
            float y,
            float width,
            float height,
            Color color,
            String name,
            int hp,
            long scoreValue) {

        super(x, y, width, height, 0);

        this.color = color != null ? color : Color.RED;
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    // ==========================================
    // CONSTRUCTOR 4: DEPENDENCY INJECTION
    // ==========================================

    public Enemy(
            float x,
            float y,
            float width,
            float height,
            String name,
            int hp,
            long scoreValue,
            ShootingPattern shootingPattern,
            EntityMovementPattern movementPattern) {

        super(
                x, y, width, height, 0,
                shootingPattern, movementPattern
        );

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    // ==========================================
    // GETTERS AND SETTERS
    // ==========================================

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = Math.max(1, maxHp);
        this.hp = Math.min(this.hp, this.maxHp);
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setSurviveAtZero(boolean surviveAtZero) {
        this.surviveAtZero = surviveAtZero;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(long scoreValue) {
        this.scoreValue = scoreValue;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (color != null) {
            this.color = color;
        }
    }

    // ==========================================
    // DAMAGE
    // ==========================================

    public void takeDamage(int damage) {

        if (isDestroyed() || damage <= 0 || invulnerable) {
            return;
        }

        hp = Math.max(0, hp - damage);

        System.out.println(
                name + " took " + damage
                        + " damage! HP: " + hp + "/" + maxHp
        );

        if (hp == 0) {

            if (surviveAtZero) {
                System.out.println(
                        name + " reached phase threshold!"
                );
                return;
            }

            System.out.println(name + " was defeated!");

            AudioSystem.getInstance().playEnemyDeath();

            destroy();
        }
    }

    // ==========================================
    // ATTACK
    // ==========================================

    public void attack(Player player, int damage) {

        if (player == null || player.isDestroyed()) {
            return;
        }

        player.setHp(player.getHp() - damage);

        System.out.println(
                name + " attacked " + player.getName()
                        + " for " + damage + " damage!"
        );
    }

    // ==========================================
    // UPDATE
    // ==========================================

    @Override
    public void update(float delta) {
        if (isDestroyed()) {
            return;
        }

        super.update(delta);
    }

    // ==========================================
    // RENDER
    // ==========================================

    @Override
    public void render(ShapeRenderer shapeRenderer) {

        if (isDestroyed() || shapeRenderer == null) {
            return;
        }

        if (getSprite() == null && getAnimation() == null) {
            shapeRenderer.setColor(color);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    // ==========================================
    // COLLISION
    // ==========================================

    @Override
    public void onCollision(Collidable other) {
        // Enemy collision behavior.
    }
}
