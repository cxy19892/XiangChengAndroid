package com.yzm.sleep.background;

import java.io.IOException;
import java.util.Map;

import android.os.Build;

public class Shell {
    private static final String LD_LIBRARY_PATH = "LD_LIBRARY_PATH";

    public static void execCmd(String strCmd) throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime()
                .exec(strCmd + "\n");
        proc.waitFor();
        proc.destroy();
    }

    public static void execCmdWithDefaultEnv(String strCmd) throws IOException,
            InterruptedException {
        String sysLib = getLdLibraryPath();
        String cmd = String.format("export " + LD_LIBRARY_PATH + "=%s\n%s", sysLib, strCmd);
        execCmd(cmd);
    }

    public static Process newShellProcess() throws IOException {
        Process proc = new ProcessBuilder("sh").redirectErrorStream(false).start();
        return proc;
    }

    public static Process newShellProcessWithDeaultEnv() throws IOException,
            UnsupportedOperationException {
        ProcessBuilder builder = new ProcessBuilder("sh").redirectErrorStream(false);
        // when <= android 2.2, no support set environment
        if (Build.VERSION.SDK_INT > 8) {
            Map<String, String> env = builder.environment();
            env.put(LD_LIBRARY_PATH, getLdLibraryPath());
        }
        return builder.start();
    }

    /**
     * 获取系统动态库的路径
     */
    public static String getLdLibraryPath() {
        String systemLibPath = System.getenv(LD_LIBRARY_PATH);
        if (null == systemLibPath) {
            systemLibPath = "/vendor/lib:/system/lib";
        } else {
            if (!systemLibPath.contains("/vendor/lib")) {
                systemLibPath += ":/vendor/lib";
            }
            if (!systemLibPath.contains("/system/lib")) {
                systemLibPath += ":/system/lib";
            }
        }
        return systemLibPath;
    }

}
