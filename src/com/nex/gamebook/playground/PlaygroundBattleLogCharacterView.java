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
import com.nex.gamebook.attack.special.ResultCombatText;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.combat.CombatProcess;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.Enemy;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Stats;
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
				showStats(masterView, _character, false);
				decoreClickableTextView(getContext(), baseStats, R.string.base_stats);
				actualAttrs.setText(R.string.actual_stats);
			}
		});
		baseStats.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showStats(masterView, _character, true);
				decoreClickableTextView(getContext(), actualAttrs, R.string.actual_stats);
				baseStats.setText(R.string.base_stats);
			}
		});
		actualAttrs.callOnClick();
		// showSkill((TextView) view.findViewById(R.id.skill_name), _character);
		showAvailableSkills();
		TextView level = (TextView) view.findViewById(R.id.level);
		com.nex.gamebook.playground.TextProgressBar progress = (com.nex.gamebook.playground.TextProgressBar) view.findViewById(R.id.experience_bar);
		changeProgressBarColor(progress, R.drawable.experience_bar_style);
		progress.setText(String.valueOf(_character.getExperience()));
		level.setText(String.valueOf(_character.getLevel()));
		progress.setProgress(_character.getXpToLevelPercentage());
		masterView.findViewById(R.id.tableLayout1).invalidate();
		TextView hots = (TextView) view.findViewById(R.id.player_hots);
		hots.setText(String.valueOf(_character.getHots()));
		TextView dots = (TextView) view.findViewById(R.id.player_dots);
		dots.setText(String.valueOf(_character.getDots()));
		TextView hotsLongest = (TextView) view.findViewById(R.id.player_longest_hot);
		hotsLongest.setText(String.valueOf(_character.getLongestHot()));
		TextView dotsLongest = (TextView) view.findViewById(R.id.player_longest_dot);
		dotsLongest.setText(String.valueOf(_character.getLongestDot()));

		TextView buffs = (TextView) view.findViewById(R.id.player_buffs);
		buffs.setText(String.valueOf(_character.getBuffs()));
		TextView debuffs = (TextView) view.findViewById(R.id.player_debuffs);
		debuffs.setText(String.valueOf(_character.getDebuffs()));
		TextView buffsLongest = (TextView) view.findViewById(R.id.player_longest_buff);
		buffsLongest.setText(String.valueOf(_character.getLongestBuff()));
		TextView debuffsLongest = (TextView) view.findViewById(R.id.player_longest_debuff);
		debuffsLongest.setText(String.valueOf(_character.getLongestDebuff()));

	}

	public void showAvailableSkills() {
		SkillsSpinner skills = (SkillsSpinner) masterView.findViewById(R.id.skills);
		List<SpecialSkill> availableSkills = new ArrayList<>();
		availableSkills.add(null);
		availableSkills.addAll(_character.getActiveSkills());
		skills.setAdapter(new SkillsAdapter(getContext(), _character, availableSkills, skills));
		skills.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		SpecialSkill selected = _character.getSelectedSkill();
		if (selected != null) {
			int index = availableSkills.indexOf(selected);
			skills.setSelection(index);
		}
	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			SpecialSkill i = (SpecialSkill) parent.getItemAtPosition(pos);
			_character.setSelectedSkill(i);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SkillsAdapter extends ArrayAdapter<SpecialSkill> {
		SkillsSpinner owner;
		List<SpecialSkill> keys;
		Context context;
		Character applicator;

		public SkillsAdapter(Context context, Character applicator, List<SpecialSkill> keys, SkillsSpinner owner) {
			super(context, R.layout.list_item, keys);
			this.context = context;
			this.keys = keys;
			this.applicator = applicator;
			this.owner = owner;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			// String key = keys.get(position);
			// if (key == null) {
			// return new LinearLayout(getContext());
			// }
			return getCustomViewView(position, convertView, parent, R.layout.spinner_dropdown_item, true);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomViewView(position, convertView, parent, R.layout.spinner_single_item_skill, false);
		}

		public View getCustomViewView(final int position, final View convertView, final ViewGroup parent, int inflate, boolean isDropdown) {
			final SpecialSkill skill = keys.get(position);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(inflate, parent, false);
			TextView name = (TextView) rowView.findViewById(R.id.name);
			if (skill != null) {
				rowView.setTag(skill);
				name.setText(skill.getName());
				if (skill.canUse() && applicator.isCanCastSkill()) {
					if (R.layout.spinner_dropdown_item == inflate)
						name.setTextColor(getContext().getResources().getColor(R.color.button_color));
					else
						name.setTextColor(getContext().getResources().getColor(R.color.condition));
				} else {
					name.setTextColor(getContext().getResources().getColor(R.color.negative));
				}
			} else {
				if (!isDropdown)
					name.setText(R.string.skills);
				else
					name.setText(R.string.no_skill);
			}
			if (inflate == R.layout.spinner_dropdown_item) {
				rowView.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View arg0) {
						if (skill == null)
							return false;
						SkillInfoDialog dialog = new SkillInfoDialog(context, applicator, skill);
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
		decoreClickableTextView(getContext(), view, applicator.getSelectedSkill().getName());
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
					Stats cs = enemy.getCurrentStats();
					Stats s = enemy.getStats();
					TextView attack = (TextView) rowView.findViewById(R.id.enemy_attr_attack);
					attack.setText(String.valueOf(cs.getAttack()));
					changeAttributeColor(getContext(), attack, s.getAttack(), cs.getAttack());
					attack = (TextView) rowView.findViewById(R.id.enemy_sel_attr_baseDmg_d);
					attack.setText(String.valueOf(cs.getDamage()));
					changeAttributeColor(getContext(), attack, s.getDamage(), cs.getDamage());
					attack = (TextView) rowView.findViewById(R.id.enemy_attr_luck);
					attack.setText(String.valueOf(enemy.getCurrentStats().getLuckPercentage()));
					changeAttributeColor(getContext(), attack, s.getLuck(), cs.getLuck());
					attack = (TextView) rowView.findViewById(R.id.enemy_attr_skill);
					attack.setText(String.valueOf(enemy.getCurrentStats().getSkill()));
					changeAttributeColor(getContext(), attack, s.getSkill(), cs.getSkill());
					attack = (TextView) rowView.findViewById(R.id.enemy_attr_defense);
					attack.setText(String.valueOf(enemy.getCurrentStats().getDefense()));
					changeAttributeColor(getContext(), attack, s.getDefense(), cs.getDefense());
					attack = (TextView) rowView.findViewById(R.id.enemy_critical);
					attack.setText(String.valueOf(enemy.getCurrentStats().getSkillPercentage()));
					attack = (TextView) rowView.findViewById(R.id.enemy_l_def_perc);
					attack.setText(String.valueOf(enemy.getCurrentStats().getDefensePercentage()));
					attack = (TextView) rowView.findViewById(R.id.enemy_skill_power);
					attack.setText(String.valueOf(enemy.getCurrentStats().getSkillpower()));
					changeAttributeColor(getContext(), attack, s.getSkillpower(), cs.getSkillpower());
					final SkillsSpinner skills = (SkillsSpinner) rowView.findViewById(R.id.enemy_skills);

					TextProgressBar bar = (TextProgressBar) rowView.findViewById(R.id.enemy_health_bar);
					int healthPercentage = (int) (((double) cs.getHealth() / (double) s.getHealth()) * 100);
					changeHealthProgressColor(bar, healthPercentage, enemy);
					bar.setProgress(healthPercentage);
					bar.setText(cs.getHealth() + "/" + s.getHealth());
					List<SpecialSkill> sk = new ArrayList<>();
					sk.add(null);
					sk.addAll(enemy.getActiveSkills());
					if (!sk.isEmpty()) {
						skills.setAdapter(new SkillsAdapter(getContext(), enemy, sk, skills));
						skills.setOnItemSelectedListener(new CustomOnItemSelectedListener());
					} else {
						skills.setVisibility(View.GONE);
					}
					TextView hots = (TextView) rowView.findViewById(R.id.enemy_hots);
					hots.setText(String.valueOf(enemy.getHots()));
					TextView dots = (TextView) rowView.findViewById(R.id.enemy_dots);
					dots.setText(String.valueOf(enemy.getDots()));

					TextView hotsLongest = (TextView) rowView.findViewById(R.id.enemy_longest_hot);
					hotsLongest.setText(String.valueOf(enemy.getLongestHot()));
					TextView dotsLongest = (TextView) rowView.findViewById(R.id.enemy_longest_dot);
					dotsLongest.setText(String.valueOf(enemy.getLongestDot()));

					TextView buffs = (TextView) rowView.findViewById(R.id.enemy_buffs);
					buffs.setText(String.valueOf(enemy.getBuffs()));
					TextView debuffs = (TextView) rowView.findViewById(R.id.enemy_debuffs);
					debuffs.setText(String.valueOf(enemy.getDebuffs()));
					TextView buffsLongest = (TextView) rowView.findViewById(R.id.enemy_longest_buff);
					buffsLongest.setText(String.valueOf(enemy.getLongestBuff()));
					TextView debuffsLongest = (TextView) rowView.findViewById(R.id.enemy_longest_debuff);
					debuffsLongest.setText(String.valueOf(enemy.getLongestDebuff()));

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
			ResultCombatText text = resultCombat.getSpecialAttack().getCombatTextDispatcher().getLogAttack(context, resultCombat);
			addResultToLog(log, text.getText(), context, text.getColor());
		}

		private void logNormalAttack(ResultCombat resultCombat) {
			int color = R.color.positive;
			String text = "";
			Context context = adapter.context;
			if (resultCombat.isCannotCast()) {
				if(resultCombat.getType().equals(CharacterType.ENEMY)) {
					text = context.getString(R.string.enemy_cannot_cast);	
				} else {
					text = context.getString(R.string.you_cannot_cast);
					color = R.color.negative;
				}
			} else if (resultCombat.isCannotAttack()) {
				if(resultCombat.getType().equals(CharacterType.ENEMY)) {
					text = context.getString(R.string.enemy_cannot_attack);	
				} else {
					text = context.getString(R.string.you_cannot_attack);
					color = R.color.negative;
				}
			} else if (resultCombat.isLuck()) {
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
