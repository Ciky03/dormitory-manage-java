package cloud.ciky.base.util;


import cloud.ciky.base.constant.DateFormatConstants;
import cloud.ciky.base.exception.BusinessException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * Snow Flow Id生成器
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
public final class IdWorkerUtil {

    private static final Random RANDOM = new Random();

    private static final long WORKER_ID_BITS = 5L;

    private static final long DATACENTERIDBITS = 5L;

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTERIDBITS);

    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTERIDBITS;

    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private static final IdWorkerUtil ID_WORKER_UTIL = new IdWorkerUtil();

    private final long workerId;

    private final long datacenterId;

    private final long idepoch;

    private long sequence = '0';

    private long lastTimestamp = -1L;

    private final AtomicInteger sequenceNum = new AtomicInteger(0);

    private IdWorkerUtil() {
        this(RANDOM.nextInt((int) MAX_WORKER_ID), RANDOM.nextInt((int) MAX_DATACENTER_ID), 1288834974657L);
    }

    private IdWorkerUtil(final long workerId, final long datacenterId, final long idepoch) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.idepoch = idepoch;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static IdWorkerUtil getInstance() {
        return ID_WORKER_UTIL;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new BusinessException(String.format("时钟倒转了。拒绝为%d毫秒生成id", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - idepoch) << TIMESTAMP_LEFT_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT) | sequence;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * Create uuid string.
     *
     * @return the string
     */
    public String createUUID() {
        return String.valueOf(ID_WORKER_UTIL.nextId());
    }

    public synchronized String createCode() {
        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(DateTimeFormatter.ofPattern(DateFormatConstants.FORMAT4));

        // 生成两位流水号
        int seq = sequenceNum.getAndIncrement();
        if (seq > 99) {
            sequenceNum.set(0); // 重置流水号
            seq = sequenceNum.getAndIncrement();
        }
        String sequenceStr = String.format("%02d", seq);

        // 拼接日期时间和流水号
        return format + sequenceStr;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(IdWorkerUtil.getInstance().createCode());
        }
    }
}
