package com.somadtech.mrsushi.font;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.somadtech.mrsushi.R;

public class MontserratTextView extends TextView {



	public MontserratTextView(Context context) {
		super(context);
		if (isInEditMode()) return;
		parseAttributes(null);
	}

	public MontserratTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) return;
		parseAttributes(attrs);
	}

	public MontserratTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode()) return;
		parseAttributes(attrs);
	}

	private void parseAttributes(AttributeSet attrs) {

		int typeface;
		if (attrs == null) { //Not created from xml
			typeface = Montserrat.MONTSERRAT;
		} else {
			TypedArray values = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFont);
			typeface = values.getInt(R.styleable.CustomFont_typeface, Montserrat.MONTSERRAT);
			values.recycle();
		}
		setTypeface(getMontserrat(typeface));
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
					Montserrat.sMontserrat = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Black.ttf");
				}
				return Montserrat.sMontserrat;
			case Montserrat.MONTSERRAT_LIGHT:
				if (Montserrat.sMontserratLight == null) {
					Montserrat.sMontserratLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Black.ttf");
				}
				return Montserrat.sMontserratLight;
			default:
				if (Montserrat.sMontserrat == null) {
					Montserrat.sMontserrat = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
				}
				return Montserrat.sMontserrat;
		}
	}

	public static class Montserrat {
		private final static int MONTSERRAT = 0;
		private final static int MONTSERRAT_LIGHT = 1;

		private static Typeface sMontserrat;
		private static Typeface sMontserratLight;

	}
}
