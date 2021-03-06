package com.beachpartnerllc.beachpartner.adpters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beachpartnerllc.beachpartner.CircularImageView;
import com.beachpartnerllc.beachpartner.R;
import com.beachpartnerllc.beachpartner.fragments.ChatFragmentPage;
import com.beachpartnerllc.beachpartner.fragments.CoachProfileFragment;
import com.beachpartnerllc.beachpartner.fragments.MessageFragment;
import com.beachpartnerllc.beachpartner.fragments.ProfileFragment;
import com.beachpartnerllc.beachpartner.models.BpFinderModel;
import com.beachpartnerllc.beachpartner.utils.AppConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seq-kala on 28/3/18.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<BpFinderModel> mCountryModel;
    ArrayList<BpFinderModel>list;
    MessageFragment messageFragment;
    public ChatListAdapter(MessageFragment messageFragment,Context context, ArrayList<BpFinderModel> chatList) {
        this.mContext = context;
        this.list     = chatList;
        this.messageFragment = messageFragment;
    }

    public void setFilter(List<BpFinderModel> countryModels) {
        mCountryModel = new ArrayList<>();
        mCountryModel.addAll(countryModels);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        //holder.person_pic.setImageResource(model.getImage());
        final BpFinderModel message = list.get(position);

        holder.txv_name.setText(message.getBpf_firstName()+ " "+ message.getBpf_lastName());
        if(list.get(position).getBpf_imageUrl()!=null){
            if(!list.get(position).getBpf_imageUrl().equals("null")){
                Glide.with(mContext).load(list.get(position).getBpf_imageUrl()).apply(RequestOptions.placeholderOf(R.mipmap.ic_user_img)).into(holder.person_pic);
                //holder.profilePic.setImageURI(Uri.parse(dataLists.get(position).getConnected_imageUrl()));
            }else {
                holder.person_pic.setImageResource(R.mipmap.ic_user_img);
            }
        }


        holder.single_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();

                ChatFragmentPage chatFragmentPage = new ChatFragmentPage();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstants.CHAT_USER, message);
                chatFragmentPage.setArguments(bundle);
                FragmentManager manager = ((FragmentActivity)mContext).getSupportFragmentManager();
                FragmentTransaction ctrans = manager.beginTransaction();
                //ctrans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                ctrans.replace(R.id.container,chatFragmentPage);
                ctrans.addToBackStack(null);
                ctrans.commit();
                disableTabMenuButtons();
            }
        });

        holder.person_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userType = message.getBpf_userType();

                //add bundle to be transfered to coachProfile or athleteProfile
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.USER_DETAIL,message);

                if(userType.equalsIgnoreCase("Athlete")){
                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setArguments(bundle);
                    FragmentManager manager = ((FragmentActivity)mContext).getSupportFragmentManager();
                    FragmentTransaction ctrans = manager.beginTransaction();
                    //ctrans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    ctrans.replace(R.id.container,profileFragment);
                    ctrans.addToBackStack(null);
                    ctrans.commit();
                    disableTabMenuButtons();
                }
                else if(userType.equalsIgnoreCase("Coach")){
                    CoachProfileFragment coachProfileFragment = new CoachProfileFragment();
                    coachProfileFragment.setArguments(bundle);
                    FragmentManager coachManager = ((FragmentActivity)mContext).getSupportFragmentManager();
                    FragmentTransaction coachtrans = coachManager.beginTransaction();
                    //ctrans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    coachtrans.replace(R.id.container,coachProfileFragment);
                    coachtrans.addToBackStack(null);
                    coachtrans.commit();
                    disableTabMenuButtons();
                }

            }
        });

    }

    private void disableTabMenuButtons(){
        messageFragment.disableMenu();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout single_item;
        public TextView txv_name,txtv_msg,txtv_activeStatus,txtv_time;
        public CircularImageView person_pic;

        public ViewHolder(View itemView) {
            super(itemView);

            single_item =   (LinearLayout) itemView.findViewById(R.id.item);
            txv_name    =   (TextView)     itemView.findViewById(R.id.pname);
            //txtv_msg    =   (TextView)     itemView.findViewById(R.id.pmessage);
            //txtv_activeStatus=(TextView)   itemView.findViewById(R.id.pactive_status);
            txtv_time   =   (TextView)     itemView.findViewById(R.id.txtv_time);
            person_pic  =   (CircularImageView)    itemView.findViewById(R.id.chatPic);
        }
    }
}
