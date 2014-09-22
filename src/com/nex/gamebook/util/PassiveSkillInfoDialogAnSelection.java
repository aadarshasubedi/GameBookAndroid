package com.nex.gamebook.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.SkillMap;
import com.nex.gamebook.playground.SkillsSpinner;
import com.nex.gamebook.skills.active.Skill;
import com.nex.gamebook.skills.passive.PassiveSkill;
public class PassiveSkillInfoDialogAnSelection {

	private Dialog dialog;
	private Context ctx;
	DismissCallBack dismiss;
	public PassiveSkillInfoDialogAnSelection(Context context, final Player player, final String skill) {
		super();
		this.ctx = context;
		dialog = new Dialog(context);
//		dialog.
		//View inflatedView = dialog.getLayoutInflater().inflate(R.layout.skill_info_layout, context);
		dialog.setContentView(R.layout.passive_skill_info_layout);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		Button button = (Button) dialog.findViewById(R.id.learn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				player.learnPassiveSkill(skill);
				player.setSkillPoints(player.getSkillPoints() - 1);
				dismiss();
			}
		});
		if(player.getSkillPoints()==0) {
			button.setVisibility(View.GONE);
		}
		showSkillData(player, SkillMap.getPassive(skill));
//		showSkillData(applicator, skill, dialog.findViewById(R.id.skill_data));
		
	}
	public void showSkillData(Character applicator, PassiveSkill skill) {
		TextView view = (TextView) dialog.findViewById(R.id.skill_name);
		view.setText(skill.getName(applicator.getStory().getProperties()));
		view = (TextView) dialog.findViewById(R.id.skill_description);
		view.setText(skill.getDescription(ctx, applicator));
	}
	
	
	public void show(DismissCallBack d) {
		this.dialog.show();
		//Grab the window of the dialog, and change the width
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		//This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		this.dismiss = d;
	}
	public interface DismissCallBack {
		void dismiss();
	}
	public void dismiss() {
		this.dialog.dismiss();
		this.dismiss.dismiss();
	}
}
