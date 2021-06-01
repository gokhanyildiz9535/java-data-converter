package com.gokhanyildiz9535;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.github.pjfanning.xlsx.StreamingReader;
import com.google.gson.stream.JsonReader;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import com.opencsv.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class JavaDataConverter {

    /* ------------------------------------------------- */
    /* Static Values */

    public static final String __FILES__PATH__ = System.getProperty("user.dir") + "\\";
    public static final String __INTEGER__ = "Integer";
    public static final String __DOUBLE__ = "Double";
    public static final String __STRING__ = "String";
    public static final int __EXCEL__ROW__SIZE__ = 1048576;

    /* ------------------------------------------------- */
    /* Constructor */

    public JavaDataConverter() {
    }

    /* ------------------------------------------------- */
    /* Convert Functions */

    public void convert_XLSX_to_CSV(String inputFilePath, String outputFilePath, String sheetName) {
        ArrayList<ArrayList<Object>> gal = readXLSX_to_ARRAYLIST(inputFilePath, sheetName);
        writeCSV(outputFilePath, gal);
    }

    public void convert_CSV_to_XLSX(String inputFilePath, String outputFilePath, String sheetName) {
        try {
            ArrayList<ArrayList<Object>> csvObjectsAll = readCSV_to_ARRAYLIST_with_OpenCSV(inputFilePath);
            writeXLSX_controlWith_EXCEL_MAXROWSIZE(outputFilePath,sheetName,csvObjectsAll);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void convert_JSON_to_XLSX(String inputFilePath, String outputFilePath, String sheetName, String jsonObjectName, boolean jsonObjectArray) {
        ArrayList<ArrayList<Object>> rows = readJSON_to_ARRAYLIST(inputFilePath, jsonObjectName, jsonObjectArray);
        writeXLSX_controlWith_EXCEL_MAXROWSIZE(outputFilePath,sheetName, rows);
    }

    public void convert_CSV_to_JSON(String inputFilePath, String outputFilePath, String jsonObjectName, boolean jsonObjectArray) {
        ArrayList<ArrayList<Object>> csv = readCSV_to_ARRAYLIST_with_OpenCSV(inputFilePath);
        ArrayList<HashMap<String, Object>> hm = convert_ARRAYLIST_to_HASHMAPLIST(csv);
        csv.clear();
        writeJSON(outputFilePath, hm, jsonObjectName, jsonObjectArray);
    }

    public void convert_XLS_to_XLSX(String inputFilePath, String outputFilePath, String sheetName) {

        try {
            FileInputStream fos = new FileInputStream(inputFilePath);
            HSSFWorkbook wb = new HSSFWorkbook(fos);
            HSSFSheet sheet = wb.getSheet(sheetName);
            ArrayList<ArrayList<Object>> eachRow = new ArrayList<>();
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                HSSFRow row = sheet.getRow(i);
                ArrayList<Object> eachCell = new ArrayList<>();
                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    HSSFCell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    eachCell.add(cell.toString());
                }
                eachRow.add(eachCell);
            }
            writeXLSX_horizontally(outputFilePath, eachRow, sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void convert_XLSX_to_JSON(String inputFilePath, String outputFilePath, String jsonObjectName, boolean jsonObjectArray, String sheetName) {
        ArrayList<ArrayList<Object>> gal = readXLSX_to_ARRAYLIST(inputFilePath, sheetName);
        convert_ARRAYLIST_to_JSON(outputFilePath, gal, jsonObjectName, jsonObjectArray);
    }

    public ArrayList<ArrayList<Object>> convert_MAPLIST_to_ARRAYLIST(ArrayList<Map<String, Object>> jsonObjects) {

        ArrayList<ArrayList<Object>> rows = new ArrayList<>();

        // get column names:
        TreeSet<String> columnNames = new TreeSet<>();
        for (int i = 0; i < jsonObjects.size(); i++) {
            for (String key : jsonObjects.get(i).keySet()) {
                columnNames.add(key);
            }
        }

        // traversal json objects:
        rows.add(new ArrayList<>(columnNames));
        for (Map<String, Object> row : jsonObjects) {
            ArrayList<Object> cells = new ArrayList<>();
            for (String key : columnNames) {
                cells.add(row.get(key));
            }
            rows.add(cells);
        }

        return rows;
    }

    public ArrayList<ArrayList<Object>> convert_HASHMAPLIST_to_ARRAYLIST(ArrayList<HashMap<String, Object>> jsonObjects) {

        ArrayList<ArrayList<Object>> rows = new ArrayList<>();

        // get column names:
        TreeSet<String> columnNames = new TreeSet<>();
        for (int i = 0; i < jsonObjects.size(); i++) {
            for (String key : jsonObjects.get(i).keySet()) {
                columnNames.add(key);
            }
        }

        // traversal json objects:
        rows.add(new ArrayList<>(columnNames));
        for (HashMap<String, Object> row : jsonObjects) {
            ArrayList<Object> cells = new ArrayList<>();
            for (String key : columnNames) {
                cells.add(row.get(key));
            }
            rows.add(cells);
        }

        return rows;
    }

    public ArrayList<HashMap<String, Object>> convert_ARRAYLIST_to_HASHMAPLIST(ArrayList<ArrayList<Object>> arrList) {
        ArrayList<Object> columnNames = arrList.get(0);
        ArrayList<HashMap<String, Object>> map = new ArrayList<>();
        for (int i = 1; i < arrList.size(); i++) {
            HashMap<String, Object> newHasMap = new HashMap<>();
            for (int j = 0; j < columnNames.size(); j++) {
                try {
                    newHasMap.put(columnNames.get(j).toString(),
                            arrList.get(i).get(j).toString());
                } catch (Exception e){ }
            }
            map.add(newHasMap
//                    .entrySet().stream()
//                    .sorted(Map.Entry.comparingByKey())
//                    .collect(Collectors.toMap(
//                            Map.Entry::getKey,
//                            Map.Entry::getValue,
//                            (a, b) -> {
//                                throw new AssertionError();
//                            },
//                            LinkedHashMap::new
//                    ))
            );
        }
        return map;
    }

    public String convert_ARRAYLIST_to_CSV_STRING(ArrayList<ArrayList<Object>> gal) {
        String csvString = new String();
        for (int i = 0; i < gal.size(); i++) {
            String rowString = new String();
            for (int j = 0; j < gal.get(i).size()/*o.size()*/; j++) {
                if (gal.get(i).get(j).equals(Statics.EMPTYSTR))
                    rowString += Statics.EMPTYSTR + ",";
                else
                    rowString += gal.get(i).get(j).toString() + ",";
            }
            csvString += rowString.substring(0, rowString.length() - 1) + "\n";
        }
        return csvString;
    }

    /* ------------------------------------------------- */
    /* JSON Process */

    public ArrayList<ArrayList<Object>> readJSON_to_ARRAYLIST(String inputFilePath, String jsonObjectName, boolean jsonObjectArray) {
        ArrayList<Map<String, Object>> jsonObjects = readJSON_to_MAPLIST(inputFilePath, jsonObjectName, jsonObjectArray);
        ArrayList<ArrayList<Object>> rows = convert_MAPLIST_to_ARRAYLIST(jsonObjects);
        return rows;
    }

    public ArrayList<Map<String, Object>> readJSON_to_MAPLIST(String inputFilePath, String jsonObjectName, boolean jsonObjectArray) {
        ArrayList<Map<String, Object>> jsonObjects = new ArrayList<>();
        try {
            Path path = Paths.get(inputFilePath);
            InputStream inputStream = Files.newInputStream(path);
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();

            if (jsonObjectArray==false && !jsonObjectName.equals(Statics.EMPTYSTR)) {
                Map<String, Object> data = new Gson().fromJson(reader, type);
                // Specific JSON Object with sheet name
                jsonObjects = (ArrayList<Map<String, Object>>) data.get(jsonObjectName);
            } else if (jsonObjectArray) {
                // buffered reading:
                reader.beginArray();
                while (reader.hasNext()) {
                    jsonObjects.add(new Gson().fromJson(reader, type));
                }
                reader.endArray();
            }
            else{
                // JSON Object without specific sheet name
                // We need specific wrapper class for serializer
            }
        } catch (Exception e) { }
        return jsonObjects;
    }

    public void convert_ARRAYLIST_to_JSON(String outputFilePath, ArrayList<ArrayList<Object>> gal, String jsonObjectName, boolean jsonObjectArray) {
        ArrayList<HashMap<String, Object>> map = convert_ARRAYLIST_to_HASHMAPLIST(gal);
        writeJSON(outputFilePath, map, jsonObjectName, jsonObjectArray);
    }

    public void writeJSON(String outputFilePath, ArrayList<HashMap<String, Object>> map, String jsonObjectName, boolean jsonObjectArray) {
        JsonObject completeJson = new JsonObject();
        try {
            Gson gson = new Gson();
            JsonArray jsonArr = new JsonArray();
            // unbuffered writing:
            for (HashMap<String, Object> m : map) {
                JsonElement jsonElement = gson.toJsonTree(m
                        .entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (a, b) -> {
                                    throw new AssertionError();
                                },
                                LinkedHashMap::new
                        ))
                );
                jsonArr.add(jsonElement);
            }
            PrintWriter pw = new PrintWriter(outputFilePath);
            if (jsonObjectArray == false) {
                // JSON Object
                completeJson.add(jsonObjectName, jsonArr);
                pw.write(completeJson.toString());
            } else {
                // JSON Array
                pw.write(jsonArr.toString());
            }
            pw.close();

        } catch (Exception e) {
        }
    }

    public ArrayList<HashMap<String, Object>> convert_JSON_to_HASHMAPLIST(String inputFilePath, String jsonObjectName, boolean jsonObjectArray) {
        ArrayList<ArrayList<Object>> jsonObjects = readJSON_to_ARRAYLIST(inputFilePath, jsonObjectName, jsonObjectArray);
        ArrayList<HashMap<String, Object>> hm = convert_ARRAYLIST_to_HASHMAPLIST(jsonObjects);
        return hm;
    }

    public void convert_HASHMAPLIST_to_JSON(ArrayList<HashMap<String, Object>> hashMap, String outputFilePath, String jsonObjectName, boolean jsonObjectArray) {
        //ArrayList<ArrayList<Object>> arr = convert_HASHMAPLIST_to_ARRAYLIST(hashMap);
        //convert_ARRAYLIST_to_JSON(outputFilePath, arr, jsonObjectName, jsonObjectArray);
        //or
        writeJSON(outputFilePath, hashMap, jsonObjectName, jsonObjectArray);
    }


    /* ------------------------------------------------- */
    /* Write XLSX Process */

    public void writeXLSX_horizontally(String outputFileName, ArrayList<ArrayList<Object>> gdl, String sheetName) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet(sheetName);
        int rowNum = 0;
        // buffered writing with SXSSF:
        for (ArrayList<Object> objectArrList : gdl) {
            Row row = sheet.createRow(rowNum++);
            int cellNum = 0;
            for (Object obj : objectArrList) {
                Cell cell = row.createCell(cellNum++);
                boolean is_double = false, is_integer = false;
                try {
                    cell.setCellValue(Double.parseDouble(obj.toString()));
                    is_double = true;
                } catch (Exception e) {
                }
                if (!is_double)
                    try {
                        cell.setCellValue(Integer.parseInt(obj.toString()));
                        is_integer = true;
                    } catch (Exception e) {
                    }
                if (!is_double && !is_integer)
                    if (obj == Statics.EMPTYSTR)
                        cell.setCellValue(Statics.EMPTYSTR);
                    else
                        cell.setCellValue(obj.toString());
            }
        }
        try {
            FileOutputStream file = new FileOutputStream(outputFileName);
            workbook.write(file);
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeXLSX_vertically(String outputFileName, ArrayList<ArrayList<Object>> gdl, String sheetName) {
        writeXLSX_controlWith_EXCEL_MAXROWSIZE(outputFileName,sheetName,get_ARRAYLIST_TRANSPOSE(gdl));
//        writeXLSX_horizontally(outputFileName, get_ARRAYLIST_TRANSPOSE(gdl), sheetName);
    }

    public void writeXLSX_controlWith_EXCEL_MAXROWSIZE(String outputFilePath, String sheetName, ArrayList<ArrayList<Object>> arr) {
        try {
//            ArrayList<ArrayList<Object>> csvObjectsAll = readCSV_to_ARRAYLIST(inputFilePath);
            int csvRowSize = arr.size();
            if (csvRowSize > __EXCEL__ROW__SIZE__) {
                System.out.println("Excel row size exceed. Multiple XLSX file will create.");
                int XLSXFileCount = (int) Math.ceil(
                        (double) csvRowSize / (double) __EXCEL__ROW__SIZE__
                );
                int startingIndex = 0;
                int endingIndex = 0;
                String newFileName = outputFilePath.replace(".xlsx", "");
                for (int i = 0; i < XLSXFileCount - 1; i++) {
                    System.out.println(String.format("Starting convert CSV to XLSX File %s...", i + 1));
                    endingIndex += __EXCEL__ROW__SIZE__;
                    String newFileName_LOOP = newFileName+String.format("_%s.xlsx", i + 1);
                    writeXLSX_horizontally(newFileName_LOOP,
                            get_ARRAYLIST_SPECIFIC_RANGE(arr, startingIndex, endingIndex), sheetName);
                    startingIndex += __EXCEL__ROW__SIZE__;
                }
                newFileName = outputFilePath.replace(".xlsx", "");
                if (true) {
                    System.out.println(String.format("Starting convert CSV to XLSX File %s...", XLSXFileCount));
                    newFileName += String.format("_%s.xlsx", XLSXFileCount);
                    int finalIndex = csvRowSize - endingIndex;
                    endingIndex += finalIndex;
                    writeXLSX_horizontally(newFileName,
                            get_ARRAYLIST_SPECIFIC_RANGE(arr, startingIndex, endingIndex), sheetName);
                }
            } else {
                writeXLSX_horizontally(outputFilePath, arr, sheetName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ------------------------------------------------- */
    /* CSV Process */

    public void writeCSV(String outputFileName, ArrayList<ArrayList<Object>> gdl) {
        try {
            // buffered writing:
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(outputFileName));
            for (ArrayList<Object> cells : gdl) {
                String csvObjects = new String();
                int cellIndex = 0;
                for (Object cell : cells) {
                    if (cells.size() - 1 == cellIndex) {
                        if (cell.equals(Statics.EMPTYSTR))
                            csvObjects += "";
                        else
                            csvObjects += cell.toString();
                    } else {
                        if (cell.equals(Statics.EMPTYSTR))
                            csvObjects += "" + ",";
                        else
                            csvObjects += cell.toString() + ",";
                    }
                    cellIndex++;
                }
                csvObjects += "\n";
                csvWriter.write(csvObjects);
            }
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Object>> readCSV_to_ARRAYLIST(String inputFilePath) {
        ArrayList<ArrayList<Object>> gal = new ArrayList<>();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(inputFilePath));
            String row;
            int rowSize = 0;
            ArrayList<String> columnList = new ArrayList<>();
            // buffered reading:
            while ((row = csvReader.readLine()) != null) {
                ArrayList<Object> rowCells = new ArrayList<>();
                if (rowSize == 0) {
                    if (row.charAt(row.length() - 1) == ',')
                        throw new Exception("CSV Format Error");
                    for (String columnName : row.split(",")) {
                        columnList.add(columnName);
                    }
                }
                int cellSize = 0;
                for (String cell : row.split(",")) {
                    if (cell.equals(Statics.EMPTYSTR)) {
                        rowCells.add(Statics.EMPTYSTR);
                    } else {
                        rowCells.add(cell);
                    }
                    cellSize++;
                }
                if (cellSize != columnList.size()) {
                    for (int i = 0; i < columnList.size() - cellSize; i++) {
                        rowCells.add(Statics.EMPTYSTR);
                    }
                }
                gal.add(rowCells);
                rowSize++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gal;
    }

    public ArrayList<ArrayList<Object>> readCSV_to_ARRAYLIST_with_OpenCSV(String inputFilePath){
        ArrayList<ArrayList<Object>> gal = new ArrayList<>();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(inputFilePath));
            CSVReader csvReader = new CSVReader(reader);
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                ArrayList<Object> lineObjects = new ArrayList<>();
                for(String cell : line)
                    lineObjects.add(cell);
                gal.add(lineObjects);
            }
            reader.close();
            csvReader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return gal;
    }

    /* ------------------------------------------------- */
    /* Read XLSX Functions */

    public ArrayList<ArrayList<Object>> readXLSX_to_ARRAYLIST(String inputFilePath, String sheetName) {
        ArrayList<ArrayList<Object>> rows = new ArrayList<>();
        try {
            File f = new File(inputFilePath);
            // buffered reading:
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(f);            // InputStream or File for XLSX file (required)
            for (Row row : workbook.getSheet(sheetName)) {
                ArrayList<Object> cells = new ArrayList<>();
                for (Cell cell : row) {
                    if (cell.getStringCellValue().equals(Statics.EMPTYSTR))
                        cells.add(Statics.EMPTYSTR);
                    else if (cell.getCellType().toString().equals(Statics.NUM)) {
                        if ((double) cell.getNumericCellValue() % 1 == 0)
                            cells.add((int) cell.getNumericCellValue());
                        else
                            cells.add(cell.getNumericCellValue());
                    } else
                        cells.add(cell.getStringCellValue());
                }
                rows.add(cells);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    public ArrayList<ArrayList<Object>> readXLSX_getColumnNames(String inputFilePath, String sheetName) {
        ArrayList<ArrayList<Object>> gal = new ArrayList<>();
        try {
            gal.add(readXLSX_to_ARRAYLIST(inputFilePath, sheetName).get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gal;
    }

    public ArrayList<ArrayList<Object>> readXLSX_getSpecificColumnById(String inputFilePath, int columnIndex, String sheetName) {
        ArrayList<ArrayList<Object>> gal = new ArrayList<>();
        try {
            ArrayList<Object> columnDataList = new ArrayList<>();
            ArrayList<ArrayList<Object>> data = readXLSX_to_ARRAYLIST(inputFilePath, sheetName);
            for (int i = 0; i < data.size(); i++) {
                columnDataList.add(data.get(i).get(columnIndex));
            }
            gal.add(columnDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gal;
    }

    public ArrayList<ArrayList<Object>> readXLSX_getSpecificColumnByIdList(String inputFilePath, int[] columnIndexList, String sheetName) {
        ArrayList<ArrayList<Object>> gal = new ArrayList<>();
        try {
            ArrayList<ArrayList<Object>> data = readXLSX_to_ARRAYLIST(inputFilePath, sheetName);
            for (int j = 0; j < columnIndexList.length; j++) {
                ArrayList<Object> rowArrayList = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    rowArrayList.add(data.get(i).get(columnIndexList[j]));
                }
                gal.add(rowArrayList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gal;
    }

    public ArrayList<ArrayList<Object>> readXLSX_getSpecificColumnByName(String inputFilePath, String columnName, String sheetName) {
        ArrayList<ArrayList<Object>> gcn = readXLSX_getColumnNames(inputFilePath, sheetName);
        ArrayList<ArrayList<Object>> gal = new ArrayList<>();
        for (ArrayList<Object> objectArrList : gcn) {
            for (int i = 0; i < objectArrList.size(); i++) {
                if (objectArrList.get(i).toString().equals(columnName))
                    gal = readXLSX_getSpecificColumnById(inputFilePath, i, sheetName);
            }
        }
        return gal;
    }

    public ArrayList<ArrayList<Object>> readXLSX_getSpecificColumnByNameList(String inputFilePath, String[] columnNameList, String sheetName) {
        ArrayList<ArrayList<Object>> gcn = readXLSX_getColumnNames(inputFilePath, sheetName);
        ArrayList<ArrayList<Object>> gal;
        int[] idList = new int[columnNameList.length];
        for (int k = 0; k < columnNameList.length; k++) {
            for (ArrayList<Object> objectArrList : gcn) {
                for (int i = 0; i < objectArrList.size(); i++) {
                    if (objectArrList.get(i).toString().equals(columnNameList[k])) {
                        idList[k] = i;
                    }
                }
            }
        }
        gal = readXLSX_getSpecificColumnByIdList(inputFilePath, idList, sheetName);
        return gal;
    }

    public ArrayList<ArrayList<Object>> readXLSX_getSpecificRowsByColumnId(String inputFilePath, int columnIndex, String cellName, String sheetName) {
        ArrayList<ArrayList<Object>> gal = new ArrayList<>();
        try {
            ArrayList<ArrayList<Object>> data = readXLSX_to_ARRAYLIST(inputFilePath, sheetName);
            for (int i = 0; i < data.size(); i++) {
                ArrayList<Object> rowArrayList = new ArrayList<>();
                try {
                    if (data.get(i).get(columnIndex).equals(cellName)) {
                        for (int j = 0; j < data.get(i).size(); j++) {
                            rowArrayList.add(data.get(i).get(j));
                        }
                        gal.add(rowArrayList);
                    }
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gal;
    }

    public ArrayList<ArrayList<Object>> readXLSX_getSpecificRowByColumnName(String inputFilePath, String columnName, String cellName, String sheetName) {
        ArrayList<ArrayList<Object>> gcn = readXLSX_getColumnNames(inputFilePath, sheetName);
        ArrayList<ArrayList<Object>> gal = new ArrayList<>();
        for (ArrayList<Object> objectArrList : gcn) {
            for (int i = 0; i < objectArrList.size(); i++) {
                if (objectArrList.get(i).toString().equals(columnName))
                    gal = readXLSX_getSpecificRowsByColumnId(inputFilePath, i, cellName, sheetName);
            }
        }
        return gal;
    }

    public ArrayList<ArrayList<Object>> readXLSX_getSpecificRowById(String inputFilePath, int rowIndex, String sheetName) {
        ArrayList<ArrayList<Object>> gal = new ArrayList<>();
        try {
            ArrayList<Object> rwArrList = new ArrayList<>();
            ArrayList<ArrayList<Object>> data = readXLSX_to_ARRAYLIST(inputFilePath, sheetName);
            for (int i = 0; i < data.get(rowIndex).size(); i++)
                rwArrList.add(data.get(rowIndex).get(i));
            gal.add(rwArrList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gal;
    }

    public ArrayList<HashMap<String, Object>> readXLSX_getColumnAndFileInfo(String inputFilePath, String sheetName) {
        HashMap<String, Object> ctl = new HashMap<>();
        long bytes = Long.MIN_VALUE;
        String fileName = new String();
        try {
            Path path = Paths.get(inputFilePath);
            bytes = Files.size(path);
            fileName = path.getFileName().toString();
        } catch (Exception e) {
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        ArrayList<Object> gcn = readXLSX_getColumnNames(inputFilePath, sheetName).get(0);

        for (int i = 0; i < gcn.size(); i++) {

            System.out.println("Col Size: " + gcn.size() + " Current: " + i + " is working...");
            ArrayList<ArrayList<Object>> gcd = readXLSX_getSpecificColumnById(inputFilePath, i, sheetName);

            for (ArrayList<Object> columnData : gcd) {

                String columnType = get_COLUMN_TYPE(columnData);
                double[] minMaxValue = get_MinMaxValue_SELECTED_COLUMN(columnType, columnData);

                if (columnType == __INTEGER__) {
                    ctl.put(columnData.get(0).toString(), new ArrayList<>
                            (Arrays.asList(columnType, // Column Type <<STR>>
                                    Integer.valueOf((int) minMaxValue[0]), // Minimum value <<STR>>
                                    Integer.valueOf((int) minMaxValue[1])))); // Maximum value <<STR>>
                } else if (columnType == __DOUBLE__) {
                    ctl.put(columnData.get(0).toString(), new ArrayList<>
                            (Arrays.asList(columnType, // Column Type <<STR>>
                                    Double.valueOf(minMaxValue[0]), // Minimum value <<STR>>
                                    Double.valueOf(minMaxValue[1])))); // Maximum value <<STR>>
                } else
                    ctl.put(columnData.get(0).toString(), columnType);
            }
        }

        ArrayList<ArrayList<Object>> myFile = readXLSX_to_ARRAYLIST(inputFilePath, sheetName);
        ctl.put("Row Size", new String(String.valueOf(myFile.size() - 1)));
        ctl.put("Column Size", new String(String.valueOf(myFile.get(0).size())));
        ctl.put("File Size", new String(bytes + " byte"));
        ctl.put("Created Date", new String(formatter.format(date)));
        ctl.put("File Name", new String(String.valueOf(fileName)));

        return new ArrayList<>(Arrays.asList(ctl));
    }

    /* ------------------------------------------------- */
    /* Other Components */

    public String get_COLUMN_TYPE(ArrayList<Object> obj) {
        int[] typo = new int[3];
        int typeFinder = 0;
        for (int j = 1; j < obj.size(); j++) {

            int doubleControl = 0;
            Object object = new Object();

            try {
                object = Double.parseDouble(obj.get(j).toString());
                doubleControl = 1;
            } catch (Exception e) {
            }

            if (doubleControl == 1) {
                if ((double) object % 1 == 0)
                    typo[1]++;
                else
                    typo[2]++;
            } else if (obj.get(j).equals(Statics.EMPTYSTR)) {
                // cell is empty case
            } else
                typo[0]++;

            typeFinder = Arrays.asList(typo[0], typo[1], typo[2]).indexOf(Collections.max(Arrays.asList(typo[0], typo[1], typo[2])));
        }

        if (typeFinder == 2)
            return __DOUBLE__;
        else if (typeFinder == 1)
            return __INTEGER__;
        else
            return __STRING__;
    }

    public double[] get_MinMaxValue_SELECTED_COLUMN(String columnType, ArrayList<Object> obj) {
        double[] values = new double[2];
        int intMax = Integer.MIN_VALUE, intMin = Integer.MAX_VALUE;
        double doubleMax = Double.MIN_VALUE, doubleMin = Double.MAX_VALUE;
        if (columnType.equals(__DOUBLE__)) {
            for (int k = 1; k < obj.size(); k++) {
                try {
                    double temp = Double.parseDouble(obj.get(k).toString());
                    if (doubleMin > temp)
                        doubleMin = temp;
                    if (doubleMax < temp)
                        doubleMax = temp;
                } catch (Exception e) {

                }
            }
            values[0] = doubleMin;
            values[1] = doubleMax;
        }
        if (columnType.equals(__INTEGER__)) {
            for (int k = 1; k < obj.size(); k++) {
                try {
                    double db = Double.parseDouble(obj.get(k).toString());
                    int temp = (int) db;
                    if (intMin > temp)
                        intMin = temp;
                    if (intMax < temp)
                        intMax = temp;
                } catch (Exception e) {

                }
            }
            values[0] = intMin;
            values[1] = intMax;
        }
        return values;
    }

    public ArrayList<ArrayList<Object>> get_ARRAYLIST_TRANSPOSE(ArrayList<ArrayList<Object>> gal) {
        ArrayList<ArrayList<Object>> arr = new ArrayList<>();
        int NEW_ARRAY_COLUMN = gal.size();
        int NEW_ARRAY_ROW = gal.get(gal.size() - 1).size();
        for (int i = 0; i < NEW_ARRAY_ROW; i++) {
            ArrayList<Object> row = new ArrayList<>();
            for (int j = 0; j < NEW_ARRAY_COLUMN; j++) {
                row.add(gal.get(j).get(i).toString());
            }
            arr.add(row);
        }
        return arr;
    }

    public ArrayList<ArrayList<Object>> get_ARRAYLIST_SPECIFIC_RANGE(ArrayList<ArrayList<Object>> arr, int startWith, int endWith) {
        ArrayList<ArrayList<Object>> sr = new ArrayList<>();
        for (int i = startWith; i < endWith; i++) {
            sr.add(arr.get(i));
        }
        return sr;
    }

    /* ------------------------------------------------- */
}

