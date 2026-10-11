import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

public class Benchmark {
    private static final int[] SIZES = {100, 1000, 10000};
    private static final int REPEATS = 5;

    public static void main(String[] args) throws Exception {
        new File("results").mkdirs();
        try (PrintWriter out = new PrintWriter("results/results.csv")) {
            out.println("Structure,Operation,Elements,AverageTimeNs");
            for (int n : SIZES) {
                long bstInsert = 0, hashInsert = 0;
                long bstSearch = 0, hashSearch = 0;
                long bstDelete = 0, hashDelete = 0;

                for (int r = 0; r < REPEATS; r++) {
                    int[] values = shuffledValues(n, 1000L + n * 31L + r);
                    BinarySearchTree bst = new BinarySearchTree();
                    HashTable hash = new HashTable(Math.max(1, n * 2));

                    long start = System.nanoTime();
                    for (int v : values) bst.insert(v);
                    bstInsert += System.nanoTime() - start;

                    start = System.nanoTime();
                    for (int v : values) hash.insert(v);
                    hashInsert += System.nanoTime() - start;

                    start = System.nanoTime();
                    for (int v : values) if (!bst.search(v)) throw new IllegalStateException("BST search failed");
                    bstSearch += System.nanoTime() - start;

                    start = System.nanoTime();
                    for (int v : values) if (!hash.search(v)) throw new IllegalStateException("Hash search failed");
                    hashSearch += System.nanoTime() - start;

                    // Delete all inserted values, in the same order for both structures.
                    start = System.nanoTime();
                    for (int v : values) bst.delete(v);
                    bstDelete += System.nanoTime() - start;

                    start = System.nanoTime();
                    for (int v : values) hash.delete(v);
                    hashDelete += System.nanoTime() - start;

                    if (bst.size() != 0 || hash.size() != 0) throw new IllegalStateException("Delete failed");
                }

                write(out, "BST", "insert", n, bstInsert / REPEATS);
                write(out, "HashTable", "insert", n, hashInsert / REPEATS);
                write(out, "BST", "search", n, bstSearch / REPEATS);
                write(out, "HashTable", "search", n, hashSearch / REPEATS);
                write(out, "BST", "delete", n, bstDelete / REPEATS);
                write(out, "HashTable", "delete", n, hashDelete / REPEATS);
            }
        }
        System.out.println("Benchmark finished. Results saved to results/results.csv");
    }

    private static void write(PrintWriter out, String structure, String operation, int n, long time) {
        out.println(structure + "," + operation + "," + n + "," + time);
        System.out.printf("%-10s %-6s n=%5d average=%d ns%n", structure, operation, n, time);
    }

    private static int[] shuffledValues(int n, long seed) {
        int[] values = new int[n];
        for (int i = 0; i < n; i++) values[i] = i;
        Random random = new Random(seed);
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = values[i]; values[i] = values[j]; values[j] = tmp;
        }
        return values;
    }
}
