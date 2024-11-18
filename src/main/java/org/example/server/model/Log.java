package org.example.server.model;


/**
 * @Author: yzhang8
 */
public class Log {

        public String ip;

        public String mac;

        public String path;

        public String time;

        public Log() {
        }

        public Log(String ip, String mac, String path, String time) {
            this.ip = ip;
            this.mac = mac;
            this.path = path;
            this.time = time;
        }

        public String toString() {
            return "Log{" + "ip='" + ip + ", mac='" + mac + ", path='" + path + ", time='" + time + '}';
        }
}
