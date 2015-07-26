package com.example.keane.hackathonsg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class JioCentreFragment extends Fragment {

    private ListView mListView;
    private ArrayList<ParseObject> mRequests;
    private ArrayList<ParseObject> mJios;
    private ArrayList<ParseObject> mJioed;

    private Button requestButt;
    private Button jiosButt;
    private Button jioedButt;

    private TextView noData;

    public JioCentreFragment() {
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
        View view = inflater.inflate(R.layout.fragment_jio_centre, container, false);
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
                    requestButt.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bottom_border));
                    requestButt.setTextColor(getResources().getColor(R.color.white));
                    jiosButt.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                    jiosButt.setTextColor(getResources().getColor(R.color.translucent_white));
                    jioedButt.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                    jioedButt.setTextColor(getResources().getColor(R.color.translucent_white));
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
                requestButt.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bottom_border));
                requestButt.setTextColor(getResources().getColor(R.color.white));
                jiosButt.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                jiosButt.setTextColor(getResources().getColor(R.color.translucent_white));
                jioedButt.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                jioedButt.setTextColor(getResources().getColor(R.color.translucent_white));
                setListView("request");
            }
        });
        jiosButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jiosButt.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bottom_border));
                jiosButt.setTextColor(getResources().getColor(R.color.white));
                requestButt.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                requestButt.setTextColor(getResources().getColor(R.color.translucent_white));
                jioedButt.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                jioedButt.setTextColor(getResources().getColor(R.color.translucent_white));
                setListView("jios");
            }
        });
        jioedButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jioedButt.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bottom_border));
                jioedButt.setTextColor(getResources().getColor(R.color.white));
                jiosButt.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                jiosButt.setTextColor(getResources().getColor(R.color.translucent_white));
                requestButt.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                requestButt.setTextColor(getResources().getColor(R.color.translucent_white));
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
            final TextView jiosTv = (TextView)row.findViewById(R.id.jiosTv);
            final TextView statusTv = (TextView)row.findViewById(R.id.jiosStatusTv);

            ParseQuery<ParseObject> query = new ParseQuery<>("artsEvents");
            query.getInBackground(jio.getString("eventId"), new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject event, ParseException e) {
                    if(e==null){
                        jiosTv.setText("You jio-ed " + jio.getString("toUser") + " to " + event.getString("Title"));
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
                            }
                            else {
                                statusTv.setText(jio.getString("toUser")+" declined! Jio someone else ):");
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

            final TextView jioedTv = (TextView)row.findViewById(R.id.jioedTv);
            final Button jioedEventButt = (Button)row.findViewById(R.id.jioedEventButt);
            final Button jioedAcceptButt = (Button)row.findViewById(R.id.jioedAcceptButt);
            final Button jioedDeclineButt = (Button)row.findViewById(R.id.jioedDeclineButt);

            ParseQuery<ParseObject> query = new ParseQuery<>("artsEvents");
            query.getInBackground(jio.getString("eventId"), new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject event, ParseException e) {
                    if(e==null){
                        jioedTv.setText(jio.getString("fromUser") + " jio-ed you to " + event.getString("Title"));
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
