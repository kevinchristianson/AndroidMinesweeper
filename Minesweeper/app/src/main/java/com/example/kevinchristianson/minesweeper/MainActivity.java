package com.example.kevinchristianson.minesweeper;

import android.graphics.Point;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import 	android.support.design.widget.Snackbar;

public class MainActivity extends AppCompatActivity {

    private View screen;
    private ToggleButton toggle;
    private Button clear;
    private MinesweeperView minesweeperView;
    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize view
        screen = findViewById(R.id.screen);
        minesweeperView = (MinesweeperView) findViewById(R.id.minesweeperView);
        toggle = (ToggleButton) findViewById(R.id.toggle);
      //  clear = (Button) findViewById(R.id.clear);
        TextView sizeText = (TextView) findViewById(R.id.size_text);
        Button plus = (Button) findViewById(R.id.plus);
        final TextView gridSize = (TextView) findViewById(R.id.grid_size);
        Button minus = (Button) findViewById(R.id.minus);
        TextView timeText = (TextView) findViewById(R.id.time_text);
        chronometer = (Chronometer) findViewById(R.id.chronometer);

        //set onclick listeners
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newNum = Integer.parseInt(gridSize.getText().toString()) + 1;
                if (newNum <= 15) {
                    gridSize.setText(Integer.toString(newNum));
                    MinesweeperModel.getInstance().setNum(newNum);
                    minesweeperView.scaleImages();
                    resetGame();
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newNum = Integer.parseInt(gridSize.getText().toString()) - 1;
                if (newNum >= 5)
                {
                    gridSize.setText(Integer.toString(newNum));
                    MinesweeperModel.getInstance().setNum(newNum);
                    minesweeperView.scaleImages();
                    resetGame();
                }
            }
        });
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (minesweeperView.getFlagMode()) {
                    minesweeperView.setFlagMode(false);
                    toggle.setBackgroundResource(R.drawable.flag);
                }
                else {
                    minesweeperView.setFlagMode(true);
                    toggle.setBackgroundResource(R.drawable.flag_clicked);
                }
            }
        });
    /*    clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        }); */


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double xScale = size.x / 720.0; // 480 = display width used in activity_main.xml file design
        minesweeperView.setLayoutParams(new LinearLayout.LayoutParams(size.x - (int)(50 * xScale), size.x - (int)(50 * xScale)));

        //set up game
        MinesweeperModel.getInstance().createField();
        minesweeperView.setFlagMode(false);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public void resetGame() {
        MinesweeperModel.getInstance().createField();
        minesweeperView.setFlagMode(false);
        minesweeperView.setGameOver(false);
        toggle.setChecked(false);
        toggle.setBackgroundResource(R.drawable.flag);
        minesweeperView.invalidate();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public void gameOver(boolean won) {
        if (won) {
            Snackbar.make(screen, "Congratulations, you won the game!", Snackbar.LENGTH_LONG).setAction("New Game", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetGame();
                }
            }).show();
        }
        else{
            Snackbar.make(screen, "BOOM! Game Over!", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetGame();
                }
            }).show();
        }
        chronometer.stop();
    }
}
