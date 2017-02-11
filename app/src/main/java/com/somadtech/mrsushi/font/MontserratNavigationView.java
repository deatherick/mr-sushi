package com.somadtech.mrsushi.font;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.NavigationView;
import android.text.Spannable;
import android.text.SpannableString;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import com.somadtech.mrsushi.R;

public class MontserratNavigationView extends NavigationView {



	public MontserratNavigationView(Context context) {
		super(context);
		if (isInEditMode()) return;
		parseAttributes(null);
	}

	public MontserratNavigationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) return;
		parseAttributes(attrs);
	}

	public MontserratNavigationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode()) return;
		parseAttributes(attrs);
	}

	private void parseAttributes(AttributeSet attrs) {

		int typeface;
		if (attrs == null) { //Not created from xml
			typeface = Montserrat.MONTSERRAT_LIGHT;
		} else {
			TypedArray values = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFont);
			typeface = values.getInt(R.styleable.CustomFont_typeface, Montserrat.MONTSERRAT_LIGHT);
			values.recycle();
		}
		setTypeface(getMontserrat(typeface));
	}

	private void setTypeface(Typeface typeface){
		Menu m = this.getMenu();
		for (int i=0;i<m.size();i++) {
			MenuItem mi = m.getItem(i);
			//for applying a font to subMenu ...
			SubMenu subMenu = mi.getSubMenu();
			if (subMenu != null && subMenu.size() > 0 ) {
				for (int j=0; j <subMenu.size();j++) {
					MenuItem subMenuItem = subMenu.getItem(j);
					SpannableString s = new SpannableString(subMenuItem.getTitle());
					s.setSpan(typeface, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					subMenuItem.setTitle(s);
				}
			}
			applyFontToMenuItem(mi, typeface);
		}
	}

	private void applyFontToMenuItem(MenuItem mi, Typeface typeface) {
		SpannableString mNewTitle = new SpannableString(mi.getTitle());
		mNewTitle.setSpan(typeface, 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		mi.setTitle(mNewTitle);
	}

	public void setMontserratTypeface(int typeface) {
		setTypeface(getMontserrat(typeface));
	}

	private Typeface getMontserrat(int typeface) {
		return getMontserrat(getContext(), typeface);
	}

	public static Typeface getMontserrat(Context context, int typeface) {
		switch (typeface) {
			case Montserrat.MONTSERRAT:
				if (Montserrat.sMontserrat == null) {
					Montserrat.sMontserrat = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.otf");
				}
				return Montserrat.sMontserrat;
			case Montserrat.MONTSERRAT_LIGHT:
				if (Montserrat.sMontserratLight == null) {
					Montserrat.sMontserratLight = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.otf");
				}
				return Montserrat.sMontserratLight;
			case Montserrat.MONTSERRAT_SEMIBOLD:
				if (Montserrat.sMontserratSemiBold == null) {
					Montserrat.sMontserratSemiBold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-SemiBold.otf");
				}
				return Montserrat.sMontserratSemiBold;
			default:
				if (Montserrat.sMontserrat == null) {
					Montserrat.sMontserrat = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.otf");
				}
				return Montserrat.sMontserrat;
		}
	}

	public static class Montserrat {
		private final static int MONTSERRAT = 0;
		private final static int MONTSERRAT_LIGHT = 1;
		private final static int MONTSERRAT_SEMIBOLD = 2;

		private static Typeface sMontserrat;
		private static Typeface sMontserratLight;
		private static Typeface sMontserratSemiBold;

	}


}

