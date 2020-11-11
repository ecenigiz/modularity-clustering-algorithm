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
        InputFileLine line1 ;
        InputFileLine line2 ;
        Random rand = new Random();

        int clusterIndex = 1;
        int elementCount = 0;
        int clusteredElementLenght = 0;
        int listCount = list.size();
        int randomElementIndex = 0;
        String selectedLibrary = "";

        //for (int i = 0; i < listCount; i++)
        while (list.size() > 0) {
            clusteredElementLenght = rand.nextInt(list.size() > 32 ? 32 : list.size());

            for (int j = 0; j < clusteredElementLenght; j+=2) {
                randomElementIndex = list.size() == 1 ? 0 : rand.nextInt(list.size());
                line1 = new InputFileLine();
                line1.ChildLib = list.get(randomElementIndex).ChildLib;
                line1.ClusterName = String.valueOf(clusterIndex);
                line1.IsClustered = true;
                line1.Name = "Contain";
                newList.add(line1);

                line2 = new InputFileLine();
                line2.ChildLib = list.get(randomElementIndex).ParentLib;
                line2.ClusterName = String.valueOf(clusterIndex);
                line2.IsClustered = true;
                line2.Name = "Contain";
                newList.add(line1);
                newList.add(line2);

                elementCount += 2;
                for (int k = 0; k < list.size(); k++) {
                    if (line2.ChildLib.equals(list.get(k).ChildLib)) {
                        list.remove(k);
                        j++;
                        k--;
                    }
                    if (line1.ChildLib.equals(list.get(k).ChildLib)) {
                        list.remove(k);
                        j++;
                        k--;
                    }
                }
            }
            clusterIndex++;
        }

        for (int i=0; i<newList.size();i++){
            for(int j=i+1; j<newList.size();j++){
                if(newList.get(i).ChildLib.equals(newList.get(j).ChildLib)){
                    newList.remove(j);
                    j--;
                }
            }
        }
        return newList;
    }
}
