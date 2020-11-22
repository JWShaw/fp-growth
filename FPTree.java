import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class FPTree {

    private double minSupNum;   
    private TreeNode root;
    private HeaderTable headerTable;

    // Creates the Global FP-Tree
    public FPTree(File f, double minSupPercent) throws FileNotFoundException {

        this.headerTable = new HeaderTable();
        this.root = new TreeNode(-1, null);

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
                    leaf = this.addLeaf(item, leaf);
                }
            }     
        }
        sc2.close();
    }

    private FPTree(double minSupNum) {
        this.headerTable = new HeaderTable();
        this.root = new TreeNode(-1, null);
        this.minSupNum = minSupNum;
    }

    public void doCoolStuff() {

        Iterator<Integer> items = headerTable.backwardsIterator();
        while (items.hasNext()) {
            int item = items.next();
            FPTree newTree = new FPTree(this.minSupNum);

            for (TreeNode n : headerTable.getNodes(item)) {
                TreeNode head = n;
                TreeNode newBranchRoot = new TreeNode(item, null);
                while (head.hasGrandparent()) {
                    TreeNode branchParent = new TreeNode(head.getParent().item(), null);
                    newBranchRoot.setParent(branchParent);
                    newBranchRoot = branchParent;
                    head = head.getParent();
                }
                newTree.addBranch(newBranchRoot, root);
            }
            System.out.println(newTree);
        }
    }


    private void addBranch(TreeNode branchRoot, TreeNode parent) {
        if (parent.hasChild(branchRoot.item())) {
            TreeNode childNode = parent.getChild(branchRoot.item());
            childNode.incrementSupport();
            headerTable.incrementSupport(branchRoot.item());
            if (branchRoot.hasChild()) {
                addBranch(branchRoot.getOnlyChild(), childNode);
            }
        } else {
            parent.addChild(branchRoot.item());
            TreeNode newborn = parent.getChild(branchRoot.item());
            headerTable.add(branchRoot.item());
            headerTable.addReference(newborn);
            if (branchRoot.hasChild()) {
                addBranch(branchRoot.getOnlyChild(), newborn);
            }
        }
    }

    /* Adds a new leaf to the tree.  Return the newly-added leaf.
     * Used when building the global FP-tree */
    private TreeNode addLeaf(int item, TreeNode parent) {
        if (parent.hasChild(item)) {
            TreeNode childNode = parent.getChild(item);
            childNode.incrementSupport();
            return childNode;
        } else {
            parent.addChild(item);
            TreeNode newborn = parent.getChild(item);
            headerTable.addReference(newborn);
            return newborn;
        }
    }

    public String toString() {
        String result = "";
        for (Integer item : headerTable) {
            result += item.toString() + ',' + headerTable.getSupport(item) + headerTable.getNodes(item).toString() + "\n";
        }
        return result;
    }
}