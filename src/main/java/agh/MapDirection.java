package agh;

public enum MapDirection
{
    NORTH, EAST, SOUTH, WEST ;

    public String toString()
    {
        switch(this)
        {
            case NORTH: return "Północ";
            case SOUTH: return "Południe";
            case WEST: return "Zachód";
            case EAST: return "Wschód";
        }
        return "";
    }
    public String toShortString() // do wypisania krotkiego
    {
        switch(this)
        {
            case NORTH: return "^";
            case SOUTH: return "v";
            case WEST: return "<";
            case EAST: return ">";
        }
        return "";
    }
    public MapDirection next()
    {
        MapDirection[] tab = values();
        return tab[(this.ordinal() + 1)  % 4];
    }
    public MapDirection prev()
    {
        MapDirection[] tab = values();
        return tab[(this.ordinal() + 3)  % 4];
    }
    public Vector2d toUnitVector()
    {
        switch(this) {
            case NORTH:
                return new Vector2d(0, 1);
            case SOUTH:
                return new Vector2d(0, -1);
            case WEST:
                return new Vector2d(-1, 0);
            case EAST:
                return new Vector2d(1, 0);
        }
        return new Vector2d(7, 7);
    }
}
