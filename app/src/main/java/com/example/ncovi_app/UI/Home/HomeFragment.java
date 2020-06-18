package com.example.ncovi_app.UI.Home;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ncovi_app.Adapter.SpinnerAdapter;
import com.example.ncovi_app.FontChangeCrawler;
import com.example.ncovi_app.GeofenceHelper;
import com.example.ncovi_app.Model.Nation;
import com.example.ncovi_app.R;
import com.example.ncovi_app.databinding.FragmentHomeBinding;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

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
public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {


    private static final String TAG = "HomeFragment";

    FragmentHomeBinding binding;
    RequestQueue queue;
    ArrayList<Nation> nationData = new ArrayList<>();
    SpinnerAdapter spinnerAdapter;
    ProgressDialog progressDialog;

    GoogleMap mMap;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private float GEOFENCE_RADIUS = 200;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";

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
        fontChanger.replaceFonts((ViewGroup) binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(getActivity());
        geofenceHelper = new GeofenceHelper(getActivity());

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
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("isAppOpened", true);
                startActivity(intent);
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
        progressDialog.setTitle(getString(R.string.loading) + " " + (country == null ? getString(R.string.world_btn) : country));
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
                    binding.txtCapNhat.setText(getString(R.string.update) + " " + convertEpochTime(updatedTime) + (country == null ? "\n" + jsonObject.getString("affectedCountries") + " " + getString(R.string.affect) : ""));
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

        //LatLng home = new LatLng(10.847047, 106.786939);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 16));

        enableUserLocation();

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng home = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 16));
            }
        });
        addDieaseArea(new LatLng(10.844006, 106.787186));
        //mMap.setOnMapLongClickListener(this);
    }

    private void addDieaseArea(LatLng latLng) {
        addMarker(latLng);
        addCircle(latLng, GEOFENCE_RADIUS);
        addGeofence(latLng, GEOFENCE_RADIUS);
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            //ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                //show user a dialog for displaying why the permisson is needed and then ask for the permisson
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                //have permission
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                //dont have permisson
            }
        }

        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                //have permission
                Toast.makeText(getActivity(), "You can add geofences...", Toast.LENGTH_SHORT).show();
            } else {
                //dont have permisson
                Toast.makeText(getActivity(), "Background location access is neccessary for geofences to trigger...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (Build.VERSION.SDK_INT >= 29){
            //need background permission
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                handleMapLongClick(latLng);
            }else{
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //show user a dialog for displaying why the permisson is needed and then ask for the permisson
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
            }
        }else{
            handleMapLongClick(latLng);
        }
    }



    private void handleMapLongClick(LatLng latLng){
        //mMap.clear();
        addMarker(latLng);
        addCircle(latLng, GEOFENCE_RADIUS);
        addGeofence(latLng, GEOFENCE_RADIUS);
    }

    private void addGeofence(LatLng latLng, float radius) {
        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: Geofence Added...");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String errorMessage = geofenceHelper.getErrorString(e);
                            Log.d(TAG, "onFailure: " + errorMessage);
                        }
                    });
        }
    }

    private void addMarker(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Vùng có dịch!")
                .snippet("Nguy hiểm! Hãy cẩn thận khi di chuyển trong vùng này.");
        mMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latLng, float radius){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255,255,0,0));
        circleOptions.fillColor(Color.argb(60,255,0,0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }

}
