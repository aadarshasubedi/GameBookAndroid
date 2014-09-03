package com.nex.gamebook.playground;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nex.gamebook.MainScreenActivity;
import com.nex.gamebook.R;
import com.nex.gamebook.ScoreActivity;
import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Stats;
import com.nex.gamebook.game.Story;
import com.nex.gamebook.game.StorySection;
import com.nex.gamebook.game.StorySectionOption;
import com.nex.gamebook.game.Bonus.BonusState;
import com.nex.gamebook.util.GameBookUtils;

public class PlaygroundStoryView extends AbstractFragment implements BattleLogCallback {
	public PlaygroundStoryView(Context context) {
		super(context);
	}

	private boolean showOptions = false;
	public Player _character;
	private LinearLayout log;
	public View create(ViewGroup container) {
		View view = getPlayground().getLayoutInflater().inflate(R.layout.fragment_playground_story, container, false);
		log = (LinearLayout) view.findViewById(R.id.log_layout);
		setShowOptions(true);
		_character = getPlayground().getCharacter();
		this.prepareView(view);
		view.setTag(this);
		return view;
	}

	private void prepareView(View view) {
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.section_story);
		TextView tw = (TextView) layout.findViewById(R.id.story_view);
		Story story = _character.getStory();
		StorySection currentSection = story.getSection(_character.getPosition());
		if (currentSection.isFighting()) {
			startFight(view.getContext(), _character.getCurrentSection());
			return;
		}
		if (currentSection.isHasLuck()) {
			if(!currentSection.isAlreadyHasLuck())
			if(currentSection.isLuckDefeatEnemies()) {
				_character.addExperience(this, currentSection.getExperienceByEnemies(_character.getLevel()));
			} else {
				_character.addExperience(this, currentSection.getExperienceByEnemiesWhenLuck(_character.getLevel()));					
			}
			currentSection.setAlreadyHasLuck(true);
			tw.setText(currentSection.getLuckText() + " " + getContext().getString(R.string.fight_aspect_luck));
		} else if (currentSection.isEnemiesAlreadyKilled()) {
			tw.setText(currentSection.getEnemiesDefeatedText());
		} else if (currentSection.isVisited()) {
			tw.setText(currentSection.getAlreadyVisitedText());
		} else {
			tw.setText(currentSection.getText());
		}
		if(currentSection.isXpGiver()) {
			_character.addExperience(this, currentSection.getExperience(_character.getLevel()));
		}
		if (currentSection.isLoseSection()) {
			displayEndGameButton(view.getContext(), view.findViewById(R.id.playground_story), R.string.endGame_lose);
		} else if (currentSection.isWinSection()) {
			displayEndGameButton(view.getContext(), view.findViewById(R.id.playground_story), R.string.endGame_win);
		} else {
			if (!currentSection.getEnemies().isEmpty()) {
				prepareFightSection(view.getContext(), layout, currentSection);
			} else if (!currentSection.getBonuses().isEmpty()) {
				if(!currentSection.isBonusesAlreadyGained())
				prepareBonusSection(view.getContext(), view, currentSection, currentSection.getBonuses());
				currentSection.setBonusesAlreadyGained(true);
			}
			if (_character.isDefeated()) {
				showGameOver(view.getContext(), view.findViewById(R.id.playground_story), currentSection);
			} else if (isShowOptions()) {
				prepareChooseSection(view.getContext(), layout, currentSection);
			}
		}
		if(currentSection.getEnemies().isEmpty() || !currentSection.isEnemiesAlreadyKilled())
		if(currentSection.isResetAttributes()) {
			resetAttributes(view, 0, R.string.reset_all);
		} else if(currentSection.isResetPositiveAttributes()) {
			resetAttributes(view, 1, R.string.lose_gear);
		} else if(currentSection.isResetNegativeAttributes()) {
			resetAttributes(view, 2, R.string.resting);
		}
	}

	private void resetAttributes(View view, int mod, int text) {
		Stats resetedOverflowedStats = _character.resetStats(mod);
		showResetedStats(getContext(), view, resetedOverflowedStats, R.color.reset, text);
	}
	
	private void prepareBonusSection(Context context, View view, StorySection section, List<Bonus> bonuses) {
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.bonuses);
		for (Bonus bonus : bonuses) {
			view.findViewById(R.id.modification).setVisibility(View.VISIBLE);
			bonus.setAlreadyGained(true);
			int realValue =_character.addBonus(bonus);
			String marker = "";
			if(realValue>0)
				marker = "+";
			TextView opt = new TextView(context);
			opt.setTextAppearance(context, R.style.number);
			String suffix = " ";
			if (bonus.isBase()) {
				suffix += context.getString(R.string.base_attr_inc);
				opt.setTextColor(context.getResources().getColor(R.color.base));
			} else if (!bonus.isPermanent()) {
				suffix += context.getString(R.string.special_skill_type_temp);
				opt.setTextColor(context.getResources().getColor(R.color.temporal));
			} else if (bonus.isCondition()) {
				suffix += context.getString(R.string.reset_condition);
				opt.setTextColor(context.getResources().getColor(R.color.condition));
			} else if (bonus.getCoeff() > 0) {
				opt.setTextColor(context.getResources().getColor(R.color.positive));
			} else {
				opt.setTextColor(context.getResources().getColor(R.color.negative));
			}
			String s = getContext().getResources().getString(bonus.getText()).toLowerCase();
			opt.setText(marker + realValue + " " + s + suffix);
			layout.addView(opt);
		}
		getPlayground().getCharacterFragment().showCurrentValues();
	}

	private void prepareFightSection(Context context, LinearLayout layout, StorySection section) {
		if (section.isEnemiesAlreadyKilled() || section.isHasLuck()) {
			prepareAfterFight(context, layout, section);
		} else {
			prepareBeforeFight(context, layout, section);
		}
	}

	private void prepareBeforeFight(final Context context, LinearLayout layout, final StorySection section) {
		if(!section.isBonusesBeforeFightAlreadyGained())
		prepareBonusSection(context, layout, section, section.getBonuses(BonusState.BEFORE_FIGHT));
		section.setBonusesBeforeFightAlreadyGained(true);
		prepareBonusSection(context, layout, section, section.getTemporalBonuses());
		_character.holdCurrentStatsToTemporal();
		if (!_character.isDefeated()) {
			final Button fight = (Button) layout.findViewById(R.id.fight_buton);
			fight.setVisibility(View.VISIBLE);
			fight.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					fight.setVisibility(View.GONE);
					startFight(context, section);
				}
			});
			setShowOptions(false);
		}
	}
	private void removeTemporalBonuses(Context context, View view) {
		Stats releasedStats = _character.releaseTemporalStats();
		showResetedStats(context, view, releasedStats, R.color.temporal, R.string.cancel_temp);
	}
	private void showResetedStats(Context context, View view, Stats releasedStats, int color, int text) {
		if (releasedStats == null)
			return;
		boolean show = false;
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.bonuses);
		int value = releasedStats.getHealth();
		if (value != 0) {
			layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_health, context, color, text));
			show = true;
		}
		value = releasedStats.getAttack();
		if (value != 0) {
			layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_attack, context, color, text));
			show = true;
		}
		value = releasedStats.getDefense();
		if (value != 0) {
			layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_defense, context, color, text));
			show = true;
		}
		value = releasedStats.getSkill();
		if (value != 0) {
			layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_skill, context, color, text));
			show = true;
		}
		value = releasedStats.getLuck();
		if (value != 0) {
			layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_luck, context, color, text));
			show = true;
		}
		value = releasedStats.getDamage();
		if (value != 0) {
			layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_baseDmg, context, color, text));
			show = true;
		}
		value = releasedStats.getSkillpower();
		if (value != 0) {
			layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_skill_power, context, color, text));
			show = true;
		}
		if (show) {
			view.findViewById(R.id.modification).setVisibility(View.VISIBLE);
			layout.setVisibility(View.VISIBLE);
		}
	}

	private TextView getViewForReleasedTemporalAttribute(int value, int attrName, Context context, int color, int additionaltext) {
		String prefix = "";
		if (value > 0) {
			prefix = "+";
		}
		TextView opt = new TextView(context);
		opt.setTextAppearance(context, R.style.number);
		opt.setTextColor(context.getResources().getColor(color));
		String s = getContext().getResources().getString(attrName).toLowerCase();
		opt.setText(prefix + value + " " + s + " " + context.getString(additionaltext));
		return opt;
	}

	private void startFight(Context context, StorySection section) {
		if (!section.isEnemiesAlreadyKilled() && section.isLuckPossible()) {
			section.tryApplyLuckForBattle(_character);
		}
		_character.save();
		if (section.isHasLuck()) {
			if (section.isLuckDefeatEnemies()) {
				section.setEnemiesAlreadyKilled(true);
			}
			PlaygroundActivity activity = getPlayground();
			activity.changeToStory(section);
		} else {
			
			PlaygroundActivity activity = getPlayground();
			activity.changeToBattle(section);
		}
	}

	private void prepareAfterFight(Context context, LinearLayout layout, StorySection section) {
		removeTemporalBonuses(context, layout);
		prepareBonusSection(context, layout, section, _character.getConditions());
		_character.getConditions().clear();
		_character.getConditions().clear();
		if(!section.isBonusesAfterFightAlreadyGained())
		prepareBonusSection(context, layout, section, section.getBonuses(BonusState.AFTER_FIGHT));
		section.setBonusesAfterFightAlreadyGained(true);
		
		setShowOptions(true);
	}

	private void prepareChooseSection(Context context, LinearLayout layout, StorySection section) {
		TextView optionslabel = new TextView(context);
		optionslabel.setTextAppearance(context, R.style.title);
		optionslabel.setText(R.string.options);
		layout.addView(optionslabel);
		for (StorySectionOption option : section.getOptions()) {
			String text = option.getText();
			_character.setCanShowOption(option);
			if (!option.isDisplayed()) {
				continue;
			}
			// if(option.getSection() == section.getUnreturnableSection() &&
			// alertUnreturnableOptions) {
			// text += " " +
			// context.getResources().getString(R.string.option_cant_return);
			// }
			if (option.isBothAspects()) {
				text += " " + context.getResources().getString(R.string.fight_aspect_luck) + " " + context.getResources().getString(R.string.fight_aspect_skill);
			} else if (option.isLuckAspect()) {
				text += " " + context.getResources().getString(R.string.fight_aspect_luck);
			} else if (option.getSkill() > 0) {
				text += " " + context.getResources().getString(R.string.fight_aspect_skill);
			}
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout optionslayout = (LinearLayout) inflater.inflate(R.layout.option_layout, layout, false);
			TextView enabled = (TextView) optionslayout.findViewById(R.id.enabled);
			TextView disabled = (TextView) optionslayout.findViewById(R.id.disabled);
			if (option.isDisabled()) {
				disabled.setText(text = text + " " + context.getResources().getString(R.string.option_disabled));
				enabled.setVisibility(View.GONE);
			} else {
				enabled.setText(text);
				disabled.setVisibility(View.GONE);
				enabled.setOnClickListener(new OptionClickListener(option, section));
			}
			layout.addView(optionslayout);
			option.setAlreadyDisplayed(true);
		}
	}

	private void showGameOver(Context context, View parent, StorySection section) {
		TextView v = (TextView) parent.findViewById(R.id.gameOver_view);
		v.setText(section.getGameOverText() + " " + context.getResources().getString(R.string.fight_aspect_health));
		v.setVisibility(View.VISIBLE);
		displayEndGameButton(context, parent, R.string.endGame_lose);

	}

	private void displayEndGameButton(Context ctx, View parent, final int text) {
		Button button = (Button) parent.findViewById(R.id.endGame_button);
		button.setText(text);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getPlayground().finish();
				if (text == R.string.endGame_win) {
					try {
						GameBookUtils.getInstance().removeSavedGame(_character);
						String fileName = GameBookUtils.getInstance().saveScore(_character);
						Intent intent = new Intent(v.getContext(), ScoreActivity.class);
						Bundle b = new Bundle();
						b.putString("fileName", fileName);
						intent.putExtras(b);
						v.getContext().startActivity(intent);
						return;
					} catch (Exception e) {
						Log.e("GameBookScore", "failed score saving", e);
					}
				}
				getPlayground().finish();
				Intent intent = new Intent(v.getContext(), MainScreenActivity.class);
				v.getContext().startActivity(intent);
			}
		});
		button.setVisibility(View.VISIBLE);
	}

	class OptionClickListener implements OnClickListener {
		StorySectionOption option;
		StorySection section;

		public OptionClickListener(StorySectionOption option, StorySection section) {
			super();
			this.option = option;
			this.section = section;
		}

		@Override
		public void onClick(View v) {
			this.section.setCompleted(true);
			if (!this.section.isVisited()) {
				_character.addVisitedSection();
			}
			this.section.setVisited(true);
			this.section.canTryLuck();
			_character.addSection();
			option.setDisabled(option.isDisableWhenSelected());
			int sectionId = _character.getPosition();
			PlaygroundStoryView.this._character.setPosition(option.getSection());
			StorySection nextSection = _character.getStory().getSection(_character.getPosition());
			if (option.isDisabled()) {
				nextSection.setUnreturnableSection(sectionId);
			}
			PlaygroundStoryView.this.refresh();
		}
	}

	public void refresh() {
		getPlayground().changeToStory(_character.getCurrentSection());
	}

	public boolean isShowOptions() {
		return showOptions;
	}

	public void setShowOptions(boolean val) {
		showOptions = val;
	}

	@Override
	public void logAttack(ResultCombat resultCombat) {
		
	}

	@Override
	public void divide(int turn) {
		
	}

	@Override
	public void fightEnd(long xp) {
		
	}

	@Override
	public void logLevelIncreased() {
		addResultToLog(getContext().getString(R.string.level_increased, _character.getLevel()), getContext(), R.color.temporal);
	}
	private void addResultToLog(String text, Context context, int color) {
		log.setVisibility(View.VISIBLE);
		TextView battleText = new TextView(context);
		battleText.setText(text);
		battleText.setTextAppearance(context, R.style.attribute);
		battleText.setTextColor(context.getResources().getColor(color));
		log.addView(battleText);
		
	}

	@Override
	public void logExperience(long xp) {
		addResultToLog(getContext().getString(R.string.gain_experience, xp), getContext(), R.color.condition);
	}
}
