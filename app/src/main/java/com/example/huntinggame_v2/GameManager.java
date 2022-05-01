package com.example.huntinggame_v2;

import java.util.Random;

public class GameManager {
    public static final int COLS=5;
    public static final int ROWS=5;
    public enum DIRECTION {UP,RIGHT,LEFT,DOWN}
    private DIRECTION direction= DIRECTION.DOWN;
    private DIRECTION directionRandom= DIRECTION.values()[new Random().nextInt(4)];
    //hunter
    private int rowHunter;
    private int colHunter;
    //hunted
    private int rowHunted;
    private int colHunted;
    //item start point
    private int rowItem = 1;
    private int colItem = 1;

    private int lives = 3;
    private boolean gameOver =false;

    public GameManager() {
        startPosition();
    }

    public void moveCharacter(DIRECTION direction){
        this.direction = direction;
        this.directionRandom= DIRECTION.values()[new Random().nextInt(4)];
        switch (direction){
            case UP:
                if(rowHunted >0){
                    rowHunted--;
                }
                break;
            case RIGHT:
                if(colHunted <COLS-1){
                    colHunted++;
                }
                break;
            case LEFT:
                if(colHunted >0){
                    colHunted--;
                }
                break;
            case DOWN:
                if(rowHunted <ROWS-1){
                    rowHunted++;
                }
                break;
        }

        switch (directionRandom){
            case UP:
                if(rowHunter >0){
                    rowHunter--;
                }
                break;
            case RIGHT:
                if(colHunter <COLS-1){
                    colHunter++;
                }
                break;
            case LEFT:
                if(colHunter >0){
                    colHunter--;
                }
                break;
            case DOWN:
                if(rowHunter <ROWS-1){
                    rowHunter++;
                }
                break;
        }
    }

    public boolean isCatch(){
        if (this.rowHunted == this.rowHunter && this.colHunted == this.colHunter){
            if(lives>0){
                lives--;
                if (lives==0){
                    gameOver = true;
                }
            }
            return true;
        }
        return false;
    }

    public boolean extraPoints(){
        if (this.rowHunted == this.rowItem && this.colHunted == this.colItem){
           return true;
        }
        return false;
    }

    public void changeItemLoc() {
        rowItem = new Random().nextInt(ROWS-1);
        colItem = new Random().nextInt(COLS-1);
    }

    public void startPosition() {
        rowHunter =3;
        colHunter =2;
        rowHunted =0;
        colHunted =2;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public GameManager setDirection(DIRECTION direction) {
        this.direction = direction;
        return this;
    }

    public int getRowHunter() {
        return rowHunter;
    }

    public GameManager setRowHunter(int rowHunter) {
        this.rowHunter = rowHunter;
        return this;
    }

    public int getColHunter() {
        return colHunter;
    }

    public GameManager setColHunter(int colHunter) {
        this.colHunter = colHunter;
        return this;
    }

    public int getRowHunted() {
        return rowHunted;
    }

    public GameManager setRowHunted(int rowHunted) {
        this.rowHunted = rowHunted;
        return this;
    }

    public int getColHunted() {
        return colHunted;
    }

    public GameManager setColHunted(int colHunted) {
        this.colHunted = colHunted;
        return this;
    }

    public int getRowItem() {
        return rowItem;
    }

    public GameManager setRowItem(int rowItem) {
        this.rowItem = rowItem;
        return this;
    }

    public int getColItem() {
        return colItem;
    }

    public GameManager setColItem(int colItem) {
        this.colItem = colItem;
        return this;
    }

    public int getLives() {
        return lives;
    }

    public GameManager setLives(int lives) {
        this.lives = lives;
        return this;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public GameManager setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        return this;
    }
}