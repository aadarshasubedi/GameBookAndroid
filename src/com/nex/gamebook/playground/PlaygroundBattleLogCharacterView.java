package com.nex.gamebook.playground;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nex.gamebook.MainScreenActivity;
import com.nex.gamebook.R;
import com.nex.gamebook.ViewFlipListener;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.combat.CombatProcess;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.Enemy;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.SpecialSkillsMap;
import com.nex.gamebook.game.StorySection;
import com.nex.gamebook.util.SkillInfoDialog;

public class PlaygroundBattleLogCharacterView extends AbstractFragment {

	public PlaygroundBattleLogCharacterView(Context context) {
		super(context);
	}

	private Player _character;
	private StorySection section;
	private Button resultButton;
	private ViewFlipper switcher;
	private View masterView;

	public View create(ViewGroup container) {
		masterView = getPlayground().getLayoutInflater().inflate(R.layout.fragment_playground_character, container, false);
		switcher = (ViewFlipper) masterView.findViewById(R.id.viewSwitcher1);
		_character = getPlayground().getCharacter();
		_character.createActiveSkills();
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
		// LinearLayout log = (LinearLayout) view.findViewById(R.id.battle_log);
		// log.removeAllViews();
		displayButtons();
		BattleLogAdapter adapter = new BattleLogAdapter(view.getContext());
		int totalEnemies = section.getEnemies().size();
		for (int i = 0; i < totalEnemies; i++) {
			Enemy enemy = section.getEnemies().get(i);
			enemy.setIndex(i + 1);
			View enemyView = adapter.create(enemy, switcher);
			TextView v = (TextView) enemyView.findViewById(R.id.enemy_name);
			String name = enemy.getName();
			name += " " + getContext().getResources().getString(enemy.getEnemyLevel().getCode()) + " - " + getContext().getString(R.string.level) + " " + enemy.getLevel();
			v.setText(name);
			switcher.addView(enemyView);
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

		final TextView actualAttrs = (TextView) view.findViewById(R.id.actualStats);
		final TextView baseStats = (TextView) view.findViewById(R.id.base_stats);
		actualAttrs.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showStats(masterView, _character.getCurrentStats(), _character.getStats(), true);
				decoreClickableTextView(getContext(), baseStats, R.string.base_stats);
				actualAttrs.setText(R.string.actual_stats);
			}
		});
		baseStats.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showStats(masterView, _character.getStats(), _character.getStats(), false);
				decoreClickableTextView(getContext(), actualAttrs, R.string.actual_stats);
				baseStats.setText(R.string.base_stats);
			}
		});
		actualAttrs.callOnClick();
		// showSkill((TextView) view.findViewById(R.id.skill_name), _character);
		showAvailableSkills();
		TextView level = (TextView) view.findViewById(R.id.level);
		TextView exp = (TextView) view.findViewById(R.id.experience);
		level.setText(String.valueOf(_character.getLevel()));
		exp.setText(String.valueOf(_character.getXpToLevelPercentage()));
		masterView.findViewById(R.id.tableLayout1).invalidate();
	}

	public void showAvailableSkills() {
		SkillsSpinner skills = (SkillsSpinner) masterView.findViewById(R.id.skills);
		List<String> availableSkills = new ArrayList<>();
		availableSkills.add(null);
		availableSkills.addAll(_character.getAvailableSkills());
		skills.setAdapter(new SkillsAdapter(getContext(), _character, availableSkills, skills));
		skills.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		String selected = _character.getSkillName();
		if (selected != null) {
			int index = availableSkills.indexOf(selected);
			skills.setSelection(index);
		}
	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			String i = (String) parent.getItemAtPosition(pos);
			_character.setSkillName(i);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SkillsAdapter extends ArrayAdapter<String> {
		SkillsSpinner owner;
		List<String> keys;
		Context context;
		Character applicator;

		public SkillsAdapter(Context context, Character applicator, List<String> keys, SkillsSpinner owner) {
			super(context, R.layout.list_item, keys);
			this.context = context;
			this.keys = keys;
			this.applicator = applicator;
			this.owner = owner;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
//			String key = keys.get(position);
//			if (key == null) {
//				return new LinearLayout(getContext());
//			}
			return getCustomViewView(position, convertView, parent, R.layout.spinner_dropdown_item, true);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomViewView(position, convertView, parent, R.layout.spinner_single_item_skill, false);
		}

		public View getCustomViewView(final int position, final View convertView, final ViewGroup parent, int inflate, boolean isDropdown) {
			final String key = keys.get(position);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(inflate, parent, false);
			TextView name = (TextView) rowView.findViewById(R.id.name);
			if (key != null) {
				SpecialSkill skill = applicator.getSpecialSkill(key);
				rowView.setTag(key);
				name.setText(context.getString(skill.getNameId()));
				
				if(skill.canUse()) {
					if(R.layout.spinner_dropdown_item==inflate)
						name.setTextColor(getContext().getResources().getColor(R.color.button_color));
					else
						name.setTextColor(getContext().getResources().getColor(R.color.condition));
				} else {
					name.setTextColor(getContext().getResources().getColor(R.color.negative));	
				}
			} else {
				if(!isDropdown)
					name.setText(R.string.skills);
				else 
					name.setText(R.string.no_skill);
			}
			if (inflate == R.layout.spinner_dropdown_item) {
				rowView.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View arg0) {
						if(key==null) return false;
						SkillInfoDialog dialog = new SkillInfoDialog(context, applicator, SpecialSkillsMap.get(key));
						dialog.show();
						return false;
					}
				});
				rowView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						owner.onDetachedFromWindow();
						if (applicator instanceof Player)
							owner.setSelection(position);
					}
				});
			} else {
				decoreClickableTextView(getContext(), name, String.valueOf(name.getText()));
			}
			return rowView;
		}

	}

	public void showSkill(TextView view, final Character applicator) {
		decoreClickableTextView(getContext(), view, applicator.getSpecialSkill().getNameId());
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SkillInfoDialog dialog = new SkillInfoDialog(getContext(), applicator);
				dialog.show();
			}
		});
	}

	public void fight(StorySection section) {
		this.section = section;
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
			LinearLayout log = (LinearLayout) rowView.findViewById(R.id.e_battle_log);
			final FightingLog fl = new FightingLog(BattleLogAdapter.this, enemy, log);
			final LinearLayoutRefreshable changeableView = new LinearLayoutRefreshable(context) {
				public void refresh() {
					final View startFight = rowView.findViewById(R.id.fight);
					final ScrollView sc = (ScrollView) rowView.findViewById(R.id.e_battleLogScrollView);
					boolean isFighting = enemy.isDefeated() || _character.isDefeated() || _character.isFighting() || section.isHasLuck();
					startFight.setVisibility(View.VISIBLE);
					if (isFighting && !enemy.equals(_character.getCurrentEnemy())) {
						startFight.setVisibility(View.GONE);
					} else {
						startFight.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (fl.isButtonPressed() || enemy.isDefeated()) {
									return;
								}
								_character.setFighting(true);
								_character.setCurrentEnemy(enemy);
								fl.run();
							}
						});
					}
					sc.setVisibility(View.VISIBLE);
					sc.post(new Runnable() {
						@Override
						public void run() {
							sc.fullScroll(View.FOCUS_DOWN);
						}
					});
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
					attack.setText(String.valueOf(enemy.getCurrentStats().getDefensePercentage()));
					attack = (TextView) rowView.findViewById(R.id.enemy_skill_power);
					attack.setText(String.valueOf(enemy.getCurrentStats().getSkillpower()));
					final SkillsSpinner skills = (SkillsSpinner) rowView.findViewById(R.id.enemy_skills);

					List<String> s = new ArrayList<>();
					s.add(null);
					s.addAll(enemy.getAvailableSkills());
					if (!s.isEmpty()) {
						skills.setAdapter(new SkillsAdapter(getContext(), enemy, s, skills));
						skills.setOnItemSelectedListener(new CustomOnItemSelectedListener());
					} else {
						skills.setVisibility(View.GONE);
					}
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

	@SuppressLint("NewApi")
	class FightingLog implements BattleLogCallback, Runnable {
		private LinearLayout log;
		private BattleLogAdapter adapter;
		private boolean buttonPressed = false;
		CombatProcess combat;

		public FightingLog(BattleLogAdapter ad, Enemy enemy, LinearLayout log) {
			super();
			this.adapter = ad;
			this.log = log;
			combat = new CombatProcess(enemy);
		}

		@Override
		public Player getCharacter() {
			return _character;
		}

		@Override
		public void logLevelIncreased() {
			addResultToLog(log, getContext().getString(R.string.level_increased, _character.getLevel()), getContext(), R.color.temporal);
		}

		@Override
		public void divide(int turn) {
			String text = turn + "." + getContext().getString(R.string.turn);
			TextView textView = new TextView(getContext());
			textView.setText(text);
			textView.setTextAppearance(getContext(), R.style.attribute);
			log.addView(textView);
		}

		@SuppressLint("NewApi")
		@Override
		public void logAttack(ResultCombat resultCombat) {
			SpecialSkill spec = resultCombat.getSpecialAttack();
			if (spec == null) {
				logNormalAttack(resultCombat);
			} else {
				logSpecialAttack(resultCombat);
			}
		}

		private void logSpecialAttack(ResultCombat resultCombat) {
			Context context = adapter.context;
			SpecialSkill skill = resultCombat.getSpecialAttack();
			if (CharacterType.ENEMY.equals(resultCombat.getType())) {
				String text = resultCombat.getEnemyName();
				text += " " + context.getString(R.string.enemy_use);
				text += " " + context.getString(skill.getNameId()).toLowerCase();
				text += " " + context.getString(R.string.for_word);
				text += " " + resultCombat.getDamage();
				text += " " + context.getString(skill.getTextId()).toLowerCase();
				addResultToLog(log, text, context, R.color.negative);
			} else {
				String text = context.getString(R.string.you_use);
				text += " " + context.getString(skill.getNameId()).toLowerCase();
				text += " " + context.getString(R.string.for_word);
				text += " " + resultCombat.getDamage();
				text += " " + context.getString(skill.getTextId()).toLowerCase();
				addResultToLog(log, text, context, R.color.positive);
			}
		}

		private void logNormalAttack(ResultCombat resultCombat) {
			int color = R.color.positive;
			String text = "";
			Context context = adapter.context;

			if (resultCombat.isLuck()) {
				if (resultCombat.getType().equals(CharacterType.ENEMY)) {
					text += context.getResources().getString(R.string.you_have_luck);
				} else {
					text += context.getResources().getString(R.string.enemy_has_luck);
					color = R.color.negative;
				}
			} else {
				if (resultCombat.getType().equals(CharacterType.ENEMY)) {
					text += context.getResources().getString(R.string.you_take);
					color = R.color.negative;
				} else {
					text += context.getResources().getString(R.string.you_cause);
				}
				text += " " + String.valueOf(resultCombat.getDamage()) + " ";
				if (resultCombat.isCritical()) {
					text += context.getResources().getString(R.string.critical_chance) + " ";
				}
				text += context.getResources().getString(R.string.damage);
				if (resultCombat.isCritical()) {
					text += " (" + resultCombat.getMultiplyAsText() + ") ";
				}
			}
			addResultToLog(log, text, context, color);
		}

		@Override
		public void logExperience(long xp) {
			addResultToLog(log, getContext().getString(R.string.gain_experience, xp), getContext(), R.color.condition);
		}

		@Override
		public void fightEnd(long xp) {
			_character.addExperience(this, xp);
			_character.setFighting(false);
			_character.setCurrentEnemy(null);
			displayButtons();
			refresh();
		}

		private void refresh() {
			showCurrentValues();
			for (LinearLayoutRefreshable layout : this.adapter.views) {
				layout.refresh();
			}
			_character.updateActiveSkills();
		}

		public boolean isButtonPressed() {
			return buttonPressed;
		}

		@Override
		public void run() {
			buttonPressed = true;
			combat.fight(this);
			buttonPressed = false;
			refresh();
		}

	}

	private void displayButtons() {
		if (_character.isDefeated()) {
			displayGameOverButton();
		} else if (section.isAllDefeated()) {
			_character.cleanActiveSkillsAfterBattleEnd();
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
			redirectToStoryBoard();
		}
	};
	OnClickListener gameoverListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), MainScreenActivity.class);
			v.getContext().startActivity(intent);
			getPlayground().finish();
		}
	};

	private void redirectToStoryBoard() {
		PlaygroundActivity activity = getPlayground();
		activity.changeToStory(section);
		section = null;
		resultButton.setVisibility(View.GONE);
	}

	public boolean isFighting() {
		return section.isFighting();
	}

	private void addResultToLog(LinearLayout log, String text, Context context, int color, int style) {
		TextView battleText = new TextView(context);
		battleText.setText(text);
		battleText.setTextAppearance(context, style);
		battleText.setTextColor(context.getResources().getColor(color));
		// LayoutParams params = new LayoutParams(new
		// LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// params.setMargins(0,0,0,0);
		// battleText.setLayoutParams(params);
		log.addView(battleText);
	}

	private void addResultToLog(LinearLayout log, String text, Context context, int color) {
		addResultToLog(log, text, context, color, R.style.attribute);

	}
}
