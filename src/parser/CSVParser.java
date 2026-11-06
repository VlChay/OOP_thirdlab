package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
    private CityRecord parseline(String line) throws IOException {
        //throw new UnsupportedOperationException("Unimplemented method 'parseline'");
        List<String> fields = new ArrayList<String>();
        StringBuilder cur_field = new StringBuilder();
        boolean in_quotes = false;

        for(int i = 0; i < line.length(); i++){
            char symb = line.charAt(i);
            if (symb == '"'){
                if((i+1) < line.length() && line.charAt(i+1) == '"'){
                    cur_field.append(symb);
                    i++;
                    continue;
                }
                else{in_quotes = !in_quotes;}
            }
            else if(symb == ';' && !in_quotes){
                fields.add(cur_field.toString());
                cur_field.setLength(0);
            }
            else{
                cur_field.append(symb);
            }
        }
        fields.add(cur_field.toString());
        if(fields.size() > 4){
            //хз наверное если не ломает обработку то можно и не вызывать
            //throw new IOException("Ожидалось 4 поля, а получено " + fields.size() + " [ " + line + " ] \n"); 
        }
        if(fields.size() < 4){
            throw new IOException("Not enough args"); 
        }
        
        int floor;
        try {
            floor = Integer.parseInt(fields.get(3).trim());
        }
        catch(NumberFormatException exception){
            throw new IOException("Floor is not integer [ "+ fields.get(3)+" ] "+exception);
        }
        return new CityRecord(fields.get(0),fields.get(1),fields.get(2),floor);
    }
    
    
}
