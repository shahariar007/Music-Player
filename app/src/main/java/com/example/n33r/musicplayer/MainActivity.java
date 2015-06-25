package com.example.n33r.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    String[] item;
    ArrayList<File> mysongs;
    ArrayAdapter<String> adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        toolbar = (Toolbar) findViewById(R.id.toolbarx);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.newone);
        listView = (ListView) findViewById(R.id.listView);
        mysongs = Allsongs(Environment.getExternalStorageDirectory());
        item = new String[mysongs.size()];
        for (int i = 0; i < mysongs.size(); i++) {
            item[i] = mysongs.get(i).getName().toString().replace(".mp3", "");
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, item);
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mysongs = Allsongs(Environment.getExternalStorageDirectory());
                item = new String[mysongs.size()];
                for (int i = 0; i < mysongs.size(); i++) {
                    item[i] = mysongs.get(i).getName().toString().replace(".mp3", "");
                }
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, item);
                listView.setAdapter(adapter);
                toast("synchronise Successfully");
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), PlaysongActivity.class)
                        .putExtra("poss", position).putExtra("Allsong", mysongs));
            }
        });

    }

    public ArrayList<File> Allsongs(File root) {
        ArrayList<File> al = new ArrayList<>();
        File[] files = root.listFiles();
        for (File single : files) {
            if (single.isDirectory() && !single.isHidden()) {
                al.addAll(Allsongs(single));
            } else if (single.getName().endsWith(".mp3")) {
                al.add(single);
            }
        }
        return al;
    }

    public void toast(String name) {
        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void ClearNotification(int id) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("");
        builder.setContentText("");
        builder.setSmallIcon(R.drawable.newone);
        Notification notification = builder.build();
       NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           ClearNotification(1);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
