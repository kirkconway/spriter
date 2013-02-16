package com.spriter.mergers;

public interface ISpriterMerger <T, K, V>{

	public V merge(T from1, K from2);
}
