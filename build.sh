javac MP1.java #PrettyPrintingMap.java #MapComparator.java
#jar cfe MP1.jar MP1 MP1.class PrettyPrintingMap.class #MapComparator.class
jar cfm MP1.jar MANIFEST.MF MP1.class
java -jar MP1.jar $1
