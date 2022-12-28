package agh;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeSet;

public class PiekielnyPortal extends AbstractWorldMap
{
    public PiekielnyPortal(int width, int height, int iloscTrawy , int energiaRoslin, boolean czyRownik, int energiaTraconaPrzyTeleportacji)
    {
        this.energiaRoslin = energiaRoslin;
        this.energiaTraconaPrzyTeleportacji = energiaTraconaPrzyTeleportacji;
        kraniecMapy = new Vector2d(width - 1, height - 1);
        if (czyRownik)
        {
            int y1 = (int)Math.floor(height * 0.4);
            int y2 = Math.max((int)Math.floor(height * 0.2) + y1 - 1, y1);
            if (y2 - y1 + 1 < height * 0.2) {y1++; y2++;}
            pocztekRownika = new Vector2d(0, y1);
            kraniecRownika = new Vector2d(width - 1, y2);
        }
        else
        {
            toksyczneTrupy = new TreeSet<>(new Comparator<doTreeSeta>() {
                public int compare (doTreeSeta a, doTreeSeta b)
                {
                    if(a.i == b.i)
                    {
                        if(a.v.x == b.v.x) return a.v.y - b.v.y;
                        else return a.v.x - b.v.x;
                    }
                    return a.i - b.i;
                }
            });
            for(int ix = 0; ix <= kraniecMapy.x; ix++)
                for(int iy = 0; iy <= kraniecMapy.y; iy++)
                    toksyczneTrupy.add(new doTreeSeta(new Vector2d(ix, iy), 0));
           // System.out.println(Arrays.toString(toksyczneTrupy.toArray()));
        }
        stworzTrawe(iloscTrawy);
    }
    public Vector2d newPosition(Vector2d oldPosition, Vector2d delta, Animal zwierz)
    {
        Vector2d pp = oldPosition.add(delta);
        if (pp.y > kraniecMapy.y || pp.y < 0 || pp.x < 0 || pp.x > kraniecMapy.x)
        {
            pp = losujVectorNaMapieDlaZwierzecia();
            zwierz.changeEnergy(energiaTraconaPrzyTeleportacji);
        }
        if (trawnik.get(pp) != null)
        {
            zwierz.zjadlRolsine();
            if(czyBylaWpreferowanych(pp)) trawyNaPolachPreferowanych--;
            //System.out.println(trawyNaPolachPreferowanych);
            trawyWogole--;
            trawnik.remove(pp);
            zwierz.changeEnergy(energiaRoslin);
        }
        return pp;
    }

}
