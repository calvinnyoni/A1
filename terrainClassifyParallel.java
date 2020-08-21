import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class terrainClassifyParallel {
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


    static class Classify extends RecursiveTask<String> {
        static int numOfBasins = 0;
        int low;
        int high;
        float[] matrix;

        public Classify(int low, int high, float[] matrix) {
            this.low = low;
            this.high = high;
            this.matrix = matrix;
        }

        public String compute() {
            if (high - low <= 512) {
                int i = this.low;

                int n = (int) Math.sqrt(matrix.length);

                String basins = "";

                int basinRow = 0;

                int basinColumn = 0;

                while (i < this.high) {
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

                return basins;
            }

            else {
                int mid = low + (high - low) / 2;

                Classify left  = new Classify(low, mid, matrix);
                Classify right = new Classify(mid, high, matrix);

                left.fork();
                String rightResult = right.compute();
                String leftResult  = left.join();

                return leftResult + rightResult;
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        Scanner scanner = new Scanner(System.in);
        
        String fileName = scanner.nextLine();

        float[] matrix = getData(fileName);

        int numOfThreads = Runtime.getRuntime().availableProcessors();


        ForkJoinPool forkJoinPool = new ForkJoinPool(numOfThreads);
        Classify parallelClassify = new Classify(0, matrix.length, matrix); 
        String result = forkJoinPool.invoke(parallelClassify);
        System.out.println(parallelClassify.numOfBasins);
        System.out.println(result);
    }
}