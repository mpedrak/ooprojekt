package agh;

import javafx.application.Platform;
public class SimulationEngine implements  Runnable
{
    IWorldMap mapa;
    private App app;
    private int moveDelay;

    public SimulationEngine(IWorldMap mapa, App app, int moveDelay)
    {
        this.mapa = mapa;
        this.app = app;
        this.moveDelay = moveDelay;
        // placowanie
    }
    public void setMoveDelay(int moveDelay)
    {
        this.moveDelay = moveDelay;
    }
    public void run()
    {

        while (true)
        {
            try
            {
                Thread.sleep(moveDelay);
            }
            catch (InterruptedException ex)
            {
                System.out.println(ex + " przerwanie symulacji");
            }
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                   // app.renderuj(mapa);
                }
            });
        }
    }

}
