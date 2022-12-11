package agh;

import java.util.Random;

public class PiekielnyPortal extends AbstractWorldMap
{
    public PiekielnyPortal(int width, int height)
    {
        kraniecMapy = new Vector2d(width - 1, height - 1);
        int wysokoscRownika = (int)(0.2 * (kraniecMapy.y + 1));
        if ((wysokoscRownika % 2 == 0 && (kraniecMapy.y + 1) % 2 == 0) ||
                (wysokoscRownika % 2 == 0 && (kraniecMapy.y + 1) % 2 == 1))
        {
            poczatekRownika = new Vector2d(0, (kraniecMapy.y + 1) / 2 - wysokoscRownika / 2);
            kraniecRownika = new Vector2d(kraniecMapy.x, (kraniecMapy.y + 1) / 2 + wysokoscRownika / 2);
        }
        else
        {
            poczatekRownika = new Vector2d(0, (kraniecMapy.y + 1) / 2 - wysokoscRownika / 2 + 1);
            kraniecRownika = new Vector2d(kraniecMapy.x, (kraniecMapy.y + 1) / 2 + wysokoscRownika / 2);
        }
    }
    public Vector2d newPosition(Vector2d oldPosition, Vector2d delta, Animal zwierz)
    {
        Vector2d pp = oldPosition.add(delta);
        if (pp.follows(poczatekMapy) && pp.precedes(kraniecMapy))
            return pp;
        else
        {
            zwierz.changeEnergy(7); // ! wykrzyknik
            Random generator = new Random();
            while (true)
            {
                Vector2d pp2 = new Vector2d(generator.nextInt(kraniecMapy.x), generator.nextInt(kraniecMapy.y));
                if (!pp2.equals(oldPosition))
                    return pp2;
            }
        }
    }
}
