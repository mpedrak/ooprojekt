package agh;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver
{
    protected Map<Vector2d, LinkedList<Animal>> zwierzeta = new HashMap<>();
    protected Map<Vector2d, Grass> trawnik = new HashMap<>();
    protected Vector2d poczatekMapy = new Vector2d(0, 0);
    protected Vector2d kraniecMapy;
    protected int energiaRoslin;
    public boolean place(Animal animal)
    {
        animal.addObserver(this);
        dodajDoHaszMapy(animal);
        return true;
    }
    public Object objectAt(Vector2d position)
    {
        if(zwierzeta.get(position) != null ) return zwierzeta.get(position).get(0);
        return trawnik.get(position);
    }
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal z)
    {
        LinkedList<Animal> t = zwierzeta.remove(oldPosition);
        t.remove(z);
        z.changeOrientation(false);
        newPosition = this.newPosition(oldPosition, z.getOrientation().toUnitVector(), z);

        if (oldPosition.equals(newPosition))
            z.changeOrientation(true);
        else
            z.setPosition(newPosition);

        if (t.size() > 0) zwierzeta.put(oldPosition, t);
        dodajDoHaszMapy(z);
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
            if (objectAt(pp) == null) return pp;
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
    public void smiercZwierzecia(Animal z)
    {
        usunZHaszMapy(z);
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
        if (t.size() > 0) zwierzeta.put(p, t);
    }
    public void wypiuszDoDebugu()
    {
        System.out.println("Mapa");
        for (Vector2d name: zwierzeta.keySet())
        {
            String key = name.toString();
            String value = Arrays.toString(zwierzeta.get(name).toArray());
            System.out.println(key + " " + value);
        }
        System.out.println("Kkoiec mapy");
    }
}
