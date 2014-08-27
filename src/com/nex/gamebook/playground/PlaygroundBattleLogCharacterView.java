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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nex.gamebook.MainScreenActivity;
import com.nex.gamebook.R;
import com.nex.gamebook.ViewFlipListener;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.combat.CombatProcess;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.CharacterType;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.entity.SpecialSkillsMap;
import com.nex.gamebook.entity.StorySection;
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
		showCurrentValues();
		_character.createActiveSkills();
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
//		LinearLayout log = (LinearLayout) view.findViewById(R.id.battle_log);
//		log.removeAllViews();
		displayButtons();
		BattleLogAdapter adapter = new BattleLogAdapter(view.getContext());
		int totalEnemies = section.getEnemies().size();
		for(int i = 0; i < totalEnemies; i++) {
			Enemy enemy = section.getEnemies().get(i);
			enemy.setIndex(i+1);
			View enemyView = adapter.create(enemy, switcher);
			TextView v = (TextView) enemyView.findViewById(R.id.enemy_name);
			String name = enemy.getName();
			name += " " + getContext().getResources().getString(enemy.getEnemyLevel().getCode()) + " - " + getContext().
					getString(R.string.level) + " " + enemy.getLevel();
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
//		showSkill((TextView) view.findViewById(R.id.skill_name), _character);
		showAvailableSkills();
		TextView level = (TextView) view.findViewById(R.id.level);
		TextView exp = (TextView) view.findViewById(R.id.experience);
		level.setText(String.valueOf(_character.getLevel()));
		exp.setText(String.valueOf(_character.getXpToLevelPercentage()));
		masterView.findViewById(R.id.tableLayout1).invalidate();
	}

	public void showAvailableSkills() {
		Spinner skills = (Spinner) masterView.findViewById(R.id.skills);
		
		skills.setAdapter(new SkillsAdapter(getContext(), _character.getAvailableSkills()));
		skills.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		SpecialSkill selected = _character.getSpecialSkill();
		if(selected!=null)
		skills.setSelection(_character.getAvailableSkills().indexOf(SpecialSkillsMap.getSkillId(selected.getClass())));
		TextView info = (TextView)  masterView.findViewById(R.id.skill_info);
		info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SkillInfoDialog d = new SkillInfoDialog(getContext(), _character);
				d.show();
			}
		});
		decoreClickableTextView(getContext(), info, R.string.skill_info);
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
		List<String> keys;
		Context context;

		public SkillsAdapter(Context context, List<String> keys) {
			super(context, R.layout.list_item, keys);
			this.context = context;
			this.keys = keys;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			String key = keys.get(position);
			if(key == null) {
				return new LinearLayout(getContext());
			}
			return getCustomViewView(position, convertView, parent, R.layout.spinner_dropdown_item);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomViewView(position, convertView, parent,
					R.layout.spinner_single_item);
		}

		public View getCustomViewView(int position, View convertView, ViewGroup parent, int inflate) {
			String key = keys.get(position);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(inflate, parent, false);
			TextView name = (TextView) rowView.findViewById(R.id.name);
			if(key != null) {
				SpecialSkill skill = SpecialSkillsMap.get(key);
				rowView.setTag(key);
				name.setText(context.getString(skill.getNameId()));
			} else {
				name.setText(R.string.select_skill);
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
			final LinearLayoutRefreshable changeableView = new LinearLayoutRefreshable(context) {
				public void refresh() {
					final View startFight = rowView.findViewById(R.id.fight);
					if (enemy.isDefeated() || _character.isDefeated() || _character.isFighting() || section.isHasLuck()) {
						startFight.setVisibility(View.GONE);
						
					} else {
						final FightingLog log = new FightingLog(BattleLogAdapter.this, enemy,
								(LinearLayout) rowView.findViewById(R.id.e_battle_log));
						startFight.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								_character.setFighting(true);
								parent.post(log);
							}
						});
					}

					if(enemy.isDefeated()) {
						final ScrollView sc = (ScrollView) rowView.findViewById(R.id.e_battleLogScrollView);
						sc.post(new Runnable() {            
						    @Override
						    public void run() {
						    	sc.fullScroll(View.FOCUS_DOWN);              
						    }
						});
						sc.setVisibility(View.VISIBLE);
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
					attack.setText(String.valueOf(enemy.getCurrentStats().getDefensePercentage()));
					LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.enemy_special_skill_row);
					if(enemy.getSpecialSkill() == null) {
						layout.setVisibility(View.GONE);
					} else {
						layout.setVisibility(View.VISIBLE);
						TextView skillname = (TextView) layout.findViewById(R.id.enemy_skill_name);
						showSkill(skillname, enemy);
						TextView skillPower = (TextView) layout.findViewById(R.id.enemy_skill_power);
						skillPower.setText(String.valueOf(enemy.getCurrentStats().getSkillpower()));
						
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
		private Enemy enemy;
		public FightingLog(BattleLogAdapter ad, Enemy enemy, LinearLayout log) {
			super();
			this.adapter = ad;
			this.enemy = enemy;
			this.log = log;
		}

		@Override
		public Player getCharacter() {
			return _character;
		}

		@Override
		public void logLevelIncreased() {
			addResultToLog(getContext().getString(R.string.level_increased, _character.getLevel()), getContext(), R.color.temporal);
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
			if(spec==null) {
				logNormalAttack(resultCombat);
			} else {
				logSpecialAttack(resultCombat);
			}
		}
		
		private void logSpecialAttack(ResultCombat resultCombat) {
			Context context = adapter.context;
			SpecialSkill skill = resultCombat.getSpecialAttack();
			if(CharacterType.ENEMY.equals(resultCombat.getType())) {
				String text = resultCombat.getEnemyName();
				text += " " + context.getString(R.string.enemy_use);
				text += " " + context.getString(skill.getNameId()).toLowerCase();
				text += " " + context.getString(R.string.for_word);
				text += " " + resultCombat.getDamage();
				text += " " + context.getString(skill.getTextId()).toLowerCase();
				addResultToLog(text, context, R.color.negative);
			} else {
				String text = context.getString(R.string.you_use);
				text += " " + context.getString(skill.getNameId()).toLowerCase();
				text += " " + context.getString(R.string.for_word);
				text += " " + resultCombat.getDamage();
				text += " " + context.getString(skill.getTextId()).toLowerCase();
				addResultToLog(text, context, R.color.positive);
			}
		}
		
		private void logNormalAttack(ResultCombat resultCombat) {
			int color = R.color.positive;
			String text = "";
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
			addResultToLog(text, context, color);
		}
		
		private void addResultToLog(String text, Context context, int color) {
			TextView battleText = new TextView(context);
			battleText.setText(text);
			battleText.setTextAppearance(context, R.style.attribute);
			battleText.setTextColor(context.getResources().getColor(color));
//			LayoutParams params = new LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//			params.setMargins(0,0,0,0);
//			battleText.setLayoutParams(params);
			log.addView(battleText);
			
		}
		@Override
		public void logExperience(long xp) {
			addResultToLog(getContext().getString(R.string.gain_experience, xp), getContext(), R.color.condition);
		}
		@Override
		public void fightEnd(long xp) {
			_character.addExperience(this, xp);
			_character.setFighting(false);
			displayButtons();
			refresh();			
		}
		
		private void refresh() {
			showCurrentValues();
			for(LinearLayoutRefreshable layout: this.adapter.views) {
				layout.refresh();
			}
			_character.updateActiveSkills();
		}
		
		@Override
		public void run() {
			CombatProcess combat = new CombatProcess(enemy);
			combat.fight(this);
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
		activity.changeToStory();
		section = null;
		resultButton.setVisibility(View.GONE);
		_character.cleanActiveSkillsAfterBattleEnd();
	}
	
	public boolean isFighting() {
		return getPlayground().isFighting();
	}
}
