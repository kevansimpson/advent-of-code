package org.base.advent.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

@AllArgsConstructor(staticName = "createNode", access = AccessLevel.PROTECTED)
@Data
@Slf4j
public class Node<T> {
    public interface NodeHas<T> extends Function<Node<T>, Boolean> {}

    public static class EarlyExit<T> extends MutablePair<NodeHas<T>, BooleanSupplier> {
        public EarlyExit(NodeHas<T> left, BooleanSupplier right) {
            super(left, right);
        }
    }

    private final Node<T> parent;
    @Getter(lazy=true) private final List<Node<T>> children = childList();
    private final T data;
    private final long depth;
    @Getter(lazy=true) private final Map<String, Object> context = newContext();

    public Node<T> addChild(final T newData) {
        final Node<T> child = createNode(this, newData, getDepth() + 1L);
        getChildren().add(child);
        return child;
    }

    public boolean contains(final T data) {
        if (Objects.equals(getData(), data)) return true;
        else if (isChildNode()) return getParent().contains(data);
        return false;
    }

    @SafeVarargs
    public final boolean contains(final NodeHas<T> target,
                                  final EarlyExit<T>... earlyExits) {
        Node<T> node = this;
        while (node != null) {
            if (earlyExits != null)
                for (final EarlyExit<T> abort : earlyExits)
                    if (abort.getLeft().apply(node)) return abort.getRight().getAsBoolean();
            // otherwise, evaluate node
            if (target.apply(node)) return true;
            node = node.getParent();
        }

        return false;
    }

    public void detach(final String reason) {
        log.debug("Detaching {} :: {}", this, reason);
        while (!getChildren().isEmpty()) getChildren().get(0).detach(reason);
        getParent().getChildren().remove(this);
    }

    @Override
    public boolean equals(Object foo) {
        return super.equals(foo);
    }

    @Override
    public String toString() {
        return String.format("Node[%s @ %d w/ %d kids]", getData(), getDepth(), getChildren().size());
    }

    /*                          Template Methods                             */

    protected List<Node<T>> childList() { // allow override, if desired
        return new ArrayList<>();
    }

    protected boolean isChildNode() {
        return getParent() != null;
    }

    protected Map<String, Object> newContext() { // allow override, if desired
        return new TreeMap<>();
    }

    /*                      Static Factory Methods                           */

    public static <N> Node<N> createRootNode(final N root) {
        return createNode(null, root, 1L);
    }

    public static <N> Node<N> createMaxNode() {
        return createNode(null, null, Long.MAX_VALUE);
    }
}
