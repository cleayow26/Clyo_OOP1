package com.clyo.frontend;

public class Enemy {
    private String name;
    private int hp;
    private int maxHp;
    private long scoreValue;

    public Enemy(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public int getMaxHp() {
        return maxHp;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(long scoreValue) {
        this.scoreValue = scoreValue;
    }

    public boolean takeDamage(int damage) {
        setHp(getHp() - damage);

        System.out.println(getName() + " took " + damage
                + " damage! HP: " + getHp() + "/" + getMaxHp());

        if (getHp() == 0) {
            System.out.println(getName() + " was defeated!");
            return true;
        }

        return false;
    }

    public void attack(Player player, int damage) {
        System.out.println(getName()
                + " unleashes bullet barrage on "
                + player.getName() + "!");
        player.takeDamage(damage);
    }

    public boolean isAlive() {
        return getHp() > 0;
    }
}