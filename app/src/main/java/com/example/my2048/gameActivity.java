package com.example.my2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my2048.tool.GameView;

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
        StringBuilder stringBuilder=new StringBuilder("");
        //获取文件在内存卡中files目录下的路径
        File file= getApplicationContext().getFilesDir();
        String filename=file.getAbsolutePath()+File.separator+"ScoreFile/bestScore.txt";
        String score="0";
        //打开文件输出流
        try {
            FileInputStream inputStream=new FileInputStream(filename);
            byte[] buffer=new byte[1024];
            int len=inputStream.read(buffer);
            while (len>0){
                stringBuilder.append(new String(buffer,0,len));
                score=stringBuilder.toString();
                len=inputStream.read(buffer);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int bestScore=Integer.parseInt(score);
        return bestScore;
    }
    //保存最高分
    public void saveBestScore(int s){
        File file=getApplicationContext().getFilesDir();
        String filename=file.getAbsolutePath()+File.separator+"ScoreFile/bestScore.txtbestScore.txt";
        try {
            FileOutputStream outputStream=new FileOutputStream(filename);
            outputStream.write(s);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
