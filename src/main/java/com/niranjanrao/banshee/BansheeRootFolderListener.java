package com.niranjanrao.banshee;

import javax.swing.JComponent;

import net.pms.dlna.DLNAResource;
import net.pms.external.AdditionalFolderAtRoot;

public class BansheeRootFolderListener implements AdditionalFolderAtRoot {

	public JComponent config() {
		// TODO Auto-generated method stub
		return null;
	}

	public String name() {
		// TODO Auto-generated method stub
		return "Banshee";
	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public DLNAResource getChild() {
		// TODO Auto-generated method stub
		return new Banshee();
	}

}
