import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

class DataPayload implements Serializable{
    private int[][] adjMatrix;
    private ArrayList<ArrayList<Integer>> adjList;
    private int pathLen;
    private int startNode, endNode;

    public DataPayload(int[][] adjMatrix, int pathLen, String startNode, String endNode){
        this.adjMatrix = adjMatrix;
        this.adjList = toAdjList();
        this.pathLen = pathLen;
        this.startNode = startNode.toUpperCase().charAt(0) - 'A';
        this.endNode = endNode.toUpperCase().charAt(0) - 'A';
    }

    private ArrayList<ArrayList<Integer> > toAdjList(){
        ArrayList<ArrayList<Integer> > adjList = new ArrayList<ArrayList<Integer> >(this.adjMatrix.length);

        for (int i = 0; i < this.adjMatrix.length; i++) {
            adjList.add(new ArrayList<>());
        }

        for (int i = 0; i < this.adjMatrix.length; i++) {
            for (int j = 0; j < this.adjMatrix.length; j++) {
                if(this.adjMatrix[i][j] >= 1){
                    adjList.get(i).add(j);
                }
            }
        }

        return adjList;
    }

    public boolean isPath(){
        boolean[] isVisited = new boolean[this.adjMatrix.length];
        ArrayList<Integer> paths = new ArrayList<>();
        ArrayList<Integer> lengths = new ArrayList<>();

        paths.add(this.startNode);

        pathDFS(this.startNode, isVisited, paths, lengths);

        return lengths.contains(this.pathLen);
    }

    private void pathDFS(Integer source, boolean[] isVisited, ArrayList<Integer> paths, ArrayList<Integer> lengths){
        if(source.equals(this.endNode)){
            lengths.add(paths.size()-1);
            return;
        }
        isVisited[source] = true;

        for(Integer x: this.adjList.get(source)){
            if(!isVisited[x]){
                paths.add(x);
                pathDFS(x, isVisited, paths, lengths);
                paths.remove(x);
            }
        }

        isVisited[this.endNode] = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row: this.adjMatrix){
            sb.append(Arrays.toString(row));
        }
        String adjMatrixStr = sb.toString();
        return "DataPayload{" +
                "adjMatrix=" + adjMatrixStr +
                ", pathLength=" + pathLen +
                ", startNode=" + startNode +
                ", endNode=" + endNode +
                '}';
    }
}