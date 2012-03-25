package com.niranjanrao.banshee;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import net.pms.PMS;
import net.pms.dlna.DLNAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.sqlite.SQLiteDataSource;

public class Banshee extends DLNAResource {

	Logger log = LoggerFactory.getLogger(Banshee.class);

	// private DataSource dataSource;
	private List<Track> tracks;
	static Hashtable<EntryType, ITrackInfoExtractor> entryInfoTable = new Hashtable<EntryType, ITrackInfoExtractor>();
	static Hashtable<String, TrackEntry> tracksData = new Hashtable<String, TrackEntry>();
	static {
		for (EntryType t : EntryType.values()) {
			entryInfoTable.put(t, new InfoExtractor(t));
		}
	};

	private DataSource getDataSource() {
		try {
			final SQLiteDataSource ds = new SQLiteDataSource();
			String pluginDir = PMS.getConfiguration().getPluginDirectory();
			File config = new File(pluginDir + "/" + "banshee.cfg");
			String filePath = null;

			if (config.exists()) {
				Properties props = new Properties();
				InputStream in = null;
				try {
					in = new FileInputStream(config);
					props.load(in);
					String type = props.getProperty("type");
					if (type.equals("Banshee")) {
						filePath = props.getProperty("db");
					}
				} finally {
					in.close();
				}

			} else {
				String home = System.getProperty("user.home");
				filePath = String.format("%s/.config/banshee-1/banshee.db",
						home);
			}
			filePath = String.format("jdbc:sqlite:%s", filePath);
			if (null != filePath) {
				ds.setUrl(filePath);
				return ds;
			}
		} catch (Exception e) {
			log.error("Could not create datasource", e);
			return null;
		}
		return null;
	}

	public Banshee() {
		DataSource dataSource = getDataSource();
		if (null == dataSource) {
			log.error("Could not create datasource");
		} else {
			String sql = "SELECT a.Name as Artist, b.Title as Album, t.* FROM CoreTracks t left outer join  CoreArtists a on t.ArtistId = a.ArtistID left outer join CoreAlbums b on t.AlbumId = b.AlbumId "
			// + "where upper(Artist) like '%BENNY DAYAL%'";
			;
			RowMapper<Track> mapper = new RowMapper<Track>() {

				public Track mapRow(ResultSet arg0, int arg1)
						throws SQLException {
					Track track = new Track();
					for (EntryType t : EntryType.values()) {
						track.addEntry(t, arg0.getString(t.name()));
					}
					track.setTrackId(arg0.getString("TrackID"));
					track.setTitle(arg0.getString("Title"));
					track.setUri(arg0.getString("Uri"));
					track.setBitRate(arg0.getInt("BitRate"));
					track.setDuration(arg0.getInt("Duration"));
					track.setMimeType(arg0.getString("MimeType"));
					track.setSize(arg0.getLong("FileSize"));
					return track;
				}
			};

			this.tracks = loadData(dataSource, sql, mapper);

			for (EntryType type : Banshee.entryInfoTable.keySet()) {
				PredefinedEntry entry = new PredefinedEntry(type, tracks, this);
				// children.add(entry);
				addChild(entry);
				// entry.load();
			}
		}
	}

	public <T> List<T> loadData(DataSource dataSource, final String query,
			final RowMapper<T> mapper, final Object... queryParams) {
		final JdbcTemplate select = new JdbcTemplate(dataSource);
		return select.query(query, queryParams, mapper);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Banshee";
	}

	@Override
	public String getDisplayName() {
		return getName();
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
		return true;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	protected String getId() {
		return getName();
	}

	public static DLNAResource getTrackEntry(Track t) {
		TrackEntry e = tracksData.get(t.getTrackId());
		if (e == null) {
			e = new TrackEntry(t);
			tracksData.put(t.getTrackId(), e);
		}
		return e;
	}

}
