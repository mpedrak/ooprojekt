package agh;

public interface IPositionChangeObserver
{
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal z);
}
