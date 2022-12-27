package agh;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;

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
    private int[] zakresIlosciMutacji;  // tab[0] -> dolne ograniczenie, tab[1] -> górne ograniczenie (włącznie!)
    private boolean pelnaLosowosc;
    private boolean szalenstwo;
    private int ileTrawyDziennie;
    private GridPane grid = null;
    private volatile boolean czyDziala = true;
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
                            int iloscGenow, int[] zakresIlosciMutacji, boolean pelnaLosowosc, boolean szalenstwo,
                            int ileTrawyDziennie)

    {
        this.mapa = mapa;
        this.app = app;
        this.moveDelay = moveDelay;
        this.energiaNaRozmnazanie = energiaNaRozmnazanie;
        this.minimalnaEnergiaDoRozmnazania = minimalnaEnergiaDoRozmnazania;
        this.iloscGenow = iloscGenow;
        this.zakresIlosciMutacji= zakresIlosciMutacji;
        this.pelnaLosowosc = pelnaLosowosc;
        this.szalenstwo= szalenstwo;
        this.ileTrawyDziennie = ileTrawyDziennie;
        int i = 0;
        while (i < iloscZwierzaat)
        {
            Vector2d p = mapa.losujVectorNaMapie();
            // p = new Vector2d(2,2);
            Animal z = new Animal(p, mapa, zrobLosoweGeny(), szalenstwo);
            z.changeEnergy(poczatkowaEnergia);
            dodajDoHaszMapy(z);
            zwierzetaPosortowane.add(z);
            mapa.place(z);
            i++;
        }
    }

    public void run()
    {
        Platform.runLater(new Runnable()
        {
            public void run()
            {
                grid = app.renderujPierwszyRaz(mapa);
                app.renderuj(mapa, grid, SimulationEngine.this);
            }
        });

        while (true) {
            if (czyDziala) {
                //Scanner scan = new Scanner(System.in);
                // scan.nextLine();


                // System.out.println("Początek iteracji -----------------------------------------");
                // wypiuszDoDebugu();
                // mapa.wypiuszDoDebugu();
                for (Animal x : zwierzetaPosortowane) {
                    // System.out.print(">> Robię se ruszanie dla: " + x.toString());

                    if (x.getEnergy() <= 1) {
                        usunZHaszMapy(x);
                        mapa.smiercZwierzecia(x);
                    } else {
                        usunZHaszMapy(x);
                        x.postarzej();
                        x.move();
                        dodajDoHaszMapy(x);
                    }

                    // System.out.println("-> Po ruchu: " + x.toString());
                }

                /// debug do usuniecia
                for (Vector2d name : zwierzeta.keySet()) {
                    String key = name.toString();
                    String value = Arrays.toString(zwierzeta.get(name).toArray());
                    // System.out.println(key + " " + value);
                }
                /// debug do usuniecia

                // System.out.println("- zwierzetaPosortowane.size() = " + zwierzetaPosortowane.size());
                zwierzetaPosortowane.clear();
                // System.out.println("- zwierzetaPosortowane.size() = " + zwierzetaPosortowane.size());
                for (LinkedList<Animal> x : zwierzeta.values())
                    for (Animal y : x)
                        zwierzetaPosortowane.add(y);
                // System.out.println("- zwierzetaPosortowane.size() = " + zwierzetaPosortowane.size());


                // System.out.println("Po ruszeniu");
                // wypiuszDoDebugu();
                // mapa.wypiuszDoDebugu();


                LinkedList<Animal> dzieci = new LinkedList<>();
                for (Vector2d p : zwierzeta.keySet()) {
                    LinkedList<Animal> t = new LinkedList<>();
                    for (Animal x : zwierzetaPosortowane)
                        if (x.getPosition().equals(p) && x.getEnergy() >= minimalnaEnergiaDoRozmnazania) t.add(x);

                    if (t.size() >= 2) {
                        // System.out.println("diecko jest robione na " + p.toString());
                        Animal z1 = t.get(0);
                        Animal z2 = t.get(1);
                        Animal dziecko = new Animal(p, mapa, zrobGenyDlaDziecka(z1, z2), szalenstwo);
                        dziecko.changeEnergy(-2 * energiaNaRozmnazanie);
                        mapa.place(dziecko);
                        dzieci.add(dziecko);

                        zwierzetaPosortowane.remove(z1);
                        zwierzetaPosortowane.remove(z2);
                        z1.changeEnergy(energiaNaRozmnazanie);
                        z1.dodajDziecko();
                        z2.changeEnergy(energiaNaRozmnazanie);
                        z2.dodajDziecko();
                        zwierzetaPosortowane.add(z1);
                        zwierzetaPosortowane.add(z2);
                    }
                }

                for (Animal x : dzieci) {
                    dodajDoHaszMapy(x);
                    zwierzetaPosortowane.add(x);
                }
                mapa.stworzTrawe(ileTrawyDziennie);
                // System.out.println("Po dzieciach");
                // wypiuszDoDebugu();
                // mapa.wypiuszDoDebugu();


                // System.out.println("Koniec iteracji -------------------------------------------");


                try {
                    Thread.sleep(moveDelay);
                } catch (InterruptedException ex) {
                    System.out.println(ex + " przerwanie symulacji");
                }


                Platform.runLater(new Runnable() {
                    public void run() {
                        app.renderuj(mapa, grid, SimulationEngine.this);
                    }
                });
            }
            else
            {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    System.out.println(ex + " przerwanie symulacji");
                }
            }
        }

    }
    public int[] zrobLosoweGeny () {
        int[] geny= new int[iloscGenow];
        Random generator = new Random();
        for (int i=0; i<iloscGenow; i++) {
            int randNum= generator.nextInt(8);
            geny[i]= randNum;
        }

        return geny;
    }
    public int[] zrobGenyDlaDziecka(Animal matka, Animal ojciec)
    {
        // budowanie podstawowego genotypu //

        int N = iloscGenow;
        int[] genyDziecka= new int[N];
        int[] genyMatki= matka.getGenes(0, N);
        int[] genyOjca= ojciec.getGenes(0, N);
        Random generator = new Random();

        int sumaEnergiiRodzicow= matka.getEnergy() + ojciec.getEnergy();
        int udzialMatki, udzialOjca;

        double temp= (double)matka.getEnergy() / (double)sumaEnergiiRodzicow * (double)N;

        if (matka.getEnergy() < ojciec.getEnergy())
            temp= Math.max(1.0, Math.floor(temp));
        else
            temp= Math.min(Math.ceil(temp), N - 1) ;

        udzialMatki= (int)temp;
        udzialOjca= N - udzialMatki;

        if (generator.nextInt(2) == 0) {
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

        // mutacje //

        int liczbaMutacji= zakresIlosciMutacji[0] + generator.nextInt(zakresIlosciMutacji[1] - zakresIlosciMutacji[0] + 1);
        boolean[] mutowalneGeny= new boolean[N];
        for (int i=0; i<N; i++)
            mutowalneGeny[i]= false;

        for (int i=0; i<liczbaMutacji;) {
            int it= generator.nextInt(N);
            if (!mutowalneGeny[it]) {
                mutowalneGeny[it]= true;
                i++;
            }
        }

        for (int i=0; i<N; i++) {
            if (!mutowalneGeny[i])
                continue;

            int zmiana= 1;
            if (pelnaLosowosc)
                zmiana= 1 + generator.nextInt(7);
            else {
                int dodatniosc= generator.nextInt(2);
                if (dodatniosc == 0)
                    zmiana*= -1;
            }

            genyDziecka[i]= (genyDziecka[i] + 8 + zmiana) % 8;
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
    public void pauza()
    {
       czyDziala = !czyDziala;
    }

}
