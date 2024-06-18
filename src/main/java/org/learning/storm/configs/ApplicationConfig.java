package org.learning.storm.configs;

import org.learning.storm.dtos.KafkaConfig;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class ApplicationConfig {

    public static KafkaConfig loadConfig(String filename) {
        LoaderOptions loaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(KafkaConfig.class, loaderOptions);
        Yaml yaml = new Yaml(constructor);
        InputStream inputStream = ApplicationConfig.class
                .getClassLoader()
                .getResourceAsStream(filename);
        return yaml.load(inputStream);
    }
}
