package com.beachpartnerllc.beachpartner.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.beachpartnerllc.beachpartner.CustomEditText;
import com.beachpartnerllc.beachpartner.R;
import com.beachpartnerllc.beachpartner.connections.ApiService;
import com.beachpartnerllc.beachpartner.connections.PrefManager;
import com.beachpartnerllc.beachpartner.utils.AppCommon;
import com.beachpartnerllc.beachpartner.utils.AppConstants;
import com.beachpartnerllc.beachpartner.utils.DrawableClickListener;
import com.beachpartnerllc.beachpartner.utils.FormValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by seq-kala on 7/2/18.
 */

@TargetApi(Build.VERSION_CODES.N)
public class SignUpActivity extends AppCompatActivity{

    private static final int REQUEST_TAKE_GALLERY_VIDEO=1;
    private static final int REQUEST_TAKE_GALLERY_IMAGE=2;
    private ImageView imgVideo,imgProfile,imgPlay;
    private VideoView videoView;

    private EditText user_fname,user_lname,user_dob,user_email,user_confPasswd,user_mobileno;
    private CustomEditText user_location_spinner;
    CustomEditText user_password;
    private String userName,lastName,dob,email,pass,confnPass,location,mobileno,android_id;
    private Button btnsignUp,user_male,user_female;
    private AwesomeValidation awesomeValidation;
    private  Uri selectedImageUri,selectedVideoUri;
    TextInputLayout dobWrapper,passwordWrapper,fNameWrapper;
    Calendar myCalendar = Calendar.getInstance();
    private static String sex;
    private LinearLayout llogin;
    private RadioGroup userTypeRadio;
    private RadioButton rb;
    private String userType ="Athlete";
    private int paymentStatus;
    private ProgressDialog progress;
    private  boolean minorStatus=false;
    private long maxDate;

    private ArrayList<String> stateList = new ArrayList<>();
    private ArrayAdapter<String> dataAdapter;
    private String locationSelectedStatus;
    TextView txt_fnameError,user_lnameError,txt_mobileError, txt_confirmError, txt_dobError,txt_emailError,txt_passwordError,txt_usrTypeError,txt_genderError,txt_cityError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //clearing when toggling between signup and login
        sex=null;
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        initActivity();
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        progress = new ProgressDialog(SignUpActivity.this);
        progress.setMessage("Loading...");


    }


    private void initActivity() {

        user_fname      =  findViewById(R.id.input_firstname);
        user_lname      =  findViewById(R.id.input_lastname);
        user_dob        =  findViewById(R.id.input_dob);
        user_male       =  findViewById(R.id.btnMale);
        user_female     =  findViewById(R.id.btnFemale);
        user_email      =  findViewById(R.id.input_email);
        user_password   =  findViewById(R.id.input_password);
        user_confPasswd =  findViewById(R.id.input_confirm_password);
        user_location_spinner= findViewById(R.id.input_city);
        user_mobileno   =  findViewById(R.id.input_mobile);
        btnsignUp       =  findViewById(R.id.btnSignUp);
        llogin          =  findViewById(R.id.login);
        userTypeRadio   =  findViewById(R.id.user_type);
        dobWrapper      =  findViewById(R.id.dobWrapper);
        passwordWrapper =  findViewById(R.id.pwd_wrapper);
        fNameWrapper    =  findViewById(R.id.fnameWrapper);

        txt_dobError    =  findViewById(R.id.txt_dobError);
        txt_usrTypeError=  findViewById(R.id.txt_athleteError);
        txt_genderError =  findViewById(R.id.txt_genderError);
        user_lnameError =  findViewById(R.id.txt_lnameError);
        txt_fnameError  =  findViewById(R.id.txt_fnameError);
        txt_dobError    =  findViewById(R.id.txt_dobError);
        txt_emailError  =  findViewById(R.id.txt_emailError);
        txt_confirmError=  findViewById(R.id.txt_conformError);
        txt_passwordError= findViewById(R.id.txt_passwordError);
        txt_mobileError =  findViewById(R.id.txt_mobileError);
        txt_cityError   =  findViewById(R.id.txt_cityError);


        user_fname.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
// Username focous Listener
        user_fname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus && new FormValidator().validateEditText(user_fname).equalsIgnoreCase("special character")) {
                    //user_fname.setText("");
                    txt_fnameError.setVisibility(View.VISIBLE);
                    txt_fnameError.setText(getResources().getString(R.string.nameerror));
                }else if(!hasFocus && new FormValidator().validateEditText(user_fname).equalsIgnoreCase("failed")){
                    //user_fname.setText("");
                    txt_fnameError.setVisibility(View.VISIBLE);
                    txt_fnameError.setText(getResources().getString(R.string.fname_blank));
                }
                else if(!hasFocus&&  new FormValidator().validateEditText(user_fname).equalsIgnoreCase("valid")){
                    txt_fnameError.setVisibility(View.GONE);
                }
                else if(hasFocus){
                    txt_fnameError.setVisibility(View.GONE);
                }
            }
        });

        user_lname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && new FormValidator().validateLastName(user_lname).equalsIgnoreCase("special character")) {
                    //user_fname.setText("");
                    user_lnameError.setVisibility(View.VISIBLE);
                    user_lnameError.setText(getResources().getString(R.string.lnameerror));
                }else if(!hasFocus && new FormValidator().validateLastName(user_lname).equalsIgnoreCase("failed")){
                    user_lnameError.setVisibility(View.VISIBLE);
                    user_lnameError.setText(getResources().getString(R.string.lname_blank));
                }
                else if(!hasFocus&&  new FormValidator().validateEditText(user_lname).equalsIgnoreCase("valid")){
                    user_lnameError.setVisibility(View.GONE);
                }
                else if(hasFocus){
                    user_lnameError.setVisibility(View.GONE);
                }
            }
        });
