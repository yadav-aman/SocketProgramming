import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import org.apache.commons.collections15.Transformer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

class DataPayload implements Serializable{
    private final int[][] adjMatrix;
    private final ArrayList<ArrayList<Integer>> adjList;
    private final int pathLen;
    private final int startNode, endNode;

    public DataPayload(int[][] adjMatrix, int pathLen, String startNode, String endNode){
        this.adjMatrix = adjMatrix;
        this.adjList = toAdjList();
        this.pathLen = pathLen;
        this.startNode = startNode.toUpperCase().charAt(0) - 'A';
        this.endNode = endNode.toUpperCase().charAt(0) - 'A';
    }

    private ArrayList<ArrayList<Integer> > toAdjList(){
        ArrayList<ArrayList<Integer> > adjList = new ArrayList<>(this.adjMatrix.length);

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

    private void pathDFS(Integer currentNode, boolean[] isVisited, ArrayList<Integer> paths, ArrayList<Integer> lengths){
        if(currentNode.equals(this.endNode)){
            lengths.add(paths.size()-1);
            return;
        }
        isVisited[currentNode] = true;

        for(Integer x: this.adjList.get(currentNode)){
            if(!isVisited[x]){
                paths.add(x);
                pathDFS(x, isVisited, paths, lengths);
                paths.remove(x);
            }
        }

        isVisited[this.endNode] = false;
    }

    public byte[] getImage() throws IOException {
        DirectedSparseGraph<String, String> graph = new DirectedSparseGraph<>();
        for (int i = 0; i < this.adjMatrix.length; i++) {
            String vertex = Character.toString('A' + i);
            graph.addVertex(vertex);
        }

        for (int i = 0; i < this.adjMatrix.length; i++) {
            for (int j = 0; j < this.adjMatrix.length; j++) {
                if(this.adjMatrix[i][j]>=1){
                    String n1 = Character.toString('A' + i);
                    String n2 = Character.toString( 'A' + j);
                    graph.addEdge("Edge" + this.adjMatrix.length*i + j, n1, n2);
                }
            }

        }
        VisualizationImageServer<String, String> vs = new VisualizationImageServer<>(new CircleLayout<>(graph), new Dimension(600, 600));
        Transformer<String, String> transformer = arg0 -> arg0;

        vs.getRenderContext().setVertexLabelTransformer(transformer);

        JFrame frame = new JFrame();
        frame.setBackground(Color.WHITE);
        frame.setUndecorated(true);
        frame.getContentPane().add(vs);
        frame.pack();
        BufferedImage bi = new BufferedImage(vs.getWidth(), vs.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bi.createGraphics();
        vs.print(graphics);
        graphics.dispose();
        frame.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpeg", baos);
        return baos.toByteArray();
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