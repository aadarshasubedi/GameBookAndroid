package com.nex.gamebook;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public abstract class ViewFlipListener implements OnClickListener {

	private ImageView leftButton;
	private ImageView rightButton;
	private ViewFlipper flipper;

	public ViewFlipListener(ImageView leftButton, ImageView rightButton,
			ViewFlipper flipper) {
		super();
		this.leftButton = leftButton;
		this.rightButton = rightButton;
		this.leftButton.setOnClickListener(this);
		this.rightButton.setOnClickListener(this);
		this.flipper = flipper;
	}

	@Override
	public void onClick(View v) {
		int leftId = leftButton.getId();
		int rightId = rightButton.getId();
		if (v.getId() == leftId) {
			// Button Next Style
			Animation animationNext = AnimationUtils.loadAnimation(
					getContext(), R.anim.btn_style_next);
			leftButton.startAnimation(animationNext);
			previousView();
			viewChanged(flipper.getCurrentView());
		} else if (v.getId() == rightId) {
			// Button Previous Style
			Animation animationPrevious = AnimationUtils.loadAnimation(
					getContext(), R.anim.btn_style_previous);
			rightButton.startAnimation(animationPrevious);
			nextView();
			viewChanged(flipper.getCurrentView());
		}
	}

	// Next, Previous Views
	private void previousView() {
		// Previous View
		flipper.setInAnimation(getContext(), R.anim.in_animation1);
		flipper.setOutAnimation(getContext(), R.anim.out_animation1);
		flipper.showPrevious();
	}

	private void nextView() {

		// Next View
		flipper.setInAnimation(getContext(), R.anim.in_animation);
		flipper.setOutAnimation(getContext(), R.anim.out_animation);
		flipper.showNext();

	}

	public void select(int position){
		flipper.setDisplayedChild(position);
	}
	
	public abstract void viewChanged(View currentView);

	public abstract Context getContext();
}
