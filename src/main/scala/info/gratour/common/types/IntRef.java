/*******************************************************************************
 *  Copyright (c) 2002, 2013 E-EYE High Tech.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/

package info.gratour.common.types;

/**
 * 整数值引用，通常用于参数返回整数。
 *
 * @author KwanKin Yau
 *
 */
public class IntRef {

	private int value;

	/**
	 * 取整数值
	 *
	 * @return
	 */
	public int get() {
		return value;
	}

	/**
	 * 设整数值
	 *
	 * @param value
	 */
	public void set(int value) {
		this.value = value;
	}
}
