package sol;

import src.IBFS;
import src.IGraph;

import java.util.*;

/**
 * class for bfs: generically creates a method which finds the most direct route
 * for a given graph
 * @param <V> vertex in graph
 * @param <E> edge associated with vertices
 */
public class BFS<V, E> implements IBFS<V, E> {
    private HashMap<V, E> visited;

    /**
     * constructor for bfs
     */
    public BFS() {
        this.visited = new HashMap<>();
    }

    /**
     * find the most direct path from a start vertex to an end vertex for a
     * given graph passed in
     * @param graph the graph including the vertices
     * @param start the start vertex
     * @param end   the end vertex
     * @return a list of edges which is the path from start vertex to end vertex
     */
    @Override
    public List<E> getPath(IGraph<V, E> graph, V start, V end) {
        LinkedList<E> pathList = new LinkedList<>();
        V current = end;
        if (this.canReach(graph, start, end)) {
            while (!current.equals(start)) {
                pathList.addFirst(this.visited.get(current));
                current = graph.getEdgeSource(this.visited.get(current));
            }
        }
        return pathList;
    }

    /**
     * helper method which determines if a given start vertex can reach an end
     * vertex for a given graph passed in; adds to visited hashmap if it can
     * @param graph the graph including the vertices
     * @param start the start vertex
     * @param end the end vertex
     * @return a boolen which is true if start can reach end and false otherwise
     */
    public boolean canReach(IGraph<V, E> graph, V start, V end) {
        if (graph.getVertices().isEmpty()) {return false;}

        LinkedList<E> toCheck = new LinkedList<>(graph.getOutgoingEdges(start));
        HashSet<E> visitedSet = new HashSet<>();

        while (!toCheck.isEmpty()) {
            E checkingEdge = toCheck.poll();

            if (visitedSet.contains(checkingEdge)) {
                continue;
            }
            visitedSet.add(checkingEdge);
            V targetCity = graph.getEdgeTarget(checkingEdge);
            if (!this.visited.containsKey(targetCity)) {
                    this.visited.put(targetCity, checkingEdge);
            }
            if (targetCity.equals(end)){
                return true;
            }
            else{
                toCheck.addAll(graph.getOutgoingEdges(targetCity));
            }
        }
        return false;
    }
}