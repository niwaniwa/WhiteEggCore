package com.github.niwaniwa.we.core.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
		} catch (FileNotFoundException | ScriptException e) {
			e.printStackTrace();
		}
	}

	public File getPath() {
		return path;
	}

	private void read() throws FileNotFoundException, ScriptException{
		manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		engine.eval(new BufferedReader(new FileReader(path)));
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
