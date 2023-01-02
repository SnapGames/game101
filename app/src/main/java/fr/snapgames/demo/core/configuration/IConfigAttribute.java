package fr.snapgames.demo.core.configuration;

import java.util.function.Function;

/**
 * Define the configuration attribute interface to implement the right accessor into the enum.
 *
 * @author Frédric Delorme
 * @since 0.0.2
 **/
public interface IConfigAttribute {

    String getAttrName();

    Function<String, Object> getAttrParser();

    Object getDefaultValue();

    String getAttrDescription();

    String getConfigKey();
}
