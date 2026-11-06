
package com.clyo.frontend.observers;

public interface GameObserver {

    void onScoreChanged(long newScore);

    void onHpChanged(int newHp);

    void onSpellCardsChanged(int newSpellCards);

    void onPowerChanged(int newPower);

    void onGrazeChanged(int newGrazeCount);

    default void onLivesChanged(int newLives) {
    }
}
