package info.gratour.common.types;

public enum RangeSearchLowCondition {
	GT, GE;

	public boolean isGT() {
		return this == GT;
	}
}
