package pers.mh.commons;

import java.io.Serializable;

public class Tuple02<A, B> implements Serializable {
	private static final long serialVersionUID = -3097795919908361292L;
	public A f0;
	public B f1;
	
	public Tuple02() {
		
	}

	public Tuple02(A a, B b) {
		f0 = a;
		f1 = b;
	}
}
