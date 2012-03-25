package com.niranjanrao.banshee;

import java.util.ArrayList;
import java.util.List;

import net.pms.dlna.DLNAResource;

public class FolderEntry extends Entry {

	EntryType type;

	public FolderEntry(EntryType type, String name, List<Track> tracks,
			DLNAResource parent) {
		super(tracks, name, parent);
		this.type = type;
	}

	@Override
	public void load() {
		loaded = true;
		ArrayList<Track> filteredTracks = new ArrayList<Track>();
		for (Track track : tracks) {

			if (isTrackAccepted(track, type)) {
				filteredTracks.add(track);

			}
		}
		setTracks(filteredTracks);
		if (filteredTracks.size() == 0) {
			return;
		}
		PredefinedEntry entry;
		// this.children = new ArrayList<DLNAResource>();
		for (EntryType t : Banshee.entryInfoTable.keySet()) {
			entry = new PredefinedEntry(t, filteredTracks, this);
			// entry.load();
			if (entry.getChildren().size() > 0) {
				// children.add(entry);
				addChild(entry);
			}
		}
		for (Track t : filteredTracks) {
			addChild(new TrackEntry(t));
		}
	}

	private boolean isTrackAccepted(Track track, EntryType t) {
		if (parent instanceof FolderEntry) {
			FolderEntry pa = (FolderEntry) parent;

			if (pa != null && !pa.acceptTrack(track, t)) {
				return false;
			}
		}
		return acceptTrack(track, t);
	}

	@Override
	public boolean acceptTrack(Track track, EntryType t) {
		if (t == type) {
			return Banshee.entryInfoTable.get(type).getData(track)
					.contains(this.name);
		}
		return super.acceptTrack(track, t);
	}

	@Override
	public boolean isFolder() {
		return true;
	}

}
