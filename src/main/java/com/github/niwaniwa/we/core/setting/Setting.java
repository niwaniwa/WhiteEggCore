package com.github.niwaniwa.we.core.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Setting implements Cloneable {

    private String title;
    private boolean enable = true;
    private Map<String, Object> settings = new HashMap<>(15 * 4 / 3);
    private List<String> tags = new ArrayList<>();

    public Setting(String title) {
        this.title = title;
    }

    public void add(String key, Object value) {
        this.settings.put(key, value);
    }

    public void remove(String key) {
        this.settings.remove(key);
    }

    public Object get(String key) {
        return this.settings.get(key);
    }

    public String getTitle() {
        return title;
    }

    public boolean containsKey(String key) {
        return this.settings.containsKey(key);
    }

    public List<Object> containsValue(Object value) {
        return settings.values().stream().filter(v -> v.equals(value)).collect(Collectors.toList());
    }

    public boolean isEnabled() {
        return enable;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> get() {
        return this.settings;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void clear() {
        this.settings.clear();
        this.tags.clear();
    }

    @Override
    public Setting clone() {
        Setting cloneInstance = new Setting(title);
        cloneInstance.clear();
        cloneInstance.get().putAll(settings);
        cloneInstance.getTags().addAll(tags);
        return cloneInstance;
    }

}
