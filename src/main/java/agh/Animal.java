package agh;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class Animal extends AbstractWorldMapElement
{
    private MapDirection orientation ;
    private LinkedList<IPositionChangeObserver> obserwatorzy = new LinkedList<>();
    private IWorldMap map;
    private int energia; // ile zostało dni życia
    private int[] geny; // 32 geny, liczby 0 - 7
    public String toString()
    {
        return orientation.toString();
    }
    public Animal(IWorldMap map, Vector2d initialPosition)
    {
        this.map = map;
        this.position = initialPosition;
    }
    public boolean isAt(Vector2d position)
    {
        return this.position.equals(position);
    }
    public void move(int direction)
    {
        Vector2d new_pos = new Vector2d(0, 0);
        if (map.canMoveTo(new_pos))
        {
            positionChanged(position, new_pos);
        }
    }

    public boolean equals(Object other)
    {
        if (this == other)
            return true;
        if (!(other instanceof Animal))
            return false;
        Animal that = (Animal) other;
        return this.position.equals(that.position) && this.orientation == that.orientation;
    }
    public int hashCode()
    {
        return Objects.hash(orientation, position);
    }
    public void addObserver(IPositionChangeObserver observer)
    {
        if (!obserwatorzy.contains(observer))
            obserwatorzy.add(observer);
    }
    public void removeObserver(IPositionChangeObserver observer)
    {
        obserwatorzy.remove(observer);
    }
    private void positionChanged(Vector2d old_p, Vector2d new_p)
    {
        for (IPositionChangeObserver x: obserwatorzy)
            x.positionChanged(old_p, new_p);
    }
    public String getPath()
    {
        return "src/main/resources/animal.png";
    }
    public int randomMove()
    {
        Random generator = new Random();
        int gen = generator.nextInt(32);
        return geny[gen];
    }
}
