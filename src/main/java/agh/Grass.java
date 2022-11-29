package agh;

import java.util.Objects;

public class Grass extends AbstractWorldMapElement
{
    public Grass(Vector2d p)
    {
        this.position = p;
    }
    public String toString()
    {
        return "*";
    }
    public boolean equals(Object other)
    {
        if (this == other)
            return true;
        if (!(other instanceof Grass))
            return false;
        Grass that = (Grass) other;
        return this.position.equals(that.position);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(position);
    }

    public String getPath()
    {
        return "src/main/resources/grass.png";
    }
}
