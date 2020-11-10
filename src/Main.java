import Entities.InputFileLine;
import Helpers.Algorithms;
import Helpers.FileOperations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import Helpers.GeneticAlgorithm;
import Helpers.ModulDependencyCalculator;

public class Main {
    static String date;
    static String folderPath;

    public static void main(String[] args) throws IOException {
        date = getDate();
        folderPath = FileOperations.createFolder(date); //klasörün path i output\2020_10_19_21_49_23

        String inputPath = "input/bash-inc-dep.txt";
        ArrayList<InputFileLine> list = FileOperations.readInputFile(inputPath);

        Algorithms algorithms = new Algorithms(folderPath);
        String outputPathFirstAlgorthm = algorithms.firstOwnAlgorithm(list);

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(folderPath);
        String outputPathGeneticAlgorithm = geneticAlgorithm.applyGeneticALgorithm(list);

        //Calculation
        ModulDependencyCalculator c = new ModulDependencyCalculator();
        String toPath = "bash-gt.rsf";
        double calculateDependency = c.CalculateDependency(inputPath,toPath);
        System.out.println("Bash alg calculation: " + calculateDependency);

        //FirstOwnAlgorithm
         calculateDependency = c.CalculateDependency(outputPathFirstAlgorthm,toPath);
        System.out.println("First algorithm calculation: " + calculateDependency);

        //GenetikAlgorithm random population
        calculateDependency = c.CalculateDependency(outputPathGeneticAlgorithm,toPath);
        System.out.println("Genetik algorithm calculation: " + calculateDependency);
    }

    public static String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
