import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client  {
    public static void main(String[] args) throws IOException  {
        Scanner scan = new Scanner(System.in);
        System.out.print("\nEnter Number of nodes : ");
        int n = scan.nextInt();

        double[][] adjMatrix = new double[n][n];
        System.out.println("Enter the values of matrix : ");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjMatrix[i][j] = scan.nextDouble();
            }
        }

        System.out.print("\nEnter path length : ");
        double pathLen = scan.nextDouble();

        System.out.print("\nEnter starting node : ");
        String startNode = scan.next();

        System.out.print("\nEnter ending node : ");
        String endNode = scan.next();

        try
        {
            System.out.println("Welcome client");
            Socket clients = new Socket("127.0.0.1",6956);
            System.out.println("Client connected");

            ObjectOutputStream op = new ObjectOutputStream(clients.getOutputStream());
            System.out.println("Output stream OK");

            ObjectInputStream ip = new ObjectInputStream(clients.getInputStream());
            System.out.println("Input stream OK");

            DataPayload data = new DataPayload(adjMatrix,pathLen,startNode, endNode);
            op.writeObject(data);
            op.flush();
            ResultPayload res = (ResultPayload) ip.readObject();
            if(res.getIsPath()){
                System.out.println("Y");
            }
            else {
                System.out.println("N");
            }
            clients.close(); //close the connection
        }
        catch(IOException | ClassNotFoundException ex) {
            System.out.println("Some Error occurred");
        }
    }
}