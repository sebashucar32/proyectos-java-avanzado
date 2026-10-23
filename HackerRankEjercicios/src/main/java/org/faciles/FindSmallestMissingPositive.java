package org.faciles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Result {
    /*
     * Complete the 'findSmallestMissingPositive' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts INTEGER_ARRAY orderNumbers as parameter.
     */

    public static int findSmallestMissingPositive(List<Integer> orderNumbers) {
        TreeSet<Integer> newOrderNumbers = orderNumbers.stream()
            .filter(numero -> numero > 0)
            .collect(Collectors.toCollection(TreeSet::new));

        int minNum = 0;
        boolean numeroEncontrado = false;

        for (int number : newOrderNumbers) {
            minNum = minNum + 1;

            if (minNum != number) {
                numeroEncontrado = true;
                break;
            }
        }

        minNum = !numeroEncontrado ? minNum + 1 : minNum;

        return minNum;
    }
}

public class FindSmallestMissingPositive {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int orderNumbersCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> orderNumbers = IntStream.range(0, orderNumbersCount).mapToObj(i -> {
                try {
                    return bufferedReader.readLine().replaceAll("\\s+$", "");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            })
            .map(String::trim)
            .map(Integer::parseInt)
            .toList();

        int result = Result.findSmallestMissingPositive(orderNumbers);

        System.out.println(result);

        bufferedReader.close();
    }
}
