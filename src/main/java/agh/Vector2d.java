package agh;

import java.util.Objects;

public class Vector2d
{
    final int x;
    final int y;

    public Vector2d(int x, int y)
    {
        this.x = x;
        this.y = y;

    }
    public String toString()
    {
        String x = String.valueOf(this.x);
        String y = String.valueOf(this.y);
        return "(" + x + ", " + y + ")";
    }
    public boolean precedes(Vector2d other)
    {
        if (this.x <= other.x && this.y <= other.y)
            return true;
        return false;
    }
    public boolean follows(Vector2d other)
    {
        if (this.x >= other.x && this.y >= other.y)
            return true;
        return false;
    }
    public Vector2d add(Vector2d other)
    {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }
    public Vector2d subtract(Vector2d other)
    {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }
    public Vector2d upperRight(Vector2d other)
    {
        int x = Math.max(this.x, other.x);
        int y = Math.max(this.y, other.y);
        return new Vector2d(x, y);
    }
    public Vector2d lowerLeft(Vector2d other)
    {
        int x = Math.min(this.x, other.x);
        int y = Math.min(this.y, other.y);
        return new Vector2d(x, y);
    }
    public Vector2d opposite()
    {
        return new Vector2d(-1 * this.x, -1 * this.y);
    }
    public boolean equals(Object other)
    {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        if (this.x == that.x && this.y == that.y)
            return true;
        return false;
    }
    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

}
