package sol;

import src.IDijkstra;
import src.IGraph;

import java.util.*;
import java.util.function.Function;

/**
 * class for dijkstra: determine a path based off of a function passed in
 * @param <V> the vertices of a graph
 * @param <E> the edges for a graph
 */
public class Dijkstra<V, E> implements IDijkstra<V, E> {
    /**
     * finds the shortest path based on a given function
     * @param graph       the graph including the vertices
     * @param source      the source vertex
     * @param destination the destination vertex
     * @param edgeWeight the function which determines the weights to pick the
     *                   route
     * @return a list of edges which is the shortest path based on a function
     */
    @Override
    public List<E> getShortestPath(IGraph<V, E> graph, V source, V destination,
                                   Function<E, Double> edgeWeight) {
        HashMap<V, E> cameFrom = new HashMap<>();
        HashMap<V, Double> holdVal = new HashMap<>();

        Comparator<V> dist = Comparator.comparingDouble(holdVal::get);

        PriorityQueue<V> toCheckQueue = new PriorityQueue<>(dist);

        for (V vertex : graph.getVertices()) {
            if (vertex.equals(source)) {
                holdVal.put(vertex, 0.0);
            } else {
                holdVal.put(vertex, Double.POSITIVE_INFINITY);
            }
        }
        toCheckQueue.addAll(graph.getVertices());

        while (!toCheckQueue.isEmpty()) {
            V checkingV = toCheckQueue.poll();
            if (checkingV.equals(destination)) {
              return this.getDestination(graph, source, destination, cameFrom);
            }

            for (E edge : graph.getOutgoingEdges(checkingV)) {

                if (holdVal.get(checkingV) + edgeWeight.apply(edge) <
                        holdVal.get(graph.getEdgeTarget(edge)))
                {
                    holdVal.replace(graph.getEdgeTarget(edge),
                            holdVal.get(checkingV) + edgeWeight.apply(edge));
                    cameFrom.put(graph.getEdgeTarget(edge), edge);
                    toCheckQueue.remove(graph.getEdgeTarget(edge));
                    toCheckQueue.add(graph.getEdgeTarget(edge));
                }
            }
        }
            return new LinkedList<>();
    }

    /**
     * makes a linked list of edges which is the values of a hashmap passed in
     * @param graph the graph including the vertices
     * @param start the source vertex
     * @param end the destination vertex
     * @param cameFrom a hashmap of vertex to an edge used to get there
     * @return a linked list which is the values of cameFrom
     */
    private LinkedList<E> getDestination(IGraph<V, E> graph, V start, V end,
                                         HashMap<V, E> cameFrom){
        LinkedList<E> result = new LinkedList<>();
        V current = end;
        while (!current.equals(start)) {
            if (cameFrom.get(current) == null) {
                return new LinkedList<>();
            }
            result.add(cameFrom.get(current));
            current = graph.getEdgeSource(cameFrom.get(current));
        }
        return result;
    }
}

