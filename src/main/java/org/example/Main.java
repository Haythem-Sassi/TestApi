package org.example;
import org.example.utility.FileTransformation;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {


        FileTransformation.concatenateExcelFiles(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx",
                "C:\\Users\\DELL G15\\Desktop\\mesures.xlsx");
        FileTransformation.addColumn(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx");
        FileTransformation.concatenateExcelFiles(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx",
                "C:\\Users\\DELL G15\\Desktop\\mesures.xlsx");
        FileTransformation.addColumn(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx");
        FileTransformation.concatenateExcelFiles(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx",
                "C:\\Users\\DELL G15\\Desktop\\mesures.xlsx");
        FileTransformation.addColumn(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx");
        FileTransformation.concatenateExcelFiles(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx",
                "C:\\Users\\DELL G15\\Desktop\\mesures.xlsx");
        FileTransformation.addColumn(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx");




        FileTransformation.deleteRow(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx");

    }}