package com.example.ncovi_app;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

public class FontChangeCrawler
{
    private Typeface typeface;

    public FontChangeCrawler(Typeface typeface)
    {
        this.typeface = typeface;
    }

    public FontChangeCrawler(Context context, int assetsFontFileName)
    {
        typeface = ResourcesCompat.getFont(context, assetsFontFileName);
    }

    public void replaceFonts(ViewGroup viewTree)
    {
        View child;
        for(int i = 0; i < viewTree.getChildCount(); ++i)
        {
            child = viewTree.getChildAt(i);
            if(child instanceof ViewGroup)
            {
                // recursive call
                replaceFonts((ViewGroup)child);
            }
            else if(child instanceof TextView)
            {
                // base case
                ((TextView) child).setTypeface(typeface);
            }
        }
    }
}