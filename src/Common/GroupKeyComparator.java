package Common;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;


public class GroupKeyComparator extends WritableComparator {
	protected GroupKeyComparator() {
		super(PointKey.class, true);
	}
	
	@SuppressWarnings("rawtypes")
	public int comapre(WritableComparable w1, WritableComparable w2) {
		PointKey k1 = (PointKey)w1;
		PointKey k2 = (PointKey)w2;
		
		int l1 = Util.GetIndexOfLevel(Util.basicInfo, k1, Util.basicInfo.depth);
		int l2 = Util.GetIndexOfLevel(Util.basicInfo, k2, Util.basicInfo.depth);
		int cmp = Integer.compare(l1, l2);
		
		return cmp;
	}
}
