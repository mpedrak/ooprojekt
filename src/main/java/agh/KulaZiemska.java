package agh;

import java.util.*;

public class KulaZiemska extends AbstractWorldMap
{
    public KulaZiemska(int width, int height, int iloscTrawy , int energiaRoslin, boolean czyRownik)
    {
        this.energiaRoslin = energiaRoslin;
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
        }
        stworzTrawe(iloscTrawy);
    }
    public Vector2d newPosition(Vector2d oldPosition, Vector2d delta, Animal zwierz)
    {
        Vector2d pp = oldPosition.add(delta);
        if (pp.y > kraniecMapy.y || pp.y < 0) return oldPosition;
        if (pp.x > kraniecMapy.x) pp = new Vector2d(0, pp.y);
        else if (pp.x < 0) pp = new Vector2d(kraniecMapy.x, pp.y);
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
