package com.gokhanyildiz9535;

public class JarGenerator {

    public final String help = "\n\n" +
            "Run java -jar \"Java-Data-Converter.jar\" \"--help\"\n" +
            "\n" +
            "C01: Convert XLSX to CSV\n" +
            "\n" +
            "Arguments: Source, Destination, SheetName\n" +
            "Example:\n" +
            "java -jar \"java-data-converter.jar\" \"C01\" \"D:\\dataset.xlsx\" \"D:\\dataset.csv\" \"MySheet\"\n" +
            "\n" +
            "C02: Convert CSV to XLSX\n" +
            "\n" +
            "Arguments: Source, Destination, SheetName\n" +
            "Example:\n" +
            "java -jar \"java-data-converter.jar\" \"C02\" \"D:\\dataset.csv\" \"D:\\dataset.xlsx\" \"MySheet\"\n" +
            "\n" +
            "C03: Convert XLSX to JSON\n" +
            "\n" +
            "Arguments: Source, Destination, SheetName, JsonObjectName, JsonObjectArray\n" +
            "Option 1:\n" +
            "java -jar \"java-data-converter.jar\" \"C03\" \"D:\\dataset.xlsx\" \"D:\\dataset_json_array.json\" \"MySheet\" \"MyDataset\" \"true\"\n" +
            "Option 2:\n" +
            "java -jar \"java-data-converter.jar\" \"C03\" \"D:\\dataset.xlsx\" \"D:\\dataset_json_object.json\" \"MySheet\" \"MyDataset\" \"false\"\n" +
            "\n" +
            "C04: Convert JSON to XLSX\n" +
            "\n" +
            "Arguments: Source, Destination, SheetName, JsonObjectName, JsonObjectArray\n" +
            "Option 1:\n" +
            "java -jar \"java-data-converter.jar\" \"C04\" \"D:\\dataset_json_array.json\" \"D:\\dataset.xlsx\" \"MySheet\" \"MyDataset\" \"true\"\n" +
            "Option 2:\n" +
            "java -jar \"java-data-converter.jar\" \"C04\" \"D:\\dataset_json_object.json\" \"D:\\dataset.xlsx\" \"MySheet\" \"MyDataset\" \"false\"\n" +
            "\n" +
            "C05: Convert CSV to JSON\n" +
            "\n" +
            "Arguments: Source, Destination, JsonObjectName, JsonObjectArray\n" +
            "Option 1:\n" +
            "java -jar \"java-data-converter.jar\" \"C05\" \"D:\\dataset.csv\" \"D:\\dataset.json\" \"null\" \"MyDataset\" \"true\"\n" +
            "Option 2:\n" +
            "java -jar \"java-data-converter.jar\" \"C05\" \"D:\\dataset.csv\" \"D:\\dataset.json\" \"null\" \"MyDataset\" \"false\"\n" +
            "\n" +
            "C06: Convert XLS to XLSX\n" +
            "\n" +
            "Arguments: Source, Destination, SheetName\n" +
            "Example:\n" +
            "java -jar \"java-data-converter.jar\" \"C06\" \"D:\\dataset.xls\" \"D:\\dataset.xlsx\" \"MySheet\"\n" +
            "\n" +
            "C07: Get XLSX File Information\n" +
            "\n" +
            "Arguments: Source, Destination, SheetName, JsonObjectName, JsonObjectArray\n" +
            "Option 1:\n" +
            "java -jar \"java-data-converter.jar\" \"C07\" \"D:\\dataset.xlsx\" \"D:\\dataset_info_ja.json\" \"MySheet\" \"MyDataset\" \"true\"\n" +
            "Option 2:\n" +
            "java -jar \"java-data-converter.jar\" \"C07\" \"D:\\dataset.xlsx\" \"D:\\dataset_info_jo.json\" \"MySheet\" \"MyDataset\" \"false\"\n" +
            "\n" +
            "Important:\n" +
            "1. Most of the functions run unbuffered, because of converting java objects array. However, some read/write functions are defined as buffered.\n" +
            "2. For yourself, directly overriding the functions for use in your project can allow you to use buffered read/write process.\n" +
            "3. If you use jar file with args, please insert full path of the files.\n" +
            "4. If you want to convert larger than or around 2MB file then set the -Xms256M -Xmx4096M\n" +
            "5. or larger than 100MB, smaller or around file then set the -Xms4096M -Xmx32768M parameter.\n" +
            "6. -Xmx(YOUR_MEMORY*1024)M parameter defines that is java virtual machine maximum heap space.\n" +
            "\n" +
            "Example: java -Xms256M -Xmx4096M -jar \"java-data-converter.jar\" \"C01\" \"D:\\dataset.xlsx\" \"D:\\dataset.csv\" \"MySheet\" \"MyDataset\" \"true\"\n" +
            "\n";

