package agh;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver
{
    protected Map<Vector2d, Animal> zwierzeta = new HashMap<>();
    protected Map<Vector2d, Grass> trawnik = new HashMap<>();
    protected Vector2d poczatekMapy = new Vector2d(0, 0);
    protected Vector2d kraniecMapy;
    protected Vector2d poczatekRownika;
    protected Vector2d kraniecRownika;
    protected boolean trawaNaRowniku = false;
    public boolean place(Animal animal)
    {
        zwierzeta.put(animal.getPosition(), animal);
        animal.addObserver(this);
        return true;
    }
    public Object objectAt(Vector2d position)
    {
        if(zwierzeta.get(position) != null) return zwierzeta.get(position);
        return trawnik.get(position);
    }
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition)
    {
        Animal z = zwierzeta.get(oldPosition);
        zwierzeta.remove(oldPosition);
        zwierzeta.put(newPosition, z);
    }
    public void stworzTrawe(int ile)
    {
        int i = 0;
        while (i < ile)
        {
            Vector2d pp = losujVectorNaMapie();
            Grass g = new Grass(pp);
            trawnik.put(pp, g);
            i++;
        }
    }
    public Vector2d losujVectorNaMapie()
    {
        Random generator = new Random();
        while (true)
        {
            Vector2d pp = new Vector2d(generator.nextInt(kraniecMapy.x), generator.nextInt(kraniecMapy.y));
            if (objectAt(pp) == null)
                return pp;
        }
    }
    public Vector2d getPoczatekMapy()
    {
        return poczatekMapy;
    }
    public Vector2d getKraniecMapy()
    {
        return kraniecMapy;
    }
}
