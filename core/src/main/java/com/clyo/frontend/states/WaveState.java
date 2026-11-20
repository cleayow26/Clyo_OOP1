package com.clyo.frontend.states;

public interface WaveState {

    void onEnter(LevelWaveManager manager);

    void update(float delta, LevelWaveManager manager);
}