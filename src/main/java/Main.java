import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class Main {
    public static void main(String[] args) {
        SparkConf config = new SparkConf()
                .setAppName("RDD_VentesAnalysis")
                .setMaster("local[*]");
        
        JavaSparkContext sparkContext = new JavaSparkContext(config);
        
        try {
            String inputFile = args.length > 0 ? args[0] : "/app/ventes.txt";
            JavaRDD<String> salesData = sparkContext.textFile(inputFile);
            
            System.out.println("=== ANALYSE DES VENTES ===");
            System.out.println("Nombre total de ventes: " + salesData.count());
            System.out.println();
            
            analyzeSalesByCity(salesData);
            System.out.println();
            analyzeSalesByCityAndYear(salesData);
            
        } finally {
            sparkContext.close();
        }
    }
    
    private static void analyzeSalesByCity(JavaRDD<String> salesData) {
        System.out.println("--- TOTAL DES VENTES PAR VILLE ---");
        
        JavaPairRDD<String, Integer> salesByCity = salesData
                .map(line -> line.split(" "))
                .filter(fields -> fields.length >= 4)
                .mapToPair(fields -> new Tuple2<>(fields[1], Integer.parseInt(fields[3])))
                .reduceByKey(Integer::sum)
                .sortByKey();
        
        salesByCity.foreach(entry -> 
            System.out.printf("Ville: %-12s | Total: %,d DH%n", entry._1, entry._2)
        );
    }
    
    private static void analyzeSalesByCityAndYear(JavaRDD<String> salesData) {
        System.out.println("--- TOTAL DES VENTES PAR VILLE ET ANNEE ---");
        
        JavaPairRDD<String, Integer> salesByCityYear = salesData
                .map(line -> line.split(" "))
                .filter(fields -> fields.length >= 4)
                .mapToPair(fields -> {
                    String city = fields[1];
                    String year = fields[0].split("/")[2];
                    int price = Integer.parseInt(fields[3]);
                    return new Tuple2<>(city + "-" + year, price);
                })
                .reduceByKey(Integer::sum)
                .sortByKey();
        
        salesByCityYear.foreach(entry -> {
            String[] parts = entry._1.split("-");
            System.out.printf("Ville: %-12s | Annee: %s | Total: %,d DH%n", 
                    parts[0], parts[1], entry._2);
        });
    }
}