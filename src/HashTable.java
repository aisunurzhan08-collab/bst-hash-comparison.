import java.util.LinkedList;
import java.util.List;

public class HashTable {
    private static class Entry {
        int key;
        Entry(int key) { this.key = key; }
    }

    private final List<LinkedList<Entry>> buckets;
    private int size = 0;

    public HashTable(int capacity) {
        int safeCapacity = Math.max(1, capacity);
        buckets = new java.util.ArrayList<>(safeCapacity);
        for (int i = 0; i < safeCapacity; i++) buckets.add(new LinkedList<>());
    }

    private int index(int key) {
        return Math.floorMod(Integer.hashCode(key), buckets.size());
    }

    public void insert(int key) {
        LinkedList<Entry> bucket = buckets.get(index(key));
        for (Entry entry : bucket) {
            if (entry.key == key) return; // Ignore duplicates.
        }
        bucket.add(new Entry(key));
        size++;
    }

    public boolean search(int key) {
        for (Entry entry : buckets.get(index(key))) {
            if (entry.key == key) return true;
        }
        return false;
    }

    public void delete(int key) {
        LinkedList<Entry> bucket = buckets.get(index(key));
        java.util.Iterator<Entry> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().key == key) {
                iterator.remove();
                size--;
                return;
            }
        }
    }

    public int size() { return size; }
}
