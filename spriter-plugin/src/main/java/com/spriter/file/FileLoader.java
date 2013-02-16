package com.spriter.file;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class FileLoader<I> {

	public HashMap<Reference,I> files = new HashMap<Reference,I>();

	abstract public void load(Reference ref, String path);
	
	public I get(Reference ref){
		return files.get(ref);
	}
	
	/**
	 * @return Array of all loaded references by this loader.
	 */
	public Reference[] getRefs(){
		Reference[] refs = new Reference[this.files.keySet().toArray().length];
		this.files.keySet().toArray(refs);
		return refs;
	}
	
	/**
	 * Searches for a reference which is equal to the given one.
	 * Equal means: the folder and file of two references are the same.
	 * @param ref Reference to search after.
	 * @return Corresponding reference or null if not found.
	 */
	public Reference findReference(Reference ref){
		Reference[] refs = this.getRefs();
		for(Reference r: refs){
			if(r.equals(ref)) return r;
		}
		return null;
	}
	
	/**
	 * Searches for all files in the given folder name.
	 * @param folderName folder to search in
	 * @return array of all references which the given folder contains.
	 */
	public Reference[] findReferencesByFolderName(String folderName){
		Reference[] refs = this.getRefs();
		ArrayList<Reference> files = new ArrayList<Reference>();
		for(Reference ref: refs)
			if(ref.folderName.equals(folderName)) files.add(ref);
		return files.toArray(refs);
	}
	
	/**
	 * Searches for a reference with the given filename and returns it, if it exists.
	 * @param fileName name of the file (complete name with folder name and extension)
	 * @return reference with given filename or null if not found
	 */
	public Reference findReferenceByFileName(String fileName){
		Reference[] refs = this.getRefs();
		for(Reference ref: refs)
			if(ref.fileName.equals(fileName)) return ref;
		return null;
	}
	
	/**
	 * Searches for a reference with the given filename and folder id and returns it, if it exists.
	 * @param fileName name of the file (relative name to the given folder)
	 * @param folderName name of the folder in which the file is.
	 * @param withoutExtension indicates whether to compare with the file extension or not. false means, the extension will be compared, too.
	 * @return the right reference to the file or null if not found
	 */
	public Reference findReferenceByFileNameAndFolder(String fileName, String folderName, boolean withoutExtension){
		Reference[] refs = this.findReferencesByFolderName(folderName);
		for(Reference ref: refs){
			String file = ref.fileName.replaceAll(folderName+"/", "");
			if(withoutExtension) file = file.replaceAll(".png", "");
			if(file.equals(fileName)) return ref;
		}
		return null;
	}
}
