
package com.clyo.frontend.objects.items;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.clyo.frontend.Collidable;
import com.clyo.frontend.GameObject;
import com.clyo.frontend.objects.Player;
import com.clyo.frontend.systems.AudioSystem;

public class Item extends GameObject {

    private ItemType type;
    private float speed;
    private String itemType;
    private long scoreValue;
    private boolean collected = false;

    public Item(
            float x,
            float y,
            float width,
            float height,
            float speed,
            String itemType,
            long scoreValue
    ) {
        super(x, y, width, height);

        this.speed = speed;
        this.itemType = itemType;
        this.scoreValue = scoreValue;
        this.type = convertToItemType(itemType);
    }

    public Item(float x, float y, ItemType type) {
        super(x, y, 30, 30);

        this.type = type;
        this.speed = 120f;
        this.itemType = type.name();
        this.scoreValue = type.getScoreValue();
    }

    private ItemType convertToItemType(String itemType) {
        if (itemType == null) {
            return ItemType.POINT;
        }

        String text = itemType.toUpperCase();

        if (text.contains("POWER")) {
            return ItemType.POWER;
        }

        if (text.contains("BOMB")) {
            return ItemType.BOMB;
        }

        if (text.contains("LIFE")) {
            return ItemType.LIFE;
        }

        return ItemType.POINT;
    }

    public ItemType getType() {
        return type;
    }

    public ItemType getItemTypeEnum() {
        return type;
    }

    public String getItemType() {
        return itemType;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public float getSpeed() {
        return speed;
    }

    public void setType(ItemType type) {
        this.type = type;
        this.itemType = type.name();
        this.scoreValue = type.getScoreValue();
    }

    @Override
    public void update(float delta) {
        updateAnimationTime(delta);
        y -= speed * delta;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (getSprite() == null && getAnimation() == null) {
            shapeRenderer.setColor(0, 1, 0, 1);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player
                && !isDestroyed()
                && !collected) {

            collected = true;

            Player player = (Player) other;

            System.out.println("ITEM COLLISION DETECTED!");

            // Collect first, before destroying the item.
            player.collectItem(this);

            // Play pickup sound.
            AudioSystem.getInstance().playItemCollect();

            // Destroy the item after collection.
            destroy();

            System.out.println("ITEM PICKUP FINISHED!");
        }
    }
}
