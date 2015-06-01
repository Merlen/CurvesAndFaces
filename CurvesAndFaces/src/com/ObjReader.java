package com;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ObjReader {

    List<Point> points = new ArrayList<>();

    public ObjReader(String aFileName) {
        fFilePath = Paths.get(aFileName);
    }

    public final void processLineByLine() throws IOException {
        try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
            while (scanner.hasNextLine()) {
                processLine(scanner.nextLine());
            }
        }
    }

    protected void processLine(String aLine) {
        java.util.Locale.setDefault(java.util.Locale.ENGLISH);
        Scanner scanner = new Scanner(aLine);
        scanner.useDelimiter(" ");
        if (scanner.hasNext()) {
            String name = scanner.next();
            switch (name) {
                case "v":
                    Scanner values = new Scanner(aLine.replace("v", ""));
                    values.useDelimiter("  | ");
                    if (values.hasNextDouble()) {
                        double x = values.nextDouble();
                        double y = values.nextDouble();
                        double z = values.nextDouble();
                        log("Value is : " + quote(x, y, z));
                    }

                    break;
            }

        } else {
            //log("Empty or invalid line. Unable to process.");
        }
    }

    private final Path fFilePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    private static void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }

    private String quote(double x, double y, double z) {
        points.add(new Point(x, y, z));
        return "x: " + x + "  y: " + y + "  z: " + z;
    }

    public List<Point> getPoints() {
        return points;
    }
}
