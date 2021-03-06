package com.beachpartnerllc.beachpartner.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.beachpartnerllc.beachpartner.R;
import com.beachpartnerllc.beachpartner.activity.TabActivity;
import com.beachpartnerllc.beachpartner.adpters.MyConnectionAdapter;
import com.beachpartnerllc.beachpartner.adpters.MyTeamAdapter;
import com.beachpartnerllc.beachpartner.calendar.compactcalendarview.domain.Event;
import com.beachpartnerllc.beachpartner.connections.ApiService;
import com.beachpartnerllc.beachpartner.connections.PrefManager;
import com.beachpartnerllc.beachpartner.models.ConnectionModel;
import com.beachpartnerllc.beachpartner.utils.TeamMakerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.beachpartnerllc.beachpartner.adpters.ConnectionAdapter;


public class ConnectionsTabFragment extends Fragment implements View.OnClickListener,TeamMakerInterface {

    private RelativeLayout rlTop,rlTeam;
    private RecyclerView rcviewConn,rcviewTeam;
    private MyConnectionAdapter  mConnectionAdapter;
    private MyTeamAdapter myTeamAdapter;;
    private ImageView upDownToggle;
    private TextView txtv_noconnection;
    private ArrayList<ConnectionModel>myTeamList = new ArrayList<>();
    private ArrayList<ConnectionModel>connectionList = new ArrayList<>();
    private ArrayList<ConnectionModel>athleteList = new ArrayList<>();  //to sort out athletes from the connectionlist
    private ArrayList<ConnectionModel>searchList = new ArrayList<>();
    List<ConnectionModel> filteredModelList = new ArrayList<>();
    LinearLayoutManager layoutmnger;
    private String token;
    private long eventDate;
    private String eventDateToCheckAvailability;
    private ArrayList<Integer>persons = new ArrayList<>();
    private Button btnInvitefrnd;

    private BottomSheetBehavior bottomSheetBehavior;
    private String registerType;
    private Event eventObject;
    private TabActivity tabActivity;
    private String YOUR_FRAGMENT_STRING_TAG;
    private AlertDialog alertDialog;
    private SearchView searchView;
    private static boolean isSearch = false;

