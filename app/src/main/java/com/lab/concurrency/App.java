package com.lab.concurrency;

import java.time.Duration;
import java.time.LocalDateTime;

import com.google.common.base.Stopwatch;

public class App {

    static int processorCount = 1;
    static int cyclesToRun;
    static int samplesToLoad;
    static LocalDateTime sampleStartDate;
    static Duration sampleIncrement;

    static void init() {
        processorCount = Runtime.getRuntime().availableProcessors();
        cyclesToRun = processorCount > 1 ? processorCount / 2 : 1;
        cyclesToRun = cyclesToRun > 4 ? 4 : cyclesToRun;
        samplesToLoad = 222222;
        sampleStartDate = LocalDateTime.of(1990, 1, 1, 1, 1, 1, 1);
        sampleIncrement = Duration.ofMinutes(5);
    }

    public static void main(String[] args) {
        init();

        Stopwatch totalMonitor = Stopwatch.createStarted();
        logMessage(String.format("Starting Execution on a %d core system. A total of %d cycles will be run", processorCount, cyclesToRun));

        for (int i = 0; i < cyclesToRun; i++) {
            try {
                logMessage(String.format("Cycle %d Started Sample Load.", i));
                Stopwatch cycleTimer = Stopwatch.createStarted();
                SampleGenerator sampleGenerator = new SampleGenerator(sampleStartDate, sampleIncrement);                
                sampleGenerator.loadSamples(samplesToLoad);
                cycleTimer.stop();
                logMessage(String.format("Cycle %d Finished Sample Load. Load Time: %d", i, cycleTimer.elapsed().toMillis()));
                
                logMessage(String.format("Cycle %d Started Sample Validation", i));
                cycleTimer.reset().start();
                sampleGenerator.validateSamples();
                cycleTimer.stop();
                logMessage(String.format("Cycle %d Finished Sample Validation. Total Samples Validated: %d. Validation Time: %d ms.", i, sampleGenerator.getSamplesValidated(), cycleTimer.elapsed().toMillis()));

                Float valueSum = 0f;

                for (Sample s : sampleGenerator.getSamples()) {
                    valueSum += s.getValue();
                }

                // TODO: why do we only seem to get 7 digits of precision? we should see at least 20!
                logMessage(String.format("Cycle %d Sum of All Samples: %.0f", i, valueSum));
                logMessage(String.format("Cycle %d Finished. Total Cycle Time: %d", i, cycleTimer.elapsed().toMillis()));
            } catch (Exception e) {
                logMessage(String.format("Execution Failed!. %s", e.toString()));
            }
        }

        totalMonitor.stop();
        logMessage("-----");
        logMessage(String.format("Execution Finished. Total Elapsed Time: %d ms.", totalMonitor.elapsed().toMillis()));
    }

    static void logMessage(String message) {
        logToFile(message);
        System.out.println(String.format("%s", message));
    }

    static void logToFile(String message) {
        // everything written to the console should also be written to a log under \temp
        // a new log with a unique file name should be created each time the application is run

    }
}
