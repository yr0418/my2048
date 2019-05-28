package com.example.my2048;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.my2048.tool.GameView;

import java.util.ArrayList;
import java.util.Map;

public class gameActivity extends AppCompatActivity {
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉顶部标题
        getSupportActionBar().hide();
        //去掉最上面时间、电量等
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.mylinearlaout);

        GameView.getGameView().initGameView();

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        ViewGroup.LayoutParams lp;
        lp= linearLayout.getLayoutParams();
        lp.width=width;
        lp.height=width;
        linearLayout.setLayoutParams(lp);
    }
}
