import Entities.InputFileLine;
import Helpers.Algorithms;
import Helpers.FileOperations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import Helpers.GeneticAlgorithm;
import Helpers.ModulDependencyCalculator;
import TurboMq.TurboMQ;

public class Main {
    static String date;
    static String folderPath;

    public static void main(String[] args) throws IOException {
        date = getDate();
        folderPath = FileOperations.createFolder(date); //klasörün path i output\2020_10_19_21_49_23

        int clusterCount = 10;
        String inputPath = "input/bash-inc-dep.txt";
        ArrayList<InputFileLine> listFirstAlgorithm = FileOperations.readInputFile(inputPath);
        ArrayList<InputFileLine> listGeneticAlgorithm = FileOperations.readInputFile(inputPath);

        Algorithms algorithms = new Algorithms(folderPath);
        String outputPathFirstAlgorthm = algorithms.firstOwnAlgorithm(listFirstAlgorithm);

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(folderPath);
        String outputPathGeneticAlgorithm = geneticAlgorithm.applyGeneticALgorithm(listGeneticAlgorithm, clusterCount);
        String clusteredPath = "bash-gt.rsf";
        TurboMQ t = new TurboMQ();
        System.out.println("Bash alg turbo hesapalaması : "+ t.TurboMQCalculate(inputPath, clusteredPath));

        ModulDependencyCalculator c = new ModulDependencyCalculator();
/*
        //Calculation
        double calculateDependency = c.CalculateDependency(inputPath, clusteredPath);
        System.out.println("Bash alg modularity calculation: " + calculateDependency);

        //FirstOwnAlgorithm
        calculateDependency = c.CalculateDependency(inputPath, outputPathFirstAlgorthm);
        System.out.println("First algorithm calculation: " + calculateDependency);

        //GenetikAlgorithm random population
        calculateDependency = c.CalculateDependency(inputPath, outputPathGeneticAlgorithm);
        System.out.println("Genetik algorithm calculation: " + calculateDependency);*/
    }

    public static String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