    public ConnectionsTabFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        eventDate   = getArguments().getLong("eventDate");
        eventObject = getArguments().getParcelable("eventObj");
        token   = new PrefManager(getContext()).getToken();
        View view =inflater.inflate(R.layout.fragment_connections_tab, container, false);
        initActivity(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof TabActivity) {
            tabActivity = (TabActivity) getActivity();
            tabActivity.setActionBarTitle(eventObject.getEventName());
        }
        layoutmnger = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        //adapter for connections
        getConnections();
        //adapter for creating my team
        LinearLayoutManager mnger = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        myTeamAdapter       =   new MyTeamAdapter(getContext(),myTeamList,this);
        rcviewTeam.setAdapter(myTeamAdapter);
        rcviewTeam.setLayoutManager(mnger);
        rcviewTeam.setHasFixedSize(true);
    }

    private void initActivity(View view) {

        // rlTop       =       (RelativeLayout)view.findViewById(R.id.rlayoutTop);
        txtv_noconnection   =  view.findViewById(R.id.no_connections);
        rlTeam              =  view.findViewById(R.id.rlayout_myteam);
        searchView          =  view.findViewById(R.id.search);

        rcviewConn          =  view.findViewById(R.id.rview_connections);
        rcviewTeam          =  view.findViewById(R.id.rcview_myteam);

        upDownToggle        =  view.findViewById(R.id.upDown);
        btnInvitefrnd       =  view.findViewById(R.id.btn_invite);
        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet));
        btnInvitefrnd.setBackground(getResources().getDrawable(R.drawable.event_desc_btns_inactive));
        btnInvitefrnd.setOnClickListener(this);
        // Capturing the callbacks for bottom sheet
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                            /*if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                                bottomSheetHeading.setText(getString(R.string.text_collapse_me));
                            } else {
                                bottomSheetHeading.setText(getString(R.string.text_expand_me));
                            }*/

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });

        rcviewConn.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        //search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (athleteList != null && athleteList.size() > 0) {
                    isSearch=true;
                    if (!newText.isEmpty()) {
                        filterPerson(newText);
                        final List<ConnectionModel> filteredModelList = filter(connectionList, newText);
                        mConnectionAdapter.setFilter(filteredModelList);
                        mConnectionAdapter.notifyDataSetChanged();

                    } else {
                        filterPerson(newText);
                        mConnectionAdapter = new MyConnectionAdapter(getContext(), athleteList, ConnectionsTabFragment.this);
                        rcviewConn.setAdapter(mConnectionAdapter);
                        mConnectionAdapter.notifyDataSetChanged();
                    }


                }
                return false;
            }
        });
    }

    private List<ConnectionModel> filter(ArrayList<ConnectionModel> connectionList, String newText) {
        newText = newText.toLowerCase().trim();
        for (ConnectionModel model : connectionList) {
            final String text = model.getConnected_firstName().toLowerCase().trim();
            if (text.contains(newText)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;

    }

    private void filterPerson(String newText) {
        String textSearch = newText;
        if (!textSearch.equals("null")) {
            searchList.clear();
            if (athleteList.size() > 0) {
                for (int i = 0; i < athleteList.size(); i++) {
                    if (athleteList.get(i).getConnected_firstName().toLowerCase().contains(textSearch.toLowerCase())) {
                        searchList.add(athleteList.get(i));
                    }
                }
            }
            if (searchList.size() > 0) {
                mConnectionAdapter = new MyConnectionAdapter(getContext(), searchList, this);
                rcviewConn.setAdapter(mConnectionAdapter);
                mConnectionAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                mConnectionAdapter = new MyConnectionAdapter(getContext(), athleteList, this);
                rcviewConn.setAdapter(mConnectionAdapter);
                mConnectionAdapter.notifyDataSetChanged();

            }

        }
    }


    private void getConnections() {
        SimpleDateFormat dft = new SimpleDateFormat("dd-MM-yyyy");
        eventDateToCheckAvailability=dft.format(eventDate);

        // Toast.makeText(getContext(), "eventClickedDate-----"+eventDateToCheckAvailability, Toast.LENGTH_SHORT).show();

        connectionList.clear();
        athleteList.clear();
        String user_id  = new PrefManager(getContext()).getUserId();
        //18-05-2018
        JsonArrayRequest arrayRequest = new JsonArrayRequest(ApiService.REQUEST_METHOD_GET, ApiService.GET_ALL_CONNECTIONS + user_id +"?status=Active"+"&filterDate="+eventDateToCheckAvailability+"&eventId="+eventObject.getEventId(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if(response!=null){
                    for(int i=0;i<response.length();i++){
                        try {
                            JSONObject object = response.getJSONObject(i);
                            JSONObject obj    = object.getJSONObject("connectedUser");
                            ConnectionModel model  = new  ConnectionModel();
                            model.setConnected_uId(obj.getString("id"));
                            model.setConnected_login(obj.getString("login"));
                            model.setConnected_firstName(obj.getString("firstName"));
                            model.setConnected_lastName(obj.getString("lastName"));
                            model.setConnected_email(obj.getString("email"));
                            model.setConnected_userType(obj.getString("userType"));
                            model.setConnected_imageUrl(obj.getString("imageUrl"));
                            //setting the availability of a user on a particular date
                            model.setConnected_isAvailable_ondate(object.getBoolean("availableOnDate"));
                            model.setConnected_invitationStatus(object.getString("invitationStatus"));
                            connectionList.add(model);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    setConnections();
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
                                Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 500:
                            json = new String(response.data);
                            json = trimMessage(json, "title");
                            if (json != null) {
                                Toast.makeText(getActivity(), "" + json, Toast.LENGTH_LONG).show();
                            }
                            break;
                        default:
                            break;
                    }
                }

            }
        }){ @Override
        public Map<String, String> getHeaders()  {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "Bearer " + token);
            //headers.put("Content-Type", "application/json; charset=utf-8");
            return headers;

        }

        };
        if (getActivity() != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            Log.d("Request", arrayRequest.toString());
            requestQueue.add(arrayRequest);
        }




    }

    private void setConnections() {
        if(connectionList!=null && connectionList.size()>0){
            for(int i=0;i<connectionList.size();i++){
                if(connectionList.get(i).getConnected_userType().equals("Athlete")){
                    athleteList.add(connectionList.get(i));
                }
            }

            if (athleteList.size() > 0 && athleteList != null) {
                mConnectionAdapter  =   new MyConnectionAdapter(getContext(),athleteList,this);
                rcviewConn.setAdapter(mConnectionAdapter);
                mConnectionAdapter.notifyDataSetChanged();
                rcviewConn.setLayoutManager(layoutmnger);
            }else {
                txtv_noconnection.setVisibility(View.VISIBLE);
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

    @Override
    public void onPause() {
        super.onPause();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
           /* case R.id.btnExpand:
                if(BottomSheetBehavior.STATE_EXPANDED==3){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
*/
            case R.id.btn_invite:
                registerEvent();
                break;

            default:
                break;
        }
    }




    //Api for register event
    private void registerEvent() {
        persons.clear();
        if (myTeamList.size() > 0) {
            for(int i=0;i<myTeamList.size();i++){
                persons.add(Integer.valueOf(myTeamList.get(i).Connected_uId));
            }

        }

        JSONArray array = new JSONArray(persons);
        registerType = "invitee";//"organizer";//invitee
        JSONObject object = new JSONObject();
        try {
            object.put("registerType",registerType);
            object.put("eventId",eventObject.getEventId());
            object.put("userIds",array);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(ApiService.REQUEST_METHOD_POST, ApiService.EVENT_REGISTER, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    Toast.makeText(getActivity(), "Request sent successfully", Toast.LENGTH_SHORT).show();
                    moveToMasterCalendar();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;

            }

        };
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            Log.d("TournamentInviteRequest", request.toString());
            queue.add(request);
        }


    }

    private void moveToMasterCalendar() {
        if (getActivity() != null) {
            CalendarFragment calendarFragment = new CalendarFragment();
            tabActivity.getSupportActionBar().setTitle("Calendar");
            FragmentManager cmngr = getActivity().getSupportFragmentManager();
            FragmentTransaction ctrans = cmngr.beginTransaction();
            ctrans.replace(R.id.container, calendarFragment);
            ctrans.commit();
            tabActivity.disableFloatButtons();
        }

    }

    //adding member from connection list to my teamlist using interface
    @Override
    public void onAddListener(ConnectionModel member,int position) {
        btnInvitefrnd.setBackground(getResources().getDrawable(R.drawable.event_desc_buttons));
        if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_COLLAPSED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        if (!isSearch) {
            if(myTeamList.size()<=4){
                myTeamList.add(athleteList.remove(position));
                mConnectionAdapter.notifyDataSetChanged();
                myTeamAdapter.notifyDataSetChanged();
            }else {
                Toast.makeText(tabActivity, "Exceeded send requests", Toast.LENGTH_SHORT).show();
            }

        }else {
            if(myTeamList.size()<=4) {
                myTeamList.add(searchList.remove(position));
                athleteList.removeAll(myTeamList);
                mConnectionAdapter.notifyDataSetChanged();
                myTeamAdapter.notifyDataSetChanged();
            }
        }


        //Toast.makeText(getActivity(), "id --->"+member.getConnected_uId()+"available-->"+member.getConnected_isAvailable_ondate()+" at position-->"+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveListener(ConnectionModel removedMember, int position) {
        removeConfirmation(myTeamList.get(position).getConnected_firstName(),position);

        //Toast.makeText(getActivity(), "You removed "+myTeamList.get(position).Connected_firstName+" from your potential partners" , Toast.LENGTH_SHORT).show();
    }

    private void removeConfirmation(String firstname, final int position){
        final AlertDialog.Builder alerts = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater   = getActivity().getLayoutInflater();
        View view     = inflater.inflate(R.layout.alert_confirm,null);
        alerts.setView(view);
        TextView txtview    =    (TextView)view.findViewById(R.id.txtview) ;
        Button   btn_confirm =   (Button) view.findViewById(R.id.btn_resetOk);
        Button   btn_cancel  =   (Button) view.findViewById(R.id.btn_resetCancel);
        txtview.setText("Are you sure you want to remove "+firstname+" from your potential partners?");
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                athleteList.add(myTeamList.remove(position));
                mConnectionAdapter.notifyDataSetChanged();
                myTeamAdapter.notifyDataSetChanged();
                if (myTeamList.size() == 0) {
                    btnInvitefrnd.setBackground(getResources().getDrawable(R.drawable.event_desc_btns_inactive));
                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog  = alerts.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(true);
    }


}