package org.dificiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

class Result {
    public static long countSubarraysWithSumAndMaxAtMost(List<Integer> nums, long k, long M) {
        long sumaAcumulada = 0;
        long subArreglosBasicos = 0;
        Map<Long, Integer> frecuenciasSuma = new HashMap<>();
        frecuenciasSuma.put(0L, 1);

        for (int num : nums) {
            if (num > M) {
                sumaAcumulada = 0;
                frecuenciasSuma.clear();
                frecuenciasSuma.put(0L, 1);
                continue;
            }

            sumaAcumulada += num;

            long sumaBuscada = sumaAcumulada - k;

            if (frecuenciasSuma.containsKey(sumaBuscada)) {
                subArreglosBasicos += frecuenciasSuma.get(sumaBuscada);
            }

            frecuenciasSuma.put(sumaAcumulada, frecuenciasSuma.getOrDefault(sumaAcumulada, 0) + 1);
        }

        return subArreglosBasicos;
    }
}

public class SubarraysBoundedMaximum {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int numsCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> nums = IntStream.range(0, numsCount).mapToObj(i -> {
                try {
                    return bufferedReader.readLine().replaceAll("\\s+$", "");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            })
            .map(String::trim)
            .map(Integer::parseInt)
            .toList();

        long k = Long.parseLong(bufferedReader.readLine().trim());

        long M = Long.parseLong(bufferedReader.readLine().trim());

        long result = Result.countSubarraysWithSumAndMaxAtMost(nums, k, M);

        System.out.println(result);

        bufferedReader.close();
    }
}
