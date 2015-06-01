/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Niclas
 */
public class CurvesAndFaces {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        File f = new File("src/input/UB1_1.obj");
        ObjReader p = new ObjReader(f.getAbsolutePath());
        p.processLineByLine();
        
        List<Point> points = p.getPoints();
        
        for(int i = 0; i < points.size(); i++){
        
            System.out.println(points.get(i));
        }
    }
    
}
