package agh;

public class KulaZiemska extends AbstractWorldMap
{
    public KulaZiemska(int width, int height, int wersjaTrawy)
    {
        kraniecMapy = new Vector2d(width - 1, height - 1);
        if (wersjaTrawy == 0)
        {
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

    }
    public Vector2d newPosition(Vector2d oldPosition, Vector2d delta, Animal zwierz)
    {
        Vector2d pp = oldPosition.add(delta);
        if (pp.y > kraniecMapy.y || pp.y < 0)
            return oldPosition;
        if (pp.x > kraniecMapy.x)
            return new Vector2d(0, pp.y);
        if (pp.x < 0)
            return new Vector2d(kraniecMapy.x, pp.y);
        else
            return pp;
    }
}
