package com.nextlevel.playarduino.arduinofullstack.Main.Schema;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nextlevel.playarduino.arduinofullstack.Base.BaseFragment;
import com.nextlevel.playarduino.arduinofullstack.R;
import com.nextlevel.playarduino.arduinofullstack.Utility.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sukumar on 12/10/17.
 */

/*
    This fragment enables the user to edit button characteristics.
 */
public class SchemaEditFragment extends BaseFragment {

    protected static String USER_VIEWS_LIST_NAME = "buttonViewModelList";

    protected static int PRE_DEFINED_BUTTON = 0;
    protected static int USER_DEFINED_BUTTON = 1;


    protected ViewGroup rootLayout1, rootLayout2, mRootView;
    protected int _xDelta;
    protected int _yDelta;
    protected Context mContext;
    private boolean mEditModeEnable = false;

    protected View highLightedView;
    protected float scale = 1f;
    protected ScaleGestureDetector detector;


    protected static final int ROUND_BUTTON_TYPE = 0;
    protected static final int LEFT_ARROW_BUTTON_TYPE = 1;
    protected static final int RIGHT_ARROW_BUTTON_TYPE = 2;
    protected static final int UP_ARROW_BUTTON_TYPE = 3;
    protected static final int BELOW_ARROW_BUTTON_TYPE = 4;
    protected static final int GYRO_X_BUTTON_TYPE = 5;
    protected static final int GYRO_Y_BUTTON_TYPE = 6;
    protected static final int GYRO_Z_BUTTON_TYPE = 7;
    protected static final int JOY_STICK_BUTTON_TYPE = 8;
    protected static final int RECTANGLE_BUTTON_TYPE = 9;

    //TODO UI 1: For now default button count is 4. Just UP, DOWN, RIGHT, LEFT buttons.
    protected static final int DEFAULT_BUTTON_COUNT = 7;

    //TODO UI 1: Maximum & Minimum width and height should be calculated programmatically depending upon screen size.
    protected static Integer MAXIMUM_WIDTH_BUTTON = Utils.dpToPx(250);
    protected static Integer MAXIMUM_HEIGHT_BUTTON = Utils.dpToPx(250);
    protected static Integer MINIUMUM_WIDTH_BUTTON = Utils.dpToPx(80);
    protected static Integer MINIMUM_HEIGHT_BUTTON = Utils.dpToPx(80);

    public static Integer DEFAULT_WIDTH_BUTTON = Utils.dpToPx(100);
    public static Integer DEFAULT_HEIGHT_BUTTON = Utils.dpToPx(100);


    List<ButtonViewModel> buttonViewModelList;

