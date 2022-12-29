package agh;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Animal extends AbstractWorldMapElement
{
    private MapDirection orientation;
    private LinkedList<IPositionChangeObserver> obserwatorzy = new LinkedList<>();
    private AbstractWorldMap map;
    private int energia = 0; // ile zostało dni życia
    private int[] geny; // N genów, liczby 0 - 7
    private int aktualnyGen;
    private int wiek = 0;
    private int iloscPotomstwa = 0;
    private boolean szalenstwo;
    private int zjadlRoslin = 0;
    private boolean zyje = true;
    private int zmarloDnia;
    private HashMap<MapDirection, ImageView> zdjecia = new HashMap<>();
    private boolean doWyroznienia = false;

    public String toString()
    {
        if(zyje) return "Genom: [" + Utils.arrayToOneString(geny) + "], Aktywny gen: " + geny[aktualnyGen] + ", Energia : " + energia + ", Zjadl roslin: " + zjadlRoslin + ", Ma dzieci: " + iloscPotomstwa + ", Zyje: " + wiek + " dni";
        else return "Zmarlo dnia: " + zmarloDnia + ", W wieku: " + wiek + ", Mialo genom: [" + Utils.arrayToOneString(geny) + "] Zjadl roslin: " + zjadlRoslin + ", Mial dzieci: " + iloscPotomstwa;
    }
    public Animal(Vector2d initialPosition, AbstractWorldMap map, int[] geny, boolean szalenstwo) throws FileNotFoundException {
        this.position = initialPosition;
        this.map = map;
        this.geny = geny;
        this.orientation= getRandomOrientation();
        this.szalenstwo= szalenstwo;
        this.aktualnyGen = losujLiczbe(geny.length);
        for(MapDirection x : MapDirection.values())
        {
            Image image = map.zdjecia.get(x);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            zdjecia.put(x, imageView);
        }
    }
    public void move()
    {
        Vector2d newPos= new Vector2d (7, -7);
        positionChanged(this.position, newPos);
    }
    public boolean equals(Object other)
    {
        return this == other;
    }
    public void addObserver(IPositionChangeObserver observer)
    {
        if (!obserwatorzy.contains(observer)) obserwatorzy.add(observer);
    }
    private void positionChanged(Vector2d old_p, Vector2d new_p)
    {
        for (IPositionChangeObserver x: obserwatorzy) x.positionChanged(old_p, new_p, this);
    }
    private int losujLiczbe(int n)
    {
        Random rand = new Random();
        return rand.nextInt(n);
    }
    private MapDirection getRandomOrientation () {
        Random rand = new Random();
        int randNum= rand.nextInt(8);
        return MapDirection.N.turnBy(randNum);
    }
    public void changeOrientation (boolean reverseOrientation) {
        if (reverseOrientation) {
            orientation= orientation.turnBy(4);
        }
        else {
            int N= geny.length;
            int step= 1;
            orientation= orientation.turnBy(geny[aktualnyGen]);

            Random generator = new Random();
            if (this.szalenstwo && generator.nextInt(5) == 0)   // jeśli jest szalony i akurat mu odwaliło
                step= 1 + generator.nextInt(N - 1);

            aktualnyGen= (aktualnyGen + step) % N;
        }
    }
    public MapDirection getOrientation () {
        return this.orientation;
    }
    public String getPath()
    {
        return "src/main/resources/" + this.orientation.toString() + ".png";
    }
    public ImageView getImageView()
    {
        return zdjecia.get(this.orientation);
    }
    public void changeEnergy(int delta)
    {
        energia += delta;
    }
    public int getEnergy()
    {
        return energia;
    }
    public int[] getGenes(int start, int end)
    {
        return Arrays.copyOfRange(geny, start, end);
    }
    public void postarzej()
    {
        wiek++;
        energia--;
    }
    public int getWiek()
    {
        return wiek;
    }
    public void dodajDziecko()
    {
        iloscPotomstwa++;
    }
    public int getIloscPotomstwa()
    {
        return iloscPotomstwa;
    }
    public void setPosition (Vector2d p)
    {
        this.position = p;
    }
    public void zjadlRolsine(){zjadlRoslin++;}
    public void zabijGo(int dzien)
    {
        zdjecia = null;
        zyje = false;
        zmarloDnia = dzien;
    }
    public List<Integer> getWholeGenes()
    {
        return Arrays.stream( geny ).boxed().collect( Collectors.toList() );
    }
    public boolean czyDoWyroznienia() {return doWyroznienia;}
    public void zmienWyroznienie(boolean x) {doWyroznienia = x;}

}
