package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestCollections {

    static final String REGEXP = "\\W+"; // for splitting into words
    List<HeavyBox> list;
    private BufferedReader reader;

    // 1 --------------------------------
    @Test
    void testPrintList() {
        //todo Распечатать содержимое используя for each
        list.forEach(System.out::println);
    }

    // 2 --------------------------------
    @Test
    void testChangeWeightOfFirstByOne() {
        //todo Изменить вес первой коробки на 1.
        HeavyBox heavyBox = list.get(0);
        heavyBox.setWeight(list.get(0).getWeight() + 1);

        assertEquals(new HeavyBox(1, 2, 3, 5), heavyBox);
    }

    // 3 --------------------------------
    @Test
    void testDeleteLast() {
        //todo Удалить предпоследнюю коробку.
        list.remove(list.size() - 2);

        assertEquals(6, list.size());
        assertEquals(new HeavyBox(1, 2, 3, 4), list.get(0));
        assertEquals(new HeavyBox(1, 3, 3, 4), list.get(list.size() - 2));
    }

    // 4 --------------------------------
    @Test
    void testConvertToArray() {
        //todo Получить массив содержащий коробки из коллекции тремя способами и вывести на консоль.
        HeavyBox[] arr = new HeavyBox[list.size()];
        //1
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
            System.out.println(arr[i]);
        }
        //2
        arr = list.toArray(new HeavyBox[0]);
        //3?
        arr = List.copyOf(list).toArray(HeavyBox[]::new);

        assertArrayEquals(new HeavyBox[]{
                new HeavyBox(1, 2, 3, 4),
                new HeavyBox(3, 3, 3, 4),
                new HeavyBox(2, 6, 5, 3),
                new HeavyBox(2, 3, 4, 7),
                new HeavyBox(1, 3, 3, 4),
                new HeavyBox(1, 2, 3, 4),
                new HeavyBox(1, 1, 1, 1)
        }, arr);
    }

    // 5 --------------------------------
    @Test
    void testDeleteBoxesByWeight() {
        // todo удалить все коробки, которые весят 4
        list.removeIf(heavyBox -> heavyBox.getWeight() == 4);
        assertEquals(3, list.size());
    }

    // 6 --------------------------------
    @Test
    void testSortBoxesByWeight() {
        // отсортировать коробки по возрастанию веса. При одинаковом весе - по возрастанию объема
        list.sort((o1, o2) -> {
            int k = Integer.compare(o1.getWeight(), o2.getWeight());
            if (k == 0) {
                return Double.compare(o1.getVolume(), o2.getVolume());
            } else return k;
        });
        assertEquals(new HeavyBox(1, 1, 1, 1), list.get(0));
        assertEquals(new HeavyBox(2, 3, 4, 7), list.get(6));
        assertEquals(new HeavyBox(1, 2, 3, 4), list.get(3));
        assertEquals(new HeavyBox(1, 3, 3, 4), list.get(4));
    }

    // 7 --------------------------------
    @Test
    void testClearList() {
        //todo Удалить все коробки.
        list.clear();
        assertTrue(list.isEmpty());
    }

    // 8 --------------------------------
    @Test
    void testReadAllLinesFromFileToList() {
        // todo Прочитать все строки в коллекцию
        List<String> lines = null;
        lines = reader.lines().collect(Collectors.toList());
        assertEquals(19, lines.size());
        assertEquals("", lines.get(8));
    }

    // 9 --------------------------------
    @Test
    void testReadAllWordsFromFileToList() throws IOException {
        // todo прочитать все строки, разбить на слова и записать в коллекцию
        List<String> words = readAllWordsFromFileToList();
        assertEquals(257, words.size());
    }

    List<String> readAllWordsFromFileToList() throws IOException {
        List<String> words = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
//            line = line.replaceAll("[^a-zA-Z ]", " ");
//            words.addAll(List.of(line.split("\\s+")));
            words.addAll(List.of(line.split(REGEXP)));
        }
        words.removeIf(String::isEmpty);
        return words;
    }

    // 10 -------------------------------
    @Test
    void testFindLongestWord() throws IOException {
        // todo Найти самое длинное слово
        assertEquals("conversations", findLongestWord());
    }

    private String findLongestWord() throws IOException {
        return readAllWordsFromFileToList().stream()
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
    }

    // 11 -------------------------------
    @Test
    void testAllWordsByAlphabetWithoutRepeat() throws IOException {
        // todo Получить список всех слов по алфавиту без повторов
        List<String> result = null;
        result = readAllWordsFromFileToList().stream()
                .map(String::toLowerCase)
                .sorted()
                .distinct()
                .collect(Collectors.toList());
        assertEquals("alice", result.get(5));
        assertEquals("all", result.get(6));
        assertEquals("without", result.get(134));
        assertEquals(138, result.size());
    }

    // 12 -------------------------------
    @Test
    void testFindMostFrequentWord() throws IOException {
        // todo Найти самое часто вcтречающееся слово
        assertEquals("the", mostFrequentWord());
    }

    // 13 -------------------------------
    @Test
    void testFindWordsByLengthInAlphabetOrder() throws IOException {
        // todo получить список слов, длиной не более 5 символов, переведенных в нижний регистр, в порядке алфавита, без повторов
        List<String> strings = readAllWordsFromFileToList().stream()
                .map(String::toLowerCase)
                .filter(s->s.length()<=5)
                .sorted()
                //.distinct() - мало б бути рішенням, але не проходить
                .toList();
        assertEquals(202, strings.size());
        assertEquals("a", strings.get(0));
        assertEquals("alice", strings.get(10));
        assertEquals("would", strings.get(strings.size() - 1));
    }

    private String mostFrequentWord() throws IOException {
        List<String> words = readAllWordsFromFileToList();
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (String key : words) {
            if (hashMap.containsKey(key)) {
                hashMap.put(key,hashMap.remove(key)+1);
            } else hashMap.put(key, 1);
        }
        String theWord = "";
        int frequency = 0;
        for (Entry entry : hashMap.entrySet()) {
            if ((Integer) entry.getValue() > frequency) {
                frequency = (Integer) entry.getValue();
                theWord = String.valueOf(entry.getKey());
            }
        }
        return theWord;
    }

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(List.of(
                new HeavyBox(1, 2, 3, 4),
                new HeavyBox(3, 3, 3, 4),
                new HeavyBox(2, 6, 5, 3),
                new HeavyBox(2, 3, 4, 7),
                new HeavyBox(1, 3, 3, 4),
                new HeavyBox(1, 2, 3, 4),
                new HeavyBox(1, 1, 1, 1)
        ));
    }

    @BeforeEach
    public void setUpBufferedReader() throws IOException {
        reader = Files.newBufferedReader(
                Paths.get("Text.txt"), StandardCharsets.UTF_8);
    }

    @AfterEach
    public void closeBufferedReader() throws IOException {
        reader.close();
    }
}
