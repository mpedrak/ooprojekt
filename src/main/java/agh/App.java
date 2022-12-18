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

    public GridPane grid = null;
    final int width = 25;
    final int height = 25;
    Vector2d bottomLeft;
    Vector2d upperRight;


    public void start(Stage primaryStage)
    {


        //AbstractWorldMap map = new KulaZiemska(15, 15,  70, 50, false);
        int e = -10; // energiaTraconaPrzyTeleportacji = naRozmanaznie !
        AbstractWorldMap map = new PiekielnyPortal(15, 15,  70, 20, false, e);
        int moveDelay = 200;
        Runnable engine = new SimulationEngine(map, this, moveDelay, 10, 500,
                e, -2 * e, 10, new int[]{3, 3},
                true, true, 5);


        grid = new GridPane();
        grid.setGridLinesVisible(false);
        bottomLeft = map.getPoczatekMapy();
        upperRight = map.getKraniecMapy();
        int w = (upperRight.x - bottomLeft.x  + 2) * width;
        int h = (upperRight.y - bottomLeft.y  + 2) * height;
        Scene scene = new Scene(grid, w, h);
        for (int i = 0; i <= upperRight.x - bottomLeft.x + 1; i++)
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        for (int i = 0; i <= upperRight.y - bottomLeft.y + 1; i++)
            grid.getRowConstraints().add(new RowConstraints(height));
        primaryStage.setScene(scene);
        primaryStage.setX(10);
        primaryStage.setY(100);
        primaryStage.show();

        //do deleta
        Thread engineThread = new Thread(engine);
        engineThread.start();
        renderuj(map);
        //to tond



        //renderujStart(engine);
    }

    public void renderuj(AbstractWorldMap map)
    {
        grid.setGridLinesVisible(false);
        grid.getChildren().clear();
        grid.setGridLinesVisible(true);


        VBox vbox = new VBox();
        Label s = new Label( "y\\x");
        vbox.getChildren().addAll(s);
        vbox.setAlignment(Pos.CENTER);
        grid.add(vbox, 0, 0, 1, 1);
        GridPane.setHalignment(vbox, HPos.CENTER);




        for (int i = bottomLeft.x ; i <= upperRight.x; ++i)
        {
            vbox = new VBox();
            s = new Label(Integer.toString(i));
            vbox.getChildren().addAll(s);
            vbox.setAlignment(Pos.CENTER);
            grid.add(vbox, i - bottomLeft.x + 1, 0, 1, 1);
        }

        int j = 1;
        for (int i = upperRight.y; i >= bottomLeft.y; --i)
        {
            vbox = new VBox();
            s = new Label(Integer.toString(i));
            vbox.getChildren().addAll(s);
            vbox.setAlignment(Pos.CENTER);
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
                    element.vbox.setOnMouseClicked(new EventHandler<Event>() {
                        @Override
                        public void handle(Event event) {
                           System.out.println(">>>>>> " + z.toString());
                        }
                    });
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
