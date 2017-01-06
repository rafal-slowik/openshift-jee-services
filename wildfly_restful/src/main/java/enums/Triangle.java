package enums;

public enum Triangle {
	NOT_EXISTING("error") {
		@Override
		public boolean isCurrentType(long a, long b, long c) {
			boolean existCurrentType = false;
			// non-negatives or zeroes
			if(a <= 0 || b <= 0 || c <= 0) {
				existCurrentType = true; 
			} else if ((a + b <= c) || (a + c <= b) || (b + c <= a)) {
				existCurrentType = true; 
			}
			return existCurrentType;
		}
	}, //
	EQUILATERAL("equilateral") {
		@Override
		// two sides same length
		public boolean isCurrentType(long a, long b, long c) {
			return (a == b && a == c);
		}
	}, //
	ISOSCELES("isosceles") {
		@Override
		public boolean isCurrentType(long a, long b, long c) {
			// all same
			return (a == b || a == c || b == c);
		}
	}, //
	SCALENE("scalene") {
		@Override
		public boolean isCurrentType(long a, long b, long c) {
			// all different
			return (a != b && a != c && b != c);
		}
	};

	private String propertyKey;

	private Triangle(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public abstract boolean isCurrentType(long a, long b, long c);

	public static Triangle getTriangleType(long a, long b, long c) {
		// important - get values ordinal because as first we have to check NOT_EXISTING conditions
		
		for (Triangle triangle : Triangle.values()) {
			if (triangle.isCurrentType(a, b, c)) {
				return triangle;
			}
		}
		throw new IllegalArgumentException("Unexpected exception was thrown!");
	}
}
