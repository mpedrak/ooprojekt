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
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;



public class App  extends Application
{

    public GridPane grid = null;

    public void start(Stage primaryStage)
    {
        grid = new GridPane();
        grid.setGridLinesVisible(true);
        Scene scene = new Scene(grid, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

        //renderujStart();
    }
    /*
    public void renderuj(IWorldMap map)
    {

        Vector2d bottomLeft = ((GrassField)map).bottomLeft();
        Vector2d upperRight = ((GrassField)map).upperRight();

        grid.getChildren().clear();
        grid.setGridLinesVisible(true);

        VBox vbox = new VBox();
        Label s = new Label( "\n" + "   " + "y\\x");
        vbox.getChildren().addAll(s);
        vbox.getStyleClass().add("cell");
        grid.add(vbox, 0, 0, 1, 1);
        GridPane.setHalignment(vbox, HPos.CENTER);

        int width = 40;
        int height = 40;

        for (int i = 0; i <= upperRight.x - bottomLeft.x + 1; i++)
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        for (int i = 0; i <= upperRight.y - bottomLeft.y + 1; i++)
            grid.getRowConstraints().add(new RowConstraints(height));

        for (int i = bottomLeft.x ; i <= upperRight.x; ++i)
        {
            vbox = new VBox();
            s = new Label("\n" + "     " + Integer.toString(i));
            vbox.getChildren().addAll(s);
            vbox.getStyleClass().add("cell");
            grid.add(vbox, i - bottomLeft.x + 1, 0, 1, 1);
        }

        int j = 1;
        for (int i = upperRight.y; i >= bottomLeft.y; --i)
        {
            vbox = new VBox();
            s = new Label("\n" + "     " + Integer.toString(i));
            vbox.getChildren().addAll(s);
            vbox.getStyleClass().add("cell");
            grid.add(vbox, 0, j, 1, 1);
            ++j;
        }


        for (int x = bottomLeft.x ; x <= upperRight.x ; ++x)
        {
            j = 1;
            for (int y = upperRight.y; y >= bottomLeft.y; --y)
            {
                Object z = ((GrassField) map).objectAt(new Vector2d(x, y));
                GuiElementBox element = null;
                try
                {
                    element = new GuiElementBox((IMapElement)z);
                }
                catch (FileNotFoundException ex)
                {
                    System.out.println(ex);
                }
                element.vbox.getStyleClass().add("cell");
                grid.add(element.vbox, x - bottomLeft.x + 1, j, 1, 1);
                GridPane.setHalignment(element.vbox, HPos.CENTER);


                ++j;
            }
        }

    }
    */

    private void renderujStart(IWorldMap map, Runnable engine)
    {
        grid.getChildren().clear();
        int width = 40;
        int height = 40;
        for (int i = 0; i <= 4 ; i++)
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        for (int i = 0; i <= 3; i++)
            grid.getRowConstraints().add(new RowConstraints(height));

        Button button = new Button("Start symulacji");
        grid.add(button, 0, 0, 3, 2);

        button.setOnAction(actionEvent ->
        {
            Thread engineThread = new Thread(engine);
            engineThread.start();
        });

    }



}
