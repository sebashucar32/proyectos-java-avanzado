package org.medios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

class Result {
    public static List<List<Integer>> mergeHighDefinitionIntervals(List<List<Integer>> intervals) {
        List<List<Integer>> resultadoFinal = new ArrayList<>();

        List<List<Integer>> intervalosOrdenados = new ArrayList<>(intervals);
        intervalosOrdenados.sort(
                Comparator.comparingInt(intervalo -> intervalo.get(0))
        );

        List<Integer> filaActual = intervalosOrdenados.size() > 0
                ? intervalosOrdenados.get(0) : new ArrayList<>();

        resultadoFinal.add(filaActual);

        for (int i = 1; i <= intervalosOrdenados.size() - 1; i++) {
            List<Integer> filaSiguiente = intervalosOrdenados.get(i);

            int finActual = filaActual.get(1);
            int inicioSiguiente = filaSiguiente.get(0);

            if(finActual >= inicioSiguiente) {
                if (finActual < filaSiguiente.get(1)) {
                    List<Integer> listaTemporal = new ArrayList<>();
                    listaTemporal.add(filaActual.get(0));
                    listaTemporal.add(filaSiguiente.get(1));
                    filaActual = listaTemporal;
                    resultadoFinal.remove(resultadoFinal.size() - 1);
                    resultadoFinal.add(filaActual);
                }
            } else {
                filaActual = filaSiguiente;
                resultadoFinal.add(filaSiguiente);
            }
        }

        return resultadoFinal;
    }
}

public class MergeAndSortIntervals {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int intervalsRows = Integer.parseInt(bufferedReader.readLine().trim());
        int intervalsColumns = Integer.parseInt(bufferedReader.readLine().trim());

        List<List<Integer>> intervals = new ArrayList<>();

        IntStream.range(0, intervalsRows).forEach(i -> {
            try {
                intervals.add(
                    Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .toList()
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        List<List<Integer>> result = Result.mergeHighDefinitionIntervals(intervals);

        result.stream()
            .map(
                r -> r.stream()
                    .map(Object::toString)
                    .collect(joining(" "))
            ).toList()
            .forEach(System.out::println);

        bufferedReader.close();
    }
}