    public JarGenerator(String args[]) {
        JavaDataConverter jdc = new JavaDataConverter();
        String method = "", source = null, destination = null, sheetName = null, jsonObjectName = null, jsonObjectArray = null;
        try {
            method = args[0]; // Select Method
            source = args[1]; // Path
            destination = args[2]; // Path
            sheetName = args[3]; // Path
            jsonObjectName = args[4]; // String
            jsonObjectArray = args[5]; // true or false
        } catch (Exception e) {
        }

        if (method.equals("--help")) {
            System.out.println(help);
        } else if (method.equals("C01") && source != null && destination != null) {
            jdc.convert_XLSX_to_CSV(source, destination, sheetName);
        } else if (method.equals("C02") && source != null && destination != null
                && sheetName != null) {
            jdc.convert_CSV_to_XLSX(source, destination, sheetName);
        } else if (method.equals("C03") && source != null && destination != null
                && jsonObjectName != null && jsonObjectArray != null && sheetName != null) {
            try {
                jdc.convert_XLSX_to_JSON(source, destination, jsonObjectName, Boolean.parseBoolean(jsonObjectArray), sheetName);
            } catch (Exception e) {
                System.out.println("Invalid arguments. Please read the --help instructions.");
            }
        } else if (method.equals("C04") && source != null && destination != null
                && jsonObjectName != null && jsonObjectArray != null && sheetName != null) {
            try {
                jdc.convert_JSON_to_XLSX(source, destination, sheetName, jsonObjectName, Boolean.parseBoolean(jsonObjectArray));
            } catch (Exception e) {
                System.out.println("Invalid arguments. Please read the --help instructions.");
            }
        } else if (method.equals("C05") && source != null && destination != null
                && jsonObjectName != null && jsonObjectArray != null) {
            try {
                jdc.convert_CSV_to_JSON(source, destination, jsonObjectName, Boolean.parseBoolean(jsonObjectArray));
            } catch (Exception e) {
                System.out.println("Invalid arguments. Please read the --help instructions.");
            }
        } else if (method.equals("C06") && source != null && destination != null && sheetName != null) {
            jdc.convert_XLS_to_XLSX(source, destination, sheetName);
        } else if (method.equals("C07") && source != null && destination != null
                && jsonObjectName != null && jsonObjectArray != null && sheetName != null) {
            try {
                jdc.convert_HASHMAPLIST_to_JSON(jdc.readXLSX_getColumnAndFileInfo(source, sheetName), destination, jsonObjectName, Boolean.parseBoolean(jsonObjectArray));
            } catch (Exception e) {
                System.out.println("Invalid arguments. Please read the --help instructions.");
            }
        } else if (method.equals("Test01")) {
            /* ------------------------------------------------- */

            System.out.println("Test stuffs here...");

            /* ------------------------------------------------- */
        } else {
            System.out.println("Invalid arguments. Please read the --help instructions.");
        }
    }
}
