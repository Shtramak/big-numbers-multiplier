package com.shtramak;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BigNumbersMultiplier {
    public static void main(String[] args) {
        System.out.println(BigNumbersMultiplier.multiply("11111", "9999"));
    }

    public static String multiply(String num1, String num2) {
        Number number1 = Number.valueOf(num1);
        Number number2 = Number.valueOf(num2);
        return number1.multiply(number2).toString();
    }

    public static class Number {
        private final String value;

        private Number(String value) {
            this.value = value;
        }

        public static Number valueOf(String number) {
            if (!isNumericString(number)) {
                throw new IllegalArgumentException("The string should contains only digits!");
            }
            return new Number(number);
        }

        public static boolean isNumericString(String str) {
            return str.chars()
                    .mapToObj(ch -> (char) ch)
                    .allMatch(Character::isDigit);
        }

        public int numberAt(int index) {
            Objects.checkIndex(index, size());
            char charValue = value.charAt(index);
            return Character.getNumericValue(charValue);
        }

        public Number add(Number number) {
            Number longer;
            Number shorter;
            if (this.size() >= number.size()) {
                longer = this;
                shorter = number;
            } else {
                longer = number;
                shorter = this;
            }
            int decimalDiff = Math.abs(this.size() - number.size());
            shorter = shorter.withMockZeros(decimalDiff);
            StringBuilder result = new StringBuilder();
            int reminder = 0;
            for (int i = longer.size() - 1; i >= 0; i--) {
                int sum = shorter.numberAt(i) + longer.numberAt(i) + reminder;
                int appender = sum % 10;
                reminder = sum / 10;
                result.append(appender);
            }
            if (reminder != 0) {
                result.append(reminder);
            }
            return Number.valueOf(result.reverse().toString());
        }

        public Number multiply(Number number) {
            List<Number> terms = termsList(number);
            Number sumOfTerms = Number.valueOf("0");
            for (Number term : terms) {
                sumOfTerms = sumOfTerms.add(term);
            }
            return sumOfTerms;
        }

        private Number numberMultiplyPositiveSingleInt(int singleInt) {
            validateSinglePositiveInteger(singleInt);
            StringBuilder resultBuilder = new StringBuilder();
            int reminder = 0;
            for (int i = this.size() - 1; i >= 0; i--) {
                int prod = numberAt(i) * singleInt + reminder;
                int appender = prod % 10;
                resultBuilder.append(appender);
                reminder = prod / 10;
            }
            if (reminder > 0) {
                resultBuilder.append(reminder);
            }
            String strReversed = resultBuilder.reverse().toString();
            return new Number(strReversed);
        }


        private Number withMockZeros(int decimalDiff) {
            StringBuilder stringBuilder = new StringBuilder(this.toString()).reverse();
            for (int i = 0; i < decimalDiff; i++) {
                stringBuilder.append("0");
            }
            return Number.valueOf(stringBuilder.reverse().toString());
        }

        private List<Number> termsList(Number number) {
            List<Number> result = new ArrayList<>();
            int place = 0;
            for (int i = this.size() - 1; i >= 0; i--) {
                Number term = number.numberMultiplyPositiveSingleInt(this.numberAt(i));
                term = term.withDecimalPlace(place);
                place++;
                result.add(term);
            }
            return result;
        }

        private Number withDecimalPlace(int place) {
            StringBuilder result = new StringBuilder(this.toString());
            for (int i = 0; i < place; i++) {
                result.append("0");
            }
            return new Number(result.toString());
        }

        private int size() {
            return value.length();
        }

        private void validateSinglePositiveInteger(int singleInt) {
            if (singleInt > 9 || singleInt < 0) {
                String message = String.format("Wrong parameter of singleInt: %d. " +
                        "Must be a single digit and positive!", singleInt);
                throw new IllegalArgumentException(message);
            }
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
