
public class Configuration {
	//Mapping of ports for IDs
	
	public static String[] ANDROID_IDS = {"c0afb34d8c947f3", "35caef3d84900823"};
	public static int[] ports = {5002, 6000};

	public static int getPortFromId(String id){
		for(int i =0;i<ANDROID_IDS.length;i++){
			if(id.equals(ANDROID_IDS[i])){
				return ports[i];
			}
		}
		return -1;
	}
}
