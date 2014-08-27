package com.nex.gamebook.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.entity.Character;
public class SkillInfoDialog {

	private Dialog dialog;

	public SkillInfoDialog(Context context, Character applicator) {
		this(context, applicator, applicator.getSpecialSkill());
		
	}
	public SkillInfoDialog(Context context, Character applicator, SpecialSkill skill) {
		super();
		dialog = new Dialog(context);
//		dialog.
		//View inflatedView = dialog.getLayoutInflater().inflate(R.layout.skill_info_layout, context);
		dialog.setContentView(R.layout.skill_info_layout);
		dialog.setCancelable(true);
		
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		showSkillData(applicator, skill, dialog.findViewById(R.id.skill_data));
		
	}

	public void showSkillData(Character applicator, SpecialSkill skill, View inflatedView) {
		TextView view = (TextView) inflatedView.findViewById(R.id.skill_name);
		view.setText(skill.getNameId());
		
		view = (TextView) inflatedView.findViewById(R.id.skill_description);
		view.setText(skill.getDescriptionId());
		
		view = (TextView) inflatedView.findViewById(R.id.skill_type);
		TextView aspect = (TextView) inflatedView.findViewById(R.id.skill_type_aspect);
		aspect.setVisibility(View.GONE);
		if(!skill.isPermanent()) {
			aspect.setVisibility(View.VISIBLE);
			aspect.setText(inflatedView.getContext().getString(R.string.special_skill_type_temp));
		}
		view.setText(inflatedView.getContext().getString(skill.getTypeId()));
		
		int value = skill.getValue(applicator);
		int luckValue = skill.getValueWhenLuck(applicator);
		if(value < 0) {
			View powerRow = inflatedView.findViewById(R.id.power_row);
			powerRow.setVisibility(View.GONE);
		}
		view = (TextView) inflatedView.findViewById(R.id.skill_aspect);
		view.setText(skill.getAspectId());
		
		TextView skillPower = (TextView) inflatedView.findViewById(R.id.skill_power);
		TextView skillPowerLuck = (TextView) inflatedView.findViewById(R.id.skill_power_luck);
		String mark = "";
		if(skill.showPercentage()) {
			mark = "%";
		}
		skillPower.setText(String.valueOf(value) + mark);
		skillPowerLuck.setText(String.valueOf(luckValue) + mark);
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
		view = (TextView) inflatedView.findViewById(R.id.attempts);
		view.setText(String.valueOf(skill.attemptsPerFight()));
		if(!skill.inFight()) {
			view.setText(R.string.once_in_battle);
			inflatedView.findViewById(R.id.attempts_marker).setVisibility(View.GONE);
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
