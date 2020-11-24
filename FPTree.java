import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;


/* Represents an FP-tree.
 * USAGE:
 *   1. Use the public constructor to generate the global FP-tree from a database file
 *   2. Call generateProjectedTrees() to recursively generate each projected tree
 *   3. Use miningResults() and numFrequentPatterns() to obtain results.
 */
public class FPTree {

    private double minSupNum;   
    private TreeNode root;
    private HeaderTable headerTable;
    private ArrayList<Integer> prefix;

    private ArrayList<FPTree> projectedTrees = new ArrayList<>();

    // Creates the Global FP-Tree
    public FPTree(File f, double minSupPercent) throws FileNotFoundException {

        this.headerTable = new HeaderTable();
        this.root = new TreeNode(-1, null);
        this.prefix = new ArrayList<>();

        // First database scan: populates support table
        Scanner sc1 = new Scanner(f);
        int dbLines = sc1.nextInt();
        this.minSupNum = dbLines * (minSupPercent / 100);

        while (sc1.hasNextInt()) {
            sc1.nextInt();
            int numItems = sc1.nextInt();

            for (int i = 0; i < numItems; i++) {

                Integer item = new Integer(sc1.nextInt());

                if (!headerTable.contains(item)) {
                    headerTable.add(item);
                } else {
                    headerTable.incrementSupport(item);
                }
            }
        }
        sc1.close();

        // Prunes infrequent items from the header table
        for (Integer item : headerTable) {
            if (headerTable.getSupport(item) < minSupNum)
                headerTable.remove(item);
        }

        // Second database scan: builds tree
        Scanner sc2 = new Scanner(f);
        sc2.nextInt();

        while (sc2.hasNextInt()) {
            sc2.nextInt();
            int numItems = sc2.nextInt();

            HashSet<Integer> transactionSet = new HashSet<>();
            for (int i = 0; i < numItems; i++) {
                transactionSet.add(sc2.nextInt());
            }

            TreeNode leaf = this.root;
            for (Integer item : headerTable) {
                if (transactionSet.contains(item)) {
                    leaf = this.addLeaf(item, 1, leaf);
                }
            }     
        }
        sc2.close();
    }

    // Generates all the projected trees for this tree
    public void generateProjectedTrees() {

        for (Integer entry : headerTable) {
            FPTree newTree = new FPTree(this.minSupNum);
            newTree.prefix = new ArrayList(this.prefix);
            newTree.prefix.add(entry);

            // An especially ugly data structure
            ArrayList<Pair<LinkedList<Integer>,Integer>> branches = new ArrayList<>();

            // Generate the linear "branches" to add to the new tree
            // (and construct its header table)
            for (TreeNode n : headerTable.getNodes(entry)) {
                TreeNode visit = n.getParent();
                LinkedList<Integer> newBranch = new LinkedList();
                int branchSupport = n.getSupport();

                while (visit.hasParent()) {
                    newBranch.addFirst(visit.item());
                    newTree.headerTable.add(visit.item(), n.getSupport());
                    visit = visit.getParent();
                }
                branches.add(new Pair(newBranch, branchSupport));
            }
            
            // Prune infrequent items from new tree's header table
            for (Integer item : newTree.headerTable) {
                if (newTree.headerTable.getSupport(item) < minSupNum)
                    newTree.headerTable.remove(item);
            }

            /* Add each linear "branch" to the tree
             * (only add the node if the minimum support threshold is satisfied
                in the header table) */
            for (Pair<LinkedList<Integer>,Integer> p : branches) {
                TreeNode addAt = newTree.root;
                for (Integer item : p.item1) {
                    if (newTree.headerTable.getSupport(item) >= minSupNum) {
                        addAt = newTree.addLeaf(item, p.item2, addAt);
                    }
                }
            }
            projectedTrees.add(newTree);
            newTree.generateProjectedTrees();
        }
    }

    /* Returns a string representation of the mining results.
     * To be called on the global FP-tree. */
    public String miningResult() {
        String result = "";
        if (headerTable.size() == 0) {
            return "";
        } else {
            for (int item : headerTable) {
                result += item;
                for (int p : prefix) {
                    result += ", " + p;
                }
                result += " : " + headerTable.getSupport(item) + "\n";
            }
        }
        for (FPTree tree : projectedTrees) {
            result += tree.miningResult();
        }
        return result;
    }

    /* Returns the total number of frequent patterns. 
     * To be called on the global FP-tree. */
    public int numFrequentPatterns() {
        int result = headerTable.size();
        for (FPTree tree : projectedTrees) {
            result += tree.numFrequentPatterns();
        }
        return result;
    }

    // Private constructor for internal use only
    private FPTree(double minSupNum) {
        this.headerTable = new HeaderTable();
        this.root = new TreeNode(-1, null);
        this.minSupNum = minSupNum;
    }

    /* Adds a new leaf to the tree, or increments the existing one with
     * the same item.  Return the newly-added leaf
     * Used when building FP-trees. */
    private TreeNode addLeaf(int item, int support, TreeNode parent) {
        if (parent.hasChild(item)) {
            TreeNode childNode = parent.getChild(item);
            childNode.incrementSupport(support);
            return childNode;
        } else {
            parent.addChild(item, support);
            TreeNode newborn = parent.getChild(item);
            headerTable.addReference(newborn);
            return newborn;
        }
    }
}