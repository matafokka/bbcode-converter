package io.github.matafokka.bbcode_converter;

import java.util.ArrayList;

/**
 * Converts BBCode to HTML.
 * 
 * How to use:
 * <pre>
 * 1. Make an instance of this class:
 * 	BBCodeConverter conv = new BBCodeConverter();
 * 
 * 2. Add your custom tags:
 * 	conv.addSimpleTag(...);
 * 	conv.addComplexTag(...);
 * 	conv.addUrlEntity(...);
 * 
 * 3. Use your converter:
 * 	String output = conv.toHtml(input);
 * </pre>
 */
public class BBCodeConverter {
	private ArrayList<String[]> complexTags = new ArrayList<String[]>();
	private ArrayList<String[]> simpleTagsAndHtmlEntities = new ArrayList<String[]>();
	private ArrayList<String[]> urlEntities = new ArrayList<String[]>();

	
	/**
	 * Constructs BBCodeConverter.
	 * <br />
	 * <b>addDefaultTags</b>:
	 * <ul>
	 * <li>If set to true, adds default tags.</li>
	 * 	<li>Otherwise constructs BBCodeConverter with only necessary tags.</li>
	 * </ul>
	 */
	public BBCodeConverter(boolean addDefaultTags) {
		if (addDefaultTags) { this.addDefaultTags(); }

		simpleTagsAndHtmlEntities.add(new String[]{"\"", "&quot;"});
		simpleTagsAndHtmlEntities.add(new String[]{"'", "&apos;"});
		simpleTagsAndHtmlEntities.add(new String[]{"<", "&lt;"});
		simpleTagsAndHtmlEntities.add(new String[]{">", "&gt;"});
		
		urlEntities.add(new String[]{"\"", "%22"});
		urlEntities.add(new String[]{"'", "%27"});
		urlEntities.add(new String[]{";", "%3B"});
		urlEntities.add(new String[]{"<", "%3C"});
		urlEntities.add(new String[]{">", "%3E"});
	}
	
	/**
	 * Constructs BBCodeConverter with all default tags.
	 */
	public BBCodeConverter() { this(true); }
	
	/**
	 * 
	 * Converts BBCode in a string to HTML tags. Output the results WITHOUT escaping!
	 * @param source - string to convert
	 * @return escaped string containing HTML
	 */
	public String toHtml(String source) {
		StringBuffer html = new StringBuffer(source);
		
		int indexOfCurrentTag = -1;
		Boolean part1Found = false;
		Boolean part2Found = false;
		
		for (int i = 0; i < html.length(); i++) {
			// Look for complex tag if none was found
			if (indexOfCurrentTag == -1) {
				for (int j = 0; j < complexTags.size(); j++) {
					String[] tag = complexTags.get(j);
					String toReplace = tag[0];
					int l = i + toReplace.length();
					if (l < html.length() && html.subSequence(i, l).equals(toReplace)) {
						String replacement = tag[3];
						html = html.replace(i, l, replacement);
						i += replacement.length() - 1;
						part1Found = true;
						indexOfCurrentTag = j;
						break;
					}
				}
				// If tag has been found, restart loop
				if (part1Found) { continue; }
			}
			// Look for part 2 of complex tag
			else if(part1Found && !part2Found) {
				String[] tag = complexTags.get(indexOfCurrentTag);
				String toReplace = tag[1];
				int l = i + toReplace.length();
				
				// If part 2 found, replace it
				if (l - 1 < html.length() && html.subSequence(i, l).equals(toReplace)) {
					String replacement = tag[4];
					html = html.replace(i, l, replacement);
					i += replacement.length() - 1;
					// If current tag has only 2 parts, stop lookup
					if (tag[2].equals("")) {
						part1Found = false;
						indexOfCurrentTag = -1;
					}
					// Proceed lookup otherwise
					else {
						part2Found = true;
					}
					continue;
				}
			}
			// Look for part 3
			else if (part1Found && part2Found) {
				String[] tag = complexTags.get(indexOfCurrentTag);
				String toReplace = tag[2];
				int l = i + toReplace.length();
				
				// If part 3 found, replace it
				if (l - 1 < html.length() && html.subSequence(i, l).equals(toReplace)) {
					String replacement = tag[5];
					html = html.replace(i, l, replacement);
					i += replacement.length() - 1;
					part1Found = part2Found = false;
					indexOfCurrentTag = -1;
					continue;
				}
			}
			
			// Replace URL entities if parsing URL part of complex tag
			if (part1Found && !part2Found) {
				Boolean doContinue = false;
				for (String[] entity: urlEntities) {
					int l = i + 1;
					if (l < html.length() && entity[0].charAt(0) == html.charAt(i)) {
						html.replace(i, l, entity[1]);
						i += 2;
						doContinue = true;
						break;
					}
				}
				if (doContinue) { continue; }
			}
			// Replace simple tags otherwise
			for (String[] tag: simpleTagsAndHtmlEntities) {
				String toReplace = tag[0];
				int l = i + toReplace.length();
				if (l - 1 < html.length() && html.subSequence(i, l).equals(toReplace)) {
					String replacement = tag[1];
					html.replace(i, l, replacement);
					i += replacement.length() - 1;
					break;
				}
			}
			
		}
		
		// Close open complex tags
		if (part1Found) {
			String[] tag = complexTags.get(indexOfCurrentTag);
			if (!part2Found) {
				html.append(tag[4]);
			}
			html.append(tag[5]);
		}
		
		return html.toString();
	}
	
