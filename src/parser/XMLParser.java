package parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
/*
    (1)<><><><><><><><><><><><><><><><><><><><>(2)
*/
public class XMLParser implements DirectoryParser {

    @Override
    public boolean supports(Path path) {
        return path.getFileName().toString().toLowerCase().endsWith(".xml");
    }

    @Override
    public void parse(Path path, Consumer<CityRecord> recordConsumer) throws IOException {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        try (InputStream input = Files.newInputStream(path)){
            XMLStreamReader reader = factory.createXMLStreamReader(input,StandardCharsets.UTF_8.name());
            try{
                while (reader.hasNext()) {
                    int event = reader.next();
                    if(event == XMLStreamConstants.START_ELEMENT
                        && "item".equals(reader.getLocalName())){
                            recordConsumer.accept(readItem(reader));
                        }
                }
            }
            finally{
                reader.close();
            }
        }
        catch(XMLStreamException exception){
            throw new IOException("Cannot parse XML");
        }        
    }

    private CityRecord readItem(XMLStreamReader reader) throws IOException {
        String city = reader.getAttributeValue(null,"city");
        String street = reader.getAttributeValue(null,"street");
        String house = reader.getAttributeValue(null,"house");
        String S_floor = reader.getAttributeValue(null,"floor");
        int line = reader.getLocation().getLineNumber();
        
        if(city == null || street == null || house == null || S_floor == null){
            throw new IOException("Not enough args in line "+ line);
        }
        
        int floor;
        try{
            floor = Integer.parseInt(S_floor.trim());
        }
        catch(NumberFormatException exception){
            throw new IOException("Floor is not integer in line "+ line);
        }

            
            


        return new CityRecord(city,street,house,floor);
    }
}
