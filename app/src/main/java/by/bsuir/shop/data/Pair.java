package by.bsuir.shop.data;

import java.util.Objects;

public class Pair {
    String name;
    String parentId;

    public Pair(String name, String parentId) {
        this.name = name;
        this.parentId = parentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(name, pair.name) && Objects.equals(parentId, pair.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parentId);
    }

    public String getName() {
        return name;
    }

    public String getParentId() {
        return parentId;
    }
}
