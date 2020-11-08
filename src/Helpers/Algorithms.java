package Helpers;

import Entities.FileLine;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class Algorithms {
    String date;
    String folderPath;

    public Algorithms() throws IOException {
        date = getDate();
        folderPath = FileOperations.createFolder(date); //klasörün path i output\2020_10_19_21_49_23
    }

    public void firstOwnAlgorithm(String inputPath) throws IOException {
        ArrayList<FileLine> list = FileOperations.readFile(inputPath);
        //FileLine objesinde list dönüyor => işlemleri bunun üzerinden yapabilelim

        list = applyOwnAlgorithm(list);
        //algorithm kullanılcak, list nasıl yazılcaksa o hale gelecek
        FileOperations.createFile(folderPath, "first_own_algorithm", list);
        //dizi gönderip dizi yazdırabilir
    }

    public static String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public ArrayList<FileLine> applyOwnAlgorithm(ArrayList<FileLine> list) {
        int selectedIndex = 0;
        int libaryCount = 0;
        String selectedLibrary;
        ArrayList<FileLine> appliedAlgorithmList = new ArrayList<FileLine>();
        FileLine line = null;

        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).IsClustered) {
                selectedIndex = i;
                selectedLibrary = list.get(i).ParentLib;
                libaryCount++;
                for (int j = selectedIndex; j < list.size(); j++) {
                    if (selectedLibrary.equals(list.get(j).ParentLib)) {
                        line = new FileLine();
                        line.Name = Integer.toString(libaryCount);
                        line.ParentLib = selectedLibrary;
                        line.ChildLib = list.get(j).ChildLib;
                        line.IsClustered = true;
                        appliedAlgorithmList.add(line);
                    }
                }
            }
        }
        Collections.sort(appliedAlgorithmList);
        return appliedAlgorithmList;
    }
}
