package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.*;

public class Java7Aggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {
        int result = 0;
        for (int number : numbers) {
            result += number;
        }
        return result;
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        List<Pair<String, Long>> list = new ArrayList<>();

        /**
         * Считаем сколько раз каждое слово встречается в листе.
         */
        for (String word: words) {
            int count = Collections.frequency(words, word);
            list.add(new Pair<>(word, (long) count));
        }

        /**
         * Удаляем из листа дубликаты.
         */
        Set<Pair<String, Long>> set = new HashSet<>(list);

        /**
         * Преобразовываем set обратно в лист для далнейшей сортировки.
         */
        List<Pair<String, Long>> sortedList = new ArrayList<>(set);

        /**
         * Сортируем лист.
         */
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

        /**
         * Создаем лист с указнным лимитом и возвращаем результат.
         */
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
        List<String> listUp = new ArrayList<>();

        /**
         * Переводим слова в верхний регистр.
         */
        for (String word: words) {
            listUp.add(word.toUpperCase());
        }


        /**
         * Добавляем в set только те слова, которые встречаются более 1 раза.
         */
        Set<String> set = new HashSet<>();
        for (String word: listUp) {
            if (Collections.frequency(listUp, word) > 1) {
                set.add(word);
            }
        }

        /**
         * Преобразовываем set в лист для дальнешей сортировки.
         */
        List<String> sortedList = new ArrayList<>(set);

        /**
         * Сортируем.
         */
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

        /**
         * Создаем лист с указнным лимитом и возвращаем результат.
         */
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
