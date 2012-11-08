package com.discobeard.spriter.mergers;

public interface Merger <T, K, V>{

	public V merge(T from1, K from2);
}
