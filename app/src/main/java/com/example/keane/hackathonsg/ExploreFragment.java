package com.example.keane.hackathonsg;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ExploreFragment extends Fragment {

    public static ArrayList<ParseObject> artsEvents;
    private Menu mMenu;

    private ListView mListView;

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
        mListView = (ListView)view.findViewById(R.id.exploreListView);
        setExploreListView("arts");
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
            CategoryDialog();
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
        EventsAdaptor adaptor;
        switch(eventType) {
            case "arts":
                adaptor = new EventsAdaptor(getActivity(), R.layout.explore_list, artsEvents, "Arts");
                break;
            default:
                adaptor = new EventsAdaptor(getActivity(), R.layout.explore_list, artsEvents, "Arts");
                break;
        }
        mListView.setAdapter(adaptor);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                EventFragment newFragment = new EventFragment();
                newFragment.eventId = artsEvents.get(position).getObjectId();
                fragmentManager.beginTransaction().replace(R.id.container, newFragment).commit();
            }
        });
    }

    private class EventsAdaptor extends ArrayAdapter<ParseObject>{
        private int mResource;
        private ArrayList<ParseObject> mEventsList;
        private String mCategory;

        public EventsAdaptor(Context context, int res, ArrayList<ParseObject> list, String category){
            super(context, res, list);
            mResource = res;
            mEventsList = list;
            mCategory = category;
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

            if(event.getString("Genre")!=null) catTv.setText(Html.fromHtml("<b>Category:</b> " + mCategory + " - " + event.getString("Genre")));
            else catTv.setText(Html.fromHtml("<b>Category:</b> " + mCategory));

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
