package com.nex.gamebook.playground;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nex.gamebook.MainScreenActivity;
import com.nex.gamebook.R;
import com.nex.gamebook.entity.character.Character;
import com.nex.gamebook.entity.character.Stats;
import com.nex.gamebook.story.Enemy;
import com.nex.gamebook.story.section.StorySection;

public class PlaygroundBattleLogCharacterTab extends AbstractFragment {

	private Character _character;
	private PlaygroundActivity activity;
	private StorySection section;
	private Button resultButton;
	private boolean fighting = false;

	public PlaygroundBattleLogCharacterTab(Character ch,
			PlaygroundActivity activity) {
		this._character = ch;
		this.activity = activity;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_playground_character,
				container, false);
		showCurrentValues(view);
		showDefaultValues(view);
		resultButton = (Button) view.findViewById(R.id.result_button);
		resultButton.setVisibility(View.GONE);

		if (section != null) {
			prepareBattleLog(view);
		}
		return view;
	}

	private void showDefaultValues(View view) {
		TextView attr = (TextView) view
				.findViewById(R.id.playground_def_health);
		attr.setText(String.valueOf(_character.getStats().getHealth()));

		attr = (TextView) view.findViewById(R.id.playground_def_luck);
		attr.setText(String.valueOf(_character.getStats().getLuck())
				+ " ("
				+ _character.getCurrentStats().getLuckPercentage()
				+ "% "
				+ view.getContext().getResources()
						.getString(R.string.keyword_from) + " "
				+ Stats.getLuckPercentage(Character.MAX_LUCK_OF_CHARACTER)
				+ "%)");

		attr = (TextView) view.findViewById(R.id.playground_def_defense);
		attr.setText(String.valueOf(_character.getStats().getDefense()));

		attr = (TextView) view.findViewById(R.id.playground_def_skill);
		attr.setText(String.valueOf(_character.getStats().getSkill()));

	}

	private void prepareBattleLog(View view) {
		ListView list = (ListView) view.findViewById(R.id.battle_log);
		if (!section.isEnemiesAlreadyKilled() && this.fighting)
			section.tryApplyLuckForBattle(_character);
		if (section.isHasLuck()) {
			if (section.isLuckDefeatEnemies()) {
				section.setEnemiesAlreadyKilled(true);
			}
			list.setAdapter(new NoBattleLogAdapter(view.getContext()));
			if (fighting) {
				displayContinueButton();
			}
		} else {
			list.setAdapter(new BattleLogAdapter(view.getContext(), view));
		}

	}

	private void showCurrentValues(View view) {
		TextView attr = (TextView) view
				.findViewById(R.id.playground_curr_health);
		attr.setText(String.valueOf(_character.getCurrentStats().getHealth()));
		changeAttributeColor(view.getContext(), attr, _character.getStats()
				.getHealth(), _character.getCurrentStats().getHealth());

		attr = (TextView) view.findViewById(R.id.playground_curr_luck);
		attr.setText(String.valueOf(_character.getCurrentStats().getLuck()));
		changeAttributeColor(view.getContext(), attr, _character.getStats()
				.getLuck(), _character.getCurrentStats().getLuck());

		attr = (TextView) view.findViewById(R.id.playground_curr_defense);
		attr.setText(String.valueOf(_character.getCurrentStats().getDefense()));
		changeAttributeColor(view.getContext(), attr, _character.getStats()
				.getDefense(), _character.getCurrentStats().getDefense());

		attr = (TextView) view.findViewById(R.id.playground_curr_skill);
		attr.setText(String.valueOf(_character.getCurrentStats().getSkill()));
		changeAttributeColor(view.getContext(), attr, _character.getStats()
				.getSkill(), _character.getCurrentStats().getSkill());

		if (fighting) {
			view.findViewById(R.id.textView1).setVisibility(View.VISIBLE);
			view.findViewById(R.id.underline).setVisibility(View.VISIBLE);
		} else {
			view.findViewById(R.id.textView1).setVisibility(View.GONE);
			view.findViewById(R.id.underline).setVisibility(View.GONE);
		}

	}

	@SuppressLint("ResourceAsColor")
	private void changeAttributeColor(Context ctx, TextView text,
			int defaultValue, int currentvalue) {
		if (currentvalue > defaultValue) {
			text.setTextColor(ctx.getResources().getColor(R.color.bonus_color));
		} else if (currentvalue < defaultValue) {
			text.setTextColor(ctx.getResources().getColor(R.color.debuf_color));
		} else {
			text.setTextColor(ctx.getResources().getColor(
					R.color.attrName_color));
		}
	}

	public void fight(StorySection section) {
		this.section = section;
		this.fighting = true;
	}

	class NoBattleLogAdapter extends ArrayAdapter<String> {
		Context context;

		public NoBattleLogAdapter(Context context) {
			super(context, R.layout.battle_log_layout_nobattle, new String[1]);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.battle_log_layout_nobattle, parent, false);
			TextView result = (TextView) rowView
					.findViewById(R.id.noBattleText);
			result.setTextColor(context.getResources().getColor(
					R.color.bonus_color));
			result.setText(context.getResources().getString(
					section.getLuckText())
					+ " "
					+ context.getResources().getString(
							R.string.fight_aspect_luck));
			return rowView;
		}
	}

	class BattleLogAdapter extends ArrayAdapter<String> {
		private final Context context;
		private View masterView;

		public BattleLogAdapter(Context context, View view) {
			super(context, R.layout.battle_log_layout, new String[section
					.getEnemies().size()]);
			this.context = context;
			masterView = view;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.battle_log_layout, parent,
					false);
			final Enemy enemy = section.getEnemies().get(position);
			TextView textView = (TextView) rowView.findViewById(R.id.enemy);
			TextView startFight = (TextView) rowView.findViewById(R.id.fight);
			decoreClickableTextView(inflater.getContext(), startFight,
					R.string.button_fight);
			final TextView resultFight = (TextView) rowView
					.findViewById(R.id.fight_result_as_text);
			if (enemy.isDefeated()) {
				startFight.setVisibility(View.GONE);
				resultFight.setText(context.getResources().getString(
						enemy.getResultText())
						+ " "
						+ context.getResources().getString(
								enemy.getResultAttrText()));
				if (enemy.isAffectPlayer()) {
					resultFight.setTextColor(context.getResources().getColor(
							R.color.debuf_color));
				} else {
					resultFight.setTextColor(context.getResources().getColor(
							R.color.bonus_color));
				}

			} else if (_character.isDefeated()) {
				resultFight.setVisibility(View.GONE);
				startFight.setVisibility(View.GONE);
			} else {
				resultFight.setVisibility(View.GONE);
				startFight.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						enemy.setDefeated(true);
						enemy.fight(_character);
						if (_character.isDefeated()) {
							displayGameOverButton();
						} else if (section.isAllDefeated()) {
							section.setEnemiesAlreadyKilled(true);
							displayContinueButton();
						}
						notifyDataSetChanged();
						showCurrentValues(masterView);
					}
				});
			}
			TextView attack = (TextView) rowView.findViewById(R.id.attack);
			attack.setText(String.valueOf(enemy.getAttack()));
			textView.setText(enemy.getName());
			return rowView;
		}

	}

	private void displayGameOverButton() {
		resultButton.setText(R.string.button_endGame_lose);
		resultButton.setOnClickListener(gameoverListener);
		resultButton.setVisibility(View.VISIBLE);
	}

	private void displayContinueButton() {
		resultButton.setText(R.string.sg_continue);
		resultButton.setOnClickListener(continueListener);
		resultButton.setVisibility(View.VISIBLE);
	}

	OnClickListener continueListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			activity.changeToStory();
			section = null;
			fighting = false;
		}
	};
	OnClickListener gameoverListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getView().getContext(),
					MainScreenActivity.class);
			getView().getContext().startActivity(intent);
		}
	};

	public void setFighting(boolean fighting) {
		this.fighting = fighting;
	}

}
