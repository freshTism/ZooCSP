package main;

import problem.CSP;
import problem.Cage;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int cageNeighborhoodCount;
        int animalCount;
        int cageCount;

        int[] cagesSizes;
        int[] animalsSizes;
        int[][] neighborhoods;
        int[][] neighborhoodConstraint;

        Scanner scanner = new Scanner(System.in);
        String input;
        String[] splitedInput;

        Cage[] cages;
        CSP problem;

        //Process first line
        input = scanner.nextLine();
        splitedInput = input.split(" ");
        cageCount = Integer.valueOf(splitedInput[0]);
        animalCount = Integer.valueOf(splitedInput[1]);
        cageNeighborhoodCount = Integer.valueOf(splitedInput[2]);

        //Process 2nd line
        input = scanner.nextLine();
        splitedInput = input.split(" ");
        cagesSizes = new int[splitedInput.length];
        for (int i = 0; i < splitedInput.length; i++) {
            cagesSizes[i] = Integer.valueOf(splitedInput[i]);
        }

        //Process 3nd line
        input = scanner.nextLine();
        splitedInput = input.split(" ");
        animalsSizes = new int[splitedInput.length];
        for (int i = 0; i < splitedInput.length; i++) {
            animalsSizes[i] = Integer.valueOf(splitedInput[i]);
        }

        //Process next part: neighbor cages
        neighborhoods = new int[cageNeighborhoodCount][2];
        for (int i = 0; i < cageNeighborhoodCount; i++) {
            input = scanner.nextLine();
            splitedInput = input.split(" ");
            neighborhoods[i][0] = Integer.valueOf(splitedInput[0]);
            neighborhoods[i][1] = Integer.valueOf(splitedInput[1]);
        }

        //Process next part: neighborhood constraints
        neighborhoodConstraint = new int[cageCount][cageCount];
        for (int i =0; i < cageCount; i++) {
            input = scanner.nextLine();
            splitedInput = input.split(" ");

            for (int j = 0; j < cageCount; j++) {
                neighborhoodConstraint[i][j] = Integer.valueOf(splitedInput[j]);
            }
        }

        //Creating cages and set CSV parameters
        cages = new Cage[cageCount];
        for (int i = 0; i < cageCount; i++) {
            cages[i] = new Cage(cagesSizes[i], i);
        }
        problem = new CSP(cages, animalsSizes, neighborhoodConstraint);
        for (Cage cage : cages) {
            cage.setNeighbors(problem, neighborhoods);
        }
        problem.setArcs(cages);
        problem.setDomains();   //Set domains and also checks node consistency

        //Checking arc consistency
        if (problem.getDomains() == null) {
            System.out.println("This problem has no solution.");
        } else {
            System.out.println("Arc consistency: " + problem.ac3());
        }

    }
}
