package com.beachpartnerllc.beachpartner.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beachpartnerllc.beachpartner.R;
import com.beachpartnerllc.beachpartner.connections.PrefManager;
import com.beachpartnerllc.beachpartner.fragments.AddOnFragment;
import com.beachpartnerllc.beachpartner.fragments.AcceptRejectRequestFragment;
import com.beachpartnerllc.beachpartner.fragments.BPFinderFragment;
import com.beachpartnerllc.beachpartner.fragments.CalendarFragment;
import com.beachpartnerllc.beachpartner.fragments.CoachHomeFragment;
import com.beachpartnerllc.beachpartner.fragments.CoachProfileFragment;
import com.beachpartnerllc.beachpartner.fragments.ConnectionFragment;
import com.beachpartnerllc.beachpartner.fragments.FeedBackFragment;
import com.beachpartnerllc.beachpartner.fragments.HiFiveFragment;
import com.beachpartnerllc.beachpartner.fragments.HomeFragment;
import com.beachpartnerllc.beachpartner.fragments.MessageFragment;
import com.beachpartnerllc.beachpartner.fragments.ProfileFragment;
import com.beachpartnerllc.beachpartner.fragments.SettingsFragment;
import com.beachpartnerllc.beachpartner.fragments.SubscriptionFragment;

public class TabActivity extends AppCompatActivity  {

