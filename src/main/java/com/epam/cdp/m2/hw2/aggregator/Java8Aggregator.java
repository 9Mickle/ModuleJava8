package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Java8Aggregator implements Aggregator {

    @Override
    public long sum(List<Integer> numbers) {
        return numbers.stream().mapToLong(a -> a).sum();
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        return words.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(e -> new Pair<String, Long>(e.getKey(), e.getValue()))
                .sorted((o1, o2) -> {
                    long compare = o2.getValue() - o1.getValue();
                    if (compare == 0) {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                    return (int) compare;
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        return words.stream()
                .map(String::toUpperCase)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(w -> w.getValue() > 1)
                .map(Map.Entry::getKey)
                .sorted((o1, o2) -> {
                    int compare = o1.length() - o2.length();
                    if (compare == 0) {
                        return o1.compareTo(o2);
                    }
                    return compare;
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
}