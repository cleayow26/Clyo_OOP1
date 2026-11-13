
package com.clyo.frontend.objects.patterns.entityStrategy;

import com.clyo.frontend.GameObject;

public interface EntityMovementPattern {

    void move(GameObject entity, float delta);

}
