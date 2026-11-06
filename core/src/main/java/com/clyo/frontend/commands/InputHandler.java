package com.clyo.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.clyo.frontend.GameObject;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {

    // ==========================================
    // 1. KEY CONFIGURATION
    // ==========================================

    private int keyShoot = Input.Keys.Z;
    private int keyBomb = Input.Keys.X;
    private int keyFocus = Input.Keys.SHIFT_LEFT;

    // ==========================================
    // 2. PRE-ALLOCATED COMMANDS
    // ==========================================

    private final ShootCommand shootCommand =
            new ShootCommand();

    private final BombCommand bombCommand =
            new BombCommand();

    private final FocusCommand focusOn =
            new FocusCommand(true);

    private final FocusCommand focusOff =
            new FocusCommand(false);

    private final MoveCommand moveCommand =
            new MoveCommand(0, 0);

    // ==========================================
    // 3. ACTIVE COMMANDS
    // ==========================================

    private final List<Command> activeCommands =
            new ArrayList<>();

    public InputHandler() {
    }

    public InputHandler(List<GameObject> entities) {
        bombCommand.setEntities(entities);
    }

    public void setEntities(List<GameObject> entities) {
        bombCommand.setEntities(entities);
    }

    // ==========================================
    // 4. KEY GETTERS AND SETTERS
    // ==========================================

    public int getKeyShoot() {
        return keyShoot;
    }

    public void setKeyShoot(int keyShoot) {
        this.keyShoot = keyShoot;
    }

    public int getKeyBomb() {
        return keyBomb;
    }

    public void setKeyBomb(int keyBomb) {
        this.keyBomb = keyBomb;
    }

    public int getKeyFocus() {
        return keyFocus;
    }

    public void setKeyFocus(int keyFocus) {
        this.keyFocus = keyFocus;
    }

    // ==========================================
    // PROBLEM 1 & 2: HANDLE INPUT
    // ==========================================

    public List<Command> handleInput(float delta) {

        // Clear previous commands
        activeCommands.clear();

        // Safety check
        if (Gdx.input == null) {
            return activeCommands;
        }

        // --------------------------------------
        // 1. FOCUS MODE
        // --------------------------------------

        boolean isFocusHeld =
                Gdx.input.isKeyPressed(keyFocus)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

        activeCommands.add(
                isFocusHeld ? focusOn : focusOff
        );

        // --------------------------------------
        // 2. 8-DIRECTIONAL MOVEMENT
        // --------------------------------------

        float dx = 0f;
        float dy = 0f;

        // UP
        if (Gdx.input.isKeyPressed(Input.Keys.W)
                || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dy += 1f;
        }

        // DOWN
        if (Gdx.input.isKeyPressed(Input.Keys.S)
                || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dy -= 1f;
        }

        // LEFT
        if (Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dx -= 1f;
        }

        // RIGHT
        if (Gdx.input.isKeyPressed(Input.Keys.D)
                || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dx += 1f;
        }

        // Focus movement speed multiplier
        float speedMult = isFocusHeld ? 0.45f : 1.0f;

        moveCommand.set(
                dx * delta * speedMult,
                dy * delta * speedMult
        );

        activeCommands.add(moveCommand);

        // --------------------------------------
        // 3. CONTINUOUS SHOOTING (Z)
        // --------------------------------------

        if (Gdx.input.isKeyPressed(keyShoot)) {
            activeCommands.add(shootCommand);
        }

        // --------------------------------------
        // 4. BOMB / SPELL CARD (X)
        // --------------------------------------

        if (Gdx.input.isKeyJustPressed(keyBomb)) {
            activeCommands.add(bombCommand);
        }

        return activeCommands;
    }
}