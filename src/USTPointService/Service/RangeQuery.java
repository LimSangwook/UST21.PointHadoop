package USTPointService.Service;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


import Common.BasicInfo;
import Common.IndexData;
import Common.PointData;
import USTPointService.GridIndexEngine.GridIndexEngine;

public class RangeQuery extends BaseService {
	BasicInfo m_pointInfo = null;
	TreeMap<Integer, IndexData> m_indexDataMap = null;
	Rectangle2D.Double m_rangeRect = new Rectangle2D.Double();
	String m_pointName;
	
	public RangeQuery(GridIndexEngine idxEngine) {
		super(idxEngine);
		// TODO Auto-generated constructor stub
	}

	public boolean Run(String[] args)	{
		System.out.println();
		System.out.println("### Start RangeQuery ###");
		
		
		m_pointName = args[0];
		m_idxEngine.SetUsePointName(m_pointName);
		m_pointInfo = m_idxEngine.GetPointInfo(m_pointName);
		m_indexDataMap = m_idxEngine.GetIndexData(m_pointName);
		
		ParseArgs(args);
		PrintSetupInfo();
		
		// 검색에 필요한 Index찾기
		ArrayList<Integer> findIndexList = GetFindRangeIndex();
		
		long startTime = System.currentTimeMillis();
		// 검색된 Index로 통계 내기
		IndexData idxData = GetStat(findIndexList);
		
		System.out.println("Range Stat : " + idxData);
		long endTime = System.currentTimeMillis(); 
		System.out.println((endTime-startTime)/1000.0 +" 초 걸림");
		return true;
	}
	
	private void PrintSetupInfo() {
		System.out.println();
		System.out.println("### Setup Information ###");
		System.out.println("PointSet : " + m_pointName);
		System.out.println("Range("+ m_rangeRect.getX() + ", " + m_rangeRect.getY() 
							+ " ~ " + (m_rangeRect.getX() + m_rangeRect.getWidth()) 
							+ ", "  + (m_rangeRect.getY() + m_rangeRect.getHeight()));
	}
	
	private IndexData GetStat(ArrayList<Integer> findIndexList) {
		IndexData statData = new IndexData();
		statData.offset = -1; // 쓰이지 않음으로...
		
		// 포함되는 인덱스와 걸쳐있는 인덱스 구분하기
		ArrayList<Integer> containedIndexList = new ArrayList<Integer>();
		ArrayList<Integer> nContainedIndexList = new ArrayList<Integer>();
		
		for (Integer idxKey : findIndexList) {
			Rectangle2D.Double idxRect = GetIndexRect(idxKey);
			Rectangle2D.Double compareRect = new Rectangle2D.Double(m_rangeRect.getX(), m_rangeRect.getY(), m_rangeRect.getWidth() + 0.001, m_rangeRect.getHeight() + 0.001);
			if (compareRect.contains(idxRect) == true) {
				containedIndexList.add(idxKey);
			} else {
				nContainedIndexList.add(idxKey);
			}
		}
		
		// 포함되지 않는 인덱스 처리 - 점가져와서 하나하나 처리
		ArrayList<PointData> ptList = m_idxEngine.LoadPointData(m_pointName, nContainedIndexList);
		Iterator<PointData> ptDataIter = ptList.iterator();

		double totZ = 0.0;
		boolean first =true;
		while (ptDataIter.hasNext()) {
			PointData ptData = ptDataIter.next();
			if (m_rangeRect.contains(ptData.GetX(), ptData.GetY()) == true) {
				if (first == true) {
					statData.SetMaxPT(ptData.clone());
					statData.SetMinPT(ptData.clone());
					first = false;
				}
				if (statData.GetMaxPT().GetZ() < ptData.GetZ()) statData.SetMaxPT(ptData.clone());
				if (statData.GetMinPT().GetZ() > ptData.GetZ()) statData.SetMinPT(ptData.clone());
				statData.cnt ++;
				totZ += ptData.GetZ();
			}
		}
		if (statData.cnt > 0) statData.avgZ = (float)(statData.cnt + totZ) / (statData.cnt);
		
		// 포함된 인덱스 처리 - 인덱스 정보에서 바로 처리
		Iterator<Integer> iter = containedIndexList.iterator();
		while(iter.hasNext()) {
			int key = iter.next();
			IndexData idxData = m_indexDataMap.get(key);
			statData.avgZ = (statData.avgZ * statData.cnt + idxData.avgZ * idxData.cnt) / (statData.cnt+idxData.cnt);
			statData.cnt += idxData.cnt;
			if (first == true) {
				statData.SetMaxPT(idxData.GetMaxPT().clone());
				statData.SetMinPT(idxData.GetMinPT().clone());
				first = false;
			}
			if (statData.GetMaxPT().GetZ() < idxData.GetMaxPT().GetZ()) statData.SetMaxPT(idxData.GetMaxPT().clone());
			if (statData.GetMinPT().GetZ() > idxData.GetMinPT().GetZ()) statData.SetMinPT(idxData.GetMinPT().clone());
		}
		
		return statData;
	}

