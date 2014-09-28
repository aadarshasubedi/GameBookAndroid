package com.nex.gamebook.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.skills.active.Skill;
public class SkillInfoDialog {

	private Dialog dialog;
	private Context ctx;
	public SkillInfoDialog(Context context, Character applicator) {
		this(context, applicator, applicator.getSelectedSkill(), null);
	}

	
	public SkillInfoDialog(Context context, Character applicator, Skill skill, final OnClickListener listener) {
		super();
		this.ctx = context;
		dialog = new Dialog(context);
//		dialog.
		//View inflatedView = dialog.getLayoutInflater().inflate(R.layout.skill_info_layout, context);
		dialog.setContentView(R.layout.skill_info_layout);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		showSkillData(applicator, skill, dialog.findViewById(R.id.skill_data));
		if(listener!=null) {
			dialog.findViewById(R.id.use_button).setVisibility(View.VISIBLE);
			
			dialog.findViewById(R.id.use_button).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.onClick(v);
					dismiss();
				}
			});
		}
		
	}

	public void showSkillData(Character applicator, Skill skill, View inflatedView) {
		TextView view = (TextView) inflatedView.findViewById(R.id.skill_name);
		view.setText(skill.getName());
		
		view = (TextView) inflatedView.findViewById(R.id.skill_description);
		view.setText(skill.getDescription(ctx, applicator));
		int value = skill.getValue(applicator);
		
		if(value < 0) {
			View powerRow = inflatedView.findViewById(R.id.power_row);
			powerRow.setVisibility(View.GONE);
		}
		if(skill.getOvertimeTurns(applicator)>0) {
			View durabilityRow = inflatedView.findViewById(R.id.skill_durability);
			durabilityRow.setVisibility(View.VISIBLE);
			TextView d = (TextView) durabilityRow.findViewById(R.id.overtimeskill_turns);
			d.setText(String.valueOf(skill.getOvertimeTurns(applicator)));
		}
		
		TextView skillPower = (TextView) inflatedView.findViewById(R.id.skill_power);
		skillPower.setText(String.valueOf(value));
		
		view = (TextView) inflatedView.findViewById(R.id.skill_trigger);
		if(skill.isTriggerAfterEnemyAttack()) {
			view.setText(R.string.special_skill_trigger_after_enemy);
		} else if(skill.isTriggerBeforeEnemyAttack()) {
			view.setText(R.string.special_skill_trigger_before_enemy);
		} else if(skill.afterNormalAttack()){
			view.setText(R.string.special_skill_trigger_after);
		} else{
			view.setText(R.string.special_skill_trigger_normal);
		}
		if(skill.isTriggerBeforeEnemySpecialAttack()) {
			String t = String.valueOf(view.getText());
			t += " " + ctx.getString(R.string.or) + " " + ctx.getString(R.string.special_skill_trigger_before_skill);
			view.setText(t);
		}
		view = (TextView) inflatedView.findViewById(R.id.attempts);
		String att = String.valueOf(skill.attemptsPerFight());
		view.setText(att);
		if(skill.resetAtBattleEnd()) {
			view.setText(att + "x ");
			TextView v = (TextView) inflatedView.findViewById(R.id.attempts_marker);
			v.setText(R.string.in_battle);
		} else if(skill.attemptsPerFight() < 0) {
			view.setText(R.string.always);
			inflatedView.findViewById(R.id.attempts_marker).setVisibility(View.GONE);
		}
	}
	
	public void show() {
		this.dialog.show();
		//Grab the window of the dialog, and change the width
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		//This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
	}
	
	public void dismiss() {
		this.dialog.dismiss();
	}
}
