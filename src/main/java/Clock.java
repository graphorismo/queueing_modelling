import java.util.ArrayList;

public class Clock {

    interface ITickListener{
        void onTick(int tickNumber);
    }

    int current_tick = 0;

    ArrayList<ITickListener> tickListeners = new ArrayList<ITickListener>();

    void addOnTickListener(ITickListener listener){
        tickListeners.add(listener);
    }

    public void tick(){
        current_tick += 1;
        for(var listener : tickListeners)
            listener.onTick(current_tick);
    }

    public void runFor(int ticksAmount){
        for (int i = 0; i<ticksAmount; ++i){
            this.tick();
        }
    }
}
