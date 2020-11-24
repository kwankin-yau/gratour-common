package info.gratour.common.types;

public enum RangeSearchHighCondition {
	LT, LE;

	public boolean isLT() {
		return this == LT;
	}
}
