package agh;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver
{
    protected Map<Vector2d, LinkedList<Animal>> zwierzeta = new HashMap<>();
    protected Map<Vector2d, Grass> trawnik = new HashMap<>();
    protected Vector2d poczatekMapy = new Vector2d(0, 0);
    protected Vector2d kraniecMapy;
    protected int energiaRoslin;
    protected Vector2d pocztekRownika = null;
    protected Vector2d kraniecRownika = null;
    protected int trawyNaRowniku = 0;
    protected class doTreeSeta
    {
        public Vector2d v;
        public int i;
        public doTreeSeta(Vector2d v, int i)
        {
            this.v = v;
            this.i = i;
        }
        public String toString(){return this.v + " " + this.i;}
    }
    protected TreeSet<doTreeSeta> toksyczneTrupy = null;

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
        if (t.size() > 0) zwierzeta.put(oldPosition, t);
        z.changeOrientation(false);
        newPosition = this.newPosition(oldPosition, z.getOrientation().toUnitVector(), z);

        if (oldPosition.equals(newPosition))
            z.changeOrientation(true);
        else
            z.setPosition(newPosition);
        dodajDoHaszMapy(z);
    }
    public void stworzTrawe(int ile)
    {
       if(toksyczneTrupy == null)
       {
           int i = 0;
           int naRowniku = (int)Math.floor(ile * 0.8);
           while (i < naRowniku)
           {
               if(trawyNaRowniku == (kraniecRownika.x + 1) * (kraniecRownika.y - pocztekRownika.y + 1)) break; // przepelnienie rownika
               Vector2d pp = losujVectorNaRowniku();
               Grass g = new Grass(pp);
               trawnik.put(pp, g);
               i++;
               trawyNaRowniku++;
           }
           while (i < ile)
           {
               Vector2d pp = losujVectorNaMappieAleNieNaRowniku();
               Grass g = new Grass(pp);
               trawnik.put(pp, g);
               i++;
           }
       }
    }
    public Vector2d losujVectorNaMapie()
    {
        Random generator = new Random();
        while (true)
        {
            Vector2d pp = new Vector2d(generator.nextInt(kraniecMapy.x + 1), generator.nextInt(kraniecMapy.y + 1));
            if (objectAt(pp) == null) return pp;
        }
    }
    public Vector2d losujVectorNaRowniku()
    {
        Random generator = new Random();
        while (true)
        {
            Vector2d pp = new Vector2d(generator.nextInt(kraniecRownika.x + 1),
                    generator.nextInt(kraniecRownika.y - pocztekRownika.y + 1) + pocztekRownika.y);
            if (objectAt(pp) == null) return pp;
        }
    }
    public Vector2d losujVectorNaMappieAleNieNaRowniku()
    {
        Random generator = new Random();
        while (true)
        {
            Vector2d pp = new Vector2d(generator.nextInt(kraniecMapy.x + 1), generator.nextInt(kraniecMapy.y + 1));
            if (!(pp.follows( pocztekRownika) && pp.precedes(kraniecRownika)) && objectAt(pp) == null) return pp;
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
        if(toksyczneTrupy != null)
        {
            doTreeSeta t = null;
            for(doTreeSeta x: toksyczneTrupy)
            {
                if(x.v.equals(z.getPosition()))
                {
                    t = x;
                    toksyczneTrupy.remove(x);
                    break;
                }
            }
            if (t == null)
            {
                t = new doTreeSeta(z.getPosition(), 0);
            }
            t.i++;
            toksyczneTrupy.add(t);
            System.out.println(Arrays.toString(toksyczneTrupy.toArray()));
        }

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
