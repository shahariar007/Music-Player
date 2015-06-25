package com.example.n33r.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class PlaysongActivity extends AppCompatActivity implements View.OnClickListener {
    SeekBar seekBar;
    Button playpause, forward, previous, formove, backmove;
    TextView starttimen, endtime;
    Toolbar toolbar;
    ArrayList<File> song;
    static MediaPlayer mediaPlayer;
    int position;
    NotificationManager notificationManager;
    Uri uri;
    //String[] alname = {"album", "artist"};
    // MediaMetadataRetriever mediaMetadataRetriever;

    int totalduration;
    int currentPossition = 0;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        toolbar = (Toolbar) findViewById(R.id.toolbarx);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.newone);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        playpause = (Button) findViewById(R.id.btnplaypause);
        playpause.setOnClickListener(this);
        forward = (Button) findViewById(R.id.btnforward);
        forward.setOnClickListener(this);

        previous = (Button) findViewById(R.id.btnprevious);
        previous.setOnClickListener(this);
        formove = (Button) findViewById(R.id.btnformove);
        formove.setOnClickListener(this);
        backmove = (Button) findViewById(R.id.btnbackmove);
        starttimen = (TextView) findViewById(R.id.starttime);
        endtime = (TextView) findViewById(R.id.endtime);
        backmove.setOnClickListener(this);
        // mediaMetadataRetriever = new MediaMetadataRetriever();


        //check media player playing or not
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        //seekbar
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                totalduration = mediaPlayer.getDuration();
                currentPossition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPossition);
                Log.d("hhh", "pppp");
                handler.postDelayed(this, 1000);
            }
        }, 1000);


        TextView textView = (TextView) findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "burdeles.ttf");
        textView.setTypeface(typeface);
        // work  in media player
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        song = (ArrayList) bundle.getParcelableArrayList("Allsong");
        position = bundle.getInt("poss", 0);
        uri = Uri.parse(song.get(position).toString());

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        StartNotification("Started Playing Next Song", "Enjoing");
        EndDuration();
        seekBar.setMax(mediaPlayer.getDuration());

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                Nextsong();
                mediaPlayer.isLooping();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                starttimen.setText(String.format("%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes((long) seekBar.getProgress()),
                                TimeUnit.MILLISECONDS.toSeconds((long) seekBar.getProgress()) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) seekBar.getProgress())))
                );


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

    }

//    public String[] AlbumFinder() {
//        alname[0] = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
//        if (alname[0] == null) {
//            alname[0] = "Artist:Unnone";
//        }
//        alname[1]=mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
//        if(alname[1]==null)
//        {
//            alname[1] = "Titme:Unnone";
//        }
//        return  alname;
//
//    }

    public void StartNotification(String notificationtitle, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(notificationtitle);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.newone);
        Notification notification = builder.build();
        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }





    public void Nextsong() {
        mediaPlayer.stop();
        mediaPlayer.release();
        playpause.setText(" || ");
        position = (position + 1) % song.size();
        uri = Uri.parse(song.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        EndDuration();
        StartNotification("Started Playing Next Song", "Enjoing");
    }

    public void EndDuration() {
        endtime.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) mediaPlayer.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds((long) mediaPlayer.getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) mediaPlayer.getDuration()))));


    }


    public void Previoussong() {
        mediaPlayer.stop();
        mediaPlayer.release();
        playpause.setText(" || ");
        position = (position - 1 < 0) ? song.size() - 1 : position - 1;
        uri = Uri.parse(song.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        EndDuration();
        StartNotification(" Playing Song", "Enjoing");


    }

    public void Toastmake(String name) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnplaypause:
                if (mediaPlayer.isPlaying()) {
                    playpause.setText(" > ");
                    mediaPlayer.pause();
                    Toastmake("Pause");
                    StartNotification("pause", "sad");

                } else {
                    mediaPlayer.start();
                    playpause.setText(" || ");
                    Toastmake("Play");
                    StartNotification("Again play", "Enjoing");

                }
                break;
            case R.id.btnformove:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 4000);
                Toastmake("Forward 4 sec");
                break;
            case R.id.btnbackmove:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 4000);
                Toastmake("Backward 4 sec");
                break;
            case R.id.btnforward:
                Nextsong();
                Toastmake("Next");

                break;
            case R.id.btnprevious:
                Previoussong();
                Toastmake("previous");
                break;



        }

    }
}
