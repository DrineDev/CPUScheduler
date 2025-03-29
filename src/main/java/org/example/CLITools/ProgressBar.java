package org.example.CLITools;

public class ProgressBar {
    private int progress;
    private int total;
    private int width;
    private StringBuilder progressBar;

    public ProgressBar(int progress, int total, int width) {
        this.progress = progress;
        this.total = total;
        this.width = width;
        drawProgressBar();
    }

    public void drawProgressBar() {
        // Values for loading bar
        float percent = (float) progress / total;
        int chars = (int) (width * percent);

        // Print loading bar
        progressBar = new StringBuilder();
        progressBar.append("[");
        for (int i = 0; i < width; i++) {
            if (i < chars) {
                progressBar.append("-");
            } else if (i == chars) {
                if (i % 2 == 0) {
                    progressBar.append("C");
                } else {
                    progressBar.append("c");
                }
            } else {
                progressBar.append(" ");
            }
        }
        progressBar.append("]");

        // Show percentage
        progressBar.append(String.format(" %.1f%%", percent * 100));
    }

    public String getString() {
        return progressBar.toString();
    }
}
