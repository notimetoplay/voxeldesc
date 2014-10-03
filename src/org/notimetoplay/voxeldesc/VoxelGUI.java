package org.notimetoplay.voxeldesc;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.script.ScriptException;
import java.util.ArrayList;

public class VoxelGUI implements ActionListener {
	private ScriptConsole console = new ScriptConsole();
	private VoxelScene scene = new VoxelScene();
	private Camera camera = new Camera(0, 0, -256, 768);
	private VoxelCanvas canvas = new VoxelCanvas(scene, camera);
	private ArrayList<String> messages = new ArrayList<String>();

	public ScriptConsole getConsole() { return console; }
	public VoxelScene getScene() { return scene; }
	public Camera getCamera() { return camera; }
	public VoxelCanvas getCanvas() { return canvas; }
	public ArrayList<String> getMessages() { return messages; }

	private JFrame top = new JFrame("VoxelDesc");
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
		try {
			console.getEngine().eval("importPackage(Packages.org.notimetoplay.voxeldesc);");
		} catch (ScriptException e) {
			System.out.println(
				"Error initializing script engine.");
		}
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
	}

	public void actionPerformed(ActionEvent ev) {
		final String action = ev.getActionCommand();
		if (action == "run") {
			try {
				final Object result =
					console.getEngine().eval(
						cmdline.getText());
				if (result != null)
					messages.add(result.toString());
				else
					messages.add("null");
			} catch (ScriptException e) {
				messages.add(
					"Error in evaluation. See console.");
				console.getOutputPane().append(e.getMessage());
			}
			cmdline.setText("");
			while (messages.size() > 100) {
				messages.remove(0);
			}
			canvas.repaint();
		} else if (action == "console") {
			console.getWindow().setVisible(true);
			console.getInputPane().requestFocusInWindow();
		} else if (action == "palette") {
			final Color choice = JColorChooser.showDialog(
				top, "Pick color", canvas.getDrawingColor());
			if (choice != null) {
				canvas.setDrawingColor(choice);
				canvas.repaint();
			}
		}
	}
	
	public static void main(String[] args) {
		VoxelGUI app = new VoxelGUI();
		app.getWindow().setVisible(true);
		app.getCmdLine().requestFocusInWindow();
	}
}
