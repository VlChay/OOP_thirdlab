package ui;

import parser.CityRecord;
import stats.Statistics;
import stats.StatisticsServies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ConsoleUI {

    private final StatisticsServies service = new StatisticsServies();
    private final BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in, StandardCharsets.UTF_8));

    public void start() {
        while (true) {
            System.out.print("Введите путь к CSV/XML (exit — выход): ");
            String input;

            try {
                input = reader.readLine();
            } catch (IOException exception) {
                System.out.println("Ошибка чтения ввода: " + exception.getMessage());
                continue;
            }

            if (input == null) {
                System.out.println();
                break;
            }

            input = input.trim();

            if (input.isEmpty()) {
                continue;
            }

            if (isExitCommand(input)) {
                break;
            }

            try {
                Statistics statistic = service.process(Path.of(input));
                printStatistics(statistic);
            } catch (Exception exception) {
                System.out.println("Ошибка обработки: " + exception.getMessage());
            }
        }

        System.out.println("Выход.");
    }

    private boolean isExitCommand(String input) {
        return input.equalsIgnoreCase("exit")
                || input.equalsIgnoreCase("quit")
                || input.equalsIgnoreCase("q");
    }

    private void printStatistics(Statistics statistic) {
        System.out.println();
        printDuplicates(statistic);
        System.out.println();
        printFloors(statistic);
        System.out.println();
        printTime(statistic);
        System.out.println();
    }

    private void printDuplicates(Statistics statistic) {
        Map<CityRecord, Integer> duplicates = statistic.duplicates();

        if (duplicates.isEmpty()) {
            System.out.println("Дубликатов нет.");
            return;
        }

        List<Map.Entry<CityRecord, Integer>> entries = new ArrayList<>(duplicates.entrySet());
        entries.sort(Comparator
                .comparingInt((Map.Entry<CityRecord, Integer> entry) -> entry.getValue())
                .reversed()
                .thenComparing(entry -> entry.getKey().city())
                .thenComparing(entry -> entry.getKey().street())
                .thenComparing(entry -> entry.getKey().house())
                .thenComparingInt(entry -> entry.getKey().floor()));

        System.out.println("Дубликаты:");
        for (Map.Entry<CityRecord, Integer> entry : entries) {
            CityRecord record = entry.getKey();
            System.out.printf("%dx %s | %s | %s | %d%n",
                    entry.getValue(),
                    record.city(),
                    record.street(),
                    record.house(),
                    record.floor());
        }
    }

    private void printFloors(Statistics statistic) {
        Map<String, int[]> floorsByCity = statistic.FloorsbyCity();

        if (floorsByCity.isEmpty()) {
            System.out.println("Нет данных по городам.");
            return;
        }

        List<String> cities = new ArrayList<>(floorsByCity.keySet());
        cities.sort(String::compareTo);

        System.out.println("Здания по этажам:");
        for (String city : cities) {
            int[] floors = floorsByCity.get(city);
            System.out.printf("%s: 1-эт = %d | 2-эт = %d | 3-эт = %d | 4-эт = %d | 5-эт = %d |%n",
                    city,
                    floors[0],
                    floors[1],
                    floors[2],
                    floors[3],
                    floors[4]);
        }
    }

    private void printTime(Statistics statistic) {
        System.out.printf("Время обработки: %d мс%n", statistic.millis());
    }
}
