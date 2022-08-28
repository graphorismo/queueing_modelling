public class App {

    public static void main(String[] args) {
        var configs = new Configs("configs.txt");
        var queueingModeller = new QueueingModeler();
        queueingModeller.setLogger(new Logger());
        queueingModeller.setClock(new Clock());
        queueingModeller.setArrivalsSource(new ArrivalsSource(configs.spawnChance));
        queueingModeller.setQueuesNest(new QueuesNest(configs.queuesAmount, configs.queuesMaxLength));
        queueingModeller.setProcessorsNest(new ProcessorsNest(configs.processorsAmount, configs.processTime));
        queueingModeller.assembleQueueingSystem();
        queueingModeller.runFor(configs.ticksAmount);
        queueingModeller.writeLogToFile(configs.outputFileName);
    }
}
