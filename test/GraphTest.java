package test;

import org.junit.Assert;
import org.junit.Test;
import sol.TravelController;
import sol.TravelGraph;
import src.City;
import src.Transport;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;

import javax.management.openmbean.KeyAlreadyExistsException;

import static org.junit.Assert.*;

/**
 * Your Graph method tests should all go in this class!
 * The test we've given you will pass, but we still expect you to write more tests using the
 * City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 */
public class GraphTest {
    private SimpleGraph graph;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;

    private City detroit;
    private City ridgefield;
    private City sparta;
    private City ridgewood;

    private TravelGraph tgraph;

    private Transport detToRField;
    private Transport rWoodToDet;
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

        this.a = new SimpleVertex("A");
        this.b = new SimpleVertex("B");
        this.c = new SimpleVertex("C");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);

        // create and insert edges
        SimpleEdge edgeAB = new SimpleEdge(1, this.a, this.b);
        SimpleEdge edgeBC = new SimpleEdge(1, this.b, this.c);
        SimpleEdge edgeCA = new SimpleEdge(1, this.c, this.a);
        SimpleEdge edgeAC = new SimpleEdge(1, this.a, this.c);

        this.graph.addEdge(this.a, edgeAB);
        this.graph.addEdge(this.b, edgeBC);
        this.graph.addEdge(this.c, edgeCA);
        this.graph.addEdge(this.a, edgeAC);
    }

    /**
     * creates a graph of cities and transports
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

        this.detToRField = new Transport(this.detroit,
                this.ridgefield, TransportType.BUS, 23, 77);
        Transport rFieldToRWood = new Transport(this.ridgefield,
                this.ridgewood, TransportType.TRAIN, 60, 39);
        this.rWoodToDet = new Transport(this.ridgewood,
                this.detroit, TransportType.PLANE, 100, 24);
        this.detToSparta = new Transport(this.detroit,
                this.sparta, TransportType.BUS, 79, 976);

        this.tgraph.addEdge(this.detroit, this.detToRField);
        this.tgraph.addEdge(this.ridgefield, rFieldToRWood);
        this.tgraph.addEdge(this.ridgewood, rWoodToDet);
        this.tgraph.addEdge(this.detroit, detToSparta);
    }

    /**
     * tests if getVertices works on simple graph
     */
    @Test
    public void testGetVertices() {
        this.createSimpleGraph();

        assertEquals(this.graph.getVertices().size(), 3);
        assertTrue(this.graph.getVertices().contains(this.a));
        assertTrue(this.graph.getVertices().contains(this.b));
        assertTrue(this.graph.getVertices().contains(this.c));
    }

    /**
     * tests if addVertex works on city graph
     */
    @Test
    public void testAddVertex() {
        this.makeCityGraph();

        assertEquals(this.tgraph.getVertices().size(), 4);
        assertTrue(this.tgraph.getVertices().contains(this.detroit));
        assertTrue(this.tgraph.getVertices().contains(this.ridgefield));
        assertTrue(this.tgraph.getVertices().contains(this.ridgewood));
        assertTrue(this.tgraph.getVertices().contains(this.sparta));
    }

    /**
     * test if addEdge works on a city graph
     */
    @Test
    public void testAddEdge() {
        this.makeCityGraph();

        assertEquals(this.detroit.getOutgoing().size(), 2);
        assertTrue(this.detroit.getOutgoing().contains(this.detToRField));
        assertTrue(this.detroit.getOutgoing().contains(this.detToSparta));
        assertTrue(this.ridgewood.getOutgoing().contains(this.rWoodToDet));
        Assert.assertFalse(this.sparta.getOutgoing().contains(this.detToSparta));
        assertEquals(this.sparta.getOutgoing().size(), 0);
    }

    /**
     * tests if getCityFromName works on city graph
     */
    @Test
    public void cityFromNameTest() {
        this.makeCityGraph();

        assertEquals(this.tgraph.getCityFromName("detroit"), this.detroit);
    }

    /**
     * tests if getCityFromName returns null when name is not in graph
     */
    @Test
    public void cityFromNameTest2() {
        this.makeCityGraph();

        assertNull(this.tgraph.getCityFromName("allendale"));
    }

    /**
     * tests if load works on given cities and transports files
     */
    @Test
    public void graphLoadTest() {
        TravelController travelController = new TravelController();
        Assert.assertEquals(travelController.load("data/cities1.csv",
                "data/transport1.csv"),
                "Successfully loaded cities and transportation files.");
    }

    /**
     * tests if load works on second given cities and transports files
     */
    @Test
    public void graphLoadTest2() {
        TravelController travelController = new TravelController();
        Assert.assertEquals(travelController.load("data/cities2.csv",
                        "data/transport2.csv"),
                "Successfully loaded cities and transportation files.");
    }

    /**
     * tests if load works on csv files we created
     */
    @Test
    public void ourGraph(){
        TravelController travelController = new TravelController();
        Assert.assertEquals(travelController.load("data/CitiesByUS.csv",
                "data/TransportsByUs.csv"),
                "Successfully loaded cities and transportation files.");
    }

    /**
     * tests if load works on second csv files we created
     */
    @Test
    public void ourGraph2(){
        TravelController travelController = new TravelController();
        Assert.assertEquals(travelController.load("data/Cities2ByUs",
                        "data/Transports2ByUs"),
                "Successfully loaded cities and transportation files.");
    }

    /**
     * tests if load works on empty csv files
      */
    @Test
    public void EmptyLoadTest() {
        TravelController travelController = new TravelController();
        Assert.assertEquals(travelController.load("data/empty.csv",
                        "data/empty.csv"),
                "Successfully loaded cities and transportation files.");
    }

    /**
     * tests if addVertex throws an exception when name is already in graph
     */
    @Test(expected = KeyAlreadyExistsException.class)
    public void addVertexException() {
        this.makeCityGraph();

        this.tgraph.addVertex(new City("sparta"));
    }
}
