// SmartCityPlanner.java
// CIT300 - Graded Practical Assignment 1
// Smart City Route Planner
// Covers: Graphs, Trees, Queues/Stacks, Linked Structures

import java.util.*;

public class SmartCityPlanner {

    // -----------------------------
    // Member 1: Graph Structure
    // -----------------------------
    static class Graph {
        private Map<String, List<String>> adjList = new HashMap<>();

        // Add new location (vertex)
        public void addLocation(String name) {
            if (!adjList.containsKey(name)) {
                adjList.put(name, new ArrayList<>());
                System.out.println("Location added: " + name);
            } else {
                System.out.println("Location already exists.");
            }
        }

        // Remove location (vertex)
        public void removeLocation(String name) {
            if (!adjList.containsKey(name)) {
                System.out.println("Location not found.");
                return;
            }
            adjList.remove(name);
            for (List<String> neighbors : adjList.values()) {
                neighbors.remove(name);
            }
            System.out.println("Location removed: " + name);
        }

        // Add road (edge)
        public void addRoad(String src, String dest) {
            if (!adjList.containsKey(src) || !adjList.containsKey(dest)) {
                System.out.println("Both locations must exist first.");
                return;
            }
            if (!adjList.get(src).contains(dest)) {
                adjList.get(src).add(dest);
                adjList.get(dest).add(src);
                System.out.println("Road added between " + src + " and " + dest);
            } else {
                System.out.println("Road already exists.");
            }
        }

        // Remove road (edge)
        public void removeRoad(String src, String dest) {
            if (!adjList.containsKey(src) || !adjList.containsKey(dest)) {
                System.out.println("Locations not found.");
                return;
            }
            adjList.get(src).remove(dest);
            adjList.get(dest).remove(src);
            System.out.println("Road removed between " + src + " and " + dest);
        }

        // Display all connections
        public void displayConnections() {
            System.out.println("\n--- City Road Connections ---");
            for (String loc : adjList.keySet()) {
                System.out.println(loc + " -> " + adjList.get(loc));
            }
        }

        public boolean contains(String loc) {
            return adjList.containsKey(loc);
        }

        // Simple BFS traversal using Queue
        public void bfsTraversal(String start) {
            if (!adjList.containsKey(start)) {
                System.out.println("Start location not found.");
                return;
            }
            Set<String> visited = new HashSet<>();
            Queue<String> queue = new LinkedList<>();
            queue.add(start);
            visited.add(start);
            System.out.println("\nBFS Traversal from " + start + ":");
            while (!queue.isEmpty()) {
                String node = queue.poll();
                System.out.print(node + " ");
                for (String neighbor : adjList.get(node)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
            System.out.println();
        }
    }

    // -----------------------------
    // Member 3: AVL Tree for Location Data
    // -----------------------------
    static class AVLNode {
        String key;
        int height;
        AVLNode left, right;

        AVLNode(String key) {
            this.key = key;
            this.height = 1;
        }
    }

    static class AVLTree {
        AVLNode root;

        int height(AVLNode N) {
            return (N == null) ? 0 : N.height;
        }

        int getBalance(AVLNode N) {
            return (N == null) ? 0 : height(N.left) - height(N.right);
        }

        AVLNode rightRotate(AVLNode y) {
            AVLNode x = y.left;
            AVLNode T2 = x.right;
            x.right = y;
            y.left = T2;
            y.height = Math.max(height(y.left), height(y.right)) + 1;
            x.height = Math.max(height(x.left), height(x.right)) + 1;
            return x;
        }

        AVLNode leftRotate(AVLNode x) {
            AVLNode y = x.right;
            AVLNode T2 = y.left;
            y.left = x;
            x.right = T2;
            x.height = Math.max(height(x.left), height(x.right)) + 1;
            y.height = Math.max(height(y.left), height(y.right)) + 1;
            return y;
        }

        AVLNode insert(AVLNode node, String key) {
            if (node == null) return new AVLNode(key);
            if (key.compareTo(node.key) < 0)
                node.left = insert(node.left, key);
            else if (key.compareTo(node.key) > 0)
                node.right = insert(node.right, key);
            else return node;

            node.height = 1 + Math.max(height(node.left), height(node.right));
            int balance = getBalance(node);

            // Rotations
            if (balance > 1 && key.compareTo(node.left.key) < 0)
                return rightRotate(node);
            if (balance < -1 && key.compareTo(node.right.key) > 0)
                return leftRotate(node);
            if (balance > 1 && key.compareTo(node.left.key) > 0) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
            if (balance < -1 && key.compareTo(node.right.key) < 0) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
            return node;
        }

        void inOrder(AVLNode node) {
            if (node != null) {
                inOrder(node.left);
                System.out.print(node.key + " ");
                inOrder(node.right);
            }
        }

        void displayLocations() {
            System.out.println("\n--- Locations (AVL Tree In-Order) ---");
            inOrder(root);
            System.out.println();
        }
    }

    // -----------------------------
    // Member 4: Menu + Integration
    // -----------------------------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Graph cityGraph = new Graph();
        AVLTree locationTree = new AVLTree();

        while (true) {
            System.out.println("\n--- Smart City Route Planner ---");
            System.out.println("1. Add a new location");
            System.out.println("2. Remove a location");
            System.out.println("3. Add a road between locations");
            System.out.println("4. Remove a road");
            System.out.println("5. Display all connections");
            System.out.println("6. Display all locations (AVL Tree)");
            System.out.println("7. Traverse city using BFS");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter location name: ");
                    String loc = sc.nextLine();
                    cityGraph.addLocation(loc);
                    locationTree.root = locationTree.insert(locationTree.root, loc);
                    break;

                case "2":
                    System.out.print("Enter location name to remove: ");
                    cityGraph.removeLocation(sc.nextLine());
                    break;

                case "3":
                    System.out.print("Enter source: ");
                    String src = sc.nextLine();
                    System.out.print("Enter destination: ");
                    String dest = sc.nextLine();
                    cityGraph.addRoad(src, dest);
                    break;

                case "4":
                    System.out.print("Enter source: ");
                    src = sc.nextLine();
                    System.out.print("Enter destination: ");
                    dest = sc.nextLine();
                    cityGraph.removeRoad(src, dest);
                    break;

                case "5":
                    cityGraph.displayConnections();
                    break;

                case "6":
                    locationTree.displayLocations();
                    break;

                case "7":
                    System.out.print("Enter starting location for BFS: ");
                    cityGraph.bfsTraversal(sc.nextLine());
                    break;

                case "0":
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
