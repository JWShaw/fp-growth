import java.util.HashMap;
import java.util.Collection;

// The node class of which the FP Tree is composed.
// All functions were are self-documenting.
public class TreeNode {

    private TreeNode parent;
    private HashMap<Integer, TreeNode> children;
    private int item;
    private int support;

    public TreeNode(int item, TreeNode parent) {
        this.parent = parent;
        children = new HashMap<>();
        this.item = item;
        support = 1;
    }

    public TreeNode(int item, TreeNode parent, int support) {
        this(item, parent);
        this.support = support;
    }

    public int item() {
        return item;
    }

    public void incrementSupport(int amount) {
        support += amount;
    }

    public int getSupport() {
        return support;
    }

    // Adds a (deep copy) of a node as a child to this node
    public void addChild(int item, int support) {
        TreeNode childNode = new TreeNode(item, this, support);
        children.put(item, childNode);
    }

    public TreeNode getChild(int item) {
        return children.get(item);
    }

    public boolean hasChild(int item) {
        return children.get(item) != null;
    }

    public TreeNode getParent() {
        return parent;
    }

    public boolean hasParent() {
        return parent != null;
    }
}