package Common;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class XYSortPointKeyComparator extends WritableComparator{
	protected XYSortPointKeyComparator() {
		super(XYSortPointKey.class, true);
	}
	@SuppressWarnings("rawtypes")
	public int comapre(WritableComparable w1, WritableComparable w2) {
		XYSortPointKey k1 = (XYSortPointKey)w1;
		XYSortPointKey k2 = (XYSortPointKey)w2;
		int cmp = k1.compareTo(k2);
		return cmp;
	}
}
