package com.fesi.funda.Fragment;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;
import com.fesi.funda.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatCallback;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;

public class QuienesSomos extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, AppCompatCallback {
    public static final String API_KEY = "AIzaSyBN9K08c3QoeZ5kA6s5Bv8ye77yaAJ-1Qg";
    private TextView txtMision, txtVision;
    private Toolbar toolbar;
    private AppCompatDelegate delegate;
    public String video_id;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_quienessomos);
        // attaching layout xml
        setContentView(R.layout.activity_quienessomos);

        video_id = getIntent().getStringExtra("urlVideo");

        txtMision = (TextView) findViewById(R.id.txt_mision);
        txtMision.setText(Html.fromHtml(getString(R.string.mision)));

        txtVision = (TextView) findViewById(R.id.txt_vision);
        txtVision.setText(Html.fromHtml(getString(R.string.vision)));

        toolbar = (Toolbar) findViewById(R.id.toolbarquees);
        delegate.setSupportActionBar(toolbar);

        delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        delegate.getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Initializing YouTube player view
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(API_KEY, this);

    }

    @Override
    @SuppressWarnings("deprecation")
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Falló el Inicio del video.", Toast.LENGTH_LONG).show();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (null == player) return;

        // Start buffering
        if (!wasRestored) {
            player.loadVideo(video_id);
        }

        // Add listeners to YouTubePlayer instance
        player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onAdStarted() {
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason arg0) {
            }

            @Override
            public void onLoaded(String arg0) {
            }

            @Override
            public void onLoading() {
            }

            @Override
            public void onVideoEnded() {
            }

            @Override
            public void onVideoStarted() {
            }
        });


        player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onBuffering(boolean arg0) {
            }

            @Override
            public void onPaused() {
            }

            @Override
            public void onPlaying() {
            }

            @Override
            public void onSeekTo(int arg0) {
            }

            @Override
            public void onStopped() {
            }
        });
    }

    @Override
    public void onSupportActionModeStarted(ActionMode actionMode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode actionMode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
}
