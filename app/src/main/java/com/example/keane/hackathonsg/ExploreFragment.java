package com.example.keane.hackathonsg;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ExploreFragment extends Fragment {

    public static ArrayList<ParseObject> artsEvents;
    public static ArrayList<ParseObject> artsEvents2;
    public static ArrayList<ParseObject> artsEvents3;
    public static ArrayList<ParseObject> sportsEvents = new ArrayList<>();
    public static ArrayList<ParseObject> allEvents;
    public static ArrayList<ParseObject> allEvents2;
    public static ArrayList<ParseObject> allEvents3;
    private Menu mMenu;
    private Button mComingUpButton;
    private Button mHotButton;
    public static Integer currentItem=0;
    public static String determinant;
    private FloatingActionButton button1;
    private FloatingActionButton button2;
    private FloatingActionButton button3;
    private FloatingActionButton button4;


    String COMING_UP_STRING = "Coming Up";
    String HOT_STRING = "Hot";
    private ListView mComingUpView;
    private ListView mHotView;


    public ExploreFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        mComingUpView = (ListView)view.findViewById(R.id.exploreListView);
        mComingUpButton = (Button) view.findViewById(R.id.comingUpButton);
        button1 = (FloatingActionButton)view.findViewById(R.id.button1);
        mHotButton = (Button)view.findViewById(R.id.hotButton);
        if (determinant == null){
            determinant = "All Events";
        }
        setExploreListView(determinant);
        mComingUpButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bottom_border));
        mComingUpButton.setTextColor(getResources().getColor(R.color.white));
        mHotButton.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        mHotButton.setTextColor(getResources().getColor(R.color.translucent_white));
        mComingUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem = 0;
                setExploreListView(determinant);
                mComingUpButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bottom_border));
                mComingUpButton.setTextColor(getResources().getColor(R.color.white));
                mHotButton.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                mHotButton.setTextColor(getResources().getColor(R.color.translucent_white));
            }
        });
        mHotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem = 1;
                setExploreListView(determinant);
                mHotButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bottom_border));
                mHotButton.setTextColor(getResources().getColor(R.color.white));
                mComingUpButton.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                mComingUpButton.setTextColor(getResources().getColor(R.color.translucent_white));
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryDialog();
            }
        });


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenu = menu;
        inflater.inflate(R.menu.menu_explore, menu);
        MenuItem item = mMenu.findItem(R.id.action_categories);
        item.setTitle("Category:" + getString(R.string.space) + "All Events");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_categories) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void CategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Category");
        builder.setItems(R.array.mCategoryList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> array = Arrays.asList(getResources().getStringArray(R.array.mCategoryList));
                MenuItem item = mMenu.findItem(R.id.action_categories);
                item.setTitle("Category:" + getString(R.string.space) + array.get(which));
                determinant = array.get(which);
                setExploreListView(array.get(which));
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setExploreListView(String eventType){

        switch(eventType) {
            case "All Events":
                buttonAll("All");
                break;
            case "Arts":
                buttonAll("Arts");
                break;
            case "Sports":
                buttonAll("Sports");
                break;
            default:
                buttonAll("All");
                break;
        }

    }

    private void buttonAll(String determinant) {
        switch(determinant) {
            case "All":
            switch (currentItem) {
                case 0:
                    EventsAdaptor adaptor = new EventsAdaptor(getActivity(), R.layout.explore_list, allEvents2);
                   if (mComingUpView!=null) {
                       mComingUpView.setAdapter(adaptor);
                       mComingUpView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                           @Override
                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                               Intent intent = new Intent(getActivity(), EventActivity.class);
                               EventActivity.eventId = allEvents2.get(position).getObjectId();
                               startActivity(intent);
                           }
                       });
                   }
                    break;
                case 1:
                    EventsAdaptor adaptor2 = new EventsAdaptor(getActivity(), R.layout.explore_list, allEvents3);
                    if (mComingUpView!=null) {
                        mComingUpView.setAdapter(adaptor2);
                        mComingUpView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), EventActivity.class);
                                EventActivity.eventId = allEvents3.get(position).getObjectId();
                                startActivity(intent);
                            }
                        });
                    }
                    break;
            }
                break;
            case "Arts":
            {
                switch (currentItem){
                    case 0:
                        EventsAdaptor adaptor3 = new EventsAdaptor(getActivity(), R.layout.explore_list, artsEvents2);
                        mComingUpView.setAdapter(adaptor3);
                        mComingUpView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), EventActivity.class);
                                EventActivity.eventId = artsEvents2.get(position).getObjectId();
                                startActivity(intent);
                            }
                        });
                        break;
                    case 1:
                        EventsAdaptor adaptor4 = new EventsAdaptor(getActivity(), R.layout.explore_list, artsEvents3);
                        mComingUpView.setAdapter(adaptor4);
                        mComingUpView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), EventActivity.class);
                                EventActivity.eventId = artsEvents3.get(position).getObjectId();
                                startActivity(intent);
                            }
                        });
                        break;
                }
                break;}
                case "Sports":
                {
                    switch (currentItem){
                        case 0:
                            EventsAdaptor adaptor3 = new EventsAdaptor(getActivity(), R.layout.explore_list, sportsEvents);
                            mComingUpView.setAdapter(adaptor3);
                            break;
                        case 1:
                            EventsAdaptor adaptor4 = new EventsAdaptor(getActivity(), R.layout.explore_list, sportsEvents);
                            mComingUpView.setAdapter(adaptor4);
                            break;
                    }
                    break;

            }
        }

    }



    private class EventsAdaptor extends ArrayAdapter<ParseObject>{
        private int mResource;
        private ArrayList<ParseObject> mEventsList;

        public EventsAdaptor(Context context, int res, ArrayList<ParseObject> list){
            super(context, res, list);
            mResource = res;
            mEventsList = list;
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
            TextView icon = (TextView)row.findViewById(R.id.exploreIcon);

            icon.setText(event.getString("Title").substring(0, 1));
            Random r = new Random();
            int iconNum = r.nextInt(5 - 1) + 1;
            if (iconNum == 1) { icon.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.icon1))); }
            else if (iconNum == 2) { icon.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.icon2))); }
            else if (iconNum == 3) { icon.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.icon3))); }
            else if (iconNum == 4) { icon.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.icon4))); }
            else if (iconNum == 5) { icon.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.icon5))); }
            else { icon.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.icon1))); }

            titleTv.setText(event.getString("Title"));

            if(event.getString("Genre")!=null) {
                catTv.setText(Html.fromHtml("<b>Category:</b> " + event.getString("Category") + " - " + event.getString("Genre")));
            }
            else {
                catTv.setText(Html.fromHtml("<b>Category:</b> " + event.getString("Category")));
            }

            String locText = "";
            if(event.getString("Block")!=null) locText += (event.getString("Block") + " ");
            if(event.getString("Street")!=null) locText += (event.getString("Street") + " ");
            if(event.getString("BuildingName")!=null) locText += (event.getString("BuildingName") + " ");
            if(event.getString("Floor")!=null){
                if(event.getString("UnitNumber")!=null) locText += ("#" +event.getString("Floor")+ "-"+ event.getString("UnitNumber"));
                else locText += ("Level " + event.getString("Floor"));
            }
            if (locText.matches("")) {
                locationTv.setVisibility(View.GONE);
            }
            else {
                locationTv.setText(Html.fromHtml("<b>Location:</b> " + locText));
            }

            if(event.getString("Organiser")!=null) orgTv.setText(Html.fromHtml("<b>Organiser:</b> " + event.getString("Organiser")));
            else orgTv.setVisibility(View.GONE);

            if(event.getString("Date")!=null && event.getString("Date2")!=null){
                if(event.getString("Date").equals(event.getString("Date2"))) dateTv.setText(Html.fromHtml("<b>Date:</b> " + event.getString("Date")));
                else dateTv.setText(Html.fromHtml("<b>Date:</b> " + event.getString("Date") + " - " + event.getString("Date2")));
            }

            return row;
        }
    }
}
