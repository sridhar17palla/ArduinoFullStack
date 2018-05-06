package com.nextlevel.playarduino.arduinofullstack.Utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nextlevel.playarduino.arduinofullstack.R;

/**
 * Created by sukumar on 8/28/17.
 */

public class Utils {

    //TODO: need to set a boolean which confirms whether the current running device is LinkerDevice
    public static boolean isThisLinkerDevice = false;

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static Dialog showCustomDialog(Context context, @StringRes int stringId){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout,null);
        GlobalDialog errorDialog = new GlobalDialog(context, R.style.Max_Width_Theme_Dialog , view);
        ((TextView)view.findViewById(R.id.dialog_text)).setText(stringId);
        return errorDialog;
    }

    public static AlertDialog showErrorDialog(Context context, @StringRes int title, @StringRes int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setMessage(message)
                .setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static class GlobalDialog extends Dialog {

        private int layoutId;

        private View view;

        private GlobalDialog(Context context, int themeResId, @LayoutRes int layoutResId){
            super(context,themeResId);
            this.layoutId = layoutResId;
        }

        private GlobalDialog(Context context, int themeResId, View view){
            super(context,themeResId);
            this.view = view;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(this.getWindow()!=null){
                this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                if(layoutId>0) {
                    setContentView(layoutId);
                }else if(view!=null){
                    setContentView(view);
                }
                setCanceledOnTouchOutside(true);
            }
        }
    }
}
