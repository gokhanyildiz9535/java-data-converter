# Java Data Converter

Hello to everyone. This repository is based on switching between some commonly used file extensions for java-based data processing and datasets. Thanks to this library, files created in a certain format environment are converted to each other. You can also include the jar file created in the project to your existing project. In this case, you can use the file extensions used for datasets as objects in your own java project.

## Install

- This repository requires Java 8 or greater.
- If you want to use it as a jar file, please don't forget to set your PATH for java.

## Some Features of the Java Data Converter

- Thanks to JDC, there is a conversion between commonly used files for data set storage.
- By means of this process, the data becomes java object arraylist and then operations are performed according to (source, destination, options) arguments.
- In the generally used java object, ArrayList:ArrayList:Object list is used.
- ArrayList:ArrayList in this list refers to the rows in the dataset/datasheet.
- ArrayList:Object refers to the cells in each row.
- There are some basic reading functions associated with this dataset.
- There are horizontal or vertical file write functions related to this dataset.
- There are functions that convert between these dataset file extensions.
- There is a function to get detailed information about a dataset file.

## Note

- There is a sample dataset in the project. You need to create xls, xlsx, csv, json files under the format used in this dataset. To access the sample datasets go to [here](../main/Docs/File%20Format).
- Libraries used in the project environment [here](../main/Docs/libs.png).
- If you want to import and use your own project, you can access the JAR file [here](../main/Output).
- Many of the functions are designed to be **unbuffered**. Conversion does not take place directly. A raw file is first converted to java object array. Then this array is used to create a different raw file.
- Defining **buffered** functions will only be done while performing database operations.

## Usage with Jar File

```

Run java -jar "Java-Data-Converter.jar" "--help"

C01: Convert XLSX to CSV

Arguments: Source, Destination, SheetName
Example:
java -jar "java-data-converter.jar" "C01" "D:\dataset.xlsx" "D:\dataset.csv" "MySheet"

C02: Convert CSV to XLSX

Arguments: Source, Destination, SheetName
Example:
java -jar "java-data-converter.jar" "C02" "D:\dataset.csv" "D:\dataset.xlsx" "MySheet"

C03: Convert XLSX to JSON

Arguments: Source, Destination, SheetName, JsonObjectName, JsonObjectArray
Option 1:
java -jar "java-data-converter.jar" "C03" "D:\dataset.xlsx" "D:\dataset_json_array.json" "MySheet" "MyDataset" "true"
Option 2:
java -jar "java-data-converter.jar" "C03" "D:\dataset.xlsx" "D:\dataset_json_object.json" "MySheet" "MyDataset" "false"

C04: Convert JSON to XLSX

Arguments: Source, Destination, SheetName, JsonObjectName, JsonObjectArray
Option 1:
java -jar "java-data-converter.jar" "C04" "D:\dataset_json_array.json" "D:\dataset.xlsx" "MySheet" "MyDataset" "true"
Option 2:
java -jar "java-data-converter.jar" "C04" "D:\dataset_json_object.json" "D:\dataset.xlsx" "MySheet" "MyDataset" "false"

C05: Convert CSV to JSON

Arguments: Source, Destination, JsonObjectName, JsonObjectArray
Option 1:
java -jar "java-data-converter.jar" "C05" "D:\dataset.csv" "D:\dataset.json" "null" "MyDataset" "true"
Option 2:
java -jar "java-data-converter.jar" "C05" "D:\dataset.csv" "D:\dataset.json" "null" "MyDataset" "false"

C06: Convert XLS to XLSX

Arguments: Source, Destination, SheetName
Example:
java -jar "java-data-converter.jar" "C06" "D:\dataset.xls" "D:\dataset.xlsx" "MySheet"

C07: Get XLSX File Information

Arguments: Source, Destination, SheetName, JsonObjectName, JsonObjectArray
Option 1:
java -jar "java-data-converter.jar" "C07" "D:\dataset.xlsx" "D:\dataset_info_ja.json" "MySheet" "MyDataset" "true"
Option 2:
java -jar "java-data-converter.jar" "C07" "D:\dataset.xlsx" "D:\dataset_info_jo.json" "MySheet" "MyDataset" "false"

Important:
1. Most of the functions run unbuffered, because of converting java objects array. However, some read/write functions are defined as buffered.
2. For yourself, directly overriding the functions for use in your project can allow you to use buffered read/write process.
3. If you use jar file with args, please insert full path of the files.
4. If you want to convert larger than or around 2MB file then set the -Xms256M -Xmx4096M
5. or larger than 100MB, smaller or around file then set the -Xms4096M -Xmx32768M parameter.
6. -Xmx(YOUR_MEMORY*1024)M parameter defines that is java virtual machine maximum heap space.

Example: java -Xms256M -Xmx4096M -jar "java-data-converter.jar" "C01" "D:\dataset.xlsx" "D:\dataset.csv" "MySheet" "MyDataset" "true"

```

