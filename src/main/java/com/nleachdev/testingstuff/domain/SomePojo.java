package com.nleachdev.testingstuff.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class SomePojo {
    final int id;
    final String name;

    public SomePojo(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SomePojo somePojo = (SomePojo) o;
        return id == somePojo.id &&
                Objects.equals(name, somePojo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SomePojo.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .toString();
    }
}
