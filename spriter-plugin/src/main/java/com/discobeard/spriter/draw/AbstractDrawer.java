package com.discobeard.spriter.draw;


import com.discobeard.spriter.file.AbstractLoader;
import com.discobeard.spriter.file.Reference;

public abstract class AbstractDrawer<L> {
	
	private final AbstractLoader<L> loader;
	
	public AbstractDrawer(AbstractLoader<L> loader){
		this.loader=loader;
	}
	
	abstract public void draw(DrawInstruction instruction);
	
	protected L getFile(Reference reference){
		return loader.get(reference);
	}
}
