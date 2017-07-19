package com.example.view;

import com.example.map.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class Digital extends LinearLayout {

	private ImageView imageView1, imageView2, imageView3;
private int[] images = { R.drawable.zero217x324, R.drawable.one217x324,
	R.drawable.two217x324, R.drawable.three217x324,
	R.drawable.four217x324, R.drawable.five217x324,
	R.drawable.six217x324, R.drawable.seven217x324,
	R.drawable.eight217x324, R.drawable.nine217x324,
	R.drawable.blank217x324, R.drawable.minus217x324 };
private int  three, two, one, numbers, textColor;
private Drawable icon0, icon1, icon2, icon3, icon4, icon5, icon6, icon7,
	icon8, icon9, icon11;
	
	public Digital(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.digitalView, defStyleAttr, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.digitalView_textNumbers:
				numbers = a.getInt(attr, 1);
				break;
			case R.styleable.digitalView_textColor:
				textColor = a.getColor(attr, Color.BLACK);
			default:
				break;
			}
		}
		initView(context);
	}
	
	public Digital(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}
	
	public Digital(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	
	public void initView(Context context) {
		View view = View.inflate(getContext(), R.layout.digital, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		addView(view);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		imageView3 = (ImageView) findViewById(R.id.imageView3);
		icon0 = context.getResources().getDrawable(images[0]);
		icon1 = context.getResources().getDrawable(images[1]);
		icon2 = context.getResources().getDrawable(images[2]);
		icon3 = context.getResources().getDrawable(images[3]);
		icon4 = context.getResources().getDrawable(images[4]);
		icon5 = context.getResources().getDrawable(images[5]);
		icon6 = context.getResources().getDrawable(images[6]);
		icon7 = context.getResources().getDrawable(images[7]);
		icon8 = context.getResources().getDrawable(images[8]);
		icon9 = context.getResources().getDrawable(images[9]);
		icon11 = context.getResources().getDrawable(images[11]);
		setNumbers(numbers);
		setColor(textColor);
	}

	/**
	 * <br/>
	 * 概述：着色 <br/>
	 * 
	 * @param color
	 */
	public void setColor(int color) {
		icon0 = tintDrawable(icon0, ColorStateList.valueOf(color));
		icon1 = tintDrawable(icon1, ColorStateList.valueOf(color));
		icon2 = tintDrawable(icon2, ColorStateList.valueOf(color));
		icon3 = tintDrawable(icon3, ColorStateList.valueOf(color));
		icon4 = tintDrawable(icon4, ColorStateList.valueOf(color));
		icon5 = tintDrawable(icon5, ColorStateList.valueOf(color));
		icon6 = tintDrawable(icon6, ColorStateList.valueOf(color));
		icon7 = tintDrawable(icon7, ColorStateList.valueOf(color));
		icon8 = tintDrawable(icon8, ColorStateList.valueOf(color));
		icon9 = tintDrawable(icon9, ColorStateList.valueOf(color));
		icon11 = tintDrawable(icon11, ColorStateList.valueOf(color));
	}

	/**
	 * <br/>
	 * 概述：设置位数 <br/>
	 */
	public void setNumbers(int numbers) {
		this.numbers = numbers;
			imageView1.setVisibility(View.VISIBLE);
			imageView2.setVisibility(View.VISIBLE);
			imageView3.setVisibility(View.VISIBLE);
	}

	/**
	 * <br/>
	 * 概述：设置数值 <br/>
	 */
	public void setValue(int value) {
		if(value >= 0)
		{
			three = value % 1000 / 100;
			two = value % 100 / 10;
			one = value % 10;
			imageView1.setImageResource(images[one]);
			imageView2.setImageResource(images[two]);
			imageView3.setImageResource(images[three]);
			if (three == 0) {
				imageView3.setImageResource(images[10]);
				if (two == 0) {
					imageView2.setImageResource(images[10]);
					}
				}
			imageView1.invalidate();
			imageView2.invalidate();
			imageView3.invalidate();
		}
	}

	/**
	 * <br/>
	 * 概述：给drawable着色 <br/>
	 * 
	 * @param drawable
	 * @param colors
	 * @return
	 */
	public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
		final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
		DrawableCompat.setTintList(wrappedDrawable, colors);
		return wrappedDrawable;
	}

}
