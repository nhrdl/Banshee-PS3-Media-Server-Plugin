package com.niranjanrao.banshee;

import java.util.Collection;
import java.util.List;

import net.pms.dlna.DLNAResource;

public class PredefinedEntry extends FolderEntry {

	public PredefinedEntry(EntryType type, List<Track> tracks,
			DLNAResource parent) {
		super(type, type.name(), tracks, parent);
	}

	public void load() {
		loaded = true;
		ITrackInfoExtractor extractor = Banshee.entryInfoTable.get(type);

		Collection<String> list = buildUniqueList(extractor, this.tracks, this);
		FolderEntry f;
		for (String val : list) {
			f = new FolderEntry(type, val, tracks, this);
			// children.add(f);
			addChild(f);
			// f.load();
		}

		// Collections.sort(getChildren(), new Comparator<DLNAResource>() {
		//
		// @Override
		// public int compare(DLNAResource o1, DLNAResource o2) {
		// return o1.getName().toLowerCase()
		// .compareTo(o2.getName().toLowerCase());
		// }
		// });

	}

	public boolean acceptTrack(Track track, EntryType t) {
		return true;
	}
}
