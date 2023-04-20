package net.pl3x.map.core.configuration;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import net.pl3x.map.core.log.Logger;
import net.pl3x.map.core.util.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.MemorySection;
import org.simpleyaml.configuration.comments.CommentType;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

public abstract class AbstractConfig {
    private YamlFile config;

    @NonNull
    public YamlFile getConfig() {
        return this.config;
    }

    protected void reload(@NonNull Path path, @NonNull Class<? extends AbstractConfig> clazz) {
        // read yaml from file
        this.config = new YamlFile(path.toFile());
        try {
            getConfig().createOrLoadWithComments();
        } catch (InvalidConfigurationException e) {
            Logger.severe("Could not load " + path.getFileName() + ", please correct your syntax errors");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // load data from yaml
        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            Key key = field.getDeclaredAnnotation(Key.class);
            Comment comment = field.getDeclaredAnnotation(Comment.class);
            if (key == null) {
                return;
            }
            try {
                Object obj = getClassObject();
                Object value = getValue(key.value(), field.get(obj));
                field.set(obj, value instanceof String str ? StringUtils.unescapeJava(str) : value);
                if (comment != null) {
                    setComment(key.value(), comment.value());
                }
            } catch (Throwable e) {
                Logger.warn("Failed to load " + key.value() + " from " + path.getFileName().toString());
                e.printStackTrace();
            }
        });

        // save yaml to disk
        try {
            getConfig().save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    protected Object getClassObject() {
        return null;
    }

    @Nullable
    protected Object getValue(@NonNull String path, @Nullable Object def) {
        if (getConfig().get(path) == null) {
            set(path, def);
        }
        return get(path, def);
    }

    protected void setComment(@NonNull String path, @Nullable String comment) {
        getConfig().setComment(path, comment, CommentType.BLOCK);
    }

    @Nullable
    protected Object get(@NonNull String path, @Nullable Object def) {
        Object val = get(path);
        return val == null ? def : val;
    }

    @Nullable
    protected Object get(@NonNull String path) {
        Object value = getConfig().get(path);
        if (!(value instanceof MemorySection)) {
            return value;
        }
        Map<Object, Object> map = new LinkedHashMap<>();
        ConfigurationSection section = getConfig().getConfigurationSection(path);
        if (section == null) {
            return map;
        }
        for (String key : section.getKeys(false)) {
            String rawValue = section.getString(key);
            if (rawValue == null) {
                continue;
            }
            map.put(key, addToMap(rawValue));
        }
        return map;
    }

    @NonNull
    protected Object addToMap(@NonNull String rawValue) {
        return rawValue;
    }

    protected void set(@NonNull String path, @Nullable Object value) {
        getConfig().set(path, value);
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Key {
        @NonNull
        String value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Comment {
        @NonNull
        String value();
    }
}