    private static ImageView seekBarThumbX, seekBarThumbY, seekBarThumbZ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mRootView = (ViewGroup) inflater.inflate(R.layout.schema_edit_fragment, container, false);
        rootLayout1 = (ViewGroup) mRootView.findViewById(R.id.layout_1);
        rootLayout1.setAlpha(0.5f);
        rootLayout2 = (ViewGroup) mRootView.findViewById(R.id.layout_2);
        mContext = this.getContext();
        detector = new ScaleGestureDetector(mContext, new ScaleListener());
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!(this instanceof SchemaFragment)) {
            setEditMode(true);
        } else {
            setEditMode(false);
        }
        drawSchema();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void drawSchema() {

        if (buttonViewModelList == null) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            Gson gson = new Gson();
            String json = sharedPrefs.getString(USER_VIEWS_LIST_NAME, null);
            if (json != null) {
                Type type = new TypeToken<ArrayList<ButtonViewModel>>() {
                }.getType();
                buttonViewModelList = gson.fromJson(json, type);
            } else {
                addDefaultButtons();
            }
        }

        //TODO LOGIC 1: Need to check this. buttonViewModelList is sometimes NULL.
        if (buttonViewModelList != null) {
            for (ButtonViewModel buttonViewModel : buttonViewModelList) {
                if (buttonViewModel.catogory != PRE_DEFINED_BUTTON || mEditModeEnable) {
                    drawNewImage(buttonViewModel);
                }
            }
        }
        rootLayout2.invalidate();
    }

    protected void addDefaultButtons() {
        buttonViewModelList = new ArrayList<>();
        ButtonViewModel buttonViewModel;
        for (int i = 1; i <= DEFAULT_BUTTON_COUNT; i++) {
            if (buttonViewModelList == null || buttonViewModelList.size() == 0) {
                buttonViewModel = new ButtonViewModel(0, i, 0, 100, DEFAULT_WIDTH_BUTTON, DEFAULT_HEIGHT_BUTTON, null, null, PRE_DEFINED_BUTTON);
                buttonViewModelList.add(buttonViewModel);
            } else {
                ButtonViewModel lastButtonViewModel = buttonViewModelList.get(buttonViewModelList.size() - 1);
                buttonViewModel = new ButtonViewModel(lastButtonViewModel.id + 1, i, 0, ((i) * 100), DEFAULT_WIDTH_BUTTON, DEFAULT_HEIGHT_BUTTON, null, null, PRE_DEFINED_BUTTON);
                buttonViewModelList.add(buttonViewModel);
            }
        }

    }


    protected View drawNewImage(ButtonViewModel buttonViewModel) {

        View imageContainer = null;

        switch (buttonViewModel.buttonType) {
           /* case ROUND_BUTTON_TYPE:
                break;*/
            case BELOW_ARROW_BUTTON_TYPE:
                imageContainer = setArrowButton(buttonViewModel, -180, "DOWN");
                break;
            case UP_ARROW_BUTTON_TYPE:
                imageContainer = setArrowButton(buttonViewModel, 0, "UP");
                break;
            case LEFT_ARROW_BUTTON_TYPE:
                imageContainer = setArrowButton(buttonViewModel, -90, "LEFT");
                break;
            case RIGHT_ARROW_BUTTON_TYPE:
                imageContainer = setArrowButton(buttonViewModel, 90, "RIGHT");
                break;
            case GYRO_X_BUTTON_TYPE:
                imageContainer = setSeekbar(buttonViewModel, 0);
                break;
            case GYRO_Y_BUTTON_TYPE:
                imageContainer = setSeekbar(buttonViewModel, -90);
                break;
            case GYRO_Z_BUTTON_TYPE:
                imageContainer = setSeekbar(buttonViewModel, -60);
                break;
        /*	case JOY_STICK_BUTTON_TYPE:
				drawable = getResources().getDrawable(R.drawable.arrow_up_button);
				firstImageView.setBackground(drawable);
				firstImageView.setRotation(45);
				break;
			case RECTANGLE_BUTTON_TYPE:
				drawable = getResources().getDrawable(R.drawable.arrow_up_button);
				firstImageView.setBackground(drawable);
				firstImageView.setRotation(45);
				break;*/
            default:
                imageContainer = setArrowButton(buttonViewModel, -180, "DOWN");
                // drawable = getResources().getDrawable(R.drawable.arrow_up_button,wrapper.getTheme());
                //firstImageView.setBackground(drawable);
                //firstImageView.setRotation(45);
                break;
        }
        //drawable.mutate().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        if (mEditModeEnable) {
            imageContainer.setOnTouchListener(layout2TouchListner);
            imageContainer.setOnClickListener(null);
        } else {
            imageContainer.setOnTouchListener(null);
            imageContainer.setOnClickListener(buttonOnClickListner);
        }
        imageContainer.setTag(buttonViewModel.id);
        rootLayout2.addView(imageContainer);
        return imageContainer;
    }

    private View setArrowButton(ButtonViewModel buttonViewModel, float angle, String text) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View buttonContainer = inflater.inflate(R.layout.schema_button_container, null, false);

        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT); //size of the button
        // layoutParams.gravity = Gravity.TOP|Gravity.LEFT;
        layoutParams.leftMargin = buttonViewModel.positionX;
        layoutParams.topMargin = buttonViewModel.positionY;
        buttonContainer.setLayoutParams(layoutParams);

        FrameLayout arrowButtonContainer = (FrameLayout) buttonContainer.findViewById(R.id.arrow_button_container);
        arrowButtonContainer.setVisibility(View.VISIBLE);

        LinearLayout seekBarContainer = (LinearLayout) buttonContainer.findViewById(R.id.seek_bar_container);
        seekBarContainer.setVisibility(View.GONE);

        ImageView buttonImageView = (ImageView) arrowButtonContainer.findViewById(R.id.image_view);
        FrameLayout.LayoutParams imageParams =
                new FrameLayout.LayoutParams(buttonViewModel.width, buttonViewModel.height);
        buttonImageView.setLayoutParams(imageParams);

        TextView textInsideImage = (TextView) arrowButtonContainer.findViewById(R.id.text_on_image);
        FrameLayout.LayoutParams textInsideParams =
                new FrameLayout.LayoutParams(buttonViewModel.width / 2, FrameLayout.LayoutParams.WRAP_CONTENT);
        textInsideImage.setLayoutParams(textInsideParams);

        TextView textUnderImage = (TextView) buttonContainer.findViewById(R.id.text_under_image);
        LinearLayout.LayoutParams textUnderParams =
                new LinearLayout.LayoutParams(buttonViewModel.width, LinearLayout.LayoutParams.WRAP_CONTENT);
        textUnderImage.setLayoutParams(textUnderParams);

        ContextThemeWrapper wrapper = new ContextThemeWrapper(mContext, R.style.BlackColor);
        Drawable drawable = getResources().getDrawable(R.drawable.arrow_up_button, wrapper.getTheme());
        buttonImageView.setBackground(drawable);
        buttonImageView.setRotation(angle);
        textInsideImage.setText(text);
        buttonImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return buttonContainer;
    }

    private View setSeekbar(ButtonViewModel buttonViewModel, float angle) {
        //TODO UI 3: Only one Gyro of each type is allowed for one remote.
        //At present I don't see any use of multiple gyro sensors for single remote.
        //This kind of behaviour will be easy to program.
        // Gyro sensors are not resizable. They are fixed in original dimensions
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View buttonContainer = inflater.inflate(R.layout.schema_button_container, null, false);

        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(DEFAULT_WIDTH_BUTTON, DEFAULT_HEIGHT_BUTTON); //size of the button
        //layoutParams.gravity = Gravity.CENTER;
        layoutParams.leftMargin = buttonViewModel.positionX;
        layoutParams.topMargin = buttonViewModel.positionY;
        buttonContainer.setLayoutParams(layoutParams);

        FrameLayout arrowButtonContainer = (FrameLayout) buttonContainer.findViewById(R.id.arrow_button_container);
        arrowButtonContainer.setVisibility(View.GONE);

        LinearLayout seekBarContainer = (LinearLayout) buttonContainer.findViewById(R.id.seek_bar_container);
        seekBarContainer.setVisibility(View.VISIBLE);

        ImageView seekBar = (ImageView) seekBarContainer.findViewById(R.id.seek_bar);
        if (angle == 0) {
            ImageView seekBarX = (ImageView) seekBarContainer.findViewById(R.id.seek_bar_thumb);
            seekBar.setRotation(0);
        } else if (angle == -90) {
            seekBar.setRotation(-90);
            ImageView seekBarY = (ImageView) seekBarContainer.findViewById(R.id.seek_bar_thumb);
        } else {
            seekBar.setRotation(-45);
            ImageView seekBarZ = (ImageView) seekBarContainer.findViewById(R.id.seek_bar_thumb);
        }
        seekBar.setVisibility(View.VISIBLE);

        return buttonContainer;
    }

    /**
     * Only takes value from -100 to 100.
     * Neutral point is 0.
     */
    public static void setXGyro(int gyroX) {
        if (seekBarThumbX != null) {
            float width = seekBarThumbX.getRootView().findViewById(R.id.seek_bar).getWidth();
            float deltaX = (width / 200) * (gyroX + 100);
            seekBarThumbX.setX(deltaX + (seekBarThumbX.getWidth() / 2));
        }
    }

    /**
     * Only takes value from -100 to 100.
     * Neutral point is 0.
     */
    public static void setYGyro(int gyroY) {
        if (seekBarThumbY != null) {
            float height = seekBarThumbX.getRootView().findViewById(R.id.seek_bar).getWidth();
            float deltaY = (height / 200) * (gyroY + 100);
            seekBarThumbY.setY(height - deltaY + (seekBarThumbY.getHeight() / 2));
        }
    }

    /**
     * Only takes value from -100 to 100.
     * Neutral point is 0.
     */
    public static void setZGyro(int gyroZ) {
        if (seekBarThumbZ != null) {

            gyroZ = gyroZ / 2; //Not sure why to divide by 2. It just worked.

            float width = seekBarThumbZ.getRootView().findViewById(R.id.seek_bar).getWidth();
            float deltaX = (width / 200) * (gyroZ + 100);
            seekBarThumbZ.setX(deltaX + (seekBarThumbZ.getWidth() / 2));

            float height = seekBarThumbZ.getRootView().findViewById(R.id.seek_bar).getWidth();
            float deltaY = (height / 200) * (gyroZ + 100);
            seekBarThumbZ.setY(height - deltaY + (seekBarThumbZ.getHeight() / 2));

        }
    }

    public void setEditMode(boolean enable) {
        if (enable) {
            mEditModeEnable = true;
            rootLayout1.setAlpha(0.5f);
            rootLayout2.setOnTouchListener(rootLayoutTouchListner);
        } else {
            mEditModeEnable = false;
            rootLayout1.setAlpha(0f);
            rootLayout2.setOnTouchListener(null);
        }
    }

    View.OnTouchListener rootLayoutTouchListner = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return detector.onTouchEvent(event);
        }
    };

    View.OnTouchListener layout2TouchListner = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            detector.onTouchEvent(event);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:

                    //Only one gyro sensors of each type is allowed for each schema
                    for (ButtonViewModel buttonViewModel : buttonViewModelList) {
                        if (buttonViewModel.id == (Integer) view.getTag()) {
                            if (((buttonViewModel.buttonType == GYRO_X_BUTTON_TYPE && seekBarThumbX != null)
                                    || (buttonViewModel.buttonType == GYRO_Y_BUTTON_TYPE && seekBarThumbY != null)
                                    || (buttonViewModel.buttonType == GYRO_Z_BUTTON_TYPE && seekBarThumbZ != null))
                                    && buttonViewModel.catogory == PRE_DEFINED_BUTTON) {
                                return false;
                            }
                        }
                    }

                    FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    if (highLightedView != null) {
                        highLightedView.setBackground(null);
                    }
                    highLightedView = view;
                    //TODO LOGIC 1: Avoid below deprecated method
                    highLightedView.setBackground(mContext.getResources().getDrawable(R.drawable.high_lighted_blue_background));
                    break;
                case MotionEvent.ACTION_UP:
                    rootLayout1.setVisibility(View.VISIBLE);
                    //Save co-ordinates
                    for (ButtonViewModel buttonViewModel : buttonViewModelList) {
                        if (buttonViewModel.id == (Integer) view.getTag()) {

                            if (buttonViewModel.catogory == PRE_DEFINED_BUTTON) {
                                drawNewImage(buttonViewModel);
                                try {
                                    buttonViewModel = (ButtonViewModel) buttonViewModel.clone();
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }

                            buttonViewModel.positionX = X - _xDelta;
                            buttonViewModel.positionY = Y - _yDelta;
                            buttonViewModel.catogory = USER_DEFINED_BUTTON;
                            ButtonViewModel lastButtonViewModel = buttonViewModelList.get(buttonViewModelList.size() - 1);
                            buttonViewModel.id = lastButtonViewModel.id + 1;
                            buttonViewModelList.add(buttonViewModel);
                            view.setTag(lastButtonViewModel.id + 1);
                            break;
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    //TODO UI 1: Apply bondaries for view positions so they can't be drawn outside of window.
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view
                            .getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    view.setLayoutParams(layoutParams);
                    break;
            }
            rootLayout2.invalidate();
            return true;
        }
    };


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            //Check for Maximum & Minimum dimensions;
            //TODO UI 1: This check is dependent upon type of view too.
            int width = highLightedView.getWidth();
            int height = highLightedView.getHeight();
            if (highLightedView.getWidth() * highLightedView.getScaleX() > MAXIMUM_WIDTH_BUTTON && detector.getScaleFactor() > 1f
                    || highLightedView.getHeight() * highLightedView.getScaleY() > MAXIMUM_HEIGHT_BUTTON && detector.getScaleFactor() > 1f) {
                highLightedView.setScaleX(((float) MINIUMUM_WIDTH_BUTTON) / ((float) DEFAULT_WIDTH_BUTTON));
                highLightedView.setScaleY(((float) MINIMUM_HEIGHT_BUTTON) / ((float) DEFAULT_HEIGHT_BUTTON));
                return false;
            } else if (highLightedView.getWidth() * highLightedView.getScaleX() < MINIUMUM_WIDTH_BUTTON && detector.getScaleFactor() < 1f
                    || highLightedView.getHeight() * highLightedView.getScaleY() < MINIMUM_HEIGHT_BUTTON && detector.getScaleFactor() < 1f) {
                highLightedView.setScaleX(((float) MINIUMUM_WIDTH_BUTTON) / ((float) DEFAULT_WIDTH_BUTTON));
                highLightedView.setScaleY(((float) MINIMUM_HEIGHT_BUTTON) / ((float) DEFAULT_HEIGHT_BUTTON));
                return false;
            }

            scale = scale * detector.getScaleFactor();

            if (highLightedView != null) {
                highLightedView.setScaleX(scale);
                highLightedView.setScaleY(scale);
            } else {
                return false;
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            if (highLightedView == null) {
                return false;
            }
            // Sensors indicators like Gyros are not allowed to resize.
            for (ButtonViewModel buttonViewModel : buttonViewModelList) {
                if (buttonViewModel.id == (Integer) highLightedView.getTag()) {
                    if (buttonViewModel.id == GYRO_X_BUTTON_TYPE
                            || buttonViewModel.id == GYRO_Y_BUTTON_TYPE
                            || buttonViewModel.id == GYRO_Z_BUTTON_TYPE) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

            if (highLightedView != null) {

                //TODO UI 3: Arrow Vector Drawables have huge padding around it. This causing troubles upon enlarging.
                scale = 1f;
                for (ButtonViewModel buttonViewModel : buttonViewModelList) {
                    if (buttonViewModel.id == (Integer) highLightedView.getTag()) {
                        //TODO LOGIC 1: Depending upon view type user can scale in X or Y or both directions.
                        buttonViewModel.width *= highLightedView.getScaleX();
                        buttonViewModel.height *= highLightedView.getScaleY();
                        rootLayout2.removeView(highLightedView);
                        highLightedView = drawNewImage(buttonViewModel);
                        highLightedView.setBackground(mContext.getResources().getDrawable(R.drawable.high_lighted_blue_background));
                        break;
                    }
                }

                //TODO UI 2: Apply lower and upper limits for button height & width.

            }
            super.onScaleEnd(detector);
        }
    }

    /**
     * For buttons OnClickListner just sends the command once. But for sensor drivers,
     * OnClickListner starts or stops sending the current value for every 0.25ms (time delay is variable),
     */

    View.OnClickListener buttonOnClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (ButtonViewModel buttonViewModel : buttonViewModelList) {
                if (buttonViewModel.id == (Integer) v.getTag()) {
                    if(buttonViewModel.command==null || buttonViewModel.command.isEmpty()){
                        //TODO LOGIC 3: SEND COMMAND TO DEVICE SERVER(ARDUINO).
                        Toast.makeText(mContext,"Button type :" +buttonViewModel.buttonType,Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };


    @Override
    public void onStop() {
        super.onStop();
        seekBarThumbX = null;
        seekBarThumbY = null;
        seekBarThumbZ = null;

        //TODO LOGIC 3: move this logic for SAVE button
        if (mEditModeEnable) {

            // Save the ButtonViewModel list in SharedPreference.
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            Gson gson = new Gson();

            String json = gson.toJson(buttonViewModelList);

            editor.putString(USER_VIEWS_LIST_NAME, json);
            editor.apply();

            //TODO LOGIC 3: Sink USER_VIEWS_LIST_NAME with the FireBase DB.
        }
    }

    public static void testFunction(){

    }

}
