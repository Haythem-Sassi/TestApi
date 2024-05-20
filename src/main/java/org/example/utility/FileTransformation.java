package org.example.utility;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.Cell;
import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileTransformation {



    public static int id=1;


    private static Double getNumericCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                return parseValue(cell.getStringCellValue());
            default:
                return null;
        }
    }
    private static double parseValue(String valueStr) {
        if (valueStr == null || valueStr.isEmpty()) {
            return Double.NaN;
        }
        // Ignorer les caractères spéciaux comme "_tbr"
        valueStr = valueStr.replaceAll("[^\\d.]", "");
        try {
            return Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    public static Map<String, Double> readJsonFile(String jsonFilePath) {
        Map<String, Double> jsonMap = new HashMap<>();

        try (FileReader reader = new FileReader(jsonFilePath)) {
            Gson gson = new Gson();
            Map<String, String> stringMap = gson.fromJson(reader, Map.class);
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                String oldKey = entry.getKey();
                String valueStr = entry.getValue();
                // Renommer les clés et nettoyer les valeurs
                String newKey = renameKey(oldKey);
                // Nettoyer les caractères spéciaux et convertir en Double
                double value = parseValue(valueStr);
                // Ajouter à la Map
                jsonMap.put(newKey, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonMap;
    }

    private static String renameKey(String oldKey) {
        switch (oldKey) {
            case "neck_circumference_cm":
                return "Neck Circumference";
            case "waist_circumference_cm":
                return "Waist Circumference";
            case "chest_circumference_cm":
                return "Chest Circumference";
            case "calf_circumference_cm":
                return "Right Calf Circumference";
            case "thigh_circumference_cm":
                return "Thigh Circumference";
            case "hips_circumference_cm":
                return "High Hip Circumference";
            // Ajouter d'autres cas si nécessaire
            default:

                return oldKey;
        }}


    public static  Map<String, Double> readExcelFile(String excelFilePath) throws IOException {
        Workbook workbook = WorkbookFactory.create(new FileInputStream(excelFilePath));
        Sheet sheet = workbook.getSheetAt(0);
        Map<String, Double> excelValues = new HashMap<>();
        excelValues.put("Neck Circumference", getNumericCellValue(sheet.getRow(2).getCell(1)));
        excelValues.put("Waist Circumference", getNumericCellValue(sheet.getRow(5).getCell(1)));
        excelValues.put("Chest Circumference", getNumericCellValue(sheet.getRow(6).getCell(1)));
        excelValues.put("Right Calf Circumference", getNumericCellValue(sheet.getRow(11).getCell(1)));
        excelValues.put("Thigh Circumference", getNumericCellValue(sheet.getRow(13).getCell(1)));
        excelValues.put("High Hip Circumference", getNumericCellValue(sheet.getRow(14).getCell(1)));



        return excelValues;
    }

    public static void convertCSVToXLSX(String csvFilePath, String xlsxFilePath) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath));
             Workbook workbook = new XSSFWorkbook();
             FileOutputStream out = new FileOutputStream(xlsxFilePath)) {

            Sheet sheet = workbook.createSheet("Sheet1");
            String[] nextLine;
            int rowNum = 0;
            while ((nextLine = reader.readNext()) != null) {
                Row currentRow = sheet.createRow(rowNum++);
                for (int i = 0; i < nextLine.length; i++) {
                    Cell currentCell = currentRow.createCell(i);
                    currentCell.setCellValue(nextLine[i]);
                }
            }
            workbook.write(out);
        }
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File deleted successfully.");
            } else {
                System.err.println("Failed to delete the file.");
            }
        } else {
            System.err.println("File not found.");
        }
    }



    public static boolean isVersionAndIdExist(String version, int id) throws IOException {
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        try {
            Row row = sheet.getRow(1); // Ligne où se trouvent l'ID et la version
            if (row != null) {
                int lastCellNum = row.getLastCellNum(); // Nombre total de colonnes dans la ligne

                // Parcourez les colonnes à partir de la troisième colonne (index 2)
                for (int i = 0; i < lastCellNum; i += 2) {
                    Cell idCell = row.getCell(i); // ID dans la colonne paire
                    Cell versionCell = row.getCell(i + 1); // Version dans la colonne impaire

                    // Affichez les valeurs des cellules pour le débogage
                    System.out.println("ID Cell Value: " + (idCell != null ? idCell.toString() : "null"));
                    System.out.println("Version Cell Value: " + (versionCell != null ? versionCell.toString() : "null"));

                    // Vérifiez si les cellules ne sont pas nulles et de type approprié
                    if (idCell != null && versionCell != null && idCell.getCellType() == CellType.NUMERIC && versionCell.getCellType() == CellType.STRING) {
                        // Récupérez les valeurs des cellules
                        int cellId = (int) idCell.getNumericCellValue();
                        String cellVersion = versionCell.getStringCellValue();

                        // Vérifiez si la paire ID-version existe déjà
                        if (cellId == id && cellVersion.equalsIgnoreCase(version)) {
                            // La paire ID-version existe déjà, retournez true
                            return true;
                        }
                    }
                }
            }
        } finally {
            // Fermez les flux dans le bloc finally pour vous assurer qu'ils sont toujours fermés
            workbook.close();
            fis.close();
        }

        // La paire ID-version n'existe pas, retournez false
        return false;
    }






    public static void addVersion(String version) throws IOException {
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        // Vérifiez si la version et l'ID existent déjà ensemble
        if (!isVersionAndIdExist(version, id)) {

            // Obtenez la première ligne (là où se trouvent les mesures originales)
            Row firstRow = sheet.getRow(0);

            // Créez une nouvelle cellule pour l'ID à côté des mesures originales
            Cell idCell = firstRow.createCell(firstRow.getLastCellNum());
            idCell.setCellValue("ID");

            // Créez une nouvelle cellule pour la version à côté de l'ID
            Cell versionCell = firstRow.createCell(firstRow.getLastCellNum());
            versionCell.setCellValue("Version");

            // Obtenez la deuxième ligne (là où se trouvent les valeurs des mesures originales)
            Row secondRow = sheet.getRow(1);

            // Créez une nouvelle cellule pour la valeur de l'ID à côté des mesures originales
            Cell idValueCell = secondRow.createCell(secondRow.getLastCellNum());
            idValueCell.setCellValue(id);

            // Créez une nouvelle cellule pour la valeur de la version à côté de l'ID
            Cell versionValueCell = secondRow.createCell(secondRow.getLastCellNum());
            versionValueCell.setCellValue(version);

            // Enregistrez les modifications dans le fichier Excel
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\original.xlsx");
            workbook.write(fos);
            fos.close();


        }
        setId(++id);
        System.out.println(id);



        // Fermez les flux
        workbook.close();
        fis.close();
    }



    public static void concatenateExcelFiles(String originalFilePath, String newFilePath) throws IOException {
        FileInputStream originalFis = new FileInputStream(originalFilePath);
        FileInputStream newFis = new FileInputStream(newFilePath);

        XSSFWorkbook originalWorkbook = new XSSFWorkbook(originalFis);
        XSSFWorkbook newWorkbook = new XSSFWorkbook(newFis);

        XSSFSheet originalSheet = originalWorkbook.getSheetAt(0);
        XSSFSheet newSheet = newWorkbook.getSheetAt(0);

        // Commence la concaténation à partir de la troisième ligne dans le fichier original
        int nextRowIndex = 2;

        int newLastRowNum = newSheet.getLastRowNum();
        CellStyle style = originalWorkbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        for (int i = 0; i <= newLastRowNum; i++) {
            Row newRow = newSheet.getRow(i);
            if (newRow == null) continue; // Vérifiez si la ligne du nouveau fichier est null

            boolean replaceRow = false; // Indique si la ligne doit être remplacée

            // Vérifier si la ligne contient les mots "Mesures Fitness" ou "Mesures Fashion"
            for (int j = 0; j < newRow.getLastCellNum(); j++) {
                Cell newCell = newRow.getCell(j);
                if (newCell != null && newCell.getCellType() == CellType.STRING) {
                    String cellValue = newCell.getStringCellValue();
                    if (cellValue != null && (cellValue.equals("Mesures Fitness") || cellValue.equals("Mesures Fashion"))) {
                        replaceRow = true;
                        break;
                    }
                }
            }

            if (replaceRow) {
                // Remplacer la ligne par la mesure suivante du nouveau fichier
                Row nextNewRow = newSheet.getRow(i + 1);
                if (nextNewRow != null) {
                    Row originalRow = originalSheet.getRow(nextRowIndex);
                    if (originalRow == null) {
                        originalRow = originalSheet.createRow(nextRowIndex); // Créez une nouvelle ligne si elle n'existe pas
                    }

                    int originalCellIndex = originalRow.getLastCellNum() >= 0 ? originalRow.getLastCellNum() : 0;

                    for (int j = 0; j < nextNewRow.getLastCellNum(); j++) {
                        Cell nextNewCell = nextNewRow.getCell(j);
                        if (nextNewCell == null) continue; // Vérifiez si la cellule du nouveau fichier est null

                        // Créer une nouvelle cellule à l'indice de colonne original
                        Cell originalCell = originalRow.createCell(originalCellIndex++);
                        copyCellValue(nextNewCell, originalCell);
                        originalCell.setCellStyle(style);
                    }

                    nextRowIndex++; // Passer à la prochaine ligne dans le fichier original
                }

                // Passer une fois de plus pour éviter la redondance
                i++;
            } else {
                // Copier la ligne normalement
                Row originalRow = originalSheet.getRow(nextRowIndex);
                if (originalRow == null) {
                    originalRow = originalSheet.createRow(nextRowIndex); // Créez une nouvelle ligne si elle n'existe pas
                }

                int originalCellIndex = originalRow.getLastCellNum() >= 0 ? originalRow.getLastCellNum() : 0;
                for (int j = 0; j < newRow.getLastCellNum(); j++) {
                    Cell newCell = newRow.getCell(j);
                    if (newCell == null) continue; // Vérifiez si la cellule du nouveau fichier est null

                    // Créer une nouvelle cellule à l'indice de colonne original
                    Cell originalCell = originalRow.createCell(originalCellIndex++);
                    copyCellValue(newCell, originalCell);
                    originalCell.setCellStyle(style);
                }

                nextRowIndex++; // Passer à la prochaine ligne dans le fichier original
            }
        }

        FileOutputStream fos = new FileOutputStream(originalFilePath);
        originalWorkbook.write(fos);

        originalWorkbook.close();
        newWorkbook.close();
        originalFis.close();
        newFis.close();
    }













    private static void copyCellValue(Cell sourceCell, Cell targetCell) {
        switch (sourceCell.getCellType()) {
            case STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(sourceCell)) {
                    targetCell.setCellValue(sourceCell.getDateCellValue());
                } else {
                    targetCell.setCellValue(sourceCell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                targetCell.setCellValue(sourceCell.getCellFormula());
                break;
            case BLANK:
                targetCell.setBlank();
                break;
            default:
                break;
        }
    }

    public static void setId(int id) {
        FileTransformation.id = id;
    }

    public static void deleteRow(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        Row row = sheet.getRow(21);
        if (row != null) {
            sheet.removeRow(row);
            // Décaler les lignes vers le haut pour remplir l'espace vide
            int lastRowNum = sheet.getLastRowNum();
            if (21 >= 0 && 21 < lastRowNum) {
                sheet.shiftRows(21 + 1, lastRowNum, -1);
            }
        }

        fileInputStream.close();

        // Sauvegarder les modifications dans le fichier
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();
    }

    public static void addColumn(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(fileInputStream);

        XSSFSheet sheet = workbook.getSheetAt(0); // Obtenez la première (et unique) feuille

        // Trouver le nombre de colonnes existantes
        int lastCellNum = sheet.getRow(2).getLastCellNum();

        // Créer un style pour les nouvelles cellules
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCellStyle style1 = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true); // Définir la police en gras

        for (int i = 0; i <= 25; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i); // Créez une nouvelle ligne si nécessaire
            }
            Cell cell = row.createCell(lastCellNum); // Créez une nouvelle cellule dans la nouvelle colonne
            if(i==0){
                cell.setCellValue("Marge de différence");
                style1.setFont(font);
                cell.setCellStyle(style1);
                sheet.setColumnWidth(lastCellNum, 5000);
            }
            else if(i==1){
                cell.setCellValue("");
                sheet.setColumnWidth(lastCellNum, 5000);

            }
            else{

                if(i==21){
                    continue;
                }
                else{
                    if ((row.getCell(lastCellNum-1).getCellType() == CellType.STRING)&&(row.getCell(1).getCellType() == CellType.NUMERIC)){
                        cell.setCellValue(Math.abs(Double.parseDouble(row.getCell(lastCellNum-1).getStringCellValue())-
                                 row.getCell(1).getNumericCellValue()));

                    }
                    else if((row.getCell(lastCellNum-1).getCellType() == CellType.STRING)&&(row.getCell(1).getCellType() == CellType.STRING)){
                        if(row.getCell(lastCellNum-1).getStringCellValue().equals(row.getCell(1).getStringCellValue())){
                            cell.setCellValue(("true"));
                        }
                        else{
                            cell.setCellValue("false");

                        }
                    }
                }
                //System.out.println("aaaaaaaaaaaaaa  "+row.getCell(lastCellNum-1).getStringCellValue());
                //cell.setCellValue(Math.abs(Double.parseDouble(row.getCell(lastCellNum-1).getStringCellValue())-
                      //  row.getCell(1).getNumericCellValue())); // Remplissez la cellule avec une valeur par défaut
                cell.setCellStyle(style); // Appliquer le style aux nouvelles cellules
                sheet.setColumnWidth(lastCellNum, 5000); // Définir la largeur de la nouvelle colonne
            }

        }

        fileInputStream.close();

        // Sauvegarder les modifications dans le fichier
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();
    }





























}
