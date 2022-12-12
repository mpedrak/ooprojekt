package agh;

import java.util.Random;

public class PiekielnyPortal extends AbstractWorldMap
{
    public PiekielnyPortal(int width, int height)
    {
        ;
    }
    public Vector2d newPosition(Vector2d oldPosition, Vector2d delta, Animal zwierz)
    {
        Vector2d pp = oldPosition.add(delta);
        if (pp.follows(poczatekMapy) && pp.precedes(kraniecMapy)) return pp;
        else
        {
            zwierz.changeEnergy(7); // ! wykrzyknik
            return losujVectorNaMapie();
        }
    }
}
