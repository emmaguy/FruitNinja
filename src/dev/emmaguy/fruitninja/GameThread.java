package dev.emmaguy.fruitninja;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import dev.emmaguy.fruitninja.ui.GameFragment.OnGameOver;

public class GameThread implements Runnable {

    private static final float STROKE_WIDTH = 5f;

    private final Paint scorePaint = new Paint();
    private final SurfaceHolder surfaceHolder;
    private final GameTimer timer = new GameTimer();
    private final OnGameOver gameOverListener;
    private final ProjectileManager projectileManager;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private volatile ScheduledFuture<?> self;

    private boolean isRunning = false;
    private int score = 0;
    private int width = 0;
    private Paint paint = new Paint();
    private SparseArray<Path> paths;

    public GameThread(SurfaceHolder surfaceHolder, ProjectileManager projectileManager, OnGameOver gameOverListener) {
	this.surfaceHolder = surfaceHolder;
	this.projectileManager = projectileManager;
	this.gameOverListener = gameOverListener;
    }

    public void pauseGame() {
	isRunning = false;
	timer.pauseGame();
    }

    public void resumeGame(int width, int height) {
	this.width = width;
	isRunning = true;
	timer.resumeGame();
	projectileManager.setWidthAndHeight(width, height);
    }

    public void startGame(int width, int height) {
	this.width = width;
	this.isRunning = true;
	this.projectileManager.setWidthAndHeight(width, height);
	this.timer.startGame();
	this.self = executor.scheduleAtFixedRate(this, 0, 10, TimeUnit.MILLISECONDS);

	this.scorePaint.setColor(Color.MAGENTA);
	this.scorePaint.setAntiAlias(true);
	this.scorePaint.setTextSize(38.0f);

	this.paint.setAntiAlias(true);
	this.paint.setColor(Color.YELLOW);
	this.paint.setStyle(Paint.Style.STROKE);
	this.paint.setStrokeJoin(Paint.Join.ROUND);
	this.paint.setStrokeWidth(STROKE_WIDTH);
    }

    @Override
    public void run() {
	Canvas canvas = null;
	if (isRunning) {
	    try {

		if (timer.isGameFinished()) {
		    isRunning = false;
		    gameOverListener.onGameOver(score);
		    self.cancel(true);
		} else {

		    projectileManager.update();

		    canvas = surfaceHolder.lockCanvas();
		    if (canvas != null) {
			synchronized (surfaceHolder) {
			    canvas.drawARGB(255, 0, 0, 0);

			    projectileManager.draw(canvas);
			    timer.draw(canvas);
			    canvas.drawText("Score: " + score, width - 160, 50, scorePaint);

			    if (paths != null) {
				for (int i = 0; i < paths.size(); i++){
				    canvas.drawPath(paths.valueAt(i), paint);
				}
			    }
			}
		    }
		}
	    } finally {
		if (canvas != null) {
		    surfaceHolder.unlockCanvasAndPost(canvas);
		}
	    }
	}

    }

    public void incrementScore() {
	this.score++;
    }

    public void updateDrawnPath(SparseArray<Path> paths) {
	this.paths = paths;
    }
}