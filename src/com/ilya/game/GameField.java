package com.ilya.game;

import static com.ilya.game.Constants.*;

public class GameField {
    //Состояние ячеек
    private int[][] theField;

    public GameField() {
        theField = new int[COUNT_CELLS_X][Constants.COUNT_CELLS_Y];

        for(int i =0; i < theField.length; i++){
            for(int j = 0; j < theField[i].length; j++){
                theField[i][j] = 0;
            }
        }
    }

    public int getState(int x, int y){
        return theField[x][y];
    }

    public void setState(int x, int y, int state){
        theField[x][y] = state;
    }

    public void setColumn(int column, int[] newColumn){
        theField[column] = newColumn;
    }

    public int[] getColumn(int column){
        return theField[column];
    }

    public void setLine(int line, int[] newLine){
        for(int j = 0; j < COUNT_CELLS_X; j++){
            theField[line][j] = newLine[j];
        }
    }

    public int[] getLine(int line){
        int[] ret = new int[COUNT_CELLS_X];

        for (int j = 0; j < COUNT_CELLS_X; j++){
            ret[j] = theField[j][line];
        }

        return ret;
    }
}
