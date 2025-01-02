import org.json.JSONObject;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class ShamirSecretSharingProblem {
    public static void main(String[] args) {
        try {
            String filePath = "testcase.json"; //path for json file
            FileReader reader = new FileReader(filePath);
            StringBuilder jsonContent = new StringBuilder();
            int character;
            while ((character = reader.read()) != -1) {
                jsonContent.append((char) character);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(jsonContent.toString());
            int k = jsonObject.getJSONObject("keys").getInt("k");
            JSONObject roots = jsonObject.getJSONObject("keys");

            Map<Integer, Integer> points = new HashMap<>();
            for (String key : roots.keySet()) {
                if (key.equals("k") || key.equals("base") || key.equals("value")) continue;
                JSONObject root = roots.getJSONObject(key);
                int x = Integer.parseInt(key);
                int base = root.getInt("base");
                String encodedValue = root.getString("value");
                int y = new BigInteger(encodedValue, base).intValue();
                points.put(x, y);
            }
            int secret = calculateConstantTerm(points, k);
            System.out.println("The secret (constant term c) is: " + secret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static int calculateConstantTerm(Map<Integer, Integer> points, int k) {
        int result = 0;
        for (Map.Entry<Integer, Integer> p : points.entrySet()) {
            int x_i = p.getKey();
            int y_i = p.getValue();

            double term = y_i;
            for (Map.Entry<Integer, Integer> q : points.entrySet()) {
                int x_j = q.getKey();
                if (x_i != x_j) {
                    term *= (0.0 - x_j) / (x_i - x_j);
                }
            }
            result += term;
        }
        return (int) Math.round(result);
    }
}
