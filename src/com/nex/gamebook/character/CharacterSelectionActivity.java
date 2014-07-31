package com.nex.gamebook.character;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nex.gamebook.R;
import com.nex.gamebook.character.definition.Character;
import com.nex.gamebook.db.CharactersDatasource;

public class CharacterSelectionActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_character_selection);
        CharactersDatasource ds = new CharactersDatasource(this);
        ds.open();
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for(Character c: ds.getAllCharacters()) {
        	ActionBar.Tab tab = actionBar.newTab().setText(c.getName());
        	Fragment fragmentTab = new FragmentTab(c);
        	tab.setTabListener(new MyTabListener(fragmentTab));
        	 actionBar.addTab(tab);
        }
        ds.close();
	}
	public class FragmentTab extends Fragment {
		
		  private Character _character;
		  public FragmentTab(Character ch) {
			  this._character = ch;
		  }
		
		  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
		                           Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_character_selection, container, false);
			TextView attr = (TextView) view.findViewById(R.id.sel_attr_health);
			attr.setText(String.valueOf(_character.getHealth()));
			
			attr = (TextView) view.findViewById(R.id.sel_attr_attack);
			attr.setText(String.valueOf(_character.getAttack()));
			
			attr = (TextView) view.findViewById(R.id.sel_attr_defense);
			attr.setText(String.valueOf(_character.getDefense()));
			
			attr = (TextView) view.findViewById(R.id.sel_attr_skill);
			attr.setText(String.valueOf(_character.getSkill()));
			
			TextView textview = (TextView) view.findViewById(R.id.sel_char_description);
			textview.setText(_character.getDescription());
			return view;
		  }
		}
	public class MyTabListener implements ActionBar.TabListener {
		Fragment fragment;
		
		public MyTabListener(Fragment fragment) {
			this.fragment = fragment;
		}
		
	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_container, fragment);
		}
		
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}
		
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			
		}
	}
}
