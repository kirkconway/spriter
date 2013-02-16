package com.spriter.file;

public class Reference {
	
	public int folder, file;
	public String folderName, fileName;
	
	public Reference(int folder,int file, String folderName, String fileName){
		this.folder = folder;
		this.file = file;
		this.folderName = folderName;
		this.fileName = fileName;
	}
	public Reference(int folder,int file){
		this(folder,file,null,null);
	}

	public int getFolder() {
		return folder;
	}

	public int getFile() {
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
