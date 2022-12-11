package agh;

import javafx.application.Platform;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class SimulationEngine implements  Runnable
{
    IWorldMap mapa;
    private App app;
    private int moveDelay;

    private LinkedList<Animal> zwierzeta = new LinkedList<>();

    public SimulationEngine(IWorldMap mapa, App app, int moveDelay, int liczbaRoslinKazdegoDnia, int iloscZwierzaat,
                            int poczatkowaEnergia)
    {
        this.mapa = mapa;
        this.app = app;
        this.moveDelay = moveDelay;
        int i = 0;
        while (i < iloscZwierzaat)
        {
            Vector2d p = mapa.losujVectorNaMapie();
            while (mapa.objectAt(p) != null)
                p = mapa.losujVectorNaMapie();
            Animal z = new Animal(p);
            z.changeEnergy(poczatkowaEnergia);
            zwierzeta.add(z);
            mapa.place(z);
        }
    }
    public void setMoveDelay(int moveDelay)
    {
        this.moveDelay = moveDelay;
    }
    public void run()
    {
        while (true)
        {
            for (Animal x: zwierzeta)
            {
                x.move();
            }
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
