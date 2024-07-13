package com.example.android_ex2;


import com.example.android_ex2.Enums.obstacle;

import java.util.Arrays;
import java.util.Random;

public class GameManeger {

    private int rowSize;
    private int colSize;
    private int life;
    private int score;
    private boolean[][] hammerMatrix;
    private boolean[][] coinMatrix;
    private boolean[] dwarfArray;
    private boolean[] heartArray;
    private int currentVisibleDwarf = 2;


    public GameManeger(int life, int rowSize, int colSize, int score) {
        this.life = life;
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.score = score;
        init();
    }


    public boolean[] getHeartArray() {
        return heartArray;
    }

    public boolean[] getDwarfArray() {
        return dwarfArray;
    }

    public int getColSize() {
        return colSize;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean[][] getHammerMatrix() {
        return hammerMatrix;
    }

    public boolean[][] getCoinMatrix() {
        return coinMatrix;
    }

    public int getCurrentVisibleDwarf() {
        return currentVisibleDwarf;
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCurrentVisibleDwarf(int currentVisibleDwarf) {
        this.currentVisibleDwarf = currentVisibleDwarf;
    }

    public void updateMatrix() {
        for (int i = rowSize - 1; i > 0; i--) {
            for (int j = 0; j < colSize; j++) {
                hammerMatrix[i][j] = hammerMatrix[i - 1][j];
                coinMatrix[i][j] = coinMatrix[i - 1][j];
            }
        }
        updateFirstRow();
    }

    public void moveDwarf(int number) {
        int currentPlace = getCurrentVisibleDwarf();
        if (currentPlace + number <= 4 && currentPlace + number >= 0) {
            for (int i = 0; i < dwarfArray.length; i++) {
                if (i == currentVisibleDwarf + number) {
                    dwarfArray[i] = true;
                } else {
                    dwarfArray[i] = false;
                }
            }
            setCurrentVisibleDwarf(currentPlace + number);
        }
    }

    public void updateFirstRow() {
        int selectedCol = randomNumber();
        obstacle selecteObstacle = hammerOrCoinRandom();
        for (int i = 0; i < colSize; i++) {
            if (selectedCol == i) {
                if (selecteObstacle == obstacle.HAMMER) {
                    hammerMatrix[0][i] = true;
                    coinMatrix[0][i] = false;
                } else {
                    coinMatrix[0][i] = true;
                    hammerMatrix[0][i] = false;

                }
            } else {
                hammerMatrix[0][i] = false;
                coinMatrix[0][i] = false;
            }
        }
    }

    public boolean checkCollition() {
        for (int i = 0; i < getColSize(); i++) {
            if (hammerMatrix[getRowSize() - 1][i] && dwarfArray[i]) {
                setLife(Math.max(getLife() - 1, 0));

                return true;
            }
        }
        return false;
    }

    public boolean checkCoinCollition() {
        for (int i = 0; i < getColSize(); i++) {
            if (coinMatrix[getRowSize() - 1][i] && dwarfArray[i]) {
                setScore(getScore() + 10);
                return true;
            }
        }
        return false;
    }

    private void init() {
        hammerMatrix = new boolean[getRowSize()][getColSize()];
        for (int i = 0; i < getRowSize(); i++) {
            for (int j = 0; j < getColSize(); j++) {
                hammerMatrix[i][j] = false;
            }
        }

        coinMatrix = new boolean[getRowSize()][getColSize()];
        for (int i = 0; i < getRowSize(); i++) {
            for (int j = 0; j < getColSize(); j++) {
                coinMatrix[i][j] = false;
            }
        }

        dwarfArray = new boolean[getColSize()];
        for (int i = 0; i < dwarfArray.length; i++) {
            if (i != currentVisibleDwarf) {
                dwarfArray[i] = false;
            } else {
                dwarfArray[i] = true;
            }
        }

        heartArray = new boolean[life];
        Arrays.fill(heartArray, true);
    }

    private int randomNumber() {
        Random rand = new Random();
        return rand.nextInt(5);
    }

    private obstacle hammerOrCoinRandom() {
        Random random = new Random();
        double randomNumber = random.nextDouble();

        if (randomNumber < 0.7) {
            return obstacle.HAMMER;
        }
        return obstacle.COIN;
    }
}
