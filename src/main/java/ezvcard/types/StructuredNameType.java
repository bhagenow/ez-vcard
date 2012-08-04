package ezvcard.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ezvcard.VCardSubTypes;
import ezvcard.VCardVersion;
import ezvcard.io.CompatibilityMode;
import ezvcard.util.VCardStringUtils;

/*
 Copyright (c) 2012, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies, 
 either expressed or implied, of the FreeBSD Project.
 */

/**
 * Represents the "N" type.
 * @author Michael Angstadt
 */
public class StructuredNameType extends VCardType {
	public static final String NAME = "N";

	private String family;
	private String given;
	private List<String> additional = new ArrayList<String>();
	private List<String> prefixes = new ArrayList<String>();
	private List<String> suffixes = new ArrayList<String>();

	public StructuredNameType() {
		super(NAME);
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getGiven() {
		return given;
	}

	public void setGiven(String given) {
		this.given = given;
	}

	public List<String> getAdditional() {
		return additional;
	}

	public void addAdditional(String additional) {
		this.additional.add(additional);
	}

	public List<String> getPrefixes() {
		return prefixes;
	}

	public void addPrefix(String prefix) {
		this.prefixes.add(prefix);
	}

	public List<String> getSuffixes() {
		return suffixes;
	}

	public void addSuffix(String suffix) {
		this.suffixes.add(suffix);
	}

	/**
	 * Gets the string that determines how this name should be sorted.
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the sort string (e.g. "Doe,John") or null if not set
	 * @see VCardSubTypes#getSortAs
	 */
	public String getSortAs() {
		return subTypes.getSortAs();
	}

	/**
	 * Sets the string that determines how this name should be sorted.
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param sortAs the sort string (e.g. "Doe,John") or null to remove
	 * @see VCardSubTypes#setSortAs
	 */
	public void setSortAs(String sortAs) {
		subTypes.setSortAs(sortAs);
	}

	/**
	 * Gets the language the name is written in.
	 * @return the language or null if not set
	 * @see VCardSubTypes#getLanguage
	 */
	public String getLanguage() {
		return subTypes.getLanguage();
	}

	/**
	 * Sets the language the name is written in.
	 * @param language the language or null to remove
	 * @see VCardSubTypes#setLanguage
	 */
	public void setLanguage(String language) {
		subTypes.setLanguage(language);
	}

	/**
	 * Gets the ALTID.
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @return the ALTID or null if it doesn't exist
	 * @see VCardSubTypes#getAltId
	 */
	public String getAltId() {
		return subTypes.getAltId();
	}

	/**
	 * Sets the ALTID.
	 * <p>
	 * vCard versions: 4.0
	 * </p>
	 * @param altId the ALTID or null to remove
	 * @see VCardSubTypes#setAltId
	 */
	public void setAltId(String altId) {
		subTypes.setAltId(altId);
	}

	@Override
	protected String doMarshalValue(VCardVersion version, List<String> warnings, CompatibilityMode compatibilityMode) {
		StringBuilder value = new StringBuilder();
		if (family != null) {
			value.append(VCardStringUtils.escapeText(family));
		}

		value.append(';');
		if (given != null) {
			value.append(VCardStringUtils.escapeText(given));
		}

		value.append(';');
		if (!additional.isEmpty()) {
			for (String s : additional) {
				value.append(VCardStringUtils.escapeText(s)).append(',');
			}
			value.deleteCharAt(value.length() - 1);
		}

		value.append(';');
		if (!prefixes.isEmpty()) {
			for (String s : prefixes) {
				value.append(VCardStringUtils.escapeText(s)).append(',');
			}
			value.deleteCharAt(value.length() - 1);
		}

		value.append(';');
		if (!suffixes.isEmpty()) {
			for (String s : suffixes) {
				value.append(VCardStringUtils.escapeText(s)).append(',');
			}
			value.deleteCharAt(value.length() - 1);
		}

		return value.toString();
	}

	@Override
	protected void doUnmarshalValue(String value, VCardVersion version, List<String> warnings, CompatibilityMode compatibilityMode) {
		//preserve empty items and don't unescape escaped characters(e.g. "additional" might have escaped commas)
		String split[] = VCardStringUtils.splitBy(value, ';', false, false);

		int i = 0;

		family = (split.length > i && split[i].length() > 0) ? VCardStringUtils.unescape(split[i]) : null;
		i++;

		given = (split.length > i && split[i].length() > 0) ? VCardStringUtils.unescape(split[i]) : null;
		i++;

		if (split.length > i && split[i].length() > 0) {
			additional = new ArrayList<String>(Arrays.asList(VCardStringUtils.splitBy(split[i], ',', true, true)));
		} else {
			additional = new ArrayList<String>();
		}
		i++;

		if (split.length > i && split[i].length() > 0) {
			prefixes = new ArrayList<String>(Arrays.asList(VCardStringUtils.splitBy(split[i], ',', true, true)));
		} else {
			prefixes = new ArrayList<String>();
		}
		i++;

		if (split.length > i && split[i].length() > 0) {
			suffixes = new ArrayList<String>(Arrays.asList(VCardStringUtils.splitBy(split[i], ',', true, true)));
		} else {
			suffixes = new ArrayList<String>();
		}
		i++;
	}
}