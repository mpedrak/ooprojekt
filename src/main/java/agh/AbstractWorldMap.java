package agh;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver
{
    protected Map<Vector2d, Animal> zwierzeta = new HashMap<>();
    protected Map<Vector2d, Grass> trawnik = new HashMap<>();
    protected Vector2d poczatekMapy = new Vector2d(0, 0);
    protected Vector2d kraniecMapy;
    protected Vector2d poczatekDzungli;
    protected Vector2d kraniecDzungli;
    public boolean place(Animal animal)
    {

        zwierzeta.put(animal.getPosition(), animal);
        animal.addObserver(this);
        return true;
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
