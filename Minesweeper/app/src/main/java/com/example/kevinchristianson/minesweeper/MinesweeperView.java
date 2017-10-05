package com.example.kevinchristianson.minesweeper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;


public class MinesweeperView extends View{

    private boolean gameOver;
    private boolean isFlagMode;
    private Bitmap unClicked;
    private Bitmap flag;
    private Bitmap mine;
    private Bitmap mineClicked;
    private Bitmap mineDefused;
    private Bitmap empty;
    private Bitmap one;
    private Bitmap two;
    private Bitmap three;
    private Bitmap four;
    private Bitmap five;
    private Bitmap six;
    private Bitmap seven;
    private Bitmap eight;
    private SparseArray<Bitmap> map;

    public MinesweeperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        unClicked = BitmapFactory.decodeResource(getResources(), R.drawable.tile_unclicked);
        flag = BitmapFactory.decodeResource(getResources(), R.drawable.flag);
        mine = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
        mineClicked = BitmapFactory.decodeResource(getResources(), R.drawable.bomb_clicked);
        mineDefused = BitmapFactory.decodeResource(getResources(), R.drawable.bomb_defused);
        empty = BitmapFactory.decodeResource(getResources(), R.drawable.tile_clicked);
        one = BitmapFactory.decodeResource(getResources(), R.drawable.one);
        two = BitmapFactory.decodeResource(getResources(), R.drawable.two);
        three = BitmapFactory.decodeResource(getResources(), R.drawable.three);
        four = BitmapFactory.decodeResource(getResources(), R.drawable.four);
        five = BitmapFactory.decodeResource(getResources(), R.drawable.five);
        six = BitmapFactory.decodeResource(getResources(), R.drawable.six);
        seven = BitmapFactory.decodeResource(getResources(), R.drawable.seven);
        eight = BitmapFactory.decodeResource(getResources(), R.drawable.eight);

        gameOver = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTiles(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        scaleImages();
    }

    public void scaleImages() {
        // scale images and place in HashMap. This allows number lookup to be fast, while also allowing
        // the original images to be saved, so that image quality is not lost when scaling, as every scale
        // is based on the source image
        int num = MinesweeperModel.getInstance().getNum();
        map = new SparseArray<>();
        map.put(-2, mineClicked.createScaledBitmap(mineClicked, getWidth() / num, getHeight() / num, false));
        map.put(-1, mine.createScaledBitmap(mine, getWidth() / num, getHeight() / num, false));
        map.put(0, empty.createScaledBitmap(empty, getWidth() / num, getHeight() / num, false));
        map.put(1, one.createScaledBitmap(one, getWidth() / num, getHeight() / num, false));
        map.put(2, two.createScaledBitmap(two, getWidth() / num, getHeight() / num, false));
        map.put(3, three.createScaledBitmap(three, getWidth() / num, getHeight() / num, false));
        map.put(4, four.createScaledBitmap(four, getWidth() / num, getHeight() / num, false));
        map.put(5, five.createScaledBitmap(five, getWidth() / num, getHeight() / num, false));
        map.put(6, six.createScaledBitmap(six, getWidth() / num, getHeight() / num, false));
        map.put(7, seven.createScaledBitmap(seven, getWidth() / num, getHeight() / num, false));
        map.put(8, eight.createScaledBitmap(eight, getWidth() / num, getHeight() / num, false));
        map.put(9, flag.createScaledBitmap(flag, getWidth() / num, getHeight() / num, false));
        map.put(10, unClicked.createScaledBitmap(unClicked, getWidth() / num, getHeight() / num, false));
        map.put(11, mineDefused.createScaledBitmap(mineDefused, getWidth() / num, getHeight() / num, false));
    }

    private void drawTiles(Canvas canvas) {
        int num = MinesweeperModel.getInstance().getNum();
        int numLeft = 0;
        for (int x = 0; x < num; x++) {
            for (int y = 0; y < num; y++) {
                Tile tile = MinesweeperModel.getInstance().getTile(x, y);
                int xCord = x * getWidth() / num;
                int yCord = y * getHeight() / num;
                if (tile.isClicked()) {
                    if (tile.isBomb()) {
                        if (tile.equals(lastClicked))
                        {
                            canvas.drawBitmap(map.get(-2), xCord, yCord, null);
                        }
                        else {
                            canvas.drawBitmap(map.get(-1), xCord, yCord, null);
                        }
                    }
                    else{
                        canvas.drawBitmap(map.get(tile.getNumber()), xCord, yCord, null);
                    }
                }
                else if (tile.isFlagged()) {
                    canvas.drawBitmap(map.get(9), xCord, yCord, null);
                    numLeft++;
                }
                else {
                    canvas.drawBitmap(map.get(10), xCord, yCord, null);
                    numLeft++;
                }
            }
        }
        if (numLeft == MinesweeperModel.getInstance().getNumBombs()) {
            // show that the rest of the tiles were bombs by over-writing the un-clicked tiles
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            int scale = getWidth() / num;
            for (Tile bomb : MinesweeperModel.getInstance().getBombs()) {
                if (bomb.isFlagged()) {
                    canvas.drawBitmap(map.get(11), bomb.getX() * scale, bomb.getY() * scale, paint);
                }
            }
            ((MainActivity) getContext()).gameOver(true);
            gameOver = true;
        }
    }

    private Tile lastClicked;

    public void setFlagMode(boolean flagMode) {
        isFlagMode = flagMode;
    }

    public boolean getFlagMode() { return isFlagMode; }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!gameOver)
            {
                int num = MinesweeperModel.getInstance().getNum();
                int tX = (int) (event.getX()) / (getWidth() / num);
                int tY = (int) (event.getY()) / (getHeight() / num);
                // when not debugging, sometimes y position can be too large (bug found but problematic code not found)
                if (tY >= num) { tY = num - 1; }
                if (MinesweeperModel.getInstance().clickTile(tX, tY, isFlagMode)) {
                    ((MainActivity) getContext()).gameOver(false);
                    lastClicked = MinesweeperModel.getInstance().getTile(tX, tY);
                    gameOver = true;
                }
                invalidate();
                return true;
            }
            else {
                ((MainActivity) getContext()).resetGame();
                invalidate();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }
}
