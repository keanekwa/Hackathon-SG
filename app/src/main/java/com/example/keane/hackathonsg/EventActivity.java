package com.example.keane.hackathonsg;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventActivity extends ActionBarActivity {

    public static String eventId;

    private ParseObject event;

    private Button jioButt;

    private TextView titleTv;
    private TextView catTv;
    private TextView locTv;
    private TextView dateTv;
    private TextView orgTv;
    private TextView lanTv;
    private TextView ratTv;
    private TextView sypTv;

    public EventActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));

        Intent intent = getIntent();
        eventId = intent.getStringExtra("EVENT_ID");

        jioButt = (Button)findViewById(R.id.eventJioButt);

        titleTv = (TextView)findViewById(R.id.eventTitle);
        catTv = (TextView)findViewById(R.id.eventCategory);
        locTv = (TextView)findViewById(R.id.eventLocation);
        dateTv = (TextView)findViewById(R.id.eventDate);
        orgTv = (TextView)findViewById(R.id.eventOrganiser);
        lanTv = (TextView)findViewById(R.id.eventLanguage);
        ratTv = (TextView)findViewById(R.id.eventRating);
        sypTv = (TextView)findViewById(R.id.eventSynopsis);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("artsEvents");
        query.getInBackground(eventId, new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, ParseException e) {
                if(e==null) {
                    event = parseObject;

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
                        locTv.setVisibility(View.GONE);
                    }
                    else {
                        locTv.setText(Html.fromHtml("<b>Location:</b> " + locText));
                    }

                    if(event.getString("Organiser")!=null) orgTv.setText(Html.fromHtml("<b>Organiser:</b> " + event.getString("Organiser")));
                    else orgTv.setVisibility(View.GONE);

                    if(event.getString("Date")!=null && event.getString("Date2")!=null){
                        if(event.getString("Date").equals(event.getString("Date2"))) dateTv.setText(Html.fromHtml("<b>Date:</b> " + event.getString("Date")));
                        else dateTv.setText(Html.fromHtml("<b>Date:</b> " + event.getString("Date") + " - " + event.getString("Date2")));
                    }

                    if(event.getString("Language")!=null) lanTv.setText(Html.fromHtml("<b>Language:</b> " + event.getString("Language")));
                    else orgTv.setVisibility(View.GONE);

                    if(event.getString("Rating")!=null) ratTv.setText(Html.fromHtml("<b>Rating:</b> " + event.getString("Rating")));
                    else ratTv.setVisibility(View.GONE);

                    if(event.getString("Synopsis")!=null) sypTv.setText(Html.fromHtml("<b>Synopsis:</b><br>" + event.getString("Synopsis")));
                    else sypTv.setVisibility(View.GONE);

                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("UserData");
                    query2.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if(e==null && parseObjects.size()==1){
                                jioButt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ParseUser.getCurrentUser().getList("friendsList").size()==0){
                                            Toast.makeText(EventActivity.this,"You do not have any friends D:",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                        Intent intent = new Intent(EventActivity.this, JioActivity.class);
                                        intent.putExtra("EVENT_ID", eventId);
                                        startActivity(intent);
                                    }}
                                });
                            }
                        }
                    });
                }
                else {
                    finish();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
