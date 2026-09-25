package com.clyo.frontend;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class GameObject implements Collidable {

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    private boolean destroyed;

    public GameObject(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.destroyed = false;
    }

    public abstract void update(float delta);

    public abstract void render(ShapeRenderer shapeRenderer);

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isOffScreen(float screenWidth, float screenHeight) {
        return x + width < 0 ||
               x > screenWidth ||
               y + height < 0 ||
               y > screenHeight;
    }

    public Hitbox getCoreHitbox() {
        return new Hitbox(x, y, width, height);
    }

    public static class Hitbox {

        private float x;
        private float y;
        private float width;
        private float height;

        public Hitbox(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean overlaps(Hitbox other) {
            return x < other.x + other.width &&
                   x + width > other.x &&
                   y < other.y + other.height &&
                   y + height > other.y;
        }
    }
}