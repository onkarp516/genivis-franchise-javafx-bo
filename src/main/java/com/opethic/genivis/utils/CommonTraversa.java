package com.opethic.genivis.utils;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class CommonTraversa {
    public static Node getNextFocusableNode(Scene scene) {
        List<Node> nodes = new ArrayList<>();
        addDescendants(scene.getRoot(), nodes);
        boolean found = false;
        for (Node node : nodes) {
            if (found && node.isFocusTraversable() && node.isVisible() && node.isManaged()) {
                return node;
            }
            if (node.equals(scene.getFocusOwner())) {
                found = true;
            }
        }
        return null;
    }

    public static Node getPreviousFocusableNode(Scene scene) {
        List<Node> nodes = new ArrayList<>();
        addDescendants(scene.getRoot(), nodes);
        Node previousNode = null;
        for (Node node : nodes) {
            if (node.equals(scene.getFocusOwner())) {
                break; // Stop searching once the current node is found
            }
            if (node.isFocusTraversable() && node.isVisible() && node.isManaged()) {
                previousNode = node;
            }
        }
        return previousNode;
    }

    public static void addDescendants(Node node, List<Node> nodes) {
        if (node != null) {
            if (node instanceof Parent) {
                ((Parent) node).getChildrenUnmodifiable().forEach(child -> addDescendants(child, nodes));
            }
            nodes.add(node);
        }
    }
}

