package stats;

import java.util.Map;

import parser.CityRecord;

public record Statistics(Map<CityRecord,Integer> duplicates, Map<String,int[]> FloorsbyCity, long millis) {
    
    
}
