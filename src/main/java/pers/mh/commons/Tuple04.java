package pers.mh.commons;

import java.io.Serializable;

public class Tuple04<A, B, C, D> extends Tuple03<A, B, C> implements Serializable {
	private static final long serialVersionUID = -746037513806343240L;
	public D f3;

	public Tuple04() {

	}

	public Tuple04(A a, B b, C c, D d) {
		super(a, b, c);
		f3 = d;
	}
}
