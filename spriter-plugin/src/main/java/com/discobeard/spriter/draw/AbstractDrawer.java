package com.discobeard.spriter.draw;


import com.discobeard.spriter.file.FileLoader;
import com.discobeard.spriter.file.Reference;

public abstract class AbstractDrawer<L> {
	
	private final FileLoader<L> loader;
	
	public AbstractDrawer(FileLoader<L> loader){
		this.loader=loader;
	}
	
	abstract public void draw(DrawInstruction instruction);
	
	protected L getFile(Reference reference){
		return loader.get(reference);
	}
}
