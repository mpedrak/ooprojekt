package agh;

import javafx.application.Platform;

import java.io.IOException;
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
    private HashMap<Vector2d, LinkedList<Animal>> zwierzeta = new HashMap<>();
    private TreeSet<Animal> zwierzetaPosortowane = new TreeSet<>(new Comparator<Animal>() {
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
    });
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
            p = new Vector2d(2,2);
            Animal z = new Animal(p, mapa, new int[]{0, 1, 2, 3, 4});
            z.changeEnergy(poczatkowaEnergia);
            dodajDoHaszMapy(z);
            zwierzetaPosortowane.add(z);
            mapa.place(z);
            i++;
        }
    }

    public void run()
    {
        while (true)
        {
            System.out.println("Początek iteracji -----------------------------------------");
            wypiuszDoDebugu();
            mapa.wypiuszDoDebugu();
            for (Animal x: zwierzetaPosortowane)
            {
                boolean debugTrashyVal= x.getEnergy() == 4 && x.getPosition().equals(new Vector2d(2, 3));
                System.out.print(">> Robię se ruszanie dla: " + x.toString() + " (" + debugTrashyVal + ")");

                if(x.getEnergy() <= 1)
                {
                    usunZHaszMapy(x);
                    mapa.smiercZwierzecia(x);
                }
                else
                {
                    usunZHaszMapy(x);
                    x.postarzej();
                    x.move();
                    dodajDoHaszMapy(x);
                }

                System.out.print("-> Po ruchu: " + x.toString() + " (" + debugTrashyVal + ")\n");
            }

            /// do usuniecia
            for (Vector2d name: zwierzeta.keySet())
            {
                String key = name.toString();
                String value = Arrays.toString(zwierzeta.get(name).toArray());
                System.out.println(key + " " + value);
            }
            /// do usuniecia

            System.out.println("- zwierzetaPosortowane.size() = " + zwierzetaPosortowane.size());
            zwierzetaPosortowane.clear();
            System.out.println("- zwierzetaPosortowane.size() = " + zwierzetaPosortowane.size());
            for (LinkedList<Animal> x: zwierzeta.values()) {
                System.out.println("> ");

                for (Animal y: x) zwierzetaPosortowane.add(y);
            }
            System.out.println("- zwierzetaPosortowane.size() = " + zwierzetaPosortowane.size());


            System.out.println("Po ruszeniu");
            wypiuszDoDebugu();
            mapa.wypiuszDoDebugu();


            LinkedList<Animal> dzieci = new LinkedList<>();
            for (Vector2d p: zwierzeta.keySet())
            {
                LinkedList<Animal> t = new LinkedList<>();
                for (Animal x: zwierzetaPosortowane)
                    if (x.getPosition().equals(p) && x.getEnergy() >= minimalnaEnergiaDoRozmnazania) t.add(x);

                if (t.size() >= 2)
                {
                    System.out.println("diecko jest robione na " + p.toString());
                    Animal z1 = t.get(0);
                    Animal z2 = t.get(1);
                    Animal dziecko = new Animal(p, mapa, zrobGenyDlaDziecka(z1, z2));
                    dziecko.changeEnergy(-2 * energiaNaRozmnazanie);
                    mapa.place(dziecko);
                    dzieci.add(dziecko);
                    /*
                    zwierzetaPosortowane.remove(z1);
                    zwierzetaPosortowane.remove(z2);
                    z1.changeEnergy(energiaNaRozmnazanie);
                    z1.dodajDziecko();
                    z2.changeEnergy(energiaNaRozmnazanie);
                    z2.dodajDziecko();
                    zwierzetaPosortowane.add(z1);
                    zwierzetaPosortowane.add(z2);
                            */

                }
            }

            for (Animal x: dzieci)
            {
                dodajDoHaszMapy(x);
                zwierzetaPosortowane.add(x);
            }
            System.out.println("Po dzieciach");
            wypiuszDoDebugu();
            mapa.wypiuszDoDebugu();


            System.out.println("Koniec iteracji -------------------------------------------");

            try
            {
                Thread.sleep(moveDelay);
            }
            catch (InterruptedException ex)
            {
                System.out.println(ex + " przerwanie symulacji");
            }

             /*
            Scanner scan = new Scanner(System.in);
            scan.nextLine();
            */
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
//        return new int[]{4, 0, 4, 0, 4};

        int N = iloscGenow;
        int[] genyDziecka= new int[N];
        int[] genyMatki= matka.getGenes(0, N);
        int[] genyOjca= ojciec.getGenes(0, N);

        int sumaEnergiiRodzicow= matka.getEnergy() + ojciec.getEnergy();
        int udzialMatki, udzialOjca;

        double temp= (double)matka.getEnergy() / (double)sumaEnergiiRodzicow * (double)N;

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
    private void dodajDoHaszMapy(Animal z)
    {
        Vector2d p = z.getPosition();
        LinkedList<Animal> t = zwierzeta.remove(p);
        if (t == null) t = new LinkedList<>();
        t.add(z);
        zwierzeta.put(p, t);
    }
    private void usunZHaszMapy(Animal z)
    {
        Vector2d p = z.getPosition();
        LinkedList<Animal> t = zwierzeta.remove(p);
        t.remove(z);
        if(t.size() > 0) zwierzeta.put(p, t);
    }
    public void wypiuszDoDebugu()
    {
        System.out.println("Engin");
        System.out.println(Arrays.toString(zwierzetaPosortowane.toArray()));
        for (Vector2d name: zwierzeta.keySet())
        {
            String key = name.toString();
            String value = Arrays.toString(zwierzeta.get(name).toArray());
            System.out.println(key + " " + value);
        }
        System.out.println("Koniec enginu");
    }
}
