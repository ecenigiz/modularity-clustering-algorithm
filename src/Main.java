import Helpers.Algorithms;
import Helpers.FileOperations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) throws IOException {

        Algorithms algorithms = new Algorithms();
        String inputPath = "input/bash-inc-dep.txt";
        algorithms.firstOwnAlgorithm(inputPath);
    }


}
