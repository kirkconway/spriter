package com.discobeard.spriter.converters;

public interface Converter<F,T> {
	
	public T convert(F from);
}
