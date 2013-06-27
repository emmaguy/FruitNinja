package dev.emmaguy.fruitninja;

import android.graphics.Path;

public class TimedPath extends Path {

    private long timeDrawn;
    
    public long getTimeDrawn() {
	return timeDrawn;
    }   
    
    public void updateTimeDrawn(long timeDrawn) {
	this.timeDrawn = timeDrawn;
    }
}
