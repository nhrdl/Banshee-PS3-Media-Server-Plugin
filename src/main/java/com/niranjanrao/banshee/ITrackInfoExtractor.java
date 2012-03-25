package com.niranjanrao.banshee;

import java.util.HashSet;

public interface ITrackInfoExtractor {

	public HashSet<String> getData(Track t);
}

class InfoExtractor implements ITrackInfoExtractor {
	private EntryType entryType;

	public InfoExtractor(EntryType t) {
		this.entryType = t;
	}

	public HashSet<String> getData(Track t) {
		return t.getEntries(entryType);

	}
}
