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
    private ArrayList<HeaderTableEntry> headerTable = new ArrayList<>();

    public FPTree() {
        this.headerTable = new ArrayList<>();
        root = new TreeNode(-1);
    }

    public void generateGlobalTree(File f, double minSupPercent) throws FileNotFoundException {

        Scanner sc1 = new Scanner(f);
        int dbLines = sc1.nextInt();
        this.minSupNum = dbLines * (minSupPercent / 100);

        HashMap<Integer, Integer> supportTable = new HashMap<>();

        while (sc1.hasNextInt()) {
            sc1.nextInt();
            int numItems = sc1.nextInt();

            for (int i = 0; i < numItems; i++) {

                Integer item = new Integer(sc1.nextInt());

                if (supportTable.get(item) == null) {
                    supportTable.put(item, 1);
                } else {
                    int occurrences = supportTable.get(item);
                    supportTable.replace(item, ++occurrences);
                }
            }
        }
        sc1.close();

        for (Integer i : supportTable.keySet()) {
            if (supportTable.get(i) >= minSupNum) {
                HeaderTableEntry hte = new HeaderTableEntry(i, supportTable.get(i));
                headerTable.add(hte);
            }
        }
        Collections.sort(headerTable);

        Scanner sc2 = new Scanner(f);
        sc2.nextInt();

        while (sc2.hasNextInt()) {
            sc2.nextInt();
            int numItems = sc2.nextInt();

            HashSet<Integer> transactionSet = new HashSet<>();
            for (int i = 0; i < numItems; i++) {
                transactionSet.add(sc2.nextInt());
            }

            TreeNode n = this.root;
            for (HeaderTableEntry ent : headerTable) {
                if (transactionSet.contains(ent.item())) {
                    n = this.add(ent, n);
                }
            }     
        }
        sc2.close();
    }

    // Adds a node to the tree.  Return the newly-added node.
    private TreeNode add(HeaderTableEntry entry, TreeNode node) {
        if (node.hasChild(entry.item())) {
            node = node.incrementChild(entry.item());
        } else {
            node = node.addChild(entry.item());
            entry.treenodes.add(node);
        }
        return node;
    }

    public TreeNode getRoot() {
        return root;
    }

    public String toString() {
        String result = "";
        for (HeaderTableEntry ent : headerTable) {
            result += ent.toString() + ent.treenodes.toString() + "\n";
        }
        return result;
    }

    public void preorder(TreeNode node) {
        System.out.print(node);
        for (TreeNode child : node.children.values()) {
            preorder(child);
        }
    }
}