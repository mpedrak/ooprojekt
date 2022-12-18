package agh;

import java.util.Comparator;
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
                    if(a.i == b.i) return 1;
                    return a.i - b.i;
                }
            });
        }
        stworzTrawe(iloscTrawy);
    }
    public Vector2d newPosition(Vector2d oldPosition, Vector2d delta, Animal zwierz)
    {
        Vector2d pp = oldPosition.add(delta);
        if (pp.y > kraniecMapy.y || pp.y < 0 || pp.x < 0 || pp.x > kraniecMapy.x)
        {
            pp = losujVectorNaMapie();
            zwierz.changeEnergy(energiaTraconaPrzyTeleportacji);
        }
        if (trawnik.get(pp) != null)
        {
            if(czyBylaWpreferowanych(pp)) trawyNaPolachPreferowanych--;
            trawyWogole--;
            trawnik.remove(pp);
            zwierz.changeEnergy(energiaRoslin);
        }
        return pp;
    }

}
