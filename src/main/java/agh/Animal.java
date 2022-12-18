package agh;

import java.io.FileInputStream;
import java.util.*;

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

    public String toString()
    {
        return orientation.toString() + " " + position.toString() + " kcal: " + energia + " wiek: " + wiek + " geny: " + Arrays.toString(geny) + " it=[" + aktualnyGen + "] " + "dzieci: " + iloscPotomstwa;
    }
    public Animal(Vector2d initialPosition, AbstractWorldMap map, int[] geny, boolean szalenstwo)
    {
        this.position = initialPosition;
        this.map = map;
        this.geny = geny;
        this.orientation= getRandomOrientation();
        this.szalenstwo= szalenstwo;
        this.aktualnyGen = losujLiczbe(geny.length);
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
        return "src/main/resources/" + orientation.toString() + ".png";
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

}
