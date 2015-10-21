package com.github.niwaniwa.we.core.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * とりあえず呼び出してみる
 * @author niwaniwa
 *
 */
public class JavaScript {

	private final File path;
	private ScriptEngineManager manager;

	/**
	 * コンストラクター
	 * @param path script階層
	 */
	public JavaScript(File path){
		this.path = path;
		try {
			read();
		} catch (ScriptException | IOException e) {
			e.printStackTrace();
		}
	}

	public JavaScript(BufferedReader buffer) {
		this.path = null;
		try {
			read(buffer);
		} catch (ScriptException | IOException e) {
			e.printStackTrace();
		}
	}

	public File getPath() {
		return path;
	}

	private void read() throws ScriptException, IOException {
		BufferedReader buffer = new BufferedReader(new FileReader(path));
		read(buffer);
	}

	public void read(BufferedReader buffer) throws ScriptException, IOException{
		manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		StringBuilder sb = init();
		String str = buffer.readLine();
		while (str != null) {
			sb.append(str);
			str = buffer.readLine();
		}
		buffer.close();
		engine.eval(sb.toString());
	}

	private StringBuilder init(){
		StringBuilder sb = new StringBuilder();
		sb.append("var events = require('events')");
		sb.append("function White(event){this.event = event; events.EventEmitter.call(this);}");
		sb.append("util.inherits(Button, events.EventEmitter);");
		sb.append("White.prototype.call = function(eventName){this.emit(eventName, this.event)}");
		sb.append("function call(eventName, event){var white = new White(event);white.call(eventName);}");
		return sb;
	}

	/**
	 * JavaScriptの実行
	 * @param function 実行する関数
	 * @param argument 関数に渡す引数
	 * @throws PrivilegedActionException 特権計算によってスローされた例外
	 */
	public void run(final String function, final Object... argument) throws PrivilegedActionException{
		PrivilegedExceptionAction<Void> run = new PrivilegedExceptionAction<Void>(){
			@Override
			public Void run() throws Exception {
				((Invocable)manager).invokeFunction(function, argument);
				return null;
			}
		};
		AccessController.doPrivileged(run, security());
	}

	private AccessControlContext security(){
		Policy policy = new Policy() {};
		Policy.setPolicy(policy);
		System.setSecurityManager(new SecurityManager());

		PermissionCollection permissions = new Permissions();

		CodeSource codeSource = JavaScript.class.getProtectionDomain().getCodeSource();
		ProtectionDomain domain = new ProtectionDomain(codeSource, permissions);
		ProtectionDomain[] domains = new ProtectionDomain[]{domain};
		AccessControlContext context = new AccessControlContext(domains);
		return context;
	}

}
