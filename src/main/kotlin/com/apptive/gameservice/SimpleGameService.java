package com.apptive.gameservice;

import com.apptive.Model.Generation;
import com.apptive.gameservice.GameService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apptive on 2017. 02. 02..
 */
@Service
@ComponentScan
public class SimpleGameService implements GameService{

    char[][] instance;
    List<Point> babies;
    List<Point> deadCells;

    public SimpleGameService() {
    }


    public char[][] getNextGeneration(char[][] firstGenerations) throws IllegalArgumentException {

        //TODO some stricter validation would be nice
        if (firstGenerations.length > 90 || firstGenerations[0].length > 180) {
            throw new IllegalArgumentException();
        }
        instance = firstGenerations;
        babies = new ArrayList<>();


        for (int i = 0; i < firstGenerations.length; i++) {
            for (int j = 0; j < firstGenerations[i].length; j++) {
                if(getNeighourCount(i,j) < 2 || getNeighourCount(i,j)>3)
                {
                    deadCells.add(new Point(i,j));
                }
                else
                {
                    babies.add(new Point(i,j));
                }

            }
        }

        for(int i=0;i<babies.size();i++)
        {
            int x = babies.get(i).x;
            int y = babies.get(i).y;
            firstGenerations[x][y] = 'x';
        }
        for(int i=0;i<deadCells.size();i++)
        {
            int x = babies.get(i).x;
            int y = babies.get(i).y;
            firstGenerations[x][y]=' ';
        }

        cleanUp();

        return firstGenerations;
    }


    private void cleanUp() {
        babies.clear();
        deadCells.clear();
    }

    private int getNeighourCount(int i, int j) {
        int neighbors = 0;

        if ((i > 0 && j > 0) && (i < instance.length && j < instance[i].length)) { //inside the square
            for (int k = i - 1; k < i + 1; k++) {
                for (int l = j - 1; l < j + 1; l++) {
                    if (instance[k][l] == 'x') {
                        neighbors++;
                    }
                }
            }
        } else if (i == 0 && j > 0 && j < instance[i].length) { // first line without corners
            for (int k = i; k < i + 1; k++) {
                for (int l = j - 1; l < j + 1; l++) {
                    if (instance[k][l] == 'x') {
                        neighbors++;
                    }
                }
            }

        } else if (i == instance.length && j > 0 && j < instance[i].length) { // last line without corners
            for (int k = i - 1; k < i; k++) {
                for (int l = j - 1; l < j + 1; l++) {
                    if (instance[k][l] == 'x') {
                        neighbors++;
                    }
                }
            }
        } else if (j == 0 && i > 0 && i < instance.length) { // first column without corners
            for (int k = i - 1; k < i + 1; k++) {
                for (int l = j; l < j + 1; l++) {
                    if (instance[k][l] == 'x') {
                        neighbors++;
                    }
                }
            }


        } else if (j == instance[i].length && i > 0 && i < instance.length) { // last column without corners
            for (int k = i - 1; k < i + 1; k++) {
                for (int l = j - 1; l < j; l++) {
                    if (instance[k][l] == 'x') {
                        neighbors++;
                    }
                }
            }

        }


        if (i == 0 && j == 0) {     //left&up corner
            for (int k = i; k < i + 1; k++) {
                for (int l = j; l < j + 1; l++) {
                    if (instance[k][l] == 'x') {
                        neighbors++;
                    }
                }
            }
        }

        if (i == 0 && j == instance[i].length) { //right&up corner
            for (int k = i; k < i + 1; k++) {
                for (int l = j - 1; l < j; l++) {
                    if (instance[k][l] == 'x') {
                        neighbors++;
                    }
                }
            }
        }

        if (i == instance.length && j == 0) { //down&left corner
            for (int k = i - 1; k < i; k++) {
                for (int l = j; l < j + 1; l++) {
                    if (instance[k][l] == 'x') {
                        neighbors++;
                    }
                }
            }
        }

        if (i == instance.length && j == instance[i].length) { //right&down corner
            for (int k = i - 1; k < i; k++) {
                for (int l = j - 1; l < j; l++) {
                    if (instance[k][l] == 'x') {
                        neighbors++;
                    }
                }
            }
        }

        return neighbors;


    }

    @Override
    public Generation getNextGeneration(Generation generation) throws IllegalArgumentException {

        char[][] a = new char[generation.getX()][generation.getY()];
        char[][] b = new char[generation.getX()][generation.getY()];

        List<Point> cells = generation.getCells();
        List<Point> newCells = new ArrayList<>();


        for (int i =0; i <cells.size() ; i++) {     // TODO refactor(getNextGeneration should  handle these for cycles
            a[cells.get(i).x][cells.get(i).y] = 'x';
        }
        for(int i =0;i < generation.getX() ;i++)
        {
            for(int j=0;j<generation.getY();j++)
            {
                if(a[i][j] != 'x')
                {
                    a[i][j] = ' ';
                }
            }
        }
        b = getNextGeneration(a);
        for(int i =0;i < generation.getX() ;i++)
        {
            for(int j=0;j<generation.getY();j++)
            {
                if(b[i][j] == 'x')
                {
                    newCells.add(new Point(i,j));
                }
            }
        }
        return new Generation(newCells,generation.getX(),generation.getY());
    }

}
