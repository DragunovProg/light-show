package ua.dragunov.lightshow.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "Color_History")
public class ColorHistoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "light_id", nullable = false)
    private Light light;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "old_color_id", nullable = false)
    private Color oldColor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "new_color_id", nullable = false)
    private Color newColor;

    @Column(name = "changed_at")
    private Instant changedAt;

    public Long getId() {
        return id;
    }

    public Light getLight() {
        return light;
    }

    public Color getOldColor() {
        return oldColor;
    }

    public Color getNewColor() {
        return newColor;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public void setOldColor(Color oldColor) {
        this.oldColor = oldColor;
    }

    public void setNewColor(Color newColor) {
        this.newColor = newColor;
    }

    public void setChangedAt(Instant changedAt) {
        this.changedAt = changedAt;
    }
}
