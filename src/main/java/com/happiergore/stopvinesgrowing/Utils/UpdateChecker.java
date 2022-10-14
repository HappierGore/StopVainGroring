package com.happiergore.stopvinesgrowing.Utils;

import com.happiergore.stopvinesgrowing.main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author KillerQueen
 */
public final class UpdateChecker {

    public final float currentVersion;
    public final float latestVersion;

    private int resourceId;
    private URL resourceURL;
    private final String currentVersionString;
    private final String latestVersionString;
    private final UpdateCheckResult updateCheckResult;

    public UpdateChecker(int resourceId) {
        try {
            this.resourceId = resourceId;
            this.resourceURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
        } catch (MalformedURLException exception) {
        }

        currentVersionString = main.getPlugin(main.class).getDescription().getVersion();
        latestVersionString = getLatestVersion();
        latestVersion = Float.parseFloat(getLatestVersion().replace("v", ""));

        if (latestVersionString == null) {
            currentVersion = 0.0f;
            updateCheckResult = UpdateCheckResult.NO_RESULT;
            return;
        }

        currentVersion = Float.parseFloat(currentVersionString.replace("v", ""));

        if (main.debugMode) {
            System.out.println("Current: " + currentVersion);
            System.out.println("Internet: " + latestVersion);
        }

        if (currentVersion < latestVersion) {
            updateCheckResult = UpdateCheckResult.OUT_DATED;
        } else if (currentVersion == latestVersion) {
            updateCheckResult = UpdateCheckResult.UP_TO_DATE;
        } else {
            updateCheckResult = UpdateCheckResult.UNRELEASED;
        }
    }

    public enum UpdateCheckResult {
        NO_RESULT, OUT_DATED, UP_TO_DATE, UNRELEASED,
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + resourceId;
    }

    public UpdateCheckResult getUpdateCheckResult() {
        return updateCheckResult;
    }

    public String getLatestVersion() {
        try {
            URLConnection urlConnection = resourceURL.openConnection();
            return new BufferedReader(new InputStreamReader(urlConnection.getInputStream())).readLine();
        } catch (IOException exception) {
            return null;
        }
    }
}
