package com.gokhanyildiz9535;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TestCase {
    /* ------------------------------------------------- */
    /* Statics */
    public static final ArrayList<Object> __COLUMN__NAMES__ =
            new ArrayList<>(Arrays.asList("COL#1", "COL#2", "COL#3", "COL#4",
                    "COL#5", "COL#6", "COL#7", "COL#8", "COL#9"));
    public static final int __ROW__SIZE__ = 15;
    public static final int __NULL__SIZE__ = 3;
    public static final int __COLUMN__SIZE__ = __COLUMN__NAMES__.size();

    public static final String __SHEET__NAME__ = "MySheet";
    public static final boolean __JSON__OBJECT__ARRAY__ = true;

    public static final int __TOTAL__CHARACTERS__ = 1;
    public static final int __NUMBER__COUNT__ = 2;
    public static final int __UPPERCASE__COUNT__ = 2;

    /* ------------------------------------------------- */
    /* Testing Files */

    public static final String P = JavaDataConverter.__FILES__PATH__ + "Project Files\\";
    public static final String[] F = new String[]{
            P + "test.xlsx", // TestCase.F[0]
            P + "test.csv", // TestCase.F[1]
            P + "test_t.json", // TestCase.F[2]
            P + "test_f.json", // TestCase.F[3]
            P + "test_new.json", // TestCase.F[4]
            P + "test_new.xlsx", // TestCase.F[5]
            P + "test_new.csv", // TestCase.F[6]
            P + "test.xls", // TestCase.F[7]
            P + "large__data.csv", // TestCase.F[8]
            P + "large__data.json", // TestCase.F[9]
            P + "Data7602DescendingYearOrder.csv", // TestCase.F[10]
            P + "Data7602DescendingYearOrder.json", // TestCase.F[11]
    };
    public static final String[] FOW = new String[]{
            P + "owid-covid-data", // 0
            P + "covid-testing-all-observations", // 1
            P + "vaccinations", // 2
            P + "excess_mortality", // 3
            P + "large__data", // 4
            P + "test__data", // 5
    };

    /* ------------------------------------------------- */
    /* Constructor */

    public TestCase() {
    }

    /* ------------------------------------------------- */
    /* Some Stuffs for testing */

    public ArrayList<ArrayList<Object>> generateGenericArrList(ArrayList<Object> columnNames, int rowSize, int columnSize, int nullSize) {
        ArrayList<ArrayList<Object>> gal = new ArrayList<>();
        gal.add(columnNames);
        int iterationCount = 0;
        // generate "nullSize" random integers (or a single one) in the range [0, rowSize*ColumnSize-1]
        ArrayList<Integer> rc = new ArrayList<>();
        for (int i : new Random().ints(nullSize, 0, rowSize * columnSize).toArray())
            rc.add(i);
        for (int i = 0; i < rowSize; i++) {
            ArrayList<Object> rowArrayList = new ArrayList<>();
            for (int j = 0; j < columnSize; j++) {
                if (j < columnSize - 2) {
                    if (rc.contains(iterationCount)) {
                        rowArrayList.add(new String());
                    } else {
                        rowArrayList.add(generateRandomString(__TOTAL__CHARACTERS__, __NUMBER__COUNT__, __UPPERCASE__COUNT__));
                    }
                }
                if (j == columnSize - 2) {
                    if (i == 2 || i == 4 || i == 5)
                        rowArrayList.add("");
                    else
                        rowArrayList.add(1 + (int) (Math.random() * 1000));
                }
                if (j == columnSize - 1) {
                    Random r = new Random();
                    if (i == 1 || i == 2 || i == 7)
                        rowArrayList.add("");
                    else
                        rowArrayList.add(1 + (100 - 1) * r.nextDouble());
                }
                iterationCount++;
            }
            gal.add(rowArrayList);
        }
//        for (int i = 0; i < 2; i++) {
//            ArrayList<Object> rowArrayList = new ArrayList<>();
//            // generate int
//            if (i == 0) {
//                for (int ff = 0; ff < rowSize; ff++) {
//                    if (ff == 2 || ff == 4 || ff == 5)
//                        rowArrayList.add("");
//                    else
//                        rowArrayList.add(1 + (int) (Math.random() * 100));
//                }
//
//            }
//            // generate double
//            else {
//                Random r = new Random();
//                for (int ff = 0; ff < rowSize; ff++)
//                    if (ff == 1 || ff == 2 || ff == 7)
//                        rowArrayList.add("");
//                    else
//                        rowArrayList.add(1 + (100 - 1) * r.nextDouble());
//            }
//            gal.add(rowArrayList);
//        }
        return gal;
    }

    public String generateRandomString(int totalCharacters, int numberCount, int upperCaseCount) {
        String upperCaseLetters = RandomStringUtils.random(upperCaseCount, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(numberCount);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(totalCharacters);
//        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
        String combinedChars = upperCaseLetters
                .concat(numbers)
//                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        String password = pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return password;
    }

    public void printGenericArrList(ArrayList<ArrayList<Object>> gal) {
        for (ArrayList<Object> objectArrList : gal) {
            System.out.println(objectArrList);
            for (Object object : objectArrList) {
//                System.out.println(object);
//                if (object.equals(Statics.EMPTYSTR)) {
//                    System.out.println(true);
//                }
            }
        }
    }

    public void printHashMapTypeArrList(HashMap<String, Object> map) {
        for (String i : map.keySet()) {
            System.out.println(i + " " + map.get(i));
        }
    }

    /* ------------------------------------------------- */

}
