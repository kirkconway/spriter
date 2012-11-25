package com.discobeard.spriter.file;

public class Reference {
	
	public int folder;
	public int file;
	
	public Reference(int folder,int file){
		this.folder = folder;
		this.file = file;
	}

	public int getFOLDER() {
		return folder;
	}

	public int getFILE() {
		return file;
	}
	
	@Override
	public int hashCode(){
		return (folder+","+file).hashCode(); 
	}
	
	@Override
	public boolean equals(Object ref){
		return ((Reference)ref).hashCode()==this.hashCode();
	}
}
