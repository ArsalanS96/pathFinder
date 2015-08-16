import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * A graph with a fixed number of vertices implemented using adjacency maps.
 * Space complexity is &Theta;(n + m) where n is the number of vertices and m
 * the number of edges.
 * 
 * @author [Name]
 * @version [Date]
 */
public class HashGraph implements Graph {
	/**
	 * The map edges[v] contains the key-value pair (w, c) if there is an edge
	 * from v to w; c is the cost assigned to this edge. The maps may be null
	 * and are allocated only when needed.
	 */
	private final Map<Integer, Integer>[] edges; //array of maps
	
	//each index in array corresponds to a vertex.
	private final static int INITIAL_MAP_SIZE = 4;

	/** Number of edges in the graph. */
	private int numEdges;
	
	//Sort of a test method
	public static void main(String[] args){
		HashGraph graph = new HashGraph(5);
		graph.add(0, 1);
		graph.add(0, 2, 200);
		graph.add(0, 3, 200);
		graph.add(0, 4, 200);
		//System.out.println(graph.getEdges()[0]);
		//System.out.println(graph.getEdges()[1]);
		//System.out.println(graph.degree(0));
		//graph.toString();
	}
		
	
	/**
	 * Constructs a HashGraph with n vertices and no edges. Time complexity:
	 * O(n)
	 * 
	 * @throws IllegalArgumentException
	 *             if n < 0
	 */
	public HashGraph(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n = " + n);

		// The array will contain only Map<Integer, Integer> instances created
		// in addEdge(). This is sufficient to ensure type safety.
		@SuppressWarnings("unchecked")
		Map<Integer, Integer>[] a = new HashMap[n];
		edges = a;
		}

	/**
	 * Add an edge without checking parameters.
	 */
	private void addEdge(int from, int to, int cost) {
		if (getEdges()[from] == null){
			getEdges()[from] = new HashMap<Integer, Integer>(INITIAL_MAP_SIZE); 
		}
		//map created at that index
		if (getEdges()[from].put(to, cost) == null) {
			setNumEdges(getNumEdges() + 1);
		}
	}

	/**
	 * {@inheritDoc Graph} Time complexity: O(1).
	 */
	@Override
	public int numVertices() {
		return edges.length;
	}

	/**
	 * {@inheritDoc Graph} Time complexity: O(1).
	 */
	@Override
	public int numEdges() {
		return getNumEdges();
	}

	/**
	 * {@inheritDoc Graph}
	 */
	@Override
	public int degree(int v) throws IllegalArgumentException {
		if(edges[v]!=null){ 
			return edges[v].size();
		}
		if(v<0){
			throw new IllegalArgumentException();
		}
	return 0;
	}

	/**
	 * {@inheritDoc Graph}
	 */
	@Override
	public VertexIterator neighbors(int v) { 
		final Map<Integer,Integer> map = edges[v];
		final Iterator<Map.Entry<Integer,Integer>> tmp = map.entrySet().iterator();
		return new VertexIterator() {

			@Override
			public boolean hasNext() {
				return tmp.hasNext();
			}

			@Override
			public int next() throws NoSuchElementException {
				if(tmp.hasNext()) {
					Map.Entry<Integer,Integer> entry = tmp.next();
					return entry.getKey();
				} else {
					throw new NoSuchElementException();
				}
			}
		};
	}
	
	
	/**
	 * {@inheritDoc Graph}
	 */
	@Override
	public boolean hasEdge(int v, int w) {
		if(edges[v].get(w)==null){ //if no edge exists between v,w
			return false;	
		}
		return true;		
	}

	/**
	 * {@inheritDoc Graph}
	 */
	@Override
	public int cost(int v, int w) throws IllegalArgumentException {
		if(v<0||w<0){
			throw new IllegalArgumentException();
		}
		if(edges[v].get(w)!=null){ //if edge exists between v,w
			return edges[v].get(w);	//gets cost associated with v,w
		}
		return NO_COST;
	}

	/**
	 * {@inheritDoc Graph}
	 */
	@Override
	public void add(int from, int to) {
		addEdge(from,to,NO_COST);				
	}

	/**
	 * {@inheritDoc Graph}
	 */
	@Override
	public void add(int from, int to, int c) {
		addEdge(from,to,c);
	}

	/**
	 * {@inheritDoc Graph}
	 */
	@Override
	public void addBi(int v, int w) {
		addEdge(v,w,NO_COST);
		addEdge(w,v,NO_COST);
	}

	/**
	 * {@inheritDoc Graph}
	 */
	@Override
	public void addBi(int v, int w, int c) {
		addEdge(v,w,c);
		addEdge(w,v,c);
	}

	/**
	 * {@inheritDoc Graph}
	 */
	@Override
	public void remove(int from, int to) {
		if(edges[from]!=null){
			edges[from].remove(to);
			}
		
	}

	/**
	 * {@inheritDoc Graph}
	 */
	@Override
	public void removeBi(int v, int w) {
		if(edges[v]!=null){
			edges[v].remove(w);
			}
		if(edges[w]!=null){
			edges[w].remove(v);
				}
		}

	/**
	 * Returns a string representation of this graph.
	 * 
	 * @return a String representation of this graph
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int i=0;i<edges.length;i++){
			Map<Integer, Integer> map = edges[i];
			
			if(map==null){
				continue; //skip this vertex
			}
			
			for (int key : map.keySet()) {
				str.append("{"+i+", "+key+"} ");
			}
		}
		System.out.println(str.toString());
		return str.toString();
	}

	
	//Get method for getting the "edges" field
	public Map<Integer, Integer>[] getEdges() {
		return edges;
	}


	public int getNumEdges() {
		return numEdges;
	}


	public int setNumEdges(int numEdges) {
		this.numEdges = numEdges;
		return numEdges;
	}
	
}