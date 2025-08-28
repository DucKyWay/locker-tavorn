package ku.cs.services.datasources;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import ku.cs.models.account.Account;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AdminFileDatasource implements Datasource<Account> {

    private String directoryName;
    private String fileName;

    public AdminFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdir();
        }
        String filePath = directoryName + File.separator +fileName;
        file = new File(filePath);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Account readData() {
        File file = new File(directoryName, fileName);
        if (!file.exists() || file.length() == 0) {
            return null;
        }

        try (Jsonb jsonb = JsonbBuilder.create();
             Reader reader = new BufferedReader(
                     new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
             )) {

            return jsonb.fromJson(reader, Account.class);

        } catch (IOException e) {
            throw new RuntimeException("Error reading admin data", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeData(Account data) {
        File file = new File(directoryName, fileName);

        try (Jsonb jsonb = JsonbBuilder.create();
             Writer writer = new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8)
             )) {

            jsonb.toJson(data, writer);

        } catch (Exception e) {
            throw new RuntimeException("Error writing admin data", e);
        }
    }
}
