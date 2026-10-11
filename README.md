# BST vs Hash Table Performance Comparison

## Goal
This project compares insertion, searching, and deletion in a Binary Search Tree (BST) and a hash table using separate chaining (linked-list buckets).

## Requirements
- Java 17 or newer (the code also uses only standard Java libraries).
- No external dependencies.

## How to compile and run
From the project root:

```bash
javac -d out src/*.java
java -cp out Benchmark
```

The benchmark creates `results/results.csv`. It tests 100, 1,000, and 10,000 unique integer keys. For each size it performs insertion of all keys, searches for all inserted keys, and deletes all keys. It repeats the experiment five times and records the average elapsed time in nanoseconds. Values are shuffled with a fixed seed for repeatability.

## Implementations
- **BST:** a regular, unbalanced binary search tree. Duplicate keys are ignored. Deletion handles leaf nodes, nodes with one child, and nodes with two children by replacing the deleted key with its in-order successor.
- **Hash table:** uses `hash(key) mod capacity` to select a bucket and separate chaining to handle collisions. Duplicate keys are ignored. The benchmark uses twice as many buckets as keys to keep the load factor low.

## Time complexity

Let `n` be the number of stored elements.

| Operation | Structure | Best case | Average/expected case | Worst case |
|---|---|---:|---:|---:|
| Insert | BST | O(1) when the tree is empty / insertion is at a null child | O(log n) for a reasonably balanced tree | O(n) when the tree becomes a chain |
| Search | BST | O(1) when the key is at the root | O(log n) for a reasonably balanced tree | O(n) when the tree becomes a chain |
| Delete | BST | O(1) when the target is the root and has at most one child (excluding traversal) | O(log n) for a reasonably balanced tree | O(n) in a chain-shaped tree |
| Insert | Hash table | O(1) when the target bucket is empty and no resize is needed | O(1) with a good hash distribution and low load factor | O(n) if many keys collide in one bucket |
| Search | Hash table | O(1) if the key is the first item in its bucket | O(1) with a good hash distribution and low load factor | O(n) if many keys collide in one bucket |
| Delete | Hash table | O(1) if the key is first in its bucket | O(1) with a good hash distribution and low load factor | O(n) if many keys collide in one bucket |

**Important:** this BST is not self-balancing, so O(log n) is not guaranteed. The expected BST complexity depends on tree shape and insertion order. The hash table's average O(1) depends on a suitable hash distribution and controlled load factor. This implementation has fixed capacity and does not resize.

## Experimental results

Each measurement below is the average of five runs, in nanoseconds (ns). The benchmark uses randomized insertion order to reduce the chance of an accidentally sorted BST input. It checks that all inserted keys can be found and that both structures are empty after deletion.

| Elements | Operation | BST (ns) | Hash table (ns) |
|---:|---|---:|---:|
| 100 | Insert all | 73,051 | 137,123 |
| 100 | Search all | 22,383 | 79,608 |
| 100 | Delete all | 23,159 | 81,258 |
| 1,000 | Insert all | 199,209 | 172,158 |
| 1,000 | Search all | 175,162 | 124,305 |
| 1,000 | Delete all | 317,740 | 223,061 |
| 10,000 | Insert all | 1,917,867 | 2,055,050 |
| 10,000 | Search all | 863,190 | 1,036,509 |
| 10,000 | Delete all | 1,590,983 | 1,032,571 |

### Results and interpretation
In this run, the BST was faster for all three operations at 100 elements. At 1,000 elements, the hash table was faster for insertion, searching, and deletion. At 10,000 elements, the BST was faster for insertion and searching, while the hash table was faster for deletion. Therefore, these measurements do **not** show that one structure wins every operation and size. The hash table's expected O(1) complexity does not guarantee it will be faster in every real benchmark.

Runtime depends on the computer, Java version, background applications, implementation details, and JVM warm-up. These measurements are one experiment, not universal timings. The CSV file contains the same results in machine-readable form.

## Why practical performance differs
1. **Data shape:** a plain BST can become unbalanced, making operations linear.
2. **Collisions:** keys that map to the same hash bucket make hash operations slower.
3. **Memory and cache:** nodes and linked lists allocate objects and follow pointers; pointer chasing can reduce cache efficiency.
4. **Constant factors:** hashing, comparisons, allocation, and method calls have real costs not shown by Big-O.
5. **Benchmark environment:** JVM warm-up, operating-system scheduling, and background applications affect elapsed time.

## Conclusion
For this workload, a hash table is expected to provide faster average insertion, searching, and deletion when the hash distribution is good and the load factor is low. A BST maintains key order and supports ordered traversal, while this hash table does not. A self-balancing BST (such as AVL or Red-Black) guarantees O(log n) operations, unlike the unbalanced BST implemented here.

## Handwritten report reminder
Write your own one-page explanation in your own words, based on the CSV numbers from your own run. Include: the goal, the two implementations, the observed trend for all three sizes, why the results differ, and one limitation of the experiment. Do not copy this README into the handwritten report.
