package com.nex.gamebook.playground;

import java.util.List;

import android.app.FragmentTransaction;
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
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.entity.character.Character;
import com.nex.gamebook.entity.io.IOGameOperation;
import com.nex.gamebook.story.Bonus;
import com.nex.gamebook.story.Bonus.BonusState;
import com.nex.gamebook.story.StorySectionOption;
import com.nex.gamebook.story.section.StorySection;

public class PlaygroundStoryTab extends AbstractFragment {
	private Character _character;
	private boolean tabClick;
	private boolean showOptions = true;
	private boolean alertUnreturnableOptions = false;
	private PlaygroundActivity activity;
	public PlaygroundStoryTab(Character ch, PlaygroundActivity activity) {
		this._character = ch;
		this.activity = activity;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_playground_story, container, false);
		this.prepareView(view);
		return view;
	}
	
	private void prepareView(View view) {
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.section_story);
		TextView tw = (TextView) layout.findViewById(R.id.story_view);
		Story story = _character.getStory();
		StorySection currentSection = story.getSection(_character.getPosition());
		if(currentSection.isEnemiesAlreadyKilled()) {
			tw.setText(currentSection.getEnemiesDefeatedText());
		} else if(currentSection.isHasLuck()){
			tw.setText(currentSection.getLuckText());
		} else if(currentSection.isVisited()){
			tw.setText(currentSection.getAlreadyVisitedText());	
		} else {
			tw.setText(currentSection.getText());	
		}
		if(!currentSection.getEnemies().isEmpty()) {
			prepareFightSection(view.getContext(), layout, currentSection);
		} else if(!currentSection.getBonuses().isEmpty()) {
			prepareBonusSection(view.getContext(), layout, currentSection, currentSection.getBonuses());
		}
		if(_character.isDefeated()) {
			showGameOver(view.getContext(), view.findViewById(R.id.playground_story), currentSection);
		} else if(currentSection.isEndGame()) {
			displayEndGameButton(view.getContext(), view.findViewById(R.id.playground_story), R.string.button_endGame_win);
		} else if(showOptions) {
			prepareChooseSection(view.getContext(), layout, currentSection);
		}
		tabClick = true;
	}
	
	private void prepareBonusSection(Context context, LinearLayout layout, StorySection section, List<Bonus> bonuses) {
		for(Bonus bonus: bonuses) {
			if(bonus.isAlreadyGained() && section.isVisited()) continue;
			bonus.setAlreadyGained(true);
			int realValue = bonus.getRealvalue();
			if(!section.isBonusesAlreadyGained()) {
				realValue = _character.addBonus(bonus);
				bonus.setRealvalue(realValue);
			}
			String marker = "+";
			TextView opt = new TextView(context);
			if(bonus.getCoeff() > 0) {
				opt.setTextAppearance(context, R.style.textview_bonus);
			} else {
				marker = "-";
				opt.setTextAppearance(context, R.style.textview_debuff);
			}
			String s = getResources().getString(bonus.getText()).toLowerCase();
			opt.setText(marker + realValue + " " + s);
			layout.addView(opt);
		}
		section.setBonusesAlreadyGained(true);
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
		if(!_character.isDefeated()) {
			TextView fight = new TextView(context);
			decoreClickableTextView(context, fight, R.string.button_fight);
			fight.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startFight(context, section);
				}
			});
			layout.addView(fight);
			showOptions = false;
		}
	}
	
	private void startFight(Context context, StorySection section) {
		section.setBonusesAlreadyGained(false);
		activity.changeToBattle(section);
	}
	
	private void prepareAfterFight(Context context, LinearLayout layout, StorySection section) {
		prepareBonusSection(context, layout, section, section.getBonuses(BonusState.AFTER_FIGHT));
		showOptions = true;
	}
	
	private void prepareChooseSection(Context context, LinearLayout layout, StorySection section) {
		for(StorySectionOption option: section.getOptions()) {
			String text = context.getResources().getString(option.getText());
			if(!this.tabClick) {
				_character.setCanShowOption(option);
			}
			if(!option.isDisplayed()) {
				continue;
			}
			if(option.getSection() == section.getUnreturnableSection() && alertUnreturnableOptions) {
				text += " " + context.getResources().getString(R.string.option_cant_return);
			}
			if(option.isBothAspects()) {
				text += " " + context.getResources().getString(R.string.fight_aspect_luck) + "" + context.getResources().getString(R.string.fight_aspect_skill);
			} else if(option.isLuckAspect()) {
				text += " " + context.getResources().getString(R.string.fight_aspect_luck);
			} else if(option.getSkill() > 0) {
				text += " " + context.getResources().getString(R.string.fight_aspect_skill);
			}
			TextView opt = new TextView(context);
			
			if(option.isDisabled()) {
				decoreClickableDisabledTextView(context, opt, text + " " + context.getResources().getString(R.string.option_disabled));
			} else {
				decoreClickableTextView(context, opt, text);
				opt.setOnClickListener(new OptionClickListener(option, section));
			}
			layout.addView(opt);
			option.setAlreadyDisplayed(true);
		}
	}
	private void showGameOver(Context context, View parent, StorySection section) {
		TextView v = (TextView) parent.findViewById(R.id.gameOver_view);
		v.setText(context.getResources().getString(section.getGameOverText()) + " " + context.getResources().getString(R.string.fight_aspect_health));
		v.setVisibility(View.VISIBLE);
		displayEndGameButton(context, parent, R.string.button_endGame_lose);

	}
	private void displayEndGameButton(Context ctx, View parent, int text) {
		Button button = (Button) parent.findViewById(R.id.endGame_button);
		button.setText(text);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getView().getContext(), MainScreenActivity.class);
				getView().getContext().startActivity(intent);
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
			tabClick = false;
			this.section.setCompleted(true);
			this.section.setVisited(true);
			this.section.setHasLuck(false);
			option.setDisabled(option.isDisableWhenSelected());
			int sectionId = _character.getPosition();
			PlaygroundStoryTab.this._character.setPosition(option.getSection());
			PlaygroundStoryTab.this.refresh();
			StorySection nextSection = _character.getStory().getSection(_character.getPosition());
			if(option.isDisabled()) {
				nextSection.setUnreturnableSection(sectionId);
			}
			try {
				IOGameOperation.saveCharacter(v.getContext(), PlaygroundStoryTab.this._character);
			} catch (Exception e) {
				Log.e("GameBookSaver", "", e);
			}
		}
		
	}
	
	public void refresh() {
		final FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.detach(this);
		ft.attach(this);
		ft.commit();
	}
	
	public void setTabClick(boolean tabClick) {
		this.tabClick = tabClick;
	}
}
