
package com.clyo.frontend.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.clyo.frontend.Main;

public class Lwjgl3Launcher {

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) {
            return;
        }

        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(
                new Main(),
                getDefaultConfiguration()
        );
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {

        Lwjgl3ApplicationConfiguration configuration =
                new Lwjgl3ApplicationConfiguration();

        configuration.setTitle("Clyo OOP");

        configuration.useVsync(true);

        configuration.setForegroundFPS(
                Lwjgl3ApplicationConfiguration
                        .getDisplayMode().refreshRate + 1
        );

        // PROBLEM 1: Change window resolution
        configuration.setWindowedMode(800, 600);

        configuration.setWindowIcon(
                "libgdx128.png",
                "libgdx64.png",
                "libgdx32.png",
                "libgdx16.png"
        );

        return configuration;
    }
}
