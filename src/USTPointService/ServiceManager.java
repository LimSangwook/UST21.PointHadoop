package USTPointService;

import USTPointService.GridIndexEngine.GridIndexEngine;
import USTPointService.Service.BaseService;
import USTPointService.Service.KNN;
import USTPointService.Service.PointList;
import USTPointService.Service.RangeQuery;

public class ServiceManager {
	GridIndexEngine idxEngine = new GridIndexEngine();
	
	public boolean SetService(String [] args) {
		String serviceCmd = args[0];
		String[] serviceArgs = new String[args.length - 1];
		
		// Argument 체크
		if (args.length < 1) {
			System.out.println("USTPointService <Command> <arg1> ...");
			return false;
		}
		
		// 서비스에 넘겨줄 argument 만들기	
		for (int i = 0 ; i < args.length - 1; i++) {
			serviceArgs[i] = args[i+1];
		}
		
		// 서비스 실행
		if (RunService(serviceCmd, serviceArgs) == true) {
			System.out.println("Service Completed");
		} else {
			System.out.println("Service Error!!");
			return false;
		}
		return false;
	}

	public boolean RunService(String serviceCmd, String[] serviceArgs) {
		BaseService service = GetService(serviceCmd);
		
		return service.Run(serviceArgs);
	}

	private BaseService GetService(String serviceCmd) {
		BaseService service = null;
		switch(serviceCmd.toUpperCase()) {
		case "RANGE":
			service = (BaseService)new RangeQuery(idxEngine); 
			break;
		case "KNN":
			service = (BaseService)new KNN(idxEngine); 
			break;
		case "LIST":
			service = (BaseService)new PointList(idxEngine); 
			break;
		default :
			service = (BaseService)new BaseService(idxEngine); 
			break;
		}
		return service;
	}
}
