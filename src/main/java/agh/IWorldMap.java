package agh;

public interface IWorldMap
{
    boolean place(Animal animal);
    Object objectAt(Vector2d position);
    Vector2d newPosition(Vector2d oldPosition, Vector2d delta, Animal zwierz);
    Vector2d losujVectorNaMapie();
}