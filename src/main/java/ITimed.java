public interface ITimed extends Clock.ITickListener
{
    void attachToClock(Clock clock);
}
