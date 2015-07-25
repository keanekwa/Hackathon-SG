package com.example.keane.hackathonsg;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class SplashScreenActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ParseQuery<ParseObject> query = new ParseQuery<>("artsEvents");
        query.addAscendingOrder("Date");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                ExploreFragment.artsEvents = new ArrayList<>(list);
                ExploreFragment.allEvents = new ArrayList<>();
                ExploreFragment.allEvents.addAll(ExploreFragment.artsEvents);
                for (int i = 0; i < ExploreFragment.artsEvents.size(); i++) {
                    ExploreFragment.artsEvents.get(i).put("Category", "Arts");
                    if (ExploreFragment.artsEvents.get(i).get("rating")==null){
                        ExploreFragment.artsEvents.get(i).put("rating", 0);
                    }
                    if (ExploreFragment.artsEvents.get(i).get("noOfRaters")==null){
                        ExploreFragment.artsEvents.get(i).put("noOfRaters", 0);
                    }
                }
                ParseQuery<ParseObject> query2 = new ParseQuery<>("artsEvents");
                query2.addAscendingOrder("Date");
                query2.setLimit(20);
                query2.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list2, ParseException e) {
                        ExploreFragment.artsEvents2 = new ArrayList<>(list2);
                        ExploreFragment.allEvents2 = new ArrayList<>();
                        ExploreFragment.allEvents2.addAll(ExploreFragment.artsEvents2);
                        ParseQuery<ParseObject> query3 = new ParseQuery<>("artsEvents");
                        query3.addDescendingOrder("Date");
                        query3.setLimit(20);
                        query3.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list3, ParseException e) {
                                ExploreFragment.artsEvents3 = new ArrayList<>(list3);
                                ExploreFragment.allEvents3 = new ArrayList<>();
                                ExploreFragment.allEvents3.addAll(ExploreFragment.artsEvents3);
                                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
            });

}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