// Username focous Listener
        user_dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(user_dob.getText().toString().trim().equals("")) {
                    user_dob.setText("");
                    txt_dobError.setVisibility(View.VISIBLE);
                    txt_dobError.setText(getResources().getString(R.string.doberror));
                }else if(hasFocus){
                    txt_dobError.setVisibility(View.GONE);
                }
            }
        });

// Username focous Listener
        user_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !new FormValidator().emailValidator(user_email)){
                    //user_email.setText("");
                    txt_emailError.setVisibility(View.VISIBLE);
                    txt_emailError.setText(getResources().getString(R.string.emailerror));
                }
                else if(!hasFocus && new FormValidator().emailValidator(user_email)){
                    txt_emailError.setVisibility(View.GONE);
                }else if(hasFocus){
                    txt_emailError.setVisibility(View.GONE);
                }
            }
        });
// Username focous Listener
        user_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus && user_password.getText().toString().trim().length()<8){
                    txt_passwordError.setVisibility(View.VISIBLE);
                    txt_passwordError.setText(getResources().getString(R.string.invalid_password));
                }else if(!hasFocus&& user_password.getText().charAt(0)==' '&& user_password.getText().charAt(user_password.getText().length()-1)==' ' ){
                    txt_passwordError.setVisibility(View.VISIBLE);
                    txt_passwordError.setText(getResources().getString(R.string.null_password));
                }
                else if(hasFocus){
                    txt_passwordError.setVisibility(View.GONE);
                }
            }
        });
// Username focous Listener
        user_confPasswd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus ) {
                    if(!user_confPasswd.getText().toString().trim().equals(null) && !user_password.getText().toString().trim().equals(null)){
                        if(!user_password.getText().toString().trim().equals(user_confPasswd.getText().toString().trim())){

                            txt_confirmError.setVisibility(View.VISIBLE);
                            txt_confirmError.setText(getResources().getString(R.string.invalid_confirmpassword));
                        }
                        else{
                            txt_confirmError.setVisibility(View.GONE);
                        }
                    }

                }else{
                    txt_confirmError.setText(getResources().getString(R.string.enter_confirm_pwd));
                    txt_confirmError.setVisibility(View.GONE);
                }
            }
        });

