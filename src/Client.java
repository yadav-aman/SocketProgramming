import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client  {
    public static void main(String[] args) throws IOException  {
        Scanner scan = new Scanner(System.in);
        // Taking user inputs
        System.out.print("\nEnter Number of nodes : ");
        int n = scan.nextInt();

        int[][] adjMatrix = new int[n][n];
        System.out.println("Enter the values of matrix : ");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjMatrix[i][j] = scan.nextInt();
            }
        }

        System.out.print("\nEnter path length : ");
        int pathLen = scan.nextInt();

        System.out.print("\nEnter starting node : ");
        String startNode = scan.next();

        System.out.print("\nEnter ending node : ");
        String endNode = scan.next();

        try
        {
            // Open Socket
            Socket clients = new Socket("127.0.0.1",6956);
            System.out.println("\nClient connected to the server");

            // create I/O streams for communicating to the Server
            ObjectOutputStream op = new ObjectOutputStream(clients.getOutputStream());
            System.out.println("Output stream established");

            ObjectInputStream ip = new ObjectInputStream(clients.getInputStream());
            System.out.println("Input stream established\n");

            // Wrapping user input into DataPayload object
            DataPayload data = new DataPayload(adjMatrix,pathLen,startNode, endNode);
            op.writeObject(data);
            op.flush();

            // getting result from the server
            ResultPayload res = (ResultPayload) ip.readObject();
            StringBuilder sb = new StringBuilder();
            if(res.getIsPath()){
                sb.append("Yes there is a path of length ");
            }
            else {
                sb.append("No there is no path of length ");
            }
            sb.append(pathLen).append(" from ").append(startNode).append(" to ").append(endNode).append(" in the graph");
            String pathRes = sb.toString();
            System.out.println(pathRes);

            // Converting byte array to Image
            Image im = ImageIO.read((new ByteArrayInputStream(res.getImage())));
            String fileName = String.format("img-%s-%s%s%s.jpeg", System.currentTimeMillis(), startNode, endNode, pathLen);
            // Saving image
            File outputFile = new File(fileName);
            ImageIO.write((RenderedImage) im, "jpeg", outputFile);
            System.out.println("Image file saved : " +  fileName);

            // Displaying image on JFrame
            JFrame frame = new JFrame(fileName);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setAlwaysOnTop(true);
            frame.getContentPane().add(new JLabel(pathRes));
            frame.pack();
            frame.getContentPane().add(new JLabel(new ImageIcon(im)));
            frame.pack();
            frame.setAlwaysOnTop(true);
            frame.setVisible(true);

            clients.close(); //close the connection
        }
        catch(IOException | ClassNotFoundException ex) {
            System.out.println("Some Error occurred: " + ex);
        }
    }
}