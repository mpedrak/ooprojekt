package agh;

import com.jogamp.common.os.Platform;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.swing.plaf.TableHeaderUI;
import java.util.ArrayList;
import java.util.LinkedList;

public class SimulationEngine implements  Runnable
{
    IWorldMap mapa;
    private ArrayList<Animal> zwierzeta = new ArrayList<Animal>();
    private int moveDelay;
    public SimulationEngine(IWorldMap mapa, int moveDelay)
    {
        this.mapa = mapa;
        this.moveDelay = moveDelay;
        // umieszczanie zwierząt
    }
    public void setMoveDelay(int md)
    {
        this.moveDelay = md;
    }
    public void run()
    {
        while (true)
        {
            // ruszanie zwierzęcia
            try
            {
                Thread.sleep(moveDelay);
            }
            catch (InterruptedException ex)
            {
                System.out.println(ex + " przerwanie symulacji");
            }
            /*
            Platform.runLater(new Runnable()
            {
                public void run()
                {
                    // rysowanie
                }
            });
            */
        }
    }

}
