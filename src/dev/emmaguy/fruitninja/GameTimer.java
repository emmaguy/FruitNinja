package dev.emmaguy.fruitninja;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameTimer {

    private final Paint timeRemainingPaint = new Paint();
    private final DateFormat formatter = new SimpleDateFormat("ss:SSS", Locale.UK);
    
    private static final int GAME_ROUND_TIME_MILLISECONDS = 60 * 1000;

    private long startTime = 0;
    private long elapsedTime = 0;
    
    public GameTimer(){

	timeRemainingPaint.setAntiAlias(true);
	timeRemainingPaint.setColor(Color.GREEN);
	timeRemainingPaint.setTextSize(38.0f);
    }
    
    private long getTime() {
	return System.currentTimeMillis();
    }
    
    public void startGame() {
	startTime = getTime();
	elapsedTime = 0;
    }
    
    public void pauseGame() {
	elapsedTime = getTime() - startTime;
    }

    public void resumeGame() {
	startTime = getTime() - elapsedTime;
    }

    public boolean isGameFinished() {
	return getMillisecondsRemaining() <= 0;
    }

    private long getMillisecondsRemaining() {
	return GAME_ROUND_TIME_MILLISECONDS - (getTime() - startTime);
    }

    public void draw(Canvas canvas) {
	canvas.drawText(formatter.format(new Date(getMillisecondsRemaining())), 10, 50, timeRemainingPaint);	
    }
}
