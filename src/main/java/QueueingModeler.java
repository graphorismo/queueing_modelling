import java.io.IOException;

public class QueueingModeler implements ILogged, ITimed
{
    Logger logger = null;
    private ProcessorsNest processorsNest;
    private Clock clock;
    private QueuesNest queuesNest;
    private ArrivalsSource arrivalsSource;

    @Override
    public void onTick(int tickNumber) {
        var arrivals = processorsNest.pollFinishedArrivals();
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void attachToClock(Clock clock) {
        clock.addOnTickListener(this);
    }

    public void setProcessorsNest(ProcessorsNest processorsNest) {
        this.processorsNest = processorsNest;
    }

    public void assembleQueueingSystem() {
        arrivalsSource.setLogger(logger);
        arrivalsSource.attachToClock(clock);

        queuesNest.setLogger(logger);
        queuesNest.attachToClock(clock);
        queuesNest.setArrivalsSource(arrivalsSource);

        processorsNest.setLogger(logger);
        processorsNest.attachToClock(clock);
        processorsNest.setQueueNest(queuesNest);
    }

    public void setClock(Clock clock) {
        this.clock = clock;
        attachToClock(clock);
    }

    public void runFor(int ticksAmount) {
        this.clock.runFor(ticksAmount);
    }

    public void writeLogToFile(String outputFileName) {
        try {
            this.logger.writeToFile(outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setQueuesNest(QueuesNest queuesNest) {
        this.queuesNest = queuesNest;
    }

    public void setArrivalsSource(ArrivalsSource arrivalsSource) {
        this.arrivalsSource = arrivalsSource;
    }
}
