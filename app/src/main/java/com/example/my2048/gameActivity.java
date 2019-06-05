package com.example.my2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my2048.tool.GameView;
import com.example.my2048.tool.MyDBHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class gameActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private int score = 0;
    private TextView tvScore;
    private TextView tvBestScore;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉顶部标题
        getSupportActionBar().hide();
        //去掉最上面时间、电量等
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        linearLayout = (LinearLayout)findViewById(R.id.mylinearlaout);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvBestScore = (TextView) findViewById(R.id.bestScore);
        myDBHelper = new MyDBHelper(this);
        db = myDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + myDBHelper.getTableName(), null);
        //开始游戏
        GameView.getGameView().initGameView();
        //设置表格的大小
        ImageButton resbtn = (ImageButton) findViewById(R.id.imageButton3);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        ViewGroup.LayoutParams lp;
        lp= linearLayout.getLayoutParams();
        lp.width=width;
        lp.height=width;
        linearLayout.setLayoutParams(lp);
        if(cursor.getCount() == 0){
            db = myDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("insert into " + MyDBHelper.TABLE_NAME + "(Id, Score)values(1,0)");
            db.execSQL("insert into " + MyDBHelper.TABLE_NAME + "(Id, Score)values(2,0)");
            db.setTransactionSuccessful();
            db.close();
        }

        //重新开始游戏
        resbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(getgameActivity())
                        .setTitle("重启游戏")
                        .setMessage("确定重启游戏吗？")
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GameView.getGameView().startGame();
                                clearScore();
                            }
                        })
                        .create().show();
            }
        });
    }

    private gameActivity getgameActivity(){
        return this;
    }

    //显示当前分数
    public void showScore() {
        tvScore.setText(score + "");
    }
    //显示最高分数
    public void showBestScore(int s) {
        tvBestScore.setText(s + "");
    }
    //分数清0
    public void clearScore() {
        score = 0;
        showScore();
    }
    //更新增加当前分数
    public void addScore(int s) {
        score += s;
        showScore();
        int maxScore = Math.max(score, getBestScore());
        saveBestScore(maxScore);
        showBestScore(maxScore);
    }
    //获取最高分
    public int getBestScore(){
        int bestScore=100;
        db = myDBHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + MyDBHelper.TABLE_NAME + " where Id = 2", null);
        while (cursor.moveToLast()){
            bestScore=cursor.getInt(1);
        }
        return bestScore;
    }
    //保存最高分
    public void saveBestScore(int s){

    }
    public void initScore(){//初始化分数
        score = 0;
        showScore();
        int bestScore=getBestScore();
        showBestScore(bestScore);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
