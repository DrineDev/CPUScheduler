package org.example.CLITools;

public class Description {
    private StringBuilder description;

    public Description(String description) {
        this.description = new StringBuilder();
        this.description.append(description);
    }

    public void addDescription(String description) {
        this.description.append(description);
    }

    public String getString() {
        return description.toString() + "\n";
    }
}
