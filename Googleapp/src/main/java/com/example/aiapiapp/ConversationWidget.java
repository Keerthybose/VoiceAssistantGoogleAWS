package com.example.aiapiapp;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class ConversationWidget extends LinearLayout {
    private ViewGroup mainViewGroup;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private Context context;
    public ConversationWidget(Context context) {
        super(context);
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            mainViewGroup = (ViewGroup) layoutInflater.inflate(R.layout.conversation_layout, this, true);
        }
        initializeViews();
    }

    public ConversationWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            mainViewGroup = (ViewGroup) layoutInflater.inflate(R.layout.conversation_layout, this, true);
        }
        initializeViews();
    }

    public ConversationWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            mainViewGroup = (ViewGroup) layoutInflater.inflate(R.layout.conversation_layout, this, true);
        }
        initializeViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ConversationWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            mainViewGroup = (ViewGroup) layoutInflater.inflate(R.layout.conversation_layout, this, true);
        }
        initializeViews();
    }

    private void initializeViews(){
        linearLayout = mainViewGroup.findViewById(R.id.scrollView_internal_layout);
    }

    public void setVoicySpeech(String voicySpeech){
        TextView textView1 = new TextView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.START;
        textView1.setText(voicySpeech);
        textView1.setBackgroundColor(0xFFFFFF); // hex color 0xAARRGGBB
        textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
        layoutParams.setMargins(5, 40, 80, 40);
        textView1.setLayoutParams(layoutParams);
        linearLayout.addView(textView1);
    }

    public void setHumanSpeech(String humanSpeech){
        TextView textView2 = new TextView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.END;
        textView2.setText(humanSpeech);
        textView2.setBackgroundColor(0xFFFFFF); // hex color 0xAARRGGBB
        textView2.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
        layoutParams.setMargins(80, 40, 5, 40);
        textView2.setLayoutParams(layoutParams);
        linearLayout.addView(textView2);
    }

}
