package dev.emmaguy.fruitninja.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import dev.emmaguy.fruitninja.FruitProjectileManager;
import dev.emmaguy.fruitninja.GameThread;
import dev.emmaguy.fruitninja.Projectile;
import dev.emmaguy.fruitninja.ProjectileManager;
import dev.emmaguy.fruitninja.R;

public class GameFragment extends Fragment implements SurfaceHolder.Callback, OnTouchListener {

    private OnGameOver gameOverListener;

    private GameThread gameThread;
    private ProjectileManager projectileManager;
    private boolean isGameInitialised = false;

    public interface OnGameOver {
	public void onGameOver(int score);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View v = inflater.inflate(R.layout.fragment_game, null);

	SurfaceView gameView = (SurfaceView) v.findViewById(R.id.gameview);
	gameView.getHolder().addCallback(this);
	gameView.setOnTouchListener(this);
	gameView.setFocusable(true);

	projectileManager = new FruitProjectileManager(getResources());
	gameThread = new GameThread(gameView.getHolder(), projectileManager, gameOverListener);

	return v;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

	float x = event.getX();
	float y = event.getY();

	switch (event.getAction()) {
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
    public void onAttach(Activity activity) {
	super.onAttach(activity);

	try {
	    gameOverListener = (OnGameOver) activity;
	} catch (ClassCastException e) {
	    throw new ClassCastException(activity.toString() + " must implement OnGameOver");
	}
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	if (isGameInitialised) {
	    gameThread.resumeGame(width, height);
	} else {
	    isGameInitialised = true;
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
}