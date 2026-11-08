package parser;

import java.nio.file.Path;
import java.util.List;

public class ParserFactory {
    private final List<DirectoryParser> parsers = List.of(new CSVParser(), new XMLParser());
    
    public DirectoryParser getParser(Path path){
        for(DirectoryParser parser: parsers){
            if(parser.supports(path)){
                return parser;
            }
        }
        throw new IllegalArgumentException("Unsupported type: "+path);
    }
}
