package com.project.mugeshm.videostreaming;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;

import java.io.IOException;

public class VideoPlayerActivity extends AppCompatActivity {
    FullscreenVideoLayout videoView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = (FullscreenVideoLayout) findViewById(R.id.videoView);
        videoView.setActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        text= bundle.getString("videoname");

        text="http://192.168.0.123:3000/videos?name="+text;


        if(text!=null || text!="") {
            getWindow().setFormat(PixelFormat.TRANSLUCENT);
            MediaController mediaController = new MediaController(VideoPlayerActivity.this);
            mediaController.setAnchorView(videoView);

            Uri video = Uri.parse(text);
            try {
                videoView.setVideoURI(video);
            } catch (IOException e) {
                e.printStackTrace();
            }
            videoView.requestFocus();
            mediaController.setAnchorView(videoView);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                }
            });
            Toast.makeText(VideoPlayerActivity.this, "loading video from " + text, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(VideoPlayerActivity.this, "Text is null", Toast.LENGTH_SHORT).show();

        }




    }

}
