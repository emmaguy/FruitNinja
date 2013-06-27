package dev.emmaguy.fruitninja;

import java.util.List;

import android.graphics.Canvas;

public interface ProjectileManager {
    void draw(Canvas c);
    void update();
    void setWidthAndHeight(int width, int height);
    List<Projectile> getProjectiles();
    int testForCollisions(List<TimedPath> allPaths);
}
