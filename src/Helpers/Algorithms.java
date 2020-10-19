package Helpers;

import Entities.FileLine;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

        //algorithm kullanılcak, list nasıl yazılcaksa o hale gelecek
        FileOperations.createFile(folderPath, "first_own_algorithm", list);
        //dizi gönderip dizi yazdırabilir
    }

    public static String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
