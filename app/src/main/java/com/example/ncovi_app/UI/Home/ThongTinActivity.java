package com.example.ncovi_app.UI.Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ncovi_app.DateInputMask;
import com.example.ncovi_app.FontChangeCrawler;
import com.example.ncovi_app.R;
import com.example.ncovi_app.Model.UserInfo;
import com.example.ncovi_app.DB.UserInfoDB;
import com.example.ncovi_app.databinding.ActivityThongTinBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ThongTinActivity extends AppCompatActivity {
    ActivityThongTinBinding binding;
    RequestQueue queue;
    ArrayList quocGiaArray;

    HashMap<String, Integer> tinhHashMap;
    ArrayList tenTinhArray;
    HashMap<String, Integer> quanHashMap;
    ArrayList tenQuanArray;
    HashMap<String, Integer> phuongHashMap;
    ArrayList tenPhuongArray;

    ArrayAdapter adapterQuocGia;
    ArrayAdapter adapterTinh;
    ArrayAdapter adapterQuan;
    ArrayAdapter adapterPhuong;

    boolean flag = false;

    UserInfoDB userInfoDB;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thong_tin);
        Integer fontRes = getApplicationContext().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("font", R.font.default_font);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getApplicationContext(), fontRes);
        fontChanger.replaceFonts((ViewGroup)binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(getApplicationContext());
        userInfoDB = new UserInfoDB(getApplicationContext());
        new DateInputMask(binding.edtNgaySinh);
        getSpinnerData();

        setEvent();
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
                    adapterPhuong = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, tenPhuongArray);
                    binding.spnPhuong.setAdapter(adapterPhuong);



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
                    adapterQuan = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, tenQuanArray);
                    binding.spnQuan.setAdapter(adapterQuan);

                    if (!flag){
                        getAllData();
                        flag = true;
                    }

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
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url;

        url = "https://restcountries.eu/rest/v2/all";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    quocGiaArray = new ArrayList();
                    for (int i = 0; i < response.length(); i++) {
                        quocGiaArray.add(response.getJSONObject(i).getString("name"));
                    }
                    adapterQuocGia = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, quocGiaArray);
                    binding.spnQuocTich.setAdapter(adapterQuocGia);
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
                    adapterTinh = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, tenTinhArray);
                    binding.spnTinh.setAdapter(adapterTinh);

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
        binding.txtTraCuuBHXH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TraCuuBHXHActivity.class));
            }
        });
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
        binding.btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasError()) {
//                    progressDialog = new ProgressDialog(getApplicationContext());
//                    progressDialog.setTitle("Đang tải dữ liệu");
//                    progressDialog.setMessage("Xin chờ...");
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
                    UserInfo userInfo = new UserInfo();
                    userInfo.setFullName(binding.edtHoTen.getText().toString().trim());
                    userInfo.setiDNumber(binding.edtCMT.getText().toString().trim());
                    userInfo.setbHXHNumber(binding.edtBHXH.getText().toString().trim());
                    userInfo.setBirthDay(binding.edtNgaySinh.getText().toString().trim());
                    userInfo.setSex((binding.rdbNam.isChecked() ? "Nam" : "Nữ"));
                    userInfo.setNationality(binding.spnQuocTich.getSelectedItem().toString());
                    userInfo.setCity(binding.spnTinh.getSelectedItem().toString());
                    userInfo.setDistrict(binding.spnQuan.getSelectedItem().toString());
                    userInfo.setWard(binding.spnPhuong.getSelectedItem().toString());
                    //userInfo.setWard("Chưa rõ");
                    userInfo.setStreet(binding.edtDuong.getText().toString().trim());
                    userInfo.setPhone(binding.edtSDT.getText().toString().trim());
                    userInfo.setEmail(binding.edtEmail.getText().toString().trim());
                    if (userInfoDB.getCount() < 1){
                        userInfoDB.add(userInfo);
                        Toast.makeText(getApplicationContext(), "Cập nhật thông tin thành công", Toast.LENGTH_LONG).show();
                    }else{
                        userInfoDB.edit(userInfo);
                        Toast.makeText(getApplicationContext(), "Cập nhật thông tin thành công", Toast.LENGTH_LONG).show();
                    }
//                    progressDialog.dismiss();
                }
            }
        });
    }

    private boolean hasError() {
        if (binding.edtHoTen.getText().toString().trim().isEmpty()) {
            binding.edtHoTen.setError(getString(R.string.empty_full_name));
            binding.edtHoTen.requestFocus();
            return true;
        }
        if (binding.edtCMT.getText().toString().trim().isEmpty()) {
            binding.edtCMT.setError(getString(R.string.empty_id_number));
            binding.edtCMT.requestFocus();
            return true;
        }
        if (binding.edtNgaySinh.getText().toString().trim().isEmpty()) {
            binding.edtNgaySinh.setError(getString(R.string.empty_dob));
            binding.edtNgaySinh.requestFocus();
            return true;
        }
        if (binding.edtSDT.getText().toString().trim().isEmpty()) {
            binding.edtSDT.setError(getString(R.string.empty_phone));
            binding.edtSDT.requestFocus();
            return true;
        }
        if (!validCMT(binding.edtCMT.getText().toString())) {
            binding.edtCMT.setError(getString(R.string.invalid_id));
            binding.edtCMT.requestFocus();
            return true;
        }
        if (!validDate(binding.edtNgaySinh.getText().toString())) {
            binding.edtNgaySinh.setError(getString(R.string.invalid_dob));
            binding.edtNgaySinh.requestFocus();
            return true;
        }
        if (!validPhone(binding.edtSDT.getText().toString())) {
            binding.edtSDT.setError(getString(R.string.invalid_name));
            binding.edtSDT.requestFocus();
            return true;
        }
//        if(!validEmail(binding.edtEmail.getText().toString()) || binding.edtEmail.getText().toString().trim().isEmpty()){
//            binding.edtEmail.setError("Email không hợp lệ");
//            binding.edtEmail.requestFocus();
//            return true;
//        }
        return false;
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private boolean validPhone(String phoneNum) {
        Pattern phonePattren = Pattern.compile("(09|08|03|07|05)[0-9]{8}");
        return phonePattren.matcher(phoneNum).matches() ? true : false;
    }

    private boolean validCMT(String cmt) {
        Pattern cmtPattren = Pattern.compile("[0-9]{9}|[0-9]{12}");
        return cmtPattren.matcher(cmt).matches() ? true : false;
    }

    private boolean validDate(String date) {
        Pattern datePattren = Pattern.compile("[0-9]{2}/[0-9]{2}/[0-9]{4}");
        return datePattren.matcher(date).matches() ? true : false;
    }

    private boolean validEmail(String email) {
        Pattern emailPattren = Pattern.compile("^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@\"\n" +
                "\t\t+ \"[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$");
        return emailPattren.matcher(email).matches() ? true : false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getAllData() {

        if (userInfoDB.getCount() > 0) {
            Cursor cursor = userInfoDB.getAllData();
            UserInfo userInfo= new UserInfo();
            while (cursor.moveToNext()) {
                userInfo = new UserInfo();

                userInfo.setFullName(cursor.getString(0));
                userInfo.setiDNumber(cursor.getString(1));
                userInfo.setbHXHNumber(cursor.getString(2));
                userInfo.setBirthDay(cursor.getString(3));
                userInfo.setSex(cursor.getString(4));
                userInfo.setNationality(cursor.getString(5));
                userInfo.setCity(cursor.getString(6));
                userInfo.setDistrict(cursor.getString(7));
                userInfo.setWard(cursor.getString(8));
                userInfo.setStreet(cursor.getString(9));
                userInfo.setPhone(cursor.getString(10));
                userInfo.setEmail(cursor.getString(11));

            }

            //if (flag){
                binding.edtHoTen.setText(userInfo.getFullName());
                binding.edtCMT.setText(userInfo.getiDNumber());
                binding.edtBHXH.setText(userInfo.getbHXHNumber());
                binding.edtNgaySinh.setText(userInfo.getBirthDay());
                if (userInfo.getSex().equals("Nam")) {
                    binding.rdbNam.setChecked(true);
                } else {
                    binding.rdbNu.setChecked(true);
                }
                binding.edtDuong.setText(userInfo.getStreet());
                binding.edtSDT.setText(userInfo.getPhone());
                binding.edtEmail.setText(userInfo.getEmail());
            //}

            binding.spnQuocTich.setSelection(quocGiaArray.indexOf(userInfo.getNationality()));
            binding.spnTinh.setSelection(tenTinhArray.indexOf(userInfo.getCity()));
            binding.spnQuan.setSelection(tenQuanArray.indexOf(userInfo.getDistrict()));
            binding.spnPhuong.setSelection(tenPhuongArray.indexOf(userInfo));
        }
    }

}
