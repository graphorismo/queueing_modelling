import java.util.ArrayList;

public class ProcessorsNest implements ITimed, ILogged{

    Logger logger = null;
    QueuesNest queueNest = null;
    ArrayList<Processor> processors = new ArrayList<>();
    ArrayList<Arrival> finishedArrivals = new ArrayList<>();
    private ArrayList<Arrival> rejectedArrivals = new ArrayList<>();

    public ProcessorsNest(int processorsAmount,
                          int processTime)
    {
        for(int i =0; i<processorsAmount; ++i){
            processors.add(new Processor(processTime));
        }
    }

    @Override
    public void onTick(int tickNumber) {
        for(int i=0, processorsAmount = processors.size(); i < processorsAmount; ++i){
            var processor = processors.get(i);
            if(processor.isStandingBy == true)
            {
                var arrival = queueNest.pollFromQueueUnderId(i);
                if (arrival != null){
                    arrival.processorEnterTime = tickNumber;
                    processor.startHandlingArrival(arrival);
                    logger.logg("Tick "+tickNumber+". "
                            +"ProcessorsNest "+this.hashCode()+". "
                            +"Push arrival "+arrival.hashCode()+" into processor N "+i+". ");
                }
            }
            else if (processor.isHoldingFinishedArrival == true)
            {
                var arrival = processor.pollFinishedArrival();
                arrival.processorLeaveTime=tickNumber;
                finishedArrivals.add(arrival);
                logger.logg("Tick "+tickNumber+". "
                        +"ProcessorsNest "+this.hashCode()+". "
                        +"Pop arrival "+arrival.hashCode()
                        +" from processor "+processor.hashCode()+". ");
            }

        }
        this.rejectedArrivals = queueNest.pollRejectedArrivals();
    }

    ArrayList<Arrival> pollRejectedArrivals(){
        var arrivals = (ArrayList<Arrival>) this.rejectedArrivals.clone();
        rejectedArrivals.clear();
        return arrivals;
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void attachToClock(Clock clock) {
        clock.addOnTickListener(this);
    }

    public void setQueueNest(QueuesNest queueNest)
    {
        this.queueNest = queueNest;
    }

    public ArrayList<Arrival> pollFinishedArrivals() {
        var arrivals = (ArrayList<Arrival>) finishedArrivals.clone();
        finishedArrivals.clear();
        return arrivals;
    }
}
