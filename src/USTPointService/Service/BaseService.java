package USTPointService.Service;

import USTPointService.GridIndexEngine.GridIndexEngine;

public class BaseService {
	GridIndexEngine m_idxEngine = null;
	String m_name = "NONE";
	String m_cmd = null;
	String [] m_args = null;
	int m_argNum = 0;
	public BaseService(GridIndexEngine idxEngine) {
		m_idxEngine = idxEngine;
	}
	public String 	GetServiceName() 		{ return m_name;}
	public String 	GetCommand() 			{ return m_cmd;}
	public String[] 	GetArgs()				{ return m_args;}
	public int 		GetArgNum()			{ return m_argNum;}
	public boolean	Run(String[] args)	{ return true;}
}
