package chucknorris;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        String answer = option();

        while (!answer.equals("exit")) {
                if(answer.equals("encode")) encode();
            else
                if(answer.equals("decode")) decode();
            else {
                System.out.println("There is no '"+ answer +"' operation");
                System.out.println();
            }
            answer = option();
        }
        exit();
    }

    public static boolean validateEncrypted(String string){
        String[] characters = string.split("");
        String cero = "0";
        String dobleCero = "00";
        String space = " ";
        String[] bloques = string.split(" ");
        int numero_bloques;
        numero_bloques = bloques.length;
        StringBuilder sb = new StringBuilder();
        String binary;


        //The encoded message includes characters other than 0 or spaces;
        for(int i = 0; i < characters.length; i ++) {
            if(!(cero.equals(characters[i]) || space.equals(characters[i]))) {
                System.out.println("Encoded string is not valid.");
                return false;
           }
        }

        //The first block of each sequence is not 0 or 00;
        for(int i = 0; i < bloques.length; i+=2){
            if(!(cero.equals(bloques[i]) || dobleCero.equals(bloques[i]))){
                System.out.println("Encoded string is not valid.");
                return false;
            }
        }

        //The number of blocks is odd;
        if(!(numero_bloques % 2 == 0)){
            System.out.println("Encoded string is not valid.");
            return false;
        }

        //The length of the decoded binary string is not a multiple of 7.
        for(int i = 1; i < bloques.length; i+=2) sb.append(bloques[i]);
        binary = String.valueOf(sb);
        if(!(binary.length() % 7 == 0)){
            System.out.println("Encoded string is not valid.");
            return false;
        }

        return true;
    }

    public static void encode(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input string:");
        String string = scanner.nextLine();
        string = convertToBinary(string);
        string = encryptString(string);
        System.out.println("Encoded string:");
        System.out.println(string);
        System.out.println();
    }

    public static void decode(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Input encoded string:");
            String string = scanner.nextLine();
            if (validateEncrypted(string)){
                string = decryptString(string);
                string = convertToString(string);
                System.out.println("Decoded string:");
                System.out.println(string);
            }
            System.out.println();
        }catch(RuntimeException e) {
            System.out.println("Encoded string is not valid.");
            System.out.println();
        }
    }

    public static void exit() {
        String string = "Bye!";
        System.out.println(string);
    }

    public static String option() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input operation (encode/decode/exit):");
        String string = scanner.nextLine();
        return string;
    }


    public static String convertToBinary(String string) {
        StringBuilder sb = new StringBuilder();
        String binary;
        for (int i = 0; i < string.length(); i++){
            sb.append(String.format("%07d",Integer.parseInt(Integer.toBinaryString(Integer.valueOf(string.charAt(i))))));
        }
        binary = String.valueOf(sb);

        return binary;
    }

    public static String convertToString(String string) {
        String[] sevenBitParts = string.split(" ");//create an array of strings of seven each bits
        char[] letters = new char[sevenBitParts.length];//Begin the array with the letter group number.
        for(int i = 0; i < sevenBitParts.length; i ++) {
            letters[i] = (char) (Integer.parseInt(sevenBitParts[i],2));
        }
        string = new String(letters);
        return string;
    }

    public static String decryptString(String string) {
        StringBuilder binaryString = new StringBuilder();
        String decryptArray[];
        String space = " ";
        int count = 0;
        decryptArray = string.split(" ");
        for (int i = 0; i < decryptArray.length - 1; i++) {
            if (decryptArray[i].equals("0")) {
                for(int j = 0; j < decryptArray[i + 1].length(); j ++){
                    binaryString.append("1");
                    count ++;
                    if (count > 6) {
                        binaryString.append(space);
                        count = 0;
                    }
                }
            } else if (decryptArray[i].equals("00")) {
                for(int j = 0; j < decryptArray[i + 1].length(); j ++){
                    binaryString.append("0");
                    count ++;
                    if (count > 6) {
                        binaryString.append(space);
                        count = 0;
                    }
                }
            }
            i++;
        }
        string = binaryString.toString();
        return string;
    }

    public static String encryptString(String string) {
        StringBuilder sb = new StringBuilder();
        String encriptedString = null;
        String bit = null , nextBit;
        int count;
        for (int i = 0; i < string.length(); i++) {
            count = 0;
            for (int j = i; j < string.length(); j ++) {
                bit = String.valueOf(string.charAt(i));
                nextBit = String.valueOf(string.charAt(j));
                if (bit.equals(nextBit)) count ++;
                else break;
            }

            if (bit.equals("1")) {
                sb.append("0");
                sb.append(" ");
                for (int j = 0; j < count; j++) {
                    sb.append("0");
                }
                sb.append(" ");
            }

            if (bit.equals("0")) {
                sb.append("00");
                sb.append(" ");
                for (int j = 0; j < count; j++) {
                    sb.append("0");
                }
                sb.append(" ");
            }

            if (count != 1)
                i += (count - 1);
        }
        encriptedString = sb.toString();
        return encriptedString;
    }
}