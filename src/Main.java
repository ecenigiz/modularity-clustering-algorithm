import Entities.InputFileLine;
import Helpers.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import TurboMq.TurboMQ;

public class Main {
    static String date;
    static String folderPath;

    public static void main(String[] args) throws IOException {
        date = getDate();
        folderPath = FileOperations.createFolder(date); //klasörün path i output\2020_10_19_21_49_23

        int clusterCount = 4;
        String inputPath = "input/bash-inc-dep.txt";
        ArrayList<InputFileLine> listFirstAlgorithm = FileOperations.readInputFile(inputPath);
        ArrayList<InputFileLine> listGeneticAlgorithm = FileOperations.readInputFile(inputPath);
        ArrayList<InputFileLine> listKmeansAlgorithm = FileOperations.readInputFile(inputPath);

        Algorithms algorithms = new Algorithms(folderPath);
        String outputPathFirstAlgorthm = algorithms.firstOwnAlgorithm(listFirstAlgorithm);

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(folderPath);
        //String outputPathGeneticAlgorithm = geneticAlgorithm.applyGeneticALgorithm(listGeneticAlgorithm, clusterCount);

        KMeansAlgorithm kMeansAlgorithm =new KMeansAlgorithm(folderPath);
        var populationKmeansAlgorithm = kMeansAlgorithm.applyKMeansAlgorithm(listKmeansAlgorithm,clusterCount);

        KMeansAlgorithm kMeansWithBinarySearchAlgorithm =new KMeansAlgorithm(folderPath);
        String outputPathKmeansWithBinarySearchAlgorithm = kMeansAlgorithm.applyKMeansWithBinarySearchAlgorithm(listKmeansAlgorithm,clusterCount);

        TurboMQ t = new TurboMQ();
        String clusteredPath = "bash-gt.rsf";
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