    private static boolean doubleBackToExitPressedOnce = false;
    private static boolean isBPActive = false;
    private static boolean isPartnerFinder = false;
    private FrameLayout fg;
    HomeFragment homeFragment;
    FloatingActionButton menu1;
    FloatingActionButton menu2;
    private TextView mTextMessage;
    private String YOUR_FRAGMENT_STRING_TAG;
    private Boolean activeMoreStatus = false;
    private Boolean moreClickedOnce = true;
    private Boolean floatMenu1Active = true;
    private Boolean floatMenu2Active = false;
    private String userType;
    private Boolean tips;
    public BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (userType.equalsIgnoreCase("Athlete")) {
                        HomeFragment homeFragment = new HomeFragment();
                        getSupportActionBar().setTitle(R.string.app_name);
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.container, homeFragment, YOUR_FRAGMENT_STRING_TAG);
                        transaction.commit();
                        disableFloatButtons();
                        return true;
                    }
                    if (userType.equalsIgnoreCase("Coach")) {
                        CoachHomeFragment coachHomeFragment = new CoachHomeFragment();
                        getSupportActionBar().setTitle(R.string.app_name);
                        FragmentManager coachHomeManager = getSupportFragmentManager();
                        FragmentTransaction coachHomeTransaction = coachHomeManager.beginTransaction();
                        coachHomeTransaction.replace(R.id.container, coachHomeFragment, YOUR_FRAGMENT_STRING_TAG);
                        coachHomeTransaction.commit();
                        disableFloatButtons();
                        return true;
                    }
                    return true;

                case R.id.navigation_bp:
                    isPartnerFinder = false;
                    BPFinderFragment bpFinderFragment = new BPFinderFragment(isBPActive, isPartnerFinder);
                    getSupportActionBar().setTitle(R.string.app_name);
                    FragmentManager mng = getSupportFragmentManager();
                    FragmentTransaction tran = mng.beginTransaction();
                    tran.replace(R.id.container, bpFinderFragment, YOUR_FRAGMENT_STRING_TAG);
                    tran.commit();
                    disableFloatButtons();
                    return true;

                case R.id.navigation_connection:

                    ConnectionFragment connectionFragment = new ConnectionFragment();
                    getSupportActionBar().setTitle("Connections");
                    FragmentManager mngr = getSupportFragmentManager();
                    FragmentTransaction trans = mngr.beginTransaction();
                    trans.replace(R.id.container, connectionFragment, YOUR_FRAGMENT_STRING_TAG);
                    trans.commit();
                    disableFloatButtons();
                    return true;

                case R.id.navigation_calendar:

                    CalendarFragment calendarFragment = new CalendarFragment();
                    getSupportActionBar().setTitle("Calendar");
                    FragmentManager cmngr = getSupportFragmentManager();
                    FragmentTransaction ctrans = cmngr.beginTransaction();
                    ctrans.replace(R.id.container, calendarFragment, YOUR_FRAGMENT_STRING_TAG);
                    ctrans.commit();
                    disableFloatButtons();
                    return true;


                case R.id.navigation_more:
                    activeMoreStatus = !activeMoreStatus;

                    if (activeMoreStatus) {
                        menu1.setVisibility(View.VISIBLE);
                        menu2.setVisibility(View.VISIBLE);
                        if (moreClickedOnce || floatMenu1Active) {
                            activateMessageFragment();
                            moreClickedOnce = false;
                        } else if (floatMenu2Active) {
                            activateHiFiveFragment();
                        }


                        menu1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                activateMessageFragment();
                                menu1.setImageResource(R.drawable.ic_chat_bubble_active);
                                menu2.setImageResource(R.drawable.ic_highfive);

                                disableFloatButtons();
                            }
                        });

                        menu2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                activateHiFiveFragment();
                                menu1.setImageResource(R.drawable.ic_chat_bubble);
                                menu2.setImageResource(R.drawable.ic_highfive_seen);

                                disableFloatButtons();
                            }
                        });

                    } else {
                        disableFloatButtons();
                    }

                    return true;

                /*case R.id.navigation_notifications:
                    return true;*/
            }
            return false;
        }
    };

    //to disable float buttons
    public void disableFloatButtons() {
        menu1.setVisibility(View.GONE);
        menu2.setVisibility(View.GONE);
        activeMoreStatus = false;
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
    //to show messages fragment
    public void activateMessageFragment() {
        floatMenu1Active = true;
        floatMenu2Active = false;
        MessageFragment messageFragment = new MessageFragment();
        getSupportActionBar().setTitle("Messages");
        FragmentManager messageFManager = getSupportFragmentManager();
        FragmentTransaction messageTrans = messageFManager.beginTransaction();
        messageTrans.replace(R.id.container, messageFragment, YOUR_FRAGMENT_STRING_TAG);
        messageTrans.commit();
    }

    public void activateHiFiveFragment() {
        floatMenu1Active = false;
        floatMenu2Active = true;
        HiFiveFragment hiFiveFragment = new HiFiveFragment();
        getSupportActionBar().setTitle("High Fives");
        FragmentManager hiFiveFManager = getSupportFragmentManager();
        FragmentTransaction hiFiveTrans = hiFiveFManager.beginTransaction();
        hiFiveTrans.replace(R.id.container, hiFiveFragment, YOUR_FRAGMENT_STRING_TAG);
        hiFiveTrans.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_tab);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        fg = (FrameLayout) findViewById(R.id.container);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        menu1 = findViewById(R.id.submenu1);
        menu2 = findViewById(R.id.submenu2);

        userType = new PrefManager(getApplicationContext()).getUserType().trim();
        tips = new PrefManager(getApplicationContext()).getTips();
        String fcm = new PrefManager(getApplicationContext()).getFCMToken();
        String eventId;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String value = bundle.getString("reDirectPage");
            if (value.equalsIgnoreCase("home")) {
                /*Load default HomeFragment*/
                if(!tips){
                    Intent intentTips = new Intent(TabActivity.this, WelcomeActivity.class);
                    intentTips.putExtra("reDirectPage", "home");
                    intentTips.putExtra(WelcomeActivity.EXTRA_IS_FROM_INIT, true);
                    startActivity(intentTips);
                    finish();
                }else {
                    if (userType.equalsIgnoreCase("Athlete")) {
                        HomeFragment homeFragment = new HomeFragment();
                        FragmentManager manager = getSupportFragmentManager();
                        //getSupportActionBar().setTitle("Beach Partner");
                        getSupportActionBar().setTitle(R.string.app_name);
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.container, homeFragment, YOUR_FRAGMENT_STRING_TAG);
                        transaction.commit();
                    }
                    if (userType.equalsIgnoreCase("Coach")) {
                        CoachHomeFragment coachHomeFragment = new CoachHomeFragment();
                        getSupportActionBar().setTitle(R.string.app_name);
                        FragmentManager coachHomeManager = getSupportFragmentManager();
                        FragmentTransaction coachHomeTransaction = coachHomeManager.beginTransaction();
                        coachHomeTransaction.replace(R.id.container, coachHomeFragment, YOUR_FRAGMENT_STRING_TAG);
                        coachHomeTransaction.commit();
                    }
                }

            } else if (value.equals("help")) {
                if (userType.equalsIgnoreCase("Athlete")) {
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    //getSupportActionBar().setTitle("Beach Partner");
                    getSupportActionBar().setTitle(R.string.app_name);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.container, homeFragment, YOUR_FRAGMENT_STRING_TAG);
                    transaction.commit();
                }
                if (userType.equalsIgnoreCase("Coach")) {
                    CoachHomeFragment coachHomeFragment = new CoachHomeFragment();
                    getSupportActionBar().setTitle(R.string.app_name);
                    FragmentManager coachHomeManager = getSupportFragmentManager();
                    FragmentTransaction coachHomeTransaction = coachHomeManager.beginTransaction();
                    coachHomeTransaction.replace(R.id.container, coachHomeFragment, YOUR_FRAGMENT_STRING_TAG);
                    coachHomeTransaction.commit();
                }

            }
            else if(value.equalsIgnoreCase("hifive")){
                activateHiFiveFragment();
            }
            else if(value.equalsIgnoreCase("INVITATION")){
                AcceptRejectRequestFragment acceptRejectRequestFragment = new AcceptRejectRequestFragment();
                Bundle invitationBundle = new Bundle();
                eventId = (String) bundle.get("event_id");
                invitationBundle.putString("event_id", eventId);
                acceptRejectRequestFragment.setArguments(invitationBundle);
                getSupportActionBar().setTitle("Invitation");
                FragmentManager arMang = getSupportFragmentManager();
                FragmentTransaction arTrans = arMang.beginTransaction();
                arTrans.replace(R.id.container, acceptRejectRequestFragment, YOUR_FRAGMENT_STRING_TAG);
                arTrans.commit();
            } else if (value.equalsIgnoreCase("ACCEPTED")) {


            } else if (value.equalsIgnoreCase("ACTIVE")) {
                ConnectionFragment connectionFragment = new ConnectionFragment();
                getSupportActionBar().setTitle("Connections");
                FragmentManager connectionMngr = getSupportFragmentManager();
                FragmentTransaction connectionTrans = connectionMngr.beginTransaction();
                connectionTrans.replace(R.id.container, connectionFragment, YOUR_FRAGMENT_STRING_TAG);
                connectionTrans.commit();
                disableFloatButtons();
            } else {
                if (tips) {
                    if (userType.equalsIgnoreCase("Athlete")) {
                        ProfileFragment pf = new ProfileFragment();
                        getSupportActionBar().setTitle("Profile");
                        FragmentManager mang = getSupportFragmentManager();
                        FragmentTransaction trans = mang.beginTransaction();
                        trans.replace(R.id.container, pf, YOUR_FRAGMENT_STRING_TAG);
                        trans.commit();
                        disableFloatButtons();
                    }
                    if (userType.equalsIgnoreCase("Coach")) {
                        CoachProfileFragment coachProfileFragment = new CoachProfileFragment();
                        getSupportActionBar().setTitle("Profile");
                        FragmentManager mang = getSupportFragmentManager();
                        FragmentTransaction trans = mang.beginTransaction();
                        trans.replace(R.id.container, coachProfileFragment, YOUR_FRAGMENT_STRING_TAG);
                        trans.commit();
                        disableFloatButtons();
                    }

                } else {
                    Intent intentTips = new Intent(TabActivity.this, WelcomeActivity.class);
                    intentTips.putExtra("reDirectPage", "profile");
                    startActivity(intentTips);
                    finish();
                }


            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_class_fragment, menu);
        return true;
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_class_fragment,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
//                    userType   =   new PrefManager(getApplicationContext()).getUserType().trim();
                if (userType.equalsIgnoreCase("Athlete")) {
                    ProfileFragment pf = new ProfileFragment();
                    getSupportActionBar().setTitle("Profile");
                    FragmentManager mang = getSupportFragmentManager();
                    FragmentTransaction trans = mang.beginTransaction();
                    trans.replace(R.id.container, pf, YOUR_FRAGMENT_STRING_TAG);
                    trans.commit();
                    disableFloatButtons();
                    break;
                }

                if (userType.equalsIgnoreCase("Coach")) {
                    CoachProfileFragment coachProfileFragment = new CoachProfileFragment();
                    getSupportActionBar().setTitle("Profile");
                    FragmentManager mang = getSupportFragmentManager();
                    FragmentTransaction trans = mang.beginTransaction();
                    trans.replace(R.id.container, coachProfileFragment, YOUR_FRAGMENT_STRING_TAG);
                    trans.commit();
                    disableFloatButtons();
                    break;
                }

                break;
