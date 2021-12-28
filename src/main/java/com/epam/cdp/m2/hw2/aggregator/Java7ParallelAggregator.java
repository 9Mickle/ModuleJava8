package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Java7ParallelAggregator implements Aggregator {

    /**
     * Разбиваем задачу на 2 потока. Первый - res1 сортирует первую половину листа,
     * Второй - res2 сортирует вторую половину листа.
     * В конце суммируем результат.
     */
    @Override
    public int sum(List<Integer> numbers) {

        int result = 0;
        Future<Integer> res1 = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() {
                int res = 0;
                for (int i = 0; i < numbers.size() / 2; i++) {
                    res += numbers.get(i);
                }
                return res;
            }
        });

        Future<Integer> res2 = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() {
                int res = 0;
                for (int i = numbers.size() / 2; i < numbers.size(); i++) {
                    res += numbers.get(i);
                }
                return res;
            }
        });

        try {
            new Thread((Runnable) res1).start();
            new Thread((Runnable) res2).start();
            result = res1.get() + res2.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Здесь я не придумал как разбить задачу на потоки.
     * Здесь таже реализация как в Java7Aggregator.
     */
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

    /**
     * Разбиваем задачу на 3 потока.
     * Первый - upCaseFirstHalf берет первую половину листа и переводит его слова в верхний регистр.
     * Второй - upCaseSecondHalf берет вторую половину листа.
     * Третий - getDuplicates ждёт выполнения работы первых двух потоков и берет из листа слова, которые встречаются больше 1 раза.
     * Сортируем лист.
     * Выдаем результат с указанным лимитом.
     */
    @Override
    public List<String> getDuplicates(List<String> words, long limit) {

        Future<List<String>> upCaseFirstHalf = new FutureTask<>(new Callable<List<String>>() {
            @Override
            public List<String> call() {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < words.size() / 2; i++) {
                    list.add(words.get(i).toUpperCase());
                }
                return list;
            }
        });

        Future<List<String>> upCaseSecondHalf = new FutureTask<>(new Callable<List<String>>() {
            @Override
            public List<String> call() {
                List<String> list = new ArrayList<>();
                for (int i = words.size() / 2; i < words.size(); i++) {
                    list.add(words.get(i).toUpperCase());
                }
                return list;
            }
        });

        Future<List<String>> getDuplicates = new FutureTask<>(new Callable<List<String>>() {
            @Override
            public List<String> call() throws ExecutionException, InterruptedException {
                List<String> listUp = new ArrayList<>(upCaseFirstHalf.get());
                listUp.addAll(upCaseSecondHalf.get());
                Set<String> set = new HashSet<>();
                for (String word: listUp) {
                    if (Collections.frequency(listUp, word) > 1) {
                        set.add(word);
                    }
                }
                listUp = new ArrayList<>(set);
                return listUp;
            }
        });

        new Thread((Runnable) upCaseFirstHalf).start();
        new Thread((Runnable) upCaseSecondHalf).start();
        new Thread((Runnable) getDuplicates).start();


        List<String> sortedList = null;
        try {
            sortedList = new ArrayList<>(getDuplicates.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

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

        List<String> limitList = new ArrayList<>();
        if (limit > sortedList.size()) {
            limitList.addAll(sortedList);
        } else {
            for (int i = 0; i < limit; i++) {
                limitList.add(sortedList.get(i));
            }
        }
        return limitList;
    }
}
