package com.nextlevel.playarduino.arduinofullstack.Main.LinkerDeviceList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextlevel.playarduino.arduinofullstack.Models.User;
import com.nextlevel.playarduino.arduinofullstack.R;
import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;

import java.util.List;

/**
 * Created by sukumar on 8/20/17.
 */

public class SubscribedUserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<User> subscribedUsersList;
    RecyclerView mLinkerDeviceRecyclerView;

    int visibleItems = 0;

    public SubscribedUserListAdapter(Context context, List<User> subscribedUserList, RecyclerView recyclerView) {
        this.subscribedUsersList = subscribedUserList;
        mContext = context;
        visibleItems = subscribedUsersList.size();
        mLinkerDeviceRecyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_list_row, parent, false);
            return new UserViewHolder(itemView);
        }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {

                UserViewHolder holder = (UserViewHolder) viewholder;
                holder.userDisplayName.getRootView().setVisibility(View.VISIBLE);
                holder.userDisplayName.getRootView().setOnClickListener(userViewClickListner);
                User user = subscribedUsersList.get(position);
                holder.userDisplayName.setText(user.getDisplayUserName());
                if (Constants.PERMISSION_LEVEL_ROOT.equals(user.getPermissionLevel())) {
                    holder.adminText.setVisibility(View.VISIBLE);
                    holder.adminText.setText(mContext.getResources().getString(R.string.root_user));
                    holder.adminText.setTextColor(mContext.getResources().getColor(R.color.red));
                } else if (Constants.PERMISSION_LEVEL_ADMIN.equals(user.getPermissionLevel())) {
                    holder.adminText.setVisibility(View.VISIBLE);
                    holder.adminText.setText(mContext.getResources().getString(R.string.admin_user));
                }
                holder.userStatusIndicator.setSelected(user.isAlive());
        }


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return subscribedUsersList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView userDisplayName, adminText, mLastMessage;

        public ImageView userStatusIndicator;

        public UserViewHolder(View view) {
            super(view);
            userDisplayName = (TextView) view.findViewById(R.id.user_name);

            adminText = (TextView) view.findViewById(R.id.admin_text);
            userStatusIndicator = (ImageView) view.findViewById(R.id.user_status_indicator);
            mLastMessage = (TextView) view.findViewById(R.id.last_message);
        }
    }


    View.OnClickListener userViewClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