## Usage with Import Library

```
Methods:

void convert_XLSX_to_CSV(String inputFilePath, String outputFilePath, String sheetName)
void convert_CSV_to_XLSX(String inputFilePath, String outputFilePath, String sheetName)
void convert_JSON_to_XLSX(String inputFilePath, String outputFilePath, String sheetName, String jsonObjectName, boolean jsonObjectArray)
void convert_CSV_to_JSON(String inputFilePath, String outputFilePath, String jsonObjectName, boolean jsonObjectArray)
void convert_XLS_to_XLSX(String inputFilePath, String outputFilePath, String sheetName)
void convert_XLSX_to_JSON(String inputFilePath, String outputFilePath, String jsonObjectName, boolean jsonObjectArray, String sheetName)
void convert_HASHMAPLIST_to_JSON(ArrayList<HashMap<String, Object>> hashMap, String outputFilePath, String jsonObjectName, boolean jsonObjectArray)
void convert_ARRAYLIST_to_JSON(String outputFilePath, ArrayList<ArrayList<Object>> gal, String jsonObjectName, boolean jsonObjectArray)
ArrayList<ArrayList<Object>> convert_MAPLIST_to_ARRAYLIST(ArrayList<Map<String, Object>> jsonObjects)
ArrayList<ArrayList<Object>> convert_HASHMAPLIST_to_ARRAYLIST(ArrayList<HashMap<String, Object>> jsonObjects)
ArrayList<HashMap<String, Object>> convert_ARRAYLIST_to_HASHMAPLIST(ArrayList<ArrayList<Object>> arrList)
ArrayList<HashMap<String, Object>> convert_JSON_to_HASHMAPLIST(String inputFilePath, String jsonObjectName, boolean jsonObjectArray)

ArrayList<ArrayList<Object>> readJSON_to_ARRAYLIST(String inputFilePath, String jsonObjectName, boolean jsonObjectArray)
ArrayList<Map<String, Object>> readJSON_to_MAPLIST(String inputFilePath, String jsonObjectName, boolean jsonObjectArray)
void writeJSON(String outputFilePath, ArrayList<HashMap<String, Object>> map, String jsonObjectName, boolean jsonObjectArray)
void writeXLSX_horizontally(String outputFileName, ArrayList<ArrayList<Object>> gdl, String sheetName)
void writeXLSX_vertically(String outputFileName, ArrayList<ArrayList<Object>> gdl, String sheetName)
void writeXLSX_controlWith_EXCEL_MAXROWSIZE(String outputFilePath, String sheetName, ArrayList<ArrayList<Object>> arr)
void writeCSV(String outputFileName, ArrayList<ArrayList<Object>> gdl)

ArrayList<ArrayList<Object>> readCSV_to_ARRAYLIST(String inputFilePath)
ArrayList<ArrayList<Object>> readCSV_to_ARRAYLIST_with_OpenCSV(String inputFilePath)
ArrayList<ArrayList<Object>> readXLSX_to_ARRAYLIST(String inputFilePath, String sheetName)
ArrayList<ArrayList<Object>> readXLSX_getColumnNames(String inputFilePath, String sheetName)
ArrayList<ArrayList<Object>> readXLSX_getSpecificColumnById(String inputFilePath, int columnIndex, String sheetName)
ArrayList<ArrayList<Object>> readXLSX_getSpecificColumnByIdList(String inputFilePath, int[] columnIndexList, String sheetName)
ArrayList<ArrayList<Object>> readXLSX_getSpecificColumnByName(String inputFilePath, String columnName, String sheetName)
ArrayList<ArrayList<Object>> readXLSX_getSpecificColumnByNameList(String inputFilePath, String[] columnNameList, String sheetName)
ArrayList<ArrayList<Object>> readXLSX_getSpecificRowsByColumnId(String inputFilePath, int columnIndex, String cellName, String sheetName)
ArrayList<ArrayList<Object>> readXLSX_getSpecificRowByColumnName(String inputFilePath, String columnName, String cellName, String sheetName)
ArrayList<ArrayList<Object>> readXLSX_getSpecificRowById(String inputFilePath, int rowIndex, String sheetName)
ArrayList<ArrayList<Object>> get_ARRAYLIST_TRANSPOSE(ArrayList<ArrayList<Object>> gal)
ArrayList<ArrayList<Object>> get_ARRAYLIST_SPECIFIC_RANGE(ArrayList<ArrayList<Object>> arr, int startWith, int endWith)
ArrayList<HashMap<String, Object>> readXLSX_getColumnAndFileInfo(String inputFilePath, String sheetName)

void printGenericArrList(ArrayList<ArrayList<Object>> gal)
void printHashMapTypeArrList(HashMap<String, Object> map)
String generateRandomString(int totalCharacters, int numberCount, int upperCaseCount)
String convert_ARRAYLIST_to_CSV_STRING(ArrayList<ArrayList<Object>> gal)
String get_COLUMN_TYPE(ArrayList<Object> obj)
ArrayList<ArrayList<Object>> generateGenericArrList(ArrayList<Object> columnNames, int rowSize, int columnSize, int nullSize)
double[] get_MinMaxValue_SELECTED_COLUMN(String columnType, ArrayList<Object> obj)

Example:

import com.gokhanyildiz9535.JavaDataConverter;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        JavaDataConverter jdc = new JavaDataConverter();
        ArrayList<ArrayList<Object>> datasetList = jdc.readXLSX_to_ARRAYLIST("D:\\dataset.xlsx","MySheet");

        for (ArrayList<Object> row : datasetList) {
            for (Object cell : row) {
                System.out.println(cell.toString());
            }
        }
    }

}
```

