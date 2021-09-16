package info.gratour.common.types;

import java.util.Comparator;
import java.util.List;


/**
 * 已排序列表搜索器
 *
 * @param <T> item type
 * @author KwanKin Yau
 */
public class SortedListSearcher<T> {

//	private static final Logger LOGGER = LoggerFactory
//			.getLogger(SortedListSearcher.class);

	private List<T> sortedList;
	private Comparator<Object> comparator;
	private Object key;

	private int i, minIndex, maxIndex, resultIndex;
	private boolean stopped;

	public SortedListSearcher(List<T> sortedList,
			Comparator<Object> comparator, Object key) {
		if (sortedList == null || comparator == null || key == null)
			throw new IllegalArgumentException();

		this.sortedList = sortedList;
		this.comparator = comparator;
		this.key = key;

		minIndex = 0;
		maxIndex = sortedList.size() - 1;
		i = (minIndex + maxIndex) / 2;
		resultIndex = -1;

		stopped = maxIndex < minIndex;

//		if (LOGGER.isDebugEnabled())
//			LOGGER.debug("key=" + key + ", minIndex=" + minIndex
//					+ ", maxIndex=" + maxIndex + ", i=" + i);
	}

	public void setMinIndex(int minIndex) {
		this.minIndex = minIndex;
		i = (this.minIndex + maxIndex) / 2;
		stopped = maxIndex < this.minIndex;
	}

	public void setMaxIndex(int maxIndex) {
		this.maxIndex = maxIndex;
		i = (minIndex + this.maxIndex) / 2;
		stopped = this.maxIndex < minIndex;
	}

	public int compare() {
		if (stopped) {
			String errMsg = "Search operation was stopped.";
			throw new RuntimeException(errMsg);
		}

		Object t = sortedList.get(i);
//		if (LOGGER.isDebugEnabled())
//			LOGGER.debug("compare to :" + t);
		return -comparator.compare(key, t);
	}

	public boolean isStopped() {
		return stopped;
	}

	public void leftSide() {
		if (i == minIndex)
			stopped = true;
		else {
			maxIndex = i - 1;
			i = (minIndex + maxIndex) / 2;
//			LOGGER.debug("maxIndex=" + maxIndex + ", i=" + i);
		}
	}

	public void rightSide() {
		if (i == maxIndex)
			stopped = true;
		else {
			minIndex = i + 1;
			i = (minIndex + maxIndex) / 2;
//			LOGGER.debug("minIndex=" + minIndex + ", i=" + i);
		}
	}

	public void setCurrentAsResult() {
		resultIndex = i;
//		LOGGER.debug("setResult=" + resultIndex);
	}

	public int getResultIndex() {
		return resultIndex;
	}
}
