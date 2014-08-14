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
import com.nex.gamebook.entity.Bonus;
import com.nex.gamebook.entity.Bonus.BonusState;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Stats;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.entity.StorySection;
import com.nex.gamebook.entity.StorySectionOption;
import com.nex.gamebook.entity.io.GameBookUtils;

public class PlaygroundStoryView extends AbstractFragment {
	public PlaygroundStoryView(Context context) {
		super(context);
	}
	private boolean showOptions = false;
	public Player _character;

	
	public View create(ViewGroup container) {
		View view = getPlayground().getLayoutInflater().inflate(R.layout.fragment_playground_story, container, false);
		setShowOptions(true);
		_character = getPlayground().getCharacter();
		this.prepareView(view);
		if(getPlayground().isFighting()) {
			startFight(view.getContext(), _character.getCurrentSection());
		}
		view.setTag(this);
		return view;
	}
	
	private void prepareView(View view) {
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.section_story);
		TextView tw = (TextView) layout.findViewById(R.id.story_view);
		Story story = _character.getStory();
		StorySection currentSection = story.getSection(_character.getPosition());
		if(currentSection.isEnemiesAlreadyKilled()) {
			tw.setText(currentSection.getEnemiesDefeatedText());
		} else if(currentSection.isHasLuck()) {
			tw.setText(currentSection.getLuckText());
		} else if(currentSection.isVisited()) {
			tw.setText(currentSection.getAlreadyVisitedText());	
		} else {
			tw.setText(currentSection.getText());	
		}
		if(currentSection.isLoseSection()) {
			displayEndGameButton(view.getContext(), view.findViewById(R.id.playground_story), R.string.endGame_lose);
		} else if(currentSection.isWinSection()) {
			displayEndGameButton(view.getContext(), view.findViewById(R.id.playground_story), R.string.endGame_win);
		} else {
		if(!currentSection.getEnemies().isEmpty()) {
			prepareFightSection(view.getContext(), layout, currentSection);
		} else if(!currentSection.getBonuses().isEmpty()) {
			prepareBonusSection(view.getContext(), view, currentSection, currentSection.getBonuses());
			currentSection.setBonusesAlreadyGained(true);
		}
			if(_character.isDefeated()) {
				showGameOver(view.getContext(), view.findViewById(R.id.playground_story), currentSection);
			}else if(isShowOptions()) {
				prepareChooseSection(view.getContext(), layout, currentSection);
			}
		}
	}
	
	private void prepareBonusSection(Context context, View view , StorySection section, List<Bonus> bonuses) {
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.bonuses);
		if(!bonuses.isEmpty() && !section.isBonusesAlreadyGained()) {
			view.findViewById(R.id.modification).setVisibility(View.VISIBLE);
		}
		for(Bonus bonus: bonuses) {
			if(bonus.isAlreadyGained() && section.isVisited() 
			|| (!bonus.isPermanent() && !BonusState.NORMAL.equals(bonus.getState()))) continue;
			bonus.setAlreadyGained(true);
			int realValue = bonus.getValue();
			if(!section.isBonusesAlreadyGained()) {
				realValue = _character.addBonus(bonus);
			}
			String suffix = " ";
			if(!bonus.isPermanent()) {
				suffix += context.getResources().getString(R.string.temporal);
			}
			String marker = "+";
			TextView opt = new TextView(context);
			opt.setTextAppearance(context, R.style.number);
			if(bonus.getCoeff() > 0) {
				opt.setTextColor(context.getResources().getColor(R.color.positive));
			} else {
				marker = "-";
				opt.setTextColor(context.getResources().getColor(R.color.negative));
			}
			String s = getContext().getResources().getString(bonus.getText()).toLowerCase();
			opt.setText(marker + realValue + " " + s + suffix);
			layout.addView(opt);
		}
		getPlayground().getCharacterFragment().showCurrentValues();
	}
	
	private void prepareFightSection(Context context, LinearLayout layout, StorySection section) {
		if(section.isEnemiesAlreadyKilled() || section.isHasLuck()) {
			prepareAfterFight(context, layout, section);
		} else {
			prepareBeforeFight(context, layout, section);
		}
	}
	
	private void prepareBeforeFight(final Context context, LinearLayout layout, final StorySection section) {
		prepareBonusSection(context, layout, section, section.getBonuses(BonusState.BEFORE_FIGHT));
		prepareBonusSection(context, layout, section, section.getTemporalBonuses());
		_character.holdCurrentStatsToTemporal();
		if(!_character.isDefeated()) {
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
		if(releasedStats==null) return;
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.bonuses);
		layout.setVisibility(View.VISIBLE);
		int value = Math.abs(releasedStats.getHealth());
		if(value>0)
		layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_health, context));
		value = Math.abs(releasedStats.getAttack());
		if(value>0)
		layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_attack, context));
		value = Math.abs(releasedStats.getDefense());
		if(value>0)
		layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_defense, context));
		value = Math.abs(releasedStats.getSkill());
		if(value>0)
		layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_skill, context));
		value = Math.abs(releasedStats.getLuck());
		if(value>0)
		layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_luck, context));
		value = Math.abs(releasedStats.getDamage());
		if(value>0)
		layout.addView(getViewForReleasedTemporalAttribute(value, R.string.attr_baseDmg, context));
	}
	
	
	private TextView getViewForReleasedTemporalAttribute(int value, int attrName, Context context) {
		TextView opt = new TextView(context);
		opt.setTextAppearance(context, R.style.number);
		opt.setTextColor(context.getResources().getColor(R.color.negative));
		String s = getContext().getResources().getString(attrName).toLowerCase();
		opt.setText(value + " " + s);
		return opt;
	}
	
	private void startFight(Context context, StorySection section) {
		section.setBonusesAlreadyGained(false);
		PlaygroundActivity activity = getPlayground();
		activity.changeToBattle(section);
	}
	
	private void prepareAfterFight(Context context, LinearLayout layout, StorySection section) {
		prepareBonusSection(context, layout, section, section.getBonuses(BonusState.AFTER_FIGHT));
		removeTemporalBonuses(context, layout);
		section.setBonusesAlreadyGained(true);
		setShowOptions(true);
	}
	
	private void prepareChooseSection(Context context, LinearLayout layout, StorySection section) {
		TextView optionslabel = new TextView(context);
		optionslabel.setTextAppearance(context, R.style.title);
		optionslabel.setText(R.string.options);
		layout.addView(optionslabel);
		for(StorySectionOption option: section.getOptions()) {
			String text = option.getText();
			_character.setCanShowOption(option);
			if(!option.isDisplayed()) {
				continue;
			}
//			if(option.getSection() == section.getUnreturnableSection() && alertUnreturnableOptions) {
//				text += " " + context.getResources().getString(R.string.option_cant_return);
//			}
			if(option.isBothAspects()) {
				text += " " + context.getResources().getString(R.string.fight_aspect_luck) + " " + context.getResources().getString(R.string.fight_aspect_skill);
			} else if(option.isLuckAspect()) {
				text += " " + context.getResources().getString(R.string.fight_aspect_luck);
			} else if(option.getSkill() > 0) {
				text += " " + context.getResources().getString(R.string.fight_aspect_skill);
			}
			LayoutInflater inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout optionslayout = (LinearLayout) inflater.inflate(R.layout.option_layout, layout, false);
			TextView enabled = (TextView) optionslayout.findViewById(R.id.enabled);
			TextView disabled = (TextView) optionslayout.findViewById(R.id.disabled);
			if(option.isDisabled()) {
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
				if(text == R.string.endGame_win) {
					try {
						GameBookUtils.getInstance().removeSavedGame(_character);
						String fileName = GameBookUtils.getInstance().saveCharacterForScore(_character);
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
			if(!this.section.isVisited()) {
				_character.addVisitedSection();
			}
			this.section.setVisited(true);
			this.section.setHasLuck(false);
			_character.addSection();
			option.setDisabled(option.isDisableWhenSelected());
			int sectionId = _character.getPosition();
			PlaygroundStoryView.this._character.setPosition(option.getSection());
			StorySection nextSection = _character.getStory().getSection(_character.getPosition());
			if(option.isDisabled()) {
				nextSection.setUnreturnableSection(sectionId);
			}
			try {
				GameBookUtils.getInstance().saveCharacter(PlaygroundStoryView.this._character);
			} catch (Exception e) {
				Log.e("GameBookSaver", "", e);
			}
			PlaygroundStoryView.this.refresh();
		}
	}
	
	public void refresh() {
		getPlayground().changeToStory();
	}

	public boolean isShowOptions() {
		return showOptions;
	}
	public void setShowOptions(boolean val) {
		showOptions = val;
	}
	
}
