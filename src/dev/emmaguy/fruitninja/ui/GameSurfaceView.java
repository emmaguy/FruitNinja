package dev.emmaguy.fruitninja.ui;

import dev.emmaguy.fruitninja.FruitProjectileManager;
import dev.emmaguy.fruitninja.GameThread;
import dev.emmaguy.fruitninja.Projectile;
import dev.emmaguy.fruitninja.ProjectileManager;
import dev.emmaguy.fruitninja.ui.GameFragment.OnGameOver;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameSurfaceView extends SurfaceView implements OnTouchListener, SurfaceHolder.Callback {

    private GameThread gameThread;
    private ProjectileManager projectileManager;
    private OnGameOver gameOverListener;
    private boolean isGameInitialised = false;
    
    public GameSurfaceView(Context context) {
	super(context);

	initialise();
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
	super(context, attrs);

	initialise();
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);

	initialise();
    }

    private void initialise() {
	this.setOnTouchListener(this);
	this.setFocusable(true);
	this.getHolder().addCallback(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

	float x = event.getX();
	float y = event.getY();

	switch (event.getAction()) {
	case MotionEvent.ACTION_UP:
	    break;
	case MotionEvent.ACTION_MOVE:
	    break;
	case MotionEvent.ACTION_DOWN:

	    for (Projectile p : projectileManager.getProjectiles()) {

		if (p.isAlive() && p.getLocation().contains(x, y)) {
		    p.kill();
		    gameThread.incrementScore();
		    break;
		}
	    }

	    return true;
	}

	return false;
    }
    

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	if (isGameInitialised) {
	    gameThread.resumeGame(width, height);
	} else {
	    isGameInitialised = true;
	    projectileManager = new FruitProjectileManager(getResources());
	    gameThread = new GameThread(getHolder(), projectileManager, gameOverListener);
	    gameThread.startGame(width, height);
	}
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
	gameThread.pauseGame();
    }

    public void setGameOverListener(OnGameOver gameOverListener) {
	this.gameOverListener = gameOverListener;
    }
}
