import java.util.HashMap;

class TreeNode {

    HashMap<Integer, TreeNode> children;
    int item;
    int occurrences;

    TreeNode(int item) {
        children = new HashMap<>();
        this.item = item;
        occurrences = 1;
    }

    TreeNode addChild(int item) {
        TreeNode childNode = new TreeNode(item);
        children.put(item, childNode);
        return childNode;
    }

    TreeNode incrementChild(int item) {
        TreeNode child = children.get(item);
        child.occurrences++;
        return child;
    }

    boolean hasChild(int item) {
        return children.get(item) != null;
    }

    boolean isLeaf() {
        return children.isEmpty();
    }

    public String toString() {
        return "[" + item + "," + occurrences + "]";
    }
}