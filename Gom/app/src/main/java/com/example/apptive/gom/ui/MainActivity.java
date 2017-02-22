package com.example.apptive.gom.ui;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.apptive.gom.R;
import com.example.apptive.gom.model.Field;
import com.example.apptive.gom.model.Generation;
import com.example.apptive.gom.service.SendGenerationRequest;
import com.example.apptive.gom.ui.customview.GameView;
import com.rainy.networkhelper.future.ExecutionFuture;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Field fields[][];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GameView gameView  = (GameView)findViewById(R.id.game);
        Button b = (Button)findViewById(R.id.btn);
        fields = new Field[90][180];
        for(int i=0;i<90;i++)
        {
            for (int j=0;j<180;j++)
            {
                fields[i][j] = new Field(i,j," ");
            }
        }
        gameView.setOnPlayerMoveListener(new GameView.OnPlayerMoveListener() {
            @Override
            public void onPlayerMove(Field field) {
                fields[field.getX()][field.getY()].setM("x");
                gameView.invalidate();
            }
        });
        gameView.init(90,180);
        gameView.setTable(fields);
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        List<Point> points = new ArrayList<Point>();
                        for(int i = 0; i<90 ;i++)
                        {
                            for(int j = 0;j<180;j++)
                            {
                                if(fields[i][j].getM().equals("x"))
                                {
                                  Point p = new Point(i,j);
                                    points.add(p);
                                }
                            }
                        }
                        Generation generation = new Generation(points,90,180);
                        new SendGenerationRequest(generation).getParsedFuture(MainActivity.this).enqueue(new ExecutionFuture.OnSuccessListener<Generation>() {
                            @Override
                            public void onSuccess(Generation result) {
                                fields = new Field[result.getX()][result.getY()];
                                for(int i=0;i<result.getX();i++)
                                {
                                    for(int j=0;j<result.getY();j++)
                                    {
                                        fields[i][j] = new Field(i,j," ");

                                    }
                                }
                                for(int i=0;i<result.getPoints().size();i++)
                                {
                                    fields[result.getPoints().get(i).x][result.getPoints().get(i).y] = new Field(result.getPoints().get(i).x,result.getPoints().get(i).y,"X");
                                }

                                gameView.setTable(fields);
                            }
                        }, new ExecutionFuture.OnErrorListener() {
                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(MainActivity.this, "Hiba történt", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );
    }




}
