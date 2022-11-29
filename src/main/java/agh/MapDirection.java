package agh;

public enum MapDirection
{
    N, NE, E, SE, S, SW, W, NW;

    public String toString()
    {
        return String.valueOf(this);
    }
    public MapDirection next()
    {
        MapDirection[] tab = values();
        return tab[(this.ordinal() + 1)  % 8];
    }
    public MapDirection prev()
    {
        MapDirection[] tab = values();
        return tab[(this.ordinal() + 7)  % 8];
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
