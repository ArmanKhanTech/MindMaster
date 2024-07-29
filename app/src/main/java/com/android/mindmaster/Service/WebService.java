package com.android.mindmaster.Service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;

import com.android.mindmaster.Activity.DrawOnTopLaunchActivity;
import com.android.mindmaster.Database.BlockDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@SuppressLint("SwitchIntDef")
public class WebService extends AccessibilityService {
    @NonNull
    private static List<SupportedBrowserConfig> getSupportedBrowsers() {
        List<SupportedBrowserConfig> browsers = new ArrayList<>();
        browsers.add(new SupportedBrowserConfig("com.android.chrome", "com.android.chrome:id/url_bar"));
        browsers.add(new SupportedBrowserConfig("org.mozilla.firefox", "org.mozilla.firefox:id/mozac_browser_toolbar_url_view"));
        browsers.add(new SupportedBrowserConfig("com.opera.browser", "com.opera.browser:id/url_field"));
        browsers.add(new SupportedBrowserConfig("com.opera.mini.native", "com.opera.mini.native:id/url_field"));
        return browsers;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String browserApp = "";
        String browserUrl = "";

        final int eventType = event.getEventType();

        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED: {
                AccessibilityNodeInfo parentNodeInfo = event.getSource();
                if (parentNodeInfo == null) {
                    return;
                }

                String packageName = event.getPackageName().toString();
                SupportedBrowserConfig browserConfig = null;
                for (SupportedBrowserConfig supportedConfig : getSupportedBrowsers()) {
                    if (supportedConfig.packageName.equals(packageName)) {
                        browserConfig = supportedConfig;
                    }
                }

                if (browserConfig == null) {
                    return;
                }

                String capturedUrl = captureUrl(parentNodeInfo, browserConfig);
                parentNodeInfo.recycle();

                if (capturedUrl == null) {
                    return;
                }

                if (!packageName.equals(browserApp)) {
                    browserUrl = capturedUrl;
                    browserApp = packageName;
                } else if (!capturedUrl.equals(browserUrl)) {
                    browserUrl = capturedUrl;
                }

                if (!browserUrl.isEmpty()) {
                    blockWeb(browserUrl, browserApp);
                    blockKey(browserUrl, browserApp);
                }
            }
            break;
        }
    }

    private void blockWeb(String browserUrl, String browserApp) {
        String url;

        int slashIndex = browserUrl.indexOf("/");
        int dotIndex = browserUrl.indexOf(".");
        if (slashIndex != -1) {
            url = browserUrl.substring(0, slashIndex);
        } else if (dotIndex != -1) {
            url = browserUrl.substring(0, dotIndex);
        } else {
            return;
        }

        try (BlockDatabase blockDatabase = new BlockDatabase(this)) {
            List<HashMap<String, String>> list = blockDatabase.readRecordsWeb(url);
            if (!list.isEmpty()) {
                Calendar cal = Calendar.getInstance();
                int currentDay = cal.get(Calendar.DAY_OF_WEEK);
                String currentDayName = getDay(currentDay);

                List<HashMap<String, String>> filteredList = new ArrayList<>();
                for (HashMap<String, String> map : list) {
                    if (Objects.requireNonNull(map.get("scheduleDays")).contains(currentDayName)) {
                        filteredList.add(map);
                    }
                }

                for (HashMap<String, String> map : filteredList) {
                    if (Objects.equals(map.get("name"), url)) {
                        if (Objects.equals(map.get("scheduleType"), "Specific Time") &&
                            Objects.equals(map.get("profileStatus"), "1")
                        ) {
                            if (Objects.equals(map.get("launch"), "1")) {
                                String[] params = Objects.requireNonNull(map.get("scheduleParams")).split(" ");
                                int fromHours = Integer.parseInt(params[0]);
                                int fromMinutes = Integer.parseInt(params[1]);
                                int toHours = Integer.parseInt(params[2]);
                                int toMinutes = Integer.parseInt(params[3]);

                                if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= fromHours &&
                                        Calendar.getInstance().get(Calendar.MINUTE) >= fromMinutes &&
                                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= toHours &&
                                        Calendar.getInstance().get(Calendar.MINUTE) <= toMinutes) {
                                    System.gc();
                                    Runtime.getRuntime().runFinalization();
                                    drawOnTop(url, browserApp, map);
                                } else {
                                    return;
                                }
                            }
                        } else if (Objects.equals(map.get("scheduleType"), "Quick Block") &&
                            Objects.equals(map.get("profileStatus"), "1")
                        ) {
                            if (Objects.equals(map.get("launch"), "1")) {
                                String[] params = Objects.requireNonNull(map.get("scheduleParams")).split(" ");
                                int untilHours = Integer.parseInt(params[0]);
                                int untilMins = Integer.parseInt(params[1]);

                                if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= untilHours &&
                                        Calendar.getInstance().get(Calendar.MINUTE) <= untilMins) {
                                    System.gc();
                                    Runtime.getRuntime().runFinalization();
                                    drawOnTop(url, browserApp, map);
                                } else {
                                    blockDatabase.deleteRecordById(map.get("id"));
                                    return;
                                }
                            }
                        } else if (Objects.equals(map.get("scheduleType"), "Fixed Block") &&
                            Objects.equals(map.get("profileStatus"), "1")
                        ) {
                            if (Objects.equals(map.get("launch"), "1")) {
                                System.gc();
                                Runtime.getRuntime().runFinalization();
                                drawOnTop(url, browserApp, map);
                            }
                        }
                    } else {
                        return;
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void blockKey(String browserUrl, String browserApp) {
        Uri uri = Uri.parse(browserUrl);
        String[] keys;

        if (uri.getQueryParameter("q") != null) {
            keys = Objects.requireNonNull(uri.getQueryParameter("q")).split("\\+");
            if (keys.length == 0) {
                return;
            }
        } else {
            return;
        }

        try (BlockDatabase blockDatabase = new BlockDatabase(this)) {
            for (String key : keys) {
                List<HashMap<String, String>> list = blockDatabase.readRecordsKey(key);
                if (!list.isEmpty()) {
                    Calendar cal = Calendar.getInstance();
                    int currentDay = cal.get(Calendar.DAY_OF_WEEK);
                    String currentDayName = getDay(currentDay);

                    List<HashMap<String, String>> filteredList = new ArrayList<>();
                    for (HashMap<String, String> map : list) {
                        if (Objects.requireNonNull(map.get("scheduleDays")).contains(currentDayName)) {
                            filteredList.add(map);
                        }
                    }

                    for (HashMap<String, String> map : filteredList) {
                        if (Objects.equals(map.get("name"), key)) {
                            if (Objects.equals(map.get("scheduleType"), "Specific Time") &&
                                Objects.equals(map.get("profileStatus"), "1")
                            ) {
                                if (Objects.equals(map.get("launch"), "1")) {
                                    String[] params = Objects.requireNonNull(map.get("scheduleParams")).split(" ");
                                    int fromHours = Integer.parseInt(params[0]);
                                    int fromMinutes = Integer.parseInt(params[1]);
                                    int toHours = Integer.parseInt(params[2]);
                                    int toMinutes = Integer.parseInt(params[3]);

                                    if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= fromHours &&
                                            Calendar.getInstance().get(Calendar.MINUTE) >= fromMinutes &&
                                            Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= toHours &&
                                            Calendar.getInstance().get(Calendar.MINUTE) <= toMinutes) {
                                        System.gc();
                                        Runtime.getRuntime().runFinalization();
                                        drawOnTop(key, browserApp, map);
                                    } else {
                                        return;
                                    }
                                }
                            } else if (Objects.equals(map.get("scheduleType"), "Quick Block") &&
                                Objects.equals(map.get("profileStatus"), "1")
                            ) {
                                if (Objects.equals(map.get("launch"), "1")) {
                                    String[] params = Objects.requireNonNull(map.get("scheduleParams")).split(" ");
                                    int untilHours = Integer.parseInt(params[0]);
                                    int untilMins = Integer.parseInt(params[1]);

                                    if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= untilHours &&
                                            Calendar.getInstance().get(Calendar.MINUTE) <= untilMins) {
                                        System.gc();
                                        Runtime.getRuntime().runFinalization();
                                        drawOnTop(key, browserApp, map);
                                    } else {
                                        blockDatabase.deleteRecordById(map.get("id"));
                                        return;
                                    }
                                }
                            } else if (Objects.equals(map.get("scheduleType"), "Fixed Block") &&
                                Objects.equals(map.get("profileStatus"), "1")
                            ) {
                                if (Objects.equals(map.get("launch"), "1")) {
                                    System.gc();
                                    Runtime.getRuntime().runFinalization();
                                    drawOnTop(key, browserApp, map);
                                }
                            }
                        } else {
                            return;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void drawOnTop(String key, String browserApp, HashMap<String, String> map) {
        Intent lockIntent = new Intent(this, DrawOnTopLaunchActivity.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        lockIntent.putExtra("name", key);
        lockIntent.putExtra("packageName", browserApp);
        lockIntent.putExtra("type", map.get("type"));
        lockIntent.putExtra("text", map.get("text"));
        startActivity(lockIntent);
    }

    private String getDay(int day) {
        return switch (day) {
            case Calendar.SUNDAY -> "Sunday";
            case Calendar.MONDAY -> "Monday";
            case Calendar.TUESDAY -> "Tuesday";
            case Calendar.WEDNESDAY -> "Wednesday";
            case Calendar.THURSDAY -> "Thursday";
            case Calendar.FRIDAY -> "Friday";
            case Calendar.SATURDAY -> "Saturday";
            default -> "";
        };
    }

    private String captureUrl(AccessibilityNodeInfo info, SupportedBrowserConfig config) {
        List<AccessibilityNodeInfo> nodes = info.findAccessibilityNodeInfosByViewId(config.addressBarId);
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }

        AccessibilityNodeInfo addressBarNodeInfo = nodes.get(0);
        String url = null;
        if (addressBarNodeInfo.getText() != null) {
            url = addressBarNodeInfo.getText().toString();
        }

        addressBarNodeInfo.refresh();
        return url;
    }

    @Override
    public void onInterrupt() {}

    @Override
    protected void onServiceConnected() {}

    private static class SupportedBrowserConfig {
        public String packageName, addressBarId;

        public SupportedBrowserConfig(String packageName, String addressBarId) {
            this.packageName = packageName;
            this.addressBarId = addressBarId;
        }
    }
}
