package stats;

import java.io.IOException;
import java.nio.file.Path;

import parser.DirectoryParser;
import parser.ParserFactory;

public class StatisticsServies {
    private final ParserFactory parserFactory = new ParserFactory();

    public Statistics process (Path path) throws IOException{
        DirectoryParser parser = parserFactory.getParser(path); 
        StatisticsCollector collector = new StatisticsCollector();

        long start = System.currentTimeMillis();
        int skipped = parser.parse(path, collector);
        long end = System.currentTimeMillis() - start;
        
        return collector.toStatistic(end,skipped);
    }
    
}