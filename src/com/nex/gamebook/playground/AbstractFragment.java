package com.nex.gamebook.playground;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nex.gamebook.R;
import com.nex.gamebook.fragment.GameBookFragment;


public abstract class AbstractFragment extends GameBookFragment {
	
	public AbstractFragment(Context context) {
		super(context);
	}
	
	protected void decoreClickableTextView(Context context, TextView view, String text) {
		SpannableString spanString = new SpannableString(text);
		spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		view.setTextAppearance(context, R.style.option);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    llp.setMargins(0, 50, 0, 0);
	    view.setLayoutParams(llp);
	    view.setText(spanString);
	}
	protected void decoreClickableDisabledTextView(Context context, TextView view, String text) {
//		SpannableString spanString = new SpannableString(text);
//		spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		view.setTextAppearance(context, R.style.option_disabled);
//		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//	    llp.setMargins(0, 50, 0, 0);
//	    view.setLayoutParams(llp);
	    view.setText(text);
	}
	protected void decoreClickableTextView(Context context, TextView view, int text) {
		this.decoreClickableTextView(context, view, context.getResources().getString(text));
	}
	protected PlaygroundActivity getPlayground() {
		return (PlaygroundActivity) getContext();
	}
}
