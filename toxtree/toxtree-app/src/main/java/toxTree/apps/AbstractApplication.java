/*
Ideaconsult Ltd. (C) 2005-2015  
Contact: nina@acad.bg

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

 */
package toxTree.apps;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import toxTree.ui.EditorFactory;
import toxtree.data.DataModule;
import toxtree.ui.editors.SwingEditorFactory;
import ambit2.base.config.Preferences;

public abstract class AbstractApplication {
	// Specify the look and feel to use. Valid values:
	// null (use the default), "Metal", "System", "Motif", "GTK+"
	final static String LOOKANDFEEL = "System";

	protected static ToxtreeOptions options;
	protected static Logger logger = Logger.getLogger(ToxTreeApp.class
			.getName());

	protected DataModule dataModule = null;
	protected JFrame mainFrame;

	public AbstractApplication(String title) {
		super();
		try {
			mainFrame = new JFrame(title);

			mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			mainFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					exit();
				}
			});
			mainFrame.setSize(new Dimension(500, 500));
			ImageIcon icon = getIcon();
			if (icon != null)
				mainFrame.setIconImage(icon.getImage());
			setEditorFactory();
			dataModule = createDataModule();
		} catch (Exception x) {
			logger.severe(x.getMessage());
		}
	}

	public void setEditorFactory() {
		EditorFactory.setInstance(new SwingEditorFactory());
	}

	protected void exit() {
		try {
			Preferences.saveProperties(getClass().getName());
		} catch (Exception x) {
			x.printStackTrace();
		}
		mainFrame.setVisible(false);
		mainFrame.dispose();
		Runtime.getRuntime().runFinalization();
		Runtime.getRuntime().exit(0);
	}

	abstract ToxtreeOptions parseCmdArgs(String[] args) throws Exception;

	protected static void initLookAndFeel() {
		String lookAndFeel = null;

		if (LOOKANDFEEL != null) {
			if (LOOKANDFEEL.equals("Metal")) {
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			} else if (LOOKANDFEEL.equals("System")) {
				lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			} else if (LOOKANDFEEL.equals("Motif")) {
				lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
			} else if (LOOKANDFEEL.equals("GTK+")) { // new in 1.4.2
				lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
			} else {
				logger.warning("Unexpected value of LOOKANDFEEL specified: "
						+ LOOKANDFEEL);
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			}

			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ClassNotFoundException e) {
				logger.warning("Couldn't find class for specified look and feel:"
						+ lookAndFeel);
				logger.warning("Did you include the L&F library in the class path?");
				logger.warning("Using the default look and feel.");
			} catch (UnsupportedLookAndFeelException e) {
				logger.warning("Can't use the specified look and feel ("
						+ lookAndFeel + ") on this platform.");
				logger.warning("Using the default look and feel.");
			} catch (Exception e) {
				logger.warning("Couldn't get specified look and feel ("
						+ lookAndFeel + "), for some reason.");
				logger.warning("Using the default look and feel.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * initLookAndFeel(); JFrame.setDefaultLookAndFeelDecorated(true); new
	 * ToxTreeApp(); Create the GUI and show it. For thread safety, this method
	 * should be invoked from the event-dispatching thread.
	 */
	protected static void createAndShowGUI() {
		initLookAndFeel();
		JFrame.setDefaultLookAndFeelDecorated(true);
		// new AbstractApplication()
	}

	protected abstract DataModule createDataModule();

	protected abstract ImageIcon getIcon();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		options = new ToxtreeOptions();
		try {
			options.parse(args);
			if (options.isNoui()) {

			} else
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						createAndShowGUI();
					}
				});
		} catch (Exception x) {
			logger.severe(x.getMessage());
		}
	}

	protected void centerScreen() {
		Dimension dim = mainFrame.getToolkit().getScreenSize();
		Rectangle abounds = mainFrame.getBounds();
		mainFrame.setLocation((dim.width - abounds.width) / 2,
				(dim.height - abounds.height) / 2);
	}

	// Create an Edit menu to support cut/copy/paste.
	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		dataModule.getActions().createMenu(menuBar);
		return menuBar;
	}

}
