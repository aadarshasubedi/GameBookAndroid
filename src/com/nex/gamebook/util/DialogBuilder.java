package com.nex.gamebook.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nex.gamebook.R;

public class DialogBuilder {

	private Dialog dialog;

	public DialogBuilder(Context context) {
		super();
		dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_layout);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}

	public DialogBuilder setText(int id) {
		// set up text
		TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
		text.setText(id);
		return this;
	}

	public DialogBuilder setPositiveButton(int textId, OnClickListener listener) {
		setButton(textId, R.id.positiveButon, listener);
		return this;
	}

	public DialogBuilder setNegativeButton(int textId, OnClickListener listener) {
		setButton(textId, R.id.negativeButton, listener);
		return this;
	}

	private void setButton(int textId, int buttonId, OnClickListener listener) {
		// set up button
		if (listener == null) {
			listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			};
		}
		Button button = (Button) dialog.findViewById(buttonId);
		button.setText(textId);
		button.setOnClickListener(listener);
	}

	public DialogBuilder setTitle(int id) {
		return this;
	}

	public void show() {
		this.dialog.show();
	}
	
	public void dismiss() {
		this.dialog.dismiss();
	}
	
}
