package org.example.dungeon.model;

import jakarta.persistence.*;

@Entity
@Table(name = "experiment_exposures")
public class ExposureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerId;
    private String experimentKey;
    private String variant;

    public ExposureEntity() {}
    public ExposureEntity(String playerId, String experimentKey, String variant) {
        this.playerId = playerId;
        this.experimentKey = experimentKey;
        this.variant = variant;
    }

    public Long getId() { return id; }
    public String getPlayerId() { return playerId; }
    public String getExperimentKey() { return experimentKey; }
    public String getVariant() { return variant; }

    public void setPlayerId(String playerId) { this.playerId = playerId; }
    public void setExperimentKey(String experimentKey) { this.experimentKey = experimentKey; }
    public void setVariant(String variant) { this.variant = variant; }
}
