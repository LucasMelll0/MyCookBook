package com.example.mycookbook.customViews;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.mycookbook.R;

public class TextGradient extends androidx.appcompat.widget.AppCompatTextView {
    public TextGradient(Context context) {
        super(context);
    }
    public TextGradient(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextGradient(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        Shader shader = new LinearGradient(10,0,0, super.getTextSize(), getResources().
                getColor(R.color.yellow),getResources().getColor(R.color.lightpink), Shader.TileMode.MIRROR);
        super.getPaint().setShader(shader);
        super.setText(text, type);
    }
}
