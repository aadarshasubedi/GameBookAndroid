package com.nex.gamebook.playground;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
			if(fighting) {
				//clear log when entering new combat
				LinearLayout log = (LinearLayout) view.findViewById(R.id.battle_log);
				log.removeAllViews();
			}
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
				+ "%)");

		attr = (TextView) view.findViewById(R.id.playground_def_skill);
		attr.setText(String.valueOf(_character.getStats().getSkill())
				+ " ("
				+ _character.getCurrentStats().getSkillPercentage()
				+ "%)");

		attr = (TextView) view.findViewById(R.id.playground_def_defense);
		attr.setText(String.valueOf(_character.getStats().getDefense()));

		attr = (TextView) view.findViewById(R.id.playground_def_attack);
		attr.setText(String.valueOf(_character.getStats().getAttack()));

	}

	private void prepareBattleLog(View view) {
		if (!section.isEnemiesAlreadyKilled() && this.fighting) {
			section.tryApplyLuckForBattle(_character);
		}
		View prev = view.findViewById(R.id.button1);
		View next = view.findViewById(R.id.button2);
		final TextView enemyName = (TextView) view.findViewById(R.id.enemy_name);
		final ViewSwitcher switcher = (ViewSwitcher) view.findViewById(R.id.viewSwitcher1);
		BattleLogAdapter adapter = new BattleLogAdapter(view.getContext(), view);
		for(int i = 0; i < section.getEnemies().size(); i++) {
			Enemy enemy = section.getEnemies().get(i);
			enemy.setIndex(i+1);
			View enemyView = adapter.create(enemy, switcher);
			switcher.addView(enemyView);
		}
		prev.setOnClickListener(new OnClickListener() {public void onClick(View v) {
			switcher.showPrevious();
			showEnemyPosition(switcher, enemyName, section.getEnemies().size());
		}});
		next.setOnClickListener(new OnClickListener() {public void onClick(View v) {
			switcher.showNext();
			showEnemyPosition(switcher, enemyName, section.getEnemies().size());
		}});
		showEnemyPosition(switcher, enemyName, section.getEnemies().size());
		
		if (section.isHasLuck()) {
			if (section.isLuckDefeatEnemies()) {
				section.setEnemiesAlreadyKilled(true);
			}
			LinearLayout log = (LinearLayout) view.findViewById(R.id.battle_log);
			NoBattleLogAdapter noBattleAdapter = new NoBattleLogAdapter(view.getContext());
			log.addView(noBattleAdapter.getView(log));
			if (fighting) {
				displayContinueButton();
			}
		}

	}

	private void showEnemyPosition(ViewSwitcher switcher, TextView enemyName, int total) {
		Enemy enemy = (Enemy) switcher.getCurrentView().getTag();
		enemyName.setText( enemy.getIndex() +"/"+total);
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

		attr = (TextView) view.findViewById(R.id.playground_curr_attack);
		attr.setText(String.valueOf(_character.getCurrentStats().getAttack()));
		changeAttributeColor(view.getContext(), attr, _character.getStats()
				.getAttack(), _character.getCurrentStats().getAttack());

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

	class NoBattleLogAdapter {
		Context context;

		public NoBattleLogAdapter(Context context) {
			this.context = context;
		}

		public View getView(ViewGroup parent) {
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
		private List<LinearLayoutRefreshable> views = new ArrayList<LinearLayoutRefreshable>();
		public BattleLogAdapter(Context context, View view) {
			this.context = context;
			masterView = view;
		}

		public View create(final Enemy enemy, final ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View rowView = inflater.inflate(R.layout.battle_log_layout, parent, false);
			final LinearLayoutRefreshable changeableView = new LinearLayoutRefreshable(context){
				public void refresh() {
					final View startFight = rowView.findViewById(R.id.fight);
					if (enemy.isDefeated() || _character.isDefeated() || _character.isFighting() || section.isHasLuck()) {
						startFight.setVisibility(View.GONE);
					} else {
						final FightingLog log = new FightingLog(BattleLogAdapter.this, enemy, this);
						startFight.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								_character.setFighting(true);
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
				}
			};
			changeableView.addView(rowView);
			changeableView.setTag(enemy);
			changeableView.refresh();
			views.add(changeableView);
			TextView v = (TextView) rowView.findViewById(R.id.enemy_name);
			v.setText(enemy.getName());
			return changeableView;
		}
		public List<LinearLayoutRefreshable> getViews() {
			return views;
		}
	}

	abstract class LinearLayoutRefreshable extends LinearLayout {

		public LinearLayoutRefreshable(Context context) {
			super(context);
		}
		
		public abstract void refresh();
		
	}
	
	class FightingLog implements AttackCallback, Runnable {
		private LinearLayout log;
		private BattleLogAdapter adapter;
		private LinearLayoutRefreshable container;
		private Enemy enemy;
		public FightingLog(BattleLogAdapter ad, Enemy enemy, LinearLayoutRefreshable container) {
			super();
			this.adapter = ad;
			this.enemy = enemy;
			this.log = (LinearLayout) ad.masterView
					.findViewById(R.id.battle_log);
			this.container = container;
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
					text += context.getResources().getString(R.string.you_have_luck);
				} else {
					text += context.getResources().getString(R.string.enemy_has_luck);
					color = R.color.debuf_color;
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
				if(resultCombat.isCritical()) {
					text += " ("+ resultCombat.getMultiplyAsText() + ") ";
				}
			}
			text += " ("+context.getResources().getString(resultCombat.getEnemyName()) + ")";
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
			refresh();			
		}
		
		private void refresh() {
			for(LinearLayoutRefreshable layout: this.adapter.views) {
				layout.refresh();
			}
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
