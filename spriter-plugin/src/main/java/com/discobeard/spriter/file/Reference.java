package com.discobeard.spriter.file;

public class Reference {
	
	final private int FOLDER;
	final private int FILE;
	
	public Reference(int folder,int file){
		this.FOLDER = folder;
		this.FILE = file;
	}

	public int getFOLDER() {
		return FOLDER;
	}

	public int getFILE() {
		return FILE;
	}
	
	@Override
	public int hashCode(){
		return (FOLDER+","+FILE).hashCode(); 
	}
	
	@Override
	public boolean equals(Object ref){
		return ((Reference)ref).hashCode()==this.hashCode();
	}
}
