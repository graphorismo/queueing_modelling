public class Processor implements ILogged, ITimed{
    public boolean isStandingBy = true;
    public boolean isHoldingFinishedArrival = false;
    public boolean isProcessing = false;
    Arrival processedArrival = null;
    private int currentTime = 0;
    private int processEachTime = 0;
    private int processStartTime = 0;
    private Logger logger;

    Processor(int processEachTime){
        this.processEachTime = processEachTime;
    }

    public void startHandlingArrival(Arrival arrival) {
        processedArrival = arrival;
        processStartTime = currentTime;
    }

    public Arrival pollFinishedArrival() {
        var arrival = processedArrival;
        if(isHoldingFinishedArrival){
            isHoldingFinishedArrival = false;
            isStandingBy = true;
            processedArrival = null;
        }
        return arrival;
    }

    @Override
    public void onTick(int tickNumber) {
        currentTime=tickNumber;
        if(isProcessing == true
                && currentTime - processStartTime == processEachTime)
        {
            isProcessing = false;
            isHoldingFinishedArrival = true;
            processedArrival.processFinishTime=tickNumber;
            logger.logg("Tick "+tickNumber+". "
                    +"Processor "+this.hashCode() + ". "
                    +"Finished processing arrival "+processedArrival.hashCode()+". ");
        }
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void attachToClock(Clock clock) {
        clock.addOnTickListener(this);
    }
}
