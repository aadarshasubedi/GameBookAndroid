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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.nex.gamebook.MainScreenActivity;
import com.nex.gamebook.R;
import com.nex.gamebook.entity.CharacterType;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.entity.Stats;
import com.nex.gamebook.story.section.StorySection;

public class PlaygroundBattleLogCharacterTab extends AbstractFragment {

	private Player _character;
	private PlaygroundActivity activity;
	private StorySection section;
	private Button resultButton;
	private boolean fighting = false;

	public PlaygroundBattleLogCharacterTab(Player ch,
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
						.getString(R.string.keyword_from)
				+ " "
				+ Stats.getPercentage(Player.MAX_LUCK_OF_CHARACTER,
						Player.TOTAL_LUCK_FOR_CALC) + "%)");

		attr = (TextView) view.findViewById(R.id.playground_def_skill);
		attr.setText(String.valueOf(_character.getStats().getSkill())
				+ " ("
				+ _character.getCurrentStats().getSkillPercentage()
				+ "% "
				+ view.getContext().getResources()
						.getString(R.string.keyword_from)
				+ " "
				+ Stats.getPercentage(Player.MAX_SKILL_OF_CHARACTER,
						Player.TOTAL_SKILL_FOR_CALC) + "%)");

		attr = (TextView) view.findViewById(R.id.playground_def_defense);
		attr.setText(String.valueOf(_character.getStats().getDefense()));

		attr = (TextView) view.findViewById(R.id.playground_def_attack);
		attr.setText(String.valueOf(_character.getStats().getAttack()));

	}

	private void prepareBattleLog(View view) {
		Button prev = (Button) view.findViewById(R.id.button1);
		Button next = (Button) view.findViewById(R.id.button2);
		final ViewSwitcher switcher = (ViewSwitcher) view.findViewById(R.id.viewSwitcher1);
		BattleLogAdapter adapter = new BattleLogAdapter(view.getContext(), view);
		for(Enemy enemy: section.getEnemies()) {
			View enemyView = adapter.getView(enemy, (ViewGroup) view);
			switcher.addView(enemyView);
		}
		prev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switcher.showPrevious();
			}
		});
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switcher.showNext();
			}
		});
		if (!section.isEnemiesAlreadyKilled() && this.fighting)
			section.tryApplyLuckForBattle(_character);
		if (section.isHasLuck()) {
			if (section.isLuckDefeatEnemies()) {
				section.setEnemiesAlreadyKilled(true);
			}
//			list.setAdapter(new NoBattleLogAdapter(view.getContext()));
			if (fighting) {
				displayContinueButton();
			}
		} else {
//			list.setAdapter();
		}

	}

	private void showCurrentValues(View view) {
		TextView attr = (TextView) view.findViewById(R.id.playground_curr_health);
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
		} else {
			view.findViewById(R.id.textView1).setVisibility(View.GONE);
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
			View rowView = inflater.inflate(R.layout.battle_log_layout_nobattle, parent, false);
			TextView result = (TextView) rowView.findViewById(R.id.noBattleText);
			result.setTextColor(context.getResources().getColor(R.color.bonus_color));
			result.setText(context.getResources().getString(
					section.getLuckText())
					+ " "
					+ context.getResources().getString(
							R.string.fight_aspect_luck));
			return rowView;
		}
	}

	class BattleLogAdapter {
		private final Context context;
		private View masterView;

		public BattleLogAdapter(Context context, View view) {
			this.context = context;
			masterView = view;
		}

		public View getView(final Enemy enemy, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View rowView = inflater.inflate(R.layout.battle_log_layout, parent, false);
				TextView textView = (TextView) rowView.findViewById(R.id.enemy);
				final View startFight = rowView.findViewById(R.id.fight);
//				decoreClickableTextView(context, (TextView) startFight, R.string.button_fight);
				if (enemy.isDefeated() || _character.isDefeated() || _character.isFighting()) {
					startFight.setVisibility(View.GONE);
				} else {
					final FightingLog log = new FightingLog(this, enemy);
					startFight.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							_character.setFighting(true);
							startFight.setVisibility(View.GONE);
							v.post(log);
						}
					});
				}

				TextView attack = (TextView) rowView.findViewById(R.id.attack);
				attack.setText(String.valueOf(enemy.getCurrentStats().getAttack()));
				attack = (TextView) rowView.findViewById(R.id.health);
				attack.setText(String.valueOf(enemy.getCurrentStats().getHealth()));
				attack = (TextView) rowView.findViewById(R.id.luck);
				attack.setText(String.valueOf(enemy.getCurrentStats().getLuck()));
				attack = (TextView) rowView.findViewById(R.id.skill);
				attack.setText(String.valueOf(enemy.getCurrentStats().getSkill()));
				attack = (TextView) rowView.findViewById(R.id.defense);
				attack.setText(String.valueOf(enemy.getCurrentStats().getDefense()));

				textView.setText(enemy.getName());
			return rowView;
		}

	}

	class FightingLog implements AttackCallback, Runnable {
		private LinearLayout log;
		private BattleLogAdapter adapter;
		private Enemy enemy;
		public FightingLog(BattleLogAdapter ad, Enemy enemy) {
			super();
			this.adapter = ad;
			this.enemy = enemy;
			this.log = (LinearLayout) ad.masterView
					.findViewById(R.id.battle_log);
			log.removeAllViews();
		}

		@Override
		public Player getCharacter() {
			return _character;
		}

		@Override
		public void attackCallBack(ResultCombat resultCombat) {
			int color = R.color.bonus_color;
			String text = "\n";
			Context context = adapter.context;
			if(resultCombat.isLuck()) {
				if(resultCombat.getType().equals(CharacterType.ENEMY)) {
					text += context.getResources().getString(R.string.enemy_has_luck);
					color = R.color.debuf_color;
				} else {
					text += context.getResources().getString(R.string.you_have_luck);
				}
			} else {
				if(resultCombat.getType().equals(CharacterType.ENEMY)) {
					text += context.getResources().getString(R.string.you_take);
					color = R.color.debuf_color;
				} else {
					text += context.getResources().getString(R.string.you_cause);
				}
				text += " " + String.valueOf(resultCombat.getDamage()) + " ";
				if(resultCombat.isCritical()) {
					text += context.getResources().getString(R.string.critical_chance) + " ";
				}
				text += context.getResources().getString(R.string.damage);
			}
			TextView battleText = new TextView(context);
			battleText.setText(text);
			battleText.setTextAppearance(context, R.style.textview_anchor);
			battleText.setTextColor(context.getResources().getColor(color));
			log.addView(battleText);
			log.invalidate();
			showCurrentValues(adapter.masterView);
		}

		@Override
		public void fightEnd() {
			_character.setFighting(false);
			if (_character.isDefeated()) {
				displayGameOverButton();
			} else if (section.isAllDefeated()) {
				section.setEnemiesAlreadyKilled(true);
				displayContinueButton();
			}
//			adapter.notifyDataSetChanged();
		}
		
		@Override
		public void run() {
			enemy.fight(this);
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
