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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
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

    public void renderuj(AbstractWorldMap map, GridPane grid, SimulationEngine engine)
    {
        grid.setGridLinesVisible(false);
        grid.getChildren().clear();
        grid.setGridLinesVisible(true);

        Button button = new Button(" Pauza ");
        grid.add(button, 0, 0, 5, 1);

        button.setOnAction(actionEvent ->
        {
            engine.pauza();
        });

        VBox vbox = new VBox();
        Label s = new Label( "y\\x");
        vbox.getChildren().addAll(s);
        vbox.setAlignment(Pos.CENTER);
        grid.add(vbox, 0, 1, 1, 1);
        GridPane.setHalignment(vbox, HPos.CENTER);

        for (int i = bottomLeft.x ; i <= upperRight.x; ++i)
        {
            vbox = new VBox();
            s = new Label(Integer.toString(i));
            vbox.getChildren().addAll(s);
            vbox.setAlignment(Pos.CENTER);
            grid.add(vbox, i - bottomLeft.x + 1, 1, 1, 1);
        }

        int j = 1;
        for (int i = upperRight.y; i >= bottomLeft.y; --i)
        {
            vbox = new VBox();
            s = new Label(Integer.toString(i));
            vbox.getChildren().addAll(s);
            vbox.setAlignment(Pos.CENTER);
            grid.add(vbox, 0, j + 1, 1, 1);
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
                    element.vbox.setOnMouseClicked(new EventHandler<Event>() {
                        @Override
                        public void handle(Event event) {
                           System.out.println(">>>>>> " + z.toString());
                        }
                    });
                    grid.add(element.vbox, x - bottomLeft.x + 1, j + 1, 1, 1);
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
        int w = Math.max((upperRight.x - bottomLeft.x  + 2), 10) * width;
        int h = Math.max((upperRight.y - bottomLeft.y  + 3), 5) * height;
        Scene scene = new Scene(grid, w, h);

        for (int i = 0; i <= upperRight.x - bottomLeft.x + 1; i++)
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        for (int i = 0; i <= upperRight.y - bottomLeft.y + 2; i++)
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
            AbstractWorldMap map = new KulaZiemska(15, 15,  20, 10, true);
            int e = -5; // energiaTraconaPrzyTeleportacji = naRozmanaznie !
            // AbstractWorldMap map = new PiekielnyPortal(10, 10,  10, 20, false, e);
            int moveDelay = 500;
            Runnable engine = new SimulationEngine(map, this, moveDelay, 10, 10,
                    e, -2 * e, 10, new int[]{3, 3},
                    true, true, 1);

            Thread engineThread = new Thread(engine);
            engineThread.start();
        });

        primaryStage.show();
    }



}
