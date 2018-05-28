package Product4.Hibernate;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JSONProcessor {
    private final String targetFilePath;

    JSONProcessor(String targetFilePath) {
        this.targetFilePath = targetFilePath;
    }

    public List<Car> parseFile() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(new FileReader(targetFilePath));
        JSONArray cars = (JSONArray) json.get("cars");

        List<JSONObject> carList = (List<JSONObject>) cars.stream().collect(Collectors.toList());

        return carList.stream()
            .map(x -> new Car((String) x.get("make"), (String) x.get("model"), (String) x.get("colour"), (Double) x.get("engine_size")))
            .collect(Collectors.toList());
    }
}
