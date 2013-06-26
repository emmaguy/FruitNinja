package dev.emmaguy.fruitninja;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

public class FruitProjectile implements Projectile {

    private final Paint paint = new Paint();
    private final Matrix m = new Matrix();
    
    private final Bitmap b;
    private final float rotationIncrement;
    
    private final float gravity;
    private final int maxWidth;
    private final int maxHeight;
    private final int angle;
    private final int initialSpeed;
    
    private float rotationAngle;
    private float xLocation;
    private float yLocation;
    private float absYLocation;
    private float time = 0.0f;
    private boolean rightToLeft;

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

	xLocation = (float) (initialSpeed * Math.cos(angle * Math.PI / 180) * time);
	yLocation = (float) (initialSpeed * Math.sin(angle * Math.PI / 180) * time - 0.5 * gravity * time * time);

	if (rightToLeft) {
	    xLocation = maxWidth - b.getWidth() - xLocation;
	}

	// 0,0 is top left, we want the parabola to go the other way up
	absYLocation = (yLocation * -1) + maxHeight;

	time += 0.1f;
    }

    @Override
    public void draw(Canvas canvas) {

	m.reset();
	m.postTranslate(-b.getWidth() / 2, -b.getHeight() / 2);
	m.postRotate(rotationAngle);
	m.postTranslate(xLocation + (b.getWidth() / 2), absYLocation + (b.getHeight() / 2));
	rotationAngle += rotationIncrement;
	
	canvas.drawBitmap(b, m, paint);
    }

    @Override
    public RectF getLocation() {
	return new RectF(xLocation, absYLocation, xLocation + b.getWidth(), absYLocation + b.getHeight());
    }
}