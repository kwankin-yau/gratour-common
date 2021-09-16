/*******************************************************************************
 *  Copyright (c) 2019, 2020 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
package info.gratour.common.utils;

import info.gratour.common.types.IntRef;
import info.gratour.common.types.RangeSearchHighCondition;
import info.gratour.common.types.RangeSearchLowCondition;
import info.gratour.common.types.SortedListSearcher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListUtils {

    /**
     * 对已排序列表进行元素查找。
     *
     * @param <T> 元素类型
     * @param list
     *            给定的列表。元素为T类型。
     * @param key
     *            关键字
     * @param comparator
     *            比较器。该比较器在被调用时，第一个总是方法传入的关键字，第二个参数总是给定列表中的元素。
     * @param descending
     *            该给定列表是否以降序排序的。
     * @param outIndex
     *            索引号引用。如果不为null，则返回元素在列表中的索引号，元素未找到时，返回-1。
     * @return 找到的元素。如找不到，则返回null。
     */
    public static <T> T sortedListSearch(final List<T> list,
                                         final Object key, final Comparator<Object> comparator,
                                         final boolean descending, final IntRef outIndex) {
        if (list.size() == 0) {
            if (outIndex != null)
                outIndex.set(-1);
            return null;
        }

        int min_index = 0;
        int max_index = list.size() - 1;

        int i = max_index / 2;
        T result;

        while (true) {
            result = list.get(i);

            int r = comparator.compare(key, result);
            if (descending)
                r = -r;

            if (r == 0) {
                if (outIndex != null)
                    outIndex.set(i);
                return result;
            } else if (r > 0) {
                if (i == max_index)
                    break;

                min_index = i + 1;
                i = (min_index + max_index) / 2;
            } else {
                if (i == min_index)
                    break;

                max_index = i - 1;
                i = (min_index + max_index) / 2;
            }
        }

        if (outIndex != null)
            outIndex.set(-1);
        return null;
    }

    /**
     * 对已排序列表进行元素查找大于（或等于）给定键值的元素。
     *
     * @param <T> 元素类型
     * @param list
     *            给定的列表。元素为T类型。
     * @param minKey
     *            关键字
     * @param comparator
     *            比较器。该比较器在被调用时，第一个总是方法传入的关键字，第二个参数总是给定列表中的元素。
     * @param descending
     *            该给定列表是否以降序排序的。
     * @param outIndex
     *            索引号引用。如果不为null，则返回元素在列表中的索引号，元素未找到时，返回-1。
     * @param lowCond
     *            小值比较条件（大于等于）。
     * @return 大于（或等于）给定键值的元素，如果没有找到，则返回null
     */
    public static <T> T sortedListSearchLower(final List<T> list,
                                              final Object minKey, final Comparator<Object> comparator,
                                              final boolean descending, final RangeSearchLowCondition lowCond,
                                              final IntRef outIndex) {
        if (list.size() == 0) {
            if (outIndex != null)
                outIndex.set(-1);
            return null;
        }

        int min_index = 0;
        int max_index = list.size() - 1;

        int i = max_index / 2;
        T result;

        while (true) {
            result = list.get(i);

            int r = comparator.compare(minKey, result);
            if (descending)
                r = -r;

            if (r == 0) {
                if (lowCond.isGT()) {
                    if (i == max_index)
                        break;

                    min_index = i + 1;
                    i = (min_index + max_index) / 2;
                } else {
                    if (outIndex != null)
                        outIndex.set(i);
                    return result;
                }
            } else if (r > 0) {
                if (i == max_index)
                    break;

                min_index = i + 1;
                i = (min_index + max_index) / 2;
            } else {
                if (i == min_index)
                    break;

                max_index = i - 1;
                i = (min_index + max_index) / 2;
            }
        }

        if (outIndex != null)
            outIndex.set(-1);
        return null;
    }

    /**
     * 对已排序列表进行元素查找小于（或等于）给定键值的元素。
     *
     * @param <T> 元素的类型
     * @param list
     *            给定的列表。元素为T类型。
     * @param minKey
     *            关键字
     * @param comparator
     *            比较器。该比较器在被调用时，第一个总是方法传入的关键字，第二个参数总是给定列表中的元素。
     * @param descending
     *            该给定列表是否以降序排序的。
     * @param outIndex
     *            索引号引用。如果不为null，则返回元素在列表中的索引号，元素未找到时，返回-1。
     * @param highCond
     *            大值比较条件（小于等于）。
     * @return 小于（或等于）给定键值的元素；如果没有找到，则返回null
     */
    public static <T> T sortedListSearchLE(final List<T> list,
                                           final Object minKey, final Comparator<Object> comparator,
                                           final boolean descending, final RangeSearchHighCondition highCond,
                                           final IntRef outIndex) {
        if (list.size() == 0) {
            if (outIndex != null)
                outIndex.set(-1);
            return null;
        }

        int min_index = 0;
        int max_index = list.size() - 1;

        int i = max_index / 2;
        T result;

        while (true) {
            result = list.get(i);

            int r = comparator.compare(minKey, result);
            if (descending)
                r = -r;

            if (r == 0) {
                if (highCond.isLT()) {
                    if (i == min_index)
                        break;

                    max_index = i - 1;
                    i = (min_index + max_index) / 2;
                } else {
                    if (outIndex != null)
                        outIndex.set(i);
                    return result;
                }
            } else if (r > 0) {
                if (i == max_index)
                    break;

                min_index = i + 1;
                i = (min_index + max_index) / 2;
            } else {
                if (i == min_index)
                    break;

                max_index = i - 1;
                i = (min_index + max_index) / 2;
            }
        }

        if (outIndex != null)
            outIndex.set(-1);
        return null;
    }

    /**
     * 对已排序列表进行范围搜索（限定上、下限搜索）。
     * @param <T> 元素类型
     * @param list 已排序的列表
     * @param comparator 比较器
     * @param minKey 范围搜索的下限值
     * @param lowCond 下限比较条件
     * @param maxKey 范围搜索的上限值
     * @param highCond 上限比较条件
     * @return 搜索到的元素列表，没有找到时，返回空列表
     */
    public static <T> List<T> sortedListRangeSearch(final List<T> list,
                                                    final Comparator<Object> comparator, final Object minKey,
                                                    final RangeSearchLowCondition lowCond, final Object maxKey,
                                                    final RangeSearchHighCondition highCond) {

        if (list == null || comparator == null)
            throw new IllegalArgumentException();

        if (minKey != null && lowCond == null)
            throw new IllegalArgumentException();

        if (maxKey != null && highCond == null)
            throw new IllegalArgumentException();

        List<T> result = new ArrayList<T>();
        if (list.size() == 0)
            return result;

        int minIndex = -1, maxIndex = -1;
        if (minKey == null) {
            minIndex = 0;
        } else {
            SortedListSearcher<T> searcher = new SortedListSearcher<T>(list,
                    comparator, minKey);
            while (!searcher.isStopped()) {
                int r = searcher.compare();
                if (r == 0) {
                    if (lowCond.isGT()) {
                        searcher.rightSide();
                    } else {
                        searcher.setCurrentAsResult();
                        searcher.leftSide();
                    }
                } else if (r < 0) {
                    searcher.rightSide();
                } else {
                    // r > 0
                    searcher.setCurrentAsResult();
                    searcher.leftSide();
                }
            }

            minIndex = searcher.getResultIndex();
            if (minIndex < 0)
                return result;
        }

        if (maxKey == null) {
            maxIndex = list.size() - 1;
        } else {
            SortedListSearcher<T> searcher = new SortedListSearcher<T>(list,
                    comparator, maxKey);
            searcher.setMinIndex(minIndex);
            while (!searcher.isStopped()) {
                int r = searcher.compare();
                if (r == 0) {
                    if (highCond.isLT())
                        searcher.leftSide();
                    else {
                        searcher.setCurrentAsResult();
                        searcher.rightSide();
                    }
                } else if (r < 0) {
                    searcher.setCurrentAsResult();
                    searcher.rightSide();
                } else {
                    // r > 0
                    searcher.leftSide();
                }
            }

            maxIndex = searcher.getResultIndex();
            if (maxIndex < 0)
                return result;
        }

        for (int i = minIndex; i <= maxIndex; i++) {
            result.add(list.get(i));
        }

        return result;
    }

    /**
     * 对已排序列表进行范围搜索（只限定下限值搜索）。
     *
     * @param <T> 元素类型
     * @param list 已排序的列表
     * @param comparator 比较器
     * @param minKey 下限值
     * @param lowCond 下限比较条件
     * @param outIndex 第一个符合条件的元素在list中的序号(0开始)
     * @return 符合条件的元素列表。如果没有符合条件的元素，则返回空列表
     */
    public static <T> T sortedListRangeSearch(final List<T> list,
                                              final Comparator<Object> comparator, final Object minKey,
                                              final RangeSearchLowCondition lowCond, final IntRef outIndex) {

        if (list == null || comparator == null)
            throw new IllegalArgumentException();

        if (minKey != null && lowCond == null)
            throw new IllegalArgumentException();

        if (outIndex != null)
            outIndex.set(-1);

        if (list.size() == 0)
            return null;

        SortedListSearcher<T> searcher = new SortedListSearcher<T>(list,
                comparator, minKey);
        while (!searcher.isStopped()) {
            int r = searcher.compare();
            if (r == 0) {
                if (lowCond.isGT()) {
                    searcher.rightSide();
                } else {
                    searcher.setCurrentAsResult();
                    searcher.leftSide();
                }
            } else if (r < 0) {
                searcher.rightSide();
            } else {
                // r > 0
                searcher.setCurrentAsResult();
                searcher.leftSide();
            }
        }

        int index = searcher.getResultIndex();

        if (index < 0)
            return null;
        else {
            if (outIndex != null)
                outIndex.set(index);

            return list.get(index);
        }
    }

    /**
     * 对已排序列表进行范围搜索。
     *
     * @param <T> 元素类型
     * @param list  已排序的列表
     * @param comparator 比较器
     * @param maxKey 上限值
     * @param highCond 上限比较条件
     * @param outIndex 最后一个符合条件的元素在list中的序号(0开始，最大的值的序号)
     * @return 符合条件的元素列表。如果没有符合条件的元素，则返回空列表
     */
    public static <T> T sortedListRangeSearch(final List<T> list,
                                              final Comparator<Object> comparator, final Object maxKey,
                                              final RangeSearchHighCondition highCond, final IntRef outIndex) {

        if (list == null || comparator == null)
            throw new IllegalArgumentException();

        if (maxKey != null && highCond == null)
            throw new IllegalArgumentException();

        if (outIndex != null)
            outIndex.set(-1);

        if (list.size() == 0)
            return null;

        SortedListSearcher<T> searcher = new SortedListSearcher<T>(list,
                comparator, maxKey);
        while (!searcher.isStopped()) {
            int r = searcher.compare();
            if (r == 0) {
                if (highCond.isLT())
                    searcher.leftSide();
                else {
                    searcher.setCurrentAsResult();
                    searcher.rightSide();
                }
            } else if (r < 0) {
                searcher.setCurrentAsResult();
                searcher.rightSide();
            } else {
                searcher.leftSide();
            }
        }

        int index = searcher.getResultIndex();
        if (index < 0)
            return null;
        else {
            if (outIndex != null)
                outIndex.set(index);

            return list.get(index);
        }
    }

}
