package com.nex.gamebook.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.Stats;
import com.nex.gamebook.playground.TextProgressBar;
import com.nex.gamebook.util.TooltipImageView;

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

	private void highlightPrimaryAttributeIcon(View view) {
		if (character == null)
			return;
		int color = R.color.temporal;
		if (StatType.HEALTH.equals(character.getPrimaryStat())) {
			((TooltipImageView) view.findViewById(R.id.ico_health)).changeColor(getContext().getResources().getColor(color));
		} else if (StatType.ATTACK.equals(character.getPrimaryStat())) {
			((TooltipImageView) view.findViewById(R.id.ico_attack)).changeColor(getContext().getResources().getColor(color));
		} else if (StatType.DEFENSE.equals(character.getPrimaryStat())) {
			((TooltipImageView) view.findViewById(R.id.ico_defense)).changeColor(getContext().getResources().getColor(color));
		} else if (StatType.SKILL.equals(character.getPrimaryStat())) {
			((TooltipImageView) view.findViewById(R.id.ico_skill)).changeColor(getContext().getResources().getColor(color));
		} else if (StatType.LUCK.equals(character.getPrimaryStat())) {
			((TooltipImageView) view.findViewById(R.id.ico_luck)).changeColor(getContext().getResources().getColor(color));
		} else if (StatType.SKILLPOWER.equals(character.getPrimaryStat())) {
			((TooltipImageView) view.findViewById(R.id.ico_skillpower)).changeColor(getContext().getResources().getColor(color));
		}
	}

	protected void showStats(View view, Character c, boolean isBase) {
		Stats stats = c.getCurrentStats();
		Stats defaultStats = c.getStats();
		if (isBase) {
			stats = defaultStats;
		}
		boolean colorify = !isBase;
		highlightPrimaryAttributeIcon(view);
		TextView attr = (TextView) view.findViewById(R.id.sel_attr_luck);
		attr.setText(String.valueOf(stats.getLuckPercentage()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));
		((TextView) view.findViewById(R.id.luck_perc)).setTextColor(getContext().getResources().getColor(R.color.number_color));
		if (colorify)
			changeAttributeColor(view.getContext(), attr, defaultStats.getLuck(), stats.getLuck());
		if (colorify)
			changeAttributeColor(view.getContext(), (TextView) view.findViewById(R.id.luck_perc), defaultStats.getLuck(), stats.getLuck());

		attr = (TextView) view.findViewById(R.id.sel_attr_defense);
		attr.setText(String.valueOf(stats.getDefense()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));

		if (colorify)
			changeAttributeColor(view.getContext(), attr, defaultStats.getDefense(), stats.getDefense());

		attr = (TextView) view.findViewById(R.id.sel_attr_skill);
		attr.setText(String.valueOf(stats.getSkill()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));

		if (colorify)
			changeAttributeColor(view.getContext(), attr, defaultStats.getSkill(), stats.getSkill());

		attr = (TextView) view.findViewById(R.id.sel_attr_attack);
		attr.setText(String.valueOf(stats.getAttack()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));

		if (colorify)
			changeAttributeColor(view.getContext(), attr, defaultStats.getAttack(), stats.getAttack());

		attr = (TextView) view.findViewById(R.id.critical);
		attr.setText(String.valueOf(stats.getSkillPercentage()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));

		attr = (TextView) view.findViewById(R.id.sel_l_def_perc);
		attr.setText(String.valueOf(stats.getDefensePercentage()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));

		attr = (TextView) view.findViewById(R.id.sel_attr_baseDmg_d);
		attr.setText(String.valueOf(stats.getDamage()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));

		if (colorify)
			changeAttributeColor(view.getContext(), attr, defaultStats.getDamage(), stats.getDamage());

		attr = (TextView) view.findViewById(R.id.skill_power);
		attr.setText(String.valueOf(stats.getSkillpower()));
		attr.setTextColor(getContext().getResources().getColor(R.color.number_color));

		TextProgressBar progress = (TextProgressBar) view.findViewById(R.id.p_health_progress);
		changeHealthProgressColor(progress, c);

		progress.setText(stats.getHealth() + "/" + defaultStats.getHealth());
		View summon = view.findViewById(R.id.player_summon);
		if(summon!=null)
		if (c.getSummon() != null) {
			summon.setVisibility(View.VISIBLE);
			TextProgressBar summonHealth = (TextProgressBar) view.findViewById(R.id.summon_health_player);
			changeHealthProgressColor(summonHealth, c.getSummon());
		} else {
			summon.setVisibility(View.GONE);
		}

		if (colorify)
			changeAttributeColor(view.getContext(), attr, defaultStats.getSkillpower(), stats.getSkillpower());
	}

	public void changeHealthProgressColor(TextProgressBar progress, Character ch) {
		int healthPercentage = (int) (((double) ch.getCurrentStats().getHealth() / (double) ch.getStats().getHealth()) * 100);
		// Log.i("HealthProgressBar", ch.getName() + " has " + healthPercentage
		// + "%");
		if (healthPercentage > 100) {
			healthPercentage = 100;
		}
		int color = R.drawable.health_bar_full;
		if (healthPercentage <= 50) {
			color = R.drawable.health_bar_half;
		}
		if (healthPercentage <= 20) {
			color = R.drawable.health_bar_quarter;
		}
		if (ch.hasDots()) {
			color = R.drawable.health_bar_condition;
		}
		changeProgressBarColor(progress, color);
		progress.setProgress(healthPercentage);
		progress.setText(ch.getCurrentStats().getHealth() + "/" + ch.getStats().getHealth());
	}

	public void changeProgressBarColor(ProgressBar pg, int drawable) {
		Drawable d = context.getResources().getDrawable(drawable);
		pg.setProgressDrawable(d);
	}

	@SuppressLint("ResourceAsColor")
	protected void changeAttributeColor(Context ctx, TextView text, int defaultValue, int currentvalue) {
		if (currentvalue > defaultValue) {
			text.setTextColor(ctx.getResources().getColor(R.color.positive));
		} else if (currentvalue < defaultValue) {
			text.setTextColor(ctx.getResources().getColor(R.color.negative));
		} else {
			text.setTextColor(ctx.getResources().getColor(R.color.number_color));
		}
	}
}
