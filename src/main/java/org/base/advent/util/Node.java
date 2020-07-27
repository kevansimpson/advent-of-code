package org.base.advent.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Data
public class Node<T, C extends NodeContext<T>> {
    private Node<T, C> parent;
    private List<Node<T, C>> children = new ArrayList<>();
    private long depth = 1;
    private C context;

    public Node<T, C> add(final C context) {
        final Node<T, C> child = new Node<>();
        getChildren().add(child.setParent(this).setDepth(1L + getDepth()).setContext(context));
        return child;
    }

    public Node<T, C> detach() {
        while (!getChildren().isEmpty()) getChildren().get(0).detach();
        getParent().getChildren().remove(this);
        return getParent();
    }

    @Override
    public String toString() {
        return String.format("Node[%s @ %d w/ %d kids]",
                getContext().getClass().getName(), getDepth(), getChildren().size());
    }
}
