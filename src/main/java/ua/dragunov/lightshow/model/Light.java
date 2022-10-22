package ua.dragunov.lightshow.model;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lights")
public class Light {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NaturalId(mutable = true)
    private String label;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    private boolean enabled;

    @OneToMany(mappedBy = "light")
    private List<ColorHistoryRecord> colorHistories;


    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<ColorHistoryRecord> getColorHistories() {
        return colorHistories;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Light light = (Light) o;
        return Objects.equals(label, light.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}
