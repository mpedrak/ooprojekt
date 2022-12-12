package agh;

public enum MapDirection
{
    N, NE, E, SE, S, SW, W, NW;

    public String toString()
    {
        switch(this)
        {
            case N: return "N";
            case S: return "S";
            case W: return "W";
            case E: return "E";
            case NW: return "NW";
            case SW: return "SW";
            case SE: return "SE";
            case NE: return "NE";
        }
        return "";
    }
    public MapDirection turnBy(int ile)
    {
        MapDirection[] tab = values();
        return tab[(this.ordinal() + ile)  % 8];
    }
    public Vector2d toUnitVector()
    {
        switch(this)
        {
            case N:
                return new Vector2d(0, 1);
            case S:
                return new Vector2d(0, -1);
            case W:
                return new Vector2d(-1, 0);
            case E:
                return new Vector2d(1, 0);
            case NE:
                return new Vector2d(1, 1);
            case SE:
                return new Vector2d(1, -1);
            case NW:
                return new Vector2d(-1, 1);
            case SW:
                return new Vector2d(-1, -1);
        }
        return new Vector2d(7, 7);
    }
}
