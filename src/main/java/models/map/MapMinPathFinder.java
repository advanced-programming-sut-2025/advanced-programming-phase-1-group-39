package models.map;

import models.Enums.Direction;
import models.Location;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class MapMinPathFinder {
    class Node implements Comparable<Node>{
        Location location;
        Node parent;
        int steps;
        int turns;
        int direction;


        public Node(Location location, Node parent, int steps, int turns, int direction) {
            this.location = location;
            this.parent = parent;
            this.steps = steps;
            this.turns = turns;
            this.direction = direction;
        }

        @Override
        public int compareTo(Node second) {
            return Integer.compare(this.steps + 10 * this.turns, second.steps + 10 * second.turns);
        }
    }

    public ArrayList<Node> findPath(Tile[][] map, Location start, Location end) {
        int rows = map.length;
        int cols = map[0].length;

        boolean[][][] visited = new boolean[rows][cols][4]; // visited have 4 direction for each tile
        PriorityQueue<Node> queue = new PriorityQueue<>();

        Node startNode = new Node(start, null, 0, 0, -1);
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (currentNode.location.equals(end)) {
                constructPath(currentNode);
            }

            int[] xChanges = {0, 0, -1, 1}; // up:0 down:1 right:2 left:3
            int[] yChanges = {-1, 1, 0, 0};

            for(int i = 0; i < 4; i++) {
                int newX = currentNode.location.x() + xChanges[i];
                int newY = currentNode.location.y() + yChanges[i];
                int newTurns;
                if (currentNode.direction == -1) newTurns = currentNode.turns;
                else if (currentNode.direction == i) newTurns = currentNode.turns;
                else newTurns = currentNode.turns + 1;


                if (canGoTo(map[newY][newX], visited[newY][newX][i])) {
                    visited[newY][newX][i] = true;
                    queue.add(new Node(new Location(newX, newY), currentNode, currentNode.steps+1, newTurns, i));
                }
            }
        }

        return new ArrayList<>();
    }

    private ArrayList<Node> constructPath(Node end) {
        Node currentNode = end;
        ArrayList<Node> path = new ArrayList<>();
        path.add(currentNode);

        while(currentNode.parent != null) {
            currentNode = currentNode.parent;

            path.add(currentNode);
        }

        return path;
    }

    private boolean canGoTo(Tile tile, boolean visited) {
        if (visited) return false;
        if (!tile.canWalkOnTile()) return false;
        return true;
    }

}
