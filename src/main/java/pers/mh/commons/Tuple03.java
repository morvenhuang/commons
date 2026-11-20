package pers.mh.commons;

import java.io.Serializable;

public class Tuple03<A, B, C> extends Tuple02<A, B> implements Serializable {
	private static final long serialVersionUID = -7705735721791672643L;
	public C f2;
	
	public Tuple03() {
		
	}

	public Tuple03(A a, B b, C c) {
		super(a, b);
		f2 = c;
	}
}
