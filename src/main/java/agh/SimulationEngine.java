package agh;

import javafx.application.Platform;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationEngine implements  Runnable
{
    AbstractWorldMap mapa;
    private App app;
    private int moveDelay;
    private int energiaNaRozmnazanie;
    private int minimalnaEnergiaDoRozmnazania;
    private int iloscGenow;

    private TreeSet<Animal> zwierzeta = new TreeSet<>(new Comparator<Animal>()
    {
        public int compare (Animal a, Animal b)
        {
            if(a.getEnergy() == b.getEnergy())
            {
                if(a.getWiek() == b.getWiek())
                {
                    if(a.getIloscPotomstwa() == b.getIloscPotomstwa()) return 1;
                    else return b.getIloscPotomstwa() - a.getIloscPotomstwa();
                }
                else return b.getWiek() - a.getWiek();
            }
            else return b.getEnergy() - a.getEnergy();
        }
    }
    );
    private HashMap<Vector2d, LinkedList<Animal>> zwierzeta2 = new HashMap<>();


    public SimulationEngine(AbstractWorldMap mapa, App app, int moveDelay, int iloscZwierzaat, int poczatkowaEnergia,
                            int energiaNaRozmnazanie, int minimalnaEnergiaDoRozmnazania,
                            int iloscGenow)
    {
        this.mapa = mapa;
        this.app = app;
        this.moveDelay = moveDelay;
        this.energiaNaRozmnazanie = energiaNaRozmnazanie;
        this.minimalnaEnergiaDoRozmnazania = minimalnaEnergiaDoRozmnazania;
        this.iloscGenow = iloscGenow;
        int i = 0;
        while (i < iloscZwierzaat)
        {
            Vector2d p = mapa.losujVectorNaMapie();
            Animal z = new Animal(p, mapa, new int[]{0, 1, 2, 3, 4});
            z.changeEnergy(poczatkowaEnergia);
            zwierzeta.add(z);
            LinkedList<Animal> t = zwierzeta2.remove(p);
            t.add(z);
            zwierzeta2.put(p, t);
            mapa.place(z);
            i++;
        }
    }

    public void run()
    {
        while (true)
        {
            for (Animal x: zwierzeta)
            {
                x.changeEnergy(-1);
                if (x.getEnergy() == 0)
                {
                    mapa.smiercZwierzecia(x);
                    zwierzeta2.remove(x.getPosition());
                }
                else
                {
                    x.move();
                    x.postarzej();
                }
            }
            zwierzeta.clear();
            for (LinkedList<Animal> x: zwierzeta2.values())
                for (Animal y: x)
                    zwierzeta.add(y);
            for (Vector2d x: zwierzeta2.keySet())
            {
                LinkedList<Animal> t = zwierzeta2.get(x);
                t.removeIf(an -> an.getEnergy() < minimalnaEnergiaDoRozmnazania);
                if (t.size() >= 2)
                {
                    Animal z1 = t.get(0);
                    Animal z2 = t.get(1);
                    z1.changeEnergy(energiaNaRozmnazanie);
                    z2.changeEnergy(energiaNaRozmnazanie);
                    Animal dziecko = new Animal(x, mapa, zrobGenyDlaDziecka(z1, z2));
                    dziecko.changeEnergy(-2 * energiaNaRozmnazanie);
                    t = zwierzeta2.remove(x);
                    mapa.place(dziecko);
                    t.add(dziecko);
                    zwierzeta2.put(x, t);
                }
            }
            for (LinkedList<Animal> x: zwierzeta2.values())
                for (Animal y: x)
                    zwierzeta.add(y);
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
    public int[] zrobGenyDlaDziecka(Animal matka, Animal ojciec)
    {
        int N = iloscGenow;
        int[] genyDziecka= new int[N];
        int[] genyMatki= matka.getGenes(0, N - 1);
        int[] genyOjca= ojciec.getGenes(0, N - 1);

        int sumaEnergiiRodzicow= matka.getEnergy() + ojciec.getEnergy();
        int udzialMatki, udzialOjca;

        double temp= matka.getEnergy() / sumaEnergiiRodzicow * N;

        if (matka.getEnergy() < ojciec.getEnergy())
            temp= Math.floor(temp);
        else
            temp= Math.ceil(temp);

        udzialMatki= (int)temp;
        udzialOjca= N - udzialMatki;

        if (ThreadLocalRandom.current().nextInt(0, 2) == 0) {
            // matka z lewej, ojciec z prawej
            for (int i=0; i < udzialMatki; i++)
                genyDziecka[i]= genyMatki[i];
            for (int i=udzialMatki; i < N; i++)
                genyDziecka[i]= genyOjca[i];
        }
        else {
            // matka z prawej, ojciec z lewej
            for (int i=0; i < udzialOjca; i++)
                genyDziecka[i]= genyOjca[i];
            for (int i=udzialOjca; i < N; i++)
                genyDziecka[i]= genyMatki[i];
        }
        return genyDziecka;
    }

}
