package com.github.niwaniwa.we.core.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonManager {

	private Gson gson = new Gson();

	public JsonManager() {
	}

	/**
	 * localにファイルを保存します
	 * @param path 保存フォルダ
	 * @param file ファイル名
	 * @param json json
	 * @param chara 文字コード
	 * @return 成功したか
	 */
	public boolean writeJson(File path, String file, JsonObject json, String chara) {
		if(!path.exists()){ path.mkdirs(); }
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path, file)), chara));
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
		}
		PrintWriter pw = new PrintWriter(bw);
		pw.write(json.toString());
		pw.close();
		return true;
	}

	public boolean writeJson(File path, String file, JsonObject json) {
		return writeJson(path, file, json, "UTF-8");
	}

	public boolean writeJson(File path, String file, JsonObject json, boolean backup) {
		if(backup){
			File f = new File(path, file);
			if(f.exists()){
				f.renameTo(new File(path, file + "_old"));
			}
		}
		writeJson(path, file, json);
		return true;
	}

	public boolean writeJson(String path, String file, JsonObject json) throws IOException {
		return writeJson(new File(file), file, json);
	}

	/**
	 * local
	 * @param file パス
	 * @return JSONObject json
	 * @throws IOException 入出力
	 */
	public JsonObject getJson(File file) {
		if (!file.exists()) { return null; }
		if (file.isDirectory()) { return null; }
		if (!file.getName().endsWith(".json")) { return null; }
		StringBuilder b = new StringBuilder();
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(file));
			String str = reader.readLine();
			while (str != null) {
				b.append(str);
				str = reader.readLine();
			}
			reader.close();
		} catch (IOException ex){
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		if (b.length() == 0) { return null; }
		return gson.fromJson(b.toString(), JsonObject.class);
	}

	public  static Map<String, Object> toMap(JsonObject json){
		Map<String, Object> map = new HashMap<>();
		for(Entry<String, JsonElement> entry : json.entrySet()){
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

	public static List<JsonElement> toList(JsonArray array){
		List<JsonElement> list = new ArrayList<JsonElement>();
		for (int i = 0; i < array.size(); i++) {
			JsonElement value = array.get(i);
			list.add(value);
		}
		return list;
	}

}
