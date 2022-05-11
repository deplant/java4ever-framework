module test {
    requires static lombok;
    requires jdk.incubator.foreign;
    requires java4ever.binding;
    requires java4ever.framework;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.module.paramnames;
    requires org.junit.jupiter.api;
    requires org.apache.logging.log4j;
    exports tech.deplant.java4ever.framework.test;
    opens tech.deplant.java4ever.framework.test to com.fasterxml.jackson.databind;
}