package test;

import org.junit.Assert;
import org.junit.Test;
import sol.BFS;
import sol.TravelController;
import sol.TravelGraph;
import src.City;
import src.Transport;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Your BFS tests should all go in this class!
 * The test we've given you will pass if you've implemented BFS correctly, but we still expect
 * you to write more tests using the City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 *
 * TODO: Recreate the test below for the City and Transport classes
 * TODO: Expand on your tests, accounting for basic cases and edge cases
 */
public class BFSTest {

    private static final double DELTA = 0.001;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;
    private SimpleVertex d;
    private SimpleVertex e;
    private SimpleVertex f;
    private SimpleGraph graph;

    private City detroit;
    private City ridgefield;
    private City sparta;
    private City ridgewood;
    private TravelGraph tgraph;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     *
     */
    public void makeSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("a");
        this.b = new SimpleVertex("b");
        this.c = new SimpleVertex("c");
        this.d = new SimpleVertex("d");
        this.e = new SimpleVertex("e");
        this.f = new SimpleVertex("f");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);
        this.graph.addVertex(this.d);
        this.graph.addVertex(this.e);
        this.graph.addVertex(this.f);

        this.graph.addEdge(this.a, new SimpleEdge(1, this.a, this.b));
        this.graph.addEdge(this.b, new SimpleEdge(1, this.b, this.c));
        this.graph.addEdge(this.c, new SimpleEdge(1, this.c, this.e));
        this.graph.addEdge(this.d, new SimpleEdge(1, this.d, this.e));
        this.graph.addEdge(this.a, new SimpleEdge(100, this.a, this.f));
        this.graph.addEdge(this.f, new SimpleEdge(100, this.f, this.e));
    }

    /**
     * creates a city graph
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

        this.tgraph.addEdge(this.detroit, new Transport(this.detroit,
                this.ridgefield, TransportType.BUS, 23, 77));
        this.tgraph.addEdge(this.ridgefield, new Transport(this.ridgefield,
                this.ridgewood, TransportType.TRAIN, 60, 39));
        this.tgraph.addEdge(this.ridgewood, new Transport(this.ridgewood,
                this.detroit, TransportType.PLANE, 100, 24));
        this.tgraph.addEdge(this.detroit, new Transport(this.detroit,
                this.sparta, TransportType.BUS, 79, 976));
    }

    /**
     * tests if canReach and getPath works on simple graph
     */
    @Test
    public void testBasicBFS() {
        this.makeSimpleGraph();
        BFS<SimpleVertex, SimpleEdge> bfs = new BFS<>();
        Assert.assertTrue(bfs.canReach(this.graph, this.a, this.e));
        List<SimpleEdge> path = bfs.getPath(this.graph, this.a, this.e);
        assertEquals(SimpleGraph.getTotalEdgeWeight(path), 200.0, DELTA);
        assertEquals(path.size(), 2);
    }

    /**
     * tests if canReach returns false when vertex can not be reached
     */
    @Test
    public void testCanNotReach() {
        this.makeSimpleGraph();
        BFS<SimpleVertex, SimpleEdge> bfs = new BFS<>();
        Assert.assertFalse(bfs.canReach(this.graph, this.e, this.a));
    }

    /**
     * tests if canReach returns false on empty graph
     */
    @Test
    public void emptyCanReach() {
        TravelGraph emptyGraph = new TravelGraph();
        BFS<City, Transport> bfs = new BFS<>();
        Assert.assertFalse(bfs.canReach(emptyGraph, this.detroit, this.sparta));
    }

    /**
     * tests if getPath returns an empty list when there is no path
     */
    @Test
    public void testNoPath() {
        this.makeSimpleGraph();
        BFS<SimpleVertex, SimpleEdge> bfs = new BFS<>();
        LinkedList<SimpleEdge> empty = new LinkedList<>();
        Assert.assertEquals(bfs.getPath(this.graph, this.e, this.a), empty);
    }

    /**
     * tests if getPath returns a list of the correct size on city graph
     */
    @Test
    public void cityTest() {
        this.makeCityGraph();
        BFS<City, Transport> bfs = new BFS<>();
        List<Transport> path =
                bfs.getPath(this.tgraph, this.detroit, this.ridgewood);
        assertEquals(path.size(), 2);
    }

    /**
     * tests if csv we made returns mostDirectRoute of correct size
     */
    @Test
    public void testOurCSVBFS(){
        TravelController travelController = new TravelController();
        travelController.load("data/CitiesByUs.csv",
                "data/TransportsByUs.csv");
        Assert.assertEquals(travelController.mostDirectRoute("Westerly",
                "Darien").size(),1);
    }

    /**
     * tests if csv we made returns mostDirectRoute of correct size
     */
    @Test
    public void testOurCSVBFS2(){
        TravelController travelController = new TravelController();
        travelController.load("data/Cities2ByUs",
                "data/Transports2ByUs");
        Assert.assertEquals(travelController.mostDirectRoute("Lisbon",
                "Tokyo").size(),1);
    }

    /**
     * tests if mostDirectRoute returns list of correct size for provided csv
     * files
     */
    @Test
    public void mostDirectTest() {
        TravelController travelController = new TravelController();
        travelController.load("data/cities1.csv",
                "data/transport1.csv");
       Assert.assertEquals(travelController.mostDirectRoute("Boston",
               "Providence").size(), 1);
    }

    /**
     * tests if mostDirectRoute returns list of correct size for provided csv
     * files
     */
    @Test
    public void mostDirectTest2() {
        TravelController travelController = new TravelController();
        travelController.load("data/cities1.csv",
                "data/transport1.csv");
        Assert.assertEquals(travelController.mostDirectRoute(
                "Providence", "Boston").size(), 1);
    }

    /**
     * tests if mostDirectRoute returns list of correct size for provided csv
     * files
     */
    @Test
    public void mostDirectTest3() {
        TravelController travelController = new TravelController();
        travelController.load("data/cities1.csv",
                "data/transport1.csv");
        Assert.assertEquals(travelController.mostDirectRoute(
                "Providence", "New York City").size(),
                2);
    }
}
