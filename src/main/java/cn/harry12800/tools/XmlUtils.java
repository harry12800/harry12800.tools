package cn.harry12800.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtils {

	/**
	 * 获取节点上的属性值
	 * 
	 * @param xmlPath
	 * @param xpath
	 * @param attrName
	 * @return
	 * @throws DocumentException
	 */
	public static String getNodeAttrValue(String xmlPath, String xpath, String attrName) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // 获取一个DocumentBuilderFactory的实例
		DocumentBuilder db = dbf.newDocumentBuilder(); // 使用工厂生成一个DocumentBuilder
		File file = new File(xmlPath); // 打开文件，获得句柄
		Document doc = db.parse(file); // 使用dom解析xml文件
		XPathFactory pathFactory = XPathFactory.newInstance();
		// 使用XPathFactory工厂创建 XPath 对象
		XPath xpathTool = pathFactory.newXPath();
		// 使用XPath对象编译XPath表达式
		XPathExpression pathExpression = xpathTool.compile(xpath);
		// 计算 XPath 表达式得到结果
		Object result = pathExpression.evaluate(doc, XPathConstants.NODESET);
		// 节点集node-set转化为NodeList
		// 将结果强制转化成 DOM NodeList
		org.w3c.dom.NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node item = nodes.item(i);
			System.out.println(i);
			NamedNodeMap attributes = item.getAttributes();
			for (int j = 0; j < attributes.getLength(); j++) {
				if (attrName.equals(attributes.item(j).getNodeName())) {
					return attributes.item(j).getNodeValue();
				}
			}
		}
		return null;
	}

	/**
	 * 获取节点上的属性值
	 * 
	 * @param xmlPath
	 * @param xpath
	 * @param attrName
	 * @return
	 * @throws DocumentException
	 */
	public static List<String> getNodeAttrValues(String xmlPath, String xpath,
			String attrName) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // 获取一个DocumentBuilderFactory的实例
		DocumentBuilder db = dbf.newDocumentBuilder(); // 使用工厂生成一个DocumentBuilder
		File file = new File(xmlPath); // 打开文件，获得句柄
		Document doc = db.parse(file); // 使用dom解析xml文件
		ArrayList<String> arrayList = new ArrayList<String>(0);
		XPathFactory pathFactory = XPathFactory.newInstance();
		// 使用XPathFactory工厂创建 XPath 对象
		XPath xpathTool = pathFactory.newXPath();
		// 使用XPath对象编译XPath表达式
		XPathExpression pathExpression = xpathTool.compile(xpath);
		// 计算 XPath 表达式得到结果
		Object result = pathExpression.evaluate(doc, XPathConstants.NODESET);
		// 节点集node-set转化为NodeList
		// 将结果强制转化成 DOM NodeList
		org.w3c.dom.NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node item = nodes.item(i);
			System.out.println(i);
			NamedNodeMap attributes = item.getAttributes();
			for (int j = 0; j < attributes.getLength(); j++) {
				if (attrName.equals(attributes.item(j).getNodeName())) {
					System.out.println(attributes.item(j).getNodeValue());
					arrayList.add(attributes.item(j).getNodeValue());
				}
			}
		}
		return arrayList;
	}

	/**
	 * 获取节点上的属性值
	 * 
	 * @param xmlPath
	 * @param xpath
	 * @param attrName
	 * @return
	 * @throws DocumentException
	 */
	public static List<String> getNodeAttrValues(InputStream in, String xpath,
			String attrName) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // 获取一个DocumentBuilderFactory的实例
		DocumentBuilder db = dbf.newDocumentBuilder(); // 使用工厂生成一个DocumentBuilder
		Document doc = db.parse(in); // 使用dom解析xml文件
		ArrayList<String> arrayList = new ArrayList<String>(0);
		XPathFactory pathFactory = XPathFactory.newInstance();
		// 使用XPathFactory工厂创建 XPath 对象
		XPath xpathTool = pathFactory.newXPath();
		// 使用XPath对象编译XPath表达式
		XPathExpression pathExpression = xpathTool.compile(xpath);
		// 计算 XPath 表达式得到结果
		Object result = pathExpression.evaluate(doc, XPathConstants.NODESET);
		// 节点集node-set转化为NodeList
		// 将结果强制转化成 DOM NodeList
		org.w3c.dom.NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node item = nodes.item(i);
			System.out.println(i);
			NamedNodeMap attributes = item.getAttributes();
			for (int j = 0; j < attributes.getLength(); j++) {
				if (attrName.equals(attributes.item(j).getNodeName())) {
					System.out.println(attributes.item(j).getNodeValue());
					arrayList.add(attributes.item(j).getNodeValue());
				}
			}
		}
		return arrayList;
	}

	/**
	 * 获取节点上的属性值
	 * 
	 * @param xmlPath
	 * @param xpath
	 * @param attrName
	 * @return
	 * @throws DocumentException
	 */
	public static String getNodeAttrValue(InputStream in, String xpath, String attrName) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // 获取一个DocumentBuilderFactory的实例
		DocumentBuilder db = dbf.newDocumentBuilder(); // 使用工厂生成一个DocumentBuilder
		Document doc = db.parse(in); // 使用dom解析xml文件
		XPathFactory pathFactory = XPathFactory.newInstance();
		// 使用XPathFactory工厂创建 XPath 对象
		XPath xpathTool = pathFactory.newXPath();
		// 使用XPath对象编译XPath表达式
		XPathExpression pathExpression = xpathTool.compile(xpath);
		// 计算 XPath 表达式得到结果
		Object result = pathExpression.evaluate(doc, XPathConstants.NODESET);
		// 节点集node-set转化为NodeList
		// 将结果强制转化成 DOM NodeList
		org.w3c.dom.NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node item = nodes.item(i);
			System.out.println(i);
			NamedNodeMap attributes = item.getAttributes();
			for (int j = 0; j < attributes.getLength(); j++) {
				if (attrName.equals(attributes.item(j).getNodeName())) {
					return attributes.item(j).getNodeValue();
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param in
	 * @param nodeName
	 * @return
	 * @throws Exception
	 */
	public static String getNodeValueByName(InputStream in, String nodeName)
			throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // 获取一个DocumentBuilderFactory的实例
		DocumentBuilder db = dbf.newDocumentBuilder(); // 使用工厂生成一个DocumentBuilder
		Document doc = db.parse(in); // 使用dom解析xml文件
		NodeList elements = doc.getElementsByTagName(nodeName);
		if (elements == null || elements.getLength() == 0) {
			//			System.out.println("没有节点");
			return null;
		} else {
			return elements.item(0).getTextContent().trim();
		}
	}

	public static void main(String[] args) throws Exception {
		XmlUtils.getNodeAttrValues("E:/Workspaces/harry12800.tools/.classPath",
				"//classpathentry[@kind='src']", "path");
	}

	public static void main1(String[] args) throws Exception {
		//		InputStream is = Thread.currentThread().getContextClassLoader()
		//				.getResourceAsStream("template/gen/curd/dao/entity.xml");
		//		Document doc = DocumentBuilderFactory.newInstance()
		//				.newDocumentBuilder().parse(is);
		//		NodeList childNodes = doc.getFirstChild().getChildNodes();
		//		int length = childNodes.getLength();

		//		String soap = null;
		//		Reader rr=new StringReader(soap); 
		//		DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance(); 
		//		DocumentBuilder domBuilder=builderFactory.newDocumentBuilder(); 
		//		Document document=domBuilder.parse(new InputSource(rr)); 
	}

	public static String getAttrValueByName(InputStream in,
			String tagName, String attrName) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // 获取一个DocumentBuilderFactory的实例
		DocumentBuilder db = dbf.newDocumentBuilder(); // 使用工厂生成一个DocumentBuilder
		Document doc = db.parse(in); // 使用dom解析xml文件
		NodeList elements = doc.getElementsByTagName(tagName);
		if (elements == null || elements.getLength() == 0) {
			//			System.out.println("没有节点");
			return null;
		} else {
			Node item = elements.item(0);
			NamedNodeMap attributes = item.getAttributes();
			if (attributes != null && attributes.getLength() > 0) {
				Node attr = attributes.getNamedItem(attrName);
				return attr.getNodeValue();
			}
			return null;
		}
	}

	/**
	 * 将对象直接转换成String类型的 XML输出
	 * 
	 * @param obj
	 * @return
	 */
	public static String convertToXml(Object obj) {
		// 创建输出流
		StringWriter sw = new StringWriter();
		try {
			// 利用jdk中自带的转换类实现
			JAXBContext context = JAXBContext.newInstance(obj.getClass());

			Marshaller marshaller = context.createMarshaller();
			// 格式化xml输出的格式
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			// 将对象转换成输出流形式的xml
			marshaller.marshal(obj, sw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	/**
	 * 将对象根据路径转换成xml文件
	 * 
	 * @param obj
	 * @param path
	 * @return
	 */
	public static void convertToXml(Object obj, String path) {
		try {
			// 利用jdk中自带的转换类实现
			JAXBContext context = JAXBContext.newInstance(obj.getClass());

			Marshaller marshaller = context.createMarshaller();
			// 格式化xml输出的格式
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			// 将对象转换成输出流形式的xml
			// 创建输出流
			FileWriter fw = null;
			try {
				fw = new FileWriter(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			marshaller.marshal(obj, fw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将String类型的xml转换成对象
	 */
	public static Object convertXmlStrToObject(Class<?> clazz, String xmlStr) {
		Object xmlObject = null;
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			// 进行将Xml转成对象的核心接口
			Unmarshaller unmarshaller = context.createUnmarshaller();
			StringReader sr = new StringReader(xmlStr);
			xmlObject = unmarshaller.unmarshal(sr);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return xmlObject;
	}

	/**
	 * 将file类型的xml转换成对象
	 */
	@SuppressWarnings("unchecked")
	public static <A> A convertXmlFileToObject(Class<A> clazz, String xmlPath) {
		Object xmlObject = null;
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			FileReader fr = null;
			try {
				fr = new FileReader(xmlPath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			xmlObject = unmarshaller.unmarshal(fr);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return (A) xmlObject;
	}
}
