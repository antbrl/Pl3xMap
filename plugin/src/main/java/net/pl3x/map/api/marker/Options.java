package net.pl3x.map.api.marker;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.Objects;
import net.pl3x.map.api.JsonSerializable;
import net.pl3x.map.api.marker.option.Fill;
import net.pl3x.map.api.marker.option.Popup;
import net.pl3x.map.api.marker.option.Stroke;
import net.pl3x.map.api.marker.option.Tooltip;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Marker options.
 */
public class Options implements JsonSerializable {
    private Stroke stroke;
    private Fill fill;
    private Tooltip tooltip;
    private Popup popup;

    /**
     * Create empty marker options.
     */
    public Options() {
    }

    /**
     * Create marker options.
     *
     * @param stroke  stroke rules
     * @param fill    fill rules
     * @param tooltip tooltip rules
     */
    public Options(@Nullable Stroke stroke, @Nullable Fill fill, @Nullable Tooltip tooltip, @Nullable Popup popup) {
        setStroke(stroke);
        setFill(fill);
        setTooltip(tooltip);
        setPopup(popup);
    }

    /**
     * Get stroke rules.
     *
     * @return stroke rules
     */
    @Nullable
    public Stroke getStroke() {
        return this.stroke;
    }

    /**
     * Set new stroke rules.
     *
     * @param stroke new stroke rules.
     * @return this marker options
     */
    @NotNull
    public Options setStroke(@Nullable Stroke stroke) {
        this.stroke = stroke;
        return this;
    }

    /**
     * Get fill rules.
     *
     * @return fill rules
     */
    @Nullable
    public Fill getFill() {
        return this.fill;
    }

    /**
     * Set new fill rules.
     *
     * @param fill new fill rules
     * @return this marker options
     */
    @NotNull
    public Options setFill(@Nullable Fill fill) {
        this.fill = fill;
        return this;
    }

    /**
     * Get tooltip rules.
     *
     * @return tooltip rules
     */
    @Nullable
    public Tooltip getTooltip() {
        return this.tooltip;
    }

    /**
     * Set new tooltip rules.
     *
     * @param tooltip new tooltip rules
     * @return this marker options
     */
    @NotNull
    public Options setTooltip(@Nullable Tooltip tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    /**
     * Get popup rules.
     *
     * @return popup rules
     */
    @Nullable
    public Popup getPopup() {
        return this.popup;
    }

    /**
     * Set new popup rules.
     *
     * @param popup new popup rules
     * @return this marker options
     */
    @NotNull
    public Options setPopup(@Nullable Popup popup) {
        this.popup = popup;
        return this;
    }

    @Override
    @NotNull
    public JsonElement toJson() {
        JsonArray json = new JsonArray();
        json.add(getStroke() == null ? new JsonArray() : getStroke().toJson());
        json.add(getFill() == null ? new JsonArray() : getFill().toJson());
        json.add(getPopup() == null ? new JsonArray() : getPopup().toJson());
        json.add(getTooltip() == null ? new JsonArray() : getTooltip().toJson());
        return json;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Options other = (Options) o;
        return Objects.equals(getStroke(), other.getStroke())
                && Objects.equals(getFill(), other.getFill())
                && Objects.equals(getTooltip(), other.getTooltip())
                && Objects.equals(getPopup(), other.getPopup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStroke(), getFill(), getTooltip(), getPopup());
    }

    @Override
    public String toString() {
        return "Options{fill=" + getFill() + ",stroke=" + getStroke() + ",tooltip=" + getTooltip() + ",popup=" + getPopup() + "}";
    }
}
