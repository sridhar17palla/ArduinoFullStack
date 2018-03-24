package com.nextlevel.playarduino.arduinofullstack.Main.LinkerDeviceList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nextlevel.playarduino.arduinofullstack.Models.LinkerDevice;
import com.nextlevel.playarduino.arduinofullstack.Models.User;
import com.nextlevel.playarduino.arduinofullstack.R;
import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sukumar on 8/20/17.
 */

public class LinkerDeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int HEADER_TYPE_LINKER_VIEW = 0;
    private static int HEADER_TYPE_USER_VIEW = 1;

    Context mContext;
    List<PresenterLinkerView> mLinkerDeviceList;

    int visibleItems = 0;

    public LinkerDeviceListAdapter(Context context, List<LinkerDevice> linkerDeviceList) {
        customLinkerViewRows(linkerDeviceList);
        mContext = context;
        visibleItems = mLinkerDeviceList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (HEADER_TYPE_LINKER_VIEW == viewType) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.linker_device_list_row, parent, false);
            return new LinkerDeviceHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_list_row, parent, false);
            return new UserViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (mLinkerDeviceList.get(position).linkerDevice != null) {
            return HEADER_TYPE_LINKER_VIEW;
        } else {
            return HEADER_TYPE_USER_VIEW;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {
        PresenterLinkerView presenterLinkerView = mLinkerDeviceList.get(position);

        if (viewholder instanceof LinkerDeviceHolder) {
            LinkerDeviceHolder holder = (LinkerDeviceHolder) viewholder;
            final LinkerDevice linkerDevice = presenterLinkerView.linkerDevice;
            holder.linkerDeviceName.setText(linkerDevice.getDeviceName());
            holder.linkerDeviceRootUser.setText(linkerDevice.getRootUser().getUserName());
            if (Constants.PERMISSION_LEVEL_ROOT.equals(linkerDevice.getRelationToCurrentUser())) {
                holder.adminText.setVisibility(View.VISIBLE);
                holder.adminText.setText(mContext.getResources().getString(R.string.root_user));
                holder.adminText.setTextColor(mContext.getResources().getColor(R.color.red));
            } else if (Constants.PERMISSION_LEVEL_ADMIN.equals(linkerDevice.getRelationToCurrentUser())) {
                holder.adminText.setVisibility(View.VISIBLE);
                holder.adminText.setText(mContext.getResources().getString(R.string.admin_user));
            }
            holder.linkerDeviceStatusIndicator.setSelected(linkerDevice.isAlive());

            //TODO : check time stamp whether to display last command sent or last message received from
            //Linker device for now showing the last command received by the LinkerDevice.

            holder.mLastMessage.setText(linkerDevice.getInput());

            holder.expandCollapseButton.setOnClickListener(expandCollapseButtonListner);

            holder.expandCollapseButton.setTag(position);

            holder.linkerDeviceName.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                    Fragment fragment = null;
                    if (fragment == null) {
                        fragment = new LinkerDeviceDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(LinkerDeviceDetailsFragment.LINKER_DEVICE_DETAILS, new Gson().toJson(linkerDevice));
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction()
                                .add(R.id.fragments, fragment)
                                .commit();
                    }

                }
            });

            //Set on click listner here.....
        } else {
            UserViewHolder holder = (UserViewHolder) viewholder;
            if (presenterLinkerView.userVisibility) {
                holder.userDisplayName.getRootView().setVisibility(View.VISIBLE);
                holder.userDisplayName.getRootView().setOnClickListener(userViewClickListner);
                User user = presenterLinkerView.user;
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
            } else {
                holder.userDisplayName.getRootView().setVisibility(View.GONE);
            }
        }
    }


    View.OnClickListener expandCollapseButtonListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();

            boolean visibility = "-".equals(((Button) v).getText());

            //TODO: Do Recyclerview animations upon Expand / Collapse.

            if (visibility && mLinkerDeviceList.get(position).linkerDevice != null) {

                for (int i = 0; i < mLinkerDeviceList.get(position).linkerDevice.getSubscribedUsers().size(); i++) {

                    if (mLinkerDeviceList.get(position + 1).user != null) {
                        mLinkerDeviceList.remove(position + 1);
                    }
                    ((Button) v).setText("+");
                }
            } else {

                for (User user : mLinkerDeviceList.get(position).linkerDevice.getSubscribedUsers()) {
                    mLinkerDeviceList.add(position + 1, new PresenterLinkerView(null, user, HEADER_TYPE_USER_VIEW));
                }
                ((Button) v).setText("-");
            }
            notifyDataSetChanged();
        }
    };

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mLinkerDeviceList.size();
    }


    public class LinkerDeviceHolder extends RecyclerView.ViewHolder {
        public TextView linkerDeviceName, linkerDeviceRootUser, adminText, mLastMessage;
        public Button expandCollapseButton;
        public ImageView linkerDeviceStatusIndicator;

        public LinkerDeviceHolder(View view) {
            super(view);
            linkerDeviceName = (TextView) view.findViewById(R.id.linker_device_name);
            linkerDeviceRootUser = (TextView) view.findViewById(R.id.linker_device_root_user);
            adminText = (TextView) view.findViewById(R.id.admin_text);
            linkerDeviceStatusIndicator = (ImageView) view.findViewById(R.id.device_status_indicator);
            mLastMessage = (TextView) view.findViewById(R.id.last_message);
            expandCollapseButton = (Button) view.findViewById(R.id.expand_collapse_button);
        }
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

    private class PresenterLinkerView {

        boolean userVisibility = true;
        LinkerDevice linkerDevice;
        User user;
        int viewType;

        public PresenterLinkerView(LinkerDevice linkerDevice, User user, int viewType) {
            this.linkerDevice = linkerDevice;
            this.user = user;
            this.viewType = viewType;
        }
    }

    private void customLinkerViewRows(List<LinkerDevice> linkerDevices) {

        if (mLinkerDeviceList == null) {
            mLinkerDeviceList = new ArrayList<>();
        }
        for (LinkerDevice linkerDevice1 : linkerDevices) {

            mLinkerDeviceList.add(new PresenterLinkerView(linkerDevice1, null, HEADER_TYPE_LINKER_VIEW));

            for (User user : linkerDevice1.getSubscribedUsers()) {
                mLinkerDeviceList.add(new PresenterLinkerView(null, user, HEADER_TYPE_USER_VIEW));
            }
        }
    }

    private void expandCollapseRecyclerView() {

    }


    View.OnClickListener userViewClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
