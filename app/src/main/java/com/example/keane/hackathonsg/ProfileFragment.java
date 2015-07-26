package com.example.keane.hackathonsg;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    ParseUser currentUser = ParseUser.getCurrentUser();
    String usernameText;
    String jioText;
    ListView friendsList;
    ParseImageView profilePic;
    TextView usernameTextView;
    TextView jioTextView;
    ArrayList<ParseUser>friendsArray = new ArrayList<>();

    public ProfileFragment() {
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
        jioText = "You have been jioed " + currentUser.getInt("noOfJios") + " times.";
        usernameText = currentUser.getString("username");
        usernameTextView = (TextView) view.findViewById(R.id.usernameText);
        jioTextView = (TextView)view.findViewById(R.id.noOfJiosText);
        usernameTextView.setText(usernameText);
        jioTextView.setText(jioText);
        profilePic = (ParseImageView)view.findViewById(R.id.profileImageView);
        final TextView subtitleTextView = (TextView) view.findViewById(R.id.noOfJiosText);
        if(currentUser.getParseFile("profilePic")==null){
            profilePic.setImageDrawable(getResources().getDrawable(R.drawable.bojioicon));
        }
        else{
            profilePic.setParseFile(currentUser.getParseFile("profilePic"));
            profilePic.setPlaceholder(getResources().getDrawable(R.drawable.defaultuserimage));
            profilePic.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                }
            });
        }

        ParseQuery<ParseObject> query = new ParseQuery<>("Jio");
        query.whereEqualTo("toUser", ParseUser.getCurrentUser().getUsername());
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                subtitleTextView.setText("Has been jio-ed " + String.valueOf(i) + " times.");
            }
        });

        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
        query1.whereContainedIn("objectId", ParseUser.getCurrentUser().getList("friendsList"));
        query1.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                for (int i = 0; i < parseUsers.size(); i++) {
                    friendsArray.add(parseUsers.get(i));
                }
                friendsList = (ListView) view.findViewById(R.id.friendsListView);
                PhotosAdapter adapter = new PhotosAdapter(getActivity(), R.layout.profile_friends_list_adapter, friendsArray);
                friendsList.setAdapter(adapter);
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

    private class PhotosAdapter extends ArrayAdapter<ParseUser> {
        //creating variables
        private int mResource;
        private ArrayList<ParseUser> mFriends;

        public PhotosAdapter(Context context, int resource, ArrayList<ParseUser> friendsList) {
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

            final ParseObject currentTopImage = mFriends.get(position);
            TextView titleTextView = (TextView) row.findViewById(R.id.profileFriendUsernameTextView);
            titleTextView.setText(currentTopImage.getString("username"));

            //set like button status on create
            ParseImageView likeImageView = (ParseImageView) row.findViewById(R.id.profileFriendImageView);
            likeImageView.setParseFile(currentTopImage.getParseFile("profilePic"));
            likeImageView.setPlaceholder(getResources().getDrawable(R.drawable.bojioicon));
            likeImageView.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {

                }
            });
            return row;
        }
    }
}