//            case R.id.about_us:
//                //Toast.makeText(this, "Clicked AboutUs", Toast.LENGTH_SHORT).show();
//                AboutUsFragment aboutUsFragment = new AboutUsFragment();
//                getSupportActionBar().setTitle("About Us");
//                FragmentManager mang = getSupportFragmentManager();
//                FragmentTransaction trans = mang.beginTransaction();
//                trans.replace(R.id.container, aboutUsFragment, YOUR_FRAGMENT_STRING_TAG);
//                trans.commit();
//                disableFloatButtons();
//                break;
            case R.id.feedback:
                //Toast.makeText(this, "Clicked Feedback", Toast.LENGTH_SHORT).show();
                FeedBackFragment feedBackFragment = new FeedBackFragment();
                getSupportActionBar().setTitle("FeedBack");
                FragmentManager mangFeed = getSupportFragmentManager();
                FragmentTransaction transFeed = mangFeed.beginTransaction();
                transFeed.replace(R.id.container, feedBackFragment, YOUR_FRAGMENT_STRING_TAG);
                transFeed.commit();
                disableFloatButtons();
                break;
            case R.id.settings:
                SettingsFragment sf = new SettingsFragment();
                getSupportActionBar().setTitle("Settings");
                FragmentManager settingsMang = getSupportFragmentManager();
                FragmentTransaction settingsTrans = settingsMang.beginTransaction().addToBackStack(null);
                settingsTrans.replace(R.id.container, sf, YOUR_FRAGMENT_STRING_TAG);
                settingsTrans.commit();
                disableFloatButtons();
                break;
            case R.id.help:
                //Toast.makeText(this, "Clicked Help", Toast.LENGTH_SHORT).show();
                new PrefManager(getApplicationContext()).saveTips(false);
                Intent intentTips = new Intent(TabActivity.this, WelcomeActivity.class);
                intentTips.putExtra("reDirectPage", "help");
                startActivity(intentTips);
