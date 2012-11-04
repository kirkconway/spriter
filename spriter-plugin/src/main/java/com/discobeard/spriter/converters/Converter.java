package com.discobeard.spriter.converters;

public interface Converter<T, V> {
	
	public V convert(T from);

}
