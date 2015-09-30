package Common;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class PointKeyComparator extends WritableComparator{
	protected PointKeyComparator() {
		super(PointKey.class, true);
	}
	@SuppressWarnings("rawtypes")
	public int comapre(WritableComparable w1, WritableComparable w2) {
		PointKey k1 = (PointKey)w1;
		PointKey k2 = (PointKey)w2;
		int cmp = k1.compareTo(k2);
		return cmp;
	}

}
