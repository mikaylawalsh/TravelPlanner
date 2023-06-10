package sol;

import src.City;
import src.IGraph;
import src.Transport;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * class for travel graph: makes the connections between vertices and edges
 */
public class TravelGraph implements IGraph<City, Transport> {
    private HashMap<String, City> cityMap;
    private HashMap<String, Transport> transportMap;
    private HashSet<City> citySet;

    /**
     * the constructor for travel graph
     */
    public TravelGraph() {
        this.cityMap = new HashMap<>();
        this.citySet = new HashSet<>();
        this.transportMap = new HashMap<>();
    }

    /**
     * adds a vertex to the graph by putting the name and vertex in hashmap and
     * hashset if it does not already exist
     * @param vertex the vertex
     */
    @Override
    public void addVertex(City vertex) {
        String name = vertex.toString();
        if (!this.cityMap.containsKey(name)) {
        this.cityMap.put(name, vertex);
        this.citySet.add(vertex);
        } else {
            throw new KeyAlreadyExistsException(name + "already exists");
        }
    }

    /**
     * adds an edge to the graph by putting it in hashmap and adding it to the
     * city
     * @param origin the origin of the edge.
     * @param edge the edge we want to add
     */
    @Override
    public void addEdge(City origin, Transport edge) {
        origin.addOut(edge);
        this.transportMap.put(edge.toString(), edge);
    }

    /**
     * gets the city set
     * @return citySet which is a set of all the cities in our graph
     */
    @Override
    public Set<City> getVertices() {
       return this.citySet;
    }

    /**
     * gets the source city of an edge
     * @return source city of edge
     */
    @Override
    public City getEdgeSource(Transport edge) {
        return edge.getSource();
    }

    /**
     * gets the target city of an edge
     * @return target city of edge
     */
    @Override
    public City getEdgeTarget(Transport edge) {
        return edge.getTarget();
    }

    /**
     * gets the edges of a city
     * @return the out going edges of fromVertex
     */
    @Override
    public Set<Transport> getOutgoingEdges(City fromVertex) {
        return fromVertex.getOutgoing();
    }

    /**
     * get a city from its name
     * @param name the name of a city we want
     * @return the city value associated with the name in cityMap
     */
    public City getCityFromName(String name) {
        return this.cityMap.get(name);
    }
}