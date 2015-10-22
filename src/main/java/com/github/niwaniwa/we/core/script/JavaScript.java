package com.github.niwaniwa.we.core.script;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.PrivilegedActionException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bukkit.event.Event;

import com.github.niwaniwa.we.core.WhiteEggCore;

/**
 * scriptクラス
 * @author niwaniwa
 *
 */
public class JavaScript {

	private final File path;
	private ScriptEngineManager manager;
	private ScriptEngine engine;
	private String script;

	/**
	 * コンストラクター
	 * @param path script階層
	 */
	public JavaScript(File path){
		this.manager = new ScriptEngineManager();
		this.engine = manager.getEngineByName("js");
		this.path = path;
		try {
			read();
		} catch (ScriptException | IOException | PrivilegedActionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * コンストラクター
	 * @param buffer ばっふぁー
	 */
	public JavaScript(BufferedReader buffer) {
		this.manager = new ScriptEngineManager();
		this.engine = manager.getEngineByName("js");
		this.path = null;
		try {
			read(buffer);
		} catch (ScriptException | IOException | PrivilegedActionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * コンストラクター
	 * @param script script文字列
	 */
	public JavaScript(String script) {
		this.manager = new ScriptEngineManager();
		this.engine = manager.getEngineByName("js");
		this.path = null;
		try {
			engine.eval(script);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ファイル階層(ない場合はnull)
	 * @return
	 */
	public File getPath() {
		return path;
	}

	/**
	 * scriptの読み込み(Fileがnullの場合{@link #read(BufferedReader)が呼ばれます}
	 * @throws ScriptException スクリプトでエラーが発生した場合
	 * @throws IOException 入出力エラーが発生した場合
	 * @throws PrivilegedActionException チェック例外
	 */
	private void read() throws ScriptException, IOException, PrivilegedActionException {
		WhiteEggCore.logger.info("Initializing ScriptEngine... ");
		BufferedReader buffer = new BufferedReader(new FileReader(path));
		read(buffer);
	}

	/**
	 * scriptの読み込み
	 * @param buffer ばっふぁー
	 * @throws ScriptException スクリプトでエラーが発生した場合
	 * @throws IOException 入出力エラーが発生した場合
	 * @throws PrivilegedActionException チェック例外
	 */
	public void read(BufferedReader buffer) throws ScriptException, IOException, PrivilegedActionException{
		WhiteEggCore.logger.info("Initializing ScriptEngine... ");
		StringBuilder sb = init();
		String str = buffer.readLine();
		while (str != null) {
			sb.append(str);
			str = buffer.readLine();
		}
		buffer.close();
		engine.eval(sb.toString());
		this.script = sb.toString();
	}

	/**
	 * 初期化メソッド
	 * @return StringBuilder scriptが記載された文字列
	 * @throws IOException 入出力エラーが発生した場合
	 */
	private StringBuilder init() throws IOException{
		StringBuilder sb = new StringBuilder();
/*		sb.append("var events = require('events');");
		sb.append("var util = require('util');");
		sb.append("function White(){events.EventEmitter.call(this);}");
		sb.append("util.inherits(White, events.EventEmitter);");
		sb.append("White.prototype.call = function(event,eventName){this.emit(eventName, event);}");
		sb.append("var white = new White();");
		sb.append("function call(eventName, event){white.call(event,eventName);}");
*/

		BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(new File("script" + File.separator + "init.js"))));
		sb.append(readFile(buffer));
		return sb;
	}

	/**
	 * ばっふぁーから文字列を取得
	 * @param reader ばっふぁー
	 * @return String scriptの文字列
	 * @throws IOException 入出力エラー
	 */
	private static String readFile(BufferedReader reader) throws IOException{
		StringBuilder sb = new StringBuilder();
		String str = reader.readLine();
		while (str != null) {
			sb.append(str);
			str = reader.readLine();
		}
		return sb.toString();
	}


	/**
	 * JavaScriptの実行
	 * @param function 実行する関数
	 * @param argument 関数に渡す引数
	 * @throws PrivilegedActionException 特権計算によってスローされた例外
	 * @throws ScriptException メソッドの呼出し中にエラーが発生した場合
	 * @throws NoSuchMethodException 指定された名前またはマッチングの引数型を持つメソッドが見つからない場合
	 */
	public void run(final String function, final Object... argument) throws PrivilegedActionException, NoSuchMethodException, ScriptException{
		((Invocable)engine).invokeFunction(function, argument);
	}

	/**
	 * scriptの文字列を返す
	 * @return
	 */
	public String getScript() {
		return script;
	}

	/**
	 * 必須JavaScriptファイルのコピー
	 * @throws IOException
	 */
	public static void copyScript() throws IOException{
		File path = new File(WhiteEggCore.getInstance().getDataFolder(), "script" + File.separator);
		if(!path.exists()){ path.mkdirs(); }
		StringBuilder sb = new StringBuilder();
//		BufferedReader buffer = new BufferedReader(
//				new InputStreamReader(WhiteEggCore.class.getClassLoader().getResourceAsStream("script/white.js"), "UTF-8"));
//		sb.append(readFile(buffer));
		BufferedReader buffer = new BufferedReader(
				new InputStreamReader(WhiteEggCore.class.getClassLoader().getResourceAsStream("script/init.js"), "UTF-8"));
		sb.append(readFile(buffer));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path, "init.js")), "UTF-8"));
		PrintWriter pw = new PrintWriter(bw);
		pw.write(sb.toString());
		pw.close();
	}

	/**
	 * 指定フォルダにあるJavaScriptの取得
	 * @return JavaScript
	 */
	public static JavaScript loadScript(){
		// TODO: Scriptファイルごとにインスタンスを生成するかすべてまとめてしまうか
		File path = new File("script" + File.separator);
		if(!path.exists()){ return null; }
		if(path.listFiles().length == 0){ return null; }
		StringBuilder sb = new StringBuilder();
		for(File script : path.listFiles()){
			if(!script.getName().endsWith(".js")){ continue; }
			try {
				sb.append(
						readFile(new BufferedReader(
								new FileReader(script))));
			} catch (IOException e) {}
		}
		JavaScript script = new JavaScript(sb.toString());
		return script;
	}

	/**
	 * リスナーの呼び出し
	 * @param eventName イベントの名前
	 * @param event イベント
	 */
	public static void callEvent(String eventName, Event event){
		try {
			WhiteEggCore.getInstance().getScript().run("call", eventName, event);
		} catch (PrivilegedActionException | NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
		}
	}

}
