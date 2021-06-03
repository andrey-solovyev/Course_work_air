package org.example.configuration;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SacsConfiguration {
	
	private Configuration restConfig;
	private Configuration soapConfig;

	@Autowired
	private ConfigurationDecoder decoder;


	public SacsConfiguration() throws ConfigurationException {
		restConfig = new PropertiesConfiguration("SACSRestConfig.properties");
		soapConfig = new PropertiesConfiguration("SACSSoapConfig.properties");
	}


	public String getRestProperty(String key) {
		return restConfig.getString(key);
	}
	
	public String getSoapProperty(String key) {
		return soapConfig.getString(key);
	}
	

	public String getEncodedSoapProperty(String key) {
		return decoder.decode(soapConfig.getString(key));
	}

	public String getEncodedRestProperty(String key) {
		return decoder.decode(restConfig.getString(key));
	}

}
