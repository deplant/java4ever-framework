module java4ever.framework {
    requires org.apache.logging.log4j;
    requires java.scripting;
    requires com.google.gson;
    requires static lombok;
    requires java4ever.binding;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.module.paramnames;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    exports tech.deplant.java4ever.framework;
    exports tech.deplant.java4ever.framework.artifact;
    exports tech.deplant.java4ever.framework.contract;
    exports tech.deplant.java4ever.framework.template;
    exports tech.deplant.java4ever.framework.type;
    exports tech.deplant.java4ever.framework.giver;
    opens tech.deplant.java4ever.framework to com.google.gson;
    opens tech.deplant.java4ever.framework.giver to com.google.gson;
    opens tech.deplant.java4ever.framework.artifact to com.google.gson;
    opens tech.deplant.java4ever.framework.template to com.google.gson;
    opens tech.deplant.java4ever.framework.contract to com.google.gson;
}