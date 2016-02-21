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
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.ProtectionDomain;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bukkit.event.Event;

import com.github.niwaniwa.we.core.WhiteEggCore;

/**
 * とりあえず呼び出してみる
 *
 * @author niwaniwa
 */
public class JavaScript {

    private final File path;
    private ScriptEngineManager manager;
    private ScriptEngine engine;
    private String script;

    /**
     * コンストラクター
     *
     * @param path script階層
     */
    public JavaScript(File path) {
        this.manager = new ScriptEngineManager();
        this.engine = manager.getEngineByName("js");
        this.path = path;
        try {
            read();
        } catch (ScriptException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * コンストラクター
     *
     * @param buffer BufferedReader
     */
    public JavaScript(BufferedReader buffer) {
        this.manager = new ScriptEngineManager();
        this.engine = manager.getEngineByName("js");
        this.path = null;
        try {
            read(buffer);
        } catch (ScriptException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * コンストラクター
     *
     * @param script javascriptの文字列
     */
    public JavaScript(String script) {
        this.manager = new ScriptEngineManager();
        this.engine = manager.getEngineByName("js");
        this.path = null;
        try {
            eval(init().append(script).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * script階層を取得
     *
     * @return File パス
     */
    public File getPath() {
        return path;
    }

    /**
     * 読み込み
     *
     * @throws ScriptException スクリプトでエラーが発生した場合
     * @throws IOException     入出力エラー
     */
    private void read() throws ScriptException, IOException {
        WhiteEggCore.logger.info("Initializing ScriptEngine... ");
        BufferedReader buffer = new BufferedReader(new FileReader(path));
        read(buffer);
    }

    /**
     * 読み込み
     *
     * @param buffer BufferedReader
     * @throws ScriptException スクリプトでエラーが発生した場合
     * @throws IOException     入出力エラー
     */
    public void read(BufferedReader buffer) throws ScriptException, IOException {
        WhiteEggCore.logger.info("Initializing ScriptEngine... ");
        StringBuilder sb = init();
        String str = buffer.readLine();
        while (str != null) {
            sb.append(str);
            str = buffer.readLine();
        }
        buffer.close();
        eval(sb.toString());
        this.script = sb.toString();
    }

    /**
     * scriptの実行
     *
     * @param str 文字列
     */
    private void eval(String str) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            @Override
            public Void run() {
                try {
                    engine.eval(str);
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, security());
    }

    private AccessControlContext security() {
        PermissionCollection permissions = new Permissions();
//		permissions.add(permission);
        CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();
        ProtectionDomain domain = new ProtectionDomain(codeSource, permissions);
        ProtectionDomain[] domains = new ProtectionDomain[]{domain};
        AccessControlContext context = new AccessControlContext(domains);
        return context;
    }

    /**
     * 必要なScriptファイルを取得する
     *
     * @return scriptの文字
     * @throws IOException 入出力エラー
     */
    private StringBuilder init() throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(
                new File(WhiteEggCore.getInstance().getDataFolder(), "script" + File.separator + "init.js"))));
        sb.append(readFile(buffer));
        return sb;
    }

    /**
     * バッファーから文字列を読み込む
     *
     * @param reader バッファー
     * @return 読み込まれた文字列
     * @throws IOException 入出力エラー
     */
    private static String readFile(BufferedReader reader) throws IOException {
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
     *
     * @param function 実行する関数
     * @param argument 関数に渡す引数
     * @throws PrivilegedActionException 特権計算によってスローされた例外
     * @throws ScriptException           メソッドの呼出し中にエラーが発生した場合
     * @throws NoSuchMethodException     指定された名前またはマッチングの引数型を持つメソッドが見つからない場合
     */
    public void run(final String function, final Object... argument) throws PrivilegedActionException, NoSuchMethodException, ScriptException {
        ((Invocable) engine).invokeFunction(function, argument);
    }

    /**
     * scriptの文字列の取得
     *
     * @return 文字列
     */
    public String getScript() {
        return script;
    }

    /**
     * scriptのコピー
     *
     * @throws IOException 入出力エラー
     */
    public static void copyScript() throws IOException {
        File path = new File(WhiteEggCore.getInstance().getDataFolder(), "script" + File.separator);
        if (!path.exists()) {
            path.mkdirs();
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader buffer = new BufferedReader(
                new InputStreamReader(WhiteEggCore.class.getClassLoader().getResourceAsStream("script/white.js"), "UTF-8"));
        sb.append(readFile(buffer));
        buffer = new BufferedReader(
                new InputStreamReader(WhiteEggCore.class.getClassLoader().getResourceAsStream("script/init.js"), "UTF-8"));
        sb.append(readFile(buffer));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path, "init.js")), "UTF-8"));
        PrintWriter pw = new PrintWriter(bw);
        pw.write(sb.toString());
        pw.close();
    }

    /**
     * JavaScriptの読み込み
     *
     * @return JavaScript インスタンス
     */
    public static JavaScript loadScript() {
        if (!WhiteEggCore.getConf().getConfig().getBoolean("setting.script.enableScript", false)) {
            return null;
        }
        // TODO: Scriptファイルごとにインスタンスを生成するかすべてまとめてしまうか
        File path = new File(WhiteEggCore.getConf().getConfig().getString("setting.script.path", "script" + File.separator));
        if (!path.exists()) {
            return null;
        }
        if (path.listFiles().length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (File script : path.listFiles()) {
            if (!script.getName().endsWith(".js")) {
                continue;
            }
            try {
                sb.append(
                        readFile(new BufferedReader(
                                new FileReader(script))));
            } catch (IOException e) {
            }
        }
        JavaScript script = new JavaScript(sb.toString());
        return script;
    }

    /**
     * イベントのコール
     *
     * @param eventName 名前
     * @param event     イベント
     */
    public static void callEvent(String eventName, Event event) {
        try {
            WhiteEggCore.getInstance().getScript().run("call", eventName, event);
        } catch (PrivilegedActionException | NoSuchMethodException | ScriptException e) {
            e.printStackTrace();
        }
    }

}
