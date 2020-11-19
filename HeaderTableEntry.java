import java.util.ArrayList;

public class HeaderTableEntry implements Comparable<HeaderTableEntry>{

    private int item;
    private int support;

    ArrayList<TreeNode> treenodes;
    
    // Also need to add list of attached tree nodes

    public HeaderTableEntry(int item, int support) {
        this.item = item;
        this.support = support;
        this.treenodes = new ArrayList<>();
    }

    public int item() {
        return this.item;
    }

    public int support() {
        return this.support;
    }

    public int compareTo(HeaderTableEntry that) {
        if (this.support == that.support())
            return 0;
        else if (this.support > that.support())
            return -1;
        else
            return 1;
    }

    @Override
    public String toString() {
        return (this.item + "," + this.support);
    }
}