package org.notimetoplay.voxeldesc;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.text.html.HTMLDocument;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.script.ScriptException;
import java.util.ArrayList;

public class VoxelGUI
		implements ActionListener, KeyListener, HyperlinkListener {
	private ScriptConsole console = new ScriptConsole();
	private VoxelScene scene = new VoxelScene();
	private Camera camera = new Camera(0, 75, -150, 768);
	private VoxelCanvas canvas = new VoxelCanvas(scene, camera);
	private JFileChooser filedlg = new JFileChooser();
	private JFrame helpdlg = new JFrame("VoxelDesc help");
	
	private JEditorPane help = new JEditorPane();
	private FileNameExtensionFilter filterJS =
		new FileNameExtensionFilter("Javascript file (*.js)", "js");
	private FileNameExtensionFilter filterVXL =
		new FileNameExtensionFilter("Voxel file (*.vxl)", "vxl");

	private ArrayList<String> messages = new ArrayList<String>();
	private ArrayList<String> history = new ArrayList<String>();
	private int histPos = 0;
	
	private File saveFile = null;

	public ScriptConsole getConsole() { return console; }
	public VoxelScene getScene() { return scene; }
	public Camera getCamera() { return camera; }
	public VoxelCanvas getCanvas() { return canvas; }
	public JFileChooser getFileDlg() { return filedlg; }
	public JFrame getHelpDlg() { return helpdlg; }
	
	public ArrayList<String> getMessages() { return messages; }
	public ArrayList<String> getHistory() { return history; }
	
	public File getSaveFile() { return saveFile; }

	private JFrame top = new JFrame(this.toString());
	private JTextField cmdline = new JTextField();
	private JToolBar toolbar = new JToolBar();
	
	public JFrame getWindow() { return top; }
	public JTextField getCmdLine() { return cmdline; }
	public JToolBar getToolBar() { return toolbar; }
	
	public VoxelGUI() {
		console.getEngine().put("console", console);
		console.getEngine().put("scene", scene);
		console.getEngine().put("camera", camera);
		console.getEngine().put("canvas", canvas);
		console.getEngine().put("app", this);
		
		console.getEngine().put("black", Color.BLACK);
		console.getEngine().put("blue", Color.BLUE);
		console.getEngine().put("green", Color.GREEN);
		console.getEngine().put("red", Color.RED);
		console.getEngine().put("white", Color.WHITE);
		
		console.getEngine().put("cyan", Color.CYAN);
		console.getEngine().put("dgray", Color.DARK_GRAY);
		console.getEngine().put("gray", Color.GRAY);
		console.getEngine().put("lgray", Color.LIGHT_GRAY);
		console.getEngine().put("magenta", Color.MAGENTA);
		console.getEngine().put("orange", Color.ORANGE);
		console.getEngine().put("pink", Color.PINK);
		console.getEngine().put("yellow", Color.YELLOW);

		canvas.setMessages(messages);
		
		top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		top.setSize(1024, 768);
		top.add(canvas);
		top.add(cmdline, BorderLayout.SOUTH);
		top.add(toolbar, BorderLayout.NORTH);
		
		JButton button;
		
		button = new JButton("New");
		button.setMnemonic(KeyEvent.VK_N);
		button.setActionCommand("new");
		button.addActionListener(this);
		toolbar.add(button);
		button = new JButton("Open");
		button.setMnemonic(KeyEvent.VK_O);
		button.setActionCommand("open");
		button.addActionListener(this);
		toolbar.add(button);
		button = new JButton("Save");
		button.setMnemonic(KeyEvent.VK_S);
		button.setActionCommand("save");
		button.addActionListener(this);
		toolbar.add(button);

		toolbar.addSeparator();

		button = new JButton("Save As");
		button.setMnemonic(KeyEvent.VK_A);
		button.setActionCommand("save-as");
		button.addActionListener(this);
		toolbar.add(button);
		button = new JButton("Load Script");
		button.setMnemonic(KeyEvent.VK_L);
		button.setActionCommand("load-script");
		button.addActionListener(this);
		toolbar.add(button);

		toolbar.addSeparator();

		button = new JButton("Palette");
		button.setMnemonic(KeyEvent.VK_P);
		button.setToolTipText("Show the color selector");
		button.setActionCommand("palette");
		button.addActionListener(this);
		toolbar.add(button);
		button = new JButton("Console");
		button.setMnemonic(KeyEvent.VK_C);
		button.setToolTipText("Show the Javascript console");
		button.setActionCommand("console");
		button.addActionListener(this);
		toolbar.add(button);
		button = new JButton("Help");
		button.setMnemonic(KeyEvent.VK_H);
		button.setActionCommand("help");
		button.addActionListener(this);
		toolbar.add(button);
		
		cmdline.setActionCommand("run");
		cmdline.addActionListener(this);
		cmdline.addKeyListener(this);
		
		help.setEditable(false);
		help.addHyperlinkListener(this);
		try {
			help.setPage(
				VoxelGUI.class.getResource(
					"/docs/index.html"));
		} catch (IOException e) {
			help.setText("Error loading documentation index.");
		}
		helpdlg.add(new JScrollPane(help));
		helpdlg.setSize(800, 600);
	}

	public void actionPerformed(ActionEvent ev) {
		final String action = ev.getActionCommand();
		if (action == "run") {
			handleCommandLine();
		} else if (action == "console") {
			console.getWindow().setVisible(true);
			console.getInputPane().requestFocusInWindow();
		} else if (action == "palette") {
			final Color choice = JColorChooser.showDialog(
				top, "Pick color", scene.getFillColor());
			if (choice != null) {
				scene.setFillColor(choice);
				canvas.repaint();
			}
		} else if (action == "help") {
			helpdlg.setVisible(true);
		} else if (action == "new") {
			final int answer = JOptionPane.showConfirmDialog(
				top,
				"Discard this scene and start anew?",
				"New scene?",
				JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				scene.getVoxels().clear();
				scene.setFillColor(Color.BLACK);
				saveFile = null;
				top.setTitle(this.toString());
				messages.clear();
				camera.moveTo(0, 75, -150);
				canvas.repaint();
			}
				
		} else if (action == "open") {
			handleOpenFile();
		} else if (action == "save") {
			if (saveFile != null) {
				saveScene();
			} else if (askForSaveFile()) {
				saveScene();
			}
		} else if (action == "save-as") {
			if (askForSaveFile()) {
				saveScene();
			}
		} else if (action == "load-script") {
			handleLoadScript();
		}
	}

	public void handleCommandLine() {
		final String cmd = cmdline.getText();
		messages.add("> " + cmd);
		try {
			final Object result =
				console.getEngine().eval(cmd);
			if (result != null)
				messages.add(result.toString());
			else
				messages.add("null");
		} catch (ScriptException e) {
			messages.add("Error in command line. See console.");
			console.getOutputPane().append(e.getMessage());
		}
		history.add(cmd);
		histPos = 0;
		cmdline.setText("");
		while (messages.size() > 100) {
			messages.remove(0);
		}
		while (history.size() > 100) {
			history.remove(0);
		}
		canvas.repaint();
	}
	
	public void handleLoadScript() {
		filedlg.removeChoosableFileFilter(filterVXL);
		filedlg.addChoosableFileFilter(filterJS);
		filedlg.setFileFilter(filterJS);
		final int answer = filedlg.showOpenDialog(top);
		if (answer != JFileChooser.APPROVE_OPTION)
			return;

		final File file = filedlg.getSelectedFile();
		final FileReader reader;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			messages.add("File not found: " + file);
			return;
		}
		
		messages.add("Loading script " + file);
		try {
			final Object result =
				console.getEngine().eval(reader);
			if (result != null)
				messages.add(result.toString());
			else
				messages.add("null");
			reader.close();
		} catch (ScriptException e) {
			messages.add("Error in script. See console.");
			console.getOutputPane().append(e.getMessage());
		} catch (IOException e) {
			messages.add("Error reading file. See console.");
			console.getOutputPane().append(e.getMessage());
		}
		canvas.repaint();
	}
	
	public void handleOpenFile() {
		filedlg.removeChoosableFileFilter(filterJS);
		filedlg.addChoosableFileFilter(filterVXL);
		filedlg.setFileFilter(filterVXL);
		final int answer = filedlg.showOpenDialog(top);
		if (answer != JFileChooser.APPROVE_OPTION)
			return;

		final File file = filedlg.getSelectedFile();
		final FileReader reader;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			messages.add("File not found: " + file);
			return;
		}
		
		scene.getVoxels().clear();
		try {
			scene.deserialize(reader);
			canvas.repaint();
			messages.add("Scene loaded from " + file);
			saveFile = file;
			top.setTitle(this.toString());
			reader.close();
		} catch (IOException e) {
			messages.add("Error loading file. See console.");
			console.getOutputPane().append(e.getMessage());
		}
	}
	
	public boolean askForSaveFile() {
		filedlg.removeChoosableFileFilter(filterJS);
		filedlg.addChoosableFileFilter(filterVXL);
		filedlg.setFileFilter(filterVXL);
		if (saveFile != null)
			filedlg.setSelectedFile(saveFile);
		final int answer = filedlg.showSaveDialog(top);
		if (answer != JFileChooser.APPROVE_OPTION) {
			return false;
		} else {
			saveFile = filedlg.getSelectedFile();
			top.setTitle(this.toString());
			return true;
		}
	}
	
	public void saveScene() {
		assert saveFile != null;
		try {
			final FileWriter out = new FileWriter(saveFile);
			scene.serialize(out);
			out.close();
			messages.add("Scene saved to " + saveFile);
		} catch (IOException e) {
			messages.add("Error saving file. See console.");
			console.getOutputPane().append(e.getMessage());
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (histPos < history.size()) {
				histPos ++;
				cmdline.setText(
					history.get(
						history.size()
							- histPos));
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (histPos > 0) {
				histPos --;
				if (histPos == 0) {
					cmdline.setText("");
				} else {
					cmdline.setText(
						history.get(
							history.size()
								- histPos));
				}
			}
		}
	}

	// Courtesy of the official documentation at
	// http://docs.oracle.com/javase/7/docs/api/javax/swing/JEditorPane.html
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
			return;

		JEditorPane pane = (JEditorPane) e.getSource();
		if (e instanceof HTMLFrameHyperlinkEvent) {
			HTMLFrameHyperlinkEvent evt =
				(HTMLFrameHyperlinkEvent) e;
			HTMLDocument doc = (HTMLDocument) help.getDocument();
			doc.processHTMLFrameHyperlinkEvent(evt);
		} else {
			try {
				help.setPage(e.getURL());
			} catch (IOException e2) {
				help.setText("Error loading " + e.getURL());
			}
		}
	}
         	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	public String toString() {
		if (saveFile != null)
			return saveFile + " | VoxelDesc";
		else
			return "Untitled scene | VoxelDesc";
	}
	
	public static void main(String[] args) {
		VoxelGUI app = new VoxelGUI();
		app.getWindow().setVisible(true);
		app.getCmdLine().requestFocusInWindow();
	}
}
