package com.yzm.sleep.utils;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceUtil {
	public static Typeface getTypeHiraginoSans(Context context){
		return Typeface.createFromAsset(context.getAssets(), "STHeitiMedium.ttc");
	}
	public static Typeface getTypeHiraginoSansBold(Context context){
		return Typeface.createFromAsset(context.getAssets(), "STHeitiMedium.ttc");
	}
}
