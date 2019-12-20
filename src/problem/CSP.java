package problem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class CSP {
    //Cage as variable in csp
    private ArrayList<Cage> variable = new ArrayList<Cage>();

    //Animals as domain in csp
    //Values of this ArrayList are the size of animals
    private ArrayList<Integer> value = new ArrayList<Integer>();

    //A queue of neighborhood of cages
    private Queue<int[]> arcs = new LinkedList<int[]>();

    private int[][] binaryConstraint;

}
