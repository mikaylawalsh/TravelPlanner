package sol;

import src.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * class for a travel controller: loads file and makes graph and determines
 * whether to use Dijkstra or BFS
 */
public class TravelController implements ITravelController<City, Transport> {
    private TravelGraph graph;

    /**
     * constructor for travel controller
     */
    public TravelController() {
        this.graph = new TravelGraph();
    }

    /**
     * loads 2 files and makes a graph from the vertices and edges
     * @param citiesFile    the filename of the cities csv
     * @param transportFile the filename of the transportations csv
     * @return a String which tells whether it was successfully loaded or not
     */
    @Override
    public String load(String citiesFile, String transportFile) {
        TravelCSVParser parser = new TravelCSVParser();

        Function<Map<String, String>, Void> addVertex = map -> {
            this.graph.addVertex(new City(map.get("name")));
            return null;
        };

        Function<Map<String, String>, Void> buildConnections = map -> {
            this.graph.addEdge(this.graph.getCityFromName(map.get("origin")),
                    new Transport(this.graph.getCityFromName(map.get("origin")),
                    this.graph.getCityFromName(map.get("destination")),
                    TransportType.fromString(map.get("type")),
                            Double.parseDouble(map.get("price")),
                            Double.parseDouble(map.get("duration"))));
            return null;
        };

        try {
            parser.parseLocations(citiesFile, addVertex);
        } catch (IOException e) {
            return "Error parsing file: " + citiesFile;
        }

        try {
            parser.parseTransportation(transportFile, buildConnections);
        } catch (IOException e) {
            return "Error parsing file: " + transportFile;
        }
        return "Successfully loaded cities and transportation files.";
    }

    /**
     * finds the fastest route from a source to a destination by implementing
     * Dijkstra
     * @param source      the name of the source city
     * @param destination the name of the destination city
     * @return a list of transports which is the fastest path from source to
     * destination
     */
    @Override
    public List<Transport> fastestRoute(String source, String destination) {
        Function<Transport, Double> getFast = Transport::getMinutes;
        Dijkstra<City, Transport> dijkstra = new Dijkstra<>();
        return dijkstra.getShortestPath(this.graph,
                this.graph.getCityFromName(source),
                this.graph.getCityFromName(destination), getFast);
    }

    /**
     * finds the cheapest route from a source to a destination by implementing
     * Dijkstra
     * @param source      the name of the source city
     * @param destination the name of the destination city
     * @return a list of transports which is the cheapest path from source to
     * destination
     */
    @Override
    public List<Transport> cheapestRoute(String source, String destination) {
        Function<Transport, Double> getCheap = Transport::getPrice;
        Dijkstra<City, Transport> dijkstra = new Dijkstra<>();
        return dijkstra.getShortestPath(this.graph,
                this.graph.getCityFromName(source),
                this.graph.getCityFromName(destination), getCheap);
    }

    /**
     * finds the most direct route from a source to a destination by
     * implementing BFS
     * @param source      the name of the source city
     * @param destination the name of the destination city
     * @return a list of transports which is the most direct path from source
     * to destination
     */
    @Override
    public List<Transport> mostDirectRoute(String source, String destination) {
        BFS<City, Transport> bfs = new BFS<>();
        return bfs.getPath(this.graph, this.graph.getCityFromName(source),
                this.graph.getCityFromName(destination));
    }
}
