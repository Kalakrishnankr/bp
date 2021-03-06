package com.beachpartnerllc.beachpartner.fragments;

import android.Manifest;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.beachpartnerllc.beachpartner.CircularImageView;
import com.beachpartnerllc.beachpartner.R;
import com.beachpartnerllc.beachpartner.activity.TabActivity;
import com.beachpartnerllc.beachpartner.connections.ApiService;
import com.beachpartnerllc.beachpartner.connections.PrefManager;
import com.beachpartnerllc.beachpartner.models.BpFinderModel;
import com.beachpartnerllc.beachpartner.models.Coach.CoachProfile.CoachProfileResponse.CoachProfileResponse;
import com.beachpartnerllc.beachpartner.models.UserProfileModel;
import com.beachpartnerllc.beachpartner.utils.AppCommon;
import com.beachpartnerllc.beachpartner.utils.AppConstants;
import com.beachpartnerllc.beachpartner.utils.FloatingActionButton;
import com.beachpartnerllc.beachpartner.utils.FloatingActionMenu;
import com.beachpartnerllc.beachpartner.utils.SelectedFilePath;
import com.beachpartnerllc.beachpartner.utils.ServiceClass;
import com.beachpartnerllc.beachpartner.utils.UploadObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.beachpartnerllc.beachpartner.utils.SelectedFilePath.getDataColumn;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ProfileFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener,ExoPlayer.EventListener {

    final static CharSequence[] items = {"AVP Next", "AVP First", "CBVA Adult", "CBVA Junior", "AAU", "BVCA", "Relentless", "BVNE", "LC", "USAV", "Volley America", "Beach Elite", "United States Association of Volleyball (USAV)", "Amateur Athletic Union (AAU)", "Association of Volleyball Professionals (AVP)", "Extreme Volleyball Professionals (EVP)", "National Volleyball League (NVL)", "VolleyAmerica", "Beach Volleyball National Events (BVNE)", "Rox Volleyball Series", "California Beach Volleyball Association", "Volley OC", "Northern California Volleyball Association", "Beach Elite/Endless Summer", "Beach Volleyball Clubs of American (BVCA)", "Junior Volleyball Association (JVA)", "Beach Volleyball San Diego", "Gulf coast Volleyball Association (GCVA)", "tArizona Tournaments", "The Island Volleyball", "Florida Tournaments", "Northeast Volleyball Qualifier", "North East Beach Volleyball", "Precision Sand Volleyball", "AVA", "Wasatch Beach Volleyball", "Ohio Valley Region", "Wisconsin Juniors", "AlohaRegionJuniors", "Ohio Valley Region", "Wisconsin Juniors", "AlohaRegionJuniors"};
    private static final int REQUEST_SAVEIMGTODRIVE = 3;
    private static final int REQUEST_TAKE_GALLERY_IMAGE = 2;
    private static final int PICK_IMAGE_REQUEST = 0;
    private static final int PICK_VIDEO_REQUEST = 1;
    private static final String TAG = "ProfileFragment";
    public static boolean isValidate = false;
    private static boolean moreUploadStatus = false;
    private static boolean editStatus = false;
    private static ProgressDialog progress;
    // private static PhotoAsyncTask asyncTask;
    public CoachProfileResponse userDataModel;
    public String token, user_id, spinnerTLValue, spinnerWTValue, spinnerTRValue, spinnerExpValue, spinnerPrefValue, spinnerPosValue, editHeightValue, imageUri, videoUri;
    Calendar myCalendar = Calendar.getInstance();
    ArrayList selectedItems;

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Context mContext;
    Bitmap profilePhoto = null;
    SimpleExoPlayer exoPlayer;
    List<String> courtPref;
    DefaultHttpDataSourceFactory dataSourceFactory = null;
    ExtractorsFactory extractorsFactory = null;
    // set the selected values from server int List

    File myProfileImageFile;
    File myProfileVideFile;
    private TabLayout tabs;
    private ProgressBar progressbar;
    private ViewPager viewPager;
    private FrameLayout videoFrameLayout;
    private ImageView imgEdit, imgVideo, imgPlay, profile_img_editIcon, imageView1, imageView2, imageView3;
    private FloatingActionMenu imgShare;
    private FloatingActionButton fabImage, fabVideo;
    private CircularImageView imgProfile;
    private TextView profileName, profileDesig, edit_tag,imgShareText, basic_info_tab, more_info_tab;
    //private VideoView videoView;
    private Uri selectedImageUri, selectedVideoUri, screenshotUri, screenshotVideoUri;
    private byte[] multipartBody;
    private LinearLayout llMenuBasic, llMenuMore, llBasicDetails, llMoreDetails;//This menu bar only for demo purpose
    private View viewBasic, viewMore;
    private EditText editFname, editLname, editGender, editDob, editPhone;
    private Spinner editCity;
    private EditText editPlayed, editCBVANo, editCBVAFName, editCBVALName, editHighschool, editIndoorClub, editColgClub, editColgBeach, editColgIndoor, editPoints, topfinishes_txt_2, topfinishes_txt_1, topfinishes_txt_3, edit_volleyRanking;
    private Button moreBtnSave, moreBtnCancel, basicBtnSave, basicBtnCancel;
    private LinearLayout btnsBottom, more_info_btns_bottom;
    private LinearLayout topFinishes1_lt, topFinishes2_lt, topFinishes3_lt;
    private RelativeLayout containingLt;
    private int finishCount = 0;
    private Spinner spinnerExp, spinnerPref, spinnerPositon, spinnerTLInterest, spinnerTourRating, spinnerWtoTravel, editHeight;
    private AwesomeValidation awesomeValidation;
    private boolean saveFile;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private ArrayAdapter<String> expAdapter, prefAdapter, positionAdapter, highestRatingAdapter, tournamentInterestAdapter, distanceAdapter, heightAdapter;
    private int videoDuration;
    private PlayerView playerView;
    private String selectedTours;
    private String location_change = "profile";
    private String location;
    private TabActivity tabActivity;
    private ScrollView scrollview_profile;
    private Handler mUiHandler = new Handler();
    private ArrayList<String> stateList = new ArrayList<>();
    private ArrayAdapter<String> dataAdapter;
    boolean saveClicked = false;
    boolean appendEditOpenedOne = false;
    boolean appendEditOpenedTwo = false;
    FrameLayout placeholder;
    private long maxDate;
    private  BpFinderModel finderModel;
    private ServiceClass uploadService;
    private MultipartBody.Part fileToUpload,videoToUploaded;
    private  String[] values;
    Bundle bundle;

    // method for add intent to arraylist
    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
            //Log.d(TAG, "Intent: " + intent.getAction() + " package: " + packageName);
        }
        return list;
    }

    public static Bitmap getImageFromResult(Context context, int resultCode,
                                            Intent imageReturnedIntent) {
        Log.d(TAG, "getImageFromResult, resultCode: " + resultCode);
        Bitmap bm = null;
        File imageFile = getTempFile(context);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage;
            boolean isCamera = (imageReturnedIntent == null ||
                    imageReturnedIntent.getData() == null ||
                    imageReturnedIntent.getData().toString().contains(imageFile.toString()));
            if (isCamera) {     /** CAMERA **/
                selectedImage = Uri.fromFile(imageFile);
            } else {            /** ALBUM **/
                selectedImage = imageReturnedIntent.getData();
            }
            Log.d(TAG, "selectedImage: " + selectedImage);

            bm = getImageResized(context, selectedImage);
            int rotation = getRotation(context, selectedImage, isCamera);
            bm = rotate(bm, rotation);
        }
        return bm;
    }

    private static File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), "BpProfileImage");
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }

    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);

        Log.d(TAG, options.inSampleSize + " sample method bitmap ... " +
                actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());

        return actuallyUsableBitmap;
    }

    /**
     * Resize to avoid using too much memory loading big images (e.g.: 2560*1920)
     **/
    private static Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm = null;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            Log.d(TAG, "resizer: new bitmap width = " + bm.getWidth());
            i++;
        } while (bm.getWidth() < 1024 && i < sampleSizes.length);
        return bm;
    }

    private static int getRotation(Context context, Uri imageUri, boolean isCamera) {
        int rotation;
        if (isCamera) {
            rotation = getRotationFromCamera(context, imageUri);
        } else {
            rotation = getRotationFromGallery(context, imageUri);
        }
        Log.d(TAG, "Image rotation: " + rotation);
        return rotation;
    }

    private static int getRotationFromCamera(Context context, Uri imageFile) {
        int rotate = 0;
        try {

            context.getContentResolver().notifyChange(imageFile, null);
            ExifInterface exif = new ExifInterface(imageFile.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static int getRotationFromGallery(Context context, Uri imageUri) {
        int result = 0;
        String[] columns = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int orientationColumnIndex = cursor.getColumnIndex(columns[0]);
                result = cursor.getInt(orientationColumnIndex);
            }
        } catch (Exception e) {
            //Do nothing
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }//End of try-catch block
        return result;
    }

    private static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bmOut = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            return bmOut;
        }
        return bm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editStatus = false;
    }



    @Override
    public void onResume() {
        super.onResume();
        if (bundle != null) {
            tabActivity = (TabActivity)getActivity();
            tabActivity.getSupportActionBar().setTitle(finderModel.getBpf_firstName()+"'s profile");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        token   = new PrefManager(getContext()).getToken();
        user_id = new PrefManager(getContext()).getUserId();
        initActivity(view);
        getActivity().getActionBar();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
        exoPlayer.addListener(this);
        dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
        extractorsFactory = new DefaultExtractorsFactory();
        playerView.hideController();
        playerView.setControllerAutoShow(false);

        progress = new ProgressDialog(getContext());

        playerView.setUseArtwork(true);
        playerView.setResizeMode(exoPlayer.getVideoScalingMode());

        bundle = getArguments();
        if (bundle != null) {
            finderModel = (BpFinderModel) bundle.getSerializable(AppConstants.USER_DETAIL);
            setViewForConnectedUSer();
        }else{
            setUp();
        }
        return view;
    }

    private void setViewForConnectedUSer() {
        try{
            //code for viewing profile of connected users
            imgEdit.setVisibility(View.INVISIBLE);
            imgProfile.setClickable(false);
            videoFrameLayout.setClickable(false);
            edit_tag.setVisibility(View.INVISIBLE);
            imgShare.setVisibility(View.INVISIBLE);
            imgShareText.setVisibility(View.INVISIBLE);

            userDataModel = new CoachProfileResponse();
            userDataModel.setId(finderModel.getBpf_id());
            userDataModel.setFirstName(finderModel.getBpf_firstName());
            userDataModel.setLastName(finderModel.getBpf_lastName());
            userDataModel.setGender(finderModel.getBpf_gender());
            userDataModel.setDob(finderModel.getBpf_dob());
            userDataModel.setCity(finderModel.getBpf_city());
            userDataModel.setPhoneNumber(finderModel.getBpf_phoneNumber());
            userDataModel.setLangKey(finderModel.getBpf_langKey());
            userDataModel.setLocation(finderModel.getBpf_city());
            //userDataModel.setSubscriptions(response.getString("subscriptions"));
            userDataModel.setImageUrl(finderModel.getBpf_imageUrl());
            userDataModel.setVideoUrl(finderModel.getBpf_videoUrl());
            userDataModel.setUserType(finderModel.getBpf_userType());
            userDataModel.setFcmToken(finderModel.getBpf_fcmToken());
            userDataModel.setAuthToken(finderModel.getBpf_authToken());
            userDataModel.setDeviceId(finderModel.getBpf_deviceId());
            userDataModel.setEmail(finderModel.getBpf_email());
            if (!finderModel.getUserProfile().equals(null) && !finderModel.getUserProfile().equals("null")) {
                UserProfileModel tempFinderModel = finderModel.getUserProfile();
                if (tempFinderModel != null) {
                    userDataModel.setUserProfile(tempFinderModel);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        setViews();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof TabActivity) {
            tabActivity = (TabActivity)getActivity();
            tabActivity.setActionBarTitle("Profile");
        }
        //setViews();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            exoPlayer.removeListener(this);
            exoPlayer.stop();
            exoPlayer.release();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            exoPlayer.removeListener(this);
            exoPlayer.stop();
            exoPlayer.release();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            exoPlayer.removeListener(this);
            exoPlayer.stop();
            exoPlayer.release();

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            exoPlayer.addListener(this);
            dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            extractorsFactory = new DefaultExtractorsFactory();
            playerView.hideController();
            playerView.setControllerAutoShow(false);


            playerView.setUseArtwork(true);
            playerView.setResizeMode(exoPlayer.getVideoScalingMode());
            bundle = getArguments();
            if (bundle != null) {
                finderModel = (BpFinderModel) bundle.getSerializable(AppConstants.USER_DETAIL);
                setViewForConnectedUSer();
            }else{
                setUp();
            }


//            if(userDataModel.getVideoUrl()!=null){
//                playVideo(userDataModel.getVideoUrl());
//            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }

//        asyncTask.setListener(null);
    }

    private void initActivity(final View view) {

        scrollview_profile = view.findViewById(R.id.scrollview_profile);
        btnsBottom         = view.findViewById(R.id.btns_at_bottom);
        more_info_btns_bottom= view.findViewById(R.id.more_info_btns_bottom);
        imgEdit             = view.findViewById(R.id.edit);
        profile_img_editIcon= view.findViewById(R.id.edit_profile_img_vid);
        imgProfile          = view.findViewById(R.id.row_icon);

        profileName         = view.findViewById(R.id.profile_name);
        profileDesig        = view.findViewById(R.id.profile_designation);
        edit_tag            = view.findViewById(R.id.edit_text);
        progressbar         = view.findViewById(R.id.progressBar);
        videoFrameLayout    = view.findViewById(R.id.header_cover_video);
        imgVideo            = view.findViewById(R.id.imgVideo);
        //videoView   = (VideoView) view.findViewById(R.id.videoView);
        playerView          = view.findViewById(R.id.exoplayer_profile);

        imgPlay             = view.findViewById(R.id.imgPlay);
        imgShare            = view.findViewById(R.id.menu_blue);
        imgShareText        = view.findViewById(R.id.img_share_text);
        fabImage            = view.findViewById(R.id.fab_image);
        fabVideo            = view.findViewById(R.id.fab_video);
        llMenuBasic         = view.findViewById(R.id.llMenuBasic);
        llMenuMore          = view.findViewById(R.id.llMenuMore);

        basic_info_tab      = view.findViewById(R.id.basic_info_tab);
        more_info_tab       = view.findViewById(R.id.more_info_tab);
        llBasicDetails      = view.findViewById(R.id.llBasicDetails);
        llMoreDetails       = view.findViewById(R.id.llMoreInfoDetails);
        viewBasic           = view.findViewById(R.id.viewBasic);
        viewMore            = view.findViewById(R.id.viewMore);
        placeholder         = view.findViewById(R.id.placeholder);
        //For Basic Details
        editFname           = view.findViewById(R.id.txtvFname);
        editLname           = view.findViewById(R.id.txtvLname);
        editGender          = view.findViewById(R.id.txtv_gender);
        editDob             = view.findViewById(R.id.txtv_dob);
        editCity            = view.findViewById(R.id.txtv_city_profile);
        editPhone           = view.findViewById(R.id.txtv_mobileno);
        //editPassword      = view.findViewById(R.id.txtv_password);
        basicBtnSave        = view.findViewById(R.id.btnsave);
        basicBtnCancel      = view.findViewById(R.id.btncancel);


        //Fore More Deatsils

        spinnerExp           = view.findViewById(R.id.spinner_exp);
        spinnerPref          = view.findViewById(R.id.spinner_pref);
        spinnerPositon       = view.findViewById(R.id.spinner_positon);
        spinnerTLInterest    = view.findViewById(R.id.spinner_tl_interest);
        spinnerTourRating    = view.findViewById(R.id.spinner_tour_rating);
        spinnerWtoTravel     = view.findViewById(R.id.spinner_Wto_travel);
        editHeight           = view.findViewById(R.id.txtvHeight);

        editPlayed           = view.findViewById(R.id.txtvPlayed);
        editCBVANo           = view.findViewById(R.id.txtvCBVANo);
        editCBVAFName        = view.findViewById(R.id.txtvCBVAFName);
        editCBVALName        = view.findViewById(R.id.txtvCBVALName);
        editHighschool       = view.findViewById(R.id.txtvHighschool);
        editIndoorClub       = view.findViewById(R.id.txtvIndoorClub);
        editColgClub         = view.findViewById(R.id.txtvColgClub);
        editColgBeach        = view.findViewById(R.id.txtvColgBeach);
        editColgIndoor       = view.findViewById(R.id.txtvColgIndoor);
        edit_volleyRanking   = view.findViewById(R.id.txtvRank);
        editPoints           = view.findViewById(R.id.txtvPoints);
        topfinishes_txt_1    = view.findViewById(R.id.topfinishes_txt_1);
        topfinishes_txt_2    = view.findViewById(R.id.topfinishes_txt_2);
        topfinishes_txt_3    = view.findViewById(R.id.topfinishes_txt_3);

        moreBtnSave          = view.findViewById(R.id.btn_save);
        moreBtnCancel        = view.findViewById(R.id.btn_cancel);
        imageView1           = view.findViewById(R.id.imageView1);
        imageView2           = view.findViewById(R.id.imageView2);
        imageView3           = view.findViewById(R.id.imageView3);

        containingLt         = view.findViewById(R.id.scroll_more);
        topFinishes1_lt      = view.findViewById(R.id.topFinishes1_lt);
        topFinishes2_lt      = view.findViewById(R.id.topFinishes2_lt);
        topFinishes3_lt      = view.findViewById(R.id.topFinishes3_lt);

        llMenuBasic.setOnClickListener(this);
        llMenuMore.setOnClickListener(this);
        spinnerExp.setEnabled(false);
        spinnerPref.setEnabled(false);
        spinnerPositon.setEnabled(false);
        spinnerTLInterest.setEnabled(false);
        spinnerTourRating.setEnabled(false);
        spinnerWtoTravel.setEnabled(false);
        editHeight.setEnabled(false);
        editCity.setEnabled(false);
        spinnerInit();
        List<String>stateList = AppConstants.getstatelist();

        imgShare.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgShare.isOpened()) {
                    //Toast.makeText(getActivity(), imgShare.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }
                imgShare.toggle(true);
            }
        });
        //share image
        fabImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Image", Toast.LENGTH_SHORT).show();
                //  if (selectedImageUri != null || userDataModel.getImageUrl()!=null ) {
                if (myProfileImageFile != null) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "https://www.beachpartner.com/preregistration/");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "BeachPartner App");
                    // intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myProfileImageFile));
                    intent.setType("image/*");
                    Uri apkURI = FileProvider.getUriForFile(
                            getContext(),
                            getContext().getPackageName()+".provider", myProfileImageFile);
                    intent.putExtra(Intent.EXTRA_STREAM, apkURI);
                    // intent.setDataAndType(apkURI, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, "Share image via..."));
                } else {
                    Toast.makeText(getActivity(), "Please upload Image and share it", Toast.LENGTH_SHORT).show();
                    imgShare.close(true);
                }
            }
        });

        //Share video
        fabVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Video", Toast.LENGTH_SHORT).show();
                // if (selectedVideoUri != null  || userDataModel.getVideoUrl()!=null ) {
                if (myProfileVideFile != null) {
                    //       screenshotVideoUri= Uri.parse(String.valueOf(selectedVideoUri));
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "https://www.beachpartner.com/preregistration/");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "BeachPartner App");
                    intent.setType("video/*");
                    //   intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myProfileVideFile)); // deprecated
                    Uri apkURI = FileProvider.getUriForFile(
                            getContext(),
                            getContext().getPackageName()+".provider", myProfileVideFile);
                    intent.putExtra(Intent.EXTRA_STREAM, apkURI);
                    // intent.setDataAndType(apkURI, "video/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, "Share video via..."));


                       /* ShareLinkContent contentvideo = new ShareLinkContent.Builder()
                                .setQuote("BeachPartner")
                                .setContentTitle("BeachPartner")
                                .setImageUrl(Uri.parse("https://beachpartner.com/preregistration/"))
                                .setContentUrl(screenshotVideoUri)
                                .build();
                        ShareDialog.show(ProfileFragment.this,contentvideo);*/
                   /* } else if (userDataModel.getVideoUrl() != null) {


                        screenshotVideoUri = Uri.parse(userDataModel.getVideoUrl());
                     *//*   Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Hey view/download this image");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myProfileVideFile));
                        intent.setType("video*//**//*");*//*
                     //   startActivity(Intent.createChooser(intent, "Share image via..."));
                       *//* ShareLinkContent contentvideo = new ShareLinkContent.Builder()
                                .setQuote("BeachPartner")
                                .setContentTitle("BeachPartner")
                                .setImageUrl(Uri.parse("https://beachpartner.com/preregistration/"))
                                .setContentUrl(screenshotVideoUri)
                                .build();
                        ShareDialog.show(ProfileFragment.this,contentvideo);*//*
                        SharePhoto photo = new SharePhoto.Builder()
                                .setImageUrl(Uri.fromFile(myProfileImageFile))
                                .setCaption("Hi This Imahe")
                                .build();
                        SharePhotoContent content = new SharePhotoContent.Builder()
                                .addPhoto(photo).setRef("gghfgfgh")
                                .build();
                        ShareDialog shareDialog = new ShareDialog(ProfileFragment.this);
                        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                    }*/


                } else {
                    Toast.makeText(getActivity(), "Please upload Video and share it", Toast.LENGTH_SHORT).show();
                    imgShare.close(true);

                }

            }
        });


        dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_style, stateList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        editCity.setAdapter(dataAdapter);
        editCity.invalidate();
        editCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                location = editCity.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         /*This for demo only end*/
        // setupViewPager(viewPager);
        // tabs.setupWithViewPager(viewPager);

