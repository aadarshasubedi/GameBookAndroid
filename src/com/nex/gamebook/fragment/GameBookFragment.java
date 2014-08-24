package com.nex.gamebook.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Stats;

public abstract class GameBookFragment {
	private Context context;
	private Player character;

	public GameBookFragment(Context context) {
		this.context = context;
	}

	public abstract View create(ViewGroup container);

	public Bundle getArguments() {
		Activity activity = (Activity) getContext();
		return activity.getIntent().getExtras();
	}

	public void setArguments(Bundle bundle) {
		Activity activity = (Activity) getContext();
		activity.getIntent().putExtras(bundle);
	}

	public Context getContext() {
		return context;
	}

	public Player getCharacter() {
		return character;
	}

	public void setCharacter(Player character) {
		this.character = character;
	}

	protected void fillDefaultStats(View view) {
		final Player _character = getCharacter();
		TextView attr = (TextView) view.findViewById(R.id.sel_attr_health_d);
		attr.setText(String.valueOf(_character.getStats().getHealth()));

		attr = (TextView) view.findViewById(R.id.sel_attr_attack_d);
		attr.setText(String.valueOf(_character.getStats().getAttack()));
		attr = (TextView) view.findViewById(R.id.sel_attr_defense_d);
		attr.setText(String.valueOf(_character.getStats().getDefense()));

		attr = (TextView) view.findViewById(R.id.sel_attr_skill_d);
		attr.setText(String.valueOf(_character.getStats().getSkill()));

		attr = (TextView) view.findViewById(R.id.sel_attr_luck_d);
		attr.setText(String.valueOf(_character.getStats().getLuckPercentage()));

		attr = (TextView) view.findViewById(R.id.critical_d);
		attr.setText(String.valueOf(_character.getStats().getSkillPercentage()));

		attr = (TextView) view.findViewById(R.id.sel_l_def_perc_d);
		attr.setText(String.valueOf(_character.getStats()
				.getDefensePercentage()));

		attr = (TextView) view.findViewById(R.id.sel_l_baseDmg_d);
		attr.setText(String.valueOf(_character.getStats().getDamage()));
		
		
		attr = (TextView) view.findViewById(R.id.skill_power);
		attr.setText(String.valueOf(_character.getCurrentStats().getSkillPower()));
	}

	protected void showStats(View view, Stats stats, Stats defaultStats, boolean colorify) {
		final Player _character = getCharacter();
		TextView attr = (TextView) view.findViewById(R.id.sel_attr_health);
		attr.setText(String.valueOf(stats.getHealth()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));
		if(colorify)
		changeAttributeColor(view.getContext(), attr, defaultStats.getHealth(), stats.getHealth());

		attr = (TextView) view.findViewById(R.id.sel_attr_luck);
		attr.setText(String.valueOf(stats.getLuckPercentage()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));
		
		if(colorify)
		changeAttributeColor(view.getContext(), attr, defaultStats
				.getLuck(), stats.getLuck());
		if(colorify)
		changeAttributeColor(view.getContext(),
				(TextView) view.findViewById(R.id.luck_perc), defaultStats.getLuck(), stats
						.getLuck());

		attr = (TextView) view.findViewById(R.id.sel_attr_defense);
		attr.setText(String.valueOf(stats.getDefense()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));
		
		if(colorify)
		changeAttributeColor(view.getContext(), attr, defaultStats
				.getDefense(), stats.getDefense());

		attr = (TextView) view.findViewById(R.id.sel_attr_skill);
		attr.setText(String.valueOf(stats.getSkill()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));
		
		if(colorify)
		changeAttributeColor(view.getContext(), attr, defaultStats.getSkill(), stats.getSkill());

		attr = (TextView) view.findViewById(R.id.sel_attr_attack);
		attr.setText(String.valueOf(stats.getAttack()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));
		
		if(colorify)
		changeAttributeColor(view.getContext(), attr, defaultStats
				.getAttack(), stats.getAttack());

		attr = (TextView) view.findViewById(R.id.critical);
		attr.setText(String.valueOf(stats.getSkillPercentage()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));

		attr = (TextView) view.findViewById(R.id.sel_l_def_perc);
		attr.setText(String.valueOf(stats.getDefensePercentage()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));

		attr = (TextView) view.findViewById(R.id.sel_attr_baseDmg_d);
		attr.setText(String.valueOf(stats.getDamage()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));
		
		if(colorify)
		changeAttributeColor(view.getContext(), attr, defaultStats
				.getDamage(), stats.getDamage());
		
		attr = (TextView) view.findViewById(R.id.skill_power);
		attr.setText(String.valueOf(stats.getSkillPower()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));
		
		if(colorify)
		changeAttributeColor(view.getContext(), attr, defaultStats.getSkillPower(), stats.getSkillPower());
	}

	@SuppressLint("ResourceAsColor")
	private void changeAttributeColor(Context ctx, TextView text,
			int defaultValue, int currentvalue) {
		if (currentvalue > defaultValue) {
			text.setTextColor(ctx.getResources().getColor(R.color.positive));
		} else if (currentvalue < defaultValue) {
			text.setTextColor(ctx.getResources().getColor(R.color.negative));
		} else {
			text.setTextColor(ctx.getResources().getColor(R.color.number_color));
		}
	}
}
