package stax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class StAXCommands {
	public static void filterProducts(String src_input, String src_output){
		XMLInputFactory xmlif = XMLInputFactory.newInstance();
		try { 
			XMLEventReader xmlr = xmlif.createXMLEventReader(new FileInputStream(new File(src_input)));
			XMLOutputFactory xof =  XMLOutputFactory.newInstance();				
			List<XMLEvent> events = new ArrayList<XMLEvent>();
			List<XMLEvent> good_events;				 
			try {					
				XMLEventWriter xew = xof.createXMLEventWriter(new FileOutputStream(new File(src_output)));
				while (xmlr.hasNext()) {
					XMLEvent e = xmlr.nextEvent();
					String localNameStartElement = null;							
					if (e.isStartElement())	{
						localNameStartElement = e.asStartElement().getName().getLocalPart();
						Boolean ok;
						if (localNameStartElement.equals("product")){
							ok = true;
							good_events = new ArrayList<XMLEvent>();
							good_events.add(e);
							while (!(e.isEndElement()&&e.asEndElement().getName().getLocalPart().equals("product"))){
								e = xmlr.nextEvent();
								if (e.isStartElement()&&e.asStartElement().getName().getLocalPart().equals("available")){
									ok = false;
								}
								good_events.add(e);
							}
							if (ok) events.addAll(good_events);
						} else {
							events.add(e);
						}		
					} else {
						events.add(e);
					}
				}
				for (XMLEvent ev: events){
					xew.add(ev);
				}
				xew.flush();							
				xew.close();
				System.out.println("Filtering with StAX ended");
				System.out.println("File "+src_output+" generated");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}				
		} catch (FileNotFoundException ffe) {
			ffe.printStackTrace();
		} catch (XMLStreamException xmlse) {
			xmlse.printStackTrace();
		}
	}
	public static void writeProducts(String src){
		XMLInputFactory xmlif = XMLInputFactory.newInstance();
		try { 
			XMLEventReader xmlr = xmlif.createXMLEventReader(new FileInputStream(new File(src)));									
			while (xmlr.hasNext()) {
				XMLEvent e = xmlr.nextEvent();
				if (e.isStartDocument()){
					System.out.println("Start StAX parsing");
				} else if (e.isStartElement())	{
					System.out.println("\n"+e.asStartElement().getName().getLocalPart()+": ");
					@SuppressWarnings("rawtypes")
					Iterator i = e.asStartElement().getAttributes();
					while (i.hasNext()){
						System.out.print("att: "+i.next().toString()+"; ");
					}	
					i = e.asStartElement().getNamespaces();								
					while (i.hasNext()){									
						System.out.print("ns: "+i.next().toString()+"; ");
					}
				} else if(e.isCharacters()) {
					System.out.print(e.asCharacters().getData().trim());
				} else if (e.isEndDocument()){ 
					System.out.println("Parsing ended");
				}
			}			
		} catch (FileNotFoundException ffe) {
			ffe.printStackTrace();
		} catch (XMLStreamException xmlse) {
			xmlse.printStackTrace();
		}
	}
	public static void validateXML(String xml_src, String xsd_src){
		XMLStreamReader reader;
		try {
			reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(xml_src));		
	        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema;
			try {
				schema = factory.newSchema(new File(xsd_src));			
		        Validator validator = schema.newValidator();
		        try {
					validator.validate(new StAXSource(reader));
					System.out.println("Xml is valid");
				} catch (IOException e) {				
					e.printStackTrace();
				}
			} catch (SAXException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		}
        

	}
}
