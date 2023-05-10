package com.lab.concurrency;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

public class Sample {

    private final long TICKS_AT_EPOCK = 621355968000000000L;
    private final long TICKS_PER_MILLISECOND = 10000;

    private Boolean isFirstSample;
    private LocalDateTime timestamp;
    private Long value;
    private Boolean hasBeenValidated;

    public Boolean getIsFirstSample() {
        return isFirstSample;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Long getValue() {
        return value;
    }

    public Boolean getHasBeenValidated() {
        return hasBeenValidated;
    }
    
    public Sample(Boolean isFirstSample) {
        this.isFirstSample = isFirstSample;
    }

    private Long getNanoSeconds(LocalDateTime dateTime) {
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
        return instant.getEpochSecond() * 1_000_000_000L + instant.getNano();
    }

    public void LoadSampleAtTime(LocalDateTime timestamp) {
        // samples take some CPU to load, don't change this!
        // reducing the CPU time to load a sample is outside of control in this example
        
        this.timestamp = timestamp;
        //this.value = timestamp.getLong(ChronoField.NANO_OF_SECOND) / 10000;
        this.value = getNanoSeconds(timestamp) / 10000;
        
        for (int i = 0; i < 1000; i++) ;
    }

    public Boolean ValidatesSample(Sample previousSample, Duration sampleInterval) throws Exception {
        // samples take some CPU to validate, don't change this!
        // reducing the CPU time to validate a sample is outside of control in this example

        for (int i = 0; i < 5000; i++) ;

        if (previousSample == null && !isFirstSample) {
            throw new Exception("Validation Failed!");
        }
        
        LocalDateTime newDateTime = this.getTimestamp().minus(sampleInterval);
        if (previousSample != null && !previousSample.getTimestamp().equals(newDateTime)) {
            throw new Exception("Validation Failed!");
        }

        hasBeenValidated = true;

        return true;
    }
}
