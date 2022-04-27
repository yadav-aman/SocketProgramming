import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    public static void main(String[] args) {
        try{
            ServerSocket ss = new ServerSocket(6956); // open the server socket
            System.out.println("Server Started");
            while(true){
                Socket soc = ss.accept(); // listen for client request

                // You need to create the ObjectOutputStream before the ObjectInputStream, at both ends
                ObjectOutputStream op = new ObjectOutputStream(soc.getOutputStream()); //create I/O streams for communicating to the client (Connect
                ObjectInputStream ip = new ObjectInputStream(soc.getInputStream()); //create I/O streams for communicating to the client (Connect)

                DataPayload data = (DataPayload) ip.readObject(); //perform communication with client (Receive)
                System.out.println("Received: " + data);
                boolean isPath = data.isPath();
                ResultPayload res = new ResultPayload(isPath);
                op.writeObject(res); //perform communication with client (send)
            }
        }
        catch (IOException | ClassNotFoundException ex) {
            System.out.println("Some Error occurred");
        }
    }
}