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
            // Generate the global FP-tree
            FPTree globalTree = new FPTree(f, minsup);

            // Recursively build every projected FP-tree
            globalTree.generateProjectedTrees();

            // Obtain results by recursively tracing through every projected tree
            int numFrequentPatterns = globalTree.numFrequentPatterns();
            String miningResult = globalTree.miningResult();

            // Print to standard out and to output file as per assignment specification
            System.out.println("|FPs| = " + numFrequentPatterns);
            outfile.println("|FPs| = " + numFrequentPatterns);
            outfile.print(miningResult);

        } catch (IOException ioe) {
            System.out.print(ioe);
            System.exit(0);
        } finally {
            outfile.close();
        }
    }
}