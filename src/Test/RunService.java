package Test;

import USTPointService.USTPointService;

public class RunService {
	
	public static void main(String[] args) throws Exception {
		String ag = "Range ust_1m.xyz 145480.781 4137365.808 167952.758 4144104.797";
		USTPointService.Run(ag.split(" "));
	}
}
