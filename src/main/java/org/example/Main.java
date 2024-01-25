package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    private static final String KEY_PATH = "C:\\Users\\Spire\\IdeaProjects\\oibLabar2\\src\\main\\resources\\M_key.txt";
    private static final String KEY_PATH2 = "C:\\Users\\Spire\\IdeaProjects\\oibLabar2\\src\\main\\resources\\Key.txt";
    private static final String ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ_*";
    private static final String TEXTX = "C:\\Users\\Spire\\IdeaProjects\\oibLabar2\\src\\main\\resources\\kod_V3.txt";

    public static void main(String[] args) throws IOException {

        //List<List<Double>> key_matrix = readMatrix(KEY_PATH);
        List<List<Double>> key_matrix2 = readMatrix(KEY_PATH2);
        //String coding = encript(key_matrix);
        //28 35 67 21 26 38
        //System.out.println(coding);
        String coding2 = Files.readAllLines(Path.of(TEXTX)).get(0);

        //String decodedText = decrypt(coding, key_matrix2);
        String decodedText = decrypt(coding2, key_matrix2);
        System.out.println(decodedText);
    }

//    private static String encript(List<List<Double>> keyMatrix) {
//        List<Integer> literaNums = new ArrayList<>();
//        for (Character c : TEXT.toCharArray()) {
//            literaNums.add(ALPHABET.indexOf(c) + 1);
//        }
//
//        int matrixSize = keyMatrix.get(0).size();
//
//        List<List<Integer>> vectors = new ArrayList<>();
//        for (int i = 0; i < literaNums.size(); i += matrixSize) {
//            vectors.add(literaNums.subList(i, Math.min(i + matrixSize, literaNums.size())));
//            //vectors.add(literaNums.subList(i,i+matrixSize));
//        }
//
//        String result = "";
//        for (int i = 0; i < vectors.size(); i++) {
//            for (int j = 0; j < keyMatrix.size(); j++) {
//                List<Double> matrixRow = keyMatrix.get(j);// значения строки матрицы
//                List<Integer> part = vectors.get(i);// значения вектора
//                int summ = 0;
//                for (int k = 0; k < matrixSize; k++) {
//                    double one = matrixRow.get(k);
//                    double two = part.get(k);
//                    summ += one * two;
//                }
//                result += summ + " ";
//                //result= result+sum+" ";
//            }
//        }
//        return result;
//    }

    private static List<List<Double>> readMatrix(String keyPath) {
        try {
            List<List<Double>> matrix = new ArrayList<>();

            List<String> rows = Files.readAllLines(Path.of(keyPath));
            for (String row : rows) {
                List<Double> r = new ArrayList<>();
                String[] numsString = row.split("\\s+");
                for (String s : numsString) {
                    r.add(Double.parseDouble(s));
                }
                matrix.add(r);
            }

            return matrix;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String decrypt(String code, List<List<Double>> reverseMatrix){
        int matrixSize = reverseMatrix.get(0).size(); //получаем размер матрицы
        List<Integer> nums = Arrays.stream(code.split("\\s+")).map(Integer::parseInt).collect(Collectors.toList()); //из строки с числами делаем лист интов
        List<List<Integer>> vectors = new ArrayList<>(IntStream.range(0, nums.size()) // получаем вектора из листа зашифрованных букв
                .boxed()
                .collect(Collectors.groupingBy(i -> i / matrixSize, Collectors.mapping(nums::get, Collectors.toList())))
                .values());

        List<Long> indexes = new ArrayList<>();
        // Умножение матрицы на вектора
        for (int i = 0; i < vectors.size(); i++) {
            for (int j = 0; j < reverseMatrix.size(); j++) {
                List<Double> matrixRow = reverseMatrix.get(j);
                List<Integer> part = vectors.get(i);
                double summ = 0;
                for (int k = 0; k < matrixSize; k++) {
                    double one = matrixRow.get(k);
                    double two = part.get(k);
                    summ += one * two;
                }
                indexes.add(Math.round(summ - 1));
            }
        }
        String text = "";
        for(Long index : indexes) {
            text+=ALPHABET.charAt(index.intValue());
        }

        return text;
    }
}


