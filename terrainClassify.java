//We fix this!

import java.util.*;
import java.io.*;

public class terrainClassify {

    //given an nxn matrix, and an index i this method determine if it is an edge element or not
    public static boolean isEdge(int i, int n) {
        if (i < n) {
            return true;
        }
        else if (i > n*(n-1)) {
            return true;
        }
        else if ((i % n) == 0) {
            return true;
        }
        else if (((i+1) % n) == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    //
    public static boolean isBasin(int i, float[] matrix) {
        //System.out.println("Checking index " + i + "...");

        int n = (int) Math.sqrt(matrix.length);

        int[] neighbours = { i-n-1, i-n, i-n+1, i-1, i+1, i+n-1, i+n, i+n+1};

        int k = 0;

        while (k < neighbours.length) {
            //System.out.println("Checking neighbour " + neighbours[k]);
            if (matrix[neighbours[k]] <= (matrix[i] + 0.01)) {
                return false;
            }
            k = k + 1;
        }
        
        return true;
    }
    
    
    public static float[] getData(String fileName) throws FileNotFoundException {

        //Open file
        File file = new File(fileName);

        Scanner scanner = new Scanner(file);

        //Instatiate matrix by given size
        String lineFromFile = scanner.nextLine();

        String[] data = lineFromFile.split(" ");

        int n = Integer.parseInt(data[0]);

        float[] matrix = new float[n*n];

        //Populate the matrix
        int i = 0;

        lineFromFile = scanner.nextLine();

        data = lineFromFile.split(" ");

        while (i < n*n) {
            matrix[i] = Float.parseFloat(data[i]);
            i = i + 1;
        }

        scanner.close();

        return matrix;

    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        //System.out.println("Enter file name: ");
        String fileName = scanner.nextLine();

        float[] matrix = getData(fileName);

        int n = (int) Math.sqrt(matrix.length);

        int i = 0;

        int numOfBasins = 0;

        String basins = "";

        int basinRow = 0;
        int basinColumn = 0;

        while (i < (n*n)) {

            if (isEdge(i, n)) {
                //System.out.println("Index " + i + " is an edge.");
                i = i + 1;
                continue;
            }

            else {
                if (isBasin(i, matrix)) {
                    numOfBasins = numOfBasins + 1;
                    basinRow = (int) i/n;
                    basinColumn = i % n;
                    basins = basins + basinRow + " " + basinColumn + "\n";
                }
            }

            i = i + 1;
        }

        scanner.close();

        System.out.println(numOfBasins + "\n" + basins);
    }
}