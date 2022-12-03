package agh;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import static java.lang.Math.sqrt;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver
{
    protected Map<Vector2d, Animal> zwierzeta = new HashMap<>();
    protected Map<Vector2d, Grass> trawnik = new HashMap<>();
    protected Vector2d poczatekMapy = new Vector2d(0, 0);
    protected Vector2d kraniecMapy;
    protected Vector2d poczatekDzungli;
    protected Vector2d kraniecDzungli;

    public String toString()
    {
        MapVisualizer rysownik = new MapVisualizer(this);
        return rysownik.draw(poczatekMapy, kraniecMapy);
    }
    public boolean place(Animal animal)
    {
        if (canMoveTo(animal.getPosition()) != -1)
        {
            zwierzeta.put(animal.getPosition(), animal);
            animal.addObserver(this);
            return true;
        }
        throw new IllegalArgumentException("Cant place animal on position: " + animal.getPosition());
    }
    public int canMoveTo(Vector2d position)
    {
        if(objectAt(position) == null)
            return 0;
        if ((objectAt(position) instanceof Grass))
        {
            trawnik.remove(position);
            // stworzenie trawy
            return 1;
        }
        if ((objectAt(position) instanceof Animal))
        {
            return 2;
            // urodzenie dziecka
        }
        return -1;
    }
    public boolean isOccupied(Vector2d position)
    {
        return objectAt(position) != null;
    }
    public Object objectAt(Vector2d position)
    {
        if(zwierzeta.get(position) != null)
            return zwierzeta.get(position);
        if(trawnik.get(position) != null)
            return trawnik.get(position);
        return null;
    }
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition)
    {
        Animal z = zwierzeta.get(oldPosition);
        zwierzeta.remove(oldPosition);
        zwierzeta.put(newPosition, z);
    }
    public Grass stworzTraweNaStepie(Vector2d old_p)
    {
        Random generator = new Random();
        while (true)
        {
            Vector2d pp = new Vector2d(generator.nextInt(kraniecMapy.x), generator.nextInt(kraniecMapy.y));
            if (objectAt(pp) == null && !pp.equals(old_p) && !czyNaTerenieDzungli(pp))
                return new Grass(pp);
        }
    }
    public Grass stworzTraweWDzungli(Vector2d old_p)
    {
        Random generator = new Random();
        while (true)
        {
            Vector2d pp = new Vector2d(generator.nextInt(kraniecDzungli.x - poczatekDzungli.x) + poczatekDzungli.x,
                    generator.nextInt(kraniecDzungli.y - poczatekDzungli.y) + poczatekDzungli.y);
            if(objectAt(pp) == null && !pp.equals(old_p) )
                return new Grass(pp);
        }
    }
    private boolean czyNaTerenieDzungli(Vector2d pos)
    {
        return pos.follows(poczatekDzungli) && pos.precedes(kraniecDzungli);
    }
}
