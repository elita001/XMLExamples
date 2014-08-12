package main;
import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import jaxb.Categories;
import jaxb.JAXBCommands;
import stax.StAXCommands;

public class RunParsers {
	final static String XML = "src/products.xml";
	final static String XSD = "src/products.xsd"; 
	final static String NEW_JAXB_XML = "src/filtered_by_jaxb_products.xml";
	final static String NEW_STAX_XML = "src/filtered_by_stax_products.xml";	
	public static void main(String[] args) throws Exception {		
			JAXBContext jc = JAXBContext.newInstance(Categories.class);			
			Unmarshaller u = jc.createUnmarshaller();
			Marshaller m = jc.createMarshaller();
			Categories categories = (Categories) u.unmarshal(new File(XML));			
			JAXBCommands.validateXML(m, categories);
			/*Уберите комментарий ниже, чтобы отобразить
			 *  в консоли разобранный XML-файл			
			 */
//			JAXBCommands.writeProducts(categories);
			JAXBCommands.filterProducts(categories);			
			JAXBCommands.marshalling(m, NEW_JAXB_XML, categories);
			/*Уберите комментарий ниже, чтобы отобразить 
			 * в консоли разобранный с помощью StAX XML-файл
			 */
//			StAXCommands.writeProducts(XML);
			StAXCommands.filterProducts(XML, NEW_STAX_XML);
			StAXCommands.validateXML(XML, XSD);

	}	
}
