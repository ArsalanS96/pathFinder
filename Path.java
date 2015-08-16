import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;


public class Path {
	
	private BufferedReader reader;
	private HashGraph graph;
	private int[] dist;
	private int[] prev;
	private static String fileName;
	
	public static void main(String[] args) {
		if (args.length != 3) {
            System.err.printf("Not enough arguments");
            System.exit(1); // Unix error handling
        }
      
		int start=Integer.parseInt(args[0]);
		int end=Integer.parseInt(args[1]);
		fileName=args[2];		
			
		Path path = new Path();
		
		path.parse();
		path.dist= new int[path.graph.numVertices()];
		path.prev= new int[path.graph.numVertices()];
					
		path.dijkstra(start);
		int totalDist=path.dist[end];
		System.out.println("The shortest distance from vertex "+start+" to vertex "+end+" was "+totalDist);
		path.printPath(start,end);	
 
	}
	
	/**
	 * Prints the path from the start vertex to the end vertex
	 * @param start
	 * @param end
	 */
	private void printPath(int start,int end) throws IllegalArgumentException{
		if(start<0||end>graph.numVertices()){
			throw new IllegalArgumentException("These parameters are out of bounds");
		}
		int i= end;
		ArrayList<Integer> list = new ArrayList<Integer>();
		while(i!=start){
			list.add(i);
			i=prev[i];
		}
		list.add(start);
		Collections.reverse(list);
		System.out.println("The path taken was "+list);
	}
	

	/**
	 * Initialises the reader to allow the text file's information
	 * to be interpreted. Throws an error if the file cannot be found.
	 */
	Path(){
		try 
		{
			reader = new BufferedReader(new FileReader(fileName));
					 
		} catch (IOException e) {
			System.err.println("File not found");
		} 
		
	}

	
	/**
	 * Interprets the data in the text file. Converts the numbers 
	 * given into a HashGraph.
	 */
	private void parse(){
		String[] parsedStr=new String[3];
		boolean sizeFound=false;
		
		int count=0;
		String currentLine=null;
		try{
			while((currentLine=reader.readLine()) != null){
				StringBuilder strBuild= new StringBuilder();
				
				if(currentLine.substring(0,2).equals("//")){
					continue; //skips over comments
				}
										
				//the first number to be found will be the size
				//This if is skipped once size is found
				if(currentLine.matches(".*\\d+.*")&&!sizeFound){ 
					String sizeStr = currentLine.replaceAll("[^0-9]","");
					int size = Integer.parseInt(sizeStr);
					graph=new HashGraph(size);
					sizeFound=true;
					continue;
				}
				
				String[] array = currentLine.split("");
				
				//iterate over every character in this line
				for(int i=0;i<array.length;i++){ 												
					if(array[i].matches("\\d")){ //matches a number
						strBuild.append(array[i]);
					}
					else if(array[i].equals(" ")){ //if the char is a blank space
						parsedStr[count]=strBuild.toString();
						strBuild.setLength(0);//resets the StringBuilder
						//System.out.println(strBuild.capacity());
						if(count==2){
							break;
						}
						count++;
					}
				}
				int node1 = Integer.parseInt(parsedStr[0]);
				int node2 = Integer.parseInt(parsedStr[1]);
				int cost = Integer.parseInt(parsedStr[2]);
				graph.addBi(node1,node2,cost);
				count=0;
			}									
		}
		
		catch(IOException e){
			System.err.println("Error while parsing file");
		}		
	}
	
	/**
	 * Iterates over all vertices while recording their
	 * distance from the source (found in dist[]). It also marks the previously visited
	 * vertices in prev[]
	 * @param source The vertex where this algorithm starts at
	 */
	private void dijkstra(int source){
		boolean[] isVisited = new boolean[graph.numVertices()];
		int u=-1;
		
		dist[source]=0;
		prev[source]=-1;	 //-1 represents the previous vertex as being undefined
		
		/*Initialises the distances of every vertex
		 *in the graph. 
		 * For v!=source, dist = infinity
		 * For v=source, dist= 0 
		 */
		for(int v=0;v<graph.getEdges().length;v++){
			if(v!=source){
				dist[v] = Integer.MAX_VALUE;
				prev[v]=-1;				
			}
		}
		
		while(!terminate(isVisited)){
			u=smallestDist(dist,isVisited);
			isVisited[u]=true;
			Set<Integer> set=graph.getEdges()[u].keySet();
			
			for(int v: set){
				if(!isVisited[v]){
					int alt=dist[u]+1; //change to cost 
					if(alt<dist[v]){
						dist[v]=alt;
						prev[v]=u;
					}
				}
			}
		}
		System.out.println("Dist"+Arrays.toString(dist));	
		System.out.println("Prev"+Arrays.toString(prev));	
		System.out.println("dist[i] is the distance from the source to i");
		System.out.println("prev[i] is the previously found vertex when using djikstra's Algorithm");
	}

	/**
	 * Used to determine if the while condition in 
	 * dijkstra should terminate. It should terminate
	 * isVisited only contains true at every index.
	 * @param isVisited An array of booleans
	 * @return True if it should terminate
	 */
	private boolean terminate(boolean[] isVisited) {
		for(int i=0;i<isVisited.length;i++){
			if(!isVisited[i]){
				return false;
			}			
		}
		return true;
	}


	/**
	 * Finds the vertex with the smallest distance from the 
	 * source. It ignores vertices that have been visited.
	 * @param dist
	 * @param isVisited
	 * @return
	 */
	private int smallestDist(int[] dist,boolean[] isVisited) {
		int max=Integer.MAX_VALUE;
		int index=0;
		for(int i=0;i<dist.length;i++){
			if(isVisited[i]){ //ignores i if it is visited
				continue;
			}
			int temp=dist[i];
			if(temp<max){
				max=temp;
				index=i;
			}
		}
		return index;		
	}
}

		

	
	
	
	