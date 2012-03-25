package com.niranjanrao.banshee;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import net.pms.dlna.DLNAResource;

public class Entry extends DLNAResource {

	protected List<Track> tracks;
	protected String name;
	protected DLNAResource parent;
	// protected ArrayList<DLNAResource> children;
	protected boolean loaded;

	public Entry(List<Track> tracks, String name, DLNAResource parent) {
		this.tracks = tracks;
		this.name = name;
		this.parent = parent;
		// children = new ArrayList<DLNAResource>();
		loaded = false;
	}

	public String getName() {
		return name;
	}

	List<Track> getTracks() {
		return tracks;
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	public List<DLNAResource> getChildren() {

		List<DLNAResource> lst = super.getChildren();
		if (!loaded) {
			load();
		}
		return lst;
	}

	public void load() {

	}

	public boolean acceptTrack(Track track, EntryType t) {
		return true;
	}

	void setTracks(ArrayList<Track> filteredTracks) {
		tracks = filteredTracks;
	}

	public static Collection<String> buildUniqueList(
			ITrackInfoExtractor extractor, List<Track> tracks, FolderEntry item) {

		HashSet<String> list = new HashSet<String>();

		for (Track t : tracks) {
			for (String val : extractor.getData(t)) {
				if (false == parentHasItem(val, item)) {
					list.add(val);
				}
			}
		}
		return list;
	}

	private static boolean parentHasItem(String val, FolderEntry item) {
		if (item.parent instanceof FolderEntry) {
			FolderEntry parent = (FolderEntry) item.parent;
			while (parent != null) {

				// boolean b1 = parent.type == item.type, b2 = parent.getName()
				// .equals(val);
				// parent.type, parent.name, b1, b2);
				if (parent.type == item.type && parent.getName().equals(val)) {
					return true;
				}
				if (parent.parent instanceof FolderEntry) {
					parent = (FolderEntry) parent.parent;
				} else {
					parent = null;
				}
			}
		}
		return false;
	}

	@Override
	public String getSystemName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFolder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected String getId() {
		return super.getId();
	}
	// @Override
	// protected String getId() {
	// String pID = "";
	// if (parent != null) {
	// if (parent instanceof FolderEntry) {
	// FolderEntry pa = (FolderEntry) parent;
	// pID = pa.getId() + "$";
	// }
	// if (parent instanceof Banshee) {
	// Banshee pa = (Banshee) parent;
	// pID = "0$" + pa.getId() + "$";
	// }
	// }
	// return pID + getName();
	// }
}
