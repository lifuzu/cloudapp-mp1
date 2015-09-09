import java.util.*;
import java.util.Map;

class MapComparator implements Comparator {
    Map base;

    public MapComparator(Map base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    @Override
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}