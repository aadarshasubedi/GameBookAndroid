package com.nex.gamebook.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nex.gamebook.R;

public class TooltipImageView extends ImageView {
	public TooltipImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		addTooltipListener(attrs);
	}

	public TooltipImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		addTooltipListener(attrs);
	}

	public TooltipImageView(Context context) {
		super(context);
	}
	String tooltip = null;
	private void addTooltipListener(AttributeSet set) {
		TypedArray a = getContext().obtainStyledAttributes(set,R.styleable.imageTooltip);
	    CharSequence s = a.getString(R.styleable.imageTooltip_imageTooltip);
	    
	    if (s != null) {
	        tooltip = s.toString();
	    }
	    a.recycle();
	    if(tooltip!=null)
		setOnClickListener(new OnClickListener() {

			// @SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				if (tooltip != null) {
					Toast.makeText(getContext(), tooltip, Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	public void changeColor(int color) {
		setColorFilter(color, Mode.SRC_ATOP);
	}
}
