package com.goldemo.beachpartner.adpters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.goldemo.beachpartner.MyInterface;
import com.goldemo.beachpartner.R;
import com.goldemo.beachpartner.utils.RotateLoading;

public class TouristSpotCardAdapter extends ArrayAdapter<TouristSpot> {

    private String YOUR_FRAGMENT_STRING_TAG;
    private Context mContext;
    MyInterface myInterface;

    public TouristSpotCardAdapter(Context context,MyInterface inter) {
        super(context,0);
        this.mContext=context;
        this.myInterface=inter;

    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        final ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_tourist_spot_card, parent, false);
            holder = new ViewHolder(contentView);

            holder.progressBar.setVisibility(View.VISIBLE);
            contentView.setTag(holder);

        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        final TouristSpot spot = getItem(position);

        holder.videoView.stopPlayback();
        holder.videoView.setVisibility(View.GONE);
        holder.spinnerView.setVisibility(View.GONE);
        holder.image.setVisibility(View.VISIBLE);

        holder.name.setText(spot.name);
        holder.city.setText(spot.city);
        Glide.with(getContext()).load(spot.img_url).into(holder.image);


       /* Video Tag onclick listener start*/

        holder.image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                holder.image.setVisibility(View.GONE);
                //holder.progressBar.setVisibility(View.VISIBLE);
                holder.spinnerView.setVisibility(View.VISIBLE);
                holder.spinnerView.start();
                holder.videoView.setVisibility(View.VISIBLE);
                holder.videoView.setVideoURI(Uri.parse(spot.url));
               // dialog.setMessage("Please wait");

                //holder.videoView.start();
                playvideo(holder);
                return false;
            }
        });



        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myInterface.addView(spot.img_url,spot.city);
                //Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });





        //End video tag here 8/02/2018


        return contentView;
    }


    private void playvideo(final ViewHolder holder) {

        holder.videoView.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            holder.videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                    if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                        holder.spinnerView.setVisibility(View.GONE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                        holder.spinnerView.start();
                        holder.spinnerView.setVisibility(View.VISIBLE);
                    }
                    if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                        holder.spinnerView.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
        }
    }



    private static class ViewHolder {
        public TextView name;
        public TextView city;
        public ImageView image;
        public VideoView videoView;
        public ProgressBar progressBar;
        public RotateLoading spinnerView;
        public Button info;
        //public VideoPlayView videoPlayView;
        // public FullscreenVideoLayout videoLayout;

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.item_tourist_spot_card_name);
            city = (TextView) view.findViewById(R.id.item_tourist_spot_card_city);
            image = (ImageView) view.findViewById(R.id.img_view);
            videoView = (VideoView) view.findViewById(R.id.item_tourist_spot_card_image);
            progressBar=(ProgressBar)view.findViewById(R.id.prsbar);
            info = (Button)view.findViewById(R.id.btnInfo);

            spinnerView  =   (RotateLoading)view.findViewById(R.id.my_spinner);

            //this.videoLayout = (FullscreenVideoLayout) view.findViewById(R.id.item_tourist_spot_card_image);
            //this.image = (ImageView) view.findViewById(R.id.item_tourist_spot_card_image);
            // this.videoPlayView = (VideoPlayView) view.findViewById(R.id.picassoVideoView);
        }
    }

}

