package com.example.kevinchristianson.minesweeper;

import java.util.ArrayList;


public class Tile {

    private int x;
    private int y;
    private boolean isBomb;
    private int number;
    private boolean isClicked;
    private boolean isFlagged;
    private ArrayList<Tile> neighbors;

    public Tile(int x, int y, boolean isBomb) {
        this.x = x;
        this.y = y;
        this.isBomb = isBomb;
        isClicked = false;
        neighbors = new ArrayList<>();
    }

    public ArrayList<Tile> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Tile neighbor) {
        neighbors.add(neighbor);
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
