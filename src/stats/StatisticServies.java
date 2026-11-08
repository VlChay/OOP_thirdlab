package stats;

import java.io.IOException;
import java.nio.file.Path;

import parser.DirectoryParser;
import parser.ParserFactory;

public class StatisticServies {
    private final ParserFactory parserFactory = new ParserFactory();

    public Statistic process (Path path) throws IOException{
        DirectoryParser parser = parserFactory.getParser(path); 
        StatisticCollector collector = new StatisticCollector();

        long start = System.currentTimeMillis();
        parser.parse(path, collector);
        long end = System.currentTimeMillis() - start;
        
        return collector.toStatistic(end);
    }
    
}