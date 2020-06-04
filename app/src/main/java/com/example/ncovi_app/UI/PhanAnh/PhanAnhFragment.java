package com.example.ncovi_app.UI.PhanAnh;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ncovi_app.FontChangeCrawler;
import com.example.ncovi_app.R;
import com.example.ncovi_app.Model.Report;
import com.example.ncovi_app.DB.ReportDB;
import com.example.ncovi_app.UI.Home.HomeFragment;
import com.example.ncovi_app.databinding.FragmentPhanAnhBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhanAnhFragment extends Fragment {

    FragmentPhanAnhBinding binding;
    RequestQueue queue;

    HashMap<String, Integer> tinhHashMap;
    ArrayList tenTinhArray;
    HashMap<String, Integer> quanHashMap;
    ArrayList tenQuanArray;
    HashMap<String, Integer> phuongHashMap;
    ArrayList tenPhuongArray;

    ArrayAdapter adapter;

    ArrayList<Report> reports = new ArrayList<>();
    ReportDB reportDB;
    private ProgressDialog progressDialog;

    public PhanAnhFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phan_anh, container, false);
        Integer fontRes = getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("font", R.font.default_font);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity(), fontRes);
        fontChanger.replaceFonts((ViewGroup)binding.getRoot());
        binding.cbCamKet.setTextColor(Color.parseColor("#33F44336"));
        ReadOnlyEditText(binding.edtThoiGian);
        binding.edtThoiGian.setText(new HomeFragment().convertEpochTime(System.currentTimeMillis()));
        reportDB = new ReportDB(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        getSpinnerData();
        setEvent();
        return binding.getRoot();
    }

    private void getQuanData(Integer id) {
        String url = "https://thongtindoanhnghiep.co/api/city/" + id + "/district";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    quanHashMap = new HashMap<>();
                    tenQuanArray = new ArrayList();
                    for (int i = 0; i < response.length(); i++) {
                        quanHashMap.put(response.getJSONObject(i).getString("Title"), response.getJSONObject(i).getInt("ID"));
                        tenQuanArray.add(response.getJSONObject(i).getString("Title"));
                    }
                    Collections.sort(tenQuanArray);
                    adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, tenQuanArray);
                    binding.spnQuan.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void getPhuongData(Integer id) {
        String url = "https://thongtindoanhnghiep.co/api/district/" + id + "/ward";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    phuongHashMap = new HashMap<>();
                    tenPhuongArray = new ArrayList();
                    for (int i = 0; i < response.length(); i++) {
                        phuongHashMap.put(response.getJSONObject(i).getString("Title"), response.getJSONObject(i).getInt("ID"));
                        tenPhuongArray.add(response.getJSONObject(i).getString("Title"));
                    }
                    Collections.sort(tenPhuongArray);
                    adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, tenPhuongArray);
                    binding.spnPhuong.setAdapter(adapter);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void getSpinnerData() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url;

        url = "https://thongtindoanhnghiep.co/api/city";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tinhHashMap = new HashMap<>();
                    tenTinhArray = new ArrayList();
                    JSONArray jsonArray = response.getJSONArray("LtsItem");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        tinhHashMap.put(jsonArray.getJSONObject(i).getString("Title"), jsonArray.getJSONObject(i).getInt("ID"));
                        tenTinhArray.add(jsonArray.getJSONObject(i).getString("Title"));
                    }
                    Collections.sort(tenTinhArray);
                    adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, tenTinhArray);
                    binding.spnTinh.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void setEvent() {
        binding.spnTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getQuanData(tinhHashMap.get(tenTinhArray.get(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spnQuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPhuongData(quanHashMap.get(tenQuanArray.get(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.cbCamKet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!binding.cbCamKet.isChecked()) {
                    binding.cbCamKet.setTextColor(Color.parseColor("#33F44336"));
                    binding.btnGuiTT.setEnabled(false);
                    binding.btnGuiTT.setBackgroundResource(R.drawable.disable_button);
                } else {
                    binding.cbCamKet.setTextColor(Color.parseColor("#FFF44336"));
                    binding.btnGuiTT.setEnabled(true);
                    binding.btnGuiTT.setBackgroundResource(R.drawable.button);
                }
            }
        });
        binding.edtThoiGian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
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
                    Report report = new Report();
                    report.setDateTime(binding.edtThoiGian.getText().toString().trim());
                    String detail = "";
                    if (binding.cb1.isChecked()) {
                        detail += binding.cb1.getText().toString();
                    }
                    if (binding.cb2.isChecked()) {
                        detail += "\n"+binding.cb2.getText().toString();
                    }
                    if (binding.cb3.isChecked()) {
                        detail += "\n"+binding.cb3.getText().toString();
                    }
                    report.setDetail(detail + "\nKhác: " + binding.edtNoiDung.getText().toString());
                    report.setAddress(binding.edtDuong.getText().toString() + ", " + binding.spnPhuong.getSelectedItem().toString() + ", " + binding.spnQuan.getSelectedItem().toString() + ", " + binding.spnTinh.getSelectedItem().toString());
                    reportDB.add(report);
                    Toast.makeText(getActivity(), getString(R.string.update_report), Toast.LENGTH_SHORT).show();
                    //progressDialog.dismiss();
                }
            }
        });
    }

    private boolean hasError() {
        if (!binding.cb1.isChecked() && !binding.cb2.isChecked() && !binding.cb3.isChecked()) {
            Toast.makeText(getActivity(), getString(R.string.choose_case), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void ReadOnlyEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setLongClickable(true);
        editText.setClickable(true);
        editText.setCursorVisible(false);
    }

    private void showDatePickerDialog(final int hour, final int minute) {
        // Get Current Date
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        binding.edtThoiGian.setText(hour + ":" + minute + ", " + dayOfMonth + "/" + monthOfYear + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                showDatePickerDialog(selectedHour, selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();

    }
//    public void getAllData() {
//        Cursor cursor = reportDB.getAllData();
//        if (cursor != null) {
//            reports.clear();
//            while (cursor.moveToNext()) {
//                Report report = new Report();
//                report.setDateTime(cursor.getString(0));
//                report.setAddress(cursor.getString(1));
//                report.setDetail(cursor.getString(2));
//                reports.add(report);
//            }
//        }
//    }
}
