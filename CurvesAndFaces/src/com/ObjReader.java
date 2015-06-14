package com;

import struct.Point;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class ObjReader {

    ArrayList<Point> points = new ArrayList<>();

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
                    if (values.hasNextFloat()) {
                        float x = values.nextFloat();
                        float y = values.nextFloat();
                        float z = values.nextFloat();
                        points.add(new Point(x, y, z));
                        //log("Value is : " + quote(x, y, z));
                    }

                    break;
            }

        } else {
            //log("Empty or invalid line. Unable to process.");
        }
    }

    private final Path fFilePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    private void log(Object aObject) {
        System.out.println(this.getClass() + " " + String.valueOf(aObject));
    }

    private String quote(float x, float y, float z) {
        return "x: " + x + "  y: " + y + "  z: " + z;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }
}
