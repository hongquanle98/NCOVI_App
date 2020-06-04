package com.example.ncovi_app.UI.Setting;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ncovi_app.FontChangeCrawler;
import com.example.ncovi_app.R;
import com.example.ncovi_app.UI.SplashActivity;
import com.example.ncovi_app.databinding.FragmentSettingBinding;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    FragmentSettingBinding binding;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        Integer fontRes = getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("font", R.font.default_font);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity(), fontRes);
        fontChanger.replaceFonts((ViewGroup)binding.getRoot());
        binding.rdbMD.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.default_font));
        binding.rdbBM.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.banh_mi_font));
        String language = getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("language", Locale.getDefault().getLanguage());
        String country = getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("country", Locale.getDefault().getCountry());
        if(language.equals("en")){
            binding.rdbEn.setChecked(true);
        }
        if(language.equals("vi")){
            binding.rdbVi.setChecked(true);
        }
        if(fontRes == R.font.default_font){
            binding.rdbMD.setChecked(true);
        }
        if(fontRes == R.font.banh_mi_font){
            binding.rdbBM.setChecked(true);
        }
        setEvent();
        return binding.getRoot();
    }

    private void setEvent() {
        binding.btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.rdbEn.isChecked()){
                    onChangeLanguage(getActivity(), new Locale("en", "US"));
                }
                if(binding.rdbVi.isChecked()){
                    onChangeLanguage(getActivity(), new Locale("vi", "VN"));
                }
                if(binding.rdbMD.isChecked()){
                    onChangeFont(R.font.default_font);
                }
                if(binding.rdbBM.isChecked()){
                    onChangeFont(R.font.banh_mi_font);
                }
                Intent refresh = new Intent(getActivity(), SplashActivity.class);
                startActivity(refresh);
            }
        });
    }

    private void onChangeFont(Integer fontRes){
        getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .edit()
                .putInt("font", fontRes)
                .commit();

    }

    private void onChangeLanguage(Context context, Locale locale){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        Configuration configuration = new Configuration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(locale);
        }else{
            configuration.locale = locale;
        }

        getResources().updateConfiguration(configuration, displayMetrics);
        context.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .edit()
                .putString("language", locale.getLanguage())
                .commit();
        context.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .edit()
                .putString("country", locale.getCountry())
                .commit();
    }

}
