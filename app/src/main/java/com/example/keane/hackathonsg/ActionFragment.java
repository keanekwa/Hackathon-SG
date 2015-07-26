package com.example.keane.hackathonsg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private ArrayList<ParseObject> mJios;
    private ArrayList<ParseObject> mJioed;

    private Button requestButt;
    private Button jiosButt;
    private Button jioedButt;

    private TextView noData;

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
        noData = (TextView)view.findViewById(R.id.noData);
        noData.setText("");

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
        ParseQuery<ParseObject> query1 = new ParseQuery<>("Jio");
        query1.whereEqualTo("toUser", ParseUser.getCurrentUser().getUsername());
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null) {
                    mJioed = new ArrayList<>(parseObjects);
                }
            }
        });
        ParseQuery<ParseObject> query2 = new ParseQuery<>("Jio");
        query2.whereEqualTo("fromUser", ParseUser.getCurrentUser().getUsername());
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null) {
                    mJios = new ArrayList<>(parseObjects);
                }
            }
        });

        requestButt = (Button)view.findViewById(R.id.requestTab);
        jiosButt = (Button)view.findViewById(R.id.jiosTab);
        jioedButt = (Button)view.findViewById(R.id.jioedTab);
        requestButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListView("request");
            }
        });
        jiosButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListView("jios");
            }
        });
        jioedButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setListView("jioed");
            }
        });

        return view;
    }

    public void setListView(String data){
        noData.setText("");
        mListView.setAdapter(null);
        if(data.equals("request") && mRequests!=null && mRequests.size()>0){
            FriendRequestAdapter adapter = new FriendRequestAdapter(getActivity(), R.layout.action_request_list, mRequests);
            mListView.setAdapter(adapter);
        }
        else if(data.equals("jios") && mJios!=null && mJios.size()>0){
            JiosAdapter adapter = new JiosAdapter(getActivity(), R.layout.action_jios_list, mJios);
            mListView.setAdapter(adapter);
        }
        else if(data.equals("jioed") && mJioed!=null && mJioed.size()>0){
            JioedAdapter adapter = new JioedAdapter(getActivity(), R.layout.action_jioed_list, mJioed);
            mListView.setAdapter(adapter);
        }
        else{
            noData.setText("No Data");
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
                                mRequests.remove(friendship);
                                setListView("request");
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

    private class JiosAdapter extends ArrayAdapter<ParseObject> {
        private int mResource;
        private ArrayList<ParseObject> mJiosList;

        public JiosAdapter(Context con, int res, ArrayList<ParseObject> lis){
            super(con, res, lis);
            mResource = res;
            mJiosList = lis;
        }

        @Override
        public View getView(int pos, View row, ViewGroup parent) {
            if (row == null) {
                row = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
            }
            final ParseObject jio = mJiosList.get(pos);

            TextView jiosToUserTv = (TextView)row.findViewById(R.id.jiosToUserTv);
            final TextView jiosEventTv = (TextView)row.findViewById(R.id.jiosEventTv);
            final Button jiosButt = (Button)row.findViewById(R.id.jiosEventButt);
            final TextView statusTv = (TextView) row.findViewById(R.id.jiosStatusTv);

            jiosToUserTv.setText(jio.getString("toUser"));
            ParseQuery<ParseObject> query = new ParseQuery<>("artsEvents");
            query.getInBackground(jio.getString("eventId"), new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject event, ParseException e) {
                    if(e==null){
                        jiosEventTv.setText(event.getString("Title"));
                        jiosButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent eventIntent = new Intent(getActivity(), EventActivity.class);
                                eventIntent.putExtra("EVENT_ID", event.getObjectId());
                                startActivity(eventIntent);
                            }
                        });

                        if(jio.get("accepted")!=null){
                            if(jio.getBoolean("accepted")){
                                statusTv.setText(jio.getString("toUser")+" accepted! Enjoy!");
                                jio.deleteInBackground();
                            }
                            else {
                                statusTv.setText(jio.getString("toUser")+" declined! Jio someone else ):");
                                jio.deleteInBackground();
                            }
                        }
                        else{
                            statusTv.setText("");
                        }
                    }
                }
            });

            return row;
        }
    }

    private class JioedAdapter extends ArrayAdapter<ParseObject> {
        private int mResource;
        private ArrayList<ParseObject> mJioedList;

        public JioedAdapter(Context con, int res, ArrayList<ParseObject> lis){
            super(con, res, lis);
            mResource = res;
            mJioedList = lis;
        }

        @Override
        public View getView(int pos, View row, ViewGroup parent) {
            if (row == null) {
                row = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
            }
            final ParseObject jio = mJioedList.get(pos);

            TextView jioFromUserTv = (TextView)row.findViewById(R.id.jioedFromUserTv);
            final TextView jioedEventTv = (TextView)row.findViewById(R.id.jioedEventTv);
            final Button jioedEventButt = (Button)row.findViewById(R.id.jioedEventButt);
            final Button jioedAcceptButt = (Button)row.findViewById(R.id.jioedAcceptButt);
            final Button jioedDeclineButt = (Button)row.findViewById(R.id.jioedDeclineButt);

            jioFromUserTv.setText(jio.getString("fromUser"));

            ParseQuery<ParseObject> query = new ParseQuery<>("artsEvents");
            query.getInBackground(jio.getString("eventId"), new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject event, ParseException e) {
                    if(e==null){
                        jioedEventTv.setText(event.getString("Title"));
                        jioedEventButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent eventIntent = new Intent(getActivity(), EventActivity.class);
                                eventIntent.putExtra("EVENT_ID", event.getObjectId());
                                startActivity(eventIntent);
                            }
                        });
                        jioedAcceptButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                jio.put("accepted", true);
                                jio.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        mJioed.remove(jio);
                                        setListView("jioed");
                                    }
                                });
                            }
                        });
                        jioedDeclineButt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                jio.put("accepted", false);
                                jio.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        mJioed.remove(jio);
                                        setListView("jioed");
                                    }
                                });
                            }
                        });
                    }
                }
            });

            return row;
        }
    }
}