//        imgEdit.setOnClickListener(this);
        //   imgVideo.setOnClickListener(this);
        videoFrameLayout.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        imgPlay.setOnClickListener(this);
//        Buttons click action for saving
        basicBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoSave();
                // editStatus = !editStatus;
            }
        });
        moreBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoSave();
                //editStatus = !editStatus;
            }
        });

        basicBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoCancelChange();
                //editStatus = true;
            }
        });
        moreBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoCancelChange();
                //editStatus = true;
            }
        });
        //edit profile button(ImageView)
        imgEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!editStatus) {
                    editCustomView();
                    editBasicInfo();
                    editMoreInfo();
                    spinnerInit();
                    imgEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_active));
                    edit_tag.setTextColor(getResources().getColor(R.color.btnColor));
                    edit_tag.setText("Edit Profile");
                    editStatus = !editStatus;
                } /*else {
                    // InfoCancelChange();
                    //InfoSave();
                    imgEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
                    edit_tag.setTextColor(getResources().getColor(R.color.btnColor));
                    edit_tag.setText("Edit Profile");

                }*/
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

//                else{
//                    String myFormat = "MM-dd-yyyy"; //In which you need put here
//                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//                    Date d = null;
//                    try {
//                        d = sdf.parse(d1);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    myCalendar.setTime(d);
//                }


                updateLabel();

            }

        };
        //dob date
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        // Subtract 5 years
        c.add( Calendar.YEAR, -5 ) ;
        maxDate = c.getTime().getTime();
        editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DatePickerDialog dialog = new DatePickerDialog(getContext(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                dialog.getDatePicker().setMaxDate(maxDate);
//                dialog.show();

                CustomDatePicker();
            }
        });

        editGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.gender_popup_layout, null);

                final RadioGroup radioGroup_gender = (RadioGroup) alertLayout.findViewById(R.id.gender_radio_group);
                final RadioButton radioButton_male = (RadioButton) alertLayout.findViewById(R.id.rd_1);
                final RadioButton female = (RadioButton) alertLayout.findViewById(R.id.rd_2);

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
                String titleText = "Select Gender!";

                // Initialize a new foreground color span instance
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blueDark));

                // Initialize a new spannable string builder instance
                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
                // Apply the text color span
                ssBuilder.setSpan(
                        foregroundColorSpan,
                        0,
                        titleText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                alert.setTitle(ssBuilder);
                alert.setView(alertLayout);
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (radioGroup_gender.getCheckedRadioButtonId() == radioButton_male.getId())
                            editGender.setText("Male");
                        else {
                            editGender.setText("Female");
                        }
                    }

                });

                final AlertDialog dialog = alert.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {

                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blueDark));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blueDark));
                    }
                });
                dialog.show();


            }

        });

