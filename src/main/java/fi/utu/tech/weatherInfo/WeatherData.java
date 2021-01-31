package fi.utu.tech.weatherInfo;

/*
 * Class presenting current weather
 * Is returned by  weather service class
 */

public class WeatherData {

	/*
	 * What kind of data is needed? What are the variable types. Define class
	 * variables to hold the data
	 */

	/*
	 * Since this class is only a container for weather data we only need to set the
	 * data in the constructor.
	 */

	double temperature;
	double precipitation;
	boolean isRaining;
	boolean belowZero;

	public WeatherData(double temperature, double precipitation) {

		this.temperature = temperature;
		this.precipitation = precipitation;
		if(precipitation > 0.0) {
			this.isRaining = true;
		}
		else {
			this.isRaining = false;
		}
		if(temperature < 0.0) {
			this.belowZero = true;
		}
		else {
			this.belowZero = false;
		}
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(double precipitation) {
		this.precipitation = precipitation;
	}

	public boolean isRaining() {
		return isRaining;
	}

	public boolean isBelowZero() {
		return belowZero;
	}
}
