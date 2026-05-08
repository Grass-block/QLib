package org.atcraftmc.qlib.config;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public interface Queries {
    Map<String, String> ENVIRONMENT_VARS = new HashMap<>();
    Pattern ENV_PATTERN = Pattern.compile("\\{\\$(.*?)}");
    Pattern ENV_PATTERN2 = Pattern.compile("/\\{\\$(.*?)}");

    static void setEnvironmentVars(ConfigurationSection section) {
        ENVIRONMENT_VARS.clear();
        for (String s : section.getKeys(false)) {
            ENVIRONMENT_VARS.put(s, section.getString(s));
        }
    }

    static String applyEnvironmentVars(String input) {
        try {
            var result2 = new ArrayList<String>();
            var matcher2 = ENV_PATTERN2.matcher(input);
            while (matcher2.find()) {
                result2.add(matcher2.group());
            }

            for (var s : result2) {
                var s2 = s.substring(3, s.length() - 1);
                var replacement = ENVIRONMENT_VARS.get(s2);
                if (replacement == null) {
                    continue;
                }
                input = input.replace(s, replacement);
            }

            var result = new ArrayList<String>();
            var matcher = ENV_PATTERN.matcher(input);
            while (matcher.find()) {
                result.add(matcher.group());
            }

            for (var s : result) {
                var s2 = s.substring(2, s.length() - 1);
                var replacement = ENVIRONMENT_VARS.get(s2);
                if (replacement == null) {
                    continue;
                }
                input = input.replace(s, replacement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return input;
    }
}
