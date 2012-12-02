package com.discobeard.spriter.file;

import java.util.HashMap;


public abstract class AbstractLoader<I> {

	protected HashMap<Reference,I> files = new HashMap<Reference,I>();

	abstract public void load(Reference ref, String path);
	
	public I get(Reference ref){
		return files.get(ref);
	}
}
