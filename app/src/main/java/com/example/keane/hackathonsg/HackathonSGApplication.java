package com.example.keane.hackathonsg;

import android.app.Application;
import android.os.Bundle;

import com.parse.Parse;

public class HackathonSGApplication extends Application {

    @Override
    public void onCreate() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "j1WgbDUHKegP3udIyJl3WTx9ZPTDxTbB48Y9L7ke", "FRZBFt87CW9fcRQhqIAoAbkqrhQXZVRYgdpn2qax");

        super.onCreate();
    }

}
