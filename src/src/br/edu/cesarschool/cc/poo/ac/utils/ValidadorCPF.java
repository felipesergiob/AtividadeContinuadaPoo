package br.edu.cesarschool.cc.poo.ac.utils;

public class ValidadorCPF {

    private ValidadorCPF() {} 

    public static boolean isCpfValido(String cpf) {
        if (StringUtils.isVaziaOuNula(cpf)) {
            return false;
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (!cpf.matches("[0-9]+")) {
            return false;
        }

        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = cpf.charAt(i) - '0';
        }
        if (!validarDigitosVerificadores(digits)) {
            return false;
        }

        return true;
    }

    private static boolean validarDigitosVerificadores(int[] digits) {
        int firstVerifierDigit = calcularDigitoVerificador(digits, 9);
        int secondVerifierDigit = calcularDigitoVerificador(digits, 10);

        return digits[9] == firstVerifierDigit && digits[10] == secondVerifierDigit;
    }

    private static int calcularDigitoVerificador(int[] digits, int length) {
        int sum = 0;
        int weight = length + 1;
        for (int i = 0; i < length; i++) {
            sum += digits[i] * weight;
            weight--;
        }
        int remainder = sum % 11;
        return (remainder < 2) ? 0 : (11 - remainder);
    }
}
