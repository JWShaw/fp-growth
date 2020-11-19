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

    private double minSupNum;   // Number of occurrences of a "frequent" item
    private ArrayList<HeaderTableEntry> headerTable = new ArrayList<>();

    public FPTree() {
        this.headerTable = new ArrayList<>();
    }

    // Turns this table into the L1 table given the database file
    public void generateGlobalTree(File f, double minSupPercent) throws FileNotFoundException {

        Scanner sc1 = new Scanner(f);
        int dbLines = sc1.nextInt();
        this.minSupNum = dbLines * (minSupPercent / 100);

        HashMap<Integer, Integer> supportTable = new HashMap<>();

        // Creates and populates the L1 table
        while (sc1.hasNextInt()) {
            sc1.nextInt();
            int numItems = sc1.nextInt();

            /* For each item, hash a 1-element set containing that item with
            the number of times that item has occurred in the database */
            for (int i = 0; i < numItems; i++) {

                Integer item = new Integer(sc1.nextInt());

                if (supportTable.get(item) == null) {
                    supportTable.put(item, 1);
                } else {
                    /* Increments the count of the set if it has already been 
                    encountered */
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

            for (HeaderTableEntry ent : headerTable) {
                if (transactionSet.contains(ent.item())) {
                    this.add(ent.item());
                }
            }     
        }
        sc2.close();
    }
}