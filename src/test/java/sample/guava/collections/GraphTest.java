package sample.guava.collections;

import com.google.common.graph.*;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.StreamSupport;

public class GraphTest {
  private Logger log = LogManager.getLogger(this.getClass().getName());
  private Graph<String> graph;
  private ValueGraph<String, Integer> valueGraph;
  private Network<String, Tuple2<String, Integer>> network;

  @Before
  public void setUp() {
    MutableGraph<String> graph = GraphBuilder.directed().nodeOrder(ElementOrder.insertion()).build();
    graph.putEdge("a", "b");
    graph.putEdge("b", "c");
    graph.putEdge("b", "d");
    graph.putEdge("c", "e");
    graph.putEdge("d", "e");
    this.graph = ImmutableGraph.copyOf(graph);

    MutableValueGraph<String, Integer> valueGraph = ValueGraphBuilder.directed().nodeOrder(ElementOrder.insertion()).build();
    valueGraph.putEdgeValue("a", "b", 1);
    valueGraph.putEdgeValue("b", "c", 1);
    valueGraph.putEdgeValue("b", "d", 3);
    valueGraph.putEdgeValue("c", "e", 2);
    valueGraph.putEdgeValue("d", "e", 4);
    this.valueGraph = ImmutableValueGraph.copyOf(valueGraph);

    MutableNetwork<String, Tuple2<String, Integer>> network = NetworkBuilder.directed().nodeOrder(ElementOrder.insertion()).build();
    network.addEdge("a", "b", Tuple.of("ab", 1));
    network.addEdge("b", "c", Tuple.of("bc", 1));
    network.addEdge("b", "d", Tuple.of("bd", 3));
    network.addEdge("c", "e", Tuple.of("ce", 2));
    network.addEdge("d", "e", Tuple.of("de", 4));
    this.network = ImmutableNetwork.copyOf(network);
  }


  @Test
  public void testCreateGraph() {
    MutableGraph<String> mutableGraph = GraphBuilder.undirected().build();
    mutableGraph.addNode("a");
    mutableGraph.addNode("b");
    mutableGraph.putEdge("a", "b");
    mutableGraph.putEdge("b", "a");
    Graph<String> graph = ImmutableGraph.copyOf(mutableGraph);
    log.info(graph);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateGraphWithSelfLoops() {
    MutableGraph<String> mutableGraph = GraphBuilder.directed().allowsSelfLoops(false).build();
    mutableGraph.addNode("a");
    // IllegalArgumentException:  Cannot add self-loop edge on node a, as self-loops are not allowed.
    mutableGraph.putEdge("a", "a");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNetworkWithDuplicateEdge() {
    MutableNetwork<String, Integer> mutableNetwork = NetworkBuilder.directed().build();
    mutableNetwork.addEdge("a", "b", 1);
    // IllegalArgumentException: Edge 1 already exists between the following nodes: <a -> b>, so it cannot be reused to connect the following nodes: <a -> c>.
    mutableNetwork.addEdge("a", "c", 1);
  }

  @Test
  public void testCreateNetworkParallelEdges() {
    // allowsParallelEdges(true)
    MutableNetwork<String, Integer> mutableNetwork = NetworkBuilder.directed().allowsParallelEdges(true).build();
    mutableNetwork.addEdge("a", "b", 1);
    // IllegalArgumentException: Edge 1 already exists between the following nodes: <a -> b>, so it cannot be reused to connect the following nodes: <a -> c>.
    mutableNetwork.addEdge("a", "b", 3);
    Network<String, Integer> network = ImmutableNetwork.copyOf(mutableNetwork);
    log.info(network);
    Assertions.assertThat(network.edgesConnecting("a", "b")).contains(1, 3);
  }

  @Test
  public void testContains() {
    Assertions.assertThat(graph.nodes().contains("a")).isTrue();
    Assertions.assertThat(graph.nodes().contains("j")).isFalse();
  }

  @Test
  public void testConnection() {
    Assertions.assertThat(graph.hasEdgeConnecting("a", "b")).isTrue();
  }

  @Test
  public void testPredecessors() {
    Assertions.assertThat(graph.predecessors("a").isEmpty()).isTrue();
    Assertions.assertThat(graph.predecessors("e")).contains("c", "d");
  }

  @Test
  public void testSuccessors() {
    Assertions.assertThat(graph.successors("e")).isEmpty();
    Assertions.assertThat(graph.successors("a")).contains("b");
  }

  @Test
  public void testAdjacentNodes() {
    Assertions.assertThat(graph.adjacentNodes("b")).contains("a", "c", "d");
  }

  @Test
  public void testEdgeValue() {
    Assertions.assertThat(valueGraph.edgeValueOrDefault("b", "c", 0)).isEqualTo(1);
  }

  @Test
  public void testEdgesConnection() {
    Assertions.assertThat(network.edgeConnecting("a", "e").isPresent()).isFalse();
    Assertions.assertThat(network.edgesConnecting("a", "e")).isEmpty();
  }

  @Test
  public void testReachableNodes() {
    Assertions.assertThat(Graphs.reachableNodes(graph, "a")).contains("a", "b", "c", "d", "e");
  }

  @Test
  public void testTraverseGraph() {
    Traverser.forGraph(graph).breadthFirst("a").forEach(System.out::print);
    Assertions.assertThat(Traverser.forGraph(graph).breadthFirst("a")).containsSequence("a", "b", "d", "c", "e");
    Assertions.assertThat(Traverser.forGraph(graph).depthFirstPreOrder("a")).containsSequence("a", "b", "d", "e", "c");
    Assertions.assertThat(Traverser.forGraph(graph).depthFirstPostOrder("a")).containsSequence("e", "d", "c", "b", "a");
    log.info(graph.successors("b"));

    Assertions.assertThat(Traverser.forGraph(valueGraph).breadthFirst("a")).containsSequence("a", "b", "d", "c", "e");
    Assertions.assertThat(Traverser.forGraph(valueGraph).depthFirstPreOrder("a")).containsSequence("a", "b", "d", "e", "c");
    Assertions.assertThat(Traverser.forGraph(valueGraph).depthFirstPostOrder("a")).containsSequence("e", "d", "c", "b", "a");
    log.info(valueGraph.successors("b"));

    Assertions.assertThat(Traverser.forGraph(network).breadthFirst("a")).containsSequence("a", "b", "c", "d", "e");
    Assertions.assertThat(Traverser.forGraph(network).depthFirstPreOrder("a")).containsSequence("a", "b", "c", "e", "d");
    Assertions.assertThat(Traverser.forGraph(network).depthFirstPostOrder("a")).containsSequence("e", "c", "d", "b", "a");
    log.info(network.successors("b"));
  }

  @Test
  public void testTraversal() {
    MutableGraph<Integer> graph = GraphBuilder.directed().nodeOrder(ElementOrder.insertion()).build();
    graph.addNode(1);graph.addNode(2);graph.addNode(3);graph.addNode(4);graph.addNode(5);
    graph.putEdge(1, 2);
    graph.putEdge(2, 3);
    graph.putEdge(2, 4);
    graph.putEdge(3, 5);
    graph.putEdge(4, 5);
    graph.successors(2).forEach(System.out::print);
    Traverser.forGraph(graph).breadthFirst(1).forEach(System.out::print);
  }

}
