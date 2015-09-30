package USTPointService.Service;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


import Common.BasicInfo;
import Common.IndexData;
import Common.PointData;
import USTPointService.GridIndexEngine.GridIndexEngine;

public class KNN extends BaseService {
	BasicInfo m_pointInfo = null;
	String m_pointName;
	TreeMap<Integer, IndexData> m_indexDataMap = null;
	Point2D.Double m_point;
	int m_SearchNum = 0;
	
	public KNN(GridIndexEngine idxEngine) {
		super(idxEngine);
		// TODO Auto-generated constructor stub
	}

	public boolean Run(String[] args)	{
		System.out.println();
		System.out.println("### Start KNN ###");
		
		m_pointName = args[0];
		m_idxEngine.SetUsePointName(m_pointName);
		m_pointInfo = m_idxEngine.GetPointInfo(m_pointName);
		m_indexDataMap = m_idxEngine.GetIndexData(m_pointName);
		
		ParseArgs(args);
		
		long startTime = System.currentTimeMillis();

		long endTime = System.currentTimeMillis(); 
		System.out.println((endTime-startTime)/1000.0 +" 초 걸림");
		return true;
	}



	private void ParseArgs(String[] args) {
		m_point = new Point2D.Double(Double.parseDouble(args[1])
								, Double.parseDouble(args[2]));
		m_SearchNum = Integer.parseInt(args[3]);
	}
}
