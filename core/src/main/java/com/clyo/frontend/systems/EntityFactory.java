
package com.clyo.frontend.systems;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.clyo.frontend.objects.Player;
import com.clyo.frontend.objects.BulletType;
import com.clyo.frontend.objects.bullets.Bullet;
import com.clyo.frontend.objects.enemies.Boss;
import com.clyo.frontend.objects.enemies.Fairy;
import com.clyo.frontend.objects.items.Item;
import com.clyo.frontend.objects.items.ItemType;

public class EntityFactory {

    private EntityFactory() {
    }

    public static Player createPlayer(
            float x, float y,
            String name, int hp, int maxHp, int power) {

        Player player = new Player(x, y, name, hp, maxHp, power);

        Animation<TextureRegion> animation =
                AssetManager.getInstance().getAnimation("player_idle");

        if (animation != null) {
            player.setAnimation(animation);
        }

        return player;
    }

    public static Item createItem(float x, float y, ItemType type) {
        Item item = new Item(x, y, type);

        String key;

        switch (type) {
            case POWER:
                key = "item_power";
                break;
            case POINT:
                key = "item_point";
                break;
            case BOMB:
                key = "item_bomb";
                break;
            case LIFE:
                key = "item_life";
                break;
            default:
                return item;
        }

        TextureRegion region =
                AssetManager.getInstance().getTextureRegion(key);

        if (region != null) {
            item.setSprite(region);
        }

        return item;
    }

    public static Bullet createEnemyBullet(
            float x, float y, int damage) {

        Bullet bullet = new Bullet(
                x, y, 400f, BulletType.DANMAKU, damage
        );

        TextureRegion region =
                AssetManager.getInstance()
                        .getTextureRegion("bullet_danmaku");

        if (region != null) {
            bullet.setSprite(region);
        }

        return bullet;
    }

    public static Fairy createFairy(
            float x, float y, String name, int hp) {

        Fairy fairy = new Fairy(name, hp);
        fairy.setX(x);
        fairy.setY(y);

        Animation<TextureRegion> animation =
                AssetManager.getInstance()
                        .getAnimation("fairy_idle_red");

        if (animation != null) {
            fairy.setAnimation(animation);
        }

        return fairy;
    }

    public static Fairy createFairy(
            float x, float y, String name,
            int hp, String keyString) {

        Fairy fairy = new Fairy(name, hp);
        fairy.setX(x);
        fairy.setY(y);

        Animation<TextureRegion> animation =
                AssetManager.getInstance().getAnimation(keyString);

        if (animation != null) {
            fairy.setAnimation(animation);
        }

        return fairy;
    }

    public static Boss createBoss(
            float x, float y, String name, int hp) {

        Boss boss = new Boss(name, hp);
        boss.setX(x);
        boss.setY(y);

        Animation<TextureRegion> animation =
                AssetManager.getInstance().getAnimation("boss_idle");

        if (animation != null) {
            boss.setAnimation(animation);
        }

        return boss;
    }

    public static Bullet createPlayerBullet(
            float x, float y, int damage, String spriteKey) {

        Bullet bullet = new Bullet(
                x, y, BulletType.AMULET, damage
        );

        TextureRegion region =
                AssetManager.getInstance().getTextureRegion(spriteKey);

        if (region != null) {
            bullet.setSprite(region);
        }

        return bullet;
    }

    public static Bullet createPlayerBullet(
            float x, float y, int damage) {

        return createPlayerBullet(
                x, y, damage, "bullet_amulet"
        );
    }
}