// Mobile focous Listener
        user_mobileno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus && (user_mobileno.getText().length()<10)) {
                    txt_mobileError.setVisibility(View.VISIBLE);
                    txt_mobileError.setText(getResources().getString(R.string.mobilerror));
                }
                else if(!hasFocus && user_mobileno.getText().toString().equals(null)){
                    txt_mobileError.setVisibility(View.VISIBLE);
                    txt_mobileError.setText(getResources().getString(R.string.mobilerror));
                }

                else if(!hasFocus &&(user_mobileno.getText().length()==10)){
                    txt_mobileError.setVisibility(View.GONE);
                }

                else if(hasFocus &&(user_mobileno.getText().length()==10)){
                    txt_mobileError.setVisibility(View.GONE);
                }

                else if(hasFocus ){
                    txt_mobileError.setVisibility(View.GONE);
                }
            }
        });

        //initially usertpe selected as athlete and date of birth's maximum date set accordingly(-5 years)

        userTypeRadio.check(R.id.athlete_radio);
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add( Calendar.YEAR, -5 ) ;
        maxDate = c.getTime().getTime();
        userTypeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb =(RadioButton) findViewById(checkedId);
                if(rb.getText().toString().trim().equals("Coach")){
                    userType="Coach";
                    myCalendar = Calendar.getInstance();
                    user_dob.setText("");
                    dob=null;
                    Date today = new Date();
                    Calendar c = Calendar.getInstance();
                    c.setTime(today);
                    // Subtract 18 years
                    c.add( Calendar.YEAR, -18 ) ;
                    maxDate = c.getTime().getTime();

                }else {
                    userType="Athlete";
                    myCalendar = Calendar.getInstance();
                    user_dob.setText("");
                    dob=null;
                    Date today = new Date();
                    Calendar c = Calendar.getInstance();
                    c.setTime(today);
                    c.add( Calendar.YEAR, -5 ) ;
                    maxDate = c.getTime().getTime();
                    // Subtract 5 years
                    if(minorStatus && !user_dob.getText().toString().equalsIgnoreCase("")){
                        alertMinor();
                    }
                }
                txt_usrTypeError.setVisibility(View.GONE);

            }


        });


        user_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex="Male";
                user_male.setTextColor(getResources().getColor(R.color.white));
                user_male.setBackgroundColor(getResources().getColor(R.color.btnColor));
                user_female.setTextColor(getResources().getColor(R.color.btnColor));
                user_female.setBackgroundColor(getResources().getColor(R.color.imgBacgnd));
                txt_genderError.setVisibility(View.GONE);
            }
        });

        user_female.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                sex="Female";
                user_female.setTextColor(getResources().getColor(R.color.white));
                user_female.setBackgroundColor(getResources().getColor(R.color.btnColor));
                user_male.setTextColor(getResources().getColor(R.color.btnColor));
                user_male.setBackgroundColor(getResources().getColor(R.color.imgBacgnd));
                txt_genderError.setVisibility(View.GONE);

            }
        });




        llogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        user_password.setDrawableClickListener(new DrawableClickListener() {
            Boolean clicked=false;
           @Override
           public void onClick(DrawablePosition target) {
               switch (target) {
                   case RIGHT:
                       clicked=!clicked;
                       if(clicked){
                           user_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                           user_password.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_passwor_toggle_disable, 0);
                       }
                       else{
                           user_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                           user_password.setCompoundDrawablesWithIntrinsicBounds(0, 0,  R.drawable.ic_pwd_toggle, 0);
                       }
                       break;

                   default:
                       break;
               }
           }
       });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dateBirth = updateLabel();
                if (dateBirth!=null){
                    Calendar today = Calendar.getInstance();
                    int age = today.get(Calendar.YEAR) - dateBirth.getCalendar().get(Calendar.YEAR);
                    if(today.get(Calendar.DAY_OF_YEAR) < dateBirth.getCalendar().get(Calendar.DAY_OF_YEAR)){
                        age--;
                    }
                    Integer ageInt = new Integer(age);
                    if(ageInt<18){
                        minorStatus =true;
                        if(userType!=null){
                            if(userType.equals("Athlete")){
                                alertMinor();
                            }
                        }
                    }
                }
            }

        };


        //dob date

        user_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_dobError.setVisibility(View.GONE);
