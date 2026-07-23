package com.bths.benchmark;

import com.bths.entity.TransactionNode;
import com.bths.entity.Transaction;
import com.bths.entity.TransactionStatus;
import com.bths.entity.TransactionType;
import com.bths.service.TransactionManagement;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ============================================================
 *  BENCHMARK: Doubly Linked List (addLast) vs ArrayList (add)
 * ============================================================
 */
public class BenchmarkTest {

    // ── Tham số thí nghiệm ──────────────────────────────────
    private static final int[]  INPUT_SIZES  = {100, 500, 1000, 5000, 10000};
    private static final int    TRIALS       = 100;   // số lần lặp mỗi mốc n
    private static final int    TRIM_PERCENT = 10;    // loại bỏ 10% cao + 10% thấp
    private static final int    WARMUP_N     = 50_000; // phần tử warm-up JVM / JIT

    // ── Entry point ─────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("==========================================================");
        System.out.println("  BENCHMARK: DLL addLast  vs  ArrayList add");
        System.out.println("  Java version : " + System.getProperty("java.version"));
        System.out.println("  OS           : " + System.getProperty("os.name"));
        System.out.println("==========================================================\n");

        // ── Warm-up: chèn WARMUP_N phần tử vào cả 2 cấu trúc để JIT ổn định
        System.out.println("[Warm-up] Inserting " + WARMUP_N + " elements to stabilise JIT...");
        warmup();
        System.out.println("[Warm-up] Done.\n");

        // ── Kết quả sẽ lưu vào đây ──────────────────────────
        double[] dllTimes = new double[INPUT_SIZES.length];
        double[] alTimes  = new double[INPUT_SIZES.length];
        long[]   dllRam   = new long[INPUT_SIZES.length];
        long[]   alRam    = new long[INPUT_SIZES.length];

        // ── Chạy benchmark ───────────────────────────────────
        for (int i = 0; i < INPUT_SIZES.length; i++) {
            int n = INPUT_SIZES[i];

            BenchmarkResult dllResult = benchmarkDLL(n);
            BenchmarkResult alResult  = benchmarkArrayList(n);

            dllTimes[i] = dllResult.avgTimeMs;
            alTimes[i]  = alResult.avgTimeMs;
            dllRam[i]   = dllResult.ramBytesPerElement;
            alRam[i]    = alResult.ramBytesPerElement;

            System.out.printf("n = %6d  |  DLL: %8.4f ms  (%4d B/elem)  |  ArrayList: %8.4f ms  (%4d B/elem)%n",
                    n, dllTimes[i], dllRam[i], alTimes[i], alRam[i]);
        }

