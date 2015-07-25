package com.example.keane.hackathonsg;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

public class EventFragment extends Fragment {

    public static String eventId;
    public static String eventLocation;

    private ParseObject event;
    private String currentUserId;
    private ParseObject currentUserData;

    private Boolean wasUserJioed;
    private Boolean wasUserGoing;

    private Button jioButt;
    private Button goButt;
    private Button pangButt;

    private TextView titleTv;
    private TextView catTv;
    private TextView locTv;
    private TextView dateTv;
    private TextView orgTv;
    private TextView sypTv;

    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        jioButt = (Button)view.findViewById(R.id.eventJioButt);
        goButt = (Button)view.findViewById(R.id.eventGoButt);
        pangButt = (Button)view.findViewById(R.id.eventPangButt);

        titleTv = (TextView)view.findViewById(R.id.eventTitle);
        catTv = (TextView)view.findViewById(R.id.eventCategory);
        locTv = (TextView)view.findViewById(R.id.eventLocation);
        dateTv = (TextView)view.findViewById(R.id.eventDate);
        orgTv = (TextView)view.findViewById(R.id.eventOrganiser);
        sypTv = (TextView)view.findViewById(R.id.eventSynopsis);

        jioButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserGoingOrJioed("jio");
            }
        });
        goButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserGoingOrJioed("go");
            }
        });
        pangButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserGoingOrJioed("pang");
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("artsEvents");
        query.getInBackground(eventId, new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, ParseException e) {
                if(e==null) {
                    event = parseObject;

                    titleTv.setText(event.getString("Title"));
                    if(event.getString("Genre")!=null) catTv.setText(event.getString("Genre"));

                    String locText = "";
                    if(event.getString("Block")!=null) locText += (event.getString("Block") + " ");
                    if(event.getString("Street")!=null) locText += (event.getString("Street") + " ");
                    if(event.getString("BuildingName")!=null) locText += (event.getString("BuildingName") + " ");
                    if(event.getString("Floor")!=null){
                        if(event.getString("UnitNumber")!=null) locText += ("#" +event.getString("Floor")+ "-"+ event.getString("UnitNumber"));
                        else locText += ("Level " + event.getString("Floor"));
                    }
                    locTv.setText(locText);

                    if(event.getString("Organiser")!=null) orgTv.setText(event.getString("Organiser"));
                    else orgTv.setVisibility(View.GONE);

                    if(event.getString("Date")!=null && event.getString("Date2")!=null){
                        if(event.getString("Date").equals(event.getString("Date2"))) dateTv.setText(event.getString("Date"));
                        else dateTv.setText(event.getString("Date") + " - " + event.getString("Date2"));
                    }

                    String sypText = "";
                    if(event.getString("Language")!=null) sypText += ("Language: " + event.getString("Language").toLowerCase() + "\n");
                    if(event.getString("Rating")!=null) sypText += ("Rating: " + event.getString("Rating") + "\n");
                    if(event.getString("Synopsis")!=null) sypText += ("\n" + event.getString("Synopsis"));
                    sypTv.setText(sypText);

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
                    query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if(e==null && parseObjects.size()==1){
                                currentUserData = parseObjects.get(0);
                                wasUserGoing = Arrays.asList(currentUserData.get("goingTo")).contains(eventId);
                                wasUserJioed = Arrays.asList(currentUserData.get("jioedTo")).contains(eventId);
                                if (wasUserGoing) setUserGoingOrJioed("go");
                                else {
                                    goButt.setVisibility(View.VISIBLE);
                                    pangButt.setVisibility(View.INVISIBLE);
                                    jioButt.setVisibility(View.INVISIBLE);
                                }
                                if (wasUserJioed) setUserGoingOrJioed("jio");
                            }
                        }
                    });
                }
                else {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    ExploreFragment newFragment = new ExploreFragment();
                    fragmentManager.beginTransaction().replace(R.id.container, newFragment).commit();
                }
            }
        });
        return view;
    }

    private void setUserGoingOrJioed(final String info){
        switch (info){
            case "go":
                if(currentUserData.get("goingTo")==null) currentUserData.put("goingTo", Arrays.asList(eventId));
                else currentUserData.add("goingTo", eventId);
                if(wasUserJioed) {
                    currentUserData.removeAll("jioedTo", Arrays.asList(eventId));
                }
                goButt.setVisibility(View.INVISIBLE);
                pangButt.setVisibility(View.VISIBLE);
                jioButt.setVisibility(View.VISIBLE);
                break;
            case "pang":
                if(wasUserJioed) {
                    if(currentUserData.get("jioedTo")==null) currentUserData.put("jioedTo", Arrays.asList(eventId));
                    else currentUserData.add("jioedTo", eventId);
                }
                currentUserData.removeAll("goingTo", Arrays.asList(eventId));
                goButt.setVisibility(View.VISIBLE);
                pangButt.setVisibility(View.INVISIBLE);
                jioButt.setVisibility(View.INVISIBLE);
                break;
            case "jio":
                break;
        }
        currentUserData.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null) Toast.makeText(getActivity(), "Sucessfully "+info, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
