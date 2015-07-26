package com.example.keane.hackathonsg;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    public static ArrayList<ParseUser> mRESULTS;
    ListView mListView;
    EditText searchText;
    private ProgressBar loading;
    ImageButton confirmButton;
    String searchQuery;
    RelativeLayout noResults;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.fragment_friends, container, false);
        if(mRESULTS ==null) {
            mRESULTS = new ArrayList<>();
        }
        mListView = (ListView) view.findViewById(R.id.searchListView2);
        loading = (ProgressBar) view.findViewById(R.id.progressBarV2);
        loading.setVisibility(View.INVISIBLE);
        searchText = (EditText)view.findViewById(R.id.searchTextBar);
        confirmButton = (ImageButton)view.findViewById(R.id.confirm_search_2);
        noResults = (RelativeLayout)view.findViewById(R.id.noResults2);

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
                    search();
                }
            }
        });

        return view;
    }

    public void search(){
        loading.setVisibility(View.VISIBLE);
        noResults.setVisibility(View.INVISIBLE);
        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.addDescendingOrder("username");
        query.whereContains("username", searchQuery);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseObjects, ParseException e) {
                if (e == null && parseObjects.size() > 0) {
                    for (int j = 0; j < parseObjects.size(); j++) {
                        mRESULTS.add(parseObjects.get(j));
                    }
                    loading.setVisibility(View.VISIBLE);
                    PhotosAdapter adapter = new PhotosAdapter(getActivity(), R.layout.friends_list_adapter_2, mRESULTS);
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

            final ParseUser user = mFriends.get(position);
            TextView titleTextView = (TextView) row.findViewById(R.id.friendUsernameTextView2);
            titleTextView.setText(user.getString("username"));

            //set like button status on create
            ParseImageView likeImageView = (ParseImageView) row.findViewById(R.id.friendImageView2);
            likeImageView.setParseFile(user.getParseFile("profilePic"));
            likeImageView.setPlaceholder(getResources().getDrawable(R.drawable.defaultuserimage));
            likeImageView.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {

                }
            });

            ImageButton addFriendButt = (ImageButton)row.findViewById(R.id.addFriendButt);
            TextView friendsTv = (TextView)row.findViewById(R.id.friendsText);
            friendsTv.setText("");
            if(ParseUser.getCurrentUser().getList("friendsList").contains(user)) {
                friendsTv.setText("Friends");
            }
            else if (ParseUser.getCurrentUser().getUsername().equals(user.getUsername())){
                friendsTv.setText("You");
            }
            else{
                addFriendButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    ParseObject friendship = new ParseObject("Friendship");
                    friendship.put("fromId", ParseUser.getCurrentUser().getObjectId());
                    friendship.put("toId", user.getObjectId());
                    friendship.put("accepted", false);
                    friendship.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "Friend request sent to " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
                    }
                });
            }

            return row;
        }
    }
}
