import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        try{
            ServerSocket ss = new ServerSocket(6956); // open the server socket
            System.out.println("Server Started");
            while(true){
                Socket soc = ss.accept(); // listen for client request

                // the ObjectOutputStream needs to be created before the ObjectInputStream, at both ends
                ObjectOutputStream op = new ObjectOutputStream(soc.getOutputStream()); //create I/O streams for communicating to the client (Connect)
                ObjectInputStream ip = new ObjectInputStream(soc.getInputStream()); //create I/O streams for communicating to the client (Connect)

                DataPayload data = (DataPayload) ip.readObject(); //perform communication with client (Receive)
                System.out.println("Received: " + data);

                ResultPayload res = new ResultPayload(data.isPath(), data.getImage()); // Wrap result in ResultPayload
                op.writeObject(res); //perform communication with client (send)
                System.out.println("Sent: " + res + "\n");
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            System.out.println("Some Error occurred: " + ex);
        }
    }
}