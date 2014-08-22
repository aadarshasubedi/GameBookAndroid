package com.nex.gamebook.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialSkill;

public class SkillInfoDialog {

	private Dialog dialog;

	public SkillInfoDialog(Context context, SpecialSkill skill) {
		super();
		dialog = new Dialog(context);
		dialog.setContentView(R.layout.skill_info_layout);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		TextView view = (TextView) dialog.findViewById(R.id.skill_name);
		view.setText(context.getString(skill.getNameId()));
		
		view = (TextView) dialog.findViewById(R.id.skill_description);
		view.setText(context.getString(skill.getDescriptionId()));
		
		view = (TextView) dialog.findViewById(R.id.skill_type);
		view.setText(context.getString(skill.getTypeId()));
//		
//		setButton(R.string.confirm, R.id.confirm, null);
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
		button.setVisibility(View.VISIBLE);
		button.setText(textId);
		button.setOnClickListener(listener);
	}


	public void show() {
		this.dialog.show();
	}
	
	public void dismiss() {
		this.dialog.dismiss();
	}
}
