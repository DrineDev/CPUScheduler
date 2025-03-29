package org.example.CLITools;

import java.util.HashMap;
import java.util.Map;

public class AsciiGenerator {
    private static final Map<Character, String[]> FONT = new HashMap<>();
    private StringBuilder result;

    static {
        // Define ASCII art for each uppercase letter
        FONT.put('A', new String[] {
                "  ___  ",
                " / _ \\ ",
                "/ /_\\ \\",
                "|  _  |",
                "| | | |",
                "\\_| |_/"
        });
        FONT.put('B', new String[] {
                "______ ",
                "| ___ \\",
                "| |_/ /",
                "| ___ \\",
                "| |_/ /",
                "\\____/ "
        });
        FONT.put('C', new String[] {
                " _____ ",
                "/  __ \\",
                "| /  \\/",
                "| |    ",
                "| \\__/\\",
                " \\____/"
        });
        FONT.put('D', new String[] {
                "______  ",
                "|  _  \\ ",
                "| | | | ",
                "| | | | ",
                "| |/ /  ",
                "|___/   "
        });
        FONT.put('E', new String[] {
                " _____ ",
                "|  ___|",
                "| |__  ",
                "|  __| ",
                "| |___ ",
                "\\____/ "
        });
        FONT.put('F', new String[] {
                "______ ",
                "|  ___|",
                "| |_   ",
                "|  _|  ",
                "| |    ",
                "\\_|    "
        });
        FONT.put('G', new String[] {
                " _____ ",
                "/  __ \\",
                "| |  \\/",
                "| | __ ",
                "| |_\\ \\",
                "\\____/ "
        });
        FONT.put('H', new String[] {
                " _   _ ",
                "| | | |",
                "| |_| |",
                "|  _  |",
                "| | | |",
                "\\_| |_/"
        });
        FONT.put('I', new String[] {
                " _____ ",
                "|_   _|",
                "  | |  ",
                "  | |  ",
                " _| |_ ",
                " \\___/ "
        });
        FONT.put('J', new String[] {
                "   ___ ",
                "  |_  |",
                "    | |",
                "    | |",
                "/\\__/ /",
                "\\____/ "
        });
        FONT.put('K', new String[] {
                " _   __",
                "| | / /",
                "| |/ / ",
                "|    \\ ",
                "| |\\  \\",
                "\\_| \\_/"
        });
        FONT.put('L', new String[] {
                " _     ",
                "| |    ",
                "| |    ",
                "| |    ",
                "| |____",
                "\\_____/"
        });
        FONT.put('M', new String[] {
                "___  ___",
                "|  \\/  |",
                "| .  . |",
                "| |\\/| |",
                "| |  | |",
                "\\_|  |_/"
        });
        FONT.put('N', new String[] {
                " _   _ ",
                "| \\ | |",
                "|  \\| |",
                "| . ` |",
                "| |\\  |",
                "\\_| \\_/"
        });
        FONT.put('O', new String[] {
                " _____ ",
                "|  _  |",
                "| | | |",
                "| | | |",
                "\\ \\_/ /",
                " \\___/ "
        });
        FONT.put('P', new String[] {
                "______ ",
                "| ___ \\",
                "| |_/ /",
                "|  __/ ",
                "| |    ",
                "\\_|    "
        });
        FONT.put('Q', new String[] {
                " _____ ",
                "|  _  |",
                "| | | |",
                "| | | |",
                "\\ \\_/ /",
                " \\__\\_\\"
        });
        FONT.put('R', new String[] {
                "______ ",
                "| ___ \\",
                "| |_/ /",
                "|    / ",
                "| |\\ \\ ",
                "\\_| \\_|"
        });
        FONT.put('S', new String[] {
                " _____ ",
                "/  ___|",
                "\\ `--. ",
                " `--. \\",
                "/\\__/ /",
                "\\____/ "
        });
        FONT.put('T', new String[] {
                " _____ ",
                "|_   _|",
                "  | |  ",
                "  | |  ",
                "  | |  ",
                "  \\_/  "
        });
        FONT.put('U', new String[] {
                " _   _ ",
                "| | | |",
                "| | | |",
                "| | | |",
                "| |_| |",
                "\\_____/"
        });
        FONT.put('V', new String[] {
                " _   _ ",
                "| | | |",
                "| | | |",
                "| | | |",
                "\\ \\_/ /",
                " \\___/ "
        });
        FONT.put('W', new String[] {
                " _    _ ",
                "| |  | |",
                "| |  | |",
                "| |/\\| |",
                "\\  /\\  /",
                " \\/  \\/ "
        });
        FONT.put('X', new String[] {
                "__   __",
                "\\ \\ / /",
                " \\ V / ",
                " /   \\ ",
                "/ /^\\ \\",
                "\\/   \\/"
        });
        FONT.put('Y', new String[] {
                "__   __",
                "\\ \\ / /",
                " \\ V / ",
                "  \\ /  ",
                "  | |  ",
                "  \\_/  "
        });
        FONT.put('Z', new String[] {
                " ______",
                "|___  /",
                "   / / ",
                "  / /  ",
                "./ /___",
                "\\_____/"
        });
        // Add more letters as needed
    }


    // Method to generate ASCII art for a given string
    AsciiGenerator(String input) {
        input = input.toUpperCase(); // Convert to uppercase
        result = new StringBuilder();
        int height = 6; // Height of the ASCII art (rows)

        for (int i = 0; i < height; i++) {
            for (char c : input.toCharArray()) {
                if (FONT.containsKey(c)) {
                    result.append(FONT.get(c)[i]).append(" ");
                } else {
                    result.append("       "); // Space for unsupported characters
                }
            }
            result.append("\n");
        }
    }

    public String getAsciiArt() {
        return result.toString();
    }
}


