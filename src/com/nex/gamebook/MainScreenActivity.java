package com.nex.gamebook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nex.gamebook.util.GameBookUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main_screen);
		GameBookUtils.initialize(this);
		String category = GameBookUtils.FOLDER;
//		GameBookUtils.getInstance().getPreferences().edit().clear().commit();
		copyAssetFolder(getAssets(), category, GameBookUtils.getGamebookStorage(this) + File.separator + category + File.separator);
	}

	public void newGame(View view) {
		Intent intent = new Intent(this, StorySelectionActivity.class);
		startActivity(intent);
	}

	public void loadGame(View view) {
		Intent intent = new Intent(this, LoadGameActivity.class);
		startActivity(intent);
	}
	
	public void guide(View view) {
		Intent intent = new Intent(this, GuideActivity.class);
		startActivity(intent);
	}
	public void scoreboard(View view) {
		Intent intent = new Intent(this, ScoreBoardActivity.class);
		startActivity(intent);
	}
	private static boolean copyAssetFolder(AssetManager assetManager,
			String fromAssetPath, String toPath) {
		try {
			String[] files = assetManager.list(fromAssetPath);
			File dir = new File(toPath);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			
			boolean res = true;
			for (String file : files)
				if (file.contains("."))
					res &= copyAsset(assetManager, fromAssetPath + File.separator + file,
							toPath + File.separator + file);
				else
					res &= copyAssetFolder(assetManager, fromAssetPath + File.separator
							+ file, toPath + File.separator + file);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean copyAsset(AssetManager assetManager,
			String fromAssetPath, String toPath) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(fromAssetPath);
			new File(toPath).createNewFile();
			out = new FileOutputStream(toPath);
			copyFile(in, out);
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static void copyFile(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
}
