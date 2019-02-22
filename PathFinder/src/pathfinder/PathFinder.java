/**
 * Holly Beeson
 * Lab 2: Path Finder
 * This program reads in an adjacency matrix and finds all possible paths between all possible nodes, without looping within the same node.
 * It also writes the matrix to an output file, as well as each path. It finds the paths using recursion. The input and
 * output files are passed in as command line arguments. 
 */
package pathfinder;

import java.io.*;
import java.util.*;

public class PathFinder {

    public static void main(String[] args) {
        //check if input and output files are provided as command line arguments
        if (args.length == 2) {
            //get files from arguments
            String inputFile = args[0];
            String outputFile = args[1];

            try {
                displayMatrix(inputFile, outputFile);
                System.out.println("Output written to file: " + outputFile);
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + inputFile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Please put input and output files as command line arguments.");
        }
    }

    /* This function writes all of the possible paths between all possible pairs of nodes
    to a text file that is passed in.*/
    public static void writeFile(String outputFile, int[][] matrix, String path, boolean append) throws IOException {
        //print writer to write to the file
        FileWriter writer = new FileWriter(new File(outputFile), append);
        //write matrix
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                writer.write(matrix[i][j] + " ");
                System.out.print(matrix[i][j] + " ");
            }

            writer.append(System.lineSeparator());
            System.out.println();
        }
        //write to all paths
        writer.write(path + System.lineSeparator());
        System.out.println(path);
        //add separator between matrices
        writer.write("************************" + System.lineSeparator());
        writer.close();
    }

    //This function finds every path from the current node to all other nodes using recursion
    public static void findPath(int[][] matrix, int[] parent, boolean[] visited, int begin, int end, int node1, int node2, boolean endReached, StringBuffer paths) {
        //check if node1 and node2 are in range
        if ((node1 >= matrix.length) || (node2 >= matrix.length)) {
            return;
        }
        //if end is reached
        if (endReached) {
            paths.append("\t" + (begin + 1));
            getPath(parent, begin, parent[end], paths);
            paths.append("->" + (end + 1) + System.lineSeparator());
            return;
        }
        //check if there is a path from node1 to node2 and if node2 is not visited
        if ((matrix[node1][node2] != 0) && (!visited[node2])) {
            //set node2 as visited
            visited[node2] = true;
            parent[node2] = node1;
            //check if end will be reached in the next iteration
            if (node2 == end) {
                endReached = true;
            } else {
                endReached = false;
            }
            //find path from beginning node
            findPath(matrix, parent, visited, begin, end, node2, 0, endReached, paths);
            //set node2 unvisited
            visited[node2] = false;
            parent[node2] = -1;
            endReached = false;
        }
        //find path from beginning node
        findPath(matrix, parent, visited, begin, end, node1, node2 + 1, endReached, paths);
    }

    // This function displays all of the paths from all of the nodes. 
    public static String displayAllPaths(int[][] matrix) {
        int length = matrix.length;
        StringBuffer allPaths = new StringBuffer();
        //Start
        for (int i = 0; i < length; i++) {
            //get path for all possible nodes
            for (int j = 0; j < length; j++) {
                //create array for visited vertex
                boolean[] visited = new boolean[length];
                //create array to hold path
                int[] parent = new int[length];
                //set parent beginning node as -1
                Arrays.fill(parent, -1);
                //Add beginning node as visited if begin and end are different
                if (i != j) {
                    visited[i] = true;
                }
                //append beginning nodes and end nodes
                StringBuffer paths = new StringBuffer();
                if (i != j) {
                    allPaths.append(System.lineSeparator() + "Path from " + (i + 1) + " to " + (j + 1) + System.lineSeparator());
                    //get path from i to j
                    findPath(matrix, parent, visited, i, j, i, 0, false, paths);
                    //check if there is a path from i to j to allPaths
                    if (paths.toString().equals("")) {
                        paths.append("\tNo Path Found" + System.lineSeparator());
                    }
                    //append paths from i to j to allPaths
                    allPaths.append(paths);
                } else {
                    paths.append("\tNo Path Found" + System.lineSeparator());

                }
            }
        }
        //return all paths
        return allPaths.toString();
    }

    // This function returns a path between the beginning node to node i as a StringBuffer. 
    public static void getPath(int[] parent, int begin, int i, StringBuffer allPaths) {
        if (i == begin) {
            return;
        } else {
            getPath(parent, begin, parent[i], allPaths);
            allPaths.append("->" + (i + 1));
        }
    }

    /* This function reads in the adjacency matrix from the input file and displays all of the paths between all
    possible pairs of nodes */
    public static void displayMatrix(String inputFile, String outputFile) throws IOException {
        //Scanner to read from file
        Scanner file = new Scanner(new File(inputFile));
        boolean isFirstMatrix = true;

        //read file
        int[][] matrix = new int[0][0];
        while (file.hasNextLine()) {
            //read size of the matrix
            int n = file.nextInt();
            //create matrix
            matrix = new int[n][n];
            //read matrix
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = file.nextInt();
                }
            }
            //Display paths
            String path = displayAllPaths(matrix);
            //write output to file
            if (isFirstMatrix) {
                writeFile(outputFile, matrix, path, false);
                isFirstMatrix = false;
            } else {
                writeFile(outputFile, matrix, path, true);
            }
        }
        //close scanner
        file.close();
    }
}
