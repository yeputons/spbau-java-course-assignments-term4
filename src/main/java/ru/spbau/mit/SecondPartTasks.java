package ru.spbau.mit;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        throw new UnsupportedOperationException();
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и
    // посчитать, какова вероятность попасть в мишень.
    private static final int MONTE_CARLO_ITERATIONS = 1000000;
    public static double piDividedBy4() {
        Random rnd = new Random();
        Stream<Point2D> points = Stream.generate(() -> new Point2D.Double(rnd.nextDouble(), rnd.nextDouble()));
        return points
                .limit(MONTE_CARLO_ITERATIONS)
                .collect(Collectors.averagingInt(p -> p.distance(0, 0) <= 1 ? 1 : 0));
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        throw new UnsupportedOperationException();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        throw new UnsupportedOperationException();
    }
}
