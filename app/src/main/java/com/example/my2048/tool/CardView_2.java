package com.example.my2048.tool;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CardView_2 extends FrameLayout {

    private int num;

    private TextView label;


    public CardView_2(Context context) {
        super(context);
        label=new TextView(getContext());
        label.setTextSize(32);
        label.setGravity(Gravity.CENTER);
        label.setBackgroundColor(0x33ffffff);

        LayoutParams layoutParams=new LayoutParams(-1,-1);//填充父类容器
        layoutParams.setMargins(20,20,0,0);
        addView(label,layoutParams);
        setNum(0);
    }

    public int getNum() {
        return num;
    }

    //卡片显示数字
    public void setNum(int num) {
        this.num = num;
        label.setText(num+"");//label只显示字符串，添加一个空格将数字装换为字符串
    }

    //判断两张卡片数字是否相等
    public boolean equals(CardView_2 card) {
        return getNum()==card.getNum();
    }
}
