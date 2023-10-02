package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import lombok.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BankAccount {

    public static String filePath = "C:\\Projects\\Lab_alg\\src\\main\\java\\resources\\accounts.csv";
    public static String writePath = "C:\\Projects\\Lab_alg\\src\\main\\java\\resources\\sortedaccounts.csv";
    private String lastName;
    private String firstName;
    private long personalCode;
    private long accountNumber;
    private double balance;
    private String currency;

    public static List<BankAccount> readFromCSV(String filePath) {
        List<BankAccount> bankAccounts = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();

            // Skip the header row (first row)
            boolean firstRow = true;

            for (String[] record : records) {
                if (firstRow) {
                    firstRow = false;
                    continue; // Skip the header row
                }

                BankAccount bankAccount = new BankAccount();
                bankAccount.setLastName(record[0]);
                bankAccount.setFirstName(record[1]);
                bankAccount.setPersonalCode(Long.parseLong(record[2].replace("-", ""))); // Remove hyphens
                bankAccount.setAccountNumber(Long.parseLong(record[3].replace("-", ""))); // Remove hyphens
                bankAccount.setBalance(Double.parseDouble(record[4]));
                bankAccount.setCurrency(record[5]);
                bankAccounts.add(bankAccount);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return bankAccounts;
    }

    public static void printAccounts() {
        List<BankAccount> bankAccounts = readFromCSV(filePath);
        // Output the bank accounts
        for (BankAccount account : bankAccounts) {
            System.out.println("Last Name: " + account.getLastName());
            System.out.println("First Name: " + account.getFirstName());
            System.out.println("Personal Code: " + account.getPersonalCode());
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Balance: " + account.getBalance());
            System.out.println("Currency: " + account.getCurrency());
            System.out.println();
        }
    }


    //First sorting method
    public static void bubbleSort(List<BankAccount> accounts) {
        int n = accounts.size();
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (accounts.get(i - 1).getLastName().compareTo(accounts.get(i).getLastName()) > 0) {
                    // Swap accounts[i-1] and accounts[i]
                    BankAccount temp = accounts.get(i - 1);
                    accounts.set(i - 1, accounts.get(i));
                    accounts.set(i, temp);
                    swapped = true;
                }
            }
        } while (swapped);
    }

    //Second sorting method
    public static void quickSort(List<BankAccount> accounts, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(accounts, low, high);
            quickSort(accounts, low, pivotIndex - 1);
            quickSort(accounts, pivotIndex + 1, high);
        }
    }

    private static int partition(List<BankAccount> accounts, int low, int high) {
        double pivot = accounts.get(high).getBalance();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (accounts.get(j).getBalance() >= pivot) { // Change ">" to ">=" here
                i++;
                swap(accounts, i, j);
            }
        }

        swap(accounts, i + 1, high);
        return i + 1;
    }

    private static void swap(List<BankAccount> accounts, int i, int j) {
        BankAccount temp = accounts.get(i);
        accounts.set(i, accounts.get(j));
        accounts.set(j, temp);
    }

    public static void mergeSort(List<BankAccount> accounts, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSort(accounts, low, mid);
            mergeSort(accounts, mid + 1, high);
            merge(accounts, low, mid, high);
        }
    }

    private static void merge(List<BankAccount> accounts, int low, int mid, int high) {
        int n1 = mid - low + 1;
        int n2 = high - mid;

        List<BankAccount> left = new ArrayList<>(n1);
        List<BankAccount> right = new ArrayList<>(n2);

        for (int i = 0; i < n1; i++) {
            left.add(accounts.get(low + i));
        }
        for (int j = 0; j < n2; j++) {
            right.add(accounts.get(mid + 1 + j));
        }

        int i = 0, j = 0, k = low;

        while (i < n1 && j < n2) {
            if (left.get(i).getBalance() >= right.get(j).getBalance()) {
                accounts.set(k, left.get(i));
                i++;
            } else {
                accounts.set(k, right.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            accounts.set(k, left.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            accounts.set(k, right.get(j));
            j++;
            k++;
        }
    }

    public static void writeSortedDataToCSV(List<BankAccount> sortedAccounts, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write the header row
            String[] header = {"lastName", "firstName", "personalCode", "accountNumber", "balance", "currency"};
            writer.writeNext(header);

            // Write the sorted data
            for (BankAccount account : sortedAccounts) {
                String[] row = {
                        account.getLastName(),
                        account.getFirstName(),
                        String.valueOf(account.getPersonalCode()),
                        String.valueOf(account.getAccountNumber()),
                        String.valueOf(account.getBalance()),
                        account.getCurrency()
                };
                writer.writeNext(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //    printAccounts();
        List<BankAccount> bankAccounts = readFromCSV(filePath);
        //  bubbleSort(bankAccounts);
        //quickSort(bankAccounts, 0, bankAccounts.size() - 1);
        mergeSort(bankAccounts, 0, bankAccounts.size() - 1);
        writeSortedDataToCSV(bankAccounts, writePath);


    }
}
