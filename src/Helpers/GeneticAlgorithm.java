package Helpers;


import Entities.InputFileLine;
import Entities.Population;
import TurboMq.TurboMQ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class GeneticAlgorithm {
    String folderPath;

    public GeneticAlgorithm(String folderPath) {
        this.folderPath = folderPath;
    }

    public String applyGeneticALgorithm(ArrayList<InputFileLine> list, int clusterCount) throws IOException {
        ArrayList<Population> populations = new ArrayList<Population>();
        ArrayList<InputFileLine> best = new ArrayList<InputFileLine>();
        ArrayList<InputFileLine> temp = new ArrayList<InputFileLine>();
        Population tempPopulation = new Population();
        String inputPath = "input/bash-inc-dep.txt";
        String path = "";

        TurboMQ t = new TurboMQ();

        // Rasgele populasyon oluşturuyoruz
        int populasyonCount = 5;
        for (int i = 0; i < populasyonCount; i++) {
            temp = preparePopulation(list, clusterCount);

            int calculationDependency = 100;
            tempPopulation = new Population();
            tempPopulation.ClusterCount = clusterCount;
            tempPopulation.DependencyCalculation = calculationDependency;
            tempPopulation.ClusteredItems = temp;
            populations.add(tempPopulation);
        }
        // Oluşturulan ilk populasyonu yazdırıp, hesaplıyoruz
        for (int i = 0; i < populations.size(); i++) {
            path = folderPath + "\\genetic_algorithm" + i + ".txt";
            FileOperations.createFile(folderPath, "genetic_algorithm" + i, populations.get(i).ClusteredItems);
            populations.get(i).DependencyCalculation = t.TurboMQCalculate(inputPath, path);
        }

        // Collections.reverse(populations); ->buyukten kucuge ıcın yapabilirsin
        //küçükten büyüğe sıralıyoruz
        Collections.sort(populations);

        // En iyi ilk elemanı seçiyoruz
        int selectedPopulationCount = 3;
        for (int i = 0; i < populations.size() - selectedPopulationCount; i++) {
            populations.remove(i);
        }

        //while döngüsü burda olcak mesela 10 kere yapabilirsin ya da max calculatıon azalana kadar yapabilirsin
        //10 kere populasyonu crossover yapıyoruz, mutasyon yapıyoruz, hesaplayıp en ıyı 10 u secıyoruz
        for (int count = 0; count < 10; count++) {

            populations = applyCrossOverDivideHalf(populations);

            populations = applyMutation(populations, clusterCount);

            //Büyükten küçüğe sıralıyoruz
            Collections.sort(populations);
            Collections.reverse(populations);

            // En iyi ilk elemanı seçiyoruz
            for (int i = selectedPopulationCount; i < populations.size(); i++) {
                populations.remove(i);
            }

            for (int i = 0; i < populations.size(); i++) {
                path = folderPath + "\\genetic_algorithm_" + count + "_" + i + ".txt";
                FileOperations.createFile(folderPath, "genetic_algorithm_" + count + "_" + i, populations.get(i).ClusteredItems);
                populations.get(i).DependencyCalculation = t.TurboMQCalculate(inputPath, path);
            }
            // En iyi ilk elemanı seçiyoruz
            Collections.sort(populations);
            selectedPopulationCount = 3;
            for (int i = 0; i < populations.size() - selectedPopulationCount; i++) {
                populations.remove(i);
            }

        }
        return folderPath + "\\" + "genetic_algorithm.txt";

    }

    public ArrayList<InputFileLine> preparePopulation(ArrayList<InputFileLine> list, int clusterCount) {
        ArrayList<InputFileLine> clusteredList = new ArrayList<InputFileLine>();
        String clusterName;
        Random rand = new Random();
        InputFileLine line = null;

        for (int i = 0; i < list.size(); i++) {
            clusterName = String.valueOf(rand.nextInt(clusterCount));
            list.get(i).ClusterName = clusterName;
            clusteredList.add(list.get(i));

            line = new InputFileLine();
            line.Name = "Contain";
            line.ClusterName = clusterName;
            line.ChildLib = list.get(i).ParentLib;
            clusteredList.add(line);
        }

        ArrayList<InputFileLine> clusteredDistinctList = new ArrayList<InputFileLine>();
        for (int i = 0; i < clusteredList.size(); i++) {
            if (!clusteredList.get(i).IsClustered) {
                clusteredList.get(i).IsClustered = true;
                clusteredDistinctList.add(clusteredList.get(i));
                for (int j = i + 1; j < clusteredList.size(); j++) {
                    if (clusteredList.get(i).ChildLib.equals(clusteredList.get(j).ChildLib)) {
                        clusteredList.get(j).IsClustered = true;
                    }
                }
            }
        }
        clusteredDistinctList.forEach((temp) -> {
            temp.IsClustered = false;
        });

        return clusteredDistinctList;
    }

    public ArrayList<Population> applyCrossOverDivideHalf(ArrayList<Population> population) {
        int populationCount = population.size();
        ArrayList<InputFileLine> itemInPopulationI;
        ArrayList<InputFileLine> itemInPopulationJ;

        ArrayList<Population> newPopulations = new ArrayList<Population>();
        Population populationInPopulationItemNew = new Population();
        ArrayList<InputFileLine> itemInPopulationNew = new ArrayList<InputFileLine>();
        InputFileLine itemInPopulationItemNew;

        for (int i = 0; i < populationCount; i++) {
            itemInPopulationI = population.get(i).ClusteredItems;

            for (int j = i + 1; j < populationCount; j++) {
                populationInPopulationItemNew = new Population();
                itemInPopulationJ = population.get(j).ClusteredItems;
                itemInPopulationNew = new ArrayList<InputFileLine>();

                for (int k = 0; k < itemInPopulationJ.size(); k++) {
                    itemInPopulationItemNew = new InputFileLine();

                    if (k < itemInPopulationJ.size() / 2) {
                        itemInPopulationItemNew.ClusterName = itemInPopulationI.get(k).ClusterName;
                        itemInPopulationItemNew.IsClustered = true;
                        itemInPopulationItemNew.ChildLib = itemInPopulationI.get(k).ChildLib;
                        itemInPopulationItemNew.Name = itemInPopulationI.get(k).Name;
                    } else {
                        itemInPopulationItemNew.ClusterName = itemInPopulationJ.get(k).ClusterName;
                        itemInPopulationItemNew.IsClustered = true;
                        itemInPopulationItemNew.ChildLib = itemInPopulationJ.get(k).ChildLib;
                        itemInPopulationItemNew.Name = itemInPopulationJ.get(k).Name;
                    }
                    itemInPopulationNew.add(itemInPopulationItemNew);
                }
                populationInPopulationItemNew.ClusteredItems = itemInPopulationNew;
                int calculation = 100;
                populationInPopulationItemNew.DependencyCalculation = calculation;
                newPopulations.add(populationInPopulationItemNew);
                //genetik yaptıktan sonra, cluster count kayboluyor.
            }
        }

        //1-1 aynı ıse siliyoz
        boolean x1, x2;
        int flag = 0;
        for (int m = 0; m < newPopulations.size(); m++) {
            for (int l = 0; l < population.size(); l++) {
                flag = 0;
                for (int i = 0; i < population.get(l).ClusteredItems.size(); i++) {
                    x1 = newPopulations.get(m).ClusteredItems.get(i).ChildLib.equals(population.get(l).ClusteredItems.get(i).ChildLib);
                    x2 = newPopulations.get(m).ClusteredItems.get(i).ClusterName.equals(population.get(l).ClusteredItems.get(i).ClusterName);
                    if (x1 && x2)
                        flag++;
                }

                if (flag == population.get(l).ClusteredItems.size()) {
                    newPopulations.remove(m);
                    m--;
                    l = population.size();
                }
            }
        }

        population.addAll(newPopulations);
        return population;
    }

    public ArrayList<Population> applyMutation(ArrayList<Population> populations, int clusterCount) {

        Random rand = new Random();
        int populationCount = populations.size();
        ArrayList<InputFileLine> itemInPopulationI;
        ArrayList<InputFileLine> itemInPopulationJ;

        ArrayList<Population> newPopulations = new ArrayList<Population>();
        Population populationInPopulationItemNew = new Population();
        ArrayList<InputFileLine> itemInPopulationNew = new ArrayList<InputFileLine>();
        InputFileLine itemInPopulationItemNew;


        for (int j = 0; j < populationCount; j++) {
            populationInPopulationItemNew = new Population();
            itemInPopulationJ = populations.get(j).ClusteredItems;
            itemInPopulationNew = new ArrayList<InputFileLine>();

            for (int k = 0; k < itemInPopulationJ.size(); k++) {
                itemInPopulationItemNew = new InputFileLine();
                itemInPopulationItemNew.ClusterName = itemInPopulationJ.get(k).ClusterName;
                itemInPopulationItemNew.IsClustered = true;
                itemInPopulationItemNew.ChildLib = itemInPopulationJ.get(k).ChildLib;
                itemInPopulationItemNew.Name = itemInPopulationJ.get(k).Name;
                itemInPopulationNew.add(itemInPopulationItemNew);
            }
            populationInPopulationItemNew.ClusteredItems = itemInPopulationNew;
            int calculation = 100;
            populationInPopulationItemNew.DependencyCalculation = calculation;
            populationInPopulationItemNew.ClusterCount = populations.get(j).ClusterCount;
            newPopulations.add(populationInPopulationItemNew);

            String randomClusterName = String.valueOf(rand.nextInt(clusterCount));
            int randomIndex = rand.nextInt(clusterCount);
            newPopulations.get(j).ClusteredItems.get(randomIndex).ClusterName = randomClusterName;
        }

        //1-1 aynı ıse siliyoz
        boolean x1, x2;
        int flag = 0;
        for (int m = 0; m < newPopulations.size(); m++) {
            for (int l = 0; l < populations.size(); l++) {
                flag = 0;
                for (int i = 0; i < populations.get(l).ClusteredItems.size(); i++) {
                    x1 = newPopulations.get(m).ClusteredItems.get(i).ChildLib.equals(populations.get(l).ClusteredItems.get(i).ChildLib);
                    x2 = newPopulations.get(m).ClusteredItems.get(i).ClusterName.equals(populations.get(l).ClusteredItems.get(i).ClusterName);
                    if (x1 && x2)
                        flag++;
                }

                if (flag == populations.get(l).ClusteredItems.size()) {
                    newPopulations.remove(m);
                    m--;
                    l = populations.size();
                }
            }
        }

        populations.addAll(newPopulations);
        return populations;
    }
}
