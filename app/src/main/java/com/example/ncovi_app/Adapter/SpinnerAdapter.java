package com.example.ncovi_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.ncovi_app.FontChangeCrawler;
import com.example.ncovi_app.Model.Nation;
import com.example.ncovi_app.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.Context.MODE_PRIVATE;

public class SpinnerAdapter extends ArrayAdapter {
    Context context;
    int layoutId;
    ArrayList<Nation> nationData;

    public SpinnerAdapter(Context context, int layoutId, ArrayList<Nation> nationData) {
        super(context, layoutId);
        this.context = context;
        this.layoutId = layoutId;
        this.nationData = nationData;
    }

    @Override
    public int getCount() {
        return nationData.size();
    }


    public int getPos(String name) {
        int pos = -1;
        for (int i = 0; i < nationData.size(); i++) {
            if (nationData.get(i).getName().equals(name)) {
                return i;
            }
        }
        return pos;
    }

    @Nullable
    @Override
    public Nation getItem(int position) {
        return nationData.get(position);
    }
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater.from(context));
        convertView = inflater.inflate(layoutId, null);
        Integer fontRes = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("font", R.font.default_font);
        FontChangeCrawler fontChanger = new FontChangeCrawler(context, fontRes);
        fontChanger.replaceFonts((ViewGroup)convertView);
        TextView txtNation =(TextView) convertView.findViewById(R.id.txtNation);
        ImageView img =(ImageView) convertView.findViewById(R.id.flag);
        txtNation.setText(nationData.get(position).getName());
//        Picasso.get().load(nationData.get(position).getFlag()).into(img);
        Glide.with(context)
                .load(nationData.get(position).getFlag())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(img);
        return  convertView;

    }
}
