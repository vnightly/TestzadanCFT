package org.example;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
public class App 
{
    public static void main(String[] args) {
        String outputPath = ".";
        String prefix = "";
        boolean appendMode = false;
        boolean shortStats = false;
        boolean fullStats = false;
        List<String> inputFiles = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    if (i + 1 < args.length) {
                        outputPath = args[++i];
                    } else {
                        System.err.println("Ошибка: после -o должен быть путь.");
                        return;
                    }
                    break;
                case "-p":
                    if (i + 1 < args.length) {
                        prefix = args[++i];
                    } else {
                        System.err.println("Ошибка: после -p должен быть префикс.");
                        return;
                    }
                    break;
                case "-a":
                    appendMode = true;
                    break;
                case "-s":
                    shortStats = true;
                    break;
                case "-f":
                    fullStats = true;
                    break;
                default:
                    inputFiles.add(args[i]);
            }
        }

        if (inputFiles.isEmpty()) {
            System.err.println("Ошибка: не указаны входные файлы.");
            return;
        }

        List<Long> smIntegers = new ArrayList<>();
        List<Double> smFloats = new ArrayList<>();
        List<String> smStrings = new ArrayList<>();

        for (String fileName : inputFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String data;
                while ((data = reader.readLine()) != null) {
                    data = data.trim();
                    if (data.isEmpty()) continue;

                    try {
                        smIntegers.add(Long.parseLong(data));
                    } catch (NumberFormatException e1) {
                        try {
                            smFloats.add(Double.parseDouble(data));
                        } catch (NumberFormatException e2) {
                            smStrings.add(data);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка чтения файла " + fileName + ": " + e.getMessage());
            }
        }

        String integersFile = outputPath + File.separator + prefix + "integers.txt";
        String floatsFile = outputPath + File.separator + prefix + "floats.txt";
        String stringsFile = outputPath + File.separator + prefix + "strings.txt";

        if (!smIntegers.isEmpty()) {
            FileUtil.writeToFile(integersFile, smIntegers.stream().map(String::valueOf).collect(Collectors.toList()), appendMode);
        }
        if (!smFloats.isEmpty()) {
            FileUtil.writeToFile(floatsFile, smFloats.stream().map(String::valueOf).collect(Collectors.toList()), appendMode);
        }
        if (!smStrings.isEmpty()) {
            FileUtil.writeToFile(stringsFile, smStrings, appendMode);
        }

        if (shortStats || fullStats) {
            System.out.println("Статистика:");
            if (!smIntegers.isEmpty()) printStats("Целые числа", smIntegers, shortStats, fullStats);
            if (!smFloats.isEmpty()) printStats("Вещественные числа", smFloats, shortStats, fullStats);
            if (!smStrings.isEmpty()) printStringStats("Строки", smStrings, shortStats, fullStats);
        }
    }

    private static void printStats(String type, List<? extends Number> list, boolean shortStats, boolean fullStats) {
        System.out.println(type + ": " + list.size());
        if (fullStats) {
            double sum = list.stream().mapToDouble(Number::doubleValue).sum();
            double min = list.stream().mapToDouble(Number::doubleValue).min().orElse(0);
            double max = list.stream().mapToDouble(Number::doubleValue).max().orElse(0);
            double avg = sum / list.size();
            System.out.println("  Минимум: " + min);
            System.out.println("  Максимум: " + max);
            System.out.println("  Сумма: " + sum);
            System.out.println("  Среднее: " + avg);
        }
    }

    private static void printStringStats(String type, List<String> list, boolean shortStats, boolean fullStats) {
        System.out.println(type + ": " + list.size());
        if (fullStats) {
            int minLen = list.stream().mapToInt(String::length).min().orElse(0);
            int maxLen = list.stream().mapToInt(String::length).max().orElse(0);
            System.out.println("  Самая короткая строка: " + minLen + " символов");
            System.out.println("  Самая длинная строка: " + maxLen + " символов");
        }
    }
}
