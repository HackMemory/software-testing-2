import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

class SystemTest {

    static double functionEps = 0.1;
    double eps = 0.1;

    static Sec secMock;
    static Cos cosMock;
    static Sin sinMock;
    static Ln lnMock;
    static Log logMock;

    static Reader secIn;
    static Reader cosIn;
    static Reader sinIn;
    static Reader lnIn;
    static Reader log2In;
    static Reader log10In;


    @BeforeAll
    static void init() {
        secMock = Mockito.mock(Sec.class);
        cosMock = Mockito.mock(Cos.class);
        sinMock = Mockito.mock(Sin.class);
        lnMock = Mockito.mock(Ln.class);
        logMock = Mockito.mock(Log.class);
        try {
            secIn = new FileReader("src/main/resources/CsvFiles/Input/SecIn.csv");
            cosIn = new FileReader("src/main/resources/CsvFiles/Input/CosIn.csv");
            sinIn = new FileReader("src/main/resources/CsvFiles/Input/SinIn.csv");
            lnIn = new FileReader("src/main/resources/CsvFiles/Input/LnIn.csv");
            log2In = new FileReader("src/main/resources/CsvFiles/Input/Log2In.csv");
            log10In = new FileReader("src/main/resources/CsvFiles/Input/Log10In.csv");

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(secIn);
            for (CSVRecord record : records) {
                Mockito.when(secMock.sec(Double.parseDouble(record.get(0)), functionEps)).thenReturn(Double.valueOf(record.get(1)));
            }
            records = CSVFormat.DEFAULT.parse(cosIn);
            for (CSVRecord record : records) {
                Mockito.when(cosMock.cos(Double.parseDouble(record.get(0)), functionEps)).thenReturn(Double.valueOf(record.get(1)));
            }
            records = CSVFormat.DEFAULT.parse(sinIn);
            for (CSVRecord record : records) {
                Mockito.when(sinMock.sin(Double.parseDouble(record.get(0)), functionEps)).thenReturn(Double.valueOf(record.get(1)));
            }
            records = CSVFormat.DEFAULT.parse(lnIn);
            for (CSVRecord record : records) {
                Mockito.when(lnMock.ln(Double.parseDouble(record.get(0)), functionEps)).thenReturn(Double.valueOf(record.get(1)));
            }
            records = CSVFormat.DEFAULT.parse(log2In);
            for (CSVRecord record : records) {
                Mockito.when(logMock.log(2, Double.parseDouble(record.get(0)), functionEps)).thenReturn(Double.valueOf(record.get(1)));
            }
            records = CSVFormat.DEFAULT.parse(log10In);
            for (CSVRecord record : records) {
                Mockito.when(logMock.log(10, Double.parseDouble(record.get(0)), functionEps)).thenReturn(Double.valueOf(record.get(1)));
            }
        } catch (IOException ex) {
            System.err.println("Ты как в тесте IOE поймал?!");
        }

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Input/SystemIn.csv")
    void testSystemWithMocks(double value, double expected) {
        SystemFunction function = new SystemFunction(secMock, lnMock, logMock);
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Input/SystemIn.csv")
    void testWithSec(double value, double expected) {
        SystemFunction function = new SystemFunction(new Sec(cosMock), lnMock, logMock);
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Input/SystemIn.csv")
    void testWithCos(double value, double expected) {
        SystemFunction function = new SystemFunction(new Sec(new Cos(sinMock)), lnMock, logMock);
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Input/SystemIn.csv")
    void testWithSin(double value, double expected) {
        SystemFunction function = new SystemFunction(new Sec(new Cos(new Sin())), lnMock, logMock);
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Input/SystemIn.csv")
    void testWithLog(double value, double expected) {
        SystemFunction function = new SystemFunction(secMock, lnMock, new Log(lnMock));
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Input/SystemIn.csv")
    void testWithLn(double value, double expected) {
        SystemFunction function = new SystemFunction(secMock, new Ln(), new Log());
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps*20);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Input/SystemIn.csv")
    void testWithSinAndLn(double value, double expected) {
        SystemFunction function = new SystemFunction();
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps*20);
    }
}