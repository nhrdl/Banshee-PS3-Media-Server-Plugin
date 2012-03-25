package com.niranjanrao.banshee;

import java.util.HashSet;
import java.util.Hashtable;

public class Track {

	Hashtable<EntryType, MultiValueEntry> valueTable;
	private String trackId;
	private String title;
	private String uri;
	private int bitRate;
	private int duration;
	private String mimeType;
	private long size;

	public Track() {
		valueTable = new Hashtable<EntryType, Track.MultiValueEntry>();
		for (EntryType t : EntryType.values()) {
			valueTable.put(t, new MultiValueEntry());
		}
	}

	public HashSet<String> getEntries(EntryType t) {
		return valueTable.get(t).entries;
	}

	public Track addEntry(EntryType t, String value) {
		valueTable.get(t).addEntry(value);
		return this;
	}

	class MultiValueEntry {
		HashSet<String> entries;
		Hashtable<String, String> entryTable;

		public MultiValueEntry() {
			entries = new HashSet<String>();
			entryTable = new Hashtable<String, String>();
		}

		public void addEntry(String artist) {
			if (null != artist) {
				String[] entries = artist.split(",|;|&|/");
				String entry;
				for (String val : entries) {
					entry = val.trim();
					if (entry.length() == 0)
						continue;
					if (false == entryTable.containsKey(entry.toLowerCase())) {
						entryTable.put(entry.toLowerCase(), entry);
						this.entries.add(entry);
					}
				}
			}
		}

		public boolean hasValue(String val) {
			if (val != null) {
				return entryTable.containsKey(val.toLowerCase());
			}
			return false;
		}
	}

	public void setTrackId(String string) {
		this.trackId = string;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTitle(String string) {
		this.title = string;
	}

	public String getTitle() {
		return title;
	}

	public void setUri(String string) {
		this.uri = string;
	}

	public String getUri() {
		return uri;
	}

	public void setBitRate(int long1) {
		this.bitRate = long1;
	}

	public int getBitRate() {
		return bitRate;
	}

	public void setDuration(int int1) {
		this.duration = int1;
	}

	public int getDuration() {
		return duration;
	}

	public void setMimeType(String string) {
		if (string != null) {
			this.mimeType = string.replace("/", ".");
		} else {
			mimeType = null;
		}
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setSize(long l) {
		this.size = l;
	}

	public long getSize() {
		return this.size;
	}
}
