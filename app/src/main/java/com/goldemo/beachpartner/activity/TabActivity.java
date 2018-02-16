package com.goldemo.beachpartner.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.goldemo.beachpartner.R;
import com.goldemo.beachpartner.fragments.BPFinderFragment;
import com.goldemo.beachpartner.fragments.HomeFragment;
import com.goldemo.beachpartner.fragments.ProfileFragment;

public class TabActivity extends AppCompatActivity {

    private TextView mTextMessage;
    HomeFragment homeFragment;
    private String YOUR_FRAGMENT_STRING_TAG;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    HomeFragment homeFragment = new HomeFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.container, homeFragment,YOUR_FRAGMENT_STRING_TAG);
                    transaction.commit();
                    return true;
                case R.id.navigation_bp:

                    BPFinderFragment bpFinderFragment = new BPFinderFragment();
                    FragmentManager mng = getSupportFragmentManager();
                    FragmentTransaction tran = mng.beginTransaction();
                    tran.replace(R.id.container, bpFinderFragment,YOUR_FRAGMENT_STRING_TAG);
                    tran.commit();
                    return true;

                case R.id.navigation_profile:

                    ProfileFragment pf = new ProfileFragment();
                    FragmentManager mang = getSupportFragmentManager();
                    FragmentTransaction trans = mang.beginTransaction();
                    trans.replace(R.id.container, pf,YOUR_FRAGMENT_STRING_TAG);
                    trans.commit();
                    return true;


                /*case R.id.navigation_notifications:
                    return true;*/
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        FrameLayout fg = (FrameLayout)findViewById(R.id.container);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /*Load default HomeFragment*/

        HomeFragment homeFragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, homeFragment,YOUR_FRAGMENT_STRING_TAG);
        transaction.commit();


    }

}
