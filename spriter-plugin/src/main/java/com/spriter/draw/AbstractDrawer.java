package com.spriter.draw;


import com.spriter.file.FileLoader;
import com.spriter.file.Reference;

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
