package problem;

import java.util.ArrayList;

public class Cage {
    private int size;
    private int number;
    private ArrayList<Cage> neighbors;

//    public Cage(int size, int number, int[][] neighborhoods) {
//        this.size = size;
//        this.number = number;
//        this.setNeighbors(neighborhoods);
//    }

    public Cage(int size, int number) {
        this.size = size;
        this.number = number;
    }

    //Gets an n*2 array that shows which cages are neighbors and returns neighbors of this cage
    public void setNeighbors(CSP csp, int[][] neighborhoods) {
        ArrayList<Cage> neighbors = new ArrayList<Cage>();
        int tempCageNumber;

        for (int i = 0; i < neighborhoods.length; i++) {
            for (int j = 0; j < 2; j++) {
                if (neighborhoods[i][j] == this.number) {
                    switch (j) {
                        case 0:
                            tempCageNumber = neighborhoods[i][1];
                            neighbors.add(csp.searchCage(tempCageNumber));
                            break;
                        case 1:
                            tempCageNumber = neighborhoods[i][0];
                            neighbors.add(csp.searchCage(tempCageNumber));
                    }
                }
            }
        }

        this.neighbors = neighbors;
    }

    public ArrayList<Cage> getNeighbors() { return this.neighbors; }

    public int getNumber() { return this.number; }
    public int getSize() { return this.size; }
}
