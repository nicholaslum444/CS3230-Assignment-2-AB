import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class A {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))); 
        
        int numTests = sc.nextInt();
        
        print(numTests);
		
        for (int i = 0; i < numTests; i++) {
            int numGraphVertices = sc.nextInt();
            int numGraphEdges = sc.nextInt();
			sc.nextLine();
			
			print(numGraphVertices);
			print(numGraphEdges);
			
			ArrayList<String> graphEdges = new ArrayList<String>();	 
			
			for (int j = 0; j < numGraphEdges; j++) { 
				String edgePair = sc.nextLine();
				graphEdges.add(edgePair);
			}
			
			print(graphEdges);
			
			AUndirectedGraph graph = new AUndirectedGraph(graphEdges, numGraphVertices);
			
			int numPathVertices = sc.nextInt();
			sc.nextLine();
			
			print(numPathVertices);
			
			String vertexOrder = sc.nextLine();
			
			print(vertexOrder);
			
			AUndirectedPath path = new AUndirectedPath(vertexOrder, numPathVertices);
			
			if (graph.isHamiltonCycle(path)) {
				pw.write("YES");			
			} else {
				pw.write("NO");
			}
			
			pw.write("\n");
        }
        pw.close(); // do not forget to use this
        sc.close();
    }
	
	public static void print(Object o) {
		//System.err.println(o.toString());
	}

}

class AUndirectedPath {

	int numVertices;
	HashMap<String, Integer> vertices;
	ArrayList<ArrayList<String>> edges;
	String start;
	String end;

	public AUndirectedPath(String order, int numVertices) {
		this.numVertices = numVertices;
		this.vertices = new HashMap<String, Integer>();
		this.edges = new ArrayList<ArrayList<String>>();
		
		String[] verticesArray = order.split("\\s+");
		this.start = verticesArray[0];
		this.end = verticesArray[verticesArray.length-1];
		for (int i = 0; i < verticesArray.length; i++) {
			String vertex = verticesArray[i];
			if (this.vertices.containsKey(vertex)) {
				int count = this.vertices.get(vertex);
				this.vertices.put(vertex, count + 1);
			} else {
				this.vertices.put(vertex, 1);
			}
		}
		
		print(vertices);
		
		for (int i = 1; i < verticesArray.length; i++) {
			String from = verticesArray[i-1];
			String to = verticesArray[i];
			
			ArrayList<String> edge = new ArrayList<String>();
			edge.add(from);
			edge.add(to);
			
			this.edges.add(edge);
		}
		
		print(edges);
	}
	
	public static void print(Object o) {
		A.print(o);
	}
	// modified
	
}

class AUndirectedGraph {
	
	int numVertices;
	HashMap<String, HashSet<String>> adjacencyList;

	public AUndirectedGraph(ArrayList<String> edges, int numVertices) {
		this.numVertices = numVertices;
		adjacencyList = new HashMap<String, HashSet<String>>();
		
		for (int i = 0; i < edges.size(); i++) {
			String[] edgePair = edges.get(i).split("\\s+");
			
			String from = edgePair[0];
			String to = edgePair[1];
			
			// put from->to
			if (adjacencyList.containsKey(from)) {
				HashSet<String> incidentEdges = adjacencyList.get(from);
				incidentEdges.add(to);
			} else {
				HashSet<String> incidentEdges = new HashSet<String>();
				incidentEdges.add(to);
				adjacencyList.put(from, incidentEdges);
			}
			
			// put to->from
			if (adjacencyList.containsKey(to)) {
				HashSet<String> incidentEdges = adjacencyList.get(to);
				incidentEdges.add(from);
			} else {
				HashSet<String> incidentEdges = new HashSet<String>();
				incidentEdges.add(from);
				adjacencyList.put(to, incidentEdges);
			}
		}
		
		print(adjacencyList);
	}

	public boolean isHamiltonCycle(AUndirectedPath path) {
		
		// check that p is a cycle
		if (!path.start.equals(path.end)) {
			return false;
		}
		
		// check if the vertices all have _edges.
		Set<String> graphVerticesWithEdges = this.adjacencyList.keySet();
		if (graphVerticesWithEdges.size() != numVertices) {
			return false;
		}
		
		// check if P contains all the vertices in G, only once.
		HashMap<String, Integer> pathVertices = path.vertices;
		for (String graphVertex : graphVerticesWithEdges) {
			if (!pathVertices.containsKey(graphVertex)) {
				return false;
			} else {
				int count = pathVertices.get(graphVertex) - 1;
				if (count <= 0) {
					pathVertices.remove(graphVertex);
				} else {
					pathVertices.put(graphVertex, count);
				}
			}
		}
		
		// if not empty now then means got extra copies of vertices.
		print("path vertices");
		print(pathVertices);
		
		Set<String> remainingPathVertices = pathVertices.keySet();
		// there should be only one vertex left, the end point (duplicate)
		if (remainingPathVertices.size() != 1) {
			return false;
		}
		String lastPathVertex = remainingPathVertices.iterator().next();
		if (pathVertices.get(lastPathVertex) != 1) {
			return false;
		}
		
		// check if _edges in P are also in G.
		ArrayList<ArrayList<String>> pathEdges = path.edges;
		for (ArrayList<String> edge : pathEdges) {
			String from = edge.get(0);
			String to = edge.get(1);
			HashSet<String> incidentToFrom = this.adjacencyList.get(from);
			if (!incidentToFrom.contains(to)) {
				return false;
			}
			HashSet<String> incidentToTo = this.adjacencyList.get(to);
			if (!incidentToTo.contains(from)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static void print(Object o) {
		A.print(o);
	}
}
