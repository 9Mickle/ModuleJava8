package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.*;

public class Java7Aggregator implements Aggregator {

    @Override
    public long sum(List<Integer> numbers) {
        long result = 0;
        for (int number : numbers) {
            result += number;
        }
        return result;
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        List<Pair<String, Long>> list = new ArrayList<>();
        for (String word: words) {
            int count = Collections.frequency(words, word);
            list.add(new Pair<>(word, (long) count));
        }

        Set<Pair<String, Long>> set = new HashSet<>(list);
        List<Pair<String, Long>> sortedList = new ArrayList<>(set);

        Collections.sort(sortedList, new Comparator<Pair<String, Long>>() {
            @Override
            public int compare(Pair<String, Long> o1, Pair<String, Long> o2) {
                long compare = o2.getValue() - o1.getValue();
                if (compare == 0) {
                    return o1.getKey().compareTo(o2.getKey());
                }
                return (int) compare;
            }
        });

        List<Pair<String, Long>> result = new ArrayList<>();
        if (limit > sortedList.size()) {
            result.addAll(sortedList);
        } else {
            for (int i = 0; i < limit; i++) {
                result.add(sortedList.get(i));
            }
        }
        return result;
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        List<String> listUp = new ArrayList<>(); //Для перевода слов в верхний регистр.
        for (String word: words) {
            listUp.add(word.toUpperCase());
        }

        Set<String> set = new HashSet<>(); //Взятие дубликатов в одиночном экземпляре.
        for (String word: listUp) {
            if (Collections.frequency(listUp, word) > 1) {
                set.add(word);
            }
        }

        List<String> sortedList = new ArrayList<>(set);
        Collections.sort(sortedList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                long compare = o1.length() - o2.length();
                if (compare == 0) {
                    return o1.compareTo(o2);
                }
                return (int) compare;
            }
        });

        List<String> result = new ArrayList<>();
        if (limit > sortedList.size()) {
            result.addAll(sortedList);
        } else {
            for (int i = 0; i < limit; i++) {
                result.add(sortedList.get(i));
            }
        }
        return result;
    }
}