	// Misc methods
	
	private void addDefaultTags() {
		complexTags.add(new String[] {
			"[URL=\"http://", "\"]", "[/URL]",
			"<a href=\"http://", "\">", "</a>"});
		complexTags.add(new String[] {
			"[URL=\"https://", "\"]", "[/URL]",
			"<a href=\"https://", "\">", "</a>"});
		complexTags.add(new String[] {
			"[COLOR=\"", "\"]", "[/COLOR]",
			"<span style=\"color: ", ";\">", "</span>"});
		
		simpleTagsAndHtmlEntities.add(new String[] {"[B]", "<b>"});
		simpleTagsAndHtmlEntities.add(new String[] {"[/B]", "</b>"});
		simpleTagsAndHtmlEntities.add(new String[] {"[I]", "<i>"});
		simpleTagsAndHtmlEntities.add(new String[] {"[/I]", "</i>"});
		simpleTagsAndHtmlEntities.add(new String[] {"[U]", "<u>"});
		simpleTagsAndHtmlEntities.add(new String[] {"[/U]", "</u>"});
		simpleTagsAndHtmlEntities.add(new String[] {"[S]", "<s>"});
		simpleTagsAndHtmlEntities.add(new String[] {"[/S]", "</s>"});
	}
	
	/**
	 * Adds a simple tag, i.e. [UL] that can be replaced with &lt;ul&gt;
	 * @param tag - BBCode tag to replace
	 * @param replacement - replacement for the tag
	 */
	public void addSimpleTag(String tag, String replacement) {
		simpleTagsAndHtmlEntities.add(new String[]{tag, replacement});
	}
	
	/**
	 * Adds a complex tag. A few rules applies to the complex tags:
	 * <ol>
	 * <li>Complex tags can either contain 2 or 3 parts. If your tag contains only of 2 parts, make part3 and replacement3 an empty strings.</li>
	 * <li>Text between parts 1 and 2 will be interpreted as URL.</li>
	 * </ol>
	 * 
	 * Example uses:
	 * <ul>
	 * <li>A complex tag containing 2 parts:</li>
	 * <pre>
	 * This code:	addComplexTag("[IMG]", "&lt;img src=\"", "[/IMG]", "\"&gt;", "", "")
	 * will replace:	[IMG]https://www.example.com/image.png[/IMG]
	 * with:		&lt;img src="https://www.example.com/image.png"&gt;
	 * </pre>
	 * 
	 * <li>A complex tag containing 3 parts</li>
	 * <pre>
	 * This code:	addComplexTag("[URL=https://", "&lt;a href=\"https://", "]", "\"&gt;", "[/URL]", "&lt;/a&gt;")
	 * will replace:	[URL=https://www.example.com]Example[/URL]
	 * with:		&lt;a href="https://www.example.com/image.png"&gt;Example&lt;/a&gt;;
	 * </pre>
	 * </ul>
	 */
	public void addComplexTag(String part1, String replacement1, String part2, String replacement2, String part3, String replacement3) {
		complexTags.add(new String[] {
				part1, part2, part3,
				replacement1, replacement2, replacement3
				});
	}
	
	/**
	 * Adds a URL entity, i.e. "(" that can be replaced with "%28"
	 * @param entity - entity to replace
	 * @param replacement - replacement for the entity
	 */
	public void addUrlEntity(String entity, String replacement) {
		urlEntities.add(new String[] {entity, replacement});
	}
	
	/**
	 * Getter for simple tags and HTML entities. Though you likely wouldn't need it, there it is.
	 * @return ArrayList containing String[2] arrays in a following format: {tag, replacement}
	 */
	public ArrayList<String[]> getSimpleTags() { return simpleTagsAndHtmlEntities; }
	
	/**
	 * Getter for complex tags. Though you likely wouldn't need it, there it is.
	 * @return ArrayList containing String[6] arrays in a following format: {part1, part2, part3, replacement1, replacement2, replacement3}
	 */
	public ArrayList<String[]> getComplexTags() { return complexTags; }
	
	/**
	 * Getter for URL entities. Though you likely wouldn't need it, there it is.
	 * @return ArrayList containing String[2] arrays in a following format: {entity, replacement}
	 */
	public ArrayList<String[]> getUrlEntities() { return urlEntities; }
	
}