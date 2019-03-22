package com.msht.minshengbao.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;


import com.msht.minshengbao.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/12/25 
 */
public class TimePickerUiUtil {

    public static void setTimePickerTextColour(TimePicker timePicker, Context context) {
        Resources system = Resources.getSystem();
        int hourNumberPickerId = system.getIdentifier("hour", "id", "android");
        int minuteNumberPickerId = system.getIdentifier("minute", "id", "android");
        int ampmNumberPickerId = system.getIdentifier("amPm", "id", "android");

        NumberPicker hourNumberPicker = (NumberPicker) timePicker.findViewById(hourNumberPickerId);
        NumberPicker minuteNumberPicker = (NumberPicker) timePicker.findViewById(minuteNumberPickerId);
        NumberPicker ampmNumberPicker = (NumberPicker) timePicker.findViewById(ampmNumberPickerId);

        set_numberpicker_text_colour(hourNumberPicker,context);
        set_numberpicker_text_colour(minuteNumberPicker,context);
        set_numberpicker_text_colour(ampmNumberPicker,context);

        setNumberPickerDividerColor(hourNumberPicker, Color.rgb(255,255,255));
        setNumberPickerDividerColor(minuteNumberPicker,Color.rgb(255,255,255));
        setNumberPickerDividerColor(ampmNumberPicker,Color.rgb(255,255,255));

        setPickerSize(hourNumberPicker,70,context);
        setPickerSize(minuteNumberPicker,70,context);
        setPickerSize(ampmNumberPicker,70,context);
    }

    public static void  setTimePickerValue(TimePicker timePicker, Context context,int hourMin,int hourMax,int minuteMin,int minuteMax){
        Resources system = Resources.getSystem();
        int hourNumberPickerId = system.getIdentifier("hour", "id", "android");
        int minuteNumberPickerId = system.getIdentifier("minute", "id", "android");
        int ampmNumberPickerId = system.getIdentifier("amPm", "id", "android");

        NumberPicker hourNumberPicker = (NumberPicker) timePicker.findViewById(hourNumberPickerId);
        NumberPicker minuteNumberPicker = (NumberPicker) timePicker.findViewById(minuteNumberPickerId);
        NumberPicker ampmNumberPicker = (NumberPicker) timePicker.findViewById(ampmNumberPickerId);

        hourNumberPicker.setMinValue(hourMin);   //设置最小hour
        hourNumberPicker.setMaxValue(hourMax);  //设置最大hour
        minuteNumberPicker .setMinValue(minuteMin);  //设置最小minute
        minuteNumberPicker .setMaxValue(minuteMax);  //设置最大minute
    }
    public static void  setDatePickerValue(DatePicker datePicker, Context context,int  yearMax, int monthMin, int monthMax, int dayMin, int dayMax){
        Resources system = Resources.getSystem();
        int yearNumberPickerId = system.getIdentifier("year", "id", "android");
        int monthNumberPickerId = system.getIdentifier("month", "id", "android");
        int dayNumberPickerId = system.getIdentifier("day", "id", "android");

        NumberPicker yearNumberPicker = (NumberPicker) datePicker.findViewById(yearNumberPickerId);
        NumberPicker monthNumberPicker = (NumberPicker) datePicker.findViewById(monthNumberPickerId);
        NumberPicker dayNumberPicker = (NumberPicker) datePicker.findViewById(dayNumberPickerId);
        //最大年份
        yearNumberPicker.setMaxValue(yearMax);
        yearNumberPicker.setVisibility(View.GONE);
        //设置最小month
      //  monthNumberPicker .setMinValue(monthMin);
        monthNumberPicker.setVisibility(View.GONE);
        //设置最大month
        monthNumberPicker .setMaxValue(monthMax);
        //设置最小day
        dayNumberPicker.setMinValue(dayMin);
        //设置最大day
        dayNumberPicker.setMaxValue(dayMax);
    }


    private static void set_numberpicker_text_colour(NumberPicker number_picker, Context context) {
        final int count = number_picker.getChildCount();
        //这里就是要设置的颜色，修改一下作为参数传入会更好
        final int color = ContextCompat.getColor(context, R.color.white);

        for (int i = 0; i < count; i++) {
            View child = number_picker.getChildAt(i);

            try {
                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);
                ((Paint) wheelpaint_field.get(number_picker)).setColor(color);
                ((EditText) child).setTextColor(color);
                number_picker.invalidate();
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {

            }
        }
    }

    public static void setNumberPickerDividerColor(NumberPicker numberPicker, int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field selectionDividerField : pickerFields) {
            if (selectionDividerField.getName().equals("mSelectionDivider")) {
                selectionDividerField.setAccessible(true);
                try {
                    selectionDividerField.set(numberPicker, new ColorDrawable(color));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    //此方法将dp值转换为px值，以保证适配不同分辨率机型
    private static int dp2px(Context context, float dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    //这个方法是改变NumberPicker大小的方法，传入的参数为要修                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  改的NumberPicker和NumberPicker的宽度值
    private static void setPickerSize(NumberPicker np, int widthDpValue,Context context) {
        int widthPxValue = dp2px(context, widthDpValue);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPxValue, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);//这儿参数可根据需要进行更改
        np.setLayoutParams(params);
    }

    private static List<NumberPicker> findNumberPicker(ViewGroup viewGroup)
    {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;

        if (null != viewGroup)
        {
            for (int i = 0; i < viewGroup.getChildCount(); i++)
            {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker)
                {
                    npList.add((NumberPicker)child);
                }
                else if (child instanceof LinearLayout)
                {
                    List<NumberPicker> result = findNumberPicker((ViewGroup)child);
                    if (result.size() > 0)
                    {
                        return result;
                    }
                }
            }
        }

        return npList;
    }

    private static EditText findEditText(NumberPicker np)
    {
        if (null != np)
        {
            for (int i = 0; i < np.getChildCount(); i++)
            {
                View child = np.getChildAt(i);

                if (child instanceof EditText)
                {
                    return (EditText)child;
                }
            }
        }

        return null;
    }

    public static void setNumberPickerTextSize(ViewGroup viewGroup)
    {
        List<NumberPicker> npList = findNumberPicker(viewGroup);
        if (null != npList)
        {
            for (NumberPicker np : npList)
            {
                EditText et = findEditText(np);
                et.setFocusable(false);
                et.setGravity(Gravity.CENTER);
                et.setTextSize(30);

            }
        }
    }
}
