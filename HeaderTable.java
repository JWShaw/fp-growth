import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class HeaderTable implements Iterable<Integer> {

    private HashMap<Integer, HeaderTableEntry> data;

    public HeaderTable() {
        data = new HashMap<>();
    }

    public void add(int item) {
        data.put(item, new HeaderTableEntry(item, 1));
    }

    public void remove(int item) {
        data.remove(item);
    }

    public int getSupport(int item) {
        return data.get(item).support;
    }

    public void incrementSupport(int item) {
        data.get(item).support++;
    }

    public void addReference(TreeNode node) {
        data.get(node.item()).treenodes.add(node);
    }

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

    public Iterator<Integer> backwardsIterator() {
        List<HeaderTableEntry> itemList = new ArrayList(data.values());
        Collections.sort(itemList, new Comparator<HeaderTableEntry>() {
            public int compare(HeaderTableEntry a, HeaderTableEntry b) 
            { 
                return a.support - b.support;
            } 
        });

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