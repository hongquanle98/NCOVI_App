package com.example.ncovi_app.UI.SucKhoe;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.ncovi_app.Adapter.ListViewAdapter;
import com.example.ncovi_app.FontChangeCrawler;
import com.example.ncovi_app.Model.HealthHistory;
import com.example.ncovi_app.DB.HealthHistoryDB;
import com.example.ncovi_app.R;
import com.example.ncovi_app.UI.Home.HomeFragment;
import com.example.ncovi_app.databinding.FragmentSucKhoeBinding;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SucKhoeFragment extends Fragment {

    FragmentSucKhoeBinding binding;

    ArrayList<HealthHistory> healthHistories = new ArrayList<>();
    HealthHistoryDB healthHistoryDB;
    ListViewAdapter listViewAdapter;
    private ProgressDialog progressDialog;

    public SucKhoeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_suc_khoe, container, false);
        Integer fontRes = getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("font", R.font.default_font);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity(), fontRes);
        fontChanger.replaceFonts((ViewGroup)binding.getRoot());
        healthHistoryDB = new HealthHistoryDB(getActivity());
        listViewAdapter = new ListViewAdapter(getActivity(), R.layout.list_view_item_health_history, healthHistories);
        binding.lsvLichSu.setAdapter(listViewAdapter);
        getAllData();
        setEvent();
        return binding.getRoot();
    }

    private void setEvent() {
        binding.cbSucKhoeTot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (binding.cbSucKhoeTot.isChecked()) {
                    binding.cbSot.setChecked(false);
                    binding.cbHo.setChecked(false);
                    binding.cbKhoTho.setChecked(false);
                    binding.cbDauNguoi.setChecked(false);
                }
            }
        });
        binding.cbSot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (binding.cbSot.isChecked()) {
                    binding.cbSucKhoeTot.setChecked(false);
                }
            }
        });
        binding.cbHo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (binding.cbHo.isChecked()) {
                    binding.cbSucKhoeTot.setChecked(false);
                }
            }
        });
        binding.cbKhoTho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (binding.cbKhoTho.isChecked()) {
                    binding.cbSucKhoeTot.setChecked(false);
                }
            }
        });
        binding.cbDauNguoi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (binding.cbDauNguoi.isChecked()) {
                    binding.cbSucKhoeTot.setChecked(false);
                }
            }
        });
        binding.btnGuiTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasError()) {
//                    progressDialog = new ProgressDialog(getActivity());
//                    progressDialog.setTitle("Đang tải dữ liệu");
//                    progressDialog.setMessage("Xin chờ...");
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
                    HealthHistory healthHistory = new HealthHistory();
                    String dateTime = new HomeFragment().convertEpochTime(System.currentTimeMillis());
                    dateTime = dateTime.replaceAll("\\s", "");
                    String dateTimeArray[] = dateTime.split(",");

                    //healthHistory.setDate(dateTimeArray[1]);
                    healthHistory.setTime(dateTimeArray[0]);
                    healthHistory.setStatus(getHealthStatus());
                    healthHistory.setInfo(getHealthInfo());



                    healthHistory.setDate(dateFormat(dateTimeArray[1]));
                    healthHistoryDB.add(healthHistory);
                    getAllData();
                    Toast.makeText(getActivity(), getString(R.string.update_info), Toast.LENGTH_LONG).show();
//                    progressDialog.dismiss();
                }
            }
        });
        binding.lsvLichSu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.delete_history))
                        .setPositiveButton(getString(R.string.delete_history_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                healthHistories.get(position).setDate(dateFormat(healthHistories.get(position).getDate()));
                                healthHistoryDB.delete(healthHistories.get(position));
                                getAllData();
                                Toast.makeText(getActivity(), getString(R.string.delete_history_result), Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(getString(R.string.delete_history_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });
    }

    private boolean hasError() {
        if (!(binding.cbSot.isChecked() || binding.cbHo.isChecked() || binding.cbKhoTho.isChecked() || binding.cbDauNguoi.isChecked() || binding.cbSucKhoeTot.isChecked())) {
            Toast.makeText(getActivity(), getString(R.string.choose_health_history), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private String getHealthStatus() {
        if (binding.cbSucKhoeTot.isChecked()) {
            return "An toàn";
        } else if (binding.cbSot.isChecked() && binding.cbHo.isChecked() && binding.cbKhoTho.isChecked() && binding.cbDauNguoi.isChecked()) {
            return "Báo động";
        } else if (binding.cbSot.isChecked() || binding.cbHo.isChecked() || binding.cbKhoTho.isChecked() || binding.cbDauNguoi.isChecked()) {
            return "Trung bình";
        } else {
            return "Trung bình";
        }
    }

    private String getHealthInfo() {
        if (binding.cbSucKhoeTot.isChecked()) {
            return "Bình thường";
        } else if (binding.cbSot.isChecked() && binding.cbHo.isChecked() && binding.cbKhoTho.isChecked() && binding.cbDauNguoi.isChecked()) {
            return "Nguy cơ nhiễm Covid-19, cần đi khám ngay";
        } else if (binding.cbSot.isChecked() || binding.cbHo.isChecked() || binding.cbKhoTho.isChecked() || binding.cbDauNguoi.isChecked()) {
            return "Không tốt, có thể điều trị tại nhà";
        } else {
            return "Không tốt, có thể điều trị tại nhà";
        }
    }

    public void getAllData() {
        Cursor cursor = healthHistoryDB.getAllData();
        if (cursor != null) {
            healthHistories.clear();
            while (cursor.moveToNext()) {
                HealthHistory healthHistory = new HealthHistory();
                healthHistory.setDate(dateFormat(cursor.getString(0)));
                healthHistory.setTime(cursor.getString(1));
                healthHistory.setStatus(cursor.getString(2));
                healthHistory.setInfo(cursor.getString(3));
                healthHistories.add(healthHistory);
            }
            listViewAdapter.notifyDataSetChanged();
        }
    }
    public String dateFormat(String date){
        String dateArray[] = date.split("-");
        String dateFormat = dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0];
        return dateFormat;
    }

}
