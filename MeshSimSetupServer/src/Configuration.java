
public class Configuration {
	//Mapping of ports for IDs
	
	public static String[] ANDROID_IDS = {"46cc61a749a317f7", 
										  "1c0bfec7a4d1b195",
										  "9db7cbf42aa54b4d"};
	//public static String[] ANDROID_IDS = {"c0afb34d8c947f3", "35caef3d84900823"};
	public static int[] ports = {5001, 5002, 5003};

	public static int getPortFromId(String id){
		for (int i = 0; i < ANDROID_IDS.length; i++) {
			if (id.equals(ANDROID_IDS[i])) {
				return ports[i];
			}
		}
		return -1;
	}
}
