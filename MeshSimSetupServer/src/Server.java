import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import sun.misc.IOUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * This is a simple class to handle handshaking until we get peer discovery setup
 * For simplicity, using HTTP for setup
 * 
 * @author Peter Henderson
 *
 */
public class Server {

	public static ArrayList<Node> connectedNodes = new ArrayList<Node>();
	public static int currentLatestPort = 5000;
	
	public static void main(String[] args){
        HttpServer server = null;
		try {
			System.out.println("Server listening on 8080...");
			server = HttpServer.create(new InetSocketAddress(8080), 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
	}

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
        	System.out.println("Received Request...");
        	BufferedReader rd = new BufferedReader(new InputStreamReader(t.getRequestBody()));
        	String id = rd.readLine();
        	int configuredPort = Configuration.getPortFromId(id);
        	if( configuredPort < 0){
        		System.out.println("You have nothing configuration for this device: " + id);
        		String response = "";
        		t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
        	}
        	Node x = new Node(configuredPort, id);
        	
        	String response = "";
        	boolean exists = false;
        	for(Node n : connectedNodes){
        		if(n.id.equals(x.id)){
        			exists = true;
        			continue;
        		}
        		response += (n.id + "," + (n.ip + ":" + n.receivePort) + "\n");
        	}
        	
        	t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            
            if(!exists){
            	connectedNodes.add(x);
            }
 
        }
    }
}
