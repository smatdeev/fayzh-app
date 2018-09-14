package com.smartdeev.faizyah;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jean.jcplayer.model.JcAudio;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.v4.content.ContextCompat.getColor;


public class AdapterSound extends RecyclerView.Adapter<AdapterSound.AudioAdapterViewHolder> {
    private static final String TAG = AdapterSound.class.getSimpleName();
    private static OnItemClickListener mListener;
    private List<JcAudio> jcAudioList;
    private SparseArray<Float> progressMap = new SparseArray<>();
    Typeface font, font1; // الخط
    //صورة
    private int row_index;
    private Context context;


    public AdapterSound(List<JcAudio> jcAudioList) {
        this.jcAudioList = jcAudioList;
        setHasStableIds(true);

        this.context = context;

    }

    // Define the method that allows the parent activity or fragment to define the jcPlayerManagerListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
        //صورة
    }

    @Override
    public AudioAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sound, parent, false);
        return new AudioAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(AudioAdapterViewHolder holder, int position) {
        String title =jcAudioList.get(position).getTitle();
        //اظهار ارقام الليسته
       // String title = position + 1 + "    " + jcAudioList.get(position).getTitle();
        holder.audioTitle.setText(title);
        holder.itemView.setTag(jcAudioList.get(position));
        applyProgressPercentage(holder, progressMap.get(position, 0.0f));

       // تغيير الخلفية عند النقر
        holder.backgrwndcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                //هذا لطلب ليسته الصوت
                if (mListener != null) mListener.onItemClick(position);

                notifyDataSetChanged();
            }
        });
        if(row_index==position){
            holder.backgrwndcolor.setBackgroundColor(Color.parseColor("#eeecee"));
            holder.iv_play_active.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.backgrwndcolor.setBackgroundColor(Color.TRANSPARENT);
            holder.iv_play_active.setVisibility(View.INVISIBLE);
        }



    }

    @Override
    public long getItemId(int position) {
       // return jcAudioList.get(position).getId();
        return position;




    }
    private void applyProgressPercentage(AudioAdapterViewHolder holder, float percentage) {
        Log.d(TAG, "applyProgressPercentage() with percentage = " + percentage);
        LinearLayout.LayoutParams progress = (LinearLayout.LayoutParams) holder.viewProgress.getLayoutParams();
        LinearLayout.LayoutParams antiProgress = (LinearLayout.LayoutParams) holder.viewAntiProgress.getLayoutParams();

        progress.weight = percentage;
        holder.viewProgress.setLayoutParams(progress);

        antiProgress.weight = 1.0f - percentage;
        holder.viewAntiProgress.setLayoutParams(antiProgress);

    }

    @Override
    public int getItemCount() {
        return jcAudioList == null ? 0 : jcAudioList.size();

    }

    public void updateProgress(JcAudio jcAudio, float progress) {
        int position = jcAudioList.indexOf(jcAudio);
        Log.d(TAG, "Progress = " + progress);

        progressMap.put(position, progress);
        if (progressMap.size() > 1) {
            for (int i = 0; i < progressMap.size(); i++) {
                if (progressMap.keyAt(i) != position) {
                    Log.d(TAG, "KeyAt(" + i + ") = " + progressMap.keyAt(i));
                    notifyItemChanged(progressMap.keyAt(i));
                    progressMap.delete(progressMap.keyAt(i));
                }
            }
        }
        notifyItemChanged(position);
    }

    // Define the mListener interface
    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    class AudioAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView audioTitle;
        private View viewProgress;
        private View viewAntiProgress;
        //صورة
        private ImageView  iv_play_active;
        private View backgrwndcolor;


        public AudioAdapterViewHolder(View view) {
            super(view);
            this.audioTitle = view.findViewById(R.id.audio_title);
            viewProgress = view.findViewById(R.id.song_progress_view);
            viewAntiProgress = view.findViewById(R.id.song_anti_progress_view);

            //صورة
            iv_play_active = (ImageView) itemView.findViewById(R.id.iv_play_active);
            backgrwndcolor=(LinearLayout)itemView.findViewById(R.id.color_row_fiz);


            //font SAEED
            font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/DroidKufi-Bold.ttf");
            font1 = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/DINNEXTLTARABIC-MEDIUM_0.TTF");
            this.audioTitle.setTypeface(font1);





            //تم الغاء هذا الاستدعاء لاننا قمنا باستدعائة في الاعلي
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Triggers click upwards to the adapter on click
//                    if (mListener != null) mListener.onItemClick(getAdapterPosition());
//
//
//                }
//            });





        }
    }

    //صورة



}
