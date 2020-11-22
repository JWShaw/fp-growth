import java.util.HashMap;
import java.util.Collection;


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

    public void addChild(int item) {
        TreeNode childNode = new TreeNode(item, this);
        children.put(item, childNode);
    }

    public int item() {
        return item;
    }

    public TreeNode getChild(int item) {
        return children.get(item);
    }

    public TreeNode getOnlyChild() {
        if (children.size() == 1) {
            return children.values().iterator().next();
        }
        return null;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void incrementSupport() {
        support++;
    }

    public boolean hasChild(int item) {
        return children.get(item) != null;
    }

    public boolean hasChild() {
        return children.size() != 0;
    }

    public boolean hasParent() {
        return parent != null;
    }
    
    public void setParent(TreeNode node) {
        parent = node;
        node.children.put(this.item, this);
    }

    public boolean hasGrandparent() {
        return parent.getParent() != null;
    }

    public String toString() {
        return "[" + item + "," + support + "]";
    }

    public Collection<TreeNode> children() {
        return children.values();
    }
}