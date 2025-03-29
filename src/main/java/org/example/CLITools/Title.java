package org.example.CLITools;

public class Title {
    private String title;

    // Constructor
    public Title(String title) {
        AsciiGenerator ascii = new AsciiGenerator(title);
        this.title = ascii.getAsciiArt(); // Use the ASCII art string
    }

    // Method to draw the title
    public void drawTitle() {
        System.out.println(this.title);
    }

    // Method to get the title as a string
    public String getString() {
        return this.title;
    }
}
