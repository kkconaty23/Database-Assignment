package org.example.javafxdb_sql_shellcode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles CSV import and export operations for Person records
 */
public class CsvHandler {

    private static final String CSV_DELIMITER = ",";
    private static final String CSV_HEADER = "ID,First Name,Last Name,Department,Major";

    /**
     * Imports persons from a CSV file
     * @param file CSV file to import
     * @return List of Person objects
     * @throws IOException If file cannot be read
     */
    public static List<Person> importPersonsFromCsv(File file) throws IOException {
        List<Person> persons = new ArrayList<>();
        int lineNumber = 0;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNumber++;

                // Skip header line
                if (lineNumber == 1 && line.startsWith("ID,")) {
                    continue;
                }

                try {
                    String[] fields = line.split(CSV_DELIMITER);
                    if (fields.length < 5) {
                        throw new IllegalArgumentException("Invalid CSV format at line " + lineNumber);
                    }

                    int id = Integer.parseInt(fields[0].trim());
                    String firstName = fields[1].trim();
                    String lastName = fields[2].trim();
                    String dept = fields[3].trim();
                    String major = fields[4].trim();

                    persons.add(new Person(id, firstName, lastName, dept, major));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid ID format at line " + lineNumber);
                }
            }
        }

        return persons;
    }

    /**
     * Exports persons to a CSV file
     * @param persons List of Person objects to export
     * @param file Destination file
     * @throws IOException If file cannot be written
     */
    public static void exportPersonsToCsv(List<Person> persons, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write header
            writer.write(CSV_HEADER);
            writer.newLine();

            // Write data rows
            for (Person person : persons) {
                StringBuilder sb = new StringBuilder();
                sb.append(person.getId()).append(CSV_DELIMITER)
                        .append(escapeCsvField(person.getFirstName())).append(CSV_DELIMITER)
                        .append(escapeCsvField(person.getLastName())).append(CSV_DELIMITER)
                        .append(escapeCsvField(person.getDept())).append(CSV_DELIMITER)
                        .append(escapeCsvField(person.getMajor()));

                writer.write(sb.toString());
                writer.newLine();
            }
        }
    }

    /**
     * Escapes CSV field value to handle commas and quotes
     * @param field The field value to escape
     * @return Escaped field value
     */
    private static String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }

        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }

        return field;
    }
}
