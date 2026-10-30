
package com.clyo.frontend.systems;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Clip;

public class AudioSystem {

    private static final AudioSystem INSTANCE = new AudioSystem();

    private static final float SAMPLE_RATE = 22050f;

    private static final AudioFormat FORMAT =
            new AudioFormat(SAMPLE_RATE, 16, 1, true, false);

    private Clip bgm;
    private Clip shoot;
    private Clip enemyDeath;
    private Clip itemCollect;

    private boolean initialized = false;

    private AudioSystem() {
    }

    public static AudioSystem getInstance() {
        return INSTANCE;
    }

    public synchronized void init() {
        if (initialized) {
            return;
        }

        try {
            // Fast retro arcade melody.
            // This is generated once and looped as a single clip.
            bgm = createMelody(new int[] {
                    659, 784, 988, 784, 659, 784, 1175, 988,
                    587, 698, 880, 698, 587, 698, 1047, 880,
                    523, 659, 784, 988, 784, 659, 523, 659,
                    587, 698, 880, 1047, 880, 698, 587, 523,
                    659, 784, 988, 1175, 988, 784, 659, 784,
                    587, 698, 880, 1047, 1175, 1047, 880, 698
            }, 125, 0.10);

            // Short shooting sound
            shoot = createEffect(
                    new int[] {1250, 950, 650},
                    35,
                    0.18
            );

            // Descending enemy defeat sound
            enemyDeath = createEffect(
                    new int[] {880, 660, 440, 300},
                    75,
                    0.20
            );

            // Ascending item pickup sound
            itemCollect = createEffect(
                    new int[] {660, 880, 1100, 1320},
                    55,
                    0.16
            );

            initialized = true;

            // Start BGM once; do not start it inside render().
            bgm.loop(Clip.LOOP_CONTINUOUSLY);

            System.out.println("Audio system ready!");

        } catch (Exception e) {
            System.err.println(
                    "Could not initialize audio: " + e.getMessage()
            );
            dispose();
        }
    }

    private Clip createMelody(
            int[] notes,
            int noteDurationMs,
            double volume
    ) throws Exception {

        int samplesPerNote =
                (int) (SAMPLE_RATE * noteDurationMs / 1000.0);

        int gapSamples =
                (int) (SAMPLE_RATE * 0.012);

        int totalSamples =
                notes.length * (samplesPerNote + gapSamples);

        byte[] data = new byte[totalSamples * 2];

        int offset = 0;

        for (int frequency : notes) {
            for (int i = 0; i < samplesPerNote; i++) {
                double time = i / SAMPLE_RATE;

                // Square-wave sound for a retro 8-bit character.
                double wave = Math.sin(
                        2.0 * Math.PI * frequency * time
                ) >= 0 ? 1.0 : -1.0;

                int fadeSamples = Math.min(
                        150,
                        samplesPerNote / 4
                );

                double envelope = Math.min(
                        1.0,
                        Math.min(
                                (double) i / fadeSamples,
                                (double) (samplesPerNote - 1 - i)
                                        / fadeSamples
                        )
                );

                short sample = (short) (
                        wave * envelope * volume * 32767
                );

                data[offset++] = (byte) (sample & 0xff);
                data[offset++] = (byte) ((sample >> 8) & 0xff);
            }

            // A tiny silence between notes.
            offset += gapSamples * 2;
        }

        return makeClip(data, offset);
    }

    private Clip createEffect(
            int[] notes,
            int noteDurationMs,
            double volume
    ) throws Exception {

        int samplesPerNote =
                (int) (SAMPLE_RATE * noteDurationMs / 1000.0);

        byte[] data = new byte[
                notes.length * samplesPerNote * 2
        ];

        int offset = 0;

        for (int frequency : notes) {
            for (int i = 0; i < samplesPerNote; i++) {
                double time = i / SAMPLE_RATE;

                double wave = Math.sin(
                        2.0 * Math.PI * frequency * time
                ) >= 0 ? 1.0 : -1.0;

                int fadeSamples = Math.min(
                        100,
                        Math.max(1, samplesPerNote / 4)
                );

                double envelope = Math.min(
                        1.0,
                        Math.min(
                                (double) i / fadeSamples,
                                (double) (samplesPerNote - 1 - i)
                                        / fadeSamples
                        )
                );

                short sample = (short) (
                        wave * envelope * volume * 32767
                );

                data[offset++] = (byte) (sample & 0xff);
                data[offset++] = (byte) ((sample >> 8) & 0xff);
            }
        }

        return makeClip(data, data.length);
    }

    private Clip makeClip(byte[] data, int length)
            throws Exception {

        Clip clip = javax.sound.sampled.AudioSystem.getClip();

        clip.open(FORMAT, data, 0, length);

        return clip;
    }

    private synchronized void playEffect(Clip clip) {
        if (!initialized || clip == null) {
            return;
        }

        // Restart the same effect instead of opening new audio lines.
        if (clip.isRunning()) {
            clip.stop();
        }

        clip.setFramePosition(0);
        clip.start();
    }

    public void playShoot() {
        playEffect(shoot);
    }

    public void playEnemyDeath() {
        playEffect(enemyDeath);
    }

    public void playItemCollect() {
        playEffect(itemCollect);
    }

    public synchronized void dispose() {
        closeClip(bgm);
        closeClip(shoot);
        closeClip(enemyDeath);
        closeClip(itemCollect);

        bgm = null;
        shoot = null;
        enemyDeath = null;
        itemCollect = null;

        initialized = false;
    }

    private void closeClip(Clip clip) {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
