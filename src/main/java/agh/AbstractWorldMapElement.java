package agh;

public abstract class AbstractWorldMapElement implements IMapElement
{
    protected Vector2d position;
    public Vector2d getPosition()
    {
        return position;
    }
}
