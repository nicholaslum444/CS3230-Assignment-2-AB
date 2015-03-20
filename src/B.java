import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;


public class B {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))); 
        
        int numTests = sc.nextInt();
        
        print(numTests);
		
        for (int i = 0; i < numTests; i++) {
            int numDirVertices = sc.nextInt();
            int numDirEdges = sc.nextInt();
			sc.nextLine();
			
			print(numDirVertices);
			print(numDirEdges);
			
			ArrayList<String> dirEdges = new ArrayList<String>();	 
			
			for (int j = 0; j < numDirEdges; j++) { 
				String edgePair = sc.nextLine();
				dirEdges.add(edgePair);
			}
			
			print(dirEdges);
			
			BUndirectedGraph graph = new BUndirectedGraph(dirEdges, numDirVertices, numDirEdges);
			graph.init();
			
			pw.write(graph.toString());
			
			//pw.write("\n");
        }
        pw.close(); // do not forget to use this
        sc.close();
    }
	
	public static void print(Object o) {
		//System.err.println(o.toString());
	}

}

class BUndirectedGraph {
	
	ArrayList<String> _dirEdges;
	HashMap<String, HashSet<String>> _adjacencyList;
	ArrayList<ArrayList<String>> _edges;

	public BUndirectedGraph(ArrayList<String> dirEdges, int numDirVertices, int numDirEdges) {
		_adjacencyList = new HashMap<String, HashSet<String>>();
		_edges = new ArrayList<ArrayList<String>>();
		_dirEdges = dirEdges;
	}
	
	public void storeEdge(String from, String to) {
		ArrayList<String> edge = new ArrayList<String>();
		edge.add(from);
		edge.add(to);
		Collections.sort(edge);
		_edges.add(edge);
	}
	
	public void convertDirEdge(String from, String to) {
		// if exist from->to or to->from, dont store the edge
		if (_adjacencyList.containsKey(from) 
				&& _adjacencyList.get(from).contains(to)) {
			// from -> to exist, dont store
		} else if (_adjacencyList.containsKey(to) 
				&& _adjacencyList.get(to).contains(from)) {
			// to -> from exist, dont store
		} else {
			storeEdge(from, to);
		}
		
		// put from->to
		if (_adjacencyList.containsKey(from)) {
			HashSet<String> incidentEdges = _adjacencyList.get(from);
			incidentEdges.add(to);
		} else {
			HashSet<String> incidentEdges = new HashSet<String>();
			incidentEdges.add(to);
			_adjacencyList.put(from, incidentEdges);
		}
		
		// put to->from
		if (_adjacencyList.containsKey(to)) {
			HashSet<String> incidentEdges = _adjacencyList.get(to);
			incidentEdges.add(from);
		} else {
			HashSet<String> incidentEdges = new HashSet<String>();
			incidentEdges.add(from);
			_adjacencyList.put(to, incidentEdges);
		}
		
		// put the edge
		

	}
	
	public void init() {
		for (int i = 0; i < _dirEdges.size(); i++) {
			String[] edgePair = _dirEdges.get(i).split("\\s+");
			
			String from = edgePair[0];
			String fromHead = "H" + from.substring(1);
			String fromTail = "T" + from.substring(1);
			
			String to = edgePair[1];
			String toHead = "H" + to.substring(1);
			String toTail = "T" + to.substring(1);
			
			convertDirEdge(fromTail, from);
			convertDirEdge(from, fromHead);
			convertDirEdge(fromHead, toTail);
			convertDirEdge(toTail, to);
			convertDirEdge(to, toHead);
		}

		print(_adjacencyList);
		
		print(_edges);
		Collections.sort(_edges, new EdgeComparator());
		print(_edges);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		// make the first line
		sb.append(_adjacencyList.size());
		sb.append(" ");
		sb.append(_edges.size());
		sb.append("\n");
		
		// make the edges
		for (int i = 0; i < _edges.size(); i++) {
			ArrayList<String> edge = _edges.get(i);
			sb.append(edge.get(0));
			sb.append(" ");
			sb.append(edge.get(1));
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public static void print(Object o) {
		B.print(o);
	}
}

class EdgeComparator implements Comparator<ArrayList<String>> {

	@Override
	public int compare(ArrayList<String> a, ArrayList<String> b) {
		if (a.get(0).compareTo(b.get(0)) < 0) {
			// a1 < b1
			// return a < b
			return -1;
		} else if (a.get(0).compareTo(b.get(0)) > 0) {
			// a1 > b1
			// return a > b
			return 1;
		} else {
			// a1 == b1
			if (a.get(1).compareTo(b.get(1)) < 0) {
				// a2 < b2
				// return a < b
				return -1;
			} else if (a.get(1).compareTo(b.get(1)) > 0) {
				// a2 > b2
				// return a > b
				return 1;
			} else {
				// a2 == b2
				// return a == b
				return 0;
			}
		}
	}
	
}
