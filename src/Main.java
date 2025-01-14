import java.io.InputStream;
import java.io.IOException;
import java.util.Scanner;

 class Main {
    private static final String FILE_NAME = "data.txt"; // Replace with your file name in src

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] data = null;

        System.out.println("Choose input method:");
        System.out.println("1. Enter data manually");
        System.out.println("2. Read data from file in src directory");
        System.out.print("Enter your choice (1 or 2): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1:
                // Manual data entry
                System.out.print("Enter the data bits: ");
                String input = scanner.nextLine();
                data = new int[input.length()];
                for (int i = 0; i < input.length(); i++) {
                    data[i] = Character.getNumericValue(input.charAt(i));
                }
                break;

            case 2:
                // Read data from file in src directory
                try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("data.txt");
                     Scanner fileScanner = new Scanner(inputStream)) {

                    if (fileScanner.hasNextLine()) {
                        String fileInput = fileScanner.nextLine();
                        data = new int[fileInput.length()];
                        for (int i = 0; i < fileInput.length(); i++) {
                            data[i] = Character.getNumericValue(fileInput.charAt(i));
                        }
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred while reading the file. Please check the file and try again.");
                    return; // Exit if file not found or error occurs
                }
                break;

            default:
                System.out.println("Invalid choice. Exiting.");
                return;
        }

        // Calculate Hamming code
        int[] hammingCode = HammingCodeErrorCorrection.calculateParityBits(data);

        System.out.print("Hamming code with parity bits: ");
        for (int bit : hammingCode) {
            System.out.print(bit);
        }
        System.out.println();

        // Introduce single-bit error
        int bitsFlipped = HammingCodeErrorCorrection.introduceSingleNoise(hammingCode);

        System.out.print("Hamming code with single-bit noise: ");
        for (int bit : hammingCode) {
            System.out.print(bit);
        }
        System.out.println();

        // Detect and correct error
        int errorsCorrected = HammingCodeErrorCorrection.detectAndCorrectError(hammingCode);
        double singleBitErrorCorrectionPercentage = HammingCodeErrorCorrection.calculateErrorCorrectionPercentage(bitsFlipped, errorsCorrected);
        System.out.println("Single-bit error correction percentage: " + singleBitErrorCorrectionPercentage + "%");

        // Introduce burst error with a random number of bits flipped
        bitsFlipped = HammingCodeErrorCorrection.introduceBurstNoise(hammingCode);

        System.out.print("Hamming code with burst noise: ");
        for (int bit : hammingCode) {
            System.out.print(bit);
        }
        System.out.println();

        // Detect and correct burst errors
        errorsCorrected = HammingCodeErrorCorrection.detectAndCorrectError(hammingCode);
        double burstErrorCorrectionPercentage = HammingCodeErrorCorrection.calculateErrorCorrectionPercentage(bitsFlipped, errorsCorrected);
        System.out.println("Burst error correction percentage: " + burstErrorCorrectionPercentage + "%");

        scanner.close();
    }
}
