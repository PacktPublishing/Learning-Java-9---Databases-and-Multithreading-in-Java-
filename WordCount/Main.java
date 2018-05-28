package Product4.WordCount;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String args[]) throws IOException, InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        int threads = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        final ExecutorCompletionService<Map<String, Long>> completionService = new ExecutorCompletionService<>(executorService);

        List<List<String>> listOfLists = getSplitLists(threads);

        Map<String, Long> allCounts = executeWork(completionService, listOfLists);

//        LinkedHashMap<String, Long> sortedMap = allCounts.entrySet().stream()
//            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
//                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Total execution time is " + elapsedTime + " ms");

        executorService.shutdown();
    }

    private static Map<String, Long> executeWork(ExecutorCompletionService<Map<String, Long>> completionService, List<List<String>> listOfLists) throws InterruptedException, ExecutionException {
        listOfLists.forEach(sublist -> completionService.submit(new WordCounter(sublist)));

        Map<String, Long> allCounts = new HashMap<>();
        for (int i = 0; i < listOfLists.size(); i++) {
            Map<String, Long> newCounts = completionService.take().get();
            newCounts.forEach((k, v) -> allCounts.merge(k, v, Long::sum));
        }
        return allCounts;
    }

    private static List<List<String>> getSplitLists(int threads) throws FileNotFoundException {
        URL file_path = Product4.Hibernate.Main.class.getClassLoader().getResource("words.txt");

        String content = new Scanner(new File(file_path.getPath())).useDelimiter("\\Z").next();
        List<String> lines = Arrays.asList(content.split("\n"));

        return splitList(lines, lines.size() / threads);
    }


    private static List<List<String>> splitList(List<String> originalList, int partitionSize) {
        List<List<String>> partitions = new LinkedList<>();
        for (int i = 0; i < originalList.size(); i += partitionSize) {
            partitions.add(originalList.subList(i,
                Math.min(i + partitionSize, originalList.size())));
        }

        return partitions;
    }
}
