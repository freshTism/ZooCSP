package problem;

import main.Utility;

import java.util.*;

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
    private HashMap<Cage, Integer> assignment;
    //private Set<Integer> varDomains = new HashSet<Integer>();

    public CSP(Cage[] cages, int[] animals, int[][] neighborhoodConstraint) {
        this.cages = cages;
        this.animals = animals;
        this.binaryConstraint = neighborhoodConstraint;
    }

    public void setArcs(Cage[] cages) {
        Queue<Cage[]> arcs = new LinkedList<Cage[]>();
        boolean arcContains = false;

        for (Cage cage : cages) {
            for (Cage neighbor : cage.getNeighbors()) {
                if (arcs.size() == 0)
                    arcs.add(new Cage[]{cage, neighbor});
                else {
                    for (Cage[] arc : arcs) {
                        //If arcs contains {neighbor, cage}
                        if (arc[0].equals(neighbor) && arc[1].equals(cage)) {
                            arcContains = true;
                            break;
                        }
                    }

                    if (!arcContains)
                        arcs.add(new Cage[]{cage, neighbor});
                }

                arcContains = false;
            }
        }
        this.arcs = arcs;
    }

    public HashMap<Cage, ArrayList<Integer>> nodeConsistency() {
        HashMap<Cage, ArrayList<Integer>> domains = new HashMap<Cage, ArrayList<Integer>>();
        ArrayList<Integer> tempDomain = new ArrayList<Integer>();

        for (Cage cage : cages) {
            //Checks size of the animals and cages
            for (int i = 0; i < animals.length; i++) {
                if (cage.getSize() >= animals[i])
                    tempDomain.add(i);
            }

            if (tempDomain.isEmpty())
                return null;
            else
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
                target = (Cage) Utility.deepCopy(cage);
//                target = new Cage(cage.getSize(), cage.getNumber());
//                target.setNeighbors(this, neighborhoods);
                break;
            }
        }

        return target;
    }

    public HashMap<Cage, Integer> backtrackingSearch(HashMap<Cage, Integer> assignment, HashMap<Cage, ArrayList<Integer>> varDomains) {
        if (this.isAssignmentComplete(assignment))
            return assignment;

        Cage currentCage = mrvHeuristic(assignment, varDomains);
        Integer[] domainOfCurrentCage = new Integer[this.getDomains().get(currentCage).size()];
        domainOfCurrentCage = this.getDomains().get(currentCage).toArray(domainOfCurrentCage);

        HashMap<Cage, ArrayList<Integer>> forwardCheckedVarDomains;
        HashMap<Cage, Integer> result;

        for (Integer animal : domainOfCurrentCage) {
            assignment.put(currentCage, animal);
            forwardCheckedVarDomains = forwardChecking(varDomains, currentCage, animal);
            for (HashMap.Entry<Cage, ArrayList<Integer>> entry : forwardCheckedVarDomains.entrySet()) {
                if (entry.getValue().isEmpty())
                    return null;
            }
            result = backtrackingSearch(assignment, forwardCheckedVarDomains);
            if (result != null)
                return result;
            assignment.remove(currentCage);
        }

        return null;
    }

    private boolean isAssignmentComplete(HashMap<Cage, Integer> assignment) {
        if (assignment.size() == cages.length)
            return true;
        else
            return false;
    }

    //Better to check for being null or not before using
    //Minimum-Remaining values heuristic
    private Cage mrvHeuristic(HashMap<Cage, Integer> assignment, HashMap<Cage, ArrayList<Integer>> varDomains) {
        Cage selectedCage = null;
        int minRemainingValues = Integer.MAX_VALUE;

        for (HashMap.Entry<Cage, ArrayList<Integer>> entry : varDomains.entrySet()) {
            if (!assignment.containsKey(entry.getKey())) {
                if (entry.getValue().size() < minRemainingValues)
                    selectedCage = entry.getKey();
            }
        }

        return selectedCage;
    }

//    private int lcvHeuristic(HashMap<Cage, Integer> assignment, HashMap<Cage, ArrayList<Integer>> varDomains) {
//
//    }

    private HashMap<Cage, ArrayList<Integer>> forwardChecking(HashMap<Cage, ArrayList<Integer>> varDomains, Cage cage,
                                                              int animal) {
        HashMap<Cage, ArrayList<Integer>> resultVarDomains = (HashMap<Cage, ArrayList<Integer>>) Utility.deepCopy(varDomains);

        for (Cage neighbor : cage.getNeighbors()) {
            for (Integer neighborAnimal : resultVarDomains.get(neighbor)) {
                if (this.binaryConstraint[animal][neighborAnimal] == 0) {
                    resultVarDomains.get(neighbor).remove(resultVarDomains.get(neighbor).indexOf(neighborAnimal));
                }
            }
        }

        return resultVarDomains;
    }

//    private HashMap<Cage, ArrayList<Integer>> deepCopy(HashMap<Cage, ArrayList<Integer>>) {
//        HashMap<Cage, >
//    }
//public static HashMap<Integer, List<MySpecialClass>> copy(
//        HashMap<Integer, List<MySpecialClass>> original)
//{
//    HashMap<Integer, List<MySpecialClass>> copy = new HashMap<Integer, List<MySpecialClass>>();
//    for (Map.Entry<Integer, List<MySpecialClass>> entry : original.entrySet())
//    {
//        copy.put(entry.getKey(),
//                // Or whatever List implementation you'd like here.
//                new ArrayList<MySpecialClass>(entry.getValue()));
//    }
//    return copy;
//}

    public int[][] getBinaryConstraint() { return this.binaryConstraint; }

    public Cage[] getCages() { return cages; }

    public void setDomains() { this.domains = this.nodeConsistency(); }
    public HashMap<Cage, ArrayList<Integer>> getDomains() { return this.domains; }
}
