package utils;

import datastructures.DynamicArray;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class JFXUtils {

    private JFXUtils() {} // static-only utility

    /**
     * Converts a DynamicArray<E> into an ObservableList<E>
     * using ONLY simple loops (no Java Collections).
     */
    public static <E> ObservableList<E> toObservableList(DynamicArray<E> array) {
        ObservableList<E> list = FXCollections.observableArrayList();

        for (int i = 0; i < array.size(); i++) {
            list.add(array.get(i)); // allowed: adds elements one-by-one
        }

        return list;
    }
}
