
public class Node {

	public String id;
	public int receivePort;
	public String ip;
	
	public Node(int port, String id){
		this.ip = "10.0.2.2";
		this.receivePort = port;
		this.id = id;
	}
	
	public Node(int port, String id, String ip){
		this.ip = ip;
		this.receivePort = port;
		this.id = id;
	}
}
