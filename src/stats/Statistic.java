package stats;

import java.util.Map;

import parser.CityRecord;

public record Statistic(Map<CityRecord,Integer> dublicates, Map<String,int[]> FloorsbyCity, long millis) {
    
    
}
