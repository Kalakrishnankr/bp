package com.beachpartnerllc.beachpartner.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.beachpartnerllc.beachpartner.CircularImageView;
import com.beachpartnerllc.beachpartner.CustomTextView;
import com.beachpartnerllc.beachpartner.MyInterface;
import com.beachpartnerllc.beachpartner.R;
import com.beachpartnerllc.beachpartner.activity.TabActivity;
import com.beachpartnerllc.beachpartner.adpters.BenefitListItemAdapter;
import com.beachpartnerllc.beachpartner.adpters.BlueBProfileAdapter;
import com.beachpartnerllc.beachpartner.adpters.SubscriptionAdapter;
import com.beachpartnerllc.beachpartner.adpters.TouristSpotCardAdapter;
import com.beachpartnerllc.beachpartner.calendar.compactcalendarview.CompactCalendarView;
import com.beachpartnerllc.beachpartner.calendar.compactcalendarview.domain.Event;
import com.beachpartnerllc.beachpartner.cardstackview.CardStackView;
import com.beachpartnerllc.beachpartner.cardstackview.SwipeDirection;
import com.beachpartnerllc.beachpartner.connections.ApiService;
import com.beachpartnerllc.beachpartner.connections.PrefManager;
import com.beachpartnerllc.beachpartner.models.BenefitModel;
import com.beachpartnerllc.beachpartner.models.BpFinderModel;
import com.beachpartnerllc.beachpartner.models.SingleSubscriptionModel;
import com.beachpartnerllc.beachpartner.models.SubscriptionItemsModels;
import com.beachpartnerllc.beachpartner.models.SubscriptonPlansModel;
import com.beachpartnerllc.beachpartner.models.SwipeResultModel;
import com.beachpartnerllc.beachpartner.utils.AppConstants;
import com.beachpartnerllc.beachpartner.utils.CheckPlan;
import com.beachpartnerllc.beachpartner.utils.SubClickInterface;
import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.apptik.widget.MultiSlider;

import static com.beachpartnerllc.beachpartner.utils.CheckPlan.selectedIndex;


public class BPFinderFragment extends Fragment implements MyInterface,SubClickInterface{


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String TAG = "BPFinderFragment";
    public ToggleButton btnMale, btnFemale;
    public boolean reverseCount = false;
    OkHttpClient mClient = new OkHttpClient();
    private ProgressBar progressBar;
    private CardStackView cardStackView;
    private TouristSpotCardAdapter adapter;
    private SubscriptionAdapter subscriptionAdapter;
    private RelativeLayout rr;
    private CoordinatorLayout llv;
    private ImageView imgv_profilepic,imgv_rvsecard,imgv_location,imgv_highfi,btnPlay,toggle;
    private TextView tvmonth, tvMin, tvMax, txtv_gender, text_nocard,type_user,tv_title;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Spinner spinner_location;
    private MultiSlider age_bar;
    private FoldingCell fc;
    private LinearLayout llvFilter;
    private ArrayAdapter<String> dataAdapter;
    private ImageButton showPreviousMonthButton, showNextMonthButton;
    private Switch sCoach;
    private LinearLayout topFrameLayout;
    private ArrayList<String> stateList = new ArrayList<>();
    private RelativeLayout rrvBottom;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    private boolean isbpActive = false;
    private boolean isPartner = false;
    private View view;
    private int bp_ageInt;
    private SharedPreferences prefs;
    private String location, sgender, profileImage, topThreeFinish,usertype;
    private Boolean isCoach;
    private int minAge, maxAge;
    private CompactCalendarView compactCalendar;
    private RecyclerView rcv_bpProfiles;
    private BlueBProfileAdapter blueBProfileAdapter;
    private CardView empty_card;
    private CircularImageView profilePic;

    private View layoutAlert;
    private LinearLayout topHeaderLayout;
    private TextView headerTitle,tv_price,tv_regPrice;
    private Button btnBuy,btnProceed,btnCancel;
    private RecyclerView rcview_sub_item;
    private RelativeLayout premius_header;
    private List<BenefitModel>benefitModelList = new ArrayList<>();
    private BenefitListItemAdapter benefitItemAdapter;

    private String token, user_id, reqPersonId, deviceId, fcmToken,userSubscription;
    private ArrayList<BpFinderModel> allCardList = new ArrayList<BpFinderModel>();
    private ArrayList<SwipeResultModel> bluebpListSecond = new ArrayList<>();
    private ArrayList<BpFinderModel> hifiList = new ArrayList<BpFinderModel>();
    private ArrayList<BpFinderModel> noLikes = new ArrayList<BpFinderModel>();
    private ArrayList<BpFinderModel> bluebpList = new ArrayList<>();
    private List<SubscriptonPlansModel>plansModelList = new ArrayList<>();
    private List<SubscriptionItemsModels>userSubList = new ArrayList<>();
    private List<SubscriptionItemsModels>userAddonsList= new ArrayList<>();
    private List<SubscriptonPlansModel> userPlanList = new ArrayList<>();
    private String item_location;
    private List<Event> personEventList = new ArrayList<Event>();
    private TabActivity tabActivity;
    private BpFinderModel cModel;
    private CustomTextView topFinishes_One, topFinishes_Two, topFinishes_Three;
    private PrefManager prefManager;
    SubscriptonPlansModel splanModels;
    AlertDialog b;

    public BPFinderFragment() {
    }

    @SuppressLint("ValidFragment")
    public BPFinderFragment(boolean isBPActive, boolean isPartnerFinder) {
        isbpActive = isBPActive;
        isPartner = isPartnerFinder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getActivity());

    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (isbpActive) {
            view = inflater.inflate(R.layout.fragment_bpfinder, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_bpfinder1, container, false);
        }
        prefManager = new PrefManager(getActivity());
        token       = prefManager.getToken();
        user_id     = prefManager.getUserId();
        userSubscription = prefManager.getSubscriptionType();
        usertype  = prefManager.getUserType();

        AlertDialog.Builder dialogbar=new AlertDialog.Builder(getActivity());
        View holder=View.inflate(getActivity(), R.layout.progress_dialouge, null);
        dialogbar.setView(holder);
        //dialogbar.setMessage("please wait...");
        dialogbar.setCancelable(false);
        b = dialogbar.create();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //dialogbar.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);

