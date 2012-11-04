package com.discobeard.spriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.discobeard.spriter.dom.SpriterData;

public class SCMLParser {
	
	private final URL DIRECTORY;
	
	public SCMLParser(String Directory)
	{
		this.DIRECTORY = getClass().getResource("/"+Directory);
		
	}
	
	public SpriterData parse(){
		File folder = new File(DIRECTORY.getPath());
		
		File scmlFile = getSCML(folder);
		try {
			return parseSpriterData(scmlFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private SpriterData parseSpriterData(File SCMLFile) throws JAXBException, FileNotFoundException{
		
		JAXBContext jc;
		jc = JAXBContext.newInstance("com.discobeard.spriter.dom");
		Unmarshaller u = jc.createUnmarshaller();

		@SuppressWarnings("unchecked")
		JAXBElement<SpriterData> root = (JAXBElement<SpriterData>) u.unmarshal(new FileInputStream(SCMLFile));
		SpriterData spriterData = root.getValue();
		return spriterData;
		
	}
	
	private File getSCML(File folder){
		
		FilenameFilter scmlFilter = new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".scml")) {
					return true;
				} else {
					return false;
				}
			}
			
		};
		
		File[] files = folder.listFiles(scmlFilter);
		
		return files[0];
	}
}
