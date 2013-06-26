package dev.emmaguy.fruitninja;

import android.graphics.Canvas;
import android.graphics.RectF;

public interface Projectile {

    boolean hasMovedOffScreen();

    void kill();
    void move();
    void draw(Canvas canvas);

    RectF getLocation();

    boolean isAlive();
}