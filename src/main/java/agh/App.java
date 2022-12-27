package agh;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;



public class App  extends Application
{
    final int width = 25;
    final int height = 25;
    Vector2d bottomLeft;
    Vector2d upperRight;
    public void start(Stage primaryStage)
    {
        renderujStart(primaryStage);
    }

    public void renderuj(AbstractWorldMap map, GridPane grid, SimulationEngine engine, Animal zwierzeDoSledzenia)
    {
        grid.setGridLinesVisible(false);
        grid.getChildren().clear();
        grid.setGridLinesVisible(true);

        bottomLeft = map.getPoczatekMapy();
        upperRight = map.getKraniecMapy();

        Rectangle r = new Rectangle(width * Math.max(30, upperRight.x - bottomLeft.x  + 2) + 5, height * 3 + 2);
        r.setFill(Color.rgb(244, 244, 244,1));
        grid.add(r, 0, 0, 1, 1);

        Rectangle r2 = new Rectangle(width * (30 - upperRight.x - bottomLeft.x  + 2), height * (upperRight.y - bottomLeft.y + 3));
        r2.setFill(Color.rgb(244, 244, 244,1));
        grid.add(r2,  upperRight.x + 2 , (int)((upperRight.y - bottomLeft.y) / 2) + 3, 1, 1);

        Button button = new Button("Pauza ");
        grid.add(button, 0, 0, 5, 1);

        if(zwierzeDoSledzenia != null)
        {
            Label label = new Label(zwierzeDoSledzenia.toString());
            grid.add(label, 0, 1, 29, 1);
        }

        button.setOnAction(actionEvent ->
        {
            engine.pauza();
        });

        Button button2 = new Button("Zatrzymaj sledzenie");
        grid.add(button2, 3, 0, 5, 1);

        button2.setOnAction(actionEvent ->
        {
            engine.zmienZwierzeDoSledzenia(null);
        });

        VBox vbox = new VBox();
        Label s = new Label( "y\\x");
        vbox.getChildren().addAll(s);
        vbox.setAlignment(Pos.CENTER);
        grid.add(vbox, 0, 2, 1, 1);
        GridPane.setHalignment(vbox, HPos.CENTER);

        for (int i = bottomLeft.x ; i <= upperRight.x; ++i)
        {
            vbox = new VBox();
            s = new Label(Integer.toString(i));
            vbox.getChildren().addAll(s);
            vbox.setAlignment(Pos.CENTER);
            grid.add(vbox, i - bottomLeft.x + 1, 2, 1, 1);
        }

        int j = 1;
        for (int i = upperRight.y; i >= bottomLeft.y; --i)
        {
            vbox = new VBox();
            s = new Label(Integer.toString(i));
            vbox.getChildren().addAll(s);
            vbox.setAlignment(Pos.CENTER);
            grid.add(vbox, 0, j + 2, 1, 1);
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
                    if (z instanceof Animal)
                    {
                        element.vbox.setOnMouseClicked(new EventHandler<Event>() {
                            @Override
                            public void handle(Event event)
                            {
                                Rectangle r = new Rectangle(width * Math.max(30, upperRight.x - bottomLeft.x  + 2) + 5, height);
                                r.setFill(Color.rgb(244, 244, 244,1));
                                grid.add(r, 0, 1, 1, 1);

                                engine.zmienZwierzeDoSledzenia((Animal)z);
                                Label label = new Label(z.toString());
                                grid.add(label, 0, 1, 29, 1);
                            }
                        });
                    }
                    grid.add(element.vbox, x - bottomLeft.x + 1, j + 2, 1, 1);
                    GridPane.setHalignment(element.vbox, HPos.CENTER);
                }
                ++j;
            }
        }

    }

    public GridPane renderujPierwszyRaz(AbstractWorldMap map)
    {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);

        bottomLeft = map.getPoczatekMapy();
        upperRight = map.getKraniecMapy();
        int pom = Math.max((upperRight.x - bottomLeft.x  + 2), 30);
        int w =  pom * width;
        int h = (upperRight.y - bottomLeft.y  + 4) * height;
        Scene scene = new Scene(grid, w, h);

        for (int i = 0; i <= pom - 1; i++)
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        for (int i = 0; i <= upperRight.y - bottomLeft.y + 3; i++)
            grid.getRowConstraints().add(new RowConstraints(height));

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        return grid;
    }


    private void renderujStart(Stage primaryStage)
    {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);

        for (int i = 0; i <= 15; i++)
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        for (int i = 0; i <= 15; i++)
            grid.getRowConstraints().add(new RowConstraints(height));

        Scene scene = new Scene(grid, 16 * width, 16 * height);
        primaryStage.setScene(scene);

        Button button = new Button("Start symulacji");
        grid.add(button, 1, 1, 5, 1);


        button.setOnAction(actionEvent ->
        {
            AbstractWorldMap map = new KulaZiemska(13, 13,  10, 1, true);
            int e = -5; // energiaTraconaPrzyTeleportacji = naRozmanaznie !
            // AbstractWorldMap map = new PiekielnyPortal(10, 10,  10, 20, false, e);
            int moveDelay = 500;
            Runnable engine = new SimulationEngine(map, this, moveDelay, 10, 25,
                    e, -2 * e, 10, new int[]{3, 3},
                    true, true, 1);

            Thread engineThread = new Thread(engine);
            engineThread.start();
        });

        primaryStage.show();
    }



}
