package ku.cs.services.datasources;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

import java.io.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class JsonListFileDatasource<T,L> implements Datasource<L> {
    private String directoryName;
    private String fileName;

    private final Supplier<L> listSupplier;
    private final Function<L, List<T>> getter;
    private final BiConsumer<L,T> adder;
    private final Class<T> elementClass;

    public JsonListFileDatasource(String directoryName, String fileName,
                                  Supplier<L> listSupplier,
                                  Function<L, List<T>> getter,
                                  BiConsumer<L, T> adder,
                                  Class<T> elementClass) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        this.listSupplier = listSupplier;
        this.getter = getter;
        this.adder = adder;
        this.elementClass = elementClass;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(directoryName, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public L readData(){
        L wrapper = listSupplier.get();
        File file = new File(directoryName, fileName);

        if (!file.exists() || file.length() == 0) {
            return wrapper;
        }

        Jsonb jsonb = JsonbBuilder.create();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }

            T[] array = (T[]) jsonb.fromJson(sb.toString(),
                    java.lang.reflect.Array.newInstance(elementClass, 0).getClass());

            for (T e : array) {
                adder.accept(wrapper, e);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return wrapper;
    }

    @Override
    public void writeData(L data) {
        File file = new File(directoryName, fileName);

        JsonbConfig config = new JsonbConfig()
                .withFormatting(true)
                .withNullValues(true);

        try (Jsonb jsonb = JsonbBuilder.create(config);
             PrintWriter out = new PrintWriter(new FileWriter(file))) {
            String result = jsonb.toJson(getter.apply(data));
            out.print(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
