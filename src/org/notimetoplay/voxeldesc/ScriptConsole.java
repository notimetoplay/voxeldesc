// 2014-10-03 Felix Pleșoianu <felixp7@yahoo.com>
// If you are asking what license this software is released under,
// you are asking the wrong question.

package org.notimetoplay.voxeldesc;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ScriptConsole implements ActionListener {
	private ScriptEngine engine;
	
	public ScriptConsole() {
		final ScriptEngineManager mgr = new ScriptEngineManager();
		engine = mgr.getEngineByName("rhino");
		makeGUI();
	}
	
	public ScriptConsole(final ScriptEngine e) {
		setEngine(e);
		makeGUI();
	}
	
	public void setEngine(final ScriptEngine e) {
		if (e == null) throw new NullPointerException(
			"Non-null script engine expected");
		engine = e;
	}
	
	public ScriptEngine getEngine() {
		return engine;
	}
	
	private JFrame top;
	private JTextArea input;
	private JTextArea output;
		
	private void makeGUI() {
		assert engine != null;
		
		top = new JFrame(this.toString());
		top.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		top.setSize(480, 640);
		
		input = new JTextArea();
		output = new JTextArea(
			engine.getFactory().getEngineName() + " is ready.\n");
		output.setEditable(false);
		output.setLineWrap(true);
		
		final JSplitPane split = new JSplitPane(
			JSplitPane.VERTICAL_SPLIT,
			new JScrollPane(output),
			new JScrollPane(input));
		split.setResizeWeight(0.5);
		top.add(split);
		
		final JMenuBar menubar = new JMenuBar();
		final JMenu menu = new JMenu("Console");
		menu.setMnemonic(KeyEvent.VK_C);
		
		JMenuItem item = new JMenuItem("Run script", KeyEvent.VK_R);
		item.setAccelerator(
			KeyStroke.getKeyStroke(
				KeyEvent.VK_R, InputEvent.CTRL_MASK));
		item.setActionCommand("run");
		item.addActionListener(this);
		menu.add(item);
		item = new JMenuItem("Erase", KeyEvent.VK_E);
		item.setAccelerator(
			KeyStroke.getKeyStroke(
				KeyEvent.VK_E, InputEvent.CTRL_MASK));
		item.setActionCommand("erase");
		item.addActionListener(this);
		menu.add(item);

		menubar.add(menu);
		top.setJMenuBar(menubar);
	}
	
	public JFrame getWindow() {
		return top;
	}
	
	public JTextArea getInputPane() {
		return input;
	}
	
	public JTextArea getOutputPane() {
		return output;
	}
	
	public void actionPerformed(ActionEvent ev) {
		final String action = ev.getActionCommand();
		if (action == "run") {
			try {
				Object result = engine.eval(input.getText());
				if (result != null)
					output.append(result.toString());
				else
					output.append("null");
			} catch (ScriptException ex) {
				output.append(ex.getMessage());
			}
			output.append("\n");
		} else if (action == "erase") {
			output.setText("");
		}
	}
	
	public String toString() {
		return String.format(
			"%s console (%s)",
			engine.getFactory().getLanguageName(),
			engine.getFactory().getEngineName());
	}
	
	public static void main(String[] args) {
		final ScriptConsole console = new ScriptConsole();
		console.getEngine().put("console", console);
		final JFrame top = console.getWindow();
		top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		top.setVisible(true);
		console.getInputPane().requestFocusInWindow();
	}
}
