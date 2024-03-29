package agh;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver
{
    protected Map<Vector2d, LinkedList<Animal>> zwierzeta = new HashMap<>();
    protected Map<Vector2d, Grass> trawnik = new HashMap<>();
    public Map<List<Integer>, Integer> genotypy = new HashMap<>();
    public boolean jestWyrozniony = false;
    protected Vector2d poczatekMapy = new Vector2d(0, 0);
    protected Vector2d kraniecMapy;
    protected int energiaRoslin;
    protected Vector2d pocztekRownika = null;
    protected Vector2d kraniecRownika = null;
    protected int trawyNaPolachPreferowanych = 0;
    protected int trawyWogole = 0;
    protected int energiaTraconaPrzyTeleportacji;
    public HashMap<MapDirection, Image> zdjecia = new HashMap<>();
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
        public boolean equals(Object obj)
        {
            if (this == obj) return true;
            if (!(obj instanceof doTreeSeta)) return false;
            return ((doTreeSeta) obj).v == this.v && ((doTreeSeta) obj).i == this.i;
        }


    }
    protected TreeSet<doTreeSeta> toksyczneTrupy = null;

    public boolean place(Animal animal)
    {
        animal.addObserver(this);
        List<Integer> t = animal.getWholeGenes();
        Integer pom = genotypy.remove(t);
        if(pom == null) genotypy.put(t, 1);
        else genotypy.put(t, pom + 1);
       // wypiuszDoDebugu();
        dodajDoHaszMapy(animal);
        return true;
    }
    public Object objectAt(Vector2d position) throws ConcurrentModificationException
    {
        LinkedList<Animal> t = zwierzeta.get(position);
        if( t != null )
        {
            if(jestWyrozniony)
            {
                Object tt[] = t.toArray();
                for (int i = 0; i < tt.length; i++) if (tt[i] != null && ((Animal)tt[i]).czyDoWyroznienia()) return tt[i];
                return tt[0];
            }
            return t.get(0);
        }
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

           //wypiuszDoDebugu();
           //System.out.println(trawnik.keySet().size() == trawyWogole);
           int i = 0;
           int naRowniku = ile - (int)Math.floor(ile * 0.2);
          // System.out.println(trawyNaPolachPreferowanych + ", " +  trawyWogole + ", " + (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1) );
           //System.out.println("7");
           while (i < naRowniku)
           {
               //System.out.println(trawyNaPolachPreferowanych + ", " +  trawyWogole + ", " + (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1) );
               if(trawyWogole >= (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1)) return;
               if(trawyNaPolachPreferowanych == (kraniecRownika.x + 1) * (kraniecRownika.y - pocztekRownika.y + 1)) break; // przepelnienie rownika
               Vector2d pp = losujVectorNaRowniku();
               Grass g = new Grass(pp);
               trawnik.put(pp, g);
               i++;
               trawyNaPolachPreferowanych++;
               trawyWogole++;
           }
         // System.out.println(i);
          // );
           while (i < ile)
           {
              // System.out.println(trawyWogole - trawyNaPolachPreferowanych >=
                      // (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1) - (kraniecRownika.x + 1) * (kraniecRownika.y - pocztekRownika.y + 1));
               if(trawyWogole >= (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1)) return;
               if(trawyWogole - trawyNaPolachPreferowanych >=
                       (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1) - (kraniecRownika.x + 1) * (kraniecRownika.y - pocztekRownika.y + 1)) return;
               Vector2d pp = losujVectorNaMappieAleNieNaRowniku();
               Grass g = new Grass(pp);
               trawnik.put(pp, g);
               i++;
               trawyWogole++;
           }
          // System.out.println("Po rowniku " + trawyNaPolachPreferowanych + ", " +  trawyWogole + ", " + (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1));
       }

       else
       {
          // System.out.println(trawnik.keySet().size() == trawyWogole);
           TreeSet<doTreeSeta> tokszczynePom = new TreeSet<>(new Comparator<doTreeSeta>() {
               public int compare (doTreeSeta a, doTreeSeta b)
               {
                   if(a.i == b.i)
                   {
                       if(a.v.equals(b.v)) return 0;
                       Random g = new Random();
                       int l = g.nextInt(2);
                       if (l == 0) l = -1;
                       return l;
                   }
                   return a.i - b.i;
               }
           });
           for (doTreeSeta x: toksyczneTrupy) tokszczynePom.add(x);
           Object[] t = tokszczynePom.toArray();
           int naPreferowanych = (int)Math.floor(ile * 0.8);
           int wpp = ile - naPreferowanych;
           int i = 0;
           int juzTrawy = 0;
           while (juzTrawy < naPreferowanych)
           {
               if (i >= t.length - 1) return;
               if(trawyWogole >= (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1)) return;
               Vector2d p = ((doTreeSeta)t[i]).v;
               if(trawnik.get(p) == null)
               {
                   Grass g = new Grass(p);
                   trawnik.put(p, g);
                   trawyWogole++;
                   juzTrawy++;
               }
               i++;
           }

           i = t.length - 1;
           juzTrawy = 0;
           while (juzTrawy < wpp)
           {
               if (i < 0) return;
               if(trawyWogole >= (kraniecMapy.x - poczatekMapy.x + 1) * (kraniecMapy.y - poczatekMapy.y + 1)) return;
               Vector2d p = ((doTreeSeta)t[i]).v;
               if(trawnik.get(p) == null)
               {
                   Grass g = new Grass(p);
                   trawnik.put(p, g);
                   trawyWogole++;
                   juzTrawy++;
               }
               i--;
           }

       }


    }
    public Vector2d losujVectorNaMapie()
    {
        Random generator = new Random();
        while (true)
        {
            try
            {
                Vector2d pp = new Vector2d(generator.nextInt(kraniecMapy.x + 1), generator.nextInt(kraniecMapy.y + 1));
                if (objectAt(pp) == null) return pp;
            }
            catch (Exception ex)
            {
                Utils.parseBoolean("false");
            }

        }
    }
    public Vector2d losujVectorNaMapieDlaZwierzecia()
    {
        Random generator = new Random();
        while (true)
        {
            try
            {
                Vector2d pp = new Vector2d(generator.nextInt(kraniecMapy.x + 1), generator.nextInt(kraniecMapy.y + 1));
                if (!(objectAt(pp) instanceof Animal)) return pp;
            }
            catch (Exception ex)
            {
                Utils.parseBoolean("false");
            }
        }
    }
    protected Vector2d losujVectorNaRowniku()
    {
        Random generator = new Random();
        while (true)
        {
            Vector2d pp = new Vector2d(generator.nextInt(kraniecRownika.x + 1),
                    generator.nextInt(kraniecRownika.y - pocztekRownika.y + 1) + pocztekRownika.y);
            if (trawnik.get(pp) == null) return pp;
        }
    }
    protected Vector2d losujVectorNaMappieAleNieNaRowniku()
    {
        Random generator = new Random();
        while (true)
        {
            Vector2d pp = new Vector2d(generator.nextInt(kraniecMapy.x + 1), generator.nextInt(kraniecMapy.y + 1));
            if (!(pp.follows( pocztekRownika) && pp.precedes(kraniecRownika)) && trawnik.get(pp) == null) return pp;
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
    public int smiercZwierzecia(Animal z, int dzien)
    {
        z.zabijGo(dzien);

        List<Integer> ttt = z.getWholeGenes();
        Integer pom = genotypy.remove(ttt);
        if(pom > 1) genotypy.put(ttt, pom - 1);

        usunZHaszMapy(z);
        if(toksyczneTrupy != null)
        {
            //System.out.println("-------------------------");
            int iPom = 0;
            doTreeSeta t = null;
            for(doTreeSeta x: toksyczneTrupy)
            {
                if(x.v.equals(z.getPosition()))
                {
                    iPom = x.i;
                    //System.out.println(x);
                    t = x;
                    break;
                }
            }
            toksyczneTrupy.remove(t);
            //System.out.println(Arrays.toString(toksyczneTrupy.toArray()));
            iPom++;
            toksyczneTrupy.add(new doTreeSeta(z.getPosition(), iPom));
            //System.out.println(Arrays.toString(toksyczneTrupy.toArray()));
        }
        return z.getWiek();
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
        System.out.println("genotypy");
        for (List<Integer> name: genotypy.keySet())
        {
            String key = Arrays.toString(name.toArray());
            String value = genotypy.get(name).toString();
            System.out.println(key + " " + value);
        }
        System.out.println("Kkoiec mapy");
    }
    protected boolean czyBylaWpreferowanych(Vector2d p)
    {
        if(pocztekRownika != null)
        {
            return  p.follows( pocztekRownika) && p.precedes(kraniecRownika);
        }
        return false;
    }
    public int iloscRosllin(){return trawnik.keySet().size();}
    public int iloscWolnychPol()
    {
        int i = 0;
        for (int x = 0; x <= kraniecMapy.x; x++)
            for (int y = 0; y <= kraniecMapy.y; y++) {
                try {
                    if (objectAt(new Vector2d(x, y)) == null) i++;
                } catch (Exception ex) {
                    Utils.parseBoolean("true");
                }
            }

        return i;
    }


}
