package avpt.gr.common;

import static avpt.gr.train.Train.S5K;

public class Version {

    // инициализация номера версии
    static {
        versionBase = new VersionBase(0, 2);
        versionJar = new VersionJar(0, 1, 2, 230411);
    }

    /**
     * версия базы sqLite
     */
    public static class VersionBase {
        private final int major;
        private final int minor;
        public VersionBase(int major, int minor) {
            this.major = major;
            this.minor = minor;
        }
        public String toString() {
            return String.format("%d.%d", major, minor);
        }
        public int getMajor() {
            return major;
        }
        public int getMinor() {
            return minor;
        }
    }

    /**
     * версия программы
     */
    public static class VersionJar {
        private final int major;
        private final int minor;
        private final int release;
        private final int build;
        // версия exe
        public VersionJar(int major, int minor, int release, int build) {
            this.major = major;
            this.minor = minor;
            this.release = release;
            this.build = build;
        }
        public String toString() {
            return String.format("%d.%d.%d.%d", major, minor, release, build);
        }
        public int getMajor() {
            return major;
        }
        public int getMinor() {
            return minor;
        }
        public int getRelease() {
            return release;
        }
        public int getBuild() {
            return release;
        }
    }

    private static final VersionBase versionBase;
    private static final VersionJar versionJar;

    /**
     * @return версия sqLite базы
     */
    public static VersionBase getVersionBase() {
        return versionBase;
    }

    /**
     * @return версия программы
     */
    public static VersionJar getVersionJar() {
        return versionJar;
    }

    /**
     * проверка поддержки армом локомотива
     * @param typeLoc - код типа
     * @return - поддерживает ли арм данный локомотив
     */
    private static boolean isSupport(int typeLoc) {
        switch (typeLoc) {
            case S5K: return true;
        }
        return false;
    }

}
