package me.lhy.pandaid.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UserIdGenerator {
    private static final char[] CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._".toCharArray();

    // 时间起点：2025-03-01 00:00:00
    private static final long EPOCH = 1743379200000L;
    private static final int MACHINE_BITS = 3; // 最大支持8台机器
    private static final int SEQUENCE_BITS = 3; // 每毫秒8个序列

    private final int machineId;
    private volatile long lastTimestamp = -1L;
    private final AtomicInteger sequence = new AtomicInteger(0);

    public UserIdGenerator(@Value("${node.id}") int machineId) {
        this.machineId = machineId & 0x7; // 限制0-7
    }

    public String nextId() {
        long currentTime = System.currentTimeMillis();
        int currentSequence;

        synchronized (this) {
            if (currentTime < lastTimestamp) {
                throw new RuntimeException("时钟回拨异常");
            }

            if (currentTime == lastTimestamp) {
                currentSequence = sequence.incrementAndGet() & 0x7;
                if (currentSequence == 0) {
                    currentTime = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence.set(0);
                currentSequence = 0;
            }

            lastTimestamp = currentTime;
        }

        long timestampPart = (currentTime - EPOCH) << (MACHINE_BITS + SEQUENCE_BITS);
        long machinePart = (long) machineId << SEQUENCE_BITS;
        long fullId = timestampPart | machinePart | currentSequence;

        return encodeBase64(fullId);
    }

    private String encodeBase64(long value) {
        char[] buffer = new char[16];
        for (int i = 15; i >= 0; i--) {
            buffer[i] = CHAR_SET[(int) (value & 0x3F)];
            value >>>= 6;
        }
        return new String(buffer);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
