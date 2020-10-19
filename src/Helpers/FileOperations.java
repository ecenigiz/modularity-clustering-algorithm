package Helpers;

import Entities.FileLine;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileOperations {

    public static  ArrayList<FileLine> readFile(String pathName) {
        ArrayList<FileLine> array = new ArrayList<FileLine>();

        try {
            File myObj = new File(pathName);

            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] lineSplit = data.split(" ");

                FileLine line = new FileLine();
                line.name=lineSplit[0];
                line.childLib = lineSplit[1];
                line.parentLib=lineSplit[2];
                array.add(line);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return array;
    }

    public static void createFile(String folderName, String fileName, ArrayList<FileLine> list) throws IOException {
        BufferedWriter bufferedWriter = null;

       // File file = new File("output/yeni.txt");
        File file = new File(folderName +"/"+fileName+".txt");

        if (!file.exists()) {

            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);

        for (FileLine fileLine : list)
        {
            bufferedWriter.write(fileLine.name + " "+ fileLine.childLib + " " + fileLine.parentLib);
            bufferedWriter.newLine();
        }

        bufferedWriter.flush();
        System.out.println("file write Success");
    }

    public static String createFolder(String folderName) throws IOException {
        {
            File folder = new File("output/"+folderName);
            folder.mkdir();
            folder.createNewFile();

           return  folder.getPath();

        }
    }
}
