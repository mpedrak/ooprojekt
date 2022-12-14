package agh;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;



public class App  extends Application
{

    public GridPane grid = null;
    final int width = 40;
    final int height = 40;
    Vector2d bottomLeft;
    Vector2d upperRight;

    public void start(Stage primaryStage)
    {
        AbstractWorldMap map = new KulaZiemska(10, 10,  10, 0);
        int moveDelay = 1000;
        Runnable engine = new SimulationEngine(map, this, moveDelay, 2, 10,
                -2, 0, 5);

        grid = new GridPane();
        grid.setGridLinesVisible(false);
        bottomLeft = map.getPoczatekMapy();
        upperRight = map.getKraniecMapy();
        int w = (upperRight.x - bottomLeft.x  + 2) * width;
        int h = (upperRight.y - bottomLeft.y  + 2) * height;
        Scene scene = new Scene(grid, w, h);
        primaryStage.setScene(scene);
        primaryStage.show();

        renderujStart(engine);
    }

    public void renderuj(AbstractWorldMap map)
    {
        grid.setGridLinesVisible(false);
        grid.getChildren().clear();
        grid.setGridLinesVisible(true);

        VBox vbox = new VBox();
        Label s = new Label( "\n" + "   " + "y\\x");
        vbox.getChildren().addAll(s);
        grid.add(vbox, 0, 0, 1, 1);
        GridPane.setHalignment(vbox, HPos.CENTER);

        for (int i = 0; i <= upperRight.x - bottomLeft.x + 1; i++)
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        for (int i = 0; i <= upperRight.y - bottomLeft.y + 1; i++)
            grid.getRowConstraints().add(new RowConstraints(height));

        for (int i = bottomLeft.x ; i <= upperRight.x; ++i)
        {
            vbox = new VBox();
            s = new Label("\n" + "     " + Integer.toString(i));
            vbox.getChildren().addAll(s);
            grid.add(vbox, i - bottomLeft.x + 1, 0, 1, 1);
        }

        int j = 1;
        for (int i = upperRight.y; i >= bottomLeft.y; --i)
        {
            vbox = new VBox();
            s = new Label("\n" + "     " + Integer.toString(i));
            vbox.getChildren().addAll(s);
            grid.add(vbox, 0, j, 1, 1);
            ++j;
        }


        for (int x = bottomLeft.x ; x <= upperRight.x ; ++x)
        {
            j = 1;
            for (int y = upperRight.y; y >= bottomLeft.y; --y)
            {
                Object z = map.objectAt(new Vector2d(x, y));
                if (z != null)
                {
                    GuiElementBox element = null;
                    try
                    {
                        element = new GuiElementBox((IMapElement)z);
                    }
                    catch (FileNotFoundException ex)
                    {
                        System.out.println(ex);
                    }
                    grid.add(element.vbox, x - bottomLeft.x + 1, j, 1, 1);
                    GridPane.setHalignment(element.vbox, HPos.CENTER);
                }
                ++j;
            }
        }

    }


    private void renderujStart(Runnable engine)
    {

        for (int i = 0; i <= 4 ; i++)
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        for (int i = 0; i <= 3; i++)
            grid.getRowConstraints().add(new RowConstraints(height));

        Button button = new Button("Start symulacji");
        grid.add(button, 1, 1, 3, 1);

        button.setOnAction(actionEvent ->
        {
            Thread engineThread = new Thread(engine);
            engineThread.start();
        });

    }



}
