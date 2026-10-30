
package com.clyo.frontend.objects.patterns;

import com.clyo.frontend.systems.BulletManager;

public interface ShootingPattern {

    void execute(
            float originX,
            float originY,
            BulletManager bulletManager,
            boolean isPlayer
    );
}
