package USTPointService;

public class USTPointService {
	public static void main(String [] args) throws Exception {
		Run(args);
	}

	
	public static void Run(String[] args) {
		ServiceManager mgr = new ServiceManager();
		mgr.SetService(args);
			
	}
}
