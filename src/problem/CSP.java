package problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class CSP {
    //Cage as variable in csp
//    private ArrayList<Cage> cages;
    Cage[] cages;

    //Animals as domain in csp
    //Values of this ArrayList are the size of animals
//    private ArrayList<Integer> animals;
    int[] animals;

    //A queue of neighborhood of cages
//    private Queue<int[]> arcs;
    private Queue<Cage[]> arcs;

    private int[][] binaryConstraint;

    private HashMap<Cage, ArrayList<Integer>> domains;

    public CSP(Cage[] cages, int[] animals, int[][] neighborhoodConstraint) {
        this.cages = cages;
        this.animals = animals;
        this.binaryConstraint = neighborhoodConstraint;
    }

//    private void setArcs(ArrayList<Cage> cages) {
//        Queue<int[]> arcs = new LinkedList<int[]>();
//
//        for (Cage cage : cages) {
//            for (Integer neighbor : cage.getNeighbors()) {
//                if (!arcs.contains(new int[]{neighbor.intValue(), cage.getNumber()}))
//                    arcs.add(new int[]{cage.getNumber(), neighbor.intValue()});
//            }
//        }
//        this.arcs = arcs;
//    }

    public void setArcs(Cage[] cages) {
        Queue<Cage[]> arcs = new LinkedList<Cage[]>();

        for (Cage cage : cages) {
            for (Cage neighbor : cage.getNeighbors()) {
                if (!arcs.contains(new Cage[]{neighbor, cage}))
                    arcs.add(new Cage[]{cage, neighbor});
            }
        }
        this.arcs = arcs;
    }

    public HashMap<Cage, ArrayList<Integer>> nodeConsistency() {
        HashMap<Cage, ArrayList<Integer>> domains = new HashMap<Cage, ArrayList<Integer>>();
        ArrayList<Integer> tempDomain = new ArrayList<Integer>();

        for (Cage cage : cages) {
            for (int i = 0; i < animals.length; i++) {
                if (cage.getSize() >= animals[i])
                    tempDomain.add(i);
            }
            domains.put(cage, tempDomain);
            tempDomain.removeAll(tempDomain);
        }

        return domains;
    }

    public boolean ac3() {
        Cage[] currentArc = new Cage[2];
        ArrayList<Cage> tempNeighbors = new ArrayList<Cage>();

        while (!arcs.isEmpty()) {
            currentArc = this.arcs.poll();
            if (this.revise(currentArc[0], currentArc[1])) {
                if (this.domains.get(currentArc[0]).isEmpty())
                    return false;

                tempNeighbors.addAll(currentArc[0].getNeighbors());
                tempNeighbors.remove(currentArc[1]);
                for (Cage cage : tempNeighbors) {
                    arcs.add(new Cage[]{cage, currentArc[0]});
                }
                tempNeighbors.removeAll(tempNeighbors);
            }
        }
        return true;
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

    //When using this first check if it's null !!!
    public Cage searchCage(int cageNumber) {
        Cage target = null;

        for (Cage cage : this.cages) {
            if (cage.getNumber() == cageNumber) {
                target = new Cage(cage.getSize(), cage.getNumber());
                break;
            }
        }

        return target;
    }

    public int[][] getBinaryConstraint() { return this.binaryConstraint; }

    public Cage[] getCages() { return cages; }

    public void setDomains() { this.domains = this.nodeConsistency(); }
    public HashMap<Cage, ArrayList<Integer>> getDomains() { return this.domains; }
}
