package com.clyo.frontend;

public class Player {
    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;

    public Player(String name, int hp, int power, int spellCards) {
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getPower() {
        return power;
    }

    public int getSpellCards() {
        return spellCards;
    }

    public long getScore() {
        return score;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
    }

    public void takeDamage(int damage) {
        setHp(getHp() - damage);

        if (getHp() > 0) {
            System.out.println(getName() + " took " + damage
                    + " damage! Remaining HP: " + getHp());
        } else {
            System.out.println(getName() + " was defeated!");
        }
    }

    public void shoot(Enemy target) {
        int damage = 10 + getPower();

        System.out.println(getName() + " shoots "
                + target.getName() + " dealing "
                + damage + " DMG!");

        target.takeDamage(damage);
    }

    public boolean isAlive() {
        return getHp() > 0;
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            System.out.println(getName() + " gained "
                    + points + " pts! Total Score: " + this.score);
        }
    }

    public void collectItem(Item item) {
        System.out.println(getName() + " collected "
                + item.getItemType() + "!");

        if (item.getScoreValue() > 0) {
            addScore(item.getScoreValue());
        }
    }
}