//                DatePickerDialog dialog = new DatePickerDialog(SignUpActivity.this,android.R.style.Theme_Holo_Light_Panel, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                dialog.getDatePicker().setMaxDate(maxDate);
//                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == DialogInterface.BUTTON_NEGATIVE) {
////                            txt_dobError.setVisibility(View.VISIBLE);
////                            txt_dobError.setText(getResources().getString(R.string.doberror));
//                        }
//                    }
//                });
//                dialog.show();

                CustomDatePicker();

            }
        });

        user_location_spinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    toursPlayed();
                }
            }
        });

        user_location_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toursPlayed();
            }
        });

        //SignUp here
        btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = user_fname.getText().toString().trim();
                lastName = user_lname.getText().toString().trim();
                //dob = user_dob.getText().toString().trim();
                email = user_email.getText().toString().trim();
                pass = user_password.getText().toString().trim();
                confnPass = user_confPasswd.getText().toString().trim();
//                location = user_location_spinner.getText().toString().trim();
                mobileno = user_mobileno.getText().toString().trim();


//                addValidationToViews();//Method for validate feilds
                    if(userName   == null || userName.length()==0){
                        txt_fnameError.setVisibility(View.VISIBLE);
                        txt_fnameError.setText(getResources().getString(R.string.fname_blank));
                    }
                    if(lastName == null || lastName.length()==0){
                        user_lnameError.setVisibility(View.VISIBLE);
                        user_lnameError.setText(getResources().getString(R.string.lname_blank));
                    }
                    if(email == null || email.length()==0){
                        txt_emailError.setVisibility(View.VISIBLE);
                        txt_emailError.setText(getResources().getString(R.string.email_blank));
                    }
                    if(pass == null || pass.length()==0){
                        txt_passwordError.setVisibility(View.VISIBLE);
                        txt_passwordError.setText(getResources().getString(R.string.pwd_blank));
                    }
                    if(confnPass == null || confnPass.length()==0){
                        txt_confirmError.setVisibility(View.VISIBLE);
                        txt_confirmError.setText(getResources().getString(R.string.enter_confirm_pwd));
                    }


                    if(mobileno == null || mobileno.length()==0){
                        txt_mobileError.setVisibility(View.VISIBLE);
                        txt_mobileError.setText(getResources().getString(R.string.mobile_blank));
                    }
                    if(location == null || location.length()==0){
                      txt_cityError.setVisibility(View.VISIBLE);
                      txt_cityError.setText(getString(R.string.cityerror));
                    }
                    else{
                        txt_cityError.setVisibility(View.GONE);
                    }

                    if (userType == null && dob.length() == 0) {

                        txt_dobError.setVisibility(View.VISIBLE);
                        txt_dobError.setText(getResources().getString(R.string.doberror));

                        txt_usrTypeError.setVisibility(View.VISIBLE);
                        txt_usrTypeError.setText(getResources().getString(R.string.enter_user_type));
                        //Toast.makeText(SignUpActivity.this, "Please enter your user type and date of birth", Toast.LENGTH_SHORT).show();
                    }
                    if (userType == null) {
                        txt_usrTypeError.setVisibility(View.VISIBLE);
                        txt_usrTypeError.setText(getResources().getString(R.string.enter_user_type));
                    }
                    //dob null check
                    if ( dob==null) {
                        txt_dobError.setVisibility(View.VISIBLE);
                        txt_dobError.setText(getResources().getString(R.string.doberror));
                    }
                    else if(dob.length() == 0||dob.equalsIgnoreCase("")){
                        txt_dobError.setVisibility(View.VISIBLE);
                        txt_dobError.setText(getResources().getString(R.string.doberror));
                    }

                    if (sex == null) {
                        txt_genderError.setVisibility(View.VISIBLE);
                        txt_genderError.setText(getResources().getString(R.string.gendererror));
                    }
                if (!AppCommon.isValidMobileNumber(mobileno)){
                    txt_mobileError.setText(getResources().getString(R.string.mobilerror));
                    txt_mobileError.setVisibility(View.VISIBLE);
                    return;
                }
                    if (userType != null && dob != null && userName.length()!=0 && lastName.length()!=0 && email.length()!=0 && pass.length()!=0 &&confnPass.length()!=0 && mobileno.length()==10 && sex!=null &&(locationSelectedStatus!=null)) {
                        if(dob.length()!=0){
                            submitForm();
                        }

                    }


            }

        });
    }

    public void toursPlayed() {
        //final String[] items = {"Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
        String[] items = new String[AppConstants.getstatelist().size()];
        items =AppConstants.getstatelist().toArray(items);
        // arraylist to keep the selected items
        final String[] finalItems = items;
        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(SignUpActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("Select State")
                .setSingleChoiceItems(items, 0, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user_location_spinner.setText(finalItems[which]);

                        location= finalItems[which];
                        locationSelectedStatus=location;
                        dialog.dismiss();
                    }
                }).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {

            }
        });

        dialog.show();

    }



    private void submitForm() {


        SimpleDateFormat sim = new SimpleDateFormat("MM-dd-yyyy'T'HH:mm:ss'Z'");

        JSONArray array = new JSONArray();
        array.put("basic");

        JSONObject object = new JSONObject();
        try {

            object.put("authToken","null");

            object.put("city",location);
        //  object.put("createdBy",dob);
        //  object.put("createdDate",sim.format(new Date()));
        //  object.put("lastModifiedDate",sim.format(new Date()));

            object.put("deviceId",android_id);
            object.put("dob",dob);
            object.put("email",email);
            object.put("firstName",userName);
            object.put("gender",sex);
        //  object.put("id",100);

            object.put("imageUrl","null");
            object.put("langKey","null");

            object.put("lastName",lastName);
            object.put("location",location);
            object.put("login",email);
            object.put("loginType","BP");
            object.put("password",pass);
            object.put("phoneNumber",mobileno);
            object.put("userType",userType);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*JSONObject jo= new JSONObject();
        try {
            jo.put("usertype",1);
            jo.put("userDetails",object);


        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        JsonObjectRequest objectRequest = new JsonObjectRequest(ApiService.REQUEST_METHOD_POST, ApiService.SIGNUP, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response--", response.toString());

                        try {
                            String statusMessage = response.getString("message").toString().trim();
                            if(response!=null && statusMessage.equals("success")){

                                //Toast.makeText(SignUpActivity.this, "Successfully Registered\nUser ID: "+response.getString("userId"), Toast.LENGTH_LONG).show();
                                Toast.makeText(SignUpActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();

                                clearFeilds();
                                PrefManager prefManager = new PrefManager(SignUpActivity.this);
                                prefManager.saveRegistrationStatus("pending");
                                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                                //chatregister firebase


                            }else{
                                PrefManager prefManager = new PrefManager(SignUpActivity.this);
                                prefManager.saveRegistrationStatus("");
                                Toast.makeText(SignUpActivity.this, "Failed", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        Log.d("error--", error.toString());
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null){
                            switch (response.statusCode){
                                case 400 :
                                    json = new String (response.data);
                                    json = trimMessage(json,"title");
                                    if(json!=null){
                                        progress.dismiss();
                                        Toast toast = Toast.makeText(SignUpActivity.this, " "+json, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                                        toast.show();
                                    }
                                    break;

                                    default:
                                        break;
                            }
                        }else{
                            Toast.makeText(SignUpActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.d("Request", objectRequest.toString());
        requestQueue.add(objectRequest);

    }
        //Method for clear the feilds

    private void clearFeilds() {
        user_fname.setText("");
        user_lname.setText("");
        user_dob.setText("");
        user_email.setText("");
        user_password.setText("");
        user_confPasswd.setText("");
        user_location_spinner.setText("");
        user_mobileno.setText("");
        //userTypeRadio.clearCheck();
    }

    public SimpleDateFormat updateLabel() {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        user_dob.setText(sdf.format(myCalendar.getTime()));

        String serverFormat = "yyyy-MM-dd";
        SimpleDateFormat server_sdf = new SimpleDateFormat(serverFormat, Locale.US);
        dob = server_sdf.format(myCalendar.getTime());

        return sdf;
    }


    private void alertMinor() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Will you be able to manage your payments?")
                .setPositiveButton("Yes", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            paymentStatus=0;
                    }
                })
                .setNegativeButton("No", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertPayments();
                        paymentStatus=1;
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog =  builder.create();
        dialog.show();

    }

    private void alertPayments() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to link your account with your parent?")
                .setPositiveButton("Yes", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        paymentStatus=2;
                        alertEnterParentDetails();
                    }
                })
                .setNegativeButton("No", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog =  builder.create();
        dialog.show();

    }





    private void CustomDatePicker(){


        LayoutInflater inflater = getLayoutInflater();
        View datealertLayout = inflater.inflate(R.layout.custom_date_picker_dialog, null);

        final DatePicker dp     =          (DatePicker) datealertLayout.findViewById(R.id.datePicker_custom);
        final Button  okBtn     =          (Button)   datealertLayout.findViewById(R.id.okBtn);
        final Button  cancelBtn =           (Button) datealertLayout.findViewById(R.id.cancel_button);


        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);



        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blueDark));



        dp.setMaxDate(maxDate);
        dp.init(myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                // Do something when the date changed from date picker object

                // Create a Date variable/object with user chosen date

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            }
        });

        alert.setView(datealertLayout);
        alert.setCancelable(true);


        final android.app.AlertDialog dialog = alert.create();


        dialog.show();

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myCalendar.set(Calendar.YEAR, dp.getYear());
                myCalendar.set(Calendar.MONTH, dp.getMonth());
                myCalendar.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
                SimpleDateFormat dateBirth = updateLabel();
                if (dateBirth!=null){
                    Calendar today = Calendar.getInstance();
                    int age = today.get(Calendar.YEAR) - dateBirth.getCalendar().get(Calendar.YEAR);
                    if(today.get(Calendar.DAY_OF_YEAR) < dateBirth.getCalendar().get(Calendar.DAY_OF_YEAR)){
                        age--;
                    }
//                    Integer ageInt = new Integer(age);
//                    if(ageInt<18){
//                        minorStatus =true;
//                        if(userType!=null){
//                            if(userType.equals("Athlete")){
//                                alertMinor();
//                            }
//                        }
//                    }
                }
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }



    private void alertEnterParentDetails() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.popup_parent_payment_layout, null);

        final EditText parent_firstname  = (EditText) alertLayout.findViewById(R.id.input_parent_firstname);
        final EditText parent_lastname   = (EditText) alertLayout.findViewById(R.id.input_parent_lastname);
        final EditText parent_email      = (EditText) alertLayout.findViewById(R.id.input_parent_email);
        final EditText parent_mobile     = (EditText) alertLayout.findViewById(R.id.input_parent_phone);
        final Button   save_btn          = (Button)   alertLayout.findViewById(R.id.parent_details_submit_btn);

        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);


        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blueDark));


        alert.setView(alertLayout);
        alert.setCancelable(true);




        final android.app.AlertDialog dialog = alert.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {

                dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.blueDark));
                dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setGravity(Gravity.CENTER);
            }
        });
        dialog.show();

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }



    private String trimMessage(String json, String detail) {
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(detail);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
