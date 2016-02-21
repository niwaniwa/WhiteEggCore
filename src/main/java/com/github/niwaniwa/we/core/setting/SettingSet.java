package com.github.niwaniwa.we.core.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SettingSet {

    private String name = "";
    private List<Setting> settings = new ArrayList<>();

    public SettingSet() {
    }

    public SettingSet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public List<Setting> getSettingByTitle(String title) {
        return settings.stream().filter(s -> s.getTitle().equalsIgnoreCase(title)).collect(Collectors.toList());
    }

    public List<Setting> getSettingByTag(String tag) {
        return settings.stream().filter(s -> (s.getTags().isEmpty() ? false : s.getTags().contains(tag))).collect(Collectors.toList());
    }

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }

    public void addSetting(Setting setting) {
        this.settings.add(setting);
    }

}
