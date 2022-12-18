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
    protected int trawyNaPolachPreferowanych = 0;
    protected int trawyWogole = 0;
    protected int energiaTraconaPrzyTeleportacji;
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
               if(trawyWogole >= (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1)) return;
               if(trawyNaPolachPreferowanych == (kraniecRownika.x + 1) * (kraniecRownika.y - pocztekRownika.y + 1)) break; // przepelnienie rownika
               Vector2d pp = losujVectorNaRowniku();
               Grass g = new Grass(pp);
               trawnik.put(pp, g);
               i++;
               trawyNaPolachPreferowanych++;
               trawyWogole++;
           }
           while (i < ile)
           {
               if(trawyWogole >= (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1)) return;
               Vector2d pp = losujVectorNaMappieAleNieNaRowniku();
               Grass g = new Grass(pp);
               trawnik.put(pp, g);
               i++;
               trawyWogole++;
           }
       }

       else
       {
           int i = 0;
           int naToksycznychTrupach = Math.min((int)Math.ceil(ile * 0.2), toksyczneTrupy.size());
           int naPreferowanych = ile - naToksycznychTrupach;
           //System.out.println("Start preferowanych");
           while (i < naPreferowanych)
           {
               if(trawyWogole >= (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1)) return;
               if(trawyNaPolachPreferowanych >= (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1) - toksyczneTrupy.size()) break;
               Vector2d pp = losujVectorNaMappieAleNieWTokszycznychTrupach();
               Grass g = new Grass(pp);
               trawnik.put(pp, g);
               i++;
               trawyNaPolachPreferowanych++;
               trawyWogole++;
           }
         // System.out.println("Start NIE preferowanych");
           while (i < ile)
           {
               //System.out.println(toksyczneTrupy.size() + " " + trawyNaPolachPreferowanych + " " + trawyWogole);
               if(trawyWogole >= (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1)
                    || trawyWogole - trawyNaPolachPreferowanych >= naToksycznychTrupach ) return;
               Vector2d pp = losujVectorZtoksycznychTrupow();
               Grass g = new Grass(pp);
               trawnik.put(pp, g);
               i++;
               trawyWogole++;
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
    protected Vector2d losujVectorNaRowniku()
    {
        Random generator = new Random();
        while (true)
        {
            Vector2d pp = new Vector2d(generator.nextInt(kraniecRownika.x + 1),
                    generator.nextInt(kraniecRownika.y - pocztekRownika.y + 1) + pocztekRownika.y);
            if (!(objectAt(pp) instanceof Grass)) return pp;
        }
    }
    protected Vector2d losujVectorNaMappieAleNieNaRowniku()
    {
        Random generator = new Random();
        while (true)
        {
            Vector2d pp = new Vector2d(generator.nextInt(kraniecMapy.x + 1), generator.nextInt(kraniecMapy.y + 1));
            if (!(pp.follows( pocztekRownika) && pp.precedes(kraniecRownika)) && !(objectAt(pp) instanceof Grass)) return pp;
        }
    }
    protected Vector2d losujVectorNaMappieAleNieWTokszycznychTrupach()
    {
        Random generator = new Random();
        while (true)
        {
            boolean f = true;
            Vector2d pp = new Vector2d(generator.nextInt(kraniecMapy.x + 1), generator.nextInt(kraniecMapy.y + 1));
            for (doTreeSeta x: toksyczneTrupy)
                if (x.v.equals(pp))
                {
                    f = false;
                    break;
                }
            if(f) return pp;
        }
    }
    protected Vector2d losujVectorZtoksycznychTrupow()
    {
        Random generator = new Random();
        while (true)
        {
            int i = generator.nextInt(toksyczneTrupy.size());
            Object[] t = toksyczneTrupy.toArray();
            Vector2d pp = ((doTreeSeta)t[i]).v;
            if(!(objectAt(pp) instanceof Grass)) return pp;
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
            if(t == null)
            {
                t = new doTreeSeta(z.getPosition(), 0);
                if (trawnik.get(z.getPosition()) != null) trawyNaPolachPreferowanych--;
            }
            t.i++;
            toksyczneTrupy.add(t);
            //System.out.println(Arrays.toString(toksyczneTrupy.toArray()));
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
    protected boolean czyBylaWpreferowanych(Vector2d p)
    {
        if(pocztekRownika == null)
        {
            for (doTreeSeta x : toksyczneTrupy)
                if (x.v.equals(p))
                    return false;
            return true;
        }
        else
        {
            return  p.follows( pocztekRownika) && p.precedes(kraniecRownika);
        }
    }

}
