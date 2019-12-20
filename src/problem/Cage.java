package problem;

import java.util.ArrayList;

public class Cage {
    private int size;
    private int number;
    private ArrayList<Integer> neighbors;

    public Cage(int size, int number, int[][] neighborhoods) {
        this.size = size;
        this.number = number;
        this.neighbors = this.setNeighbors(neighborhoods);
    }

    //Gets an n*2 array that shows which cages are neighbors and returns neighbors of this cage
    private ArrayList<Integer> setNeighbors(int[][] neighborhoods) {
        ArrayList<Integer> neighbors = new ArrayList<Integer>();

        for (int i = 0; i < neighborhoods.length; i++) {
            for (int j = 0; j < 2; j++) {
                if (neighborhoods[i][j] == this.number) {
                    switch (j) {
                        case 0:
                            neighbors.add(neighborhoods[i][1]);
                            break;
                        case 1:
                            neighbors.add(neighborhoods[i][0]);
                    }
                }
            }
        }

        return neighbors;
    }
}
