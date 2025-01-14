import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class HammingCodeErrorCorrection {

    public static int @NotNull [] calculateParityBits(int[] data) {
        int n = data.length;
        int r = 0;

        // Calculate the number of parity bits required
        while (Math.pow(2, r) < n + r + 1) {
            r++;
        }

        // Array to hold data + parity bits
        int[] hammingCode = new int[n + r];

        // Positioning data bits in their places (excluding parity bit positions)
        for (int i = 0, j = 0; i < hammingCode.length; i++) {
            if ((Math.pow(2, j) - 1) == i) {
                hammingCode[i] = 0; // Initialize parity bits to 0
                j++;
            } else {
                hammingCode[i] = data[n - i + j - 1];
            }
        }

        // Calculate parity bits and place them in their positions
        for (int i = 0; i < r; i++) {
            int x = (int) Math.pow(2, i);
            for (int j = 1; j <= hammingCode.length; j++) {
                if (((j >> i) & 1) == 1) {
                    if (x != j) {
                        hammingCode[x - 1] ^= hammingCode[j - 1];
                    }
                }
            }
        } 

        return hammingCode;
    }

    // Method to introduce single-bit noise by flipping one random bit
    public static int introduceSingleNoise(int[] hammingCode) {
        Random random = new Random();
        int randomBit = random.nextInt(hammingCode.length);
        hammingCode[randomBit] ^= 1; // Flip the bit
        System.out.println("Single-bit noise introduced at position: " + (randomBit + 1));
        return 1; // Only one bit was flipped
    }

    // Method to introduce burst noise by flipping a random number of bits between 2 and 10
    public static int introduceBurstNoise(int[] hammingCode) {
        Random random = new Random();  // Compliant

        // Generate a random number of bits to flip, between 2 and 10
        int numBitsToFlip = random.nextInt(9) + 2; // Random number between 2 and 10

        System.out.println("Introducing burst noise with " + numBitsToFlip + " bits flipped:");
        for (int i = 0; i < numBitsToFlip; i++) {
            int randomBit = random.nextInt(hammingCode.length);
            hammingCode[randomBit] ^= 1; // Flip the bit
            System.out.println("Flipped bit at position: " + (randomBit + 1));
        }
        return numBitsToFlip; // Number of bits flipped
    }

    // Method to detect and correct errors in Hamming code
    public static int detectAndCorrectError(int[] hammingCode) {
        int r = 0;

        // Calculate the number of parity bits required
        while (Math.pow(2, r) < hammingCode.length + 1) {
            r++;
        }

        // Find the error position
        int errorPosition = 0;
        for (int i = 0; i < r; i++) {
            int x = (int) Math.pow(2, i);
            int parity = 0;
            for (int j = 1; j <= hammingCode.length; j++) {
                if (((j >> i) & 1) == 1) {
                    parity ^= hammingCode[j - 1];
                }
            }
            errorPosition += parity * x;
        }

        // Correct the error if any and ensure error position is within bounds
        if (errorPosition != 0 && errorPosition <= hammingCode.length) {
            hammingCode[errorPosition - 1] ^= 1;
            System.out.println("Error detected and corrected at position: " + errorPosition);
        } else {
            System.out.println("No error detected or error position out of bounds.");
        }

        // Display the Hamming code after correction
        System.out.print("Hamming code after correction: ");
        for (int bit : hammingCode) {
            System.out.print(bit);
        }
        System.out.println();

        return errorPosition != 0 ? 1 : 0; // Return 1 if an error was corrected, otherwise 0
    }

    // Method to calculate the percentage of error correction
    public static double calculateErrorCorrectionPercentage(int bitsFlipped, int errorsCorrected) {
        return ((double) errorsCorrected / bitsFlipped) * 100;
    }


}