//                finish();
                break;

            case R.id.subscription:
                SubscriptionFragment subscriptionFragment = new SubscriptionFragment();
                FragmentManager mnger = getSupportFragmentManager();
                FragmentTransaction subscriptionTrans = mnger.beginTransaction().addToBackStack(null);
                subscriptionTrans.replace(R.id.container, subscriptionFragment, YOUR_FRAGMENT_STRING_TAG);
                subscriptionTrans.commit();
                disableFloatButtons();
                break;

            case R.id.addOns:
                AddOnFragment addOnFragment = new AddOnFragment();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction().addToBackStack(null);
                transaction.replace(R.id.container, addOnFragment, YOUR_FRAGMENT_STRING_TAG);
                transaction.commit();
                disableFloatButtons();
                break;
            case R.id.logout:
                //Toast.makeText(this, "Clicked LogOut", Toast.LENGTH_SHORT).show();
                alertLogout();
                break;

            /*case R.id.subscribe:
                SubscriptionFragment fragment = new SubscriptionFragment();
                FragmentManager sett = getSupportFragmentManager();
                FragmentTransaction settingsTran = sett.beginTransaction().addToBackStack(null);
                settingsTran.replace(R.id.container, fragment, YOUR_FRAGMENT_STRING_TAG);
                settingsTran.commit();
                break;*/
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void loadProfileFragment(){
        ProfileFragment pf = new ProfileFragment();
        getSupportActionBar().setTitle("Profile");
        FragmentManager mang = getSupportFragmentManager();
        FragmentTransaction trans = mang.beginTransaction();
        trans.replace(R.id.container, pf, YOUR_FRAGMENT_STRING_TAG);
        trans.commit();
        disableFloatButtons();
    }

    public void loadCoachProfileFragment(){
        CoachProfileFragment cpf = new CoachProfileFragment();
        getSupportActionBar().setTitle("Profile");
        FragmentManager Coachmang = getSupportFragmentManager();
        FragmentTransaction trans = Coachmang.beginTransaction();
        trans.replace(R.id.container, cpf, YOUR_FRAGMENT_STRING_TAG);
        trans.commit();
        disableFloatButtons();
    }
    private void alertLogout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.message_logout)
                .setMessage(R.string.hint_logout)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //logout
                        clearPreference();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        //System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }



    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        boolean isHome = fragment instanceof HomeFragment || fragment instanceof CoachHomeFragment;

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!isHome) {
            navigation.setSelectedItemId(R.id.navigation_home);
        } else {
           // super.onBackPressed();
          new AlertDialog.Builder(this)
                  //.setTitle("Close App ?")
                  .setMessage("Are you sure you want to exit?")
                  .setPositiveButton("YES",
                          new DialogInterface.OnClickListener() {

                              @Override
                              public void onClick(DialogInterface dialog,
                                                  int which) {

                                  finish();
                                  /*Intent intent = new Intent(TabActivity.this,LoginActivity.class);
                                  startActivity(intent);
                                  finish();*/
                              }
                          })
                  .setNegativeButton("NO",
                          new DialogInterface.OnClickListener() {

                              @Override
                              public void onClick(DialogInterface dialog,
                                                  int which) {
                              }
                          }).show();
        }


    }


    private void clearPreference() {
        PrefManager prefManager = new PrefManager(getApplicationContext());
        prefManager.clearAllPrefrence();

    }



}
