package com.nex.gamebook.story.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.nex.gamebook.R;
import com.nex.gamebook.story.StorySection;
import com.nex.gamebook.story.StorySectionOption;

import android.content.Context;
import android.content.res.XmlResourceParser;

public class XmlParser {
	
	private final String SECTION = "section";
	private final String TEXT = "text";
	private final String OPTIONS = "options";
	private final String OPTION = "option";

	public void parse(Context context) throws XmlPullParserException, IOException {
		XmlResourceParser xrp = context.getResources().getXml(R.xml.story_line1);
		xrp.next();
		List<StorySection> sections = new ArrayList<StorySection>();
		int eventType = xrp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
		    if (eventType == XmlPullParser.START_TAG && xrp.getName().equalsIgnoreCase(SECTION)) {
		       StorySection sc = processSection(xrp);
		       sections.add(sc);
		       break;
		    }
		    eventType = xrp.next();
		}
	}
	private StorySection processSection(XmlResourceParser xrp) throws XmlPullParserException, IOException {
		//first process all attributes
		int position = xrp.getAttributeIntValue(null, "position", 0);
		StorySection section = new StorySection() {};
		section.setPosition(position);
		//next process child elements
		int type = xrp.next();
		while(type == XmlPullParser.START_TAG) {
			String name = xrp.getName();
			if(type != XmlPullParser.TEXT && name.equals(TEXT)) {
				section.setDescription(getText(xrp));
			} else if (name.equals(OPTIONS)) {
				section.getOptions().addAll(processOptions(xrp));
			}
			xrp.next();
		}		
		return section;
	}
	private int getText(XmlResourceParser xrp) throws XmlPullParserException, IOException {
		Object text = xrp.getAttributeValue(null, "name");
		return 0;
	}
	private List<StorySectionOption> processOptions(XmlResourceParser xrp) throws XmlPullParserException, IOException {
		List<StorySectionOption> list = new ArrayList<>();
		int type = xrp.next();
		while(type==XmlPullParser.START_TAG && xrp.getName().equals(OPTION)) {
			StorySectionOption option = new StorySectionOption();
			list.add(option);
			xrp.next();
		}
		return list;
	}
}
