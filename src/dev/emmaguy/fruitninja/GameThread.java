package dev.emmaguy.fruitninja;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import dev.emmaguy.fruitninja.ui.GameFragment.OnGameOver;

public class GameThread implements Runnable {

    private final Paint scorePaint = new Paint();
    private final SurfaceHolder surfaceHolder;
    private final GameTimer timer = new GameTimer();
    private final OnGameOver gameOverListener;
    private final ProjectileManager projectileManager;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final Paint linePaint = new Paint();
    private final Paint linePaintBlur = new Paint();

    private volatile ScheduledFuture<?> self;

    private int score = 0;
    private int width = 0;
    private boolean isRunning = false;
    private SparseArrayCompat<TimedPath> paths;

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

	this.linePaint.setAntiAlias(true);
	this.linePaint.setColor(Color.YELLOW);
	this.linePaint.setStyle(Paint.Style.STROKE);
	this.linePaint.setStrokeJoin(Paint.Join.ROUND);
	this.linePaint.setStrokeWidth(5.0f);

	this.linePaintBlur.set(this.linePaint);
	this.linePaintBlur.setMaskFilter(new BlurMaskFilter(9.0f, BlurMaskFilter.Blur.NORMAL));
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

		    if (paths != null && paths.size() > 0) {
			List<TimedPath> allPaths = new ArrayList<TimedPath>();
			for (int i = 0; i < paths.size(); i++) {
			    allPaths.add(paths.valueAt(i));
			}
			score += projectileManager.testForCollisions(allPaths);
		    }

		    canvas = surfaceHolder.lockCanvas();
		    if (canvas != null) {
			synchronized (surfaceHolder) {
			    canvas.drawARGB(255, 0, 0, 0);

			    projectileManager.draw(canvas);
			    timer.draw(canvas);
			    canvas.drawText("Score: " + score, width - 160, 50, scorePaint);

			    if (paths != null) {
				for (int i = 0; i < paths.size(); i++) {
				    canvas.drawPath(paths.valueAt(i), linePaintBlur);
				    canvas.drawPath(paths.valueAt(i), linePaint);

				    if (paths.valueAt(i).getTimeDrawn() + 500 < System.currentTimeMillis()) {
					paths.removeAt(i);
				    }
				}
			    }
			}
		    }
		}
	    } catch(Exception e){
		Log.e("FruitNinja", e.getMessage());
	    } finally {
		if (canvas != null) {
		    surfaceHolder.unlockCanvasAndPost(canvas);
		}
	    }
	}

    }

    public void updateDrawnPath(SparseArrayCompat<TimedPath> paths) {
	this.paths = paths;
    }
}