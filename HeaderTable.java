import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

// The header table for the FP-trees.  Most methods are self-documenting.
public class HeaderTable implements Iterable<Integer> {

    private HashMap<Integer, HeaderTableEntry> data;

    public HeaderTable() {
        data = new HashMap<>();
    }

    public void add(int item) {
        data.put(item, new HeaderTableEntry(item, 1));
    }

    // Adds an item to the table, or increments its support appropriately
    // if the item entry already exists in the table.
    public void add(int item, int supp) {
        if (data.get(item) == null) {
            data.put(item, new HeaderTableEntry(item, supp));
        } else {
            data.get(item).support += supp;
        }
    }

    public void remove(int item) {
        data.remove(item);
    }

    public int getSupport(int item) {
        if (!this.contains(item)) {
            return 0;
        }
        return data.get(item).support;
    }

    // Adds one to the support of an item
    public void incrementSupport(int item) {
        data.get(item).support++;
    }

    // Adds a hyperlink to a given TreeNode to the
    // appropriate table entry
    public void addReference(TreeNode node) {
        data.get(node.item()).treenodes.add(node);
    }

    // Returns a collection of all nodes in a tree corresponding
    // to a particular item.
    public Collection<TreeNode> getNodes(int item) {
        return data.get(item).treenodes;
    }

    public boolean contains(int item) {
        return data.get(item) != null;
    }

    public int size() {
        return data.size();
    }

    public Iterator<Integer> iterator() {
        List<HeaderTableEntry> itemList = new ArrayList(data.values());
        Collections.sort(itemList);

        Iterator<Integer> result = new Iterator<>() {
            Iterator<HeaderTableEntry> inner = itemList.iterator();

            public boolean hasNext() {
                return inner.hasNext();
            }

            public Integer next() {
                return inner.next().item;
            }
        };

        return result;
    }

    @Override
    public String toString() {
        String result = "";
        for (int item : this) {
            result += item + "," + getSupport(item) + "\n";
        }
        return result;
    }

    // Internal table entry class
    class HeaderTableEntry implements Comparable<HeaderTableEntry> {

        int item;
        int support;

        ArrayList<TreeNode> treenodes;

        HeaderTableEntry(int item, int support) {
            this.item = item;
            this.support = support;
            this.treenodes = new ArrayList<>();
        }

        public int compareTo(HeaderTableEntry that) {
            if (this.support == that.support)
                return 0;
            else if (this.support > that.support)
                return -1;
            else
                return 1;
        }

        @Override
        public String toString() {
            return (this.item + "," + this.support);
        }
    }
}