//        editPlayed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toursPlayed();
//            }
//        });


        //Browse video from gallery
        /*imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onItemClick(View view) {

                Intent intent= new Intent();
                intent.setType("video*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);

            }
        });*/

        //browse profile picture from  gallery
        /*imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onItemClick(View view) {
                Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),REQUEST_TAKE_GALLERY_IMAGE);
            }
        });*/

        //play video
       /* imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onItemClick(View view) {
                videoView.start();
            }
        });
        */

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                finishCount += 1;
                if (finishCount == 1) {
                    imageView2.setVisibility(View.VISIBLE);
                    topFinishes2_lt.setVisibility(View.VISIBLE);
                    appendEditOpenedOne = true;
                    Log.i(TAG,"ONE OPEN");
                } else if (finishCount == 2) {
                    imageView1.setVisibility(View.GONE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.VISIBLE);

                    topFinishes2_lt.setVisibility(View.VISIBLE);
                    topFinishes3_lt.setVisibility(View.VISIBLE);
                    appendEditOpenedTwo = true;
                    Log.i(TAG,"TWO OPEN");
                } else {
                    topFinishes2_lt.setVisibility(View.GONE);
                    topFinishes3_lt.setVisibility(View.GONE);
                }

            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCount -= 1;
                topFinishes2_lt.setVisibility(View.GONE);
                topfinishes_txt_2.setText(null);

                    imageView1.setVisibility(View.VISIBLE);

            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCount -= 1;
                topFinishes3_lt.setVisibility(View.GONE);
                topfinishes_txt_3.setText(null);
                if (finishCount < 2) {
                    imageView1.setVisibility(View.VISIBLE);
                }
            }
        });


    }


    private void CustomDatePicker(){
        LayoutInflater inflater = getLayoutInflater();
        View datealertLayout    = inflater.inflate(R.layout.custom_date_picker_dialog, null);
        final DatePicker dp     =  datealertLayout.findViewById(R.id.datePicker_custom);
        final Button  okBtn     =  datealertLayout.findViewById(R.id.okBtn);
        final Button  cancelBtn =  datealertLayout.findViewById(R.id.cancel_button);
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getContext());
        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blueDark));
        dp.setMaxDate(maxDate);
        dp.init(myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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
            updateLabel();
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

    private void spinnerInit(){
                /*Experience Spinner*/
        List<String> experience = AppConstants.getExperienceLevels();

        expAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style, experience);
        expAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExp.setAdapter(expAdapter);
        spinnerExp.invalidate();
        spinnerExp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                moreUploadStatus = true;
                if(!editStatus){
                    if (spinnerExp.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                        spinnerExp.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    spinnerExp.setVisibility(View.VISIBLE);

                }
                if (!spinnerExp.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                    spinnerExpValue = spinnerExp.getSelectedItem().toString();
                } else {
                    int spinnerExpPos = expAdapter.getPosition(spinnerExpValue);
                    spinnerExp.setSelection(spinnerExpPos);
                    spinnerExpValue = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        /*Court Preference spinner*/
        spinnerPref.setOnItemSelectedListener(this);
        courtPref = AppConstants.getCourtSidePreference();
        prefAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_style, courtPref);
        prefAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPref.setAdapter(prefAdapter);
        spinnerPref.invalidate();
        spinnerPref.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                moreUploadStatus = true;
                if(!editStatus){
                    if(spinnerPref.getSelectedItem().toString().equalsIgnoreCase("Please Select")){
                        spinnerPref.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    spinnerPref.setVisibility(View.VISIBLE);
                }
                if (!spinnerPref.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                    spinnerPrefValue = spinnerPref.getSelectedItem().toString();
                } else {
                    int spinnerpref = prefAdapter.getPosition(spinnerPrefValue);
                    spinnerPref.setSelection(spinnerpref);
                    spinnerPrefValue = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        /*position Spinner*/
        spinnerPositon.setOnItemSelectedListener(this);
        List<String> position = AppConstants.getPlayerPosition();
        positionAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_style, position);
        positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPositon.setAdapter(positionAdapter);
        spinnerPositon.invalidate();
        spinnerPositon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                moreUploadStatus = true;
                if(!editStatus){
                    if(spinnerPositon.getSelectedItem().toString().equalsIgnoreCase("Please Select")){
                        spinnerPositon.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    spinnerPositon.setVisibility(View.VISIBLE);

                }
                if (!spinnerPositon.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                    spinnerPosValue = spinnerPositon.getSelectedItem().toString();
                } else {
                    int spinnerposition = positionAdapter.getPosition(spinnerPosValue);
                    spinnerPositon.setSelection(spinnerposition);
                    spinnerPosValue = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        /*Tournament Level interest spinner*/
        spinnerTLInterest.setOnItemSelectedListener(this);
        List<String> tournamentInterest = AppConstants.getTournamentLevels();
        tournamentInterestAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_style, tournamentInterest);
        tournamentInterestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTLInterest.setAdapter(tournamentInterestAdapter);
        spinnerTLInterest.invalidate();
        spinnerTLInterest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                moreUploadStatus = true;
                if(!editStatus){
                    if (spinnerTLInterest.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                        spinnerTLInterest.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    spinnerTLInterest.setVisibility(View.VISIBLE);
                }
                if (!spinnerTLInterest.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                    spinnerTLValue = spinnerTLInterest.getSelectedItem().toString();
                } else {
                    int spinnerTLValuePos = tournamentInterestAdapter.getPosition(spinnerTLValue);
                    spinnerTLInterest.setSelection(spinnerTLValuePos);
                    spinnerTLValue = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        spinnerTourRating.setOnItemSelectedListener(this);
        List<String> rating = AppConstants.getTournamentRating();
        highestRatingAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_style, rating);
        highestRatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTourRating.setAdapter(highestRatingAdapter);
        spinnerTourRating.invalidate();

        spinnerTourRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                moreUploadStatus = true;
                if(!editStatus){
                    if (spinnerTourRating.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                        spinnerTourRating.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    spinnerTourRating.setVisibility(View.VISIBLE);
                }

                if (!spinnerTourRating.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                    spinnerTRValue = spinnerTourRating.getSelectedItem().toString();
                } else {
                    int spinnerTRValuePos = highestRatingAdapter.getPosition(spinnerTRValue);
                    spinnerTourRating.setSelection(spinnerTRValuePos);
                    spinnerTRValue = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        /*Spinner for willing to travel*/
        spinnerWtoTravel.setOnItemSelectedListener(this);
        List<String> distance = AppConstants.getTravelList();
        distanceAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_style, distance);
        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWtoTravel.setAdapter(distanceAdapter);
        spinnerWtoTravel.invalidate();
        spinnerWtoTravel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                moreUploadStatus = true;
                if(!editStatus){
                    if (spinnerWtoTravel.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                        spinnerWtoTravel.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    spinnerWtoTravel.setVisibility(View.VISIBLE);
                }
                if (!spinnerWtoTravel.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                    spinnerWTValue = spinnerWtoTravel.getSelectedItem().toString();
                } else {
                    int travelValuePos = distanceAdapter.getPosition(spinnerWTValue);
                    spinnerWtoTravel.setSelection(travelValuePos);
                    spinnerWTValue = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        List<String>height = AppConstants.getHeightList();
        heightAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_style, height);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editHeight.setAdapter(heightAdapter);
        editHeight.invalidate();
        editHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                moreUploadStatus = true;
                if(!editStatus){
                    if (editHeight.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                        editHeight.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    editHeight.setVisibility(View.VISIBLE);
                }
                if (!editHeight.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                    editHeightValue = editHeight.getSelectedItem().toString();
                } else {
                    int editHeightValuePos = heightAdapter.getPosition(editHeightValue);
                    editHeight.setSelection(editHeightValuePos);
                    editHeightValue = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }
    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getChildFragmentManager());
        // adapter.addFragment(new BasicInfoFragment(),"Basic Information");
        //adapter.addFragment(new MoreInfoFragment(),"More Information");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        menus.add(imgShare);



       /* menuYellow.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                String text;
                if (opened) {
                    text = "Menu opened";
                } else {
                    text = "Menu closed";
                }
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });*/

        /*fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);*/

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabEdit.show(true);
            }
        }, delay + 150);

        menuRed.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onItemClick(View v) {
                if (menuRed.isOpened()) {
                    Toast.makeText(getActivity(), menuRed.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }

                menuRed.toggle(true);
            }
        });*/

        createCustomAnimation();
    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        /*ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menuGreen.getMenuIconView().setImageResource(menuGreen.isOpened()
                        ? R.drawable.ic_close : R.drawable.ic_star);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menuGreen.setIconToggleAnimatorSet(set);*/
    }

    @Override
    public void onClick(View view) {


        String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        switch (view.getId()) {

            // case R.id.imgVideo:
            case R.id.header_cover_video:

                if (!hasPermissions(getActivity(), PERMISSIONS)) {
                    requestPermissions(PERMISSIONS, 20);
                } else {
                    Intent chooseVideoIntent = getPickImageIntent(getActivity().getApplicationContext(), "videoIntent");
                    chooseVideoIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);

                }
                break;
            case R.id.row_icon:
                //if (editStatus) {
                if (!hasPermissions(getActivity(), PERMISSIONS)) {
                    requestPermissions(PERMISSIONS, 21);
                } else {
                    Intent chooseImageIntent = getPickImageIntent(getActivity().getApplicationContext(), "imageIntent");
                    chooseImageIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);

                    startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST);
                }

                //}
                break;
            case R.id.imgPlay:
                // videoView.setVisibility(View.VISIBLE);
                //playVideo();
                // videoView.start();
                break;
            //Demo Only
            case R.id.llMenuBasic:
                llMoreDetails.setVisibility(View.GONE);
                llBasicDetails.setVisibility(View.VISIBLE);
                viewBasic.setBackgroundColor(getResources().getColor(R.color.blueDark));
                viewMore.setBackgroundColor(getResources().getColor(R.color.white));
                basic_info_tab.setTextColor(getResources().getColor(R.color.blueDark));
                more_info_tab.setTextColor(getResources().getColor(R.color.darkGrey));
                break;
            case R.id.llMenuMore:
                llBasicDetails.setVisibility(View.GONE);
                llMoreDetails.setVisibility(View.VISIBLE);
                viewMore.setBackgroundColor(getResources().getColor(R.color.blueDark));
                viewBasic.setBackgroundColor(getResources().getColor(R.color.white));
                more_info_tab.setTextColor(getResources().getColor(R.color.blueDark));
                basic_info_tab.setTextColor(getResources().getColor(R.color.darkGrey));
                break;
            default:
                break;
        }
    }

    private void playVideo(String videoURL) {
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL), dataSourceFactory, extractorsFactory, null, null);
        playerView.setPlayer(exoPlayer);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        playerView.setUseController(false);
        exoPlayer.setVolume(0);
    }

    private void playVideoFromFile(Uri fileURL) {
        placeholder.setVisibility(View.GONE);
        DataSpec dataSpec = new DataSpec(fileURL);
        final FileDataSource fileDataSource = new FileDataSource();
        try {
            fileDataSource.open(dataSpec);
        } catch (FileDataSource.FileDataSourceException e) {
            e.printStackTrace();
        }
        try{
            DataSource.Factory factory = new DataSource.Factory() {
                @Override
                public DataSource createDataSource() {
                    return fileDataSource;
                }
            };
            MediaSource audioSource = new ExtractorMediaSource(fileDataSource.getUri(),
                    factory, new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(audioSource);
            playerView.setPlayer(exoPlayer);
            exoPlayer.prepare(audioSource);
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
            playerView.setUseController(false);
            exoPlayer.setVolume(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    private void videoBrowse() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("video/*");
        startActivityForResult(galleryIntent, PICK_VIDEO_REQUEST);
    }

    private boolean checkExternalDrivePermission(final int type) {

        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) || (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, type);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, type);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 21: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent chooseImageIntent = getPickImageIntent(getActivity().getApplicationContext(), "imageIntent");
                    chooseImageIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);

                    startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST);

                } else {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary otherwise image upload functionality fails ");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                }
                return;
            }
            case 20: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    Intent chooseVideoIntent = getPickImageIntent(getActivity().getApplicationContext(), "videoIntent");
                    chooseVideoIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);

                    startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);

                } else {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary otherwise video upload functionality fails ");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_VIDEO_REQUEST);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case REQUEST_SAVEIMGTODRIVE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary otherwise video upload functionality fails ");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_SAVEIMGTODRIVE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
        }
    }

    private void editCustomView() {

        //  imgVideo.setVisibility(View.VISIBLE);
        //profile_img_editIcon.setVisibility(View.VISIBLE);
        imgEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_active));
        edit_tag.setTextColor(getResources().getColor(R.color.btnColor));
    }

    public void updateLabel() {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editDob.setText(sdf.format(myCalendar.getTime()));
    }

    //tabHost.addTab(tabHost.newTabSpec("basicInfo").setIndicator("Basic Information").setContent());
    //tabHost.addTab(tabHost.newTabSpec("moreInfo").setIndicator("More Information").setContent());


    ArrayList seletedItems = new ArrayList();
    boolean[] checkedItem = new boolean[items.length];
    boolean unchecked = false;
    private int getIndex(CharSequence[] items, CharSequence sequence) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(sequence))
                return i;
        }
        return -1;
    }
    private void setSelectedToursFromServer(String selectedTours) {
        ArrayList<String >mToursSelectedfromServer = new ArrayList<>(Arrays.asList(selectedTours.split("\\s*,\\s*")));
        mToursSelectedfromServer.removeAll(Arrays.asList(null, ""));
        Log.d("size", String.valueOf(mToursSelectedfromServer.size()));
        Set<String> mToursSelectedfromServerHash = new HashSet<>();
        mToursSelectedfromServerHash.addAll(mToursSelectedfromServer);
        mToursSelectedfromServer.clear();
        mToursSelectedfromServer.addAll(mToursSelectedfromServerHash);
        Log.d("size hash", String.valueOf(mToursSelectedfromServer.size()));
        ListIterator<String> iterator = mToursSelectedfromServer.listIterator();
        while (iterator.hasNext()) {
            int position = getIndex(items, iterator.next());
            if (position != -1){
                //selectedItems.add(position);
                if (selectedItems ==null){
                    selectedItems = new ArrayList();
                }
                selectedItems.add(items[position]);
                checkedItem[position] = true;
            }

        }
    }
    public void toursPlayed() {
        // final CharSequence[] items = {"AVP Next", "AVP First", "CBVA Adult", "CBVA Junior", "AAU", "BVCA","Relentless","BVNE","LC","USAV","Volley America","Beach Elite","United States Association of Volleyball (USAV)","Amateur Athletic Union (AAU)","Association of Volleyball Professionals (AVP)","Extreme Volleyball Professionals (EVP)","National Volleyball League (NVL)","VolleyAmerica","Beach Volleyball National Events (BVNE)","Rox Volleyball Series","California Beach Volleyball Association","Volley OC","Northern California Volleyball Association","Beach Elite/Endless Summer","Beach Volleyball Clubs of American (BVCA)","Junior Volleyball Association (JVA)","Beach Volleyball San Diego","Gulf coast Volleyball Association (GCVA)","tArizona Tournaments","The Island Volleyball","Florida Tournaments","Northeast Volleyball Qualifier","North East Beach Volleyball","Precision Sand Volleyball","AVA","Wasatch Beach Volleyball","Ohio Valley Region","Wisconsin Juniors","AlohaRegionJuniors","Ohio Valley Region","Wisconsin Juniors","AlohaRegionJuniors"};
// arraylist to keep the selected items



        final AlertDialog dialog = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("Select-Tours Played in")
                .setMultiChoiceItems(items, checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            seletedItems.add(indexSelected);
                            checkedItem[indexSelected] = true;
                            Log.d(TAG,"CHECKED");
                        } else if (seletedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            Log.d(TAG,"UNCHECKED-ITEM-CONTAINED");
                            seletedItems.remove(Integer.valueOf(indexSelected));
                            checkedItem[indexSelected] = false;
                        }else {
                            Log.d(TAG,"UNCHECKED-ITEM-NOT-CONTAINED");
                            seletedItems.remove(Integer.valueOf(indexSelected));
                            checkedItem[indexSelected] = false;
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        // Edit Text and array list should be cleared on every Tour selection
                        editPlayed.setText("");
                        for(int i=0;i<checkedItem.length;i++){
                            if (checkedItem[i] == true){
                                editPlayed.append(items[i]+",");
                            }
                            Log.i(TAG, String.valueOf(checkedItem[i]));
                        }
                        return;
                        /*for(int i = 0;i<seletedItems.size();i++){
                                if (i != seletedItems.size()-1)
                                    editPlayed.append(items[(int) seletedItems.get(i)]+",");
                                else
                                    editPlayed.append(items[(int) seletedItems.get(i)]+"");
                        }*/
                        //editPlayed.setText(selectedTours);

                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blueDark));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blueDark));
            }
        });

        dialog.show();
        unchecked = false;

    }

    private void reloadFragment() {
        if (getActivity() instanceof TabActivity) {
            TabActivity tabActivity = (TabActivity) getActivity();
            tabActivity.loadProfileFragment();
        }
    }

    private void editBasicInfo() {

        //Profile image edit icon active

        imgProfile.setClickable(true);
        //  imgVideo.setClickable(true);
        btnsBottom.setVisibility(View.VISIBLE);
        more_info_btns_bottom.setVisibility(View.VISIBLE);
        //    profile_img_editIcon.setVisibility(View.VISIBLE);
        editFname.setEnabled(true);
        editFname.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editLname.setEnabled(true);
        editLname.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editGender.setEnabled(true);
        editGender.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editDob.setEnabled(true);
        editDob.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editCity.setEnabled(true);
        editCity.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editPhone.setEnabled(true);
        editPhone.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));

