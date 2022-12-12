package agh;

public class KulaZiemska extends AbstractWorldMap
{
    public KulaZiemska(int width, int height, int iloscTrawy , int energiaRoslin)
    {
        this.energiaRoslin = energiaRoslin;
        kraniecMapy = new Vector2d(width - 1, height - 1);
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
            trawnik.remove(pp);
            zwierz.changeEnergy(energiaRoslin);
        }
        return pp;
    }
}
