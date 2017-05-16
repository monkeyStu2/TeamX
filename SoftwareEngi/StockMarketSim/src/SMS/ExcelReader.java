package SMS;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelReader {

    public static Object[] readInitialData(String file) {
        HashMap<String, ArrayList<Integer>> clients;
        List<Company> companies = new ArrayList<>();

        try {
            FileInputStream excelFile = new FileInputStream(new File(file));
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
            Sheet dataSheet = workbook.getSheetAt(0);

            // gather client info
            Row clientNames = dataSheet.getRow(8);
            clients = new HashMap<>();
            int i = 7;
            while (clientNames.getCell(i) != null) {
                String clientName = clientNames.getCell(i).getStringCellValue();  // get client name
                int j = 11;
                ArrayList<Integer> stock = new ArrayList<>();
                while (dataSheet.getRow(j) != null) {
                    Row clientStock = dataSheet.getRow(j);  // in client column, go each row
                    stock.add((int)clientStock.getCell(i).getNumericCellValue());  // to get stock value
                    j++;
                }
                stock.add((int)dataSheet.getRow(32).getCell(i).getNumericCellValue()); // get client cash holding
                clients.put(clientName, stock); // add a client
                i++;
            }

            // gather companies info
            int k = 11;
            while (dataSheet.getRow(k) != null) {
                Row companyRow = dataSheet.getRow(k);
                Company c;
                String companyName = companyRow.getCell(0).getStringCellValue();
                String companyType = companyRow.getCell(2).getStringCellValue();
                int companyTotalShare = (int) companyRow.getCell(3).getNumericCellValue();
                int companySharePrice = (int) companyRow.getCell(18).getNumericCellValue();
                c = new Company(companyName, companyTotalShare, companySharePrice, Company.StockType.valueOf(companyType));
                companies.add(c);
                k++;
            }
            return new Object[] {clients, companies};

        } catch (Exception io) {
            io.printStackTrace();
        }
        return null;
    }
}
