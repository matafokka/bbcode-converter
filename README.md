# BBCode Converter
Provides a class to effectively convert custom BBCode to HTML

# How to use
0. Download and import the project :D
```java
import io.github.matafokka.bbcode_converter.BBCodeConverter
```

1. Make an instance of this class:
```java
// With all default tags included
BBCodeConverter conv = new BBCodeConverter();
BBCodeConverter conv = new BBCodeConverter(true);

// Only with necessary tags (HTML and URL-entities):
BBCodeConverter conv = new BBCodeConverter(false);
````

2. Add your custom tags:
```java
conv.addSimpleTag(...);
conv.addComplexTag(...);
conv.addUrlEntity(...);
```

3. Use your converter:
```java
String output = conv.toHtml(input);
```

4. Put the output to your page or whatever. **DO NOT** escape the output!
