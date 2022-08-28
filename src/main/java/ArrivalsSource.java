import java.util.ArrayList;
import java.util.Random;

public class ArrivalsSource implements ITimed, ILogged
{
    float spawnChance;
    Random random_generator = new Random();
    Logger logger = null;
    ArrayList<Arrival> spawned_arrivals = new ArrayList<>();

    public ArrivalsSource(float spawnChance){
        this.spawnChance = spawnChance;
    }

    @Override
    public void attachToClock(Clock clock) {
        clock.addOnTickListener(this);
    }

    @Override
    public void onTick(int tickNumber) {
        var spawn_roll = random_generator.nextInt(1000)/1000f;
        if(spawn_roll < spawnChance) {
            var arrival = new Arrival();
            arrival.spawnTime = tickNumber;
            spawned_arrivals.add(arrival);
            logger.logg("Tick " + tickNumber +". "
                    +"Source "+this.hashCode()+". "
                    + "Spawn arrival " + arrival.hashCode()+". ");
        }
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    ArrayList<Arrival> poll_arrivals(){
        var arrivals = (ArrayList<Arrival>) this.spawned_arrivals.clone();
        this.spawned_arrivals.clear();
        return arrivals;
    }
}
