package dev.emmaguy.fruitninja.ui;

import android.content.Context;
import android.graphics.Path;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import dev.emmaguy.fruitninja.FruitProjectileManager;
import dev.emmaguy.fruitninja.GameThread;
import dev.emmaguy.fruitninja.ProjectileManager;
import dev.emmaguy.fruitninja.ui.GameFragment.OnGameOver;

public class GameSurfaceView extends SurfaceView implements OnTouchListener, SurfaceHolder.Callback {

    private GameThread gameThread;
    private ProjectileManager projectileManager;
    private OnGameOver gameOverListener;
    private boolean isGameInitialised = false;
    private final SparseArray<Path> paths = new SparseArray<Path>();

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
	switch (event.getActionMasked()) {
	case MotionEvent.ACTION_DOWN:
	    createNewPath(event.getX(), event.getY(), event.getPointerId(0));
	    break;
	case MotionEvent.ACTION_POINTER_DOWN:

	    int newPointerIndex = event.getActionIndex();
	    createNewPath(event.getX(newPointerIndex), event.getY(newPointerIndex), event.getPointerId(newPointerIndex));
	    
	    break;
	case MotionEvent.ACTION_MOVE:

	    for (int i = 0; i < paths.size(); i++){
		int pointerIndex = event.findPointerIndex(paths.indexOfKey(i));

		if(pointerIndex >= 0){
		    paths.valueAt(i).lineTo(event.getX(pointerIndex), event.getY(pointerIndex));
		}		
	    }
	    break;
	}
	gameThread.updateDrawnPath(paths);
	return true;
    }

    private void createNewPath(float x, float y, int ptrId) {
	Path path = new Path();
	path.moveTo(x, y);
	paths.append(ptrId, path);
    }

    // @Override
    // public boolean onTouch(View v, MotionEvent event) {
    //
    // int action = MotionEventCompat.getActionMasked(event);
    // int pointerIndex = MotionEventCompat.getActionIndex(event);
    // int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);
    //
    // float eventX = event.getX(pointerIndex);
    // float eventY = event.getY(pointerIndex);
    //
    // Log.e("index", "X: " + eventX + " Y: " + eventY + " Index: " +
    // pointerIndex + " id: " + pointerId + " ptr count: " +
    // event.getPointerCount());
    //
    // switch (action) {
    // case MotionEvent.ACTION_POINTER_DOWN:
    // int newPointerIndex = event.getActionIndex();
    // newPointer = event.getPointerId(newPointerIndex);
    // return true;
    // case MotionEvent.ACTION_DOWN:
    // Path path = new Path();
    // paths.append(pointerId, path);
    // path.moveTo(eventX, eventY);
    // return true;
    //
    // case MotionEvent.ACTION_MOVE:
    // case MotionEvent.ACTION_UP:
    // case MotionEvent.ACTION_POINTER_UP:
    // Path p = paths.get(pointerId);
    // if(p != null)
    // p.lineTo(eventX, eventY);
    //
    // break;
    //
    // default:
    // return false;
    // }
    //
    // gameThread.updateDrawnPath(paths);
    // return true;
    // }

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
