package com.beachpartnerllc.beachpartner.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.beachpartnerllc.beachpartner.R;
import com.beachpartnerllc.beachpartner.activity.TabActivity;
import com.beachpartnerllc.beachpartner.connections.ApiService;
import com.beachpartnerllc.beachpartner.connections.PrefManager;
import com.beachpartnerllc.beachpartner.utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.apptik.widget.MultiSlider;


public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayAdapter<String> dataAdapter;
    ArrayList<String> stateList = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tvMin, tvMax, txtv_gender;
    private MultiSlider age_bar;
    private Spinner spinner_location;
    private ToggleButton btnMale, btnFemale;
    private Button btnSave;
    private SharedPreferences prefs;
    private String location, sgender;
    private int minAge, maxAge;
    private TabActivity tabActivity;
    private String location_change, token;
    private PrefManager prefManager;
    private AlertDialog b;
    private     int myage;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = new PrefManager(getContext()).getToken();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);

        //to show focus on location field when clicking on location icon in bp

        Bundle arguments = getArguments();

        try {
            location_change = arguments.getString("prime_card");
            if (location_change == "location" || location_change.equalsIgnoreCase("location")) {
                spinner_location.setEnabled(true);
                blink();
                dataAdapter.setDropDownViewResource(R.layout.spinner_style_bp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AlertDialog.Builder dialogbar = new AlertDialog.Builder(getActivity());
        View holder = View.inflate(getActivity(), R.layout.progress_dialouge, null);
        dialogbar.setView(holder);
        //dialogbar.setMessage("please wait...");
        dialogbar.setCancelable(false);
        b = dialogbar.create();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof TabActivity) {
            tabActivity = (TabActivity) getActivity();
            tabActivity.setActionBarTitle("Settings");
        }
    }

    private void initView(View view) {

        tvMin = (TextView) view.findViewById(R.id.txtv_minAge);
        tvMax = (TextView) view.findViewById(R.id.txtv_maxAge);
        age_bar = (MultiSlider) view.findViewById(R.id.rangebarOne);
        spinner_location = (Spinner) view.findViewById(R.id.spinner_location_settings);


        txtv_gender = (TextView) view.findViewById(R.id.txtv_gender);

        btnMale = (ToggleButton) view.findViewById(R.id.btnMen);
        btnFemale = (ToggleButton) view.findViewById(R.id.btnWomen);
        btnSave = (Button) view.findViewById(R.id.saveSettings);

        btnFemale.setText("Female");
        btnMale.setText("Male");
        btnFemale.setTextOff("Female");
        btnMale.setTextOff("Male");
        btnFemale.setTextOn("Female");
        btnMale.setTextOn("Male");

        List<String> stateList = AppConstants.getstatelist();
        dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style_bp, stateList);
        spinner_location.setAdapter(dataAdapter);
        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                location = spinner_location.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        //check shared prefvalue
        prefs = new PrefManager(getActivity()).getSettingsData();
        if (prefs != null) {
            location = prefs.getString("location", null);
            if (location != null) {
                int positions = dataAdapter.getPosition(location);
                spinner_location.setSelection(positions);
            }
            sgender = prefs.getString("gender", null);
            minAge = prefs.getInt("minAge", 0);
            maxAge = prefs.getInt("maxAge", 0);
            prefManager = new PrefManager(getActivity());
            myage = prefManager.getAge();
            if(myage>=19) {
                age_bar.setMin(19);
                age_bar.setMax(80);
                tvMin.setText(String.valueOf(19));
                tvMax.setText(String.valueOf(80));
            }else{
                age_bar.setMin(5);
                age_bar.setMax(18);
                tvMin.setText(String.valueOf(5));
                tvMax.setText(String.valueOf(18));
            }
            if (minAge == 0 && maxAge == 0) {
                if(myage>=19) {
                    minAge = 19;
                    maxAge = 80;
                }
                else{
                    minAge = 5;
                    maxAge = 18;
                }

                age_bar.getThumb(0).setValue(minAge).setEnabled(true);
                age_bar.getThumb(1).setValue(maxAge).setEnabled(true);
                tvMin.setText(String.valueOf(minAge));
                tvMax.setText(String.valueOf(maxAge));

            } else {
                age_bar.getThumb(0).setValue(minAge).setEnabled(true);
                age_bar.getThumb(1).setValue(maxAge).setEnabled(true);
                tvMin.setText(String.valueOf(minAge));
                tvMax.setText(String.valueOf(maxAge));
            }


            if (sgender != null) {
                if (sgender.equals("Male")) {
                    txtv_gender.setText("Male");
                    btnMale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnMale.setTextColor(getResources().getColor(R.color.white));
                    btnMale.setChecked(true);
                } else if (sgender.equals("Female")) {
                    txtv_gender.setText("Female");
                    btnFemale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnFemale.setTextColor(getResources().getColor(R.color.white));
                    btnFemale.setChecked(true);
                } else {
                    btnMale.setChecked(true);
                    btnFemale.setChecked(true);
                    txtv_gender.setText("Both");
                    btnMale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnMale.setTextColor(getResources().getColor(R.color.white));
                    btnFemale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnFemale.setTextColor(getResources().getColor(R.color.white));
                }
            }

        }

        //age range bar
        age_bar.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {




                if(myage>=19){
                    age_bar.setMin(19);
                    age_bar.setMax(80);
                    if (thumbIndex == 0) {
                        tvMin.setText(String.valueOf(value));
                    } else {
                        tvMax.setText(String.valueOf(value));
                    }
                }
                else{
                    age_bar.setMin(5);
                    age_bar.setMax(18);
                    if (thumbIndex == 0) {
                        tvMin.setText(String.valueOf(value));
                    } else {
                        tvMax.setText(String.valueOf(value));
                    }
                }

            }
        });


        // attach click listener to folding cell


        //button Men
        btnMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (btnFemale.isChecked() && isChecked) {
                    txtv_gender.setText("Both");
                    btnFemale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnMale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnFemale.setTextColor(getResources().getColor(R.color.white));
                    btnMale.setTextColor(getResources().getColor(R.color.white));
                } else if (isChecked) {
                    txtv_gender.setText("Male");
                    btnMale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnMale.setTextColor(getResources().getColor(R.color.white));
                } else if (!isChecked) {
                    txtv_gender.setText("Female");
                    btnMale.setBackground(getResources().getDrawable(R.color.imgBacgnd));
                    btnMale.setTextColor(getResources().getColor(R.color.black));
                    btnFemale.setChecked(true);
                    btnFemale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnFemale.setTextColor(getResources().getColor(R.color.white));
                }


            }
        });

        //button Women
        btnFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (btnMale.isChecked() && isChecked) {
                    txtv_gender.setText("Both");
                    btnFemale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnMale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnFemale.setTextColor(getResources().getColor(R.color.white));
                    btnMale.setTextColor(getResources().getColor(R.color.white));
                } else if (isChecked) {
                    txtv_gender.setText("Female");
                    btnFemale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnFemale.setTextColor(getResources().getColor(R.color.white));
                } else if (!isChecked) {
                    txtv_gender.setText("Male");
                    btnFemale.setBackground(getResources().getDrawable(R.color.imgBacgnd));
                    btnFemale.setTextColor(getResources().getColor(R.color.black));
                    btnMale.setChecked(true);
                    btnMale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnMale.setTextColor(getResources().getColor(R.color.white));
                }


            }
        });


        //add data to shared preference
        //play button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.show();
                //location    =   spinner_location.getText().toString().trim();
                sgender = txtv_gender.getText().toString();
                minAge = Integer.parseInt(tvMin.getText().toString().trim());
                maxAge = Integer.parseInt(tvMax.getText().toString().trim());
                JSONObject jsonCity = new JSONObject();
                try {
                    jsonCity.put("city", location);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateCity(jsonCity);
                new PrefManager(getActivity()).saveSettingData(location, sgender, false, minAge, maxAge);
                getActivity().onBackPressed();

            }

        });
    }

    private void updateCity(JSONObject object) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ApiService.REQUEST_METHOD_POST, ApiService.UPDATE_USER_CITY , object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            b.dismiss();
                            Toast.makeText(tabActivity, "Updated current settings", Toast.LENGTH_SHORT).show();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                b.dismiss();
                Toast.makeText(getActivity(), "Failed to update your settings", Toast.LENGTH_LONG).show();

                String json = null;
                Log.d("error--", error.toString());
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 401:
                            json = new String(response.data);
                            json = trimMessage(json, "detail");
                            if (json != null) {
                                Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;

                        default:
                            break;
                    }
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Log.d("Request", jsonObjectRequest.toString());
        requestQueue.add(jsonObjectRequest);

    }

    private String trimMessage(String json, String detail) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(detail);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    private void blink() {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(30);
        spinner_location.startAnimation(anim);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        new PrefManager(getActivity()).saveSettingData(location, sgender, false, minAge, maxAge);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}