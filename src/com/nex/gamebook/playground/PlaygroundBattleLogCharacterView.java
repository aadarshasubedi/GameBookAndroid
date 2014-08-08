package com.nex.gamebook.playground;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nex.gamebook.MainScreenActivity;
import com.nex.gamebook.R;
import com.nex.gamebook.ViewFlipListener;
import com.nex.gamebook.entity.CharacterType;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.story.section.StorySection;

public class PlaygroundBattleLogCharacterView extends AbstractFragment {

	public PlaygroundBattleLogCharacterView(Context context) {
		super(context);
	}

	private Player _character;
	private StorySection section;
	private Button resultButton;
	private boolean newBattle = false;
	private ViewFlipper switcher;
	private View masterView;
	public View create(ViewGroup container) {
		masterView = getPlayground().getLayoutInflater().inflate(R.layout.fragment_playground_character, container, false);
		switcher = (ViewFlipper) masterView.findViewById(R.id.viewSwitcher1);
		_character = getPlayground().getCharacter();
		showCurrentValues();
		resultButton = (Button) masterView.findViewById(R.id.result_button);
		resultButton.setVisibility(View.GONE);
		if (section != null) {
			prepareBattleLog(masterView);
		}
		
		masterView.setTag(this);
		
		return masterView;
	}

	public ViewFlipper getSwitcher() {
		return switcher;
	}

	private void prepareBattleLog(View view) {
		LinearLayout log = (LinearLayout) view.findViewById(R.id.battle_log);
		log.removeAllViews();
		if(newBattle) {
			if (!section.isEnemiesAlreadyKilled()) {
				section.tryApplyLuckForBattle(_character);
			}
		}
		displayButtons();
		BattleLogAdapter adapter = new BattleLogAdapter(view.getContext());
		int totalEnemies = section.getEnemies().size();
		for(int i = 0; i < totalEnemies; i++) {
			Enemy enemy = section.getEnemies().get(i);
			enemy.setIndex(i+1);
			View enemyView = adapter.create(enemy, switcher);
			TextView v = (TextView) enemyView.findViewById(R.id.enemy_name);
			v.setText(enemy.getName());
			switcher.addView(enemyView);
		}
		
		if (section.isHasLuck() && newBattle) {
			if (section.isLuckDefeatEnemies()) {
				section.setEnemiesAlreadyKilled(true);
			}
			NoBattleLogAdapter noBattleAdapter = new NoBattleLogAdapter(view.getContext());
			log.addView(noBattleAdapter.getView(log));
			if (isFighting()) {
				displayContinueButton();
			}
		}
	}

	public ViewFlipListener createListener(ImageView left, ImageView right, final TextView title) {
		ViewFlipListener listener = new ViewFlipListener(left, right, switcher, title) {
			
			@Override
			public void viewChanged(View currentView) {
				showEnemyPosition(switcher, title, section.getEnemies().size());
				
			}
			@Override
			public Context getContext() {
				return PlaygroundBattleLogCharacterView.this.getContext();
			}
		};
		listener.select(0);
		showEnemyPosition(switcher, title, section.getEnemies().size());
		return listener;
	}
	
	private void showEnemyPosition(ViewFlipper switcher, TextView enemyName, int total) {
		Enemy enemy = (Enemy) switcher.getCurrentView().getTag();
		int index = enemy.getIndex();
		enemyName.setText(index + "/" + total);
	}
	
	public void showCurrentValues() {
		View view = masterView;
		TextView attr = (TextView) view.findViewById(R.id.sel_attr_health);
		attr.setText(String.valueOf(_character.getCurrentStats().getHealth()));
		attr.invalidate();
		changeAttributeColor(view.getContext(), attr, _character.getStats()
				.getHealth(), _character.getCurrentStats().getHealth());

		attr = (TextView) view.findViewById(R.id.sel_attr_luck);
		attr.setText(String.valueOf(_character.getCurrentStats().getLuckPercentage()));
		changeAttributeColor(view.getContext(), attr, _character.getStats()
				.getLuck(), _character.getCurrentStats().getLuck());
		changeAttributeColor(view.getContext(), (TextView) view.findViewById(R.id.luck_perc), _character.getStats()
				.getLuck(), _character.getCurrentStats().getLuck());
		
		attr = (TextView) view.findViewById(R.id.sel_attr_defense);
		attr.setText(String.valueOf(_character.getCurrentStats().getDefense()));
		changeAttributeColor(view.getContext(), attr, _character.getStats()
				.getDefense(), _character.getCurrentStats().getDefense());

		attr = (TextView) view.findViewById(R.id.sel_attr_skill);
		attr.setText(String.valueOf(_character.getCurrentStats().getSkill()));
		changeAttributeColor(view.getContext(), attr, _character.getStats()
				.getSkill(), _character.getCurrentStats().getSkill());

		attr = (TextView) view.findViewById(R.id.sel_attr_attack);
		attr.setText(String.valueOf(_character.getCurrentStats().getAttack()));
		changeAttributeColor(view.getContext(), attr, _character.getStats()
				.getAttack(), _character.getCurrentStats().getAttack());

		attr = (TextView) view.findViewById(R.id.critical);
		attr.setText(String.valueOf(_character.getCurrentStats().getSkillPercentage()));
		
		attr = (TextView) view.findViewById(R.id.sel_l_def_perc);
		attr.setText(String.valueOf(_character.getStats().getDefensePercentage()));
		
		masterView.findViewById(R.id.tableLayout1).invalidate();
	}

