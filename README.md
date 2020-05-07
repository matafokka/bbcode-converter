# BBCode Converter
A Java library that provides a class to effectively convert custom BBCode to HTML. Also prevents XSS attacks.

Grab it from maven:
```xml
<dependency>
  <groupId>io.github.matafokka</groupId>
  <artifactId>bbcode-converter</artifactId>
  <version>1.0</version>
</dependency>
```

Or for your building system from code [here](https://search.maven.org/artifact/io.github.matafokka/bbcode-converter/1.0/jar).

You can find the documentation [here](https://javadoc.io/doc/io.github.matafokka/bbcode-converter/latest/io/github/matafokka/bbcode_converter/BBCodeConverter.html).

# Intro
First of all, you've got 3 types of so-called "tags":
1. **Simple tags**. These tags contains only one so-called part (string), i.e. "\[B\]", that will be replaced with another string, i.e. "\<b\>". These tags shouldn't be closed. Also, this tags contains HTML entities.
1. **Complex tags**. These tags can contain 2 or 3 parts. Check [addComplexTag()](https://javadoc.io/static/io.github.matafokka/bbcode-converter/1.0/io/github/matafokka/bbcode_converter/BBCodeConverter.html#addComplexTag(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)) for more information.
1. **URL entites**. Same as simple tags, but these will be used when parsing text between 1st and 2nd parts of a complex tag.

Next, you need to know these rules:
1. All tags are case-dependent. Don't add all the cases by yourself! Apply a code-style to your project, i.e. all tags should be either in a lower case or in a upper case.
1. BBCodeConverter will escape all necessary HTML and URL entities. You don't need to add it by yourself.
1. Put the output of toHtml() into `<object></object>` block. This is needed to deal with bad markup.

**List of included tags:**
<details>
  <summary>Click to expand</summary>

**Tags added anyways**

**Simple tags:**
```
"	=>	&quot;
'	=>	&apos;
<	=>	&lt;
>	=>	&gt;
```

**URL entities:**
```
"	=>	%22
'	=>	%27
;	=>	%3B
<	=>	%3C
>	=>	%3E
```

**Tags added when addDefaultTags = true**

**Simple tags:**
```
[B]	=>	<b>
[/B]	=>	</b>
[I]	=>	<i>
[/I]	=>	</i>
[U]	=>	<u>
[/U]	=>	</u>
[S]	=>	<s>
[/S]	=>	</s>
```

**Complex tags:**
```
[URL="http://		"]		[/URL]
	V		V		  V
<a href="http://	">"		</a>

[URL="https://		"]		[/URL]
	V		V		  V
<a href="https://	">"		</a>

[COLOR="		"]		[/COLOR]
	V		V		  V
<span style="color:	;">		</span>
```
</details>

# How to use
0. Import the project :D
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