        allCardList.clear();
        //
        setUp(view);
        getPreferences();
        btnFemale.setText("Female");
        btnMale.setText("Male");
        btnFemale.setTextOff("Female");
        btnMale.setTextOff("Male");
        btnFemale.setTextOn("Female");
        btnMale.setTextOn("Male");
        //reload card
        reload();
        //get profile pic from preference
        profileImage = new PrefManager(getActivity()).getProfilePic();

        Bundle data = getArguments();
        if (data != null) {
            getBpProfiles();//Method for getting next strip
            if (data.containsKey(AppConstants.HI_FI_LIST))
                hifiList = data.getParcelableArrayList(AppConstants.HI_FI_LIST);
            if (data.containsKey(AppConstants.NO_LIKES_LIST))
                noLikes = data.getParcelableArrayList(AppConstants.NO_LIKES_LIST);
            if (data.containsKey(AppConstants.BLUE_BP_LIST))
                bluebpList = data.getParcelableArrayList(AppConstants.BLUE_BP_LIST);

            else if (hifiList != null && hifiList.size() > 0) {
                text_nocard.setText("No more hi-fi's");
                addUserList(hifiList);
            } else if (bluebpList != null && bluebpList.size() > 0) {
                addUserList(bluebpList);
            } else if (noLikes != null && noLikes.size() > 0) {
                text_nocard.setText("No more Likes");
                addUserList(noLikes);
            } else if (data.containsKey(AppConstants.BP_PROFILE)) {
                text_nocard.setText("Please tap the BP logo for more cards");
                cModel = data.getParcelable(AppConstants.BP_PROFILE);
                addASingleUser(cModel);
            } else {
                if (minAge == 0 && maxAge == 0) {
                    getAllCards(null, "", false, 5, 35);
                } else {
                    getAllCards(location, sgender, isCoach, minAge, maxAge);
                }
            }
            //From 20+ Likes
        }
        return view;
    }

    private void addASingleUser(BpFinderModel bpFinderModel) {

        initializeUserAdapter();
        if (adapter != null && bpFinderModel != null) {
            adapter.clear();
            adapter.add(bpFinderModel);
            adapter.notifyDataSetChanged();
        }
    }

    private void addUserList(ArrayList<BpFinderModel> allCardList) {
        initializeUserAdapter();
        adapter.addAll(allCardList);
        adapter.notifyDataSetChanged();
    }

    private void initializeUserAdapter() {
        if (adapter == null) {
            adapter = new TouristSpotCardAdapter(getContext(), this);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof TabActivity) {
            tabActivity = (TabActivity) getActivity();
            if (!isPartner) {
                tabActivity.getSupportActionBar().setTitle((R.string.app_name));
            }
            //tabActivity.getSupportActionBar().setTitle((R.string.app_name));
            //tabActivity.setActionBarTitle("Beach Partner");
        }
        getuserSubscriptions();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferences();
    }

    private void getPreferences() {
        //check shared prefvalue
        prefs = new PrefManager(getActivity()).getSettingsData();
        if (prefs != null) {

            location = prefs.getString("location", null);
            sgender = prefs.getString("gender", null);
            isCoach = prefs.getBoolean("isCoachActive", false);
            minAge = prefs.getInt("minAge", 0);
            maxAge = prefs.getInt("maxAge", 0);

            tvMin.setText(String.valueOf(minAge));
            tvMax.setText(String.valueOf(maxAge));
            age_bar.getThumb(0).setValue(minAge).setEnabled(true);
            age_bar.getThumb(1).setValue(maxAge).setEnabled(true);
            if (location != null) {
                int positions = dataAdapter.getPosition(location);
                spinner_location.setSelection(positions);
            }
            sCoach.setChecked(isCoach);

            if (sgender != null) {
                if (sgender.equals("Male")) {
                    txtv_gender.setText("Male");
                    btnMale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnMale.setTextColor(getResources().getColor(R.color.white));
                    btnMale.setChecked(true);
                    btnFemale.setBackground(getResources().getDrawable(R.color.imgBacgnd));
                    btnFemale.setTextColor(getResources().getColor(R.color.black));
                } else if (sgender.equals("Female")) {
                    txtv_gender.setText("Female");
                    btnFemale.setBackground(getResources().getDrawable(R.color.menubar));
                    btnFemale.setTextColor(getResources().getColor(R.color.white));
                    btnFemale.setChecked(true);
                    btnMale.setBackground(getResources().getDrawable(R.color.imgBacgnd));
                    btnMale.setTextColor(getResources().getColor(R.color.black));
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
    }

    public void setUp(final View view) {

        progressBar     = view.findViewById(R.id.activity_main_progress_bar);
        rr              = view.findViewById(R.id.rr);
        llv             = view.findViewById(R.id.llMoreinfo);
        imgv_profilepic = view.findViewById(R.id.img_profile);
        cardStackView   = view.findViewById(R.id.activity_main_card_stack_view);
        tvmonth         = view.findViewById(R.id.month_name);
        toggle          = view.findViewById(R.id.toggle);
        rrvBottom       = view.findViewById(R.id.rrv_bottomMenus);
        empty_card      = view.findViewById(R.id.no_cards);
        profilePic      = view.findViewById(R.id.profilePic);

        //Layout for filters
        llvFilter       = view.findViewById(R.id.llFilter);
        tvMin           = view.findViewById(R.id.txtv_minAge);
        tvMax           = view.findViewById(R.id.txtv_maxAge);
        age_bar         = view.findViewById(R.id.rangebar);
        spinner_location= view.findViewById(R.id.spinner_location);
        txtv_gender     = view.findViewById(R.id.txtv_gender);
        btnMale         = view.findViewById(R.id.btnMen);
        btnFemale       = view.findViewById(R.id.btnWomen);
        btnPlay         = view.findViewById(R.id.imgPlay);
        fc              = view.findViewById(R.id.folding_cell);
        topFrameLayout  = view.findViewById(R.id.frmeOne);

        type_user       = view.findViewById(R.id.txtv_typeUser);
        tv_title        = view.findViewById(R.id.txtv_title);
        showPreviousMonthButton = view.findViewById(R.id.prev_button);
        showNextMonthButton =     view.findViewById(R.id.next_button);
        sCoach          = view.findViewById(R.id.swich_coach);

        compactCalendar = view.findViewById(R.id.compactcalendar_view);
        compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        collapsingToolbarLayout = view.findViewById(R.id.CollapsingToolbarLayout1);

        imgv_rvsecard   = view.findViewById(R.id.ic_rvsecard);
        imgv_highfi     = view.findViewById(R.id.ic_high);
        imgv_location   = view.findViewById(R.id.ic_location);
        topFinishes_One = view.findViewById(R.id.topOne_finishes);
        topFinishes_Two = view.findViewById(R.id.topTwo_finishes);
        topFinishes_Three=view.findViewById(R.id.topThree_finishes);
        text_nocard     = view.findViewById(R.id.text_nocard);
        rcv_bpProfiles  = view.findViewById(R.id.rrv_topbpProfiles);
        //recycler for top bp profiles
        LinearLayoutManager lmnger = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcv_bpProfiles.setLayoutManager(lmnger);
        rcv_bpProfiles.setHasFixedSize(true);

        stateList.clear();
        List<String>stateList = AppConstants.getstatelist();

        showPreviousMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendar.showPreviousMonth();
            }
        });

        showNextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendar.showNextMonth();
            }
        });

        //choose Location
        if (getActivity() != null) {
            dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style_bp, stateList);
            spinner_location.setAdapter(dataAdapter);
        }
        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String Slocaton = spinner_location.getSelectedItem().toString().trim();
                location = Slocaton.replaceAll("\\s", "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        //age range bar
        age_bar.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                int myage = prefManager.getAge();
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
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });
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
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    //new PrefManager(getContext()).savePageno(-1);
                    getBpProfiles();
                }
                location = spinner_location.getSelectedItem().toString();
                sgender = txtv_gender.getText().toString();
                isCoach = sCoach.isChecked();
                minAge = Integer.parseInt(tvMin.getText().toString().trim());
                maxAge = Integer.parseInt(tvMax.getText().toString().trim());

                if (sgender.equals("Both")) {
                    sgender = "";
                }

                //changed as per request from rp on 2-05-2018 9:07
                //new PrefManager(getActivity()).saveSettingData(location, sgender, isCoach, minAge, maxAge);
                llvFilter.setVisibility(View.GONE);
                rr.setVisibility(View.VISIBLE);
                rrvBottom.setVisibility(View.VISIBLE);

                //check top bp strip **if its active only in bpfinder page(ie,BPFinder Fragment),it's disabled on calendar find Partner page
                if (isbpActive) {
                    topFrameLayout.setVisibility(View.GONE);

                    //changed as per request from rp on 2-05-2018 9:07
//                    new PrefManager(getActivity()).saveSettingData(location, sgender, isCoach, minAge, maxAge);
                    llvFilter.setVisibility(View.GONE);
                    rr.setVisibility(View.VISIBLE);


                } else {
                    if (isPartner) {
                        topFrameLayout.setVisibility(View.GONE);

                    } else {
                        topFrameLayout.setVisibility(View.VISIBLE);
                        if (bluebpList.size() != 0) {
                            if (getActivity() != null) {
                               /* blueBProfileAdapter = new BlueBProfileAdapter(getActivity(), bluebpList);
                                rcv_bpProfiles.setAdapter(blueBProfileAdapter);*/
                            }
                        }
                    }

                }
                progressBar.setVisibility(View.VISIBLE);
                getAllCards(location, sgender, isCoach, minAge, maxAge);

            }
        });


        //Methods for card swipe

        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
               // Log.d("CardStackView", "onCardDragging");
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                //abraham 08-03-2018

                reverseCount = true;
                imgv_rvsecard.setImageResource(R.drawable.ic_backcard);
               // Log.d("CardStackView", "onCardSwiped: " + direction.toString());
               // Log.d("CardStackView", "topIndex: " + cardStackView.getTopIndex());
                //Methods for swipe card kalakrishnan 06/04/2018
                if (direction.toString().equalsIgnoreCase("Right")) {
                    //Toast.makeText(getActivity(), "You right swiped : "+reqPersonId, Toast.LENGTH_SHORT).show();
                    //Api for Right swipe/like
                    if (reqPersonId != null) {
                        cardRightSwiped(reqPersonId);
                    }

                } else if (direction.toString().equalsIgnoreCase("Left")) {
                    //Toast.makeText(getActivity(), "You Left swiped : "+reqPersonId, Toast.LENGTH_SHORT).show();
                    if (reqPersonId != null) {
                        cardLeftSwiped(reqPersonId);
                    }

                } else if (direction.toString().equalsIgnoreCase("Top")) {
                    //Toast.makeText(getActivity(), "You Hified : "+reqPersonId, Toast.LENGTH_SHORT).show();
                    if (reqPersonId != null) {
                        cardHifiSwiped(reqPersonId);
                    }
                    //r.put(reqPersonId);
//                    String r = fcmToken;
//                    String uName = new PrefManager(getContext()).getUserName();
//                    if (fcmToken != null && !fcmToken.equalsIgnoreCase("null") && uName != null && !uName.equalsIgnoreCase("null")) {
//                        sendMessage(r, "BeachPartner", uName + " sent you a high five", "");
//                    }
                }

                if (cardStackView.getTopIndex() == adapter.getCount()) {
                   // Log.d("CardStackView", "Paginate: no card displayed " + cardStackView.getTopIndex());
                    noCrads();
                    // paginate();
                }

            }

            @Override
            public void onCardReversed() {
                reverseCount = false;
                //Log.d("CardStackView", "onCardReversed");
            }

            @Override
            public void onCardMovedToOrigin() {
                //Log.d("CardStackView", "onCardMovedToOrigin");
            }

            @Override
            public void onCardClicked(int index) {
                //Log.d("CardStackView", "onCardClicked: " + index);
            }
        });


        //Calendar
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rr.setVisibility(View.VISIBLE);
                llv.setVisibility(View.GONE);
                rrvBottom.setVisibility(View.VISIBLE);

            }
        });

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> bookingsMap = compactCalendar.getEvents(dateClicked);
                if (bookingsMap != null) {
                    for (int i = 0; i < bookingsMap.size(); i++) {
                        // Date date=new Date(bookingsFromMap.get(i).getTimeInMillis());
                        if ((DatetoMilli(dateClicked)) == (DatetoMilli(new Date(bookingsMap.get(i).getTimeInMillis())))) {

                        }
                    }
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
                tvmonth.setText(dateFormatForMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));
            }


        });


        // String newFormat= "dd MMMM";
        tvmonth.setText(dateFormatForMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));

        //Card reverse onclick
        imgv_rvsecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = new PrefManager(getActivity()).getReverseCardID();
                actionReverse(id);
            }
        });

        //Button Click for hifi
        imgv_highfi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeUp();
            }
        });
        //Get Location
        imgv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

    }
    private void actionReverse(String id) {
        if (!usertype.equalsIgnoreCase(AppConstants.USER_TYPE_COACH)) {
            if (userSubscription != null) {
                if (CheckPlan.isPlanAvailable(plansModelList, userSubscription, AppConstants.BENIFIT_CODE_B17)) {
                    userPlanList.clear();
                    //Toast.makeText(tabActivity, "Selecetd index"+selectedIndex, Toast.LENGTH_SHORT).show();
                    if (plansModelList.get(selectedIndex).getPlanName().equalsIgnoreCase(userSubscription)) {
                        reverse();
                        cardReverse(id);
                        reversecard();
                    } else {
                        if (plansModelList.get(2).getPlanName().equalsIgnoreCase(userSubscription)) {
                            reverse();
                            cardReverse(id);
                            reversecard();
                        } else if (plansModelList.get(3).getPlanName().equalsIgnoreCase(userSubscription)) {
                            reverse();
                            cardReverse(id);
                            reversecard();
                        } else {
                            for (int i = selectedIndex; i < plansModelList.size(); i++) {
                                userPlanList.add(plansModelList.get(i));
                            }
                            showAlertDialouge();
                        }
                    }

                }


               /* if (userSubscription.equalsIgnoreCase("FREE") || userSubscription.equalsIgnoreCase("LITE")) {
                    showAlertDialouge();
                } else {

                }*/
            }
        }else {
            reverse();
            cardReverse(id);
            reversecard();
        }

    }

    private void reversecard() {
        cardStackView.setVisibility(View.VISIBLE);
        empty_card.setVisibility(View.INVISIBLE);
    }

    private void noCrads() {
        if (getActivity() != null) {
            cardStackView.setVisibility(View.INVISIBLE);
            rr.setVisibility(View.VISIBLE);
            empty_card.setVisibility(View.VISIBLE);

//            donot delete changed as per request from client 6/8/2018


//            if (profileImage != null && !profileImage.equals("null")) {
//                Glide.with(getActivity()).load(profileImage).into(profilePic);
//            }else {
//                Glide.with(getActivity()).load(R.drawable.user_img).into(profilePic);
//            }
            profilePic.setVisibility(View.INVISIBLE);
        }
    }

    //GEt all cards
    private void getAllCards(String location, String sgender, Boolean isCoach, int minAge, int maxAge) {
        allCardList.clear();
        if(adapter!=null){
            adapter.clear();       //adapter cleared inorder to clear the list when returning from settings
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ApiService.REQUEST_METHOD_GET, ApiService.SEARCH_USER_CARD + "?includeCoach=" + isCoach + "&location="+ location + "&minAge=" + minAge + "&maxAge=" + maxAge + "&gender=" + sgender + "&hideConnectedUser=true&hideLikedUser=true&hideRejectedConnections=true&hideBlockedUsers=true", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                       Log.d(TAG, "onResponse all cards: " + response.toString());
                        Type listType = new TypeToken<List<BpFinderModel>>() {}.getType();
                        allCardList = new Gson().fromJson(response.toString(), listType);
                        addCards();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String json = null;
                //Log.d("error--", error.toString());
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 401:
                            json = new String(response.data);
                            json = trimMessage(json, "detail");
                            if (json != null) {
                                //  Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;

                        default:
                            break;
                    }
                }else{
                    Toast.makeText(tabActivity, R.string.no_internet, Toast.LENGTH_SHORT).show();
                }



            }
        }) {

            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        if (getActivity() != null) {
            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            Log.d("Request", jsonArrayRequest.toString());
            jsonArrayRequest.setRetryPolicy(policy);
            requestQueue.add(jsonArrayRequest);
        }


    }

    private void addCards() {
        if (allCardList.size() > 0) {
            progressBar.setVisibility(View.INVISIBLE);
            paginate();
        } else {
            if (bluebpList.size() > 0 || noLikes.size() > 0 || hifiList.size() > 0) {
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                noCrads();
                imgv_rvsecard.setClickable(false);
                b.dismiss();
            }

        }
    }

    private void paginate() {

        if (getActivity() != null) {
            if (allCardList.size() != 0) {
                if (adapter != null) {
                    cardStackView.setPaginationReserved();
                    adapter.addAll(allCardList);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    //Method for hifi

    //Method for right swipe
    private void cardRightSwiped(String reqPersonId) {
        b.show();
        JsonObjectRequest request = new JsonObjectRequest(ApiService.REQUEST_METHOD_POST, ApiService.RIGHT_SWIPE_REQUEST_SEND + reqPersonId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {

                            JSONObject obj = null;
                            try {
                                SwipeResultModel swipeResultModel =
                                        new Gson().fromJson(response.toString(), SwipeResultModel.class);
                                obj = response.getJSONObject("connectedUser");
                                Type type = new TypeToken<BpFinderModel>(){}.getType();
                                BpFinderModel finderModel = new Gson().fromJson(obj.toString(),type);
                                if (finderModel != null) {
                                    getBpProfiles();
                                    //Log.d(TAG, "onResponse: Reverse Card ID: "+swipeResultModel.getId());
                                    if (swipeResultModel.getId() != null) {
                                        new PrefManager(getActivity()).saveReverseCardId(swipeResultModel.getId());
                                    }
                                    cModel = finderModel;
                                    if (swipeResultModel.isActive())
                                        showAlertDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                b.dismiss();
                            }


                            /*SwipeResultModel swipeResultModel =
                                    new Gson().fromJson(response.toString(), SwipeResultModel.class);
                            if (getActivity() != null) {
                                if (swipeResultModel != null) {
                                    getBpProfiles();
                                    if (swipeResultModel.getId() != null) {
                                        new PrefManager(getActivity()).saveReverseCardId(swipeResultModel.getId());
                                    }
                                    cModel = swipeResultModel.getBpFinderModel();
                                    if (swipeResultModel.isActive())
                                        showAlertDialog();
                                }
                            }*/


                        }
                        b.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                b.dismiss();
                String json = null;
                //Log.d("error--", error.toString());
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                // Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 401:
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                //Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 404:
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                //Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;

                        default:
                            break;
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;

            }

        };
        if (getActivity() != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            Log.d("RequestSend", request.toString());
            requestQueue.add(request);
        }
        else{
            b.dismiss();
        }

    }

    //Method for card left swiped
    private void cardLeftSwiped(String reqPersonId) {
        b.show();
        JsonObjectRequest jrequest = new JsonObjectRequest(ApiService.REQUEST_METHOD_POST, ApiService.LEFT_SWIPE_DISLIKE + reqPersonId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (getActivity() != null) {
                    if (response != null) {
                        getBpProfiles();
                        try {
                            if (response.has(AppConstants.ID)) {
                                {
                                    SwipeResultModel swipeResultModel =
                                            new Gson().fromJson(response.toString(), SwipeResultModel.class);
                                    JSONObject obj = response.getJSONObject("connectedUser");
                                    Type type = new TypeToken<BpFinderModel>(){}.getType();
                                    BpFinderModel finderModel = new Gson().fromJson(obj.toString(),type);
                                    if (finderModel != null) {
                                        getBpProfiles();
                                        //Log.d(TAG, "onResponse: 2nd : "+swipeResultModel.getId());
                                        if (swipeResultModel.getId() != null) {
                                            new PrefManager(getActivity()).saveReverseCardId(swipeResultModel.getId());
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            b.dismiss();
                        }

                    }
                }
                b.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
               // Log.d("error--", error.toString());
                b.dismiss();
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                //Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 401:
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                // Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 404:
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                //Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;

                        default:
                            break;
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;

            }

        };
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            Log.d("RejectRequest", queue.toString());
            queue.add(jrequest);
        }
        else{
            b.dismiss();
        }

    }


    //Api for get individual events for a particular person

    private void cardHifiSwiped(String reqPersonId) {
        b.show();
        JsonObjectRequest requests = new JsonObjectRequest(ApiService.REQUEST_METHOD_POST, ApiService.HIFI_SWIPE_UP + reqPersonId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (getActivity() != null) {
                            if (response != null) {

                               /* getBpProfiles();
                                try {
                                    //  Toast.makeText(getActivity(), "ID :" + response.getString("id"), Toast.LENGTH_SHORT).show();
                                    new PrefManager(getActivity()).saveReverseCardId(response.getString("id"));
                                    String status = response.getString("status").toString().trim();
                                    if (status.equals("New")) {
                                        Log.d("request send", status);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/
                               /* SwipeResultModel swipeResultModelhifi =
                                        new Gson().fromJson(response.toString(), SwipeResultModel.class);
                                if (getActivity() != null) {
                                    if (swipeResultModelhifi != null) {
                                        getBpProfiles();
                                        if (swipeResultModelhifi.getId() != null) {
                                            new PrefManager(getActivity()).saveReverseCardId(swipeResultModelhifi.getId());
                                        }
                                        cModel = swipeResultModelhifi.getBpFinderModel();
                                        if (swipeResultModelhifi.isActive())
                                            showAlertDialog();
                                    }
                                }*/
                                JSONObject obj = null;
                                try {
                                    SwipeResultModel swipeResultModel =
                                            new Gson().fromJson(response.toString(), SwipeResultModel.class);
                                    obj = response.getJSONObject("connectedUser");
                                    Type type = new TypeToken<BpFinderModel>(){}.getType();
                                    BpFinderModel finderModel = new Gson().fromJson(obj.toString(),type);
                                    if (finderModel != null) {
                                        getBpProfiles();
                                        if (swipeResultModel.getId() != null) {
                                            new PrefManager(getActivity()).saveReverseCardId(swipeResultModel.getId());
                                        }
                                        cModel = finderModel;
                                        if (swipeResultModel.isActive())
                                            showAlertDialog();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    b.dismiss();
                                }
                            }
                            b.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String json = null;
                b.dismiss();

                //Log.d("error--", error.toString());
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            if (!usertype.equalsIgnoreCase(AppConstants.USER_TYPE_COACH)) {
                                //showAlertDialouge();
                            }
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                //Toast.makeText(getContext(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 401:
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                //Toast.makeText(getContext(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 404:
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                // Toast.makeText(getContext(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;

                        default:
                            break;
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;

            }

        };
        if (getActivity() != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            Log.d("HifiRequestSend", requests.toString());
            requestQueue.add(requests);
        }
        else{
            b.dismiss();
        }


    }

    //Card Reverse Api
    private void cardReverse(String id) {
        b.show();
        JsonObjectRequest request = new JsonObjectRequest(ApiService.REQUEST_METHOD_POST, ApiService.REVERSE_SWIPE_CARD + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            SwipeResultModel swipeResultModel =
                                    new Gson().fromJson(response.toString(), SwipeResultModel.class);

                            if (swipeResultModel != null) {
                                getBpProfiles();
                                cModel = swipeResultModel.getBpFinderModel();
                            }
                        }
                        b.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String json = null;
                b.dismiss();
                // Log.d("error--", error.toString());
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                //Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 401:
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                //Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 404:
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                //Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;

                        default:
                            break;
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;

            }

        };
        if (getActivity() != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            //Log.d("RequestSend", request.toString());
            requestQueue.add(request);
        }
        else{
            b.dismiss();
        }

    }

    public void getIndividualEvents(String reqPersonId) {
        personEventList.clear();
        JsonArrayRequest jsonArrayRqst = new JsonArrayRequest(ApiService.REQUEST_METHOD_GET, ApiService.GET_USER_EVENTS + reqPersonId, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            for (int i = 0; i < response.length(); i++) {

                                try {

                                    JSONObject obj = response.getJSONObject(i);

                                    JSONObject jsonObject = obj.getJSONObject("event");
                                    Event eventModel = new Event();
                                    eventModel.setEventId(jsonObject.getString("id"));
                                    eventModel.setEventName(jsonObject.getString("eventName"));
                                    eventModel.setData(jsonObject.getString("eventDescription"));
                                    eventModel.setEventLocation(jsonObject.getString("eventLocation"));
                                    eventModel.setEventVenue(jsonObject.getString("eventVenue"));
                                    eventModel.setEventStartDate(jsonObject.getLong("eventStartDate"));
                                    eventModel.setTimeInMillis(Long.parseLong(jsonObject.getString("eventStartDate")));
                                    eventModel.setEventEndDate(jsonObject.getLong("eventEndDate"));
                                    JSONArray dateArray = obj.getJSONArray("eventDates");
                                    long [] mydatearray = new long[dateArray.length()];
                                    if (dateArray.length()>0 ) {
                                        for (int j = 0; j < dateArray.length() ; j++) {
                                            mydatearray [j]= dateArray.getLong(j);
                                        }
                                        eventModel.setEventDates(mydatearray);
                                    }
                                    personEventList.add(eventModel);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setupPersonCalendar();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                Log.d("error--", error.toString());
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 401:
                            json = new String(response.data);
                            json = trimMessage(json, "detail");
                            if (json != null) {
                                //   Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;

                        default:
                            break;
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        if (getActivity() != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            //Log.d("Request", jsonArrayRqst.toString());
            requestQueue.add(jsonArrayRqst);
        }

    }

    //Calendar for individual persons
    private void setupPersonCalendar() {
        if (personEventList != null && personEventList.size() > 0) {
            for (int i = 0; i < personEventList.size(); i++) {
                compactCalendar.addEvent(personEventList.get(i));
            }
        }
    }

    //Method for getting bluebpstrips
    private void getBpProfiles() {

        /*if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }*/

        VolleyLog.DEBUG = true;
        JsonArrayRequest jsonRequest = new JsonArrayRequest(ApiService.REQUEST_METHOD_GET, ApiService.GET_SUBSCRIPTIONS + "?subscriptionType=BlueBP&hideConnectedUser=true&hideLikedUser=true&hideRejectedConnections=true&hideBlockedUsers=true", null, new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //Log.d(TAG, "onResponse: " + response.toString());

                        if (response.length() > 0) {
                            Type listType = new TypeToken<List<SwipeResultModel>>() {
                            }.getType();
                            bluebpListSecond = new Gson().fromJson(response.toString(), listType);
                            setUpBlueBPStrips();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String json = null;
                //Log.d("error--", error.toString());
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 401:
                            json = new String(response.data);
                            json = trimMessage(json, "detail");
                            if (json != null) {
                                //  Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;

                        default:
                            break;
                    }
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        if (getActivity() != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            //Log.d("Request", jsonRequest.toString());
            requestQueue.add(jsonRequest);
        }

    }

    private void setUpBlueBPStrips() {
        if (bluebpListSecond != null && bluebpListSecond.size() > 0) {
            if (getActivity() != null) {
                blueBProfileAdapter = new BlueBProfileAdapter(getActivity(), bluebpListSecond);
                rcv_bpProfiles.setAdapter(blueBProfileAdapter);
                //   blueBProfileAdapter.setOnRecyclerOnClickListener(this);
                blueBProfileAdapter.notifyDataSetChanged();
            }
        }
    }

    private void reload() {
        cardStackView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null && cardStackView != null) {
                    if (adapter == null) {
                        adapter = createTouristSpotCardAdapter();
                    }
                    cardStackView.setAdapter(adapter);
                    //  adapter.notifyDataSetChanged();
                    cardStackView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, 1000);
    }

    private TouristSpotCardAdapter createTouristSpotCardAdapter() {

        if (allCardList != null) {
            adapter = new TouristSpotCardAdapter(getActivity().getApplicationContext(), this);
            adapter.addAll(allCardList);

        }
        return adapter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    //Method for card reverse

    @Override
    public void addView(String url, String firtName,String lastName,String finder_age,String type) {
        rr.setVisibility(View.GONE);
        llv.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(url).into(imgv_profilepic);
        if (finder_age != null && !finder_age.isEmpty()) {
            long millisecond = Long.parseLong(finder_age);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date datef = new Date(millisecond);
            Calendar today = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            cal.setTime(datef);
            int age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < cal.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            bp_ageInt = new Integer(age);
        }
       // Log.d(TAG, "age FRom Server: "+player_age+"age from Calculation:"+age);
        //collapsingToolbarLayout.setTitle(nm+", "+bp_ageInt);
        tv_title.setText(firtName+" "+lastName+", "+bp_ageInt);
        type_user.setText(type);
        rrvBottom.setVisibility(View.GONE);

    }

    @Override
    public void onClick(String bpf_id, String bpf_deviceId, String bpf_fcmToken, String bpf_topFinishes) {
        reqPersonId = bpf_id;
        deviceId = bpf_deviceId;
        fcmToken = bpf_fcmToken;
        topThreeFinish = bpf_topFinishes;
        getIndividualEvents(reqPersonId);

        if (topThreeFinish != null && !topThreeFinish.equalsIgnoreCase("") && !topThreeFinish.equalsIgnoreCase("null")) {
            String[] values=null;
            topFinishes_One.setText("");
            topFinishes_Two.setText("");
            topFinishes_Three.setText("");
            values= topThreeFinish.split(",");
            if (values.length == 0) {
                topFinishes_Two.setText("No notable finishes");
            }else if (values.length == 1) {
                if (values[0] != null) {
                    topFinishes_One.setText(values[0].trim());
                }
            }else if (values.length == 2) {
                if (values[0] != null) {
                    topFinishes_One.setText(values[0].trim());
                }
                if (values[1] != null) {
                    topFinishes_Two.setText(values[1].trim());
                }
            }else if (values.length == 3) {
                if (values[0] != null) {
                    topFinishes_One.setText(values[0].trim());
                }
                if (values[1] != null) {
                    topFinishes_Two.setText(values[1].trim());
                }
                if (values[2] != null) {
                    topFinishes_Three.setText(values[2].trim());
                }
            }


            //
            //topFinishes_Three.setText(values[2].trim());
        }else {
            topFinishes_Two.setText("No notable finishes");
            topFinishes_One.setText("");
            topFinishes_Three.setText("");
        }
    }

    //Method for SwipeUp

    private void reverse() {

        if (reverseCount) {
            cardStackView.reverse();
            imgv_rvsecard.setImageDrawable(getResources().getDrawable(R.drawable.ic_backcard_disable));
        }
    }

    private LinkedList<BpFinderModel> extractRemainingTouristSpots() {
        LinkedList<BpFinderModel> spots = new LinkedList<>();
        if (adapter != null) {
            for (int i = cardStackView.getTopIndex(); i < adapter.getCount(); i++) {
                spots.add(adapter.getItem(i));
            }
        }
        return spots;
    }

    private void swipeUp() {

        List<BpFinderModel> spots = extractRemainingTouristSpots();
        if (spots.isEmpty()) {
            return;
        }
        reqPersonId = String.valueOf(spots.get(0).getBpf_id());
        View target = cardStackView.getTopView();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 0f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 0f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, -2000f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);

        cardStackView.swipe(SwipeDirection.Top, set);
    }

    //Method for getting current location
    //Conditions for show location
    //if user_subscription = PRime didn't show payment dialogue
    //if user_subscription = BlueBP show payment dialogue,once the user paid then show the location
    // user_subscription = null show the payment dialogue  ,once the user paid then show the location

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

    public long DatetoMilli(Date dateClicked) {
        Date givenDateString = dateClicked;
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date mDate = sdf.parse(String.valueOf(givenDateString));
            mDate.setHours(0);
            mDate.setMinutes(0);
            mDate.setSeconds(0);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    //push notification

    private void getLocation() {
        if (!usertype.equalsIgnoreCase(AppConstants.USER_TYPE_COACH)) {
            if (userSubscription != null) {
                    if (CheckPlan.isPlanAvailable(plansModelList, userSubscription, AppConstants.BENIFIT_CODE_B16)) {
                        userPlanList.clear();
                        //Toast.makeText(tabActivity, "Selecetd index"+selectedIndex, Toast.LENGTH_SHORT).show();
                        if (plansModelList.get(selectedIndex).getPlanName().equalsIgnoreCase(userSubscription)) {
                            gotoSettings();
                        } else {
                            if (plansModelList.get(2).getPlanName().equalsIgnoreCase(userSubscription)) {
                                gotoSettings();
                            } else if (plansModelList.get(3).getPlanName().equalsIgnoreCase(userSubscription)) {
                                gotoSettings();
                            } else {
                                for (int i = selectedIndex; i < plansModelList.size(); i++) {
                                    userPlanList.add(plansModelList.get(i));
                                }
                                showAlertDialouge();
                            }
                        }

                    }
            }
        }else {
            gotoSettings();
        }
    }

    //Method for goto Settings Page
    private void gotoSettings() {
        if (getActivity() != null) {
            SettingsFragment sf = new SettingsFragment();
            Bundle arguments = new Bundle();
            arguments.putString("prime_card", "location");
            sf.setArguments(arguments);
            FragmentManager mang = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = mang.beginTransaction().addToBackStack(null);
            trans.replace(R.id.container, sf);
            trans.commit();
        }
    }

    private void showAlertDialouge() {
        LayoutInflater inflater = getLayoutInflater();
        //View alertLayout = inflater.inflate(R.layout.popup_no_of_likes_layout, null);//Alert dialogue previous one
        View alertLayout = inflater.inflate(R.layout.alert_subscription_layout, null);

        btnProceed    = alertLayout.findViewById(R.id.btn_proceed);
        btnCancel     = alertLayout.findViewById(R.id.btn_sub_cancel);
        RecyclerView rc_view  = alertLayout.findViewById(R.id.rcv_subscribe);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rc_view.setLayoutManager(layoutManager);
        btnProceed.setClickable(false);

        if (userPlanList != null && userPlanList.size() > 0 ) {
            subscriptionAdapter = new SubscriptionAdapter(getContext(),userPlanList, this,userSubscription);
            rc_view.setAdapter(subscriptionAdapter);
        }
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getContext());
        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blueDark));
        alert.setView(alertLayout);
        alert.setCancelable(true);
        final android.app.AlertDialog dialog = alert.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                //dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.blueDark));
                //dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setGravity(Gravity.CENTER);
            }
        });
        dialog.show();
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (splanModels != null) {
                    changeSubLayout(splanModels);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void changeViews(SubscriptonPlansModel plansModel) {
        btnProceed.setBackgroundColor(getResources().getColor(R.color.btn_sub));
        btnProceed.setTextColor(getResources().getColor(R.color.white));
        splanModels = plansModel;
    }

    public void changeSubLayout(SubscriptonPlansModel subscriptonPlansModel) {
        LayoutInflater inflater = getLayoutInflater();
        layoutAlert = inflater.inflate(R.layout.subscription_free_layout, null);
        LinearLayoutManager linearManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        topHeaderLayout = layoutAlert.findViewById(R.id.top_sub_header);
        headerTitle     = layoutAlert.findViewById(R.id.head_sub_title);
        tv_price        = layoutAlert.findViewById(R.id.tv_price);
        tv_regPrice     = layoutAlert.findViewById(R.id.tv_subPrice);
        btnBuy          = layoutAlert.findViewById(R.id.sub_buy);
        rcview_sub_item = layoutAlert.findViewById(R.id.rcview_sub);
        premius_header  = layoutAlert.findViewById(R.id.recruit_header);

        rcview_sub_item.setLayoutManager(linearManager);
        //benefitModelList.clear();
        benefitModelList =subscriptonPlansModel.getBenefitList();
        if (subscriptonPlansModel.getPlanName().equalsIgnoreCase("FREE")) {
            //layoutAlert = inflater.inflate(R.layout.subscription_free_layout, null);
            tv_price.setText("$"+subscriptonPlansModel.getMonthlyCharge());
            btnBuy.setBackgroundColor(getResources().getColor(R.color.butn_free));
            if (benefitModelList != null) {
                tv_regPrice.setText("$"+subscriptonPlansModel.getRegFee());
                benefitItemAdapter = new BenefitListItemAdapter(getContext(),benefitModelList);
                rcview_sub_item.setAdapter(benefitItemAdapter);
            }
        }else if (subscriptonPlansModel.getPlanName().equalsIgnoreCase("LITE")) {
            tv_price.setText("$"+subscriptonPlansModel.getMonthlyCharge());
            topHeaderLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.header_lite));
            headerTitle.setText(R.string.lite_sub);
            btnBuy.setBackgroundColor(getResources().getColor(R.color.butn_lite));
            if (benefitModelList != null) {
                tv_regPrice.setText("$"+subscriptonPlansModel.getRegFee()+" one-time");
                benefitItemAdapter = new BenefitListItemAdapter(getContext(),benefitModelList);
                rcview_sub_item.setAdapter(benefitItemAdapter);
            }
        } else if (subscriptonPlansModel.getPlanName().equalsIgnoreCase("STANDARD")) {
            tv_price.setText("$"+subscriptonPlansModel.getMonthlyCharge());
            topHeaderLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.header_standard));
            headerTitle.setText(R.string.standard_sub);
            btnBuy.setBackgroundColor(getResources().getColor(R.color.butn_std));
            if (benefitModelList != null) {
                tv_regPrice.setText("$"+subscriptonPlansModel.getRegFee()+" one-time");
                benefitItemAdapter = new BenefitListItemAdapter(getContext(),benefitModelList);
                rcview_sub_item.setAdapter(benefitItemAdapter);
            }
        }else {
            tv_price.setText("$"+subscriptonPlansModel.getMonthlyCharge());
            topHeaderLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.header_rec));
            headerTitle.setText(R.string.recruiting_sub);
            btnBuy.setBackgroundColor(getResources().getColor(R.color.butn_recruit));
            premius_header.setVisibility(View.VISIBLE);
            if (benefitModelList != null) {
                tv_regPrice.setText("$"+subscriptonPlansModel.getRegFee()+" one-time");
                benefitItemAdapter = new BenefitListItemAdapter(getContext(),benefitModelList);
                rcview_sub_item.setAdapter(benefitItemAdapter);
            }
        }

        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getContext());
        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blueDark));
        alert.setView(layoutAlert);
        alert.setCancelable(true);
        final android.app.AlertDialog dialog = alert.create();

        dialog.show();

    }
    //
    String postToFCM(String bodyString) throws IOException {
        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + "AAAA_n1CZM0:APA91bEzOCp33DEXluIkYPsfheiCk7PINyo0MaxDkk7AfX-uG6PUO1CCggbEnN3IR1sljAm05WY-BGDeyeAOM7l1ciFdFnAV5z5DrnkBaYKQusqJPcvzbyKX-84t1b5SvpXPp75NjJI9")
                .build();
        com.squareup.okhttp.Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.swipe_matchfound_layout, null);
        final Button btnMsg = layout.findViewById(R.id.sendMsg);
        final Button btnFind = layout.findViewById(R.id.findBtn);
        final Button btnSwipe = layout.findViewById(R.id.btnKeep);
        alert.setView(layout);
        final AlertDialog alertDialog = alert.create();

        int partnerAge = Integer.parseInt(cModel.getBpf_age());
        int myage = prefManager.getAge();
        /*if (partnerAge >18 && myage > 18) {
            btnMsg.setVisibility(View.VISIBLE);
        }else if(partnerAge < 18 && myage < 18){
            btnMsg.setVisibility(View.VISIBLE);
        }else {
            btnMsg.setVisibility(View.GONE);
        }*/
        //Button Send Message
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    alertDialog.dismiss();
                    if (cModel != null) {
                        tabActivity.navigation.setSelectedItemId(R.id.navigation_more);
                        tabActivity.disableFloatButtons();
                        ChatFragmentPage chatFragmentPage = new ChatFragmentPage();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(AppConstants.CHAT_USER, cModel);
                        chatFragmentPage.setArguments(bundle);
                        FragmentManager manager = (getActivity()).getSupportFragmentManager();
                        FragmentTransaction ctrans = manager.beginTransaction();
                        ctrans.replace(R.id.container, chatFragmentPage);
                        ctrans.commit();
                    }
                }
            }
        });

        //Button Find partner
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // alertDialog.dismiss();
                //Moving to Calendar Fragment
                if (getActivity() != null) {
                    alertDialog.dismiss();
                    tabActivity.navigation.setSelectedItemId(R.id.navigation_calendar);
                    tabActivity.disableFloatButtons();
                    CalendarFragment calendarFragment = new CalendarFragment();
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction ctrans = manager.beginTransaction();
                    //ctrans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    ctrans.replace(R.id.container, calendarFragment);
                    ctrans.commit();
                }
            }
        });

        //Button Swipe
        btnSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void getuserSubscriptions() {
        JsonObjectRequest request = new JsonObjectRequest(ApiService.REQUEST_METHOD_GET, ApiService.GET_USER_ACTIVE_PLANS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    Log.d(TAG, "onUserPlansResponse: ");
                    SingleSubscriptionModel subscriptionModel = new Gson().fromJson(response.toString(),SingleSubscriptionModel.class);
                    userSubList = subscriptionModel.getSubItemModel();
                    userAddonsList=subscriptionModel.getAddonsItemModel();
                    saveSubscription();
                    getSubscriptionPlans();//Information for all subscriptions

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            Log.d("SubscriptionRequest", queue.toString());
            queue.add(request);
        }

    }
    private void saveSubscription() {

        if (userSubList != null && userSubList.size() > 0) {
            for (int i = 0; i < userSubList.size() ; i++) {
                if (getActivity() != null) {
                    new PrefManager(getContext()).saveSubscription(userSubList.get(i).getPlanName(),userSubList.get(i).getRemainingDays());
                }
            }
        }
    }

    //Get all subscriptions
    private void getSubscriptionPlans() {
        plansModelList.clear();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(ApiService.REQUEST_METHOD_GET, ApiService.GET_SUBSCRIPTION_PLANS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    Log.d(TAG, "onResponse: "+response.toString());
                    Type type = new TypeToken<List<SubscriptonPlansModel>>() {}.getType();
                    plansModelList = new Gson().fromJson(response.toString(),type);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        if (getActivity() != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            Log.d("SubscriptionRequest", arrayRequest.toString());
            requestQueue.add(arrayRequest);
        }

    }


   /* @Override
    public void onItemClick(Object item, int position) {
        adapter = null;
        SwipeResultModel swipeResultModel = (SwipeResultModel) item;
        addASingleUser(swipeResultModel.getBpFinderModel());
        bluebpListSecond.remove(position);
        blueBProfileAdapter.notifyDataSetChanged();
    }*/


}