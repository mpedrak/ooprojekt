package agh;

public interface IWorldMap
{
    boolean place(Animal animal);
    Object objectAt(Vector2d position);
    Vector2d newPosition(Vector2d position);
}