package Helpers;


import Entities.InputFileLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {
    String folderPath;

    public GeneticAlgorithm(String folderPath) {
        this.folderPath = folderPath;
    }

    public String applyGeneticALgorithm(ArrayList<InputFileLine> list) throws IOException {
        list = preparePopulation(list);

        FileOperations.createFile(folderPath, "genetic_algorithm", list);

        return folderPath + "\\" + "genetic_algorithm.txt";
    }

    public ArrayList<InputFileLine> preparePopulation(ArrayList<InputFileLine> list) {
        ArrayList<InputFileLine> newList = new ArrayList<InputFileLine>();
        Random rand = new Random();

        int clusterIndex = 1;
        int elementCount = 0;
        int clusteredElementLenght;
        int listCount = list.size();
        int randomElementIndex = 0;

        for (int i = 0; i < listCount; i++) {
            clusteredElementLenght = rand.nextInt(list.size() > 16 ? 16 : list.size());

            for (int j = 0; j < clusteredElementLenght; j++) {
                randomElementIndex = rand.nextInt(list.size());
                newList.add(list.get(randomElementIndex));
                newList.get(elementCount).ClusterName = String.valueOf(clusterIndex);
                elementCount++;
                list.remove(randomElementIndex);
            }
            clusterIndex++;
        }
        return newList;
    }
}