	private ArrayList<Integer> GetFindRangeIndex() {
		TreeMap<Integer, IndexData> oriMap = m_indexDataMap;
		ArrayList<Integer> keyList = new ArrayList<Integer>();
		Set<Integer> keySet = oriMap.keySet();
		for (Integer key : keySet) {
			if (ContainedIndex(key) == true) {
				keyList.add(key);				
			}
		}
		System.out.println("Finded Index Count :  " + keyList.size());
		return keyList;
	}

	private boolean ContainedIndex(Integer key) {
		Rectangle2D.Double idxRect = GetIndexRect(key);
		
		Rectangle2D.Double compareRect = new Rectangle2D.Double(m_rangeRect.getX(), m_rangeRect.getY(), m_rangeRect.getWidth() + 0.001, m_rangeRect.getHeight() + 0.001);
		if (compareRect.intersects(idxRect) == true) {
			return true;
		}
		return false;
	}

	private Rectangle2D.Double GetIndexRect(Integer key) {
		double rangeX, rangeY, midX, midY;
		int cntOfIndex, levelIdx;
		
		BasicInfo tmpBI = new BasicInfo(m_pointInfo);
		
		for (int i = 2; i <= m_pointInfo.depth ; i++) {
			rangeX = tmpBI.maxX - tmpBI.minX;
			rangeY = tmpBI.maxY - tmpBI.minY;
			midX = tmpBI.minX + rangeX / 2.0;
			midY = tmpBI.minY + rangeY / 2.0;
			cntOfIndex = (int)Math.pow(4, m_pointInfo.depth - i);
			levelIdx = ((key.intValue()-1)/ cntOfIndex) % 4;
			switch(levelIdx){
			case 0:	tmpBI.maxX = midX;	tmpBI.minY = midY;	break;
			case 1:	tmpBI.minX = midX;	tmpBI.minY = midY;	break;
			case 2:	tmpBI.maxX = midX;	tmpBI.maxY = midY;	break;
			case 3:	tmpBI.minX = midX;	tmpBI.maxY = midY;	break;
			}
		}
		
		Rectangle2D.Double idxRect = new Rectangle2D.Double();
		idxRect.setRect(tmpBI.minX, tmpBI.minY, tmpBI.maxX - tmpBI.minX, tmpBI.maxY - tmpBI.minY);
		return idxRect;
	}

	private void ParseArgs(String[] args) {
		m_rangeRect.setRect(Double.parseDouble(args[1])
								, Double.parseDouble(args[2])
								, Double.parseDouble(args[3]) - Double.parseDouble(args[1])
								, Double.parseDouble(args[4]) - Double.parseDouble(args[2]));
	}
}
