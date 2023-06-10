package test;

import org.junit.Assert;
import org.junit.Test;
import sol.Dijkstra;
import sol.TravelController;
import sol.TravelGraph;
import src.City;
import src.IDijkstra;
import src.Transport;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * Your Dijkstra's tests should all go in this class!
 * The test we've given you will pass if you've implemented Dijkstra's correctly, but we still
 * expect you to write more tests using the City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 *
 * TODO: Recreate the test below for the City and Transport classes
 * TODO: Expand on your tests, accounting for basic cases and edge cases
 */
public class DijkstraTest {

    private static final double DELTA = 0.001;

    private SimpleGraph graph;
    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;

    private City detroit;
    private City ridgefield;
    private City sparta;
    private City ridgewood;
    private TravelGraph tgraph;
    private Transport detToSparta;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     *
     * TODO: create more setup methods!
     */
    private void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);

        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.b));
        this.graph.addEdge(this.a, new SimpleEdge(3, this.a, this.c));
        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.e));
        this.graph.addEdge(this.c, new SimpleEdge(6, this.c, this.b));
        this.graph.addEdge(this.c, new SimpleEdge(2, this.c, this.d));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.b));
        this.graph.addEdge(this.d, new SimpleEdge(5, this.e, this.d));
    }

    /**
     * create a city graph
     */
    public void makeCityGraph() {
        this.tgraph = new TravelGraph();

        this.detroit = new City("detroit");
        this.ridgefield = new City("ridgefield");
        this.sparta = new City("sparta");
        this.ridgewood = new City("ridgewood");

        this.tgraph.addVertex(this.detroit);
        this.tgraph.addVertex(this.ridgefield);
        this.tgraph.addVertex(this.sparta);
        this.tgraph.addVertex(this.ridgewood);

        this.detToSparta = new Transport(this.detroit,
                this.sparta, TransportType.BUS, 79, 976);

        this.tgraph.addEdge(this.detroit, new Transport(this.detroit,
                this.ridgefield, TransportType.BUS, 23, 77));
        this.tgraph.addEdge(this.ridgefield, new Transport(this.ridgefield,
                this.ridgewood, TransportType.TRAIN, 60, 39));
        this.tgraph.addEdge(this.ridgewood, new Transport(this.ridgewood,
                this.detroit, TransportType.PLANE, 100, 24));
        this.tgraph.addEdge(this.detroit, this.detToSparta);
    }

    /**
     * tests if getShortestPath returns shortest path
     */
    @Test
    public void testSimple() {
        this.createSimpleGraph();

        IDijkstra<SimpleVertex, SimpleEdge> dijkstra = new Dijkstra<>();
        Function<SimpleEdge, Double> edgeWeightCalculation = e -> e.weight;
        // a -> c -> d -> b
        List<SimpleEdge> path =
            dijkstra.getShortestPath(this.graph, this.a, this.b, edgeWeightCalculation);
        assertEquals(6, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(3, path.size());

        // c -> d -> b
        path = dijkstra.getShortestPath(this.graph, this.c, this.b, edgeWeightCalculation);
        assertEquals(3, SimpleGraph.getTotalEdgeWeight(path), DELTA);
        assertEquals(2, path.size());
    }

    /**
     * tests if getShortestPath returns an empty list if there is no path
     */
    @Test
    public void testNoPath() {
        this.createSimpleGraph();
        Function<SimpleEdge, Double> edgeWeightCalculation = e -> e.weight;
        IDijkstra<SimpleVertex, SimpleEdge> dijkstra = new Dijkstra<>();
        List<SimpleEdge> path = dijkstra.getShortestPath(this.graph, this.e,
                this.a, edgeWeightCalculation);
        LinkedList<SimpleEdge> empty = new LinkedList<>();
        Assert.assertEquals(path, empty);
    }

    /**
     * tests if getShortetPath works on city graph
     */
    @Test
    public void fastTestG() {
        this.makeCityGraph();
        IDijkstra<City, Transport> dijkstra = new Dijkstra<>();
        Function<Transport, Double> getFast = Transport::getMinutes;
        List<Transport> fastPath = dijkstra.getShortestPath(this.tgraph, this.detroit,
                this.sparta, getFast);
        List<Transport> check = new LinkedList<>();
        check.add(this.detToSparta);
        assertEquals(fastPath, check);
    }

    /**
     * tests if fastestRoute works on provided csv files
     */
    @Test
    public void fastTest() {
        TravelController travelController = new TravelController();
        travelController.load("data/cities1.csv",
                "data/transport1.csv");
        List<Transport> fastPath = travelController.fastestRoute(
                "Boston", "Providence");
        assertEquals(fastPath.get(0).getMinutes(), 80, 0.0);
    }

    /**
     * tests if fastestRoute works on provided csv files
     */
    @Test
    public void fastTest2() {
        TravelController travelController = new TravelController();
        travelController.load("data/cities2.csv",
                "data/transport2.csv");
        List<Transport> fastPath = travelController.fastestRoute(
                "Washington", "Providence");
        assertEquals(fastPath.get(0).getMinutes() +
                fastPath.get(1).getMinutes(), 2, 0.0);
    }

    /**
     * tests if fastestRoute works on csv files we made
     */
    @Test
    public void ourFastTest() {
        TravelController travelController = new TravelController();
        travelController.load("data/CitiesByUS.csv",
                "data/TransportsByUs.csv");
        List<Transport> fastestRoute = travelController.fastestRoute(
                "Westerly", "Portland");
        assertEquals(fastestRoute.get(0).getMinutes(), 40.00, 0.0);
    }

    /**
     * tests if fastestRoute works on csv files we made
     */
    @Test
    public void ourFastTest2() {
        TravelController travelController = new TravelController();
        travelController.load("data/Cities2ByUS",
                "data/Transports2ByUs");
        List<Transport> fastestRoute = travelController.fastestRoute(
                "Lisbon", "London");
        assertEquals(fastestRoute.get(0).getMinutes(), 45.00, 0.0);
    }

    /**
     * tests if cheapestRoute works on provided csv files
     */
    @Test
    public void cheapTest() {
        TravelController travelController = new TravelController();
        travelController.load("data/cities2.csv",
                "data/transport2.csv");
        List<Transport> cheapPath = travelController.cheapestRoute(
                "Boston", "Washington");
        assertEquals(cheapPath.get(0).getPrice(), 1, 0.0);
    }

    /**
     * tests if cheapestRoute works on provided csv files
     */
    @Test
    public void cheapTest2() {
        TravelController travelController = new TravelController();
        travelController.load("data/cities2.csv",
                "data/transport2.csv");
        List<Transport> cheapPath = travelController.cheapestRoute(
                "Boston", "Chicago");
        assertEquals(cheapPath.get(0).getPrice() +
                cheapPath.get(1).getPrice(), 5, 0.0);
    }

    /**
     * tests if cheapestRoute works on csv files we made
     */
    @Test
    public void ourCSVCheap(){
        TravelController travelController = new TravelController();
        travelController.load("data/CitiesByUS.csv",
                "data/TransportsByUs.csv");
        List<Transport> cheapPath = travelController.cheapestRoute(
                "Westerly", "Portland");
        assertEquals(cheapPath.get(0).getPrice(), 1, 0.0);
    }

    /**
     * tests if cheapestRoute works on csv files we made
     */
    @Test
    public void ourCSVCheap2(){
        TravelController travelController = new TravelController();
        travelController.load("data/CitiesByUS.csv",
                "data/TransportsByUs.csv");
        List<Transport> cheapPath = travelController.cheapestRoute(
                "Portland", "Westerly");
        assertEquals(cheapPath.get(0).getPrice() +
                        cheapPath.get(1).getPrice(), 610, 0.0);
    }

    /**
     * tests if cheapestRoute works on csv files we made
     */
    @Test
    public void ourCSVCheap3(){
        TravelController travelController = new TravelController();
        travelController.load("data/Cities2ByUS",
                "data/Transports2ByUs");
        List<Transport> cheapestRoute = travelController.fastestRoute(
                "London", "Lisbon");
        assertEquals(cheapestRoute.get(0).getPrice()
                , 400, 0.0);
    }

}
