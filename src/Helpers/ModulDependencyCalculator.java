package Helpers;

import Entities.InputFileLine;
import java.util.ArrayList;

public class ModulDependencyCalculator {

    double totalSum = 0;
    int edgeWeigthIsExist = 1;
    int edgeWeigthIsNotExist = 0;
    int clusterCount = 1;

    public double CalculateDependency(String fromPath, String toPath) {
        long startTime = System.currentTimeMillis();

        ArrayList<InputFileLine> firstList = FileOperations.readInputFile(fromPath);
        ArrayList<InputFileLine> clusteredList = FileOperations.readOutputFile(toPath);

        int[] eachOfEdgeCount = new int[clusteredList.size()];

        //Kaç tane farklı küme var bunu buluyoruz
        for (int i = 1; i < clusteredList.size(); i++) {
            int j = 0;
            for (j = 0; j < i; j++) {
                if (clusteredList.get(i).ClusterName.equals(clusteredList.get(j).ClusterName))
                    break;
            }
                if (i == j)
                    clusterCount++;
        }


        //Her kümenin eleman sayısını buluyoruz
        int[] clusterHasEdge = new int[clusterCount + 1];
        int clusterIndex = 0;
        String previousClusterName = clusteredList.get(0).ClusterName;

        for (int i = 0; i < clusteredList.size(); i++) {
            if (previousClusterName.equals(clusteredList.get(i).ClusterName))
                clusterHasEdge[clusterIndex]++;
            else {
                clusterIndex++;
                clusterHasEdge[clusterIndex]++;
                previousClusterName = clusteredList.get(i).ClusterName;
            }
        }

        //1 nokta kaç yere bağlı onu buluyoruz
        for (int i = 0; i < clusteredList.size(); i++) {
            for (int j = 0; j < firstList.size(); j++) {
                if (clusteredList.get(i).ChildLib.equals(firstList.get(j).ChildLib) ||
                        (clusteredList.get(i).ChildLib.equals(firstList.get(j).ParentLib))) {
                    eachOfEdgeCount[i]++;
                }
            }
        }

        //Hesaplama kısmı yöntem 1
        for (int i = 0; i < clusterCount; i++) {
            for (int j = 0; j < clusterHasEdge[i]; j++) {
                for (int l = j + 1; l < clusterHasEdge[i]; l++) {
                    boolean flag = false;
                    for (int k = 0; k < firstList.size(); k++) {
                        boolean x1 = (firstList.get(k).ChildLib.equals(clusteredList.get(i).ChildLib)
                                && firstList.get(k).ParentLib.equals(clusteredList.get(j).ChildLib));
                        boolean x2 = (firstList.get(k).ParentLib.equals(clusteredList.get(i).ChildLib)
                                && firstList.get(k).ChildLib.equals(clusteredList.get(j).ChildLib));

                        if (x1 || x2) flag = true;
                    }
                    if (flag) {
                        totalSum += edgeWeigthIsExist - ((double) (eachOfEdgeCount[i] * eachOfEdgeCount[j]) / (2 * clusterCount));
                    } else {
                        totalSum += edgeWeigthIsNotExist - ((double) (eachOfEdgeCount[i] * eachOfEdgeCount[j]) / (2 * clusterCount));
                    }
                }
            }
        }

    //Hesaplama kısmı yöntem 2
        /*for (int i = 0; i < clusteredList.size(); i++) {
            for (int j = i + 1; j < clusteredList.size(); j++) {
                boolean flag = false;
                if (clusteredList.get(i).ClusterName.equals(clusteredList.get(j).ClusterName)) { //aynı kümedelerse
                    for (int k = 0; k < firstList.size(); k++) {
                        boolean x1 = (firstList.get(k).ChildLib.equals(clusteredList.get(i).ChildLib)
                                && firstList.get(k).ParentLib.equals(clusteredList.get(j).ChildLib));
                        boolean x2 = (firstList.get(k).ParentLib.equals(clusteredList.get(i).ChildLib)
                                && firstList.get(k).ChildLib.equals(clusteredList.get(j).ChildLib));

                        if (x1 || x2) flag = true;
                    }
                    if (flag) {
                        totalSum += edgeWeigthIsExist - ((double) (eachOfEdgeCount[i] * eachOfEdgeCount[j]) / (2 * clusterCount));
                    } else {
                        totalSum += edgeWeigthIsNotExist - ((double) (eachOfEdgeCount[i] * eachOfEdgeCount[j]) / (2 * clusterCount));
                    }
                } else
                    j = clusteredList.size();
            }
        }*/

        totalSum = totalSum / (2 * clusterCount);
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Hesaplamada harcanan süre: " + estimatedTime);
        return totalSum;
    }
}

