package agh;

import javafx.application.Platform;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class SimulationEngine implements  Runnable
{
    AbstractWorldMap mapa;
    private App app;
    private int moveDelay;

    private LinkedList<Animal> zwierzeta = new LinkedList<>();

    public SimulationEngine(AbstractWorldMap mapa, App app, int moveDelay, int iloscZwierzaat, int poczatkowaEnergia)
    {
        this.mapa = mapa;
        this.app = app;
        this.moveDelay = moveDelay;
        int i = 0;
        while (i < iloscZwierzaat)
        {
            Vector2d p = mapa.losujVectorNaMapie();
            Animal z = new Animal(p, mapa);
            z.changeEnergy(poczatkowaEnergia);
            zwierzeta.add(z);
            mapa.place(z);
            i++;
        }
    }

    public void run()
    {
        while (true)
        {
            for (Animal x: zwierzeta) x.move();
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
                public void run()
                {
                   app.renderuj(mapa);
                }
            });
        }
    }

}
