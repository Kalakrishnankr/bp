package com.goldemo.beachpartner.connections;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by seq-kala on 19/3/18.
 */

public class PrefManager {
    Context context;

    public PrefManager(Context context) {
        this.context = context;
    }

    public void saveLoginDetails(String email, String password,String id_token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.putString("Token",id_token);
        editor.commit();
    }


    public String getEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Email", "");
    }

    public String getToken(){
        SharedPreferences  preferences = context.getSharedPreferences("LoginDetails",Context.MODE_PRIVATE);
        return preferences.getString("Token","");
    }

    public boolean isUserLogedOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        boolean isEmailEmpty = sharedPreferences.getString("Email", "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString("Password", "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }

}
