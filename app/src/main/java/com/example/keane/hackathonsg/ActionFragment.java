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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ActionFragment extends Fragment {

    private ListView mListView;
    private ArrayList<ParseObject> mRequests;

    public ActionFragment() {
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
        View view = inflater.inflate(R.layout.fragment_action, container, false);
        mListView = (ListView)view.findViewById(R.id.actionListView);

        ParseQuery<ParseObject> query = new ParseQuery<>("Friendship");
        query.whereEqualTo("toId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null) {
                    mRequests = new ArrayList<>(parseObjects);
                    setListView("request");
                }
            }
        });
        return view;
    }

    public void setListView(String data){
        if(data.equals("request")){
            FriendRequestAdapter adapter = new FriendRequestAdapter(getActivity(), R.layout.action_request_list, mRequests);
            mListView.setAdapter(adapter);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class FriendRequestAdapter extends ArrayAdapter<ParseObject> {
        private int mResource;
        private ArrayList<ParseObject> mFriendsList;

        public FriendRequestAdapter(Context context, int res, ArrayList<ParseObject> list){
            super(context, res, list);
            mResource = res;
            mFriendsList = list;
        }

        @Override
        public View getView(int pos, View row, ViewGroup parent){
            if(row == null){
                row = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
            }

            final ParseObject friendship = mFriendsList.get(pos);
            final TextView usernameTv = (TextView)row.findViewById(R.id.friendRequestUsernameTextView);
            final ImageButton tickButt = (ImageButton)row.findViewById(R.id.requestTickButt);
            final ImageButton crossButt = (ImageButton)row.findViewById(R.id.requestCrossButt);
            final ParseImageView likeImageView = (ParseImageView) row.findViewById(R.id.friendRequestImageView);

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(friendship.getString("fromId"), new GetCallback<ParseUser>() {
                @Override
                public void done(final ParseUser user, ParseException e) {
                usernameTv.setText(user.getUsername());
                tickButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    friendship.put("accepted", true);
                    friendship.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParseUser.getCurrentUser().addUnique("friendsList", friendship.getString("fromId"));
                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Toast.makeText(getActivity(), "You are now friends with "+user.getUsername()+"!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    }
                });
                crossButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendship.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(getActivity(), "Friend Request Deleted!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                likeImageView.setParseFile(friendship.getParseFile("profilePic"));
                likeImageView.setPlaceholder(getResources().getDrawable(R.drawable.defaultuserimage));
                likeImageView.loadInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {

                    }
                });
                }
            });

            return row;
        }
    }
}
