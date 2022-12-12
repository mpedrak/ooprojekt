package agh;

public class KulaZiemska extends AbstractWorldMap
{
    public KulaZiemska(int width, int height, int iloscTrawy)
    {
        kraniecMapy = new Vector2d(width - 1, height - 1);
        stworzTrawe(iloscTrawy);
    }
    public Vector2d newPosition(Vector2d oldPosition, Vector2d delta, Animal zwierz)
    {
        Vector2d pp = oldPosition.add(delta);
        if (pp.y > kraniecMapy.y || pp.y < 0) return oldPosition;
        if (pp.x > kraniecMapy.x) return new Vector2d(0, pp.y);
        if (pp.x < 0) return new Vector2d(kraniecMapy.x, pp.y);
        else return pp;
    }
}
