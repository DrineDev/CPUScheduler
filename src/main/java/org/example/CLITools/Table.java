package org.example.CLITools;

public class Table {
    private StringBuilder table;
    private String[] columns;
    private int[] columnWidths;
    private int rowCounter;

    public Table(String[] columns) {
        this.columns = columns;
        this.columnWidths = new int[columns.length + 1];
        rowCounter = 0;

        columnWidths[0] = 3; // Number for labels
        for(int i = 0; i < columns.length; i++)
            columnWidths[i + 1] = columns[i].length();

        this.table = new StringBuilder();
        //
        table.append(String.format("%-" + columnWidths[0] + "s", "#"));
        // append row to the stringbuilder
        for (int i = 0; i < columns.length; i++) {
            table.append(String.format("%-" + (columnWidths[i + 1] + 2) + "s", columns[i]));
        }
        table.append("\n");
    }

    public void addRow(String[] row) {
        if(row.length != columns.length)
            throw new IllegalArgumentException("Row length must match the number of columns.");

        // calculate proper length size
        for(int i = 0; i < row.length; i++)
            if (row[i].length() > columnWidths[i + 1])
                columnWidths[i + 1] = row[i].length();

        // append row to the table with proper format
        rowCounter++;
        table.append(String.format("%-" + columnWidths[0] + "s", rowCounter + ")")); // numbered column header

        for(int i = 0; i < row.length; i++)
            table.append(String.format("%-" + (columnWidths[i + 1] + 2) + "s", row[i]));

        table.append("\n");
    }

    public void drawTable() {
        System.out.println(table.toString());
    }

    public String getString() {
        return table.toString();
    }
}
