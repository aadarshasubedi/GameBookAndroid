package com.nex.gamebook.playground;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nex.gamebook.MainScreenActivity;
import com.nex.gamebook.R;
import com.nex.gamebook.ViewFlipListener;
import com.nex.gamebook.combat.CombatProcess;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.Enemy;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.SkillMap;
import com.nex.gamebook.game.Stats;
import com.nex.gamebook.game.StorySection;
import com.nex.gamebook.skills.ResultCombatText;
import com.nex.gamebook.skills.active.OvertimeSkill;
import com.nex.gamebook.skills.active.Skill;
import com.nex.gamebook.util.DialogBuilder;
import com.nex.gamebook.util.PassiveSkillInfoDialogAnSelection;
import com.nex.gamebook.util.PassiveSkillInfoDialogAnSelection.DismissCallBack;
import com.nex.gamebook.util.SkillInfoDialog;
import com.nex.gamebook.util.Statistics.StatisticItem;

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
		boolean battle = section != null;
		int inflate = R.layout.fragment_playground_character;
		masterView = getPlayground().getLayoutInflater().inflate(inflate, container, false);
		switcher = (ViewFlipper) masterView.findViewById(R.id.viewSwitcher1);
		_character = getPlayground().getCharacter();
		_character.createActiveSkills();
		showCurrentValues();
		resultButton = (Button) masterView.findViewById(R.id.result_button);
		resultButton.setVisibility(View.GONE);
		LinearLayout layout = (LinearLayout) masterView.findViewById(R.id.statistics_space);
		
		if (battle) {
			layout.setVisibility(View.GONE);
			prepareBattleLog(masterView);
		} else {
			layout.setVisibility(View.VISIBLE);
			showStatistics(layout);
		}

		masterView.setTag(this);

		return masterView;
	}

	private void showStatistics(final LinearLayout view) {
		LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		final View rowView = inflater.inflate(R.layout.fragment_statistics, view, false);
		view.addView(rowView);
		ListView log = (ListView) rowView.findViewById(R.id.statistics_list);
		final List<StatisticItem> items = _character.getStatistics().asList();
		
		log.setAdapter(new ArrayAdapter<StatisticItem>(view.getContext(), R.layout.statistics_item, items) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View rowView = inflater.inflate(R.layout.statistics_item, parent, false);
				StatisticItem item = items.get(position);
				TextView text = (TextView) rowView.findViewById(R.id.stat_title);
				text.setText(item.getId());
				text = (TextView) rowView.findViewById(R.id.stat_summary);
				text.setText(String.valueOf(item.getValue()));
				return rowView;
			}
		});
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
//		String name = enemy.getName() + " " + getContext().getResources().getString(enemy.getEnemyLevel().getCode()) + " - " + getContext().getString(R.string.level) + " " + enemy.getLevel();
		// enemyName.setText(name + " ("+index + "/" + total+")");
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
		if(section!=null) {
			actualAttrs.setVisibility(View.GONE);
			baseStats.setVisibility(View.GONE);
			view.findViewById(R.id.attrs_title).setVisibility(View.GONE);
		} else {
			actualAttrs.setVisibility(View.VISIBLE);
			baseStats.setVisibility(View.VISIBLE);
			view.findViewById(R.id.attrs_title).setVisibility(View.VISIBLE);
		}
		actualAttrs.callOnClick();
		// showSkill((TextView) view.findViewById(R.id.skill_name), _character);
		showAvailableSkills();
		showPassiveSkills();
		TextView level = (TextView) view.findViewById(R.id.level);
		com.nex.gamebook.playground.TextProgressBar progress = (com.nex.gamebook.playground.TextProgressBar) view.findViewById(R.id.experience_bar);
		changeProgressBarColor(progress, R.drawable.experience_bar_style);
		progress.setText(String.valueOf(_character.getExperience()));
		level.setText(String.valueOf(_character.getLevel()));
		progress.setProgress(_character.getXpToLevelPercentage());
		masterView.findViewById(R.id.tableLayout1).invalidate();
		
		TextView hots = (TextView) view.findViewById(R.id.player_total_hots);
		hots.setText(String.valueOf(getTotalHots(_character)));
		TextView dots = (TextView) view.findViewById(R.id.player_total_dots);
		dots.setText(String.valueOf(getTotalDots(_character)));
		
		
		TextView buffs = (TextView) view.findViewById(R.id.player_buffs);
		buffs.setText(String.valueOf(_character.getBuffs()));
		TextView debuffs = (TextView) view.findViewById(R.id.player_debuffs);
		debuffs.setText(String.valueOf(_character.getDebuffs()));
		TextView buffsLongest = (TextView) view.findViewById(R.id.player_longest_buff);
		buffsLongest.setText(String.valueOf(_character.getLongestBuff()));
		TextView debuffsLongest = (TextView) view.findViewById(R.id.player_longest_debuff);
		debuffsLongest.setText(String.valueOf(_character.getLongestDebuff()));

	}
	
	
	private int getTotalHots(Character c) {
		int value = 0;
		for (OvertimeSkill a : c.getOvertimeSkills()) {
			if (!a.getTargetSkill().isCondition())
				value += a.getDamage() * a.getRemainsTurns();
		}
		return value;
	}
	private int getTotalDots(Character c) {
		int value = 0;
		for (OvertimeSkill a : c.getOvertimeSkills()) {
			if (a.getTargetSkill().isCondition())
				value += a.getReducedDamage(c) * a.getRemainsTurns();
		}
		return value;
	}
	public String RESET_SKILLS = "resetSkills";
	public void showPassiveSkills() {
		SkillsSpinner skills = (SkillsSpinner) masterView.findViewById(R.id.passive_skills);
		int skillPoints = _character.getSkillPoints();
		List<String> passiveSkills = new ArrayList<>();
		if(_character.getLearnedPassiveSkills().size()>0 && _character.getResetSkillsAvailable()>0)
		passiveSkills.add(RESET_SKILLS);
		List<String> availableSkills = new ArrayList<String>();
		if (skillPoints > 0) {
			availableSkills = SkillMap.getUnlearnedSkills(_character);
		} else {
			availableSkills = new ArrayList<>(_character.getLearnedPassiveSkills());
		}
		Collections.sort(availableSkills);
		passiveSkills.addAll(availableSkills);
		// if(availableSkills.isEmpty()) {
		// availableSkills.add("none");
		// }
		skills.setAdapter(new PassiveSkillsAdapter(getContext(), passiveSkills, skills));
	}

	public void showAvailableSkills() {
		SkillsSpinner skills = (SkillsSpinner) masterView.findViewById(R.id.skills);
		List<Skill> availableSkills = new ArrayList<>();
		availableSkills.add(null);
		List<Skill> tosort = new ArrayList<Skill>(_character.getActiveSkills());
		Collections.sort(tosort, new Comparator<Skill>() {
			@Override
			public int compare(Skill lhs, Skill rhs) {
				return Integer.valueOf(lhs.getProperties().getLevelRequired()).compareTo(rhs.getProperties().getLevelRequired());
			}
		});
		availableSkills.addAll(tosort);
		skills.setAdapter(new SkillsAdapter(getContext(), _character, availableSkills, skills));
//		skills.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		Skill selected = _character.getSelectedSkill();
		if (selected != null) {
			int index = availableSkills.indexOf(selected);
			skills.setSelection(index);
		} else {
			skills.setSelection(0);
		}
	}

