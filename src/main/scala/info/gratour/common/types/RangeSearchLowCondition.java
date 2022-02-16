/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.types;

public enum RangeSearchLowCondition {
	GT, GE;

	public boolean isGT() {
		return this == GT;
	}
}
