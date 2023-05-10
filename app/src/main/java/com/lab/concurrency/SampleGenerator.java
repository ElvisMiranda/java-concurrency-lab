package com.lab.concurrency;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SampleGenerator {
    private LocalDateTime sampleStartDate;
    private Duration sampleIncrement;
    int samplesValidated;
    
    private List<Sample> sampleList;

    public SampleGenerator(LocalDateTime sampleStartDate, Duration sampleIncrement) {
        this.sampleList = new ArrayList<Sample>();

        this.sampleStartDate = sampleStartDate;
        this.sampleIncrement = sampleIncrement;
    }

    // Samples should a time-descending ordered list
    public List<Sample> getSamples() {
        return sampleList;
    }

    public int getSamplesValidated() {
        return samplesValidated;
    }

    public void loadSamples(int samplesToGenerate) {
        // TODO: can we load samples faster?
        sampleList.clear();

        LocalDateTime date = sampleStartDate;

        for (int i = 0; i < samplesToGenerate; i++) {
            Sample s = new Sample(i == 0);
            s.LoadSampleAtTime(date);

            sampleList.add(0, s);

            date = date.plus(sampleIncrement);
        }
    }

    public void validateSamples() throws Exception {
        // TODO: can we validate samples faster?
        int samplesValidated = 0;

        //int n = sampleList.size();
        for (int i = 0; i < sampleList.size(); i++) {
            // in this sample the validateSample is always true but assume that's not always the case
            if (sampleList.get(i).ValidatesSample(i < sampleList.size() - 1 ? sampleList.get(i + 1) : null, sampleIncrement)) {
                samplesValidated++;
            }
        }

        this.samplesValidated = samplesValidated;
    }
}
