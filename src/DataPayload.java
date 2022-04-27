import java.io.Serializable;
import java.util.Arrays;

class DataPayload implements Serializable{
    private int[][] adjMatrix;
    private int pathLen;
    private int startNode, endNode;

    public DataPayload(int[][] adjMatrix, int pathLen, String startNode, String endNode){
        this.adjMatrix = adjMatrix;
        this.pathLen = pathLen;
        this.startNode = startNode.toUpperCase().charAt(0) - 'A';
        this.endNode = endNode.toUpperCase().charAt(0) - 'A';
    }

    public boolean isPath(){
        return true;
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