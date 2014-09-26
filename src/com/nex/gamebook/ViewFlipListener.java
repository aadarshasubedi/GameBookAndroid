package com.nex.gamebook;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public abstract class ViewFlipListener implements OnClickListener,
		OnGestureListener {

	private ImageView leftButton;
	private ImageView rightButton;
	private ViewFlipper flipper;
	private GestureDetector gesturedetector = null;
	private TextView title;
	public ViewFlipListener(ImageView leftButton, ImageView rightButton,
			ViewFlipper flipper, TextView title) {
		super();
		this.leftButton = leftButton;
		this.rightButton = rightButton;
		this.leftButton.setOnClickListener(this);
		this.rightButton.setOnClickListener(this);
		this.flipper = flipper;
		gesturedetector = new GestureDetector(getContext(), this);
		this.title = title;
	}

	@Override
	public void onClick(View v) {
		int leftId = leftButton.getId();
		int rightId = rightButton.getId();
		if (v.getId() == leftId) {
			
			previousView();
		} else if (v.getId() == rightId) {
			
			nextView();
		}
	}

	// Next, Previous Views
	private void previousView() {
		// Button Next Style
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.btn_style_next);
		leftButton.startAnimation(animation);
		animation = AnimationUtils.loadAnimation(getContext(), R.anim.btn_style_previous);
//		title.startAnimation(animation);
		// Previous View
		flipper.setInAnimation(getContext(), R.anim.in_animation);
		flipper.setOutAnimation(getContext(), R.anim.out_animation);
		flipper.showPrevious();
		viewChanged(flipper.getCurrentView());
	}

	private void nextView() {
		// Button Previous Style
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.btn_style_previous);
		rightButton.startAnimation(animation);
		animation = AnimationUtils.loadAnimation(getContext(), R.anim.btn_style_next);
//		title.startAnimation(animation);
		// Next View
		flipper.setInAnimation(getContext(), R.anim.in_animation1);
		flipper.setOutAnimation(getContext(), R.anim.out_animation1);
		flipper.showNext();
		viewChanged(flipper.getCurrentView());

	}
	public boolean onTouchEvent(MotionEvent event) {
        return gesturedetector.onTouchEvent(event);
	}
	public void select(int position) {
		flipper.setDisplayedChild(position);
	}

	public abstract void viewChanged(View currentView);

	public abstract Context getContext();

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	int SWIPE_MIN_VELOCITY = 100;
	int SWIPE_MIN_DISTANCE = 100;

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(e1==null || e2 == null) return true;
		float ev1X = e1.getX();
		float ev2X = e2.getX();
		final float xdistance = Math.abs(ev1X - ev2X);
		final float xvelocity = Math.abs(velocityX);
		if ((xvelocity > SWIPE_MIN_VELOCITY)
				&& (xdistance > SWIPE_MIN_DISTANCE)) {
			if (ev1X > ev2X) {
				nextView();
			} else {
				previousView();
			}
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}	
}