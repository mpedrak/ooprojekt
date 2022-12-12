package agh;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class Animal extends AbstractWorldMapElement
{
    private MapDirection orientation = MapDirection.N;
    private LinkedList<IPositionChangeObserver> obserwatorzy = new LinkedList<>();
    private AbstractWorldMap map;
    private int energia = 0; // ile zostało dni życia
    private int[] geny; // N genów, liczby 0 - 7
    private int aktualnyGen = 0;
    private int wiek = 0;
    private int iloscPotomstwa = 0;

    public String toString()
    {
        return orientation.toString();
    }
    public Animal(Vector2d initialPosition, AbstractWorldMap map, int[] geny)
    {
        this.position = initialPosition;
        this.map = map;
        this.geny = geny;
    }
    public void move()
    {
        this.orientation = this.orientation.turnBy(geny[aktualnyGen]);
        aktualnyGen = (aktualnyGen + 1) % geny.length;
        Vector2d newPos = map.newPosition(this.position, this.orientation.toUnitVector(), this);
        if (newPos.equals(this.position))
        {
            this.orientation = this.orientation.turnBy(4);
        }
        else
        {
            positionChanged(this.position, newPos);
            this.position = newPos;
        }
    }
    public boolean equals(Object other)
    {
        if (this == other) return true;
        if (!(other instanceof Animal)) return false;
        Animal that = (Animal) other;
        return this.position.equals(that.position) && this.orientation == that.orientation;
    }
    public int hashCode()
    {
        return Objects.hash(orientation, position);
    }
    public void addObserver(IPositionChangeObserver observer)
    {
        if (!obserwatorzy.contains(observer)) obserwatorzy.add(observer);
    }
    private void positionChanged(Vector2d old_p, Vector2d new_p)
    {
        for (IPositionChangeObserver x: obserwatorzy) x.positionChanged(old_p, new_p, this);
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

}
