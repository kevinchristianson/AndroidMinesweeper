package com.example.kevinchristianson.minesweeper;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


public class MinesweeperModel {

    private static MinesweeperModel minesweeperModel = null;

    private MinesweeperModel(){}

    public static MinesweeperModel getInstance() {
        if (minesweeperModel == null) {
            minesweeperModel = new MinesweeperModel();
        }
        return minesweeperModel;
    }

    private Tile[][] field;
    private int num = 8; // represents the number of rows, columns and bombs
    private ArrayList<Tile> bombList;

    public int getNum() {
        return num;
    }


    public void setNum(int num) {
        this.num = num;
    }

    public int getNumBombs() {
        return bombList.size();
    }

    public ArrayList<Tile> getBombs() {
        return bombList;
    }



    public void createField() {
        field = new Tile[num][num];
        bombList = new ArrayList<>();
        Random random = new Random();
        ArrayList<Integer> bombPositions = new ArrayList<>(); // list of bomb positions (can be duplicates)
        for (int i = 0; i < num + (num / 2); i++) {
            bombPositions.add(random.nextInt(num * num));
        }
        // initialize field
        for (int x = 0; x < num; x++) {
            for (int y = 0; y < num; y++) {
                if (bombPositions.contains(y * num + x)) {
                    // create bomb
                    field[x][y] = new Tile(x, y, true);
                    bombList.add(field[x][y]);
                }
                else{
                    // create normal tile
                    field[x][y] = new Tile(x, y, false);
                }
            }
        }
        // add neighbor information to tiles and set number values
        for (int x = 0; x < num; x++) {
            for (int y = 0; y < num; y++) {
                int adjacentBombs = 0;
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (0 <= x + i && x + i < num && 0 <= y + j && y + j < num && (i != 0 || j != 0)) {
                            field[x][y].addNeighbor(field[x+i][y+j]);
                            if (field[x+i][y+j].isBomb()) {
                                adjacentBombs++;
                            }
                        }
                    }
                }
                field[x][y].setNumber(adjacentBombs);
            }
        }
    }

    public Tile getTile(int x, int y) {
        return field[x][y];
    }

    public boolean clickTile(int x, int y, boolean flag) {
        Tile tile = field[x][y];
        if (flag) {
            if (tile.isFlagged()) {
                tile.setFlagged(false);
            } else {
                tile.setFlagged(true);
            }
        } else {
            if (tile.isFlagged()) {
                return false;
            }
            if (tile.isBomb()) {
                for (Tile bomb : bombList) {
                    bomb.setClicked(true);
                }
                return true;
            }
            tileCascade(tile);
        }
        return false;
    }

    private void tileCascade(Tile tile) {
        ArrayList<Tile> seen = new ArrayList<>();
        Stack<Tile> stack = new Stack<>();
        stack.push(tile);
        seen.add(tile);
        while (!stack.isEmpty()) {
            tile = stack.pop();
            if (tile.getNumber() == 0) {
                for (Tile neighbor : tile.getNeighbors()) {
                    if (!neighbor.isClicked() && !seen.contains(neighbor)) {
                        stack.push(neighbor);
                        seen.add(neighbor);
                    }
                }
            }
            tile.setClicked(true);
        }
    }
}