//	public class CustomOnItemSelectedListener implements OnItemSelectedListener {
//		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//			Skill i = (Skill) parent.getItemAtPosition(pos);
//			_character.setSelectedSkill(i);
//		}
//
//		@Override
//		public void onNothingSelected(AdapterView<?> arg0) {
//		}
//	}

	class PassiveSkillsAdapter extends ArrayAdapter<String> {
		private List<String> skills;
		SkillsSpinner owner;
		Context context;

		public PassiveSkillsAdapter(Context context, List<String> keys, SkillsSpinner owner) {
			super(context, R.layout.list_item, keys);
			this.skills = keys;
			this.context = context;
			this.owner = owner;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			final String skill = skills.get(position);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);
			TextView name = (TextView) rowView.findViewById(R.id.name);
			String text = "";
			final boolean isReset = skill.equals(RESET_SKILLS);
			if(isReset) {
				text = getContext().getString(R.string.reset_skills, _character.getResetSkillsAvailable());
			} else {
				text = SkillMap.getPassive(skill).getName(_character.getStory().getProperties());
			}
			name.setText(text);
			rowView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// owner.onDetachedFromWindow();
					if(isReset) {
						final DialogBuilder s = new DialogBuilder(getContext()).setText(R.string.reset_skills_prompt)
						.setNegativeButton(R.string.no, null);
						s.setPositiveButton(R.string.yes, new OnClickListener() {
							@Override
							public void onClick(View v) {
								owner.onDetachedFromWindow();
								_character.resetPassiveSkills();
								showCurrentValues();
								s.dismiss();
							}
						}).show();
					} else {
						PassiveSkillInfoDialogAnSelection dialog = new PassiveSkillInfoDialogAnSelection(context, _character, skill);
						dialog.show(new DismissCallBack() {
							@Override
							public void dismiss() {
								owner.onDetachedFromWindow();
								showCurrentValues();
							}
						});
					}
				}
			});
			return rowView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.spinner_single_item_skill, parent, false);
			TextView name = (TextView) rowView.findViewById(R.id.name);
			name.setTextColor(getContext().getResources().getColor(R.color.passive_skill_triggered));
			name.setText(context.getString(R.string.select_passive_skill, _character.getSkillPoints()));
			decoreClickableTextView(getContext(), name, String.valueOf(name.getText()));
			return rowView;
		}
	}

	class SkillsAdapter extends ArrayAdapter<Skill> {
		SkillsSpinner owner;
		List<Skill> keys;
		Context context;
		Character applicator;

		public SkillsAdapter(Context context, Character applicator, List<Skill> keys, SkillsSpinner owner) {
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
			final Skill skill = keys.get(position);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(inflate, parent, false);
			TextView name = (TextView) rowView.findViewById(R.id.name);
			if (skill != null) {
				rowView.setTag(skill);
				name.setText(skill.getRemainings() + "x " + skill.getName());
				if (skill.canUse() && applicator.isCanCastSkill()) {
					if (R.layout.spinner_dropdown_item == inflate)
						name.setTextColor(getContext().getResources().getColor(R.color.button_color));
					else
						name.setTextColor(getContext().getResources().getColor(R.color.condition));
				} else if (!applicator.isCanCastSkill()) {
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
				rowView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if(skill==null) {
							_character.setSelectedSkill(null);
							owner.onDetachedFromWindow();
							owner.setSelection(position);
							return ;
						}
						OnClickListener listener = null;
						if (applicator instanceof Player && section!=null) {
							owner.setSelection(position);
							listener = new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									owner.onDetachedFromWindow();
									owner.setSelection(position);
									_character.setSelectedSkill(skill);
								}
							};
						}
						SkillInfoDialog dialog = new SkillInfoDialog(context, applicator, skill, listener);
						dialog.show();
					}
					
				});
			} else {
				decoreClickableTextView(getContext(), name, String.valueOf(name.getText()));
			}
			return rowView;
		}

	}
	
	public void setSection(StorySection section) {
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
					changeHealthProgressColor(bar, enemy);
					List<Skill> sk = new ArrayList<>();
					sk.add(null);
					sk.addAll(enemy.getActiveSkills());
					if (!sk.isEmpty()) {
						skills.setAdapter(new SkillsAdapter(getContext(), enemy, sk, skills));
//						skills.setOnItemSelectedListener(new CustomOnItemSelectedListener());
					} else {
						skills.setVisibility(View.GONE);
					}
					TextView hots = (TextView) rowView.findViewById(R.id.enemy_total_hots);
					hots.setText(String.valueOf(getTotalHots(enemy)));
					TextView dots = (TextView) rowView.findViewById(R.id.enemy_total_dots);
					dots.setText(String.valueOf(getTotalDots(enemy)));

					TextView buffs = (TextView) rowView.findViewById(R.id.enemy_buffs);
					buffs.setText(String.valueOf(enemy.getBuffs()));
					TextView debuffs = (TextView) rowView.findViewById(R.id.enemy_debuffs);
					debuffs.setText(String.valueOf(enemy.getDebuffs()));
					TextView buffsLongest = (TextView) rowView.findViewById(R.id.enemy_longest_buff);
					buffsLongest.setText(String.valueOf(enemy.getLongestBuff()));
					TextView debuffsLongest = (TextView) rowView.findViewById(R.id.enemy_longest_debuff);
					debuffsLongest.setText(String.valueOf(enemy.getLongestDebuff()));
					if(enemy.getSummon()!=null) {
						rowView.findViewById(R.id.enemy_summon).setVisibility(View.VISIBLE);
						TextProgressBar summonHealth = (TextProgressBar) rowView.findViewById(R.id.summon_health_enemy);
						changeHealthProgressColor(summonHealth, enemy.getSummon());
					} else {
						rowView.findViewById(R.id.enemy_summon).setVisibility(View.GONE);
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
		private Enemy enemy;

		public FightingLog(BattleLogAdapter ad, Enemy enemy, LinearLayout log) {
			super();
			this.adapter = ad;
			this.log = log;
			this.enemy = enemy;
			combat = new CombatProcess(enemy, getEnemies());
		}

		@Override
		public Context getContext() {
			return PlaygroundBattleLogCharacterView.this.getContext();
		}

		@Override
		public List<Enemy> getEnemies() {
			List<Enemy> others = new ArrayList<Enemy>(section.getEnemies());
			others.remove(enemy);
			return others;
		}

		@Override
		public Player getCharacter() {
			return _character;
		}

		@Override
		public void logLevelIncreased() {
			addResultToLog(log, getContext().getString(R.string.level_increased, _character.getLevel()), getContext(), R.color.temporal);
		}

		public void logText(String text, int color) {
			addResultToLog(log, text, getContext(), color);
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
			Skill spec = resultCombat.getSpecialAttack();
			if (spec == null) {
				logNormalAttack(resultCombat);
			} else {
				logSpecialAttack(resultCombat);
			}
		}

		private void logSpecialAttack(ResultCombat resultCombat) {
			Context context = adapter.context;
			ResultCombatText text = resultCombat.getSpecialAttack().getCombatTextDispatcher().getLogAttack(context, resultCombat);
			String t = text.getText();
			if (resultCombat.isCritical()) {
				t += " " + adapter.context.getString(R.string.critical_strike);
			}
			addResultToLog(log, t, context, text.getColor());
		}

		private void logNormalAttack(ResultCombat resultCombat) {
			if(resultCombat.isStrikeIsTaunted()) return;
			int color = R.color.positive;
			String text = "";
			Context context = adapter.context;
			if (resultCombat.isCannotCast()) {
				if (resultCombat.getType().equals(CharacterType.ENEMY)) {
					text = context.getString(R.string.enemy_cannot_cast);
				} else {
					text = context.getString(R.string.you_cannot_cast);
					color = R.color.negative;
				}
			} else if (resultCombat.isCannotAttack()) {
				if (resultCombat.getType().equals(CharacterType.ENEMY)) {
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
		public void logPassiveSkillsTriggered(String text) {
			addResultToLog(log, text, getContext(), R.color.passive_skill_triggered);
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

		@Override
		public void logSummonDie(String summonName) {
			addResultToLog(log, getContext().getString(R.string.summon_die, summonName), getContext(), R.color.condition);
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
		StorySection s = section;
		section = null;
		activity.changeToStory(s);
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
