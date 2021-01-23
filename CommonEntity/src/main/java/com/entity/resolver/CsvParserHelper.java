package com.entity.resolver;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class CsvParserHelper {
    public Iterable<CSVRecord> getCsvRecords(InputStream inputStream) throws IOException {
        final BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(inputStream,
                        "UTF-8"));
        final CSVParser csvParser = new CSVParser(fileReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim());
        return csvParser.getRecords();

    }

}
