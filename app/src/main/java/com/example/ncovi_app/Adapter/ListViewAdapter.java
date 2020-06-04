package com.example.ncovi_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ncovi_app.FontChangeCrawler;
import com.example.ncovi_app.Model.HealthHistory;
import com.example.ncovi_app.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.Context.MODE_PRIVATE;

public class ListViewAdapter extends ArrayAdapter{
    Context context;
    int layoutId;
    ArrayList<HealthHistory> healthHistories = new ArrayList<>();

    public ListViewAdapter(@NonNull Context context, int layoutId, ArrayList<HealthHistory> healthHistories) {
        super(context,layoutId);
        this.context = context;
        this.layoutId = layoutId;
        this.healthHistories = healthHistories;
    }

    @Override
    public int getCount() {
        return healthHistories.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater.from(context));
        convertView = inflater.inflate(layoutId, null);
        Integer fontRes = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("font", R.font.sans_serif);
        FontChangeCrawler fontChanger = new FontChangeCrawler(context, fontRes);
        fontChanger.replaceFonts((ViewGroup)convertView);
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        TextView txtTime = convertView.findViewById(R.id.txtTime);
        TextView txtStatus = convertView.findViewById(R.id.txtStatus);
        TextView txtInfo = convertView.findViewById(R.id.txtInfo);
        View divide = convertView.findViewById(R.id.divide);
        txtDate.setText(healthHistories.get(position).getDate());
        txtTime.setText(healthHistories.get(position).getTime());
        //txtStatus.setText(healthHistories.get(position).getStatus());
        //txtInfo.setText(healthHistories.get(position).getInfo());

        if(healthHistories.get(position).getStatus().equals("An toàn")){
            txtStatus.setText(R.string.health_status_1);
            txtInfo.setText(R.string.health_info_1);
            txtStatus.setBackgroundResource(R.drawable.status_1);
        }
        if(healthHistories.get(position).getStatus().equals("Trung bình")){
            txtStatus.setText(R.string.health_status_2);
            txtInfo.setText(R.string.health_info_2);
            txtStatus.setBackgroundResource(R.drawable.status_2);
        }
        if(healthHistories.get(position).getStatus().equals("Báo động")){
            txtStatus.setText(R.string.health_status_3);
            txtInfo.setText(R.string.health_info_3);
            txtStatus.setBackgroundResource(R.drawable.status_3);
        }
        if (position + 1 < healthHistories.size()){
            if(!healthHistories.get(position).getDate().equals(healthHistories.get(position + 1).getDate())){
                divide.setVisibility(View.VISIBLE);
            }
        }


        return  convertView;
    }
}
