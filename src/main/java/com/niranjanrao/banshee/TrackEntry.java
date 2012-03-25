package com.niranjanrao.banshee;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.pms.dlna.DLNAMediaInfo;
import net.pms.dlna.DLNAResource;
import net.pms.formats.Format;
import net.pms.formats.FormatFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackEntry extends DLNAResource {

	private Track track;

	public TrackEntry(Track t) {
		this.track = t;
	}

	@Override
	public String getName() {
		return track.getTitle() + ".mp3";
	}

	@Override
	public String getDisplayName() {
		return track.getTitle();
	}

	@Override
	public String getSystemName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long length() {
		return track.getSize();
	}

	Logger log = LoggerFactory.getLogger(TrackEntry.class);

	@Override
	public InputStream getInputStream() throws IOException {
		try {
			URL uri = new URL(track.getUri());
			return uri.openStream();
		} catch (Exception e) {
			log.error("Error while reading file " + track.getUri(), e);
			throw new IOException(e);
		}
	}

	@Override
	public boolean isFolder() {
		return false;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public DLNAMediaInfo getMedia() {
		DLNAMediaInfo info = new DLNAMediaInfo();
		info.setBitrate(track.getBitRate());
		info.setDuration((double) track.getDuration() / 1000);
		info.setSize(track.getSize());
		info.setMediaparsed(true);
		return info;
	}

	@Override
	public Format getExt() {
		return FormatFactory.getAssociatedExtension(track.getMimeType());
	}

	@Override
	public String mimeType() {
		return track.getMimeType().endsWith("mp3") ? "audio/mpeg" : track
				.getMimeType();
	}
}
