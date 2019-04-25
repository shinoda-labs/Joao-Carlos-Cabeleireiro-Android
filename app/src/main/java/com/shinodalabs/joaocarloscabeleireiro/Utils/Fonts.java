package com.shinodalabs.joaocarloscabeleireiro.Utils;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {

    public static Typeface TypefaceBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
    }

    public static Typeface TypefaceLight(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
    }

    public static Typeface TypefaceItalic(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Italic.ttf");
    }

    public static Typeface TypefaceThin(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
    }

}
