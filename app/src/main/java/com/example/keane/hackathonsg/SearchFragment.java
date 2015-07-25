package com.example.keane.hackathonsg;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    public static ArrayList<ParseObject> mRESULTS;
    ListView mListView;
    EditText searchText;
    private ProgressBar loading;
    ImageButton confirmButton;
    ImageButton backButton;
    String searchQuery;
    RelativeLayout noResults;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        if(mRESULTS ==null) {
            mRESULTS = new ArrayList<>();
        }
        mListView = (ListView) view.findViewById(R.id.searchListView);
        loading = (ProgressBar) view.findViewById(R.id.progressBar3);
        loading.setVisibility(View.GONE);
        searchText = (EditText)view.findViewById(R.id.searchText);
        confirmButton = (ImageButton)view.findViewById(R.id.confirm_search);
        backButton = (ImageButton)view.findViewById(R.id.backArrow);
        noResults = (RelativeLayout)view.findViewById(R.id.noResults);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRESULTS.clear();
                searchQuery = searchText.getText().toString().toLowerCase();
                if (searchQuery.equals("")) {
                    Toast.makeText(getActivity(), "Please enter text.", Toast.LENGTH_LONG).show();
                } else {
                    searchText.setText("");
                    mRESULTS.clear();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }

    public void search(){
        loading.setVisibility(View.VISIBLE);
        noResults.setVisibility(View.INVISIBLE);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("onNationalDay");
        query.addDescendingOrder("createdAt");
        query.whereContains("postTitle", searchQuery);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null && parseObjects.size() > 0) {
                    for (int j = 0; j < parseObjects.size(); j++) {
                        mRESULTS.add(parseObjects.get(j));
                    }
                    loading.setVisibility(View.VISIBLE);
                    EventsAdaptor adapter = new EventsAdaptor(getActivity(), R.layout.explore_list, mRESULTS, "Arts");
                    mListView.setVisibility(View.VISIBLE);
                    mListView.setAdapter(adapter);
                    loading.setVisibility(View.GONE);
                } else {
                    noResults.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    query.cancel();
                }
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

    private class EventsAdaptor extends ArrayAdapter<ParseObject>{
        private int mResource;
        private ArrayList<ParseObject> mEventsList;

        public EventsAdaptor(Context context, int res, ArrayList<ParseObject> list, String category){
            super(context, res, list);
            mResource = res;
            mEventsList = list;
//            mCategory = category;
        }

        @Override
        public View getView(final int pos, View row, ViewGroup parent){
            if(row == null){
                row = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
            }

            final ParseObject event = mEventsList.get(pos);

            TextView titleTv = (TextView)row.findViewById(R.id.exploreTitle);
            TextView catTv = (TextView)row.findViewById(R.id.exploreCategory);
            TextView locationTv = (TextView)row.findViewById(R.id.exploreLocation);
            TextView orgTv = (TextView)row.findViewById(R.id.exploreOrganiser);
            TextView dateTv = (TextView)row.findViewById(R.id.exploreDate);

            titleTv.setText(event.getString("Title"));
//            catTv.setText(Html.fromHtml("<b>Category:</b> " + mCategory + " - " + event.getString("Genre")));


            String locText = "";
            if(event.getString("Block")!=null) locText += (event.getString("Block") + " ");
            if(event.getString("Street")!=null) locText += (event.getString("Street") + " ");
            if(event.getString("BuildingName")!=null) locText += (event.getString("BuildingName") + " ");
            if(event.getString("Floor")!=null){
                if(event.getString("UnitNumber")!=null) locText += ("#" +event.getString("Floor")+ "-"+ event.getString("UnitNumber"));
                else locText += ("Level " + event.getString("Floor"));
            }
            locationTv.setText(Html.fromHtml("<b>Location:</b> " + locText));

            if(event.getString("Organiser")!=null) orgTv.setText(Html.fromHtml("<b>Organiser:</b> " + event.getString("Organiser")));
            else orgTv.setVisibility(View.GONE);

            if(event.getString("Date")!=null && event.getString("Date2")!=null){
                if(event.getString("Date").equals(event.getString("Date2"))) dateTv.setText(Html.fromHtml("<b>Date:</b> " + event.getString("Date")));
                else dateTv.setText(Html.fromHtml("<b>Date:</b> " + event.getString(" Date ") + " - " + event.getString("Date2")));
            }

            return row;
        }
    }
}
