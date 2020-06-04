package com.example.ncovi_app.UI.Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ncovi_app.Adapter.SpinnerAdapter;
import com.example.ncovi_app.FontChangeCrawler;
import com.example.ncovi_app.Model.Nation;
import com.example.ncovi_app.R;
import com.example.ncovi_app.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback {

    FragmentHomeBinding binding;
    RequestQueue queue;
    ArrayList<Nation> nationData = new ArrayList<>();
    SpinnerAdapter spinnerAdapter;
    ProgressDialog progressDialog;
    GoogleMap mMap;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        Integer fontRes = getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("font", R.font.default_font);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity(), fontRes);
        fontChanger.replaceFonts((ViewGroup)binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        queue = Volley.newRequestQueue(getActivity());

        getCountry();

        boolean firstrun = getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun) {
            //... Display the dialog message here ...
            FragmentManager fm = getFragmentManager();
            ReminderDialog dialog = new ReminderDialog();
            dialog.setCancelable(false);
            dialog.show(fm, null);
            // Save the state
            getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstrun", false)
                    .commit();
        }
        setEvent();
        return binding.getRoot();
    }

    private void setEvent() {
        binding.btnVietNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStyleButton(binding.btnTheGioi);
                binding.btnVietNam.setCompoundDrawableTintList(ColorStateList.valueOf(Color.argb(100, 233, 30, 99)));
                binding.btnVietNam.setTextColor(Color.rgb(63, 81, 181));
                binding.spnQuocGia.setSelection(spinnerAdapter.getPos("Vietnam"));
                //getData("Vietnam");
            }
        });
        binding.btnTheGioi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStyleButton(binding.btnVietNam);
                binding.btnTheGioi.setCompoundDrawableTintList(ColorStateList.valueOf(Color.argb(100, 76, 175, 80)));
                binding.btnTheGioi.setTextColor(Color.rgb(63, 81, 181));
                binding.spnQuocGia.setSelection(spinnerAdapter.getPos(getString(R.string.world_btn)));
                //getData(null);
            }
        });
        binding.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nationData.get(binding.spnQuocGia.getSelectedItemPosition()).getName().contains(getString(R.string.world_btn))) {
                    getData(null);
                } else {
                    getData(nationData.get(binding.spnQuocGia.getSelectedItemPosition()).getName());
                }
            }
        });
        binding.spnQuocGia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (nationData.get(position).getName().contains(getString(R.string.world_btn))) {
                    getData(null);
                } else {
                    getData(nationData.get(position).getName());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.txtChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://ncovi.vnpt.vn/views/ncovi_detail.html")));
            }
        });
        binding.txtNguon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://ncov.moh.gov.vn/")));
            }
        });
        binding.txtMoRong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MapActivity.class));
            }
        });
        binding.btnHuongDan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HuongDanActivity.class));
            }
        });
        binding.btnKhaiBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ThongTinActivity.class));
            }
        });
    }

    private void getCountry() {
        String url = "https://disease.sh/v2/countries";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    nationData.add(new Nation(getString(R.string.world_btn), "https://cdn2.iconfinder.com/data/icons/social-messaging-productivity-6/128/world-all-outline-512.png"));
                    for (int i = 0; i < response.length(); i++) {
                        Nation nation = new Nation();
                        nation.setName(response.getJSONObject(i).getString("country"));
                        nation.setFlag(response.getJSONObject(i).getJSONObject("countryInfo").getString("flag"));
                        nationData.add(nation);
                    }

                    spinnerAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_item, nationData);
                    binding.spnQuocGia.setAdapter(spinnerAdapter);

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
        queue.add(request);

    }

    private void getData(final String country) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.loading)+" " + (country == null ? getString(R.string.world_btn) : country));
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.setCancelable(false);
        progressDialog.show();
        String url;
        if (country == null) {
            url = "https://disease.sh/v2/all";
        } else {
            url = "https://disease.sh/v2/countries/" + country;
        }
        Toast.makeText(getActivity(), (country == null ? getString(R.string.world_btn) : country), Toast.LENGTH_LONG).show();
        final DecimalFormat formatter = new DecimalFormat("#,###,###");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    binding.txtNBNum.setText(formatter.format(Long.parseLong(jsonObject.getString("cases"))));
                    binding.txtTVNum.setText(formatter.format(Long.parseLong(jsonObject.getString("deaths"))));
                    binding.txtBPNum.setText(formatter.format(Long.parseLong(jsonObject.getString("recovered"))));
                    binding.txtNBtoday.setText("+ " + formatter.format(Long.parseLong(jsonObject.getString("todayCases"))));
                    binding.txtTVtoday.setText("+ " + formatter.format(Long.parseLong(jsonObject.getString("todayDeaths"))));
                    Long updatedTime = Long.parseLong(jsonObject.getString("updated"));
                    binding.txtCapNhat.setText(getString(R.string.update)+ " " + convertEpochTime(updatedTime) + (country == null ? "\n" + jsonObject.getString("affectedCountries") + " "+getString(R.string.affect) : ""));
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);

    }

    public String convertEpochTime(Long updatedTime) {
        Date date = new Date(updatedTime);
        DateFormat format = new SimpleDateFormat("HH:mm:ss, dd-MM-yyyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String formattedTime = format.format(date);
        return formattedTime;
    }


    private void resetStyleButton(Button btn) {
        btn.setTextColor(Color.WHITE);
        btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
                mMap.clear();

                MarkerOptions mp = new MarkerOptions();

                mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
                mp.title("Vị trí của tôi");
                mp.snippet("Ở yên đấy đi!");

                mMap.addMarker(mp);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);

            }
        });
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//        LatLng location = new LatLng(latitude, longitude);
//        map.addMarker(new MarkerOptions().position(location)
//                .title("Vị trí của tôi")
//                .snippet("Ở yên đấy đi!"));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
//    }

}
