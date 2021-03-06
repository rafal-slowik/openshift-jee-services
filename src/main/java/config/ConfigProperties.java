package config;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;

/**
 * 
 * 
 * @author Rafal Slowik, rafal2308@gmail.com
 *
 */
@ApplicationScoped
public class ConfigProperties {

	private static final ResourceBundle bundle = ResourceBundle.getBundle("config");
	
	public String findByKey(String key) {
		return bundle.getString(key);
	}
	
	public String formatProperty(String key, Object ...argumentsToReplace) {
		String pattern = findByKey(key);
		return MessageFormat.format(pattern, argumentsToReplace);
	}
}
