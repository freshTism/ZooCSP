package problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class CSP {
    //Cage as variable in csp
    private ArrayList<Cage> cages;

    //Animals as domain in csp
    //Values of this ArrayList are the size of animals
    private ArrayList<Integer> animals;

    //A queue of neighborhood of cages
    private Queue<int[]> arcs;

    private int[][] binaryConstraint;

    private HashMap<Cage, ArrayList<Integer>> domains;

    public CSP(ArrayList<Cage> cages, ArrayList<Integer> animals, int[][] neighborhoodConstraint) {
        this.cages = cages;
        this.animals = animals;
        this.setArcs(cages);
        this.domains = this.nodeConsistency();
    }

    private void setArcs(ArrayList<Cage> cages) {
        Queue<int[]> arcs = new LinkedList<int[]>();

        for (Cage cage : cages) {
            for (Integer neighbor : cage.getNeighbors()) {
                if (!arcs.contains(new int[]{neighbor.intValue(), cage.getNumber()}))
                    arcs.add(new int[]{cage.getNumber(), neighbor.intValue()});
            }
        }
        this.arcs = arcs;
    }

    public HashMap<Cage, ArrayList<Integer>> nodeConsistency() {
        HashMap<Cage, ArrayList<Integer>> domains = new HashMap<Cage, ArrayList<Integer>>();
        ArrayList<Integer> tempDomain = new ArrayList<Integer>();

        for (Cage cage : cages) {
            for (int i = 0; i < animals.size(); i++) {
                if (cage.getSize() >= animals.get(i))
                    tempDomain.add(i);
            }
            domains.put(cage, tempDomain);
            tempDomain.removeAll(tempDomain);
        }

        return domains;
    }

    public boolean ac3() {
        while (!arcs.isEmpty()) {

        }
    }

    private boolean revise(Cage cage1, Cage cage2) {
        boolean revised = false;
        boolean satisfy = false;

        for (int animal1 : domains.get(cage1)) {
            //Checks if there is at least one animal in cage2's domain that satisfy the constraint between cage1 & cage2
            for (int animal2 : domains.get(cage2)) {
                if (this.binaryConstraint[animal1][animal2] == 1)
                    satisfy = true;
            }

            if (!satisfy) {
                domains.get(cage1).remove(animal1);
                revised = true;
             }
        }

        return revised;
    }

    private int[][] getBinaryConstraint() { return this.binaryConstraint; }
}
