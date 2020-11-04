package com.example.mypc.uco;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class CommonUtils {
    static String IP = "https://www.ucosocietychennai.in";

    public String StringToRound(String str_value){
        double new_value;
        new_value=Double.parseDouble(str_value);
        return Long.toString(Math.round(new_value));
    }

    public static TableRow createTableRow(Context context, int textColor, int backgroundColor, JSONObject jsonObject, boolean isHeader, int orientation){
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        tableRow.setPadding(1, 1, 1, 1);

        try {
            Iterator<String> keys = jsonObject.keys();

            while(keys.hasNext()) {
                String key = keys.next();

                if(jsonObject.get(key).toString().isEmpty()) continue;

                if(!isHeader) {
                    if (keys.hasNext()) {
                        tableRow.addView(createTextView(context, textColor, backgroundColor, jsonObject.get(key).toString(), true, orientation));
                    } else {
                        tableRow.addView(createTextView(context, textColor, backgroundColor, jsonObject.get(key).toString(), false, orientation));
                    }
                }else {
                    if (keys.hasNext()) {
                        tableRow.addView(createTextView(context, textColor, backgroundColor, jsonObject.get(key).toString(), true, orientation));
                    } else {
                        tableRow.addView(createTextView(context, textColor, backgroundColor, jsonObject.get(key).toString(), false, orientation));
                    }
                }

            }

        }catch(JSONException e){
            e.printStackTrace();
        }

        return tableRow;
    }

    public static TextView createTextView(Context context, int textColor, int backgroundColor, String text, boolean isMarginRight, int orientation) {
        TextView textView = new TextView(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0.2f);
        if (isMarginRight) params.setMargins(0, 0, 5, 5);
        else params.setMargins(0, 0, 0, 5);
        textView.setLayoutParams(params);
        textView.setBackgroundColor(backgroundColor);
        textView.setGravity(orientation);
        textView.setPadding(5, 5, 5, 5);
        textView.setText(text);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
        textView.setTextSize(8);
        textView.setTextColor(textColor);
        return textView;
    }
}