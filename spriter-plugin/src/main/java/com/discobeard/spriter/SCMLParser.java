package com.discobeard.spriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.discobeard.spriter.dom.SpriterData;

public class SCMLParser {
	
	final private File scmlFile;
	
	public SCMLParser(File scmlFile)
	{
		this.scmlFile=scmlFile;
	}
	
	public SpriterData parse(){
		try {
			return parseSpriterData(scmlFile);
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: SCML File Not Found :"+ scmlFile.getPath());
			e.printStackTrace();
		} catch (JAXBException e) {
			System.out.println("ERROR: Failed to make scml :"+ scmlFile.getPath() + " contents map to spriter objects, likely your using an SCML version that is not currently supporter");
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
}