## Future Works

- Maven and Gradle plugins will be created as soon as possible.
- Commonly used database functions will be added to the project environment using the PostgreSQL, MySQL and SQLite libraries.
- CSV to ARFF, ARFF to CSV, CSV to MATLAB, MATLAB to CSV converter functions will be added.
- Weka library will be added to the project environment and the tables taken from the database will be enabled to work with weka functions.
- Rest API functions will be added through the Java Retrofit library.

## Development Sources for Future Works

- The Apache Software Foundation - [HSSF and XSSF Limitations](http://poi.apache.org/components/spreadsheet/limitations.html), [XLS Serializer](http://cocoon.apache.org/2.1/userdocs/xls-serializer.html)

## Thanks

- The Apache Software Foundation - [POI API Documentation](http://poi.apache.org/apidocs/5.0/), [Apache Commons](https://commons.apache.org/)
- Google - [Gson User Guide](https://sites.google.com/site/gson/gson-user-guide)
- Source Forge - [OpenCSV](http://opencsv.sourceforge.net/)
- pjfanning - [Excel Streaming Reader](https://github.com/pjfanning/excel-streaming-reader)
- Dua, D. and Graff, C. (2019) - [UCI Machine Learning Repository](http://archive.ics.uci.edu/ml)
- Carmine DiMascio - [dotenv-java](https://github.com/cdimascio/dotenv-java)
- FasterXML - [jackson-docs](https://github.com/FasterXML/jackson-docs)

## Authors

- **Gokhan Yildiz** - [Send Mail](mailto:gokhanyildiz9535@gmail.com)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
