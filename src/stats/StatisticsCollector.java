package stats;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import parser.CityRecord;

public class StatisticsCollector implements Consumer<CityRecord>{
    private final Map<CityRecord,Integer> occurrecenes = new HashMap<>();
    private final Map<String,int[]> floorsByCity = new HashMap<>();
    @Override 
    public void accept(CityRecord record){
        occurrecenes.merge(record, 1, Integer::sum);

        int[] floors = floorsByCity.computeIfAbsent(record.city(), c -> new int[5]);
        int floor = record.floor();
        if(floor >=1 && floor <=5){
            floors[floor-1]++;
        }

    }

    public Statistics toStatistic(long millis){
        Map<CityRecord,Integer> duplicates = new HashMap<>();

        for(Map.Entry<CityRecord,Integer> entry: occurrecenes.entrySet()){
            if(entry.getValue() > 1){
                duplicates.put(entry.getKey(), entry.getValue());
            }
        }

        Map<String,int[]> floorsCopy = new HashMap<>();
        for(Map.Entry<String,int[]> entry : floorsByCity.entrySet()){
            floorsCopy.put(entry.getKey(), entry.getValue().clone());
        }


        return new Statistics(duplicates,floorsCopy,millis);
    }
}
