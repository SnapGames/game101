# Enum as Configuration

One of the first thing you have to provide, before any game mechanic, it's a way to set configuration values.

To satisfy this need, we will use a well known java structure with in a particular way: `enum` !

## Introduction

First we need to understand what would be the age for our configuration. we need to define some value to be set by
configuration (via a file), or fall back to a default value. the default model must support providing a description. We
will also need to override the configuration value at execution time by providing a different value through the CLI.

- name in configuration file (a key configuration name),
- a name for command line interface ( a simple name),
- a description,
- a default value.

And as values coming from file or CLI, we need to get a converter from text to the real configuration attribute value we
want to provide.

- a converter to translate a String value to a Java Type (native or custom).

So our implementation will be a little more complex to allow an easy way to manager enumeration values:

![the configuration attributes implementation](https://www.plantuml.com/plantuml/png/RP1VIyGm4CJVyodYcwAVG4Jkj5KE_rp1qzSZrqxRbMHRaWqYudStfGebtkl-PfQPMGKnNgApwaHwEEZH6cBLKmDjH3mTeY0eOe5lJGszkHsufMxznxwKFjSTP3ey6uVJiykNXBZxlS_o1tpcO38K2BMdKMZW71TeJRJoPcn4Ojl3EsfPM2lZ0tmYTv7hwS7LRB_Gi_Gw5wfl5VhXGsVv5otdrZbta7veWW97zm-I6oqSMCAnOTPLjTzjTe-bqaLvytg_VzeMPSgn0ZTfyXy0)

## The attribute interface

the `IConfigAttribute` will be our interface to enumeration to define configuration attribute to be provided through a
file of command line:

```java
public interface IConfigAttribute {

    String getAttrName();

    Function<String, Object> getAttrParser();

    Object getDefaultValue();

    String getAttrDescription();

    String getConfigKey();
}
```

So we can first provide an implementation of the enumeration :

```java
public enum ConfigAttribute implements IConfigAttribute {
    APP_TITLE(
            "appTitle",
            "app.main.title",
            "Set the name of the application to be displayed in log and UI",
            "GDemoApp",
            v -> v),
    DEBUG_MODE(
            "debugMode",
            "app.debug.mode",
            "Setting the debug mode (0 to 5)",
            0,
            v -> Integer.valueOf(v));

    //... other values will enhance the enum.

    // internal attributes of the enum entry.
    private final String attrName;
    private final String attrDescription;
    private final Object attrDefaultValue;
    private final Function<String, Object> attrParser;
    private String attrConfigKey;

    ConfigAttribute(
            String attrName,
            String attrConfigKey,
            String attrDescription,
            Object attrDefaultValue,
            Function<String, Object> parser) {
        this.attrName = attrName;
        this.attrConfigKey = attrConfigKey;
        this.attrDescription = attrDescription;
        this.attrDefaultValue = attrDefaultValue;
        this.attrParser = parser;
    }

    // implementation of getters
    //...
}
```

So referencing one configuration key will be nothing else than using the `ConfigAttribut.DEBUG_MODE`, for example.

but this is nothing without the `Configuration` system to retrieve and manage values:

## Configuration class

THe CConfiguration system will provide access to the configuration attribute values, from file AND from command line
argument.

But first, initialize the attributes with their default values:

```Java
public class Configuration {

    IConfigAttribute[] attributes;
    private Map<IConfigAttribute, Object> configurationValues = new ConcurrentHashMap<>();

    public Configuration(IConfigAttribute[] attributes) {
        setAttributes(attributes);
        // initialize all default values.
        Arrays.stream(attributes).forEach(ca -> {
            configurationValues.put(ca, ca.getDefaultValue());
        });
    }
    //...
}
```

Now we can delegate the argument parsing to our Configuration class, to extract values from:

```Java

public class App implements Game {
    //...
    public int parseArgs(String[] args) {
        boolean displayHelpMessage = false;

        if (args.length > 0) {
            for (String arg : args) {
                String[] kv = arg.split("=");
                if (!isArgumentFound(kv)) {
                    displayHelpMessage(kv[0], kv[1]);
                    return -1;
                }
            }
            if (displayHelpMessage) {
                displayHelpMessage();
            }
        }
        return 0;
    }

    private boolean isArgumentFound(String[] kv) {
        boolean found = false;
        for (IConfigAttribute ca : attributes) {
            if (ca.getAttrName().equals(kv[0]) || ca.getConfigKey().equals(kv[0])) {
                configurationValues.put(ca, ca.getAttrParser().apply(kv[1]));
                found = true;
            }
        }
        return found;
    }
    //...
}
```

We also can display a help message in case of error during argument parsing:

```Java
    /**
 * Display an error message if argument unknownAttributeName is unknown.
 *
 * @param unknownAttributeName the unknown argument.
 * @param attributeValue       the value for this unknown argument.
 */
private void displayHelpMessage(String unknownAttributeName,String attributeValue){
        System.out.printf("INFO | Configuration : The argument %s=%s is unknown%n",unknownAttributeName,attributeValue);
        displayHelpMessage();
        }

/**
 * Display CLI argument help message based on values from the {@link ConfigAttribute} enum.
 */
private void displayHelpMessage(){
        System.out.printf("INFO | Configuration : Here is the list of possible arguments:%n--%n");
        Arrays.stream(attributes).forEach(ca->{
        System.out.printf("INFO | Configuration : - %s : %s (default value is %s)%n",
        ca.getAttrName(),
        ca.getAttrDescription(),
        ca.getDefaultValue().toString());
        });
        }
```

And when al least we get some configuration values, we can get it :

```Java
    public Object get(IConfigAttribute ca){
        return configurationValues.get(ca);
        }
```

You can notice that error are output on the `System.err` output stream, and information are out on the `System.out`
output stream.
