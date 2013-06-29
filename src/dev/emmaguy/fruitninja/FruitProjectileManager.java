package dev.emmaguy.fruitninja;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Region;
import android.util.SparseArray;

public class FruitProjectileManager implements ProjectileManager {

    private final Random random = new Random();
    private final List<Projectile> fruitProjectiles = new ArrayList<Projectile>();
    private final SparseArray<Bitmap> bitmapCache;
    private Region clip;
    private int maxWidth;
    private int maxHeight;

    public FruitProjectileManager(Resources r) {

	bitmapCache = new SparseArray<Bitmap>(FruitType.values().length);

	for (FruitType t : FruitType.values()) {
	    bitmapCache.put(t.getResourceId(), BitmapFactory.decodeResource(r, t.getResourceId(), new Options()));
	}
    }

    public void draw(Canvas canvas) {
	for (Projectile f : fruitProjectiles) {
	    f.draw(canvas);
	}
    }

    public void update() {

	if (maxWidth < 0 || maxHeight < 0) {
	    return;
	}

	if (random.nextInt(1000) <= 30) {
	    fruitProjectiles.add(createNewFruitProjectile());
	}

	for (Iterator<Projectile> iter = fruitProjectiles.iterator(); iter.hasNext();) {

	    Projectile f = iter.next();
	    f.move();

	    if (f.hasMovedOffScreen()) {
		iter.remove();
	    }
	}
    }

    private FruitProjectile createNewFruitProjectile() {
	int angle = random.nextInt(20) + 70;
	int speed = random.nextInt(30) + 120;
	boolean rightToLeft = random.nextBoolean();

	float gravity = random.nextInt(6) + 14.0f;
	float rotationStartingAngle = random.nextInt(360);
	float rotationIncrement = random.nextInt(100) / 10.0f;

	if (random.nextInt(1) % 2 == 0) {
	    rotationIncrement *= -1;
	}

	return new FruitProjectile(bitmapCache.get(FruitType.randomFruit().getResourceId()), maxWidth, maxHeight,
		angle, speed, gravity, rightToLeft, rotationIncrement, rotationStartingAngle);
    }

    public void setWidthAndHeight(int width, int height) {
	this.maxWidth = width;
	this.maxHeight = height;
	this.clip = new Region(0, 0, width, height);
    }

    @Override
    public int testForCollisions(List<TimedPath> allPaths) {

	int score = 0;
	for (TimedPath p : allPaths) {
	    for (Projectile f : fruitProjectiles) {

		if(!f.isAlive())
		    continue;

		Region projectile = new Region(f.getLocation());
		Region path = new Region();
		path.setPath(p, clip);
		
		if (!projectile.quickReject(path) && projectile.op(path, Region.Op.INTERSECT)) {
		    f.kill();
		    score++;
		}
	    }
	}
	return score;
    }
}
