package org.example.rest.services;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
@Component
public class ParserCsvFile {
    public ParserCsvFile() {
    }

    public String getCompanyAirByIata(String code) {
        File csvFile = new File("airline-codes.csv");
        var nameCompany = "";
        if (csvFile.isFile()) {
            try {
                String row = "";
                BufferedReader csvReader = new BufferedReader(new FileReader("airline-codes.csv"));
                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(";");
                    if (data.length>4 && data[0].equals(code)){
                        nameCompany = data[2];
                    }
                }
                csvReader.close();
            } catch (Exception e) {

            }
        }
        if (nameCompany.isEmpty()) return code;
        return nameCompany;
    }
}
