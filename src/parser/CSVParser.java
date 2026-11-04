package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class CSVParser implements DirectoryParser{


    @Override
    public boolean supports(Path path) {
        
        return path.getFileName().toString().toLowerCase().endsWith(".csv");
    } 
    @Override
    public void parse(Path path, Consumer<CityRecord> recordConsumer) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(path,StandardCharsets.UTF_8)){
            reader.readLine();
            String line;
            while ((line = reader.readLine())!= null) {
                if(line.isBlank()){
                    continue;
                }
                
                recordConsumer.accept(parseline(line));
            }
        }
        
    }
    private CityRecord parseline(String line) {
        
        throw new UnsupportedOperationException("Unimplemented method 'parseline'");
        return new CityRecord();
    }
    
    
}
