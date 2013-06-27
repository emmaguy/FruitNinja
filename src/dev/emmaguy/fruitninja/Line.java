package dev.emmaguy.fruitninja;

public class Line {

    private float startX;
    private float startY;
    private float x;
    private float y;
    private int ticksRemaining = 10;

    public Line(float startX, float startY, float x, float y) {
	this.startX = startX;
	this.startY = startY;
	this.x = x;
	this.y = y;
    }
    
    public int decrementTicksRemaining() {
	return ticksRemaining--;
    }

    public float getStartX() {
	return startX;
    }

    public float getStartY() {
	return startY;
    }

    public float getStopX() {
	return x;
    }    
    
    public float getStopY() {
	return y;
    }    
}
