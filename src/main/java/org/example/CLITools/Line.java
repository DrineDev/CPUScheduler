package org.example.CLITools;

public class Line {
    private StringBuilder lineString;

    public Line(int lineLength) {
        lineString = new StringBuilder();

        for(int i = 0; i < lineLength; i++) {
            lineString.append("=");
        }
        lineString.append("\n");
    }

    public void drawLine() {
        System.out.println(lineString);
    }

    public String getString() {
        return lineString.toString();
    }
}
