package com.nex.gamebook;

import java.util.Map;

import com.nex.gamebook.game.ExperienceMap;
import com.nex.gamebook.game.Bonus.StatType;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GuideActivity extends Activity {

	// Within which the entire activity is enclosed
	private DrawerLayout mDrawerLayout;

	// ListView represents Navigation Drawer
	private ListView mDrawerList;

	// ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
	private ActionBarDrawerToggle mDrawerToggle;

	// Title of the action bar
	private String mTitle = "";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		mTitle = getString(R.string.guide);
		getActionBar().setTitle(mTitle);
		// Getting reference to the DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		// Getting reference to the ActionBarDrawerToggle
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
			}
			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(getString(R.string.guide));
			}
		};

		// Setting DrawerToggle on DrawerLayout
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		String[] names = new String[getTexts().length];
		for(int i = 0; i < getTexts().length; i++) {
			Object[] o = getTexts()[i];
			Object[] texts = (Object[]) o[0];
			names[i] = (String) texts[0];
		}
		// Creating an ArrayAdapter to add items to the listview mDrawerList
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), 
				R.layout.guide_item, names);

		// Setting the adapter on mDrawerList
		mDrawerList.setAdapter(adapter);

		// Enabling Home button
		getActionBar().setHomeButtonEnabled(true);

		// Enabling Up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Setting item click listener for the listview mDrawerList
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object[] row = getTexts()[position];
				Object[] texts = (Object[]) row[0];
				mTitle = (String) texts[0];
				int text = (int) texts[1];
				Object[] args = (Object[]) row[1];
				TextView textView = (TextView) findViewById(R.id.guideText);
				textView.setText(getString(text, args));
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private Object[][] getTexts() {
		return new Object[][]{
			{new Object[]{getString(R.string.level), R.string.guide_levels}, new Object[]{
				String.valueOf(StatType.HEALTH.getIncreaseByLevel()),
				String.valueOf(StatType.ATTACK.getIncreaseByLevel()),
				String.valueOf(StatType.DEFENSE.getIncreaseByLevel()),
				String.valueOf(StatType.SKILL.getIncreaseByLevel()),
				String.valueOf(StatType.LUCK.getIncreaseByLevel()),
				String.valueOf(StatType.DAMAGE.getIncreaseByLevel()),
				String.valueOf(StatType.SKILLPOWER.getIncreaseByLevel()),
				String.valueOf(ExperienceMap.MAX_LEVEL)
			}},
			{new Object[]{getString(R.string.skills),R.string.guide_skills}, null},
			{new Object[]{getString(R.string.section),R.string.guide_section}, null},
			{new Object[]{getString(R.string.options),R.string.guide_option}, null},
			{new Object[]{getString(R.string.enemies),R.string.guide_enemies}, null},
			{new Object[]{getString(R.string.battle),R.string.guide_battle}, null},
			{new Object[]{getString(R.string.modifications),R.string.guide_modifications}, null},
			{new Object[]{getString(R.string.attr_health),R.string.guide_health}, null},
			{new Object[]{getString(R.string.attr_attack),R.string.guide_attack}, null},
			{new Object[]{getString(R.string.attr_defense),R.string.guide_defense}, null},
			{new Object[]{getString(R.string.attr_skill),R.string.guide_skill}, null},
			{new Object[]{getString(R.string.attr_luck),R.string.guide_luck}, null},
			{new Object[]{getString(R.string.attr_skill_power), R.string.guide_skillpower}, null},
			{new Object[]{getString(R.string.game_saving),R.string.guide_save}, null}};
	}
}
