package travelGraph;

import java.util.Map;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import flights.Flight;
import someExceptions.NoSuchNodeException;
import someExceptions.NoSuchPathException;

/**
 * A Graph class (generic) with nodes and directed edges. Nodes store edges they
 * can travel to (destination edges), but do not store edges that travel to
 * them. // Seems not to be a requirement. Simplifies things. //
 * 
 * @author kael
 * @param <T2> type of value stored in the nodes of this graph.
 * @param <T1> type of value stored in the edges of this graph
 */

// GRAPH AND FLIGHT DIRECTORY SETUP
// --------------@Anthony  @Julia--------- In progress pseudocode to building the Flight Directory--what do you think of steps?------
// Steps are in order.
/**
 * Steps to building the Flight Directory (Graph):
 * (Our ultimate goal is to construct a Graph where all the cities are nodes, and the graph knows which cities are directly connected.) 
 * 
 * This will take a bunch of steps: when we make a Flight, we have to see if the cities that it leaves from/arrives at are already in the Graph or not.
 * If we find a city that's not in the graph, we make a node and add it. Also, if we find an Arrival City that isn't already directly connected to a
 * departure city, we "connect" them. Connections ("edges") are stored like this: <Node, Set<Nodes that are arrival cities>>. 
 * 
 * Making this pseudocode into code line by line should be all that is needed. This should be broken down into helper methods, and many of the tasks 
 * already have methods written up in Node, Graph, flight, or by Java, etc.
 * 
 * 0. Create a new Graph.
 * 1. Open a flights csv file. Start reading. For each line in the file: 
 * 		a. Create a new Flight object from the text info.
 * 		b. If the name of the departure city (an attribute of the Flight object, like flight.departureCity or something) is
 * 		not yet in the Graph (by checking the graph's nodeMap.keySet().contains()), create this node and add it to the Graph. Do the same check for the arrival city.
 * 				-> If you are adding new nodes, you construct it with one String parameter -- value (the name of the city).
 * 				-> We also need put the Flight object in an Edge<Flight, String> to connect the departure (pointingFrom) and arrival (pointingTo) nodes.
 * 					Recall that our Graph is a HashMap. It maps Nodes of departure cities to the Edge that has a flight leaving from that city. 
 * 
 * 
 * 					Example: suppose the first flight we've found is from Toronto to Venice. If we don't yet have nodes for them, we make new ones by passing
 * 					the Strings "Toronto" and "Venice" into the Node constructor. Then, we create an Edge whose value will be the actual Flight object.
 *					For the sake of this example, call this Edge x. Then x.value will be this Flight we've just found, x.pointingTo will be Venice, and x.pointingFrom will
 *					be Toronto. In our map, we need the node whose value is Toronto the map to this Edge containing the Flight that leaves from Toronto.
 * 2. Done!!!!!
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * A directed graph of Node<T2>s connected by Edge<T1>s.
 * @author g5gilenk
 *
 * @param <T2> the type of values in this Graph's Nodes.
 * @param <T1> the type of values in this Graph's Edges.
 */

public class Graph<T2, T1> implements Serializable{
	

	private static final long serialVersionUID = -7022401234809676662L;
	
	// Each Node X will be mapped to a Set of Edges.
	private HashMap<Node<T2>, Set<Edge<T1, T2>>> contents;
	
	public Graph() {
		contents = new HashMap<Node<T2>, Set<Edge<T1, T2>>>();
	}
	
	/**
	 * Returns a Set of Nodes in this Graph. 
	 * @return a Set of Nodes in this Graph
	 */
	public Set<Node<T2>> getNodes() {
		return this.contents.keySet();
	}
	
	/**
     * Returns the Node from this Graph with the given value.
     * @param value the value of the Node to return
     * @return the Node from this Graph with the given value
     * @throws NoSuchNodeException if there is no Node with value
     *    value in this Graph
     */
	public Node<T2> getNode(T2 value) throws NoSuchNodeException {
		Node<T2> n = new Node<T2>(value);
		if (contents.containsKey(n)) {
			return n;
		}
		throw new NoSuchNodeException();
	}

	/**
	 * Returns the contents of this Graph.
	 * @return the contents of this Graph.
	 */
	public HashMap<Node<T2>, Set<Edge<T1, T2>>> getContents() {
		return this.contents;
	}
	
	/**
     * Adds a new Node with the given value to this Graph. 
     * @param value the value of the new Node
     */
    public void addNode(T2 value) { //catch this error
    	Node<T2> n = new Node<T2>(value);
    	Set<Edge<T1, T2>> s = new HashSet<Edge<T1, T2>>();
    	this.contents.put(n, s);
    }
    
	/**
     * Adds a new Node with the given value to this Graph. 
     * @param node the new Node to be added
     */
    public void addNode(Node<T2> node) {
    	Set<Edge<T1, T2>> s = new HashSet<Edge<T1, T2>>();
    	this.contents.put(node, s);
    } 
    
    
    /**
     * Returns the Set of Edges stemming from this Node.
     * @param node The Node whose Edges to return
     * @return the Set of Edges of Node node
     */
    public Set<Edge<T1, T2>> getEdges(Node<T2> node) {
    	return contents.get(node);
    }
    
    /**
     * Adds an Edge from node1 to node2 in this Graph. If this 
     * given Edge already exists between the two nodes, does nothing.
     * @param node1 the node to add an edge to node2
     * @param node2 the node to add an edge from node1
     * @param edge the edge to add from node1 to node2
     * @throws NoSuchNodeException if node1 or node2 is not in
     *    this Graph
     */
    public void addEdge(Node<T2> node1, Node<T2> node2, Edge<T1, T2> edge)
    		throws NoSuchNodeException {
    	Set<Edge<T1, T2>> allEdges = getEdges(node1);
    	if (!allEdges.contains(edge)) {
    		edge.setPointingFrom(node1);
    		edge.setPointingTo(node2);
    		allEdges.add(edge);
    	}
    }
    
    /**
     * Adds an edge between the nodes with the given values in this Graph. 
     * @param value1 ID of the node to add an edge to and from
     * @param value2 ID of the node to add an edge to and from
     * @throws NoSuchNodeException 
     * @throws NoSuchNodeException there is no Node with ID
     *    id1 or ID id2 in this Graph.
     */
    public void addEdge(T2 value1, T2 value2, Edge<T1, T2> edge) 
    		throws NoSuchNodeException {
		Node<T2> node1 = this.getNode(value1);
		Node<T2> node2 = this.getNode(value2);
		this.addEdge(node1, node2, edge);
		}
    
    /**
     * Returns true iff the node with the given value is in this graph.
     * @param value the value of the node
     * @return true iff the node with the given value is in this graph
     */
    public boolean containsNode(T2 value) {
    	Node<T2> n = new Node<T2>(value);
    	return contents.containsKey(n);
    }
    
    @Override
    public String toString() {
    	Set<Node<T2>> allNodes = this.getNodes();
    	String s = "";
    	if (allNodes.isEmpty()) {
    		return "This graph is empty.";
    	}
    	for (Node<T2> n : allNodes) {
    		s += n.getValue() + ": " + this.getEdges(n) + "\n";
    	}
    	return s;
    }
}
