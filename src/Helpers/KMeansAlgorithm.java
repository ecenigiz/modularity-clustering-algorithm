package Helpers;

import Entities.InputFileLine;
import Entities.Population;
import TurboMq.TurboMQ;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class KMeansAlgorithm {
    String folderPath;
    public KMeansAlgorithm(String folderPath) {
        this.folderPath = folderPath;
    }
    public String applyKMeansAlgorithm(ArrayList<InputFileLine> list, int clusterCount) throws IOException {
        ArrayList<Population> populations = new ArrayList<Population>();
        ArrayList<Population> populationsNew = new ArrayList<Population>();
        ArrayList<InputFileLine> temp;
        Population tempPopulation;

        DecimalFormat df = new DecimalFormat("0.000");

        TurboMQ t = new TurboMQ();

        for (int i = 0; i < populationsNew.size(); i++) {
            String path = folderPath + "\\genetic_algorithm" + i + ".txt";
            FileOperations.createFile(folderPath, "kmeans_algorithm_cluster_" + clusterCount + "_population_number_" + i + "_calculation_" + populationsNew.get(i).DependencyCalculation, populationsNew.get(i).ClusteredItems);
        }

        return folderPath + "\\" + "genetic_algorithm.txt";

    }
}