//        editPassword.setEnabled(true);
//        editPassword.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));

        editLname.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z ]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });
    }

    private void editMoreInfo() {

        btnsBottom.setVisibility(View.VISIBLE);
        more_info_btns_bottom.setVisibility(View.VISIBLE);
        spinnerExp.setEnabled(true);
        spinnerExp.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        spinnerPref.setEnabled(true);
        spinnerPref.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        spinnerPositon.setEnabled(true);
        spinnerPositon.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editHeight.setEnabled(true);
        editHeight.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        spinnerTLInterest.setEnabled(true);
        spinnerTLInterest.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editPlayed.setEnabled(true);
        editPlayed.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        spinnerTourRating.setEnabled(true);
        spinnerTourRating.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editCBVANo.setEnabled(true);
        editCBVANo.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editCBVAFName.setEnabled(true);
        editCBVAFName.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editCBVALName.setEnabled(true);
        editCBVALName.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        spinnerWtoTravel.setEnabled(true);
        spinnerWtoTravel.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editHighschool.setEnabled(true);
        editHighschool.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editIndoorClub.setEnabled(true);
        editIndoorClub.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editColgClub.setEnabled(true);
        editColgClub.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editColgBeach.setEnabled(true);
        editColgBeach.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editColgIndoor.setEnabled(true);
        editColgIndoor.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        editPoints.setEnabled(true);
        editPoints.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        edit_volleyRanking.setEnabled(true);
        edit_volleyRanking.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        topfinishes_txt_1.setEnabled(true);
        topfinishes_txt_1.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        if(finishCount<1){
            imageView1.setVisibility(View.VISIBLE);
        }
        imageView2.setVisibility(View.VISIBLE);
        imageView3.setVisibility(View.VISIBLE);
        topfinishes_txt_2.setEnabled(true);
        topfinishes_txt_2.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        imageView2.setVisibility(View.VISIBLE);
        topfinishes_txt_3.setEnabled(true);
        topfinishes_txt_3.setBackground(getResources().getDrawable(R.drawable.edit_test_bg));
        imageView3.setVisibility(View.VISIBLE);
    }


    //Saving information after edit
    public void InfoSave() {

    /*validating feilds*/

        if (!validate()) {

            // imgProfile.setClickable(false);
            //  profile_img_editIcon.setVisibility(View.GONE);
            //  imgVideo.setVisibility(View.GONE);
            btnsBottom.setVisibility(View.GONE);
            imgEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
            edit_tag.setTextColor(getResources().getColor(R.color.imgBacgnd));
            more_info_btns_bottom.setVisibility(View.GONE);
            //BasicInfo

            editFname.setEnabled(false);
            editFname.setBackground(null);
            editLname.setEnabled(false);
            editLname.setBackground(null);
            editGender.setEnabled(false);
            editGender.setBackground(null);
            editDob.setEnabled(false);
            editDob.setBackground(null);
            editCity.setEnabled(false);
            editCity.setBackground(null);
            editPhone.setEnabled(false);
            editPhone.setBackground(null);

//        editPassword.setEnabled(false);
//        editPassword.setBackground(null);
            //MoreInfo
            spinnerExp.setEnabled(false);
            spinnerExp.setBackground(null);
            spinnerPref.setEnabled(false);
            spinnerPref.setBackground(null);
            spinnerPositon.setEnabled(false);
            spinnerPositon.setBackground(null);
            editHeight.setEnabled(false);
            editHeight.setBackground(null);
            spinnerTLInterest.setEnabled(false);
            spinnerTLInterest.setBackground(null);
            editPlayed.setEnabled(false);
            editPlayed.setBackground(null);
            spinnerTourRating.setEnabled(false);
            spinnerTourRating.setBackground(null);
            editCBVANo.setEnabled(false);
            editCBVANo.setBackground(null);
            editCBVAFName.setEnabled(false);
            editCBVAFName.setBackground(null);
            editCBVALName.setEnabled(false);
            editCBVALName.setBackground(null);
            spinnerWtoTravel.setEnabled(false);
            spinnerWtoTravel.setBackground(null);
            editHighschool.setEnabled(false);
            editHighschool.setBackground(null);
            editIndoorClub.setEnabled(false);
            editIndoorClub.setBackground(null);
            editColgClub.setEnabled(false);
            editColgClub.setBackground(null);
            editColgBeach.setEnabled(false);
            editColgBeach.setBackground(null);
            editColgIndoor.setEnabled(false);
            editColgIndoor.setBackground(null);
            editPoints.setEnabled(false);
            editPoints.setBackground(null);
            edit_volleyRanking.setEnabled(false);
            edit_volleyRanking.setBackground(null);
            imageView1.setVisibility(View.GONE);
            topfinishes_txt_1.setEnabled(false);
            topfinishes_txt_1.setBackground(null);
            topfinishes_txt_2.setEnabled(false);
            topfinishes_txt_2.setBackground(null);
            imageView2.setVisibility(View.GONE);

            topfinishes_txt_3.setEnabled(false);
            topfinishes_txt_3.setBackground(null);
            imageView3.setVisibility(View.GONE);
            String dateOb = editDob.getText().toString();

            // Log.d("date--",c);

            //long date = Long.parseLong(dateOb);
            // DateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
            //         Locale.ENGLISH);

            //  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = null;
            Date dateLong = null;
            String stringDate = null;
            try {
                date = new SimpleDateFormat("MM-dd-yyyy").parse(dateOb);
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);  //2018-04-25T05:29:19.777Z
                stringDate = dateFormat.format(date);
                dateLong = dateFormat.parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            JSONObject object = new JSONObject();
            try {
                //object.put("activated",true);
                object.put("firstName", editFname.getText().toString().trim());
                object.put("lastName", editLname.getText().toString().trim());
                object.put("gender", editGender.getText().toString().trim());
                object.put("dob", stringDate);
                object.put("city", userDataModel.getCity());
                object.put("location", location.toString().trim());
                object.put("phoneNumber", editPhone.getText().toString().trim());
                object.put("imageUrl", userDataModel.getImageUrl());
                object.put("videoUrl", userDataModel.getVideoUrl());
                object.put("userType", userDataModel.getUserType().trim());
                object.put("langKey", userDataModel.getLangKey());
                object.put("fcmToken", userDataModel.getFcmToken().trim());
                object.put("email", userDataModel.getEmail().trim());
                object.put("deviceId", userDataModel.getDeviceId().trim());
                object.put("authToken", userDataModel.getAuthToken());
//                object.put("city",userDataModel.getLocation().trim());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject jsonObjectMore = new JSONObject();
            try {
                jsonObjectMore.put("cbvaFirstName", editCBVAFName.getText().toString().trim());
                jsonObjectMore.put("cbvaLastName", editCBVALName.getText().toString().trim());
                jsonObjectMore.put("cbvaPlayerNumber", editCBVANo.getText().toString().trim());
                jsonObjectMore.put("collage", "");
                jsonObjectMore.put("collageClub", editColgClub.getText().toString().trim());
                jsonObjectMore.put("collegeBeach", editColgBeach.getText().toString().trim());
                jsonObjectMore.put("collegeIndoor", editColgIndoor.getText().toString().trim());
                jsonObjectMore.put("courtSidePreference", spinnerPref.getSelectedItem());
                jsonObjectMore.put("description", "");
                jsonObjectMore.put("division", "");
                jsonObjectMore.put("experience", spinnerExp.getSelectedItem());
                jsonObjectMore.put("fundingStatus", "");
                jsonObjectMore.put("height", editHeight.getSelectedItem());
                jsonObjectMore.put("highSchoolAttended", editHighschool.getText().toString().trim());
                jsonObjectMore.put("highestTourRatingEarned", spinnerTourRating.getSelectedItem());
                jsonObjectMore.put("indoorClubPlayed", editIndoorClub.getText().toString().trim());
                jsonObjectMore.put("numOfAthlets", "");
                jsonObjectMore.put("position", spinnerPositon.getSelectedItem());
                jsonObjectMore.put("programsOffered", "");
                jsonObjectMore.put("shareAthlets", "");
                StringBuffer topFinishes = new StringBuffer();
                // jsonObjectMore.put("topFinishes", topfinishes_txt_1.getText().toString().trim()+",
                // "+topfinishes_txt_2.getText().toString().trim()+","+topfinishes_txt_3.getText().toString().trim());
                if (!topfinishes_txt_1.getText().toString().trim().isEmpty()) {
                    topFinishes.append(topfinishes_txt_1.getText().toString().trim() + ",");
                }
                if (!topfinishes_txt_2.getText().toString().trim().isEmpty()) {
                    topFinishes.append(topfinishes_txt_2.getText().toString().trim() + ",");
                }
                if (!topfinishes_txt_3.getText().toString().trim().isEmpty()) {
                    topFinishes.append(topfinishes_txt_3.getText().toString().trim());
                }
                jsonObjectMore.put("topFinishes", topFinishes.toString());
                jsonObjectMore.put("totalPoints", editPoints.getText().toString().trim());
                jsonObjectMore.put("tournamentLevelInterest", spinnerTLValue);
                jsonObjectMore.put("toursPlayedIn", editPlayed.getText().toString().trim());
                jsonObjectMore.put("usaVolleyballRanking", edit_volleyRanking.getText().toString().trim());
                jsonObjectMore.put("willingToTravel", spinnerWtoTravel.getSelectedItem());
                jsonObjectMore.put("yearsRunning", "");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //update user fields
            JSONObject jsonallobj = new JSONObject();
            try {
                jsonallobj.put("userInputDto", object);
                jsonallobj.put("userProfileDto", jsonObjectMore);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            updateAllUserDetails(jsonallobj);

        } else {

            editStatus = true;
            imgEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_active));
            edit_tag.setTextColor(getResources().getColor(R.color.btnColor));
            edit_tag.setText("Save Profile");
            editCustomView();
            editBasicInfo();
            editMoreInfo();

        }


    }

    private boolean validate() {
        isValidate = false;
        if (editFname.getText().toString().trim().matches("")) {
            editFname.setError("Please enter your First name");
            isValidate = true;
        } else if (editLname.getText().toString().trim().matches("")) {
            editLname.setError("Please enter your Last name");
            isValidate = true;
        } else if (editGender.getText().toString().trim().matches("")) {
            editGender.setError("Please Choose Gender");
            isValidate = true;
        } else if (location.toString().trim().matches("")) {
            isValidate = true;
        } else if (editPhone.getText().toString().trim().isEmpty()) {
            editPhone.setError("Please enter your Mobile no");
            isValidate = true;
        } else if (editDob.getText().toString().trim().matches("")) {
            editDob.setError("Please enter your dob");
            isValidate = true;
        } else if (!editPhone.getText().toString().trim().isEmpty()) {
            String mobile = editPhone.getText().toString();
            if (!AppCommon.isValidMobileNumber(mobile)){
                editPhone.setError(getString(R.string.mobilerror));
                isValidate = true;
            }
            /*if (!Pattern.matches(AppCommon.MOBILE_REGEX_PATTERN, mobile)) {
                editPhone.setError(getString(R.string.mobilerror));
                isValidate = true;
            }*/
        }
//        else if(selectedImageUri == null){
//            Toast.makeText(getActivity(), "Please upload a picture", Toast.LENGTH_SHORT).show();
//            isValidate = true;
//        }else if(selectedVideoUri == null){
//            Toast.makeText(getActivity(), "Please upload a Video", Toast.LENGTH_SHORT).show();
//            isValidate = true;
//        }
        return isValidate;
    }

    private void nullCheck() {

    }

    private void validateFeilds() {
        awesomeValidation.addValidation(getActivity(), R.id.txtvFname, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(getActivity(), R.id.txtvLname, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.lnameerror);
        awesomeValidation.addValidation(getActivity(), R.id.txtv_gender, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.gendererror);
        awesomeValidation.addValidation(getActivity(), R.id.txtv_city, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.cityerror);
        awesomeValidation.addValidation(getActivity(), R.id.txtv_mobileno, "^[1-9]{2}[0-9]{8}$", R.string.mobilerror);
    }

    public void InfoCancelChange() {

        //  profile_img_editIcon.setVisibility(View.GONE);
        //  imgVideo.setVisibility(View.GONE);
        btnsBottom.setVisibility(View.GONE);
        imgEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
        edit_tag.setTextColor(getResources().getColor(R.color.imgBacgnd));
        edit_tag.setText("Edit Profile");
        more_info_btns_bottom.setVisibility(View.GONE);
        //BasicInfo
        editFname.setEnabled(false);
        editFname.setBackground(null);
        editLname.setEnabled(false);
        editLname.setBackground(null);
        editGender.setEnabled(false);
        editGender.setBackground(null);
        editDob.setEnabled(false);
        editDob.setBackground(null);
        editCity.setEnabled(false);
        editCity.setBackground(null);
        editPhone.setEnabled(false);
        editPhone.setBackground(null);

//        editPassword.setEnabled(false);
//        editPassword.setBackground(null);


        //MoreInfo
        spinnerExp.setEnabled(false);
        spinnerExp.setBackground(null);
        spinnerPref.setEnabled(false);
        spinnerPref.setBackground(null);
        spinnerPositon.setEnabled(false);
        spinnerPositon.setBackground(null);
        editHeight.setEnabled(false);
        editHeight.setBackground(null);
        spinnerTLInterest.setEnabled(false);
        spinnerTLInterest.setBackground(null);
        editPlayed.setEnabled(false);
        editPlayed.setBackground(null);
        spinnerTourRating.setEnabled(false);
        spinnerTourRating.setBackground(null);
        editCBVANo.setEnabled(false);
        editCBVANo.setBackground(null);
        editCBVAFName.setEnabled(false);
        editCBVAFName.setBackground(null);
        editCBVALName.setEnabled(false);
        editCBVALName.setBackground(null);
        spinnerWtoTravel.setEnabled(false);
        spinnerWtoTravel.setBackground(null);
        editHighschool.setEnabled(false);
        editHighschool.setBackground(null);
        editIndoorClub.setEnabled(false);
        editIndoorClub.setBackground(null);
        editColgClub.setEnabled(false);
        editColgClub.setBackground(null);
        editColgBeach.setEnabled(false);
        editColgBeach.setBackground(null);
        editColgIndoor.setEnabled(false);
        editColgIndoor.setBackground(null);
        editPoints.setEnabled(false);
        editPoints.setBackground(null);
        edit_volleyRanking.setEnabled(false);
        edit_volleyRanking.setBackground(null);
        topfinishes_txt_1.setEnabled(false);
        imageView1.setVisibility(View.GONE);
        topfinishes_txt_1.setBackground(null);
        topfinishes_txt_2.setEnabled(false);
        imageView2.setVisibility(View.GONE);
        topfinishes_txt_2.setBackground(null);
        topfinishes_txt_3.setEnabled(false);
        imageView3.setVisibility(View.GONE);
        topfinishes_txt_3.setBackground(null);


       /* if (values[1] != null) {
            topfinishes_txt_2.setText(values[1].trim());
            topFinishes2_lt.setVisibility(View.VISIBLE);
            topfinishes_txt_2.setBackground(null);
        }
        if (values[2] != null) {
            topfinishes_txt_3.setText(values[2].trim());
            topFinishes3_lt.setVisibility(View.VISIBLE);
            topfinishes_txt_3.setBackground(null);
        }*/

//        if (appendEditOpenedOne){
//            topfinishes_txt_2.setEnabled(false);
//            topfinishes_txt_2.setText("");
//            topFinishes2_lt.setVisibility(View.GONE);
//            imageView2.setVisibility(View.GONE);
//            topfinishes_txt_2.setBackground(null);
//            appendEditOpenedOne = false;
//        }
//        if (appendEditOpenedTwo){
//            topfinishes_txt_3.setEnabled(false);
//            topfinishes_txt_3.setText("");
//            topFinishes3_lt.setVisibility(View.GONE);
//            imageView3.setVisibility(View.GONE);
//            topfinishes_txt_3.setBackground(null);
//            appendEditOpenedTwo = false;
//        }
        reloadFragment();
        editStatus = false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                if (data.hasExtra("data")) {
                    profilePhoto = (Bitmap) data.getExtras().get("data");
                    selectedImageUri = getImageUri(getApplicationContext(), profilePhoto);
                    try {
                        profilePhoto = handleSamplingAndRotationBitmap(getContext(),getImageUri(getApplicationContext(),profilePhoto));
                    } catch (Exception e) {
                        Log.e(TAG,"COVERT_ERROR");
                    }

                } else {
                    selectedImageUri = data.getData();

                }
                if (selectedImageUri != null) {
                    File imgfile = new File(getRealPathFromURI(selectedImageUri));
                    //Glide.with(getApplicationContext()).clear(imgProfile);
                    //Glide.with(getApplicationContext()).load(selectedImageUri)
                            //.apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate().signature(new ObjectKey(System.currentTimeMillis()))).into(imgProfile);
                    Log.d(TAG, "Image Name: "+imgfile.getName());
                    // Get length of file in bytes
                    if (fileSize(imgfile.length()) <= 4) {
                        imageUri = getRealPathFromURI(selectedImageUri);
                        createDirectoryAndSaveFile(selectedImageUri, imgfile.getName(), "image");
                    } else {
                        Toast.makeText(getActivity(), "Image size is too large", Toast.LENGTH_SHORT).show();
                    }
                }
                if (imageUri != null || videoUri != null) {
                    uploadImgFiles(imageUri, videoUri, user_id);
                    if(editStatus){
                        userDataModel.setImageUrl(imageUri);
                        InfoSave();
                    }
                }
            } else if (requestCode == PICK_VIDEO_REQUEST) {
                Intent intent = new Intent();
                intent.setType("video/*");
                Uri picUri = data.getData();
//                selectedVideoUri = Uri.parse(data.getExtras().get("data").toString());//data.getExtras().get("data");
                selectedVideoUri = data.getData();


                if (selectedVideoUri != null) {
                    // File file = new File(String.valueOf(getPath(selectedVideoUri)));
                    File file = new File(SelectedFilePath.getPath(getApplicationContext(), selectedVideoUri));
                    exoPlayer.stop();
                    Uri uri = Uri.fromFile(file);    //Uri.parse(getPath(selectedVideoUri));
                    playVideoFromFile(uri);

                    if (fileSize(file.length()) <= 30 && videoDuration <= 30) {
                        videoUri = getPath(selectedVideoUri);
                        createDirectoryAndSaveFile(selectedVideoUri, file.getName(), "video");

                        //imgVideo.setVisibility(View.VISIBLE);
                        //imgPlay.setVisibility(View.VISIBLE);
                        // exoPlayer.stop();

                        //videoView.setVideoURI(Uri.parse(String.valueOf(selectedVideoUri)));
                    } else {
                        Toast.makeText(getActivity(), "Aw!! Video is too large", Toast.LENGTH_SHORT).show();
                    }
                }
                if (imageUri != null || videoUri != null) {
                    uploadImgFiles(imageUri, videoUri, user_id);
                    if(editStatus){
                        userDataModel.setVideoUrl(videoUri);
                        InfoSave();
                    }
                }


            }

            if (imageUri != null || videoUri != null) {
                userDataModel.setImageUrl(imageUri);
                userDataModel.setVideoUrl(videoUri);
                uploadImgFiles(imageUri, videoUri, user_id);

            }
        }
    }

    //Method for check the size of the selected file
    private float fileSize(long fileLength) {
        long fileSizeInBytes = fileLength;
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        float fileSizeInMB = fileSizeInKB / 1024;

        return fileSizeInMB;
    }
    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

