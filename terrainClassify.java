/**
 * Terrain classification programme
 * @since 8 August 2020
 * @author Calvin Nyoni
 */

 import java.io.*;
 import java.util.*;

public class terrainClassify {
    public static float[][] getData(String fileName) throws FileNotFoundException {

        //Open file
        File file = new File(fileName);

        Scanner scanner = new Scanner(file);

        //Instatiate matrix by given size
        String firstLine = scanner.nextLine();

        String[] firstLineArray = firstLine.split(" ");

        int size = Integer.parseInt(firstLineArray[0]);

        float[][] matrix = new float[size][size];

        //Populate the matrix
        int i = 0;

        while (scanner.hasNextLine()) {
            
            String currentLine = scanner.nextLine();

            //System.out.println(currentLine);

            String[] ithRow = currentLine.split(" ");
            
            int j = 0;

            for (String value : ithRow) {
                float jthValue = Float.parseFloat(value);
                matrix[i][j] = jthValue;
                j = j + 1;
            }

            i = i + 1;
        }

        scanner.close();

        return matrix;

    }

    public static boolean checkBasin(int i, int j, float[][] matrix) {

        int[][] neigbourIndices = { {i-1, j-1}, {i-1, j}, {i-1, j+1}, //row below indices
                                    {i,j-1}, {i,j+1}, //same row indices
                                    {i+1, j-1}, {i+1, j}, {i+1, j+1} }; //row above indices

        for (int[] indices : neigbourIndices) {
            int iNeighbour = indices[0];
            int jNeighbour = indices[1];

            if ((matrix[iNeighbour][jNeighbour] - matrix[i][j]) <= - 0.01) {
                return false;
            }
        }

        return true;
    }
    
    public static void classify(float[][] matrix) {
        
        int size = matrix.length;

        int i = 1;
        int j = 1;

        ArrayList<String> basins = new ArrayList<String>();

        //loop over each row 
        while (i < size - 1) {
            System.out.println("Checking row " + i);
            //loop over each column
            while (j < size - 1) {
                System.out.println("Checking position (" + i + "," + j + ")");
                if (checkBasin(i, j, matrix) == true) {
                    basins.add(i + " " + j);
                }
                j = j + 1;
            }
            j = 1;
            i = i + 1;
        }

        System.out.println(basins.size());

        for (String basin: basins) {
            System.out.println(basin);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter file name: ");
        String fileName = scanner.nextLine();

        float[][] matrix = getData(fileName);

        // for (float[] ithRow : matrix) {
        //     for (float jthValue : ithRow) {
        //         System.out.print(jthValue);
        //     }
        //     System.out.println();
        // }

        scanner.close();

        classify(matrix);
    }
}