package dev.emmaguy.fruitninja;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class FruitProjectile implements Projectile {

    private final Bitmap[] splitFruit = new Bitmap[2];
    private final Paint paint = new Paint();
    private final Matrix m = new Matrix();

    private final Bitmap b;
    private final float rotationIncrement;

    private float gravity;
    private final int maxWidth;
    private final int maxHeight;
    private final int angle;
    private final int initialSpeed;

    private int xLocation;
    private int yLocation;
    private int absYLocation;
    private float rotationAngle;
    private float time = 0.0f;
    private float fallingVelocity = 1.0f;
    private boolean rightToLeft;
    private boolean isAlive = true;

    public FruitProjectile(Bitmap b, int maxWidth, int maxHeight, int angle, int initialSpeed, float gravity,
	    boolean rightToLeft, float rotationIncrement, float rotationStartingAngle) {
	this.b = b;
	this.maxHeight = maxHeight;
	this.angle = angle;
	this.initialSpeed = initialSpeed;
	this.gravity = gravity;
	this.maxWidth = maxWidth;
	this.rightToLeft = rightToLeft;
	this.rotationIncrement = rotationIncrement;
	this.rotationAngle = rotationStartingAngle;

	paint.setAntiAlias(true);
    }

    @Override
    public boolean hasMovedOffScreen() {
	return yLocation < 0 || xLocation + b.getWidth() < 0 || xLocation > maxWidth;
    }

    @Override
    public void move() {

	if (isAlive) {
	    xLocation = (int) (initialSpeed * Math.cos(angle * Math.PI / 180) * time);
	    yLocation = (int) (initialSpeed * Math.sin(angle * Math.PI / 180) * time - 0.5 * gravity * time * time);

	    if (rightToLeft) {
		xLocation = maxWidth - b.getWidth() - xLocation;
	    }
	} else {
	    yLocation -= time * (fallingVelocity + time * gravity / 2);
	    fallingVelocity += time * gravity;
	}

	// 0,0 is top left, we want the parabola to go the other way up
	absYLocation = (yLocation * -1) + maxHeight;
	
	time += 0.1f;
    }

    @Override
    public void draw(Canvas canvas) {

	if (isAlive) {
	    m.reset();
	    m.postTranslate(-b.getWidth() / 2, -b.getHeight() / 2);
	    m.postRotate(rotationAngle);
	    m.postTranslate(xLocation + (b.getWidth() / 2), absYLocation + (b.getHeight() / 2));
	    rotationAngle += rotationIncrement;

	    canvas.drawBitmap(b, m, paint);
	} else {
	    canvas.drawBitmap(splitFruit[0], xLocation, absYLocation, paint);
	    canvas.drawBitmap(splitFruit[1], xLocation + b.getWidth() / 2 + 5, absYLocation, paint);
	}
    }

    @Override
    public Rect getLocation() {
	return new Rect(xLocation, absYLocation, xLocation + b.getWidth(), absYLocation + b.getHeight());
    }

    @Override
    public void kill() {
	this.gravity /= 12.0f;
	this.time = 0.0f;
	this.isAlive = false;
	
	splitFruit[0] = Bitmap.createBitmap(b, 0, 0, b.getWidth() / 2, b.getHeight(), m, true);
	splitFruit[1] = Bitmap.createBitmap(b, b.getWidth() / 2, 0, b.getWidth() / 2, b.getHeight(), m, true);
    }

    @Override
    public boolean isAlive() {
	return isAlive;
    }
}