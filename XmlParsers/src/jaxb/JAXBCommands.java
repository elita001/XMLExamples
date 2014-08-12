package jaxb;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class JAXBCommands {
	
	public static Categories filterProducts(Categories cs){
		List <Category> category_list = cs.getCategory();
		List <Subcategory> subcategory_list = null; 
		List <Product> product_list = null;
		for (int i=0; i<category_list.size(); i++){
			Category c = category_list.get(i);
			subcategory_list = c.getSubcategory();
			for (int j=0; j<subcategory_list.size(); j++){
				Subcategory sc = subcategory_list.get(j);
				product_list = sc.getProduct();
				for (int k=0; k<product_list.size(); k++){
					Product p = product_list.get(k);						
					if (p.isAvailable() != null) 
						product_list.remove(k);
				}
			}		
		}
		System.out.println("Filtering with JAXB ended");
		return cs;
	}
	
	public static void writeProducts(Categories cs){
		List <Category> category_list = cs.getCategory();
		List <Subcategory> subcategory_list = null; 
		List <Product> product_list = null;
		for (int i=0; i<category_list.size(); i++){
			Category c = category_list.get(i);
			System.out.println("***");
			System.out.println("Category: " + c.getName());
			subcategory_list = c.getSubcategory();
			for (int j=0; j<subcategory_list.size(); j++){
				Subcategory sc = subcategory_list.get(j);
				System.out.println("  ***");
				System.out.println("  Subcategory: " + sc.getName());
				product_list = sc.getProduct();
				for (int k=0; k<product_list.size(); k++){
					Product p = product_list.get(k);
					System.out.println("    ***");
					System.out.println("    Product: " + p.getName());
					System.out.println("      colour: " + p.getColour());
					System.out.println("      date: "+p.getDate());
					System.out.println("      manufacturer: "+p.getManufacturer());
					System.out.println("      model: "+p.getModel());						
					System.out.println("      price: "+p.getPrice());						
					if (p.isAvailable() != null) 
						System.out.println("      not available!");
					else 
						System.out.println("      quantity: "+p.getQuantity());
				}
				System.out.println("  ***");
			}
			System.out.println("***");
		}
	}
	
	public static void validateXML(Marshaller m_valid, Categories cats){
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		File schemaLocation = new File("src/products.xsd");
        Schema schema;
		try {
			schema = factory.newSchema(schemaLocation);
			try {				
				m_valid.setSchema(schema);
		        m_valid.marshal(cats, new DefaultHandler());
		        System.out.println("Xml is valid");
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		} catch (SAXException e) {	
			e.printStackTrace();
		}		
	}
	
	public static void marshalling(Marshaller m, String src, Categories cats){
		try {
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);			
			m.marshal(cats, new File(src));
			System.out.println("File "+src+" generated");
		} catch (JAXBException e) {
			e.printStackTrace();
		}		
	}
}
