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
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.KeyStroke;

import java.awt.BorderLayout;
import java.awt.Font;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ScriptConsole implements ActionListener {
	private ScriptEngine engine;
	
	public ScriptConsole() {
		this("javascript");
	}
	
	public ScriptConsole(String engineName) {
		final ScriptEngineManager mgr = new ScriptEngineManager();
		setEngine(mgr.getEngineByName(engineName));
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
		
		final JToolBar toolbar = new JToolBar();

		JButton button;
		
		button = new JButton("Run");
		button.setMnemonic(KeyEvent.VK_R);
		button.setActionCommand("run");
		button.addActionListener(this);
		toolbar.add(button);
		button = new JButton("Erase");
		button.setMnemonic(KeyEvent.VK_E);
		button.setActionCommand("erase");
		button.addActionListener(this);
		toolbar.add(button);

		top.add(toolbar, BorderLayout.NORTH);

		output.getAccessibleContext()
			.setAccessibleName("Console Output");
		input.getAccessibleContext()
			.setAccessibleName("Console Input");

		final Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		input.setFont(mono);
		output.setFont(mono);
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
			input.requestFocusInWindow();
		} else if (action == "erase") {
			output.setText("");
			input.requestFocusInWindow();
		}
	}
	
	public String toString() {
		return String.format(
			"%s console (%s)",
			engine.getFactory().getLanguageName(),
			engine.getFactory().getEngineName());
	}
	
	public static void main(String[] args) {
		final ScriptConsole console;
		if (args.length > 0)
			console = new ScriptConsole(args[0]);
		else
			console = new ScriptConsole();
		console.getEngine().put("console", console);
		final JFrame top = console.getWindow();
		top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		top.setVisible(true);
		console.getInputPane().requestFocusInWindow();
	}
}
