package Product4.WordCount;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WordCounter implements Callable<Map<String, Long>> {
    private final List<String> lineList;

    WordCounter(List<String> lineList) {
        this.lineList = lineList;
    }

    @Override
    public Map<String, Long> call() {
        long startTime = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();

        Map<String, Long> results = lineList.stream()
            .filter(line -> !line.equals(""))
            .flatMap(line -> Arrays.stream(line.split(" ")))
            .map(word -> word.replaceAll("[^\\w]", ""))
            .filter(word -> !word.equals(""))
            .filter(word -> word.length() > 1)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(threadName + "Finished work in " + elapsedTime + " ms");

        return results;
    }
}
