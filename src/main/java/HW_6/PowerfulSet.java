import java.util.HashSet;
import java.util.Set;

public class PowerfulSet {

    public <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }

    public <T> Set<T> union(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.addAll(set2);
        return result;
    }

    public <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.removeAll(set2);
        return result;
    }

    public static void main(String[] args) {
        System.out.println("\n=== Задание 3: PowerfulSet ===");

        PowerfulSet powerfulSet = new PowerfulSet();

        Set<Integer> set1 = new HashSet<>();
        set1.add(1);
        set1.add(2);
        set1.add(3);

        Set<Integer> set2 = new HashSet<>();
        set2.add(0);
        set2.add(1);
        set2.add(2);
        set2.add(4);

        System.out.println("set1: " + set1);
        System.out.println("set2: " + set2);

        Set<Integer> intersection = powerfulSet.intersection(set1, set2);
        System.out.println("Пересечение (intersection): " + intersection);

        Set<Integer> union = powerfulSet.union(set1, set2);
        System.out.println("Объединение (union): " + union);

        Set<Integer> complement = powerfulSet.relativeComplement(set1, set2);
        System.out.println("Относительное дополнение (relativeComplement): " + complement);

        System.out.println("\nДополнительные тесты со строками:");

        Set<String> stringSet1 = new HashSet<>();
        stringSet1.add("apple");
        stringSet1.add("banana");
        stringSet1.add("orange");

        Set<String> stringSet2 = new HashSet<>();
        stringSet2.add("banana");
        stringSet2.add("grape");
        stringSet2.add("kiwi");

        System.out.println("stringSet1: " + stringSet1);
        System.out.println("stringSet2: " + stringSet2);

        Set<String> stringIntersection = powerfulSet.intersection(stringSet1, stringSet2);
        System.out.println("Пересечение строк: " + stringIntersection);

        Set<String> stringUnion = powerfulSet.union(stringSet1, stringSet2);
        System.out.println("Объединение строк: " + stringUnion);

        Set<String> stringComplement = powerfulSet.relativeComplement(stringSet1, stringSet2);
        System.out.println("Дополнение строк: " + stringComplement);
    }
}
