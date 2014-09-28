package com.nex.gamebook;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public abstract class ViewFlipListener implements OnClickListener {

	private ImageView leftButton;
	private ImageView rightButton;
	private ViewFlipper flipper;
	private TextView title;

	public ViewFlipListener(ImageView leftButton, ImageView rightButton, ViewFlipper flipper, TextView title) {
		super();
		this.leftButton = leftButton;
		this.rightButton = rightButton;
		this.leftButton.setOnClickListener(this);
		this.rightButton.setOnClickListener(this);
		this.flipper = flipper;
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
		if(flipper.getChildCount()==1) return;
		// Button Next Style
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.btn_style_next);
		leftButton.startAnimation(animation);
		animation = AnimationUtils.loadAnimation(getContext(), R.anim.btn_style_previous);
		// title.startAnimation(animation);
		// Previous View
		flipper.setInAnimation(getContext(), R.anim.in_animation);
		flipper.setOutAnimation(getContext(), R.anim.out_animation);
		flipper.showPrevious();
		viewChanged(flipper.getCurrentView());
	}

	private void nextView() {
		if(flipper.getChildCount()==1) return;
		// Button Previous Style
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.btn_style_previous);
		rightButton.startAnimation(animation);
		animation = AnimationUtils.loadAnimation(getContext(), R.anim.btn_style_next);
		// title.startAnimation(animation);
		// Next View
		flipper.setInAnimation(getContext(), R.anim.in_animation1);
		flipper.setOutAnimation(getContext(), R.anim.out_animation1);
		flipper.showNext();
		viewChanged(flipper.getCurrentView());

	}

	float downXValue;

	public boolean onTouchEvent(MotionEvent arg1) {

		// Get the action that was done on this touch event
		switch (arg1.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// store the X value when the user's finger was pressed down
			downXValue = arg1.getX();
			break;
		}

		case MotionEvent.ACTION_UP: {
			// Get the X value when the user released his/her finger
			float currentX = arg1.getX();

			// going backwards: pushing stuff to the right
			if (downXValue < currentX) {
				// Get a reference to the ViewFlipper
				// Set the animation
//				this.flipper.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.btn_style_next));
				previousView();
			}

			// going forwards: pushing stuff to the left
			if (downXValue > currentX) {
				// Get a reference to the ViewFlipper

				// Set the animation
//				this.flipper.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.btn_style_previous));
				nextView();
			}
			break;
		}
		}
		return true;
		// return gesturedetector.onTouchEvent(event);
	}


	public void select(int position) {
		flipper.setDisplayedChild(position);
	}

	public abstract void viewChanged(View currentView);

	public abstract Context getContext();

}