package com.projects.kevinbarassa.emergencyresponder;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.luseen.verticalintrolibrary.VerticalIntro;
import com.luseen.verticalintrolibrary.VerticalIntroItem;
import com.projects.kevinbarassa.emergencyresponder.helper.SessionManager;

/**
 * Created by Kevin Barassa on 09-Jan-17.
 */

public class AppIntroActivity extends VerticalIntro {

    private SessionManager session;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(AppIntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void init() {

        addIntroItem(new VerticalIntroItem.Builder()
                .backgroundColor(R.color.accent)
                .image(R.drawable.available)
                .title("Always available!")
                .text("Emergency Responder has been made to stay on active mode even with lock screen." +
                        "\nThis makes sure you are monitored on every second.")
                .build());

        addIntroItem(new VerticalIntroItem.Builder()
                .backgroundColor(R.color.colorPrimaryDark)
                .image(R.drawable.convenient)
                .title("Very Convenient")
                .text("Once you have set the ICE Contact list, \nthe rest will be take care by the app for you." +
                        "\n\nProcess are done on background so no need to start the app everytime")
                .build());

        addIntroItem(new VerticalIntroItem.Builder()
                .backgroundColor(R.color.primary)
                .image(R.drawable.location)
                .title("Location tracking")
                .text("Once emergency mode activated, your location will be monitored for next 24hrs and logged to your private account, " +
                        "\nONLY shared by ICE contact list. \nThis can assist recover stolen phones")
                .build());

        setSkipEnabled(true);
        setVibrateEnabled(true);
        setNextText("NEXT");
        setDoneText("GET SAFE");
        setSkipText("SKIP");
        setVibrateIntensity(20);
        setCustomTypeFace(Typeface.createFromAsset(getAssets(), "fonts/NotoSans-Regular.ttf"));
    }

    @Override
    protected Integer setLastItemBottomViewColor() {
        return null;
    }

    @Override
    protected void onSkipPressed(View view) {

    }

    @Override
    protected void onFragmentChanged(int position) {

    }

    @Override
    protected void onDonePressed() {
        Intent intent = new Intent(AppIntroActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
