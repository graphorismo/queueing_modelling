import java.util.ArrayDeque;
import java.util.ArrayList;

public class QueuesNest implements ITimed, ILogged {

    private final int queuesMaxLength;
    ArrivalsSource arrivalsSource = null;
    Logger logger = null;
    ArrayList<ArrayDeque<Arrival>> queues = new ArrayList<ArrayDeque<Arrival>>();
    private ArrayList<Arrival> rejectedArrivals  = new ArrayList<>();


    public QueuesNest(int queuesAmount, int queuesMaxLength){
        for (int i =0; i < queuesAmount; ++i){
            queues.add(new ArrayDeque<Arrival>());
        }
        this.queuesMaxLength = queuesMaxLength;
    }

    ArrayList<Arrival> pollRejectedArrivals(){
        var arrivals = (ArrayList<Arrival>) this.rejectedArrivals.clone();
        rejectedArrivals.clear();
        return arrivals;
    }

    @Override
    public void onTick(int tickNumber) {
        var arrivals = arrivalsSource.poll_arrivals();
        if(arrivals.size() > 0){
            for(var arrival : arrivals){
                var shortestQueue = getShortestQueue();
                if (shortestQueue.size() <= queuesMaxLength){
                    arrival.queueEnterTime = tickNumber;
                    shortestQueue.add(arrival);
                }
                else
                {
                    rejectedArrivals.add(arrival);
                }
            }
            logger.logg("Tick "+tickNumber+". "
                            + "Queuer "+this.hashCode()+". "
                            + "Poll " + arrivals.size() + " arrivals"
                            + "from Source " + arrivalsSource+ " ." );
        }
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setQueuesNumber(int number){
        queues.clear();
        for (int i =0; i < number; ++i){
            this.queues.add(new ArrayDeque<>());
        }
    }

    private ArrayDeque<Arrival> getShortestQueue() {
        ArrayDeque<Arrival> shortestQueue = queues.get(0);
        for(var deque : queues){
            if(deque.size() < shortestQueue.size())
                shortestQueue = deque;
        }
        return shortestQueue;
    }

    public Arrival pollFromQueueUnderId(int number){
        return queues.get(number).poll();
    }

    @Override
    public void attachToClock(Clock clock) {
        clock.addOnTickListener(this);
    }

    public void setArrivalsSource(ArrivalsSource source) {
        this.arrivalsSource = source;
    }
}
