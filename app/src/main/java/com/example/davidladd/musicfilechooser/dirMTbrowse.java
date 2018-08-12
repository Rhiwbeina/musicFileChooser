package com.example.davidladd.musicfilechooser;

import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.Preference;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;


public class dirMTbrowse extends AppCompatActivity {
    private static dirMTbrowse instance;
    private TextView mTextMessage;
    private android.support.v7.widget.RecyclerView mFileList;
    private static final String TAG = "Dave";
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mIcons = new ArrayList<>();
    private static recyclerViewAdapter adapter;
    private String path = "";
    private File directory = new File(path);
    public ProgressBar progressBarLoading;
    private Handler mHandler;
    private SharedPreferences mPreferences;

    public static dirMTbrowse getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dir_mtbrowse);
        mTextMessage = findViewById(R.id.pathText);
        mPreferences = getSharedPreferences("daves", MODE_PRIVATE);
        path = mPreferences.getString("path", "/");

        View backBar = findViewById(R.id.copy_of_parent_layout); // click anywhere back icon or text or container
        backBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wrapListNewDir("Back");
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(dirMTbrowse.getInstance() , "set path to " + mTextMessage.getText(),
                        Toast.LENGTH_LONG).show();
                SharedPreferences.Editor spedit = mPreferences.edit();
                spedit.putString("path", path);
                spedit.commit();
                finish();
            }
        });

        progressBarLoading = findViewById(R.id.progressBar);
        mHandler = new Handler();
        instance = this;

        initDlist();
    }

    private void initDlist(){
        mNames.add("flipin");
        mIcons.add(R.drawable.ic_check_black_24dp);
        dinitRecyclerView();
        wrapListNewDir("");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void dinitRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recycler view");
        RecyclerView recyclerView = findViewById(R.id.mFileList );
        //recyclerViewAdapter adapter = new recyclerViewAdapter(mNames, this);
        adapter = new recyclerViewAdapter(mNames, mIcons,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void wrapListNewDir(String nDir){
        //mTextMessage.setText("loading");
        progressBarLoading.setVisibility(View.VISIBLE);
        listDirThread myp = new listDirThread(nDir);
        new Thread(myp).start();

    }

    class listDirThread implements Runnable {
        String nDir;

        listDirThread(String nndd) {
            this.nDir = nndd;
        }

        public void run() {
            String lastAbleToRead = path;
            if (nDir == "Back"){
                if (path.length() > 1){
                    path = path.substring(0, path.lastIndexOf("/") - 1); // remove trailing slash
                    path = path.substring(0, path.lastIndexOf("/") + 1); // remove last dir but leave preceeding slash
                }
            }
            else {
                path = path  + nDir + "/";
            }

            Log.d("Files", "Path extending " + path);
            File tDir = new File(path);
            if (tDir.isFile()){
                //MainActivity.getInstance().prepPlayer(path);

                mHandler.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.getInstance().songAudioPlayer.loadPlay(path);

                            }
                        }
                );
                finish();
                return;
            }
            File[] files;
            try {
                files = tDir.listFiles();
            }
            catch (Exception sss){
                Log.d("Files", "security exception " + path);
                return;
            }

            try {
                Integer lll = files.length;
            }
            catch (Exception eee){
                Log.d("Files", "some exception " + eee);
                path = lastAbleToRead;
                mHandler.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(dirMTbrowse.getInstance(), "Unable to read Dir !",
                                        Toast.LENGTH_LONG).show();
                                progressBarLoading.setVisibility(View.INVISIBLE);
                            }
                        }
                );

                return;
            }

            mNames.clear();
            mIcons.clear();
            //mTextMessage.setText(path + " count= " + files.length );
            //mNames.add("Back");
            //mIcons.add(R.drawable.ic_undo_black_24dp);

            for (int i=0; i<files.length; ++i){
                if(files[i].isDirectory()){
                    mNames.add(files[i].getName());
                    mIcons.add(R.drawable.ic_folder_black_24dp);
                }
                else
                {
                    mNames.add(files[i].getName());
                    mIcons.add(R.drawable.ic_info_outline_black_24dp);
                }


            }

            mHandler.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            mTextMessage.setText(path);
                            adapter.notifyDataSetChanged();
                            progressBarLoading.setVisibility(View.INVISIBLE);
                        }
                    }
            );
            //adapter.notifyDataSetChanged();
        }
    }

}
