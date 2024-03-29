package com.scholarly.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class DeviceInfo {
    private static final String TAG = "DeviceInfo: ";

    private String name;
    private String version;
    @SerializedName("api_level")
    private String apiLevel;
    private String platform;
    @SerializedName("form_factor")
    private String formFactor;
    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("app_version_name")
    private String appVersionName;

    public DeviceInfo() {
        name = "";
        version = "";
        apiLevel = "";
        platform = "";
        formFactor = "";
        deviceId = "";
        appVersionName = "";
    }

    public DeviceInfo(String name, String version, String apiLevel, String platform, String formFactor, String deviceId, String appVersionName) {
        this.name = name;
        this.version = version;
        this.apiLevel = apiLevel;
        this.platform = platform;
        this.formFactor = formFactor;
        this.deviceId = deviceId;
        this.appVersionName = appVersionName;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getApiLevel() {
        return apiLevel;
    }

    public String getPlatform() {
        return platform;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public String getDeviceId() {
        return formatDeviceId(deviceId);
    }

    private static String formatDeviceId(String deviceId) {
        return deviceId.replaceAll("-", "").substring(0, 16);
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setApiLevel(String apiLevel) {
        this.apiLevel = apiLevel;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public static DeviceInfo getSystemProperties() {
        Properties properties = System.getProperties();

        DeviceInfo deviceInfo = new DeviceInfo();

        String deviceName = properties.getProperty("os.name");
        String deviceId = "";
        String platform = "";
        if (deviceName.contains("Windows")) {
            deviceId = getWindowsUUID();
            platform = "windows";
            System.out.println(TAG + "Got Windows device ID -> " + deviceId);
        } else if (deviceName.contains("Mac")) {
            deviceId = getMacUUID();
            platform = "macos";
            System.out.println(TAG + "Got Mac device ID -> " + deviceId);
        } else if (deviceName.contains("Linux")) {
            deviceId = getLinuxUUID();
            platform = "linux";
            System.out.println(TAG + "Got Linux device ID -> " + deviceId);
        }

        deviceInfo.setName(deviceName);
        deviceInfo.setPlatform(platform);
        deviceInfo.setFormFactor("desktop");
        deviceInfo.setDeviceId(formatDeviceId(deviceId));
        deviceInfo.setAppVersionName("1.0.0");

//        System.out.println(TAG + "Got device ID with OS name -> " + properties.getProperty("os.name") + " AND arch -> " + properties.getProperty("os.arch") + " AND username -> " + properties.getProperty("user.name"));

        return deviceInfo;
    }

    // Get Windows Machine UUID
    static String getWindowsUUID() {
        try {
            String command = "wmic csproduct get UUID";
            StringBuilder output = new StringBuilder();

            Process SerNumProcess = Runtime.getRuntime().exec(command);
            BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));

            String line = "";
            while ((line = sNumReader.readLine()) != null) {
                output.append(line).append("\n");
            }
            String uuid = output.substring(output.indexOf("\n"), output.length()).trim();;
//            System.out.println(uuid);
            return uuid;

        } catch(Exception ex) {
            System.out.println(TAG + "Cannot get UUID because -> " + ex.getMessage());
        }
        return "";
    }

    //Get Mac Machine UUID
    static String getMacUUID() {
        try {
            String command = "system_profiler SPHardwareDataType | awk '/UUID/ { print $3; }'";

            StringBuilder output = new StringBuilder();

            Process SerNumProcess = Runtime.getRuntime().exec(command);

            BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));

            String line = "";

            while ((line = sNumReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            String uuid = output.substring(output.indexOf("UUID: "), output.length()).replace("UUID: ", "");

            SerNumProcess.waitFor();

            sNumReader.close();

//            System.out.println(uuid);

            return uuid;

        } catch (Exception ex) {
            System.out.println("Cannot get UUID because -> " + ex.getMessage());
        }

        return "";
    }

    // Method for get System UUID for Linux Machine
    static String getLinuxUUID() {
        String command = "dmidecode -s system-uuid";

        // setting uuid to null first
        String uuid = null;
        try {
            Process SerNumProcess
                    = Runtime.getRuntime().exec(command);
            BufferedReader sNumReader
                    = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));

            // reads the uuid line by line to separate the
            // uuid into 4 parts
            uuid = sNumReader.readLine().trim();

            SerNumProcess.waitFor();
            sNumReader.close();

            return uuid;
        }
        catch (Exception ex) {
            System.err.println("Linux UUID Exp : "
                    + ex.getMessage());
            uuid = null;
        }
        return " ";
    }

}