	@SuppressLint("ResourceAsColor")
	private void changeAttributeColor(Context ctx, TextView text,
			int defaultValue, int currentvalue) {
		if (currentvalue > defaultValue) {
			text.setTextColor(ctx.getResources().getColor(R.color.positive));
		} else if (currentvalue < defaultValue) {
			text.setTextColor(ctx.getResources().getColor(R.color.negative));
		} else {
			text.setTextColor(ctx.getResources().getColor(
					R.color.number_color));
		}
	}

	public void fight(StorySection section) {
		this.section = section;
	}

	class NoBattleLogAdapter {
		Context context;

		public NoBattleLogAdapter(Context context) {
			this.context = context;
		}

		public View getView(ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.fragment_battle_log_layout_nobattle, parent, false);
			TextView result = (TextView) rowView.findViewById(R.id.noBattleText);
			result.setTextColor(context.getResources().getColor(R.color.positive));
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
		private List<LinearLayoutRefreshable> views = new ArrayList<LinearLayoutRefreshable>();
		public BattleLogAdapter(Context context) {
			this.context = context;
		}

		public View create(final Enemy enemy, final ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View rowView = inflater.inflate(R.layout.fragment_enemy_layout, parent, false);
			final LinearLayoutRefreshable changeableView = new LinearLayoutRefreshable(context) {
				public void refresh() {
					final View startFight = rowView.findViewById(R.id.fight);
					if (enemy.isDefeated() || _character.isDefeated() || _character.isFighting() || section.isHasLuck()) {
						startFight.setVisibility(View.GONE);
						
					} else {
						final FightingLog log = new FightingLog(BattleLogAdapter.this, enemy);
						startFight.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								_character.setFighting(true);
								v.post(log);
							}
						});
					}

					if(enemy.isDefeated()) {
						rowView.findViewById(R.id.dead_label).setVisibility(View.VISIBLE);
					}
					TextView attack = (TextView) rowView.findViewById(R.id.enemy_attr_attack);
					attack.setText(String.valueOf(enemy.getCurrentStats().getAttack()));
					attack = (TextView) rowView.findViewById(R.id.enemy_attr_health);
					attack.setText(String.valueOf(enemy.getCurrentStats().getHealth()));
					attack = (TextView) rowView.findViewById(R.id.enemy_attr_luck);
					attack.setText(String.valueOf(enemy.getCurrentStats().getLuckPercentage()));
					attack = (TextView) rowView.findViewById(R.id.enemy_attr_skill);
					attack.setText(String.valueOf(enemy.getCurrentStats().getSkill()));
					attack = (TextView) rowView.findViewById(R.id.enemy_attr_defense);
					attack.setText(String.valueOf(enemy.getCurrentStats().getDefense()));
					attack = (TextView) rowView.findViewById(R.id.enemy_critical);
					attack.setText(String.valueOf(enemy.getCurrentStats().getSkillPercentage()));
					attack = (TextView) rowView.findViewById(R.id.enemy_l_def_perc);
					attack.setText(String.valueOf(enemy.getStats().getDefensePercentage()));
				}
			};
			changeableView.addView(rowView);
			changeableView.setTag(enemy);
			changeableView.refresh();
			views.add(changeableView);
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
		private Enemy enemy;
		public FightingLog(BattleLogAdapter ad, Enemy enemy) {
			super();
			this.adapter = ad;
			this.enemy = enemy;
			this.log = (LinearLayout) masterView
					.findViewById(R.id.battle_log);
		}

		@Override
		public Player getCharacter() {
			return _character;
		}

		@SuppressLint("NewApi")
		@Override
		public void attackCallBack(ResultCombat resultCombat) {
			int color = R.color.positive;
			String text = "\n";
			Context context = adapter.context;
			if(resultCombat.isLuck()) {
				if(resultCombat.getType().equals(CharacterType.ENEMY)) {
					text += context.getResources().getString(R.string.you_have_luck);
				} else {
					text += context.getResources().getString(R.string.enemy_has_luck);
					color = R.color.negative;
				}
			} else {
				if(resultCombat.getType().equals(CharacterType.ENEMY)) {
					text += context.getResources().getString(R.string.you_take);
					color = R.color.negative;
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
			text += " ("+resultCombat.getEnemyName() + ")";
			TextView battleText = new TextView(context);
			battleText.setText(text);
			battleText.setTextColor(context.getResources().getColor(color));
			LayoutParams params = new LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			params.setMargins(0,0,0,0);
			battleText.setLayoutParams(params);
			log.addView(battleText);
			log.requestLayout();
//			getPlayground().getBattleLog().add(battleText);
		}

		@Override
		public void fightEnd() {
			_character.setFighting(false);
			displayButtons();
			refresh();			
		}
		
		private void refresh() {
			showCurrentValues();
			for(LinearLayoutRefreshable layout: this.adapter.views) {
				layout.refresh();
			}
		}
		
		@Override
		public void run() {
			enemy.fight(this);
		}
		
	}

	private void displayButtons() {
		if (_character.isDefeated()) {
			displayGameOverButton();
		} else if (section.isAllDefeated()) {
			section.setEnemiesAlreadyKilled(true);
			displayContinueButton();
		}
	}
	
	private void displayGameOverButton() {
		resultButton.setText(R.string.endGame_lose);
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
			PlaygroundActivity activity = getPlayground();
			activity.changeToStory();
			section = null;
			resultButton.setVisibility(View.GONE);
		}
	};
	OnClickListener gameoverListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(),
					MainScreenActivity.class);
			v.getContext().startActivity(intent);
		}
	};

	public boolean isFighting() {
		return getPlayground().isFighting();
	}
	public void setNewBattle(boolean newBattle) {
		this.newBattle = newBattle;
	}
}