        // ── Bảng kết quả đẹp ─────────────────────────────────
        printResultTable(dllTimes, alTimes, dllRam, alRam);
    }

    // ────────────────────────────────────────────────────────
    //  Benchmark DLL
    // ────────────────────────────────────────────────────────
    private static BenchmarkResult benchmarkDLL(int n) {
        double[] times = new double[TRIALS];
        long totalRam  = 0;

        for (int t = 0; t < TRIALS; t++) {
            System.gc();
            long ramBefore = usedMemory();

            TransactionManagement dll = new TransactionManagement();
            long start = System.nanoTime();

            for (int k = 0; k < n; k++) {
                dll.addLast(makeTransaction(k));
            }

            long end = System.nanoTime();
            long ramAfter = usedMemory();

            times[t]  = (end - start) / 1_000_000.0;   // ns → ms
            totalRam += Math.max(0L, ramAfter - ramBefore);
        }

        double avgMs  = trimmedMean(times, TRIM_PERCENT);
        long   avgRam = (n > 0) ? (totalRam / TRIALS / n) : 0;
        return new BenchmarkResult(avgMs, avgRam);
    }

    // ────────────────────────────────────────────────────────
    //  Benchmark ArrayList
    // ────────────────────────────────────────────────────────
    private static BenchmarkResult benchmarkArrayList(int n) {
        double[] times = new double[TRIALS];
        long totalRam  = 0;

        for (int t = 0; t < TRIALS; t++) {
            System.gc();
            long ramBefore = usedMemory();

            ArrayList<Transaction> list = new ArrayList<>();
            long start = System.nanoTime();

            for (int k = 0; k < n; k++) {
                list.add(makeTransaction(k));
            }

            long end = System.nanoTime();
            long ramAfter = usedMemory();

            times[t]  = (end - start) / 1_000_000.0;
            totalRam += Math.max(0L, ramAfter - ramBefore);
        }

        double avgMs  = trimmedMean(times, TRIM_PERCENT);
        long   avgRam = (n > 0) ? (totalRam / TRIALS / n) : 0;
        return new BenchmarkResult(avgMs, avgRam);
    }

    // ────────────────────────────────────────────────────────
    //  Warm-up JVM
    // ────────────────────────────────────────────────────────
    private static void warmup() {
        TransactionManagement dll = new TransactionManagement();
        ArrayList<Transaction> list = new ArrayList<>();
        for (int i = 0; i < WARMUP_N; i++) {
            Transaction t = makeTransaction(i);
            dll.addLast(t);
            list.add(t);
        }
        // Giữ tham chiếu để JIT không tối ưu hoá bỏ vòng lặp
        if (dll.getSize() == 0 || list.isEmpty()) System.out.println("(unreachable)");
    }

    // ────────────────────────────────────────────────────────
    //  Tạo Transaction giả
    // ────────────────────────────────────────────────────────
    private static Transaction makeTransaction(int index) {
        return new Transaction(
                "TX" + index,
                "ACC" + index,
                "ACC" + (index + 1),
                1000.0 + index,
                TransactionType.TRANSFER,
                "2025-01-01 00:00:00",
                TransactionStatus.COMPLETED
        );
    }

    // ────────────────────────────────────────────────────────
    //  Trimmed Mean: loại bỏ top X% và bottom X%
    // ────────────────────────────────────────────────────────
    private static double trimmedMean(double[] data, int trimPercent) {
        double[] sorted = Arrays.copyOf(data, data.length);
        Arrays.sort(sorted);
        int cut = (int) Math.round(sorted.length * trimPercent / 100.0);
        double sum = 0;
        int count = 0;
        for (int i = cut; i < sorted.length - cut; i++) {
            sum += sorted[i];
            count++;
        }
        return count > 0 ? sum / count : 0;
    }

    // ────────────────────────────────────────────────────────
    //  Đo RAM đang dùng (bytes)
    // ────────────────────────────────────────────────────────
    private static long usedMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

    // ────────────────────────────────────────────────────────
    //  In bảng kết quả
    // ────────────────────────────────────────────────────────
    private static void printResultTable(double[] dllTimes, double[] alTimes,
                                         long[] dllRam, long[] alRam) {
        System.out.println("\n==========================================================");
        System.out.println("  KẾT QUẢ ");
        System.out.println("==========================================================");
        System.out.printf("%-12s | %-20s | %-20s%n",
                "Input Size", "Algorithm A (DLL ms)", "Algorithm B (ArrayList ms)");
        System.out.println("-------------|----------------------|----------------------");
        for (int i = 0; i < INPUT_SIZES.length; i++) {
            System.out.printf("%-12d | %-20.4f | %-20.4f%n",
                    INPUT_SIZES[i], dllTimes[i], alTimes[i]);
        }

        System.out.println("\n==========================================================");
        System.out.println("  RAM / phần tử (bytes)");
        System.out.println("==========================================================");
        System.out.printf("%-12s | %-20s | %-20s%n",
                "Input Size", "DLL (B/elem)", "ArrayList (B/elem)");
        System.out.println("-------------|----------------------|----------------------");
        for (int i = 0; i < INPUT_SIZES.length; i++) {
            System.out.printf("%-12d | %-20d | %-20d%n",
                    INPUT_SIZES[i], dllRam[i], alRam[i]);
        }

    }

    // ────────────────────────────────────────────────────────
    //  Inner class giữ kết quả một lần đo
    // ────────────────────────────────────────────────────────
    private static class BenchmarkResult {
        final double avgTimeMs;
        final long   ramBytesPerElement;

        BenchmarkResult(double avgTimeMs, long ramBytesPerElement) {
            this.avgTimeMs          = avgTimeMs;
            this.ramBytesPerElement = ramBytesPerElement;
        }
    }
}