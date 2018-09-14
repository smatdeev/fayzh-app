package com.smartdeev.faizyah;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.general.errors.OnInvalidPathListener;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.service.JcPlayerManagerListener;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.smartdeev.faizyah.AdapterSound.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Sound extends AppCompatActivity implements OnInvalidPathListener, JcPlayerManagerListener{

    private static final String TAG = Sound.class.getSimpleName();
    private JcPlayerView player;
    private RecyclerView recyclerView;
    private AdapterSound audioAdapter;
    private RequestQueue mRequestQueue;



    private ProgressBar progressBarpage;
    private TextView txtlding,listtitle;
    SwipyRefreshLayout mSwipyRefreshLayout;
    Typeface font, font1; // الخط

    private ArrayList<ItemSound> mExampleList;
    private List<JcAudio> jcAudioList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recycle_sound);
        recyclerView = findViewById(R.id.recycler_view);
        player = findViewById(R.id.jcplayer);

        mRequestQueue = Volley.newRequestQueue(this);


        //loding waite
        progressBarpage = (ProgressBar) findViewById(R.id.progressBarpage);
        progressBarpage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        txtlding =(TextView) findViewById(R.id.txtlding);
        txtlding.setVisibility(View.VISIBLE);
        //loding waite

        //الخطوط
        font = Typeface.createFromAsset(getAssets(),"fonts/DroidKufi-Bold.ttf");
        font1 = Typeface.createFromAsset(getAssets(),"fonts/DINNEXTLTARABIC-MEDIUM_0.TTF");

        //عنوان الصفحة
        listtitle =(TextView) findViewById(R.id.titlelist);
        listtitle.setText("قائمة الصوتيات");
        listtitle.setTypeface(font);


        ArrayList<JcAudio> saeed = new ArrayList<>();
        // String url = "http://10.0.2.2/servsaeed/index.php/homenews/sound";
        String url = "https://dleelbaha.com/fayziah/index.php/homenews/sound";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,(JSONObject response) -> {

                    //loding waite
                    progressBarpage.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    txtlding.setVisibility(View.GONE);
                    //loding waite

                    try {
                        JSONArray jsonArray = response.getJSONArray("sound");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject hit = jsonArray.getJSONObject(i);
                            String titlesound = hit.getString("soundtitle");
                            String dessound = hit.getString("soundtext");
                            String urlsound = hit.getString("soundup");

                            saeed.add(JcAudio.createFromURL(titlesound,urlsound));

                            player.initPlaylist(saeed, this);
                            adapterSetup();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> error.printStackTrace());

        mRequestQueue.add(request);


    }

    @Override
    protected void onStop() {
        super.onStop();
        player.createNotification();
    }

    protected void adapterSetup() {
        audioAdapter = new AdapterSound(player.getMyPlaylist());
        audioAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                player.playAudio(player.getMyPlaylist().get(position));
                //صورة


            }


        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(audioAdapter);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

    }

    @Override
    public void onPause() {
        super.onPause();
        player.createNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.kill();
    }

    @Override
    public void onPathError(JcAudio jcAudio) {
        Toast.makeText(this, jcAudio.getPath() + " with problems", Toast.LENGTH_LONG).show();
//        player.removeAudio(jcAudio);
//        player.next();
    }


    @Override
    public void onPreparedAudio(JcStatus status) {

    }

    @Override
    public void onCompletedAudio() {

    }

    @Override
    public void onPaused(JcStatus status) {

    }

    @Override
    public void onContinueAudio(JcStatus status) {

    }

    @Override
    public void onPlaying(JcStatus status) {


    }

    @Override
    public void onTimeChanged(@NonNull JcStatus status) {
        updateProgress(status);


    }

    @Override
    public void onJcpError(@NonNull Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void updateProgress(final JcStatus jcStatus) {
        Log.d(TAG, "Song id = " + jcStatus.getJcAudio().getId() + "Song duration = " + jcStatus.getDuration());
        Log.d(TAG, "Song duration = " + jcStatus.getDuration()+ "\n song position = " + jcStatus.getCurrentPosition());



        runOnUiThread(() -> {
            // calculate progress
            float progress = (float) (jcStatus.getDuration() - jcStatus.getCurrentPosition())
                    / (float) jcStatus.getDuration();
            progress = 1.0f - progress;
            audioAdapter.updateProgress(jcStatus.getJcAudio(), progress);

        });
    }



//    @Override
//    public void onStopped(JcStatus status) {
//
//    }
}