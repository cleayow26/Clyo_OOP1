
package com.clyo.frontend.objects.patterns.bulletStrategy;

import com.clyo.frontend.objects.bullets.Bullet;

public interface BulletMovementPattern {

    void move(Bullet bullet, float delta);

}
