package models.map;

import models.Constants;
import models.Enums.Direction;
import models.Location;

import java.util.*;

public class MapMinPathFinder {
    public class Node implements Comparable<Node>{
        public Location location;
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

        public int getQuantity() {
            return this.steps + 10 * this.turns;
        }

        public double getEnergyCost() {
            return (double)(getQuantity())/ Constants.EACH_TILE_ENERGY_COST;
        }

        @Override
        public int compareTo(Node second) {
            return Integer.compare(this.getQuantity(), second.getQuantity());
        }
    }

    public boolean canReachTarget(Tile[][] map, Location start, Location end) {
        int rows = map.length;
        int cols = map[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<Location> queue = new LinkedList<>();

        queue.add(start);
        visited[start.y()][start.x()] = true;

        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        while (!queue.isEmpty()) {
            Location current = queue.poll();

            if (current.equals(end)) return true;

            for (int i = 0; i < 4; i++) {
                int newX = current.x() + dx[i];
                int newY = current.y() + dy[i];
                if (newX < 0 || newX >= cols || newY < 0 || newY >= rows) continue;
                if (canGoTo(map[newY][newX]) && !visited[newY][newX]) {
                    visited[newY][newX] = true;
                    queue.add(new Location(newX, newY));
                }
            }
        }

        return false;
    }

    public ArrayList<Node> findPath(Tile[][] map, Location start, Location end) {
        if (!canReachTarget(map, start, end)) return new ArrayList<>();

        int rows = map.length;
        int cols = map[0].length;

        int[][][] minTurns = new int[rows][cols][4]; // visited have 4 direction for each tile
        for (int[][] row : minTurns)
            for (int[] cell : row)
                Arrays.fill(cell, Integer.MAX_VALUE);


        PriorityQueue<Node> queue = new PriorityQueue<>();

        Node startNode = new Node(start, null, 0, 0, -1);
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (currentNode.location.equals(end)) {
                return constructPath(currentNode);
            }

            int[] xChanges = {0, 0, -1, 1}; // up:0 down:1 right:2 left:3
            int[] yChanges = {-1, 1, 0, 0};

            for(int i = 0; i < 4; i++) {
                int newX = currentNode.location.x() + xChanges[i];
                int newY = currentNode.location.y() + yChanges[i];
                if (newX < 0 || newX >= cols || newY < 0 || newY >= rows) continue;

                int newTurns;
                if (currentNode.direction == -1) newTurns = currentNode.turns;
                else if (currentNode.direction == i) newTurns = currentNode.turns;
                else newTurns = currentNode.turns + 1;


                if (canGoTo(map[newY][newX])) {
                    if (newTurns >= minTurns[newY][newX][0]) continue;
                    minTurns[newY][newX][i] = newTurns;

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
        Collections.reverse(path);
        return path;
    }

    private boolean canGoTo(Tile tile) {
        if (!tile.canWalkOnTile()) return false;
        return true;
    }

}
