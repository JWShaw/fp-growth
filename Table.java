import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/* This class behaves as both the C-table and L-table class */
public class Table {

    /* The rather complicated underlying data structure.  
       Matches each itemset to its number of occurrences. */
    private HashMap<HashSet<Integer>, Integer> supportTable;

    private boolean isCTable;
    private double minSupNum;   // Number of occurrences of a "frequent" item

    /* Generates an empty table.  Can be thought of as a C table with zero
    itemsets. */
    public Table() {
        this.isCTable = true;
        this.supportTable = new HashMap<>();
    }

    /* Used locally by the selfJoin method to build the next C-table from an 
    L-table. */
    private Table(HashMap<HashSet<Integer>, Integer> map, double minSupNum) {
        this.supportTable = map;
        this.minSupNum = minSupNum;
        this.isCTable = true;
    }

    // Turns this table into the L1 table given the database file
    public void generateL1(File f, double minSupPercent) throws FileNotFoundException {

        Scanner sc = new Scanner(f);
        int dbLines = sc.nextInt();
        this.minSupNum = dbLines * (minSupPercent / 100);

        // Creates and populates the L1 table
        while (sc.hasNextInt()) {
            sc.nextInt();
            int numItems = sc.nextInt();

            /* For each item, hash a 1-element set containing that item with
            the number of times that item has occurred in the database */
            for (int i = 0; i < numItems; i++) {
                HashSet<Integer> singleton = new HashSet<>();
                singleton.add(sc.nextInt());

                if (supportTable.get(singleton) == null) {
                    supportTable.put(singleton, 1);
                } else {
                    /* Increments the count of the set if it has already been 
                    encountered */
                    int occurrences = supportTable.get(singleton);
                    supportTable.replace(singleton, ++occurrences);
                }
            }
        }
        sc.close();
    }

    // Reads in the database file and populates the L-table for a given C-table
    public void populateLTable(File f) throws FileNotFoundException {
        this.isCTable = false;
        Scanner sc = new Scanner(f);
        sc.nextInt();

        while (sc.hasNextInt()) {
            sc.nextInt();
            int numItems = sc.nextInt();

            HashSet<Integer> transactionSet = new HashSet<>();
            for (int i = 0; i < numItems; i++) {
                transactionSet.add(sc.nextInt());
            }
            
            /* Increments the support table count only if an itemset is a
            subset of the transaction set */
            for (HashSet<Integer> itemset : supportTable.keySet()) {
                if (transactionSet.containsAll(itemset)) {
                    int occurrences = supportTable.get(itemset);
                    supportTable.replace(itemset, ++occurrences);
                }
            }

            
        }
    }

    // Returns a new C-table based on this L-table
    public Table selfJoin() {
        
        List<HashSet<Integer>> itemsetsList = 
            new ArrayList<HashSet<Integer>>(supportTable.keySet());
        HashMap<HashSet<Integer>, Integer> newTable = new HashMap<>();

        // This is the size of the itemsets that are found in the next L1 table
        int newItemSetSize = itemsetsList.get(0).size() + 1;

        for (int i = 0; i < itemsetsList.size(); i++) {
            for (int j = i + 1; j < itemsetsList.size(); j++) {
                HashSet<Integer> mergedSet = new HashSet<>();
                HashSet<Integer> set1 = itemsetsList.get(i);
                HashSet<Integer> set2 = itemsetsList.get(j);
                boolean addToNextTable = true;

                if (supportTable.get(set1) >= minSupNum &&
                    supportTable.get(set2) >= minSupNum) {
                        mergedSet.addAll(set1);
                        mergedSet.addAll(set2);
                }

                /* We only want itemsets whose size are one greater than the
                itemsets in this table */
                if (mergedSet.size() != newItemSetSize)
                    continue;

                /* Checks if the itemset has any infrequent subsets.  If so,
                the itemset is itself infrequent. */
                for (Integer item : mergedSet) {
                    HashSet<Integer> subset = new HashSet<Integer>();
                    subset.addAll(mergedSet);
                    subset.remove(item);

                    if (supportTable.get(subset) == null || 
                        supportTable.get(subset) < minSupNum) {
                        addToNextTable = false;
                        break;
                     }
                } 

                if (addToNextTable) {
                    newTable.put(mergedSet, 0);
                }
            }
        }

        return new Table(newTable, this.minSupNum);
    }

    public boolean isCTable() {
        return isCTable;
    }

    public boolean isLTable() {
        return !isCTable;
    }

    public int size() {
        int count = 0;
        for (HashSet<Integer> itemset : supportTable.keySet()) {
            if (supportTable.get(itemset) >= minSupNum) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        String result = "";
        for (HashSet<Integer> itemset : supportTable.keySet()) {
            if (supportTable.get(itemset) >= minSupNum) {
                result += itemset.toString() + " : " + 
                    supportTable.get(itemset).toString() + "\n";
            }
        }
        return result;
    }
}