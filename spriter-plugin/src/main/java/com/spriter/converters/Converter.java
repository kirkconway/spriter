package com.spriter.converters;

public interface Converter<F,T> {
	
	public T convert(F from);
}
