package parser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface DirectoryParser {
    boolean supports(Path path);
    int parse (Path path, Consumer<CityRecord> recordConsumer) throws IOException;
}