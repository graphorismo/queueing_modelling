import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Logger{

    private ArrayList<String> logLines = new ArrayList<>();

    public void logg(String log){
        logLines.add(log);
    }

    void writeToFile(String fileName) throws IOException {
        var file = new FileWriter(fileName);
        for(var line : this.logLines)
            file.write(line+"\n");
    }
}
