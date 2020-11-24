import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FPGrowth {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws FileNotFoundException {
        
        File f = new File(args[0]);
        double minsup = Double.parseDouble(args[1]);

        PrintWriter outfile = new PrintWriter("MiningResult.txt");

        try {
            FPTree globalTree = new FPTree(f, minsup);
            globalTree.generateProjectedTrees();
            System.out.println("|FPs| = " + globalTree.numFrequentPatterns());
            System.out.print(globalTree.miningResults());
        } catch (IOException ioe) {
            System.out.print(ioe);
            System.exit(0);
        } finally {
            outfile.close();
        }
    }
}