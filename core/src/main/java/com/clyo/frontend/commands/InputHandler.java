
package com.clyo.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.clyo.frontend.GameObject;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {

    // KEY CONFIGURATION
    private int keyShoot = Input.Keys.Z;
    private int keyBomb = Input.Keys.X;
    private int keyFocus = Input.Keys.SHIFT_LEFT;

    // PRE-ALLOCATED COMMANDS
    private final ShootCommand shootCommand = new ShootCommand();
    private final BombCommand bombCommand = new BombCommand();

    private final FocusCommand focusOn = new FocusCommand(true);
    private final FocusCommand focusOff = new FocusCommand(false);

    private final MoveCommand moveCommand = new MoveCommand(0, 0);

    // ACTIVE COMMANDS
    private final List<Command> activeCommands = new ArrayList<>();

    public InputHandler() {
    }

    public InputHandler(List<GameObject> entities) {
        bombCommand.setEntities(entities);
    }

    public void setEntities(List<GameObject> entities) {
        bombCommand.setEntities(entities);
    }

    // KEY GETTERS AND SETTERS
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

    // HANDLE INPUT
    public List<Command> handleInput(float delta) {

        activeCommands.clear();

        if (Gdx.input == null) {
            return activeCommands;
        }

        // FOCUS MODE
        boolean isFocusHeld =
                Gdx.input.isKeyPressed(keyFocus)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

        activeCommands.add(isFocusHeld ? focusOn : focusOff);

        // 8-DIRECTIONAL MOVEMENT
        float dx = 0f;
        float dy = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.W)
                || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dy += 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)
                || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dy -= 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dx -= 1f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)
                || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dx += 1f;
        }

        // Normalize diagonal movement
        if (dx != 0f && dy != 0f) {
            float diagonalFactor = 0.70710678f;
            dx *= diagonalFactor;
            dy *= diagonalFactor;
        }

        // Focus movement speed multiplier
        float speedMult = isFocusHeld ? 0.45f : 1.0f;

        moveCommand.set(
                dx * delta * speedMult,
                dy * delta * speedMult
        );

        activeCommands.add(moveCommand);

        // CONTINUOUS SHOOTING (Z)
        if (Gdx.input.isKeyPressed(keyShoot)) {
            activeCommands.add(shootCommand);
        }

        // BOMB / SPELL CARD (X)
        if (Gdx.input.isKeyJustPressed(keyBomb)) {
            activeCommands.add(bombCommand);
        }

        return activeCommands;
    }
}
