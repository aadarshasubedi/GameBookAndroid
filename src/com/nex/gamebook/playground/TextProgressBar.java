package com.nex.gamebook.playground;

import com.nex.gamebook.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar {
	private String text;
	private Paint textPaint;

	public TextProgressBar(Context context) {
		super(context);
		init(context);
	}

	public TextProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void init(Context ctx) {
		text = "0/0";
		textPaint = new Paint();//ctx.getResources().getColor(R.color.number_color)
		textPaint.setColor(0xFFFFFFFF);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// First draw the regular progress bar, then custom draw our text
		super.onDraw(canvas);
		Rect bounds = new Rect();
		textPaint.setTextSize(40f);
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		int x = getWidth() / 2 - bounds.centerX();
		int y = getHeight() / 2 - bounds.centerY();
		canvas.drawText(text, x, y, textPaint);
	}

	public synchronized void setText(String text) {
		this.text = text;
		drawableStateChanged();
	}

	public void setTextColor(int color) {
		textPaint.setColor(color);
		drawableStateChanged();
	}
}
