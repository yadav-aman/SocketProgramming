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
import java.util.Arrays;

class DataPayload implements Serializable{
    private final int[][] adjMatrix;
    private final int pathLen;
    private final int startNode, endNode;

    // Constructor
    public DataPayload(int[][] adjMatrix, int pathLen, String startNode, String endNode){
        this.adjMatrix = adjMatrix;
        this.pathLen = pathLen;
        this.startNode = startNode.toUpperCase().charAt(0) - 'A';
        this.endNode = endNode.toUpperCase().charAt(0) - 'A';
    }

    // check if path exist
    public boolean isPath(){
        return checkPath(this.startNode, this.pathLen) > 0;
    }

    // calculate path
    private int checkPath(int currentNode, int k)
    {
        // Base cases
        // same node
        if (k == 0 && currentNode == this.endNode)
            return 1;
        // direct connection
        if (k == 1 && this.adjMatrix[currentNode][this.endNode] == 1)
            return 1;
        // negative path length
        if (k <= 0)
            return 0;

        int count = 0;

        // Go to all adjacent nodes of currentNode and recur
        for (int i = 0; i < this.adjMatrix.length; i++)
            // if adjacent increase count
            if (this.adjMatrix[currentNode][i] == 1)
                count += checkPath( i, k - 1);

        return count;
    }


    public byte[] getImage() throws IOException {
        // Initialize Direct Sparse Graph of JUNG library
        DirectedSparseGraph<String, String> graph = new DirectedSparseGraph<>();

        // Add vertex to graph
        for (int i = 0; i < this.adjMatrix.length; i++) {
            String vertex = Character.toString('A' + i);
            graph.addVertex(vertex);
        }

        // Add Edges
        for (int i = 0; i < this.adjMatrix.length; i++) {
            for (int j = 0; j < this.adjMatrix.length; j++) {
                if(this.adjMatrix[i][j]>=1){
                    String n1 = Character.toString('A' + i);
                    String n2 = Character.toString( 'A' + j);
                    graph.addEdge("Edge" + this.adjMatrix.length*i + j, n1, n2);
                }
            }

        }

        // Initiate Visualization server
        VisualizationImageServer<String, String> vs = new VisualizationImageServer<>(new CircleLayout<>(graph), new Dimension(600, 600));
        Transformer<String, String> transformer = arg0 -> arg0;
        vs.getRenderContext().setVertexLabelTransformer(transformer);

        // Attach JFrame without it's GUI window
        JFrame frame = new JFrame();
        frame.setBackground(Color.WHITE);
        frame.setUndecorated(true);
        // Add graph to JFrame
        frame.getContentPane().add(vs);
        frame.pack();
        // Create image of graph
        BufferedImage bi = new BufferedImage(vs.getWidth(), vs.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bi.createGraphics();
        vs.print(graphics);
        // Dispose JFrame and Graphic
        graphics.dispose();
        frame.dispose();

        // Write image to Byte output stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpeg", baos);

        // convert OutputStream to byte array
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