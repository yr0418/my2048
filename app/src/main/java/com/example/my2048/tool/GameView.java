package com.example.my2048.tool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;

import com.example.my2048.gameActivity;

import java.util.ArrayList;
import java.util.List;

public class GameView extends GridLayout {

//    private gameActivity my2048;
    private static GameView gview;
    private float startX, startY, endX, endY, offX, offY;
    private int row = 4, colunm = 4;// 行row对应y，列colunm对应x,默认开始都为4
    private CardView[][] cardsMap = new CardView[10][10];// 用一个二维数组来存
    private List<Point> emptyPoints = new ArrayList<Point>();// 链表方便增加删除
    // 在xml中能够访问则要添加构造方法
    // 以防万一三个构造方法都要写:对应参分别为上下文，属性，样式
    public GameView(Context context) {
        super(context);
        //initGameView();
        gview = this;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initGameView();
        gview = this;
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        initGameView();
        gview = this;
    }

    public static GameView getGameView() {
        return gview;
    }

    // 由于手机可能不同，我们需要动态地获取卡片的宽高，所以要重写下面这个方法获取当前布局的宽高，
    // 为了让手机不会因倒过来改变宽高，要去mainifest里配置
    // 只会在手机里第一次运行的时候执行，之后不会改变

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    public void startGame() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
    }

    private void addCards() {
        //获取屏幕宽度
        DisplayMetrics displayMetrics;
        displayMetrics = getResources().getDisplayMetrics();
        int width=displayMetrics.widthPixels;
        int cardWidth=(width-20)/4;
        CardView c;
        //cw = cardWidth;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new CardView(getContext());
                // 先都初始画0号图片
                c.setNum(0);
                addView(c,cardWidth,cardWidth);
                // 把所有的卡片都记录下来
                cardsMap[x][y] = c;
            }
        }
    }

    // 添加随机数的时候要先遍历
    private void addRandomNum() {
        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));// 把空位给emptypoints链表
                }
            }
        }
        // 随机把emptyPoints中的一个赋值，生成2的概率为9,4为1
        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        // 2号图片和4号图片
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    public void initGameView() {
        setColumnCount(4);// 设置表格为4列
        addCards();// 把参数传过去
        startGame();
        setBackgroundColor(0xffccccff);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();// 获取触屏的动作
                switch (action) {
                    // 按下获取起始点
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    // 松开获取终止点，通过比较位移来判断滑动方向
                    // 要处理一下滑动偏的，看offx和offy哪个绝对值大就按照哪个来
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        offX = startX - endX;
                        offY = startY - endY;
                        //判断 offx 与 offy 的绝对值大小
                        if (Math.abs(offX) >= Math.abs(offY)) {
                            if (offX >= 5){
                                moveLeft();
                                System.out.println("left");
                            }
                            else if (offX < -5) {
                                moveRight();
                                System.out.println("right");
                            }
                        } else {
                            if (offY >= 5) {
                                moveUp();
                                System.out.println("up");
                            }
                            else if (offY < -5) {
                                moveDown();
                                System.out.println("down");
                            }
                        }
                        break;
                }
                // !!!要改为true，否则ACTION_UP不会执行
                return true;
            }
        });
    }

    private void moveLeft() {
        boolean merge = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                // 遍历当前位置的右边，如果有数字，如果当前位置没有数字，则合并到当前位置
                for (int x1 = x + 1; x1 < 4; x1++) {
                    // 每个右边的位置只判断执行一次
                    if (cardsMap[x1][y].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x--;// 填补空位后，还要再次判断有没相同的可以合并的
                            merge = true;
                            break;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                           // my2048.addScore(cardsMap[x][y].getNum());
                            break;
                        }
                        break;
                    }
                }
            }
        }
        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void moveRight() {
        boolean merge = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 4 - 1; x >= 0; x--) {
                // 遍历当前位置的右边，如果有数字，如果当前位置没有数字，则合并到当前位置
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    // 每个右边的位置只判断执行一次
                    if (cardsMap[x1][y].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x++;// 填补空位后，还要再次判断有没相同的可以合并的
                            merge = true;
                            break;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            //my2048.addScore(cardsMap[x][y].getNum());
                            merge = true;
                            break;
                        }
                        break;
                    }
                }
            }
        }
        if (merge) {
            addRandomNum();
            checkComplete();
        }

    }

    private void moveUp() {
        boolean merge = false;
        for (int x = 0; x < colunm; x++) {
            for (int y = 0; y < row; y++) {
                // 遍历当前位置的右边，如果有数字，如果当前位置没有数字，则合并到当前位置
                for (int y1 = y + 1; y1 < row; y1++) {
                    // 每个右边的位置只判断执行一次
                    if (cardsMap[x][y1].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y--;// 填补空位后，还要再次判断有没相同的可以合并的
                            merge = true;
                            break;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                           // my2048.addScore(cardsMap[x][y].getNum());
                            merge = true;
                            break;
                        }
                        break;
                    }
                }
            }
        }
        if (merge) {
            addRandomNum();
            checkComplete();
        }

    }

    private void moveDown() {
        boolean merge = false;
        for (int x = 0; x < colunm; x++) {
            for (int y = row - 1; y >= 0; y--) {
                // 遍历当前位置的右边，如果有数字，如果当前位置没有数字，则合并到当前位置
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    // 每个右边的位置只判断执行一次
                    if (cardsMap[x][y1].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y++;// 填补空位后，还要再次判断有没相同的可以合并的
                            merge = true;
                            break;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            //my2048.addScore(cardsMap[x][y].getNum());
                            merge = true;
                            break;
                        }
                        break;
                    }
                }
            }
        }if (merge) {
            addRandomNum();
            checkComplete();
        }

    }

     //判断结束
    private void checkComplete() {

        boolean complete = true;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() == 4096)
                    new AlertDialog.Builder(getContext())
                            .setTitle("你好")
                            .setMessage("游戏胜利")
                            .setPositiveButton("重来",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            startGame();
                                        }
                                    }).show();
            }
        }

        ALL: for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                // 如果还有空位，或者四个方向上还有相同的
                if (cardsMap[x][y].getNum() == 0
                        || (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y]))
                        || (x < 3 && cardsMap[x][y].equals(cardsMap[x + 1][y]))
                        || (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1]))
                        || (y < 3 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) {

                    complete = false;
                    break ALL;// 如果出现这种情况，跳出双重循环，只写一个break只能跳出当前循环
                }
            }
        }
        if (complete) {
            new AlertDialog.Builder(getContext())
                    .setTitle("你好")
                    .setMessage("游戏结束")
                    .setPositiveButton("重来",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    startGame();
                                }
                            }).show();
        }
    }
}