/*
*  Methods for convert the camera image
*
* */

    private void uploadImgFiles(final String imagePath, final String videoPath, final String userId) {

        if (!progress.isShowing()) {
            progress.setTitle("Loading");
            progress.setMessage("Please wait while we save your data");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
        }

        // START AsyncTask
        /*asyncTask = new PhotoAsyncTask(imagePath, videoPath, userId);
        asyncTask.setListener(new PhotoAsyncTask.PhotoAsyncTaskListener() {
            @Override
            public void onPhotoAsyncTaskFinished(HttpEntity value) {
                if (value != null) {
                    videoUri = null;
                    imageUri = null;
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Successfully updated your details", Toast.LENGTH_LONG).show();
                    reloadFragment();

                }
            }
        });

        asyncTask.execute();*/

        /*upload image and video to server using retrofit*/

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        // Change base URL to your upload server URL.
        uploadService = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ServiceClass.class);

        uploadImageVideo(imagePath,videoPath,userId);

    }


    private void uploadImageVideo(final String imagePath, final String videoPath, String userId) {
        {

            File videoFile =null;
            File imageFile = null;
            if (imagePath != null) {
                imageFile = new File(imagePath);
                //Glide.with(ProfileFragment.this).load(imageFile.getAbsolutePath()).into(imgProfile);
            }
            if (videoPath != null) {
                videoFile = new File(videoPath);
                //playVideoFromFile(Uri.fromFile(videoFile.getAbsoluteFile()));
            }
            if (imageFile != null) {
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                fileToUpload = MultipartBody.Part.createFormData("profileImg", imageFile.getName(), mFile);
            }
            if (videoFile != null) {
                RequestBody mFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);
                videoToUploaded = MultipartBody.Part.createFormData("profileVideo", videoFile.getName(), mFile1);

            }
            RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), userId);

            Call<UploadObject> fileUpload = uploadService.uploadMultiFile(fileToUpload,videoToUploaded,descBody);
            fileUpload.enqueue(new Callback<UploadObject>() {
                @Override
                public void onResponse(Call<UploadObject> call, retrofit2.Response<UploadObject> response) {
                    //Toast.makeText(getActivity(), "Response " + response.raw().message(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "Success " + response.body().getSuccess(), Toast.LENGTH_LONG).show();
                    progress.dismiss();
                    if (imagePath != null) {
                        Log.d(TAG, "image Url"+selectedImageUri);
                        Glide.with(getApplicationContext()).load(selectedImageUri)
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate().signature(new ObjectKey(System.currentTimeMillis()))).into(imgProfile);
                        if (getActivity() != null) {
                            new PrefManager(getContext()).saveProfileImage(String.valueOf(selectedImageUri));
                        }
                        userDataModel.setImageUrl(String.valueOf(selectedImageUri));
                    }
                    if (videoPath != null) {
                        playVideoFromFile(Uri.parse(videoPath));
                        userDataModel.setVideoUrl(videoPath);
                    }

                    //reloadFragment();
                    //if (!editStatus){
                        Toast.makeText(getApplicationContext(), "Successfully updated your details", Toast.LENGTH_LONG).show();
                    //}

                }

                @Override
                public void onFailure(Call<UploadObject> call, Throwable t) {
                    // progressDialog.dismiss();
                    progress.dismiss();
                    Log.d(TAG, "Error " + t.getMessage());
                }

            });


        }
    }

    // UPDATED!
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void setUp() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(ApiService.REQUEST_METHOD_GET, ApiService.GET_ACCOUNT_DETAILS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response get account--", response.toString());
                        if (response != null) {
                            try {
                                Gson gson = new Gson();
                                userDataModel = gson.fromJson(response.toString(),CoachProfileResponse.class);
                                setViews();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
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
                                        Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                                    }
                                    break;

                                default:
                                    break;
                            }
                        }else{
                            //Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                //headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Log.d("Request", objectRequest.toString());
        requestQueue.add(objectRequest);

    }

    private void updateAllUserDetails(JSONObject object) {
        if (!progress.isShowing()) {
            progress.setTitle("Loading");

            progress.setMessage("Please wait while we save your data");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

            progress.show();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ApiService.REQUEST_METHOD_PUT, ApiService.UPDATE_USER_PROFILE + user_id, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            if (getActivity() != null) {

                                editStatus = false;
                                edit_tag.setTextColor(getResources().getColor(R.color.btnColor));
                                edit_tag.setText("Edit Profile");
                                if (userDataModel.getImageUrl() != null) {
                                    File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/" + userDataModel.getImageUrl().substring(userDataModel.getImageUrl().lastIndexOf('/') + 1));

                                    if (myFile.exists()) {
                                       // Glide.with(ProfileFragment.this).load(myFile.getAbsolutePath()).into(imgProfile);

                                    }
                                    // if((userDataModel.getImageUrl().substring(userDataModel.getImageUrl().lastIndexOf('/') + 1).equals()){

                                    //  }
                                    //  Glide.with(ProfileFragment.this).load(userDataModel.getImageUrl()).into(imgProfile);
                                } else {
                                    imgProfile.setImageResource(R.drawable.ic_person);
                                }
                                if (userDataModel.getVideoUrl() != null) {
                                    // playVideo(userDataModel.getVideoUrl());
                                }

                                progress.dismiss();
                                Toast.makeText(getActivity(), "Successfully updated your details", Toast.LENGTH_LONG).show();
                                reloadFragment();
                            }
                        } else {
                            progress.dismiss();
                            Toast.makeText(getActivity(), "Failed to update your details", Toast.LENGTH_LONG).show();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                editStatus = false;
                edit_tag.setText("Edit Profile");
                videoUri = null;
                imageUri = null;
                Toast.makeText(getActivity(), "Failed to update your details", Toast.LENGTH_LONG).show();

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

    private void setViews() {
        if (getActivity() != null) {
            if (userDataModel != null) {
                //set basic informations of user
                if (userDataModel.getImageUrl() != null && !userDataModel.getImageUrl().equalsIgnoreCase("null")) {
                    String imageName = userDataModel.getImageUrl().substring(userDataModel.getImageUrl().lastIndexOf('/') + 1);
                    String[] imagePathArray = imageName.split("-");
                    if (imagePathArray != null && imagePathArray.length > 0) {

                        String exactImageName = imagePathArray[imagePathArray.length-1];
                        Log.d("Image----",exactImageName);

                        Log.d("filename---", userDataModel.getImageUrl().substring(userDataModel.getImageUrl().lastIndexOf('/') + 1));
                        myProfileImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/" + "image" + "/" + exactImageName);

                        if (myProfileImageFile.exists()) {
                            Glide.with(ProfileFragment.this).load(myProfileImageFile.getAbsolutePath()).into(imgProfile);
                        } else {
                            Glide.with(ProfileFragment.this).load(userDataModel.getImageUrl()).into(imgProfile);
                            new DownloadFileFromURL(exactImageName, "image",userDataModel.getImageUrl()).execute();


                        }
                    }

                } else {
                    imgProfile.setImageResource(R.drawable.ic_person);
                }
                if (userDataModel.getVideoUrl() != null && !userDataModel.getVideoUrl().equalsIgnoreCase("null")) {
                    String videoName = userDataModel.getVideoUrl().substring(userDataModel.getVideoUrl().lastIndexOf('/') + 1);
                    String[] videoPathArray = videoName.split("-");
                    if (videoPathArray != null && videoPathArray.length >= 0) {
                        String exactVideoName = videoPathArray[videoPathArray.length-1];

                        Log.d("filename---", userDataModel.getVideoUrl().substring(userDataModel.getVideoUrl().lastIndexOf('/') + 1));
                        myProfileVideFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/" + "video" + "/" + exactVideoName);

                        if (myProfileVideFile.exists() && bundle == null) {
                            playVideoFromFile(Uri.parse(myProfileVideFile.getPath()));
                        } else {
                            playVideo(userDataModel.getVideoUrl());
                            if(bundle==null){
                                new DownloadFileFromURL(exactVideoName, "video",userDataModel.getVideoUrl()).execute();

                            }

                        }
                    }
                }
                else{
                    progressbar.setVisibility(View.GONE);
                    placeholder.setVisibility(View.VISIBLE);
                }
                if (userDataModel.getToursPlayedIn() != null && userDataModel.getToursPlayedIn().equalsIgnoreCase("")) {
                    setSelectedToursFromServer(userDataModel.getToursPlayedIn());
                }
                profileName.setText(userDataModel.getFirstName().trim());
                profileDesig.setText(userDataModel.getUserType().trim());
                editLname.setText(userDataModel.getLastName().trim());
                editFname.setText(userDataModel.getFirstName().trim());
                editGender.setText(userDataModel.getGender().trim());
                location = userDataModel.getLocation().trim();
                if (location != null) {
                    int positions = dataAdapter.getPosition(location);
                    editCity.setSelection(positions);
                }
                //Long value to date conversion
                if (userDataModel.getDob().trim() != null && !userDataModel.getDob().equals("null")) {
                    SimpleDateFormat dft = new SimpleDateFormat("MM-dd-yyyy");
                    long dob = Long.parseLong(userDataModel.getDob());
                    Date date_dob = new Date(dob);
                    editDob.setText(dft.format(date_dob));
                    myCalendar.setTime(date_dob);
                }
                editPhone.setText(userDataModel.getPhoneNumber());
                //set More information
                if (userDataModel.getUserProfile().getUpf_cbvaFirstName() != null || userDataModel.getUserProfile().getUpf_cbvaFirstName() != "null") {
                    editCBVAFName.setText(userDataModel.getUserProfile().getUpf_cbvaFirstName());
                }
                if (userDataModel.getUserProfile().getUpf_cbvaLastName() != "null" || userDataModel.getUserProfile().getUpf_cbvaLastName() != null) {
                    editCBVALName.setText(userDataModel.getUserProfile().getUpf_cbvaLastName());
                }
                if (userDataModel.getUserProfile().getUpf_cbvaPlayerNumber() != "null" ||userDataModel.getUserProfile().getUpf_cbvaPlayerNumber()!= null) {
                    editCBVANo.setText(userDataModel.getCbvaPlayerNumber());
                }
                if (userDataModel.getUserProfile().getUpf_collageClub() != "null" || userDataModel.getUserProfile().getUpf_collageClub() != null) {
                    editColgClub.setText(userDataModel.getUserProfile().getUpf_collageClub());
                }
                if (userDataModel.getUserProfile().getUpf_collegeBeach() != "null" || userDataModel.getUserProfile().getUpf_collegeBeach() != null) {
                    editColgBeach.setText(userDataModel.getUserProfile().getUpf_collegeBeach());
                }
                if (userDataModel.getUserProfile().getUpf_collegeIndoor() != "null" || userDataModel.getUserProfile().getUpf_collegeIndoor()  != null) {
                    editColgIndoor.setText(userDataModel.getUserProfile().getUpf_collegeIndoor() );
                }
                if (userDataModel.getUserProfile().getUpf_highSchoolAttended() != "null" || userDataModel.getUserProfile().getUpf_highSchoolAttended() != null) {
                    editHighschool.setText(userDataModel.getUserProfile().getUpf_highSchoolAttended());
                }
                if (userDataModel.getUserProfile().getUpf_indoorClubPlayed() != "null" || userDataModel.getUserProfile().getUpf_indoorClubPlayed() != null) {
                    editIndoorClub.setText(userDataModel.getUserProfile().getUpf_indoorClubPlayed());
                }
                if (userDataModel.getUserProfile().getUpf_totalPoints() != "null" || userDataModel.getUserProfile().getUpf_totalPoints() != null) {
                    editPoints.setText(userDataModel.getUserProfile().getUpf_totalPoints());
                }
                if (userDataModel.getUserProfile().getUpf_toursPlayedIn() != "null" || userDataModel.getUserProfile().getUpf_toursPlayedIn() != null) {
                    editPlayed.setText(userDataModel.getUserProfile().getUpf_toursPlayedIn());
                }
                if (userDataModel.getUserProfile().getUpf_usaVolleyballRanking() != "null" || userDataModel.getUserProfile().getUpf_usaVolleyballRanking() != null) {
                    edit_volleyRanking.setText(userDataModel.getUserProfile().getUpf_usaVolleyballRanking());
                }
//                String topFinishes = userDataModel.getUserProfile().getTopFinishes();
                String courSidePref = userDataModel.getUserProfile().getUpf_courtSidePreference();
                if (courSidePref != null) {
                    int courtPos = prefAdapter.getPosition(courSidePref);
                    // spinnerPref.setSelection(courtPos);
                    spinnerPref.setSelection(prefAdapter.getPosition(courSidePref));

                }
                String exp = userDataModel.getUserProfile().getUpf_experience();
                if (exp != null) {
                    int exper = expAdapter.getPosition(exp);
                    spinnerExp.setSelection(exper);
                }
                String highestTER = userDataModel.getUserProfile().getUpf_highestTourRatingEarned();
                if (highestTER != null) {
                    int hter = highestRatingAdapter.getPosition(highestTER);
                    spinnerTourRating.setSelection(hter);
                }
                String tourIntrest = userDataModel.getUserProfile().getUpf_tournamentLevelInterest();
                if (tourIntrest != null) {
                    int tIL = tournamentInterestAdapter.getPosition(tourIntrest);
                    spinnerTLInterest.setSelection(tIL);
                }
                String pos = userDataModel.getUserProfile().getUpf_position();
                if (pos != null) {
                    int positions = positionAdapter.getPosition(pos);
                    spinnerPositon.setSelection(positions);
                }
                String wTot = userDataModel.getUserProfile().getUpf_willingToTravel();
                if (wTot != null) {
                    int willingTotravel = distanceAdapter.getPosition(wTot);
                    spinnerWtoTravel.setSelection(willingTotravel);
                }
                String height = userDataModel.getUserProfile().getUpf_height();
                if (height != null) {
                    int heightVal = heightAdapter.getPosition(height);
                    editHeight.setSelection(heightVal);
                }
                if ( userDataModel.getUserProfile().getTopFinishes() != null) {

                    values =  userDataModel.getUserProfile().getTopFinishes().split(",");
                    if (values.length == 1) {
                        if (values[0] != null) {
                            topfinishes_txt_1.setText(values[0].trim());
                        }
                    }
                    if (values.length == 2) {
                        imageView2.setVisibility(View.GONE);
                        imageView3.setVisibility(View.GONE);
                        if (values[0] != null) {
                            topfinishes_txt_1.setText(values[0].trim());
                        }
                        if (values[1] != null) {
                            topfinishes_txt_2.setText(values[1].trim());
                            topFinishes2_lt.setVisibility(View.VISIBLE);
                        }
                    }
                    if (values.length == 3) {
                        finishCount=2;
                        imageView1.setVisibility(View.GONE);
                        imageView2.setVisibility(View.GONE);
                        imageView3.setVisibility(View.GONE);
                        if (values[0] != null) {
                            topfinishes_txt_1.setText(values[0].trim());

                        }
                        if (values[1] != null) {
                            topfinishes_txt_2.setText(values[1].trim());
                            topFinishes2_lt.setVisibility(View.VISIBLE);
                        }
                        if (values[2] != null) {
                            topfinishes_txt_3.setText(values[2].trim());
                            topFinishes3_lt.setVisibility(View.VISIBLE);
                        }

                    }
                }

            }
        }
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

    public Intent getPickImageIntent(Context context, String type) {
        Intent chooserIntent = null;
        List<Intent> intentList = new ArrayList<>();


        if (type.equals("videoIntent")) {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);

            Intent pickIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Video.Media.INTERNAL_CONTENT_URI);

            pickIntent.setType("video/*");

            if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                takeVideoIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                // takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,30000);
                // takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,0);
                takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10485760L);// 10*1024*1024 = 10MB  10485760L
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                takeVideoIntent.putExtra(MediaStore.Video.Thumbnails.HEIGHT, 480);
                takeVideoIntent.putExtra(MediaStore.Video.Thumbnails.WIDTH, 720);


                //startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);

            }
            intentList = addIntentsToList(context, intentList, pickIntent);
            intentList = addIntentsToList(context, intentList, takeVideoIntent);

        } else if (type.equals("imageIntent")) {


            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);

            Intent pickIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            intentList = addIntentsToList(context, intentList, pickIntent);
            intentList = addIntentsToList(context, intentList, takePictureIntent);
        /*if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }*/


        }


        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1), "");
            // context.getString(R.string.pick_image_intent_text));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    return false;
                }
            }
        }
        return true;
    }

    @SuppressLint("NewApi")
    private String getRealPath(Uri uri) {
        String filePath = null;
        String uriString = uri.toString();

        if (uriString.startsWith("content://media")) {
            filePath = getDataColumn(getActivity(), uri, null, null);
        } else if (uriString.startsWith("file")) {
            filePath = uri.getPath();
        } else if (uriString.startsWith("content://com")) {
            String docId = DocumentsContract.getDocumentId(uri);
            String[] split = docId.split(":");
            Uri contentUri = null;
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String selection = "_id=?";
            String[] selectionArgs = new String[]{split[1]};
            filePath = getDataColumn(getActivity(), contentUri, selection, selectionArgs);
        }

        return filePath;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);

    }

    public String isEmptyOrNull(String stringToCheck) {
        String stringValue = " ";
        if (stringToCheck == null && stringToCheck.isEmpty()) {
            stringValue = " ";
        } else {
            if (stringToCheck.equalsIgnoreCase("null")) {
                stringValue = " ";
            } else {
                stringValue = stringToCheck;
            }
        }
        return stringValue;
    }

    //method to write profile image and video into a local file
    private void createDirectoryAndSaveFile(Uri uri, String fileName, String fileType) {


        File direct = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));


        if (!direct.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
            wallpaperDirectory.mkdirs();
        }
        if (fileType.equals("image")) {
            File profileImageDir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/" + "image");
            //File imageDirectory=null;
            if (!profileImageDir.exists()) {
                new File(direct, "image").mkdir();

                writeImageToDirectory(fileName, uri);
            } else {
                String[] children = profileImageDir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(profileImageDir, children[i]).delete();
                }

                writeImageToDirectory(fileName, uri);

            }

        } else {
            InputStream inStream = null;
            OutputStream outStream = null;

            String sourcePath = SelectedFilePath.getPath(getApplicationContext(), selectedVideoUri);
            File source = new File(sourcePath);

            File profileVideoDir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/" + "video");
            //File imageDirectory=null;
            if (!profileVideoDir.exists()) {
                new File(direct, "video").mkdir();

                writeVideoToDirectory(fileName, source);
            } else {
                String[] children = profileVideoDir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(profileVideoDir, children[i]).delete();
                }
                writeVideoToDirectory(fileName, source);

            }

        }
    }

    private void writeVideoToDirectory(String fileName, File source) {


        String destinationPath = Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/" + "video" + "/" + fileName;
        File destination = new File(destinationPath);

        try {
            destination.createNewFile();

            FileUtils.copyFile(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeImageToDirectory(String fileName, Uri uri) {
        Bitmap mBitmap = null;
        File file = null;

        try {
            mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }


        file = new File(new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/" + "image"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.e(TAG,"LOADING");

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == ExoPlayer.STATE_BUFFERING) {
            Log.e(TAG,"BUFFERING");
            progressbar.setVisibility(View.VISIBLE);
            //show your progress dialog here
        }
        if (playbackState == ExoPlayer.STATE_READY) {
            Log.e(TAG,"READY");
            progressbar.setVisibility(View.GONE);
            //show your progress dialog here
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }




    private class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /***
     * Download profile file from the server when the local file directory is empty
     */


    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private String fileName;
        private String fileType;
        private String f_url;


        public DownloadFileFromURL(String fileName, String fileType, String f_url) {
            this.fileName = fileName;
            this.fileType = fileType;
            this.f_url =f_url;
        }

        /**
         * Before starting background thread
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");

           /* pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... url) {

            if (getActivity() != null) {
                File parentDirectory = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));

                if (!parentDirectory.exists()) {
                    File wallpaperDirectory = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
                    wallpaperDirectory.mkdirs();
                }
                if (fileType.equals("image")) {


                    File profileImageDir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/" + "image");
                    //File imageDirectory=null;
                    if (!profileImageDir.exists()) {
                        new File(parentDirectory, "image").mkdir();

                        downloadProfileImageAndVideo(fileName, fileType, f_url);

                    } else {
                        String[] children = profileImageDir.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(profileImageDir, children[i]).delete();
                        }

                        downloadProfileImageAndVideo(fileName, fileType, f_url);

                    }


                } else {
                    File profileVideoDir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/" + "video");
                    //File imageDirectory=null;
                    if (!profileVideoDir.exists()) {
                        new File(parentDirectory, "video").mkdir();

                        downloadProfileImageAndVideo(fileName, fileType, f_url);

                    } else {
                        String[] children = profileVideoDir.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(profileVideoDir, children[i]).delete();
                        }

                    }
                    downloadProfileImageAndVideo(fileName, fileType, f_url);

                }
            }
            return null;

        }

        private void downloadProfileImageAndVideo(String fileName, String fileType, String... f_url) {
            int count;
            if(getActivity()!=null) {
                try {
                    String root = Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/" + fileType + "/";

                    System.out.println("Downloading");
                    URL url = new URL(f_url[0]);

                    URLConnection conection = url.openConnection();
                    conection.connect();
                    // getting file length
                    int lenghtOfFile = conection.getContentLength();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    // Output stream to write file

                    OutputStream output = new FileOutputStream(root + fileName);
                    byte data[] = new byte[1024];

                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;

                        // writing data to file
                        output.write(data, 0, count);

                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            }
        }


        /**
         * After completing background task
         **/
        @Override
        protected void onPostExecute(String file_url) {
            System.out.println("Downloaded");

            //pDialog.dismiss();
        }

    }


}