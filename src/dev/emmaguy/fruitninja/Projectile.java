package dev.emmaguy.fruitninja;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface Projectile {

    boolean hasMovedOffScreen();

    void kill();
    void move();
    void draw(Canvas canvas);

    Rect getLocation();

    boolean isAlive();
}