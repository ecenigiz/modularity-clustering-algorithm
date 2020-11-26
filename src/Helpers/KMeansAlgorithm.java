package Helpers;

import Entities.InputFileLine;
import Entities.Population;
import TurboMq.TurboMQ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.MAX_VALUE;

public class KMeansAlgorithm {
    String folderPath;

    public KMeansAlgorithm(String folderPath) {
        this.folderPath = folderPath;
    }

    public String applyKMeansAlgorithm(ArrayList<InputFileLine> list, int clusterCount) throws IOException {
        ArrayList<Population> populations = new ArrayList<Population>();
        Population tempPopulation = new Population();
        ArrayList<InputFileLine> tempLines = new ArrayList<InputFileLine>();
        InputFileLine tmpline;

        List<String> col = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            col.add(list.get(i).ChildLib);
            col.add(list.get(i).ParentLib);
        }

        //1->a, 2->b. Bu sayede her class ı sayısallaştırmış oluyoruz.
        List<String> distincLine = col.stream().distinct().collect(Collectors.toList());
        int lineCount = distincLine.size();

        boolean[][] dependArray = new boolean[lineCount][lineCount];
        int x;
        int y;
        for (int i = 0; i < list.size(); i++) {
            x = distincLine.indexOf(list.get(i).ChildLib);
            y = distincLine.indexOf(list.get(i).ParentLib);
            dependArray[x][y] = true;
        }
        //depended array-> noktaların varlığı oluyor.

        clusterCount = 3;
        int clusterSize = (distincLine.size() / clusterCount);

        int[] centroid = {0, 1, 2};//= new int[clusterCount];
        int[] centroidBefore = {0, 1, 2};

        HashMap<String, Integer> distanceEachCluster = new HashMap<String, Integer>();
        int iterationCount = 1000;
        int centroidSameCount = 0;
        int iteration = 0;
        //for (int iteration = 0; iteration < iterationCount; iteration++)
        while (centroidSameCount<clusterCount) {
            //her küme için her nokta ile arasındaki
            distanceEachCluster = new HashMap<String, Integer>();
            int distanceX = 0, distanceY = 0, distance = 0;
            int tempClusterName = -1, tempDistanceValue = 0;
            int minValueInCluster;

            for (int i = 0; i < lineCount; i++) {
                for (int j = 0; j < lineCount; j++) {
                    if (dependArray[i][j]) {
                        minValueInCluster = MAX_VALUE;
                        tempClusterName = -1;
                        //her c ile distance karşılastırıyoeuz, en kucuk dıstance ı secıyoruz ve o noktanın cluster ına atamam yapıyoruz
                        for (int c = 0; c < clusterCount; c++) {
                            distanceX = i - centroid[c];
                            distanceY = j - centroid[c];
                            distance = Math.abs((distanceX * distanceX) + (distanceY * distanceY));
                            if (distance < minValueInCluster) {
                                minValueInCluster = distance;
                                tempClusterName = c;
                            }
                        }
                    }
                }
                distanceEachCluster.put(String.valueOf(i), tempClusterName);
            }

            int[] sumOfEachCluster = new int[clusterCount];
            int[] countOfEachCluster = new int[clusterCount];
            for (int k = 0; k < distanceEachCluster.size(); k++) {
                sumOfEachCluster[distanceEachCluster.get(String.valueOf(k))] += k;
                countOfEachCluster[distanceEachCluster.get(String.valueOf(k))]++;
            }

            //her sınıf ıcın ortalamayı bulduk
            int[] averageOfEachCluster = new int[clusterCount];
            for (int k = 0; k < clusterCount; k++) {
                if (countOfEachCluster[k] > 0) {
                    averageOfEachCluster[k] = sumOfEachCluster[k] / countOfEachCluster[k];
                }
            }

            centroidSameCount = 0;
            //ortalamayı atamamız lazım
            for (int k = 0; k < clusterCount; k++) {
                centroid[k] = averageOfEachCluster[k];
                if (centroidBefore[k] == centroid[k]) {
                    centroidSameCount++;
                }
                centroidBefore[k] = centroid[k];
            }
            iteration++;

        }
        System.out.println("iteration:" + iteration);
        for (
                int i = 0; i < distincLine.size(); i++) {
            tmpline = new InputFileLine();
            tmpline.ChildLib = distincLine.get(i); //??
            tmpline.ClusterName = String.valueOf(distanceEachCluster.get(String.valueOf(i)));
            tmpline.IsClustered = true;
            tmpline.Name = "contain";
            tempLines.add(tmpline);
        }

        TurboMQ t = new TurboMQ();

        tempPopulation.ClusterCount = clusterCount;
        tempPopulation.ClusteredItems = tempLines;
        tempPopulation.DependencyCalculation = t.TurboMQCalculateWithList(list, tempPopulation.ClusteredItems);
        populations.add(tempPopulation);

        for (
                int i = 0; i < populations.size(); i++) {
            String path = folderPath + "\\genetic_algorithm" + i + ".txt";
            FileOperations.createFile(folderPath, "kmeans_algorithm_cluster_" + clusterCount + "_population_number_" + i + "_calculation_" + populations.get(i).DependencyCalculation, populations.get(i).ClusteredItems);
        }

        return folderPath + "\\" + "kmeans_algorithm.txt";

    }
}
