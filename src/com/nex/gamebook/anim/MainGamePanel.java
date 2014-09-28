/**
 * 
 */
package com.nex.gamebook.anim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.nex.gamebook.R;
import com.nex.gamebook.anim.model.ElaineAnimated;

/**
 * @author impaler This is the main surface that handles the ontouch events and
 *         draws the image to the screen.
 */
@SuppressLint("NewApi")
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();

	private MainThread thread;
	private ElaineAnimated elaine;

	public MainGamePanel(Context context) {
		super(context);
		init();
	}

	public MainGamePanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MainGamePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		setZOrderOnTop(true); // necessary
		SurfaceHolder sfhTrackHolder = getHolder();
		sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop

		// create Elaine and load bitmap
		Bitmap bt = BitmapFactory.decodeResource(getResources(), R.drawable.open_book_anim);
		int loops = 9;
		int wImg = bt.getWidth();
		int hImg = bt.getHeight();
		int marginTop = -20;
//		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//		Display display = getDisplay();
//		Point size = new Point();
//		display.getSize(size);
		int width = getWidth();
		int height = getHeight();

		int wPart = bt.getWidth() / loops;
		Log.i(TAG, "screenH:" + height + "screenW:" + width + " imageW:" + wPart + " xPosition:" + (width - wPart) / 2);
		elaine = new ElaineAnimated(bt, (width - wPart) / 2, height / 2 + marginTop, wImg, hImg, 4, loops);

		thread = new MainThread(holder, this);
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.interrupt();
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				Log.e(TAG, "", e);
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// handle touch
		}
		return true;
	}

	public void render(Canvas canvas) {
		// canvas.drawColor(Color.BLACK);
		canvas.drawColor(android.R.color.transparent);
		elaine.draw(canvas);
		// display fps
		// displayFps(canvas, avgFps);
	}

	/**
	 * This is the game update method. It iterates through all the objects and
	 * calls their update method if they have one or calls specific engine's
	 * update method.
	 */
	public void update() {
		elaine.update(System.currentTimeMillis());
	}
}
