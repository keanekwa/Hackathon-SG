package com.example.keane.hackathonsg;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class JioActivity extends ActionBarActivity {

    private String eventId;
    private ArrayList<ParseUser> mFriends = new ArrayList<>();
    private ListView jioFriendsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jio);
        jioFriendsListView = (ListView)findViewById(R.id.jioFriendsList);

        Intent infoIntent = getIntent();
        eventId = infoIntent.getStringExtra("EVENT_ID");
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn("objectId", ParseUser.getCurrentUser().getList("friendsList"));
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null && parseUsers.size()>0) {
                    for (int i = 0; i < parseUsers.size(); i++) {
                        mFriends.add(parseUsers.get(i));
                    }
                    Friends adapter = new Friends(JioActivity.this, R.layout.friends_list_adapter, mFriends);
                    jioFriendsListView.setAdapter(adapter);
                }

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jio, menu);
        return true;
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

    private class Friends extends ArrayAdapter<ParseUser> {
        //creating variables
        private int mResource;
        private ArrayList<ParseUser> mFriends;

        public Friends(Context context, int resource, ArrayList<ParseUser> friendsList) {
            super(context, resource, friendsList);
            mResource = resource;
            mFriends = friendsList;
        }

        //display subject data in every row of listView
        @Override
        public View getView(final int position, View row, ViewGroup parent) {
            if (row == null) {
                row = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
            }
            final Button jioButt = (Button)row.findViewById(R.id.jioJioButt);

            final ParseObject friend = mFriends.get(position);
            TextView titleTextView = (TextView) row.findViewById(R.id.friendUsernameTextView);
            titleTextView.setText(friend.getString("username"));
            TextView makeBlank = (TextView)row.findViewById(R.id.friendJioText);
            makeBlank.setText("");

            ParseImageView likeImageView = (ParseImageView) row.findViewById(R.id.friendImageView);
            likeImageView.setParseFile(friend.getParseFile("profilePic"));
            likeImageView.setPlaceholder(getResources().getDrawable(R.drawable.defaultuserimage));
            likeImageView.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {

                }
            });

            ParseQuery<ParseObject> query = new ParseQuery<>("Jio");
            query.whereEqualTo("eventId", eventId);
            query.whereEqualTo("toUser", friend.getString("username"));
            query.countInBackground(new CountCallback() {
                @Override
                public void done(int i, ParseException e) {
                    if(e==null && i==0){
                        jioButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            ParseObject newObj = new ParseObject("Jio");
                            newObj.put("fromUser", ParseUser.getCurrentUser().getUsername());
                            newObj.put("eventId", eventId);
                            newObj.put("toUser", friend.getString("username"));
                            newObj.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Toast.makeText(JioActivity.this, friend.getString("username")+" jioed!", Toast.LENGTH_SHORT).show();
                                    jioButt.setVisibility(View.INVISIBLE);
                                }
                            });
                            }
                        });
                    }
                    else if (i>0){
                        jioButt.setVisibility(View.INVISIBLE);
                    }
                }
            });


            return row;
        }
    }
}
