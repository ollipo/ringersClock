package fi.utu.tech.weatherInfo;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.IOException;
import java.net.URL;

public class FMIWeatherService {

	private final String CapURL = "https://opendata.fmi.fi/wfs?request=GetCapabilities";
	private final String FeaURL = "https://opendata.fmi.fi/wfs?request=GetFeature";
	private final String ValURL = "https://opendata.fmi.fi/wfs?request=GetPropertyValue";
	private static final String DataURL = "http://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&storedquery_id=fmi::forecast::hirlam::surface::point::multipointcoverage&place=turku&";

	/*
	 * In this method your are required to fetch weather data from The Finnish
	 * Meteorological Institute. The data is received in XML-format.
	 */

	public static WeatherData getWeather() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new URL(DataURL).openStream());
		document.normalize();

		XPath path = XPathFactory.newInstance().newXPath();
		String expression = "normalize-space(/*/*/*/result/MultiPointCoverage/rangeSet/DataBlock/doubleOrNilReasonTupleList)";
		String values = (String) path.compile(expression).evaluate(document, XPathConstants.STRING);

		String[] valuesArray = values.split(" ");

		double temperature = Double.parseDouble(valuesArray[1]);
		double precipitation = Double.parseDouble(valuesArray[16]);

		WeatherData weatherData = new WeatherData(temperature, precipitation);

		return weatherData;
	}